package com.vlad_iglin.google_leads_transmitter.adapter.google_ads.out.query;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
enum LeadCollection {

    LEAD_COLLECTION("local_services_lead"),
    LEAD_CONVERSATION_COLLECTION("local_services_lead_conversation");

    private final String collection;

}
