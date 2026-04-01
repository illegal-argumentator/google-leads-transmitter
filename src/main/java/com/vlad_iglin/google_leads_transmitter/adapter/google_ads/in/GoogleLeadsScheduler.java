package com.vlad_iglin.google_leads_transmitter.adapter.google_ads.in;

import com.vlad_iglin.google_leads_transmitter.application.GoogleLeadsTransmissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
class GoogleLeadsScheduler {

    private final GoogleLeadsTransmissionService transmissionService;

    @Scheduled(fixedDelay =  5000 * 60 * 60, initialDelay = 1000 * 60 * 60)
    private void processLeadsTransmission() {
        transmissionService.processTransmission();
    }

}
