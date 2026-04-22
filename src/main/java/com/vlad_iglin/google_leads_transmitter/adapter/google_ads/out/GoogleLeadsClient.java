package com.vlad_iglin.google_leads_transmitter.adapter.google_ads.out;

import com.google.ads.googleads.v23.resources.LocalServicesLead;
import com.google.ads.googleads.v23.resources.LocalServicesLeadConversation;
import com.google.ads.googleads.v23.services.GoogleAdsRow;
import com.google.ads.googleads.v23.services.GoogleAdsServiceClient;
import com.google.ads.googleads.v23.services.SearchGoogleAdsRequest;
import com.vlad_iglin.google_leads_transmitter.adapter.google_ads.out.exception.GoogleAdsApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
class GoogleLeadsClient {

    private final GoogleAdsServiceClient serviceClient;

    public List<GoogleAdsRow> searchAds(String query, String customerId) {
        List<GoogleAdsRow> rows = new ArrayList<>();
        SearchGoogleAdsRequest request = buildRequest(query, customerId);

        try {
            GoogleAdsServiceClient.SearchPagedResponse response = serviceClient.search(request);
            response.iterateAll().forEach(rows::add);
        } catch (Exception e) {
            throw new GoogleAdsApiException(e.getMessage());
        }

        return  rows;
    }

    public List<LocalServicesLead> searchLeads(String query, String customerId) {
        List<GoogleAdsRow> rows = searchAds(query, customerId);
        return rows.stream()
                .filter(GoogleAdsRow::hasLocalServicesLead)
                .map(GoogleAdsRow::getLocalServicesLead)
                .toList();
    }

    public List<LocalServicesLeadConversation> searchConversations(String query, String customerId) {
        List<GoogleAdsRow> rows = searchAds(query, customerId);
        return rows.stream()
                .filter(GoogleAdsRow::hasLocalServicesLeadConversation)
                .map(GoogleAdsRow::getLocalServicesLeadConversation)
                .toList();
    }

    private SearchGoogleAdsRequest buildRequest(String query, String customerId) {
        return SearchGoogleAdsRequest.newBuilder()
                .setCustomerId(customerId)
                .setQuery(query)
                .build();
    }
}