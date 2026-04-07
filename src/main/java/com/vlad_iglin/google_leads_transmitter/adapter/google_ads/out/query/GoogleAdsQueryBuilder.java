package com.vlad_iglin.google_leads_transmitter.adapter.google_ads.out.query;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class GoogleAdsQueryBuilder {
    static String GOOGLE_ADS_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    static String LEAD_COLLECTION = "local_services_lead";

    static String LEAD_ID = LEAD_COLLECTION + ".id";
    static String LEAD_TYPE = LEAD_COLLECTION + ".lead_type";
    static String CONTACT_DETAILS = LEAD_COLLECTION + ".contact_details";
    static String LEAD_STATUS = LEAD_COLLECTION + ".lead_status";
    static String CREATION_DATE_TIME = LEAD_COLLECTION + ".creation_date_time";

    static List<String> LEAD_FIELDS = List.of(LEAD_ID, LEAD_TYPE, CONTACT_DETAILS, LEAD_STATUS, CREATION_DATE_TIME);

    static String SELECT = "SELECT";
    static String FROM = "FROM";
    static String WHERE = "WHERE";
    static String COMMA_DELIMITER = ", ";

    static String SELECT_TEMPLATE_WHERE = SELECT + " %s " + FROM + " %s " + WHERE + " %s";
    static String SELECT_TEMPLATE = SELECT + " %s " + FROM + " %s";

    public static String leadsSearchByCreationDateFrom(ZonedDateTime from) {
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern(GOOGLE_ADS_TIME_FORMAT);
        String date = from.format(pattern);
        String where = CREATION_DATE_TIME + " >= '" + date + "'";

        if (from.toEpochSecond() == 0) return SELECT_TEMPLATE.formatted(String.join(COMMA_DELIMITER, LEAD_FIELDS), LEAD_COLLECTION);
        return SELECT_TEMPLATE_WHERE.formatted(String.join(COMMA_DELIMITER, LEAD_FIELDS), LEAD_COLLECTION, where);
    }
}