package com.vlad_iglin.google_leads_transmitter.adapter.lead.out.props;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "lead")
public class LeadProps {

    String name;
    String referralSource;
    int leadType;
    List<Account> accounts;

    public record Account(
            String location,
            String branchId,
            String customerId
    ) {}

    public Account getByBranchId(String branchId) {
        return accounts.stream()
                .filter(account -> account.branchId.equals(branchId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Account not found by branch id: %s.".formatted(branchId)));
    }
}