package com.vlad_iglin.google_leads_transmitter.adapter.google_ads.out.props;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "google")
public class GoogleProps {

    private String developerToken;

    private String clientId;
    private List<String> customerIds;

    private String clientSecret;
    private String refreshToken;

    private String referralSource;

    private Account account;

    public record Account(String timezone) {}
}
