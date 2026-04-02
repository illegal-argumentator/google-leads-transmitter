package com.vlad_iglin.google_leads_transmitter.domain.lead;

public record Lead(
        String fullName,
        String email,
        String phoneNumber,
        String referralSource
) {
}
