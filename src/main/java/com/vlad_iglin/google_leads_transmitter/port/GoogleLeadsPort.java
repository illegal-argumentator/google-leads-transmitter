package com.vlad_iglin.google_leads_transmitter.port;

import com.vlad_iglin.google_leads_transmitter.domain.lead.Lead;

import java.util.List;

public interface GoogleLeadsPort {

    List<Lead> getLatestLeads();

}
