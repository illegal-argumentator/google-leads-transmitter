package com.vlad_iglin.google_leads_transmitter.application;

import com.vlad_iglin.google_leads_transmitter.domain.lead.Lead;
import com.vlad_iglin.google_leads_transmitter.port.GoogleLeadsPort;
import com.vlad_iglin.google_leads_transmitter.port.LeadCommandPort;
import com.vlad_iglin.google_leads_transmitter.port.MovingLeadCommandPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleLeadsTransmissionService {

    private final GoogleLeadsPort googleLeadsPort;
    private final LeadCommandPort leadCommandPort;
    private final MovingLeadCommandPort movingLeadCommandPort;

    public void processTransmission() {
        try {
            log.info("Retrieving latest leads from Google Ads.");
            List<Lead> leads = googleLeadsPort.getLatestLeads();
            log.info("Saving leads.");
            leads.forEach(this::saveLead);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private void saveLead(Lead lead) {
        boolean savedLocal = leadCommandPort.save(lead);
        if (!savedLocal) return;
        boolean savedCrm = movingLeadCommandPort.save(lead);
        if (!savedCrm) leadCommandPort.delete(lead);
    }
}
