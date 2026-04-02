package com.vlad_iglin.google_leads_transmitter.adapter.google_ads.out.props;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "google")
public class GoogleProps {

    private String developerToken;

    private String clientId;
    private String customerId;

    private String clientSecret;
    private String refreshToken;

    private String referralSource;

    private Account account;

    public record Account(String timezone) {}
}
