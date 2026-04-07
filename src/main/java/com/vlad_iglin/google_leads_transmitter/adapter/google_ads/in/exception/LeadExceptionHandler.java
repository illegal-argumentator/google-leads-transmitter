package com.vlad_iglin.google_leads_transmitter.adapter.google_ads.in.exception;

import com.vlad_iglin.google_leads_transmitter.adapter.google_ads.out.exception.GoogleAdsApiException;
import com.vlad_iglin.google_leads_transmitter.adapter.google_ads.out.exception.NoLeadsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class LeadExceptionHandler {

    @ExceptionHandler(NoLeadsException.class)
    public void handleNoLeadsException(NoLeadsException e) {
        log.info(e.getMessage());
    }

    @ExceptionHandler(GoogleAdsApiException.class)
    public void handleGoogleAdsApiException(GoogleAdsApiException e) {
        log.error(e.getMessage());
    }

}
