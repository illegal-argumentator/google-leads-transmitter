package com.vlad_iglin.google_leads_transmitter.domain.lead;

import com.vlad_iglin.google_leads_transmitter.shared.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public record Lead(
        long leadId,
        String fullName,
        String email,
        String phoneNumber,
        String referralSource,
        List<String> notes,
        String resourceName,
        String creationDateTime,
        String originAddressFull,
        String branchId
) {

    public static String getCustomerId(String resourceName) {
        if (resourceName == null || resourceName.isBlank()) {
            throw new IllegalStateException("Resource name is empty.");
        }

        String[] split = resourceName.split("/");
        if (split.length < 2) {
            throw new IllegalArgumentException("Invalid resource name format.");
        }

        return split[1];
    }

    public boolean contains(List<Lead> leads) {
        Set<String> emails = leads.stream()
                .map(Lead::email)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Set<String> phones = leads.stream()
                .map(Lead::phoneNumber)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        return (!StringUtils.isBlank(email) && emails.contains(email))
                || (!StringUtils.isBlank(phoneNumber) && phones.contains(phoneNumber));
    }
}
