package com.vlad_iglin.google_leads_transmitter.application;

import com.vlad_iglin.google_leads_transmitter.domain.lead.Lead;
import com.vlad_iglin.google_leads_transmitter.port.GoogleLeadsPort;
import com.vlad_iglin.google_leads_transmitter.port.LeadCommandPort;
import com.vlad_iglin.google_leads_transmitter.port.MovingLeadCommandPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GoogleLeadsTransmissionService {

    private final GoogleLeadsPort googleLeadsPort;
    private final LeadCommandPort leadCommandPort;
    private final MovingLeadCommandPort movingLeadCommandPort;

    public void processTransmission() {
        List<Lead> leads = googleLeadsPort.getLatestLeads();
        leads.forEach(this::saveLead);
    }

    private void saveLead(Lead lead) {
        leadCommandPort.save(lead);
        movingLeadCommandPort.save(lead);
    }
}
