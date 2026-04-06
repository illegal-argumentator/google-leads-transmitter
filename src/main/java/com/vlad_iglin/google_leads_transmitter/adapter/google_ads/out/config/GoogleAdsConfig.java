package com.vlad_iglin.google_leads_transmitter.adapter.google_ads.out.config;

import com.google.ads.googleads.lib.GoogleAdsClient;
import com.google.ads.googleads.v23.services.GoogleAdsServiceClient;
import com.google.auth.oauth2.UserCredentials;
import com.vlad_iglin.google_leads_transmitter.adapter.google_ads.out.props.GoogleProps;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
class GoogleAdsConfig {

    private final GoogleProps props;

    @Bean
    public GoogleAdsClient googleAdsClient() {
        return GoogleAdsClient.newBuilder()
                .setDeveloperToken(props.getDeveloperToken())
                .setLoginCustomerId(Long.parseLong(props.getLoginCustomerId()))
                .setCredentials(UserCredentials.newBuilder()
                        .setClientId(props.getClientId())
                        .setClientSecret(props.getClientSecret())
                        .setRefreshToken(props.getRefreshToken())
                        .build())
                .build();
    }

    @Bean
    public GoogleAdsServiceClient googleAdsServiceClient() {
        return googleAdsClient().getVersion23().createGoogleAdsServiceClient();
    }

}
