package com.vlad_iglin.google_leads_transmitter.adapter.http.out.dto;

public record ResponseBody(int code, String body, String message) {

    public boolean isSuccessful() {
        return code >= 200 && code < 300;
    }
}
