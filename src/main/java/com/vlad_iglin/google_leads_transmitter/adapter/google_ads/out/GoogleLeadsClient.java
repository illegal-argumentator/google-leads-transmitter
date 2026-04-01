package com.vlad_iglin.google_leads_transmitter.adapter.google_ads.out;

import com.vlad_iglin.google_leads_transmitter.domain.lead.Lead;
import com.vlad_iglin.google_leads_transmitter.port.GoogleLeadsPort;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
class GoogleLeadsClient implements GoogleLeadsPort {

    @Override
    public List<Lead> getLatestLeads() {
        return List.of();
    }

}
