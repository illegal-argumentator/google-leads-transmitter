package com.vlad_iglin.google_leads_transmitter.adapter.lead.out.persistence;

import com.vlad_iglin.google_leads_transmitter.domain.lead.Lead;
import com.vlad_iglin.google_leads_transmitter.port.LeadCommandPort;
import org.springframework.stereotype.Service;

@Service
class LeadCommandAdapter implements LeadCommandPort {

    @Override
    public void save(Lead lead) {
    }

}
