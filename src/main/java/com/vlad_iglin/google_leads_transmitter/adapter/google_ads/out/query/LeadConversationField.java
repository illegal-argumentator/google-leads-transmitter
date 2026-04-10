package com.vlad_iglin.google_leads_transmitter.adapter.google_ads.out.query;

import com.vlad_iglin.google_leads_transmitter.shared.Delimiters;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
enum LeadConversationField {

    LEAD("lead"),
    MESSAGE_DETAILS_TEXT("message_details.text");

    private final String field;

    static String[] allWithCollection() {
        return Arrays.stream(values())
                .map(value -> LeadCollection.LEAD_CONVERSATION_COLLECTION.getCollection() + Delimiters.DOT_DELIMITER.getDelimiter() + value.getField())
                .toArray(String[]::new);
    }

    String withCollection() {
        return LeadCollection.LEAD_CONVERSATION_COLLECTION.getCollection() + Delimiters.DOT_DELIMITER.getDelimiter() + this.getField();
    }
}
