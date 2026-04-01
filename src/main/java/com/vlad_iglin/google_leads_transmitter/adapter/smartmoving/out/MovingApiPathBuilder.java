package com.vlad_iglin.google_leads_transmitter.adapter.smartmoving.out;

final class MovingApiPathBuilder {

    private static final String BASE_URL = "https://api.smartmoving.com/api";

    static String buildLeads(String providerKey) {
        return BASE_URL + "/leads/from-provider/v2?providerKey=" + providerKey;
    }

}
