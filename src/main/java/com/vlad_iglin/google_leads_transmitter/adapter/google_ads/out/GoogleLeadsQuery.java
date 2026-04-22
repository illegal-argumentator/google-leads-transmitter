package com.vlad_iglin.google_leads_transmitter.adapter.google_ads.out;

import com.google.ads.googleads.v23.resources.LocalServicesLead;
import com.google.ads.googleads.v23.resources.LocalServicesLeadConversation;
import com.vlad_iglin.google_leads_transmitter.adapter.google_ads.out.exception.NoLeadsException;
import com.vlad_iglin.google_leads_transmitter.adapter.google_ads.out.mapper.GoogleLeadsMapper;
import com.vlad_iglin.google_leads_transmitter.adapter.google_ads.out.props.GoogleProps;
import com.vlad_iglin.google_leads_transmitter.adapter.google_ads.out.query.GoogleAdsQueryBuilder;
import com.vlad_iglin.google_leads_transmitter.domain.lead.Lead;
import com.vlad_iglin.google_leads_transmitter.port.GoogleLeadsPort;
import com.vlad_iglin.google_leads_transmitter.shared.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class GoogleLeadsQuery implements GoogleLeadsPort {

    private final GoogleProps props;
    private final GoogleLeadsClient client;
    private final GoogleLeadsMapper mapper;
    private final ExecutorService executorService;

    @Value("${lead.default.name}")
    private String DEFAULT_LEAD_NAME;

    @Value("${data-sync.interval}")
    private long interval;

    @Override
    public List<Lead> getLatestLeads() {
        List<Lead> leads = getMappedLeads();
        withConversations(leads);
        return leads;
    }

    private List<Lead> getMappedLeads() {
        ZonedDateTime time = ZonedDateTime.now(ZoneId.of(props.getAccount().timezone())).minus(interval, ChronoUnit.MILLIS);

        List<LocalServicesLead> rows = searchAdsConcurrently(time);
        if (rows.isEmpty())
            throw new NoLeadsException("Not found new leads for last %s minutes.".formatted(interval / 1000 / 60));

        log.info("Retrieved {} rows from Google Ads.", rows.size());
        return rows.stream()
                .filter(LocalServicesLead::hasContactDetails)
                .map(row -> mapper.toLead(row, props.getReferralSource(), DEFAULT_LEAD_NAME))
                .toList();
    }

    private List<LocalServicesLead> searchAdsConcurrently(ZonedDateTime time) {
        List<CompletableFuture<List<LocalServicesLead>>> futures = new ArrayList<>();
        String query = GoogleAdsQueryBuilder.leadsSearchByCreationDateFrom(time);

        for (String customerId : props.getCustomerIds()) {
            CompletableFuture<List<LocalServicesLead>> future = CompletableFuture.supplyAsync(() ->  client.searchLeads(query, customerId), executorService);
            futures.add(future);
        }

        return futures.stream()
                .flatMap(row -> row.exceptionally(throwable -> Collections.emptyList()).join().stream())
                .toList();
    }

    private void withConversations(List<Lead> leads) {
        List<CompletableFuture<Void>> futures = new ArrayList<>();

        for (Lead lead : leads) {
            String query = GoogleAdsQueryBuilder.leadConversationsSearchByResource(lead.resourceName());
            CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> client.searchConversations(query, Lead.getCustomerId(lead.resourceName())), executorService)
                    .exceptionally(throwable -> Collections.emptyList())
                    .thenAccept(conversations -> handleConversation(lead, conversations));
            futures.add(future);
        }

        futures.forEach(CompletableFuture::join);
    }

    private void handleConversation(Lead lead, List<LocalServicesLeadConversation> conversations) {
        if (conversations.isEmpty()) return;

        String messages = conversations.stream()
                .filter(LocalServicesLeadConversation::hasMessageDetails)
                .map(conversation -> " - " + conversation.getMessageDetails().getText())
                .collect(Collectors.joining("\n"));

        if (StringUtils.isBlank(messages)) return;

        lead.notes().add("Conversations: " + messages);
    }
}
