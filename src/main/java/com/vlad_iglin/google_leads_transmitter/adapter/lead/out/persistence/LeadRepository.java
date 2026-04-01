package com.vlad_iglin.google_leads_transmitter.adapter.lead.out.persistence;

import com.vlad_iglin.google_leads_transmitter.adapter.lead.out.model.MongoLead;
import org.springframework.data.mongodb.repository.MongoRepository;

interface LeadRepository extends MongoRepository<MongoLead, String> {
}
