package com.vlad_iglin.google_leads_transmitter.adapter.google_ads.out;

import com.google.ads.googleads.v23.resources.LocalServicesLead;
import com.vlad_iglin.google_leads_transmitter.adapter.google_ads.out.exception.NoLeadsException;
import com.vlad_iglin.google_leads_transmitter.adapter.google_ads.out.mapper.GoogleLeadsMapper;
import com.vlad_iglin.google_leads_transmitter.adapter.google_ads.out.props.GoogleProps;
import com.vlad_iglin.google_leads_transmitter.adapter.google_ads.out.query.GoogleAdsQueryBuilder;
import com.vlad_iglin.google_leads_transmitter.adapter.lead.out.props.LeadProps;
import com.vlad_iglin.google_leads_transmitter.domain.lead.Lead;
import com.vlad_iglin.google_leads_transmitter.port.GoogleLeadsPort;
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

@Slf4j
@Component
@RequiredArgsConstructor
public class GoogleLeadsQuery implements GoogleLeadsPort {

    private final LeadProps leadProps;
    private final GoogleProps googleProps;

    private final GoogleLeadsClient client;
    private final GoogleLeadsMapper mapper;

    @Value("${data-sync.interval}")
    private long interval;

    @Override
    public List<Lead> getLatestLeads() {
        return getMappedLeads();
    }

    private List<Lead> getMappedLeads() {
        ZonedDateTime time = ZonedDateTime.now(ZoneId.of(googleProps.getAccount().timezone())).minus(interval, ChronoUnit.MILLIS);

        List<LocalServicesLead> rows = searchAdsConcurrently(time);
        if (rows.isEmpty())
            throw new NoLeadsException("Not found new leads for last %s minutes.".formatted(interval / 1000 / 60));

        log.info("Retrieved {} rows from Google Ads.", rows.size());
        return rows.stream()
                .filter(LocalServicesLead::hasContactDetails)
                .map(row -> mapper.toLead(row, leadProps))
                .toList();
    }

    private List<LocalServicesLead> searchAdsConcurrently(ZonedDateTime time) {
        List<CompletableFuture<List<LocalServicesLead>>> futures = new ArrayList<>();
        String query = GoogleAdsQueryBuilder.leadsSearchByCreationDateFrom(time);

        for (LeadProps.Account account : leadProps.getAccounts()) {
            CompletableFuture<List<LocalServicesLead>> future = CompletableFuture.supplyAsync(() -> client.searchLeads(query, account.customerId()));
            futures.add(future);
        }

        return futures.stream()
                .flatMap(row -> row.exceptionally(throwable -> Collections.emptyList()).join().stream())
                .toList();
    }
}
