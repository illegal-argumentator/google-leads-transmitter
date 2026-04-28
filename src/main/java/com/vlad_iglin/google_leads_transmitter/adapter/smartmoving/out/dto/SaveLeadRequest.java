package com.vlad_iglin.google_leads_transmitter.adapter.smartmoving.out.dto;

public record SaveLeadRequest(
        String fullName,
        String email,
        String phoneNumber,
        String referralSource,
        String notes,
        String resourceName,
        String creationDateTime
) {
}
