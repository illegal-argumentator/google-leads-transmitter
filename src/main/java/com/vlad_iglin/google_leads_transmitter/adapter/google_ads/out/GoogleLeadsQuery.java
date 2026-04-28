package com.vlad_iglin.google_leads_transmitter.adapter.google_ads.out;

import com.google.ads.googleads.v23.enums.LocalServicesLeadTypeEnum;
import com.google.ads.googleads.v23.resources.LocalServicesLead;
import com.google.ads.googleads.v23.resources.LocalServicesLeadConversation;
import com.vlad_iglin.google_leads_transmitter.adapter.google_ads.out.exception.NoLeadsException;
import com.vlad_iglin.google_leads_transmitter.adapter.google_ads.out.mapper.GoogleLeadsMapper;
import com.vlad_iglin.google_leads_transmitter.adapter.google_ads.out.props.GoogleProps;
import com.vlad_iglin.google_leads_transmitter.adapter.google_ads.out.query.GoogleAdsQueryBuilder;
import com.vlad_iglin.google_leads_transmitter.adapter.lead.out.props.LeadProps;
import com.vlad_iglin.google_leads_transmitter.domain.lead.Lead;
import com.vlad_iglin.google_leads_transmitter.port.GoogleLeadsPort;
import com.vlad_iglin.google_leads_transmitter.port.LeadQueryPort;
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

    private final LeadProps leadProps;
    private final LeadQueryPort leadQueryPort;
    private final GoogleProps googleProps;

    private final GoogleLeadsClient client;
    private final GoogleLeadsMapper mapper;
    private final ExecutorService executorService;

    @Value("${data-sync.interval}")
    private long interval;

    @Override
    public List<Lead> getLatestLeads() {
        List<Lead> leads = getNotExistingLeads();
        withConversations(leads);
        return leads;
    }

    private List<Lead> getNotExistingLeads() {
        List<Lead> leads = leadQueryPort.getAll();
        List<CompletableFuture<List<Lead>>> futures = new ArrayList<>();

        for (LeadProps.Account account : leadProps.getAccounts()) {
            CompletableFuture<List<Lead>> future = CompletableFuture.supplyAsync(() -> getLeads(account.branchId()), executorService);
            futures.add(future);
        }

        return futures.stream()
                .flatMap(row -> row.exceptionally(throwable -> {
                    log.error("Error fetching leads: {}.", throwable.getMessage());
                    return Collections.emptyList();
                }).join().stream())
                .peek(lead -> System.out.println("Lead:" + lead))
                .filter(lead -> !lead.contains(leads))
                .toList();
    }

    private List<Lead> getLeads(String branchId) {
        ZonedDateTime time = ZonedDateTime.now(ZoneId.of(googleProps.getAccount().timezone())).minus(interval, ChronoUnit.MILLIS);
        LocalServicesLeadTypeEnum.LeadType leadType = LocalServicesLeadTypeEnum.LeadType.forNumber(leadProps.getLeadType());

        List<LocalServicesLead> rows = searchLeadsConcurrently(branchId, time);
        if (rows.isEmpty())
            throw new NoLeadsException("Not found new leads for last %s minutes.".formatted(interval / 1000 / 60));

        log.info("Retrieved {} rows from Google Ads.", rows.size());
        return rows.stream()
                .filter(LocalServicesLead::hasContactDetails)
                .filter(localServicesLead -> leadProps.getLeadType() == -1 || localServicesLead.getLeadType() == leadType)
                .map(row -> mapper.toLead(branchId, row))
                .toList();
    }

    private List<LocalServicesLead> searchLeadsConcurrently(String branchId, ZonedDateTime time) {
        String query = GoogleAdsQueryBuilder.leadsSearchByCreationDateFrom(time);
        LeadProps.Account account = leadProps.getByBranchId(branchId);
        return client.searchLeads(query, account.customerId());
    }

    private void withConversations(List<Lead> leads) {
        List<CompletableFuture<Void>> futures = new ArrayList<>();

        for (Lead lead : leads) {
            String query = GoogleAdsQueryBuilder.leadConversationsSearchByResource(lead.resourceName());
            CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> client.searchConversations(query, Lead.getCustomerId(lead.resourceName())), executorService)
                    .thenAccept(conversations -> handleConversation(lead, conversations))
                    .exceptionally(throwable -> {
                        log.error("Error in conversation pipeline", throwable);
                        return null;
                    });
            futures.add(future);
        }

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
    }

    private void handleConversation(Lead lead, List<LocalServicesLeadConversation> conversations) {
        if (conversations.isEmpty()) return;

        String messages = conversations.stream()
                .filter(LocalServicesLeadConversation::hasMessageDetails)
                .filter(conversation -> !conversation.getMessageDetails().getText().isBlank())
                .map(conversation -> " - " + conversation.getMessageDetails().getText())
                .collect(Collectors.joining("\n"));

        if (StringUtils.isBlank(messages)) return;

        lead.notes().add("Conversations: " + messages);
    }
}
