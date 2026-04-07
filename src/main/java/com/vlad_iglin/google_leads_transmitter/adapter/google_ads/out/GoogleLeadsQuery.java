package com.vlad_iglin.google_leads_transmitter.adapter.google_ads.out;

import com.google.ads.googleads.v23.services.GoogleAdsRow;
import com.vlad_iglin.google_leads_transmitter.adapter.google_ads.out.exception.NoLeadsException;
import com.vlad_iglin.google_leads_transmitter.adapter.google_ads.out.mapper.GoogleLeadsMapper;
import com.vlad_iglin.google_leads_transmitter.adapter.google_ads.out.props.GoogleProps;
import com.vlad_iglin.google_leads_transmitter.adapter.google_ads.out.query.GoogleAdsQueryBuilder;
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

    private final GoogleProps props;
    private final GoogleLeadsClient client;
    private final GoogleLeadsMapper mapper;

    @Value("${data-sync.interval}")
    private long interval;

    @Override
    public List<Lead> getLatestLeads() {
        return getMappedLeads();
    }

    private List<Lead> getMappedLeads() {
        ZonedDateTime time = ZonedDateTime.now(ZoneId.of(props.getAccount().timezone())).minus(interval, ChronoUnit.MILLIS);

        List<GoogleAdsRow> rows = searchAdsConcurrently(time);
        if (rows.isEmpty()) throw new NoLeadsException("Not found new leads for last %s minutes.".formatted(interval / 1000 / 5));

        log.info("Retrieved {} rows from Google Ads.", rows.size());
        return rows.stream().filter(GoogleAdsRow::hasLocalServicesLead).map(row -> mapper.toLead(row, props.getReferralSource())).toList();
    }

    private List<GoogleAdsRow> searchAdsConcurrently(ZonedDateTime time) {
        List<CompletableFuture<List<GoogleAdsRow>>> futures = new ArrayList<>();

        for (String customerId : props.getCustomerIds()) {
            String query = GoogleAdsQueryBuilder.leadsSearchByCreationDateFrom(time);
            CompletableFuture<List<GoogleAdsRow>> future = CompletableFuture.supplyAsync(() -> client.searchAds(query, customerId));
            futures.add(future);
        }

        return futures.stream()
                .flatMap(row -> row.exceptionally(throwable -> Collections.emptyList()).join().stream())
                .toList();
    }
}
