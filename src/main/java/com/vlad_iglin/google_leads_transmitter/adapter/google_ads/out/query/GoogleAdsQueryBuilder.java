package com.vlad_iglin.google_leads_transmitter.adapter.google_ads.out.query;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static com.vlad_iglin.google_leads_transmitter.adapter.google_ads.out.query.LeadCollection.LEAD_COLLECTION;
import static com.vlad_iglin.google_leads_transmitter.adapter.google_ads.out.query.LeadCollection.LEAD_CONVERSATION_COLLECTION;
import static com.vlad_iglin.google_leads_transmitter.shared.Delimiters.COMMA_DELIMITER;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class GoogleAdsQueryBuilder {

    static String SELECT = "SELECT";
    static String FROM = "FROM";
    static String WHERE = "WHERE";

    static String SELECT_TEMPLATE_WHERE = SELECT + " %s " + FROM + " %s " + WHERE + " %s";
    static String GOOGLE_ADS_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static String leadsSearchByCreationDateFrom(ZonedDateTime from) {
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern(GOOGLE_ADS_TIME_FORMAT);
        String date = from.format(pattern);
        String where = LeadField.CREATION_DATE_TIME.withCollection() + " >= '" + date + "'";
        return SELECT_TEMPLATE_WHERE.formatted(String.join(COMMA_DELIMITER.getDelimiter(), LeadField.allWithCollection()), LEAD_COLLECTION.getCollection(), where);
    }

    public static String leadConversationsSearchByResource(String resource) {
        String where = LeadConversationField.LEAD.withCollection() + " = '" + resource + "'";
        return SELECT_TEMPLATE_WHERE.formatted(String.join(COMMA_DELIMITER.getDelimiter(), LeadConversationField.allWithCollection()), LEAD_CONVERSATION_COLLECTION.getCollection(), where);
    }
}