package com.vlad_iglin.google_leads_transmitter.port;

import com.vlad_iglin.google_leads_transmitter.domain.lead.Lead;

public interface LeadCommandPort {

    boolean save(Lead lead);

}
