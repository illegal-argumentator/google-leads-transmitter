package com.vlad_iglin.google_leads_transmitter.adapter.google_ads.out;

import com.google.ads.googleads.v23.services.GoogleAdsRow;
import com.google.ads.googleads.v23.services.GoogleAdsServiceClient;
import com.google.ads.googleads.v23.services.SearchGoogleAdsRequest;
import com.vlad_iglin.google_leads_transmitter.adapter.google_ads.out.exception.GoogleAdsApiException;
import com.vlad_iglin.google_leads_transmitter.adapter.google_ads.out.props.GoogleProps;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
class GoogleLeadsClient {

    private final GoogleProps props;
    private final GoogleAdsServiceClient serviceClient;

    public List<GoogleAdsRow> searchAds(String query) {
        List<GoogleAdsRow> rows = new ArrayList<>();
        SearchGoogleAdsRequest request = SearchGoogleAdsRequest.newBuilder()
                .setCustomerId(props.getCustomerId())
                .setQuery(query)
                .build();

        try {
            GoogleAdsServiceClient.SearchPagedResponse response = serviceClient.search(request);
            response.iterateAll().forEach(rows::add);
        } catch (Exception e) {
            throw new GoogleAdsApiException(e.getMessage());
        }

        return rows;
    }

}