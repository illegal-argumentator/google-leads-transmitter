package com.vlad_iglin.google_leads_transmitter.domain.lead;

import java.util.List;

public record Lead(
        String fullName,
        String email,
        String phoneNumber,
        String referralSource,
        List<String> notes,
        String resourceName,
        String creationDateTime
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
}
