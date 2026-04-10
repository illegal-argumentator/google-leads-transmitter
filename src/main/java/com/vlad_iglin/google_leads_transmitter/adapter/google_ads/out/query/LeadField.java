package com.vlad_iglin.google_leads_transmitter.adapter.google_ads.out.query;

import com.vlad_iglin.google_leads_transmitter.shared.Delimiters;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
enum LeadField {

    ID("id"),
    LEAD_TYPE("lead_type"),
    CONTACT_DETAILS("contact_details"),
    LEAD_STATUS("lead_status"),
    CREATION_DATE_TIME("creation_date_time"),
    NOTE_CONVERSATION("note.description");

    private final String field;

    static String[] allWithCollection() {
        return Arrays.stream(values())
                .map(value -> LeadCollection.LEAD_COLLECTION.getCollection() + Delimiters.DOT_DELIMITER.getDelimiter() + value.getField())
                .toArray(String[]::new);
    }

    String withCollection() {
        return LeadCollection.LEAD_COLLECTION.getCollection() + Delimiters.DOT_DELIMITER.getDelimiter() + this.getField();
    }

}
