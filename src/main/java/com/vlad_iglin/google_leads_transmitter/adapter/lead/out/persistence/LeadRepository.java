package com.vlad_iglin.google_leads_transmitter.adapter.lead.out.persistence;

import com.vlad_iglin.google_leads_transmitter.adapter.lead.out.model.MongoLead;
import org.springframework.data.mongodb.repository.MongoRepository;

interface LeadRepository extends MongoRepository<MongoLead, String> {
    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);

    void deleteByEmail(String email);

    void deleteByPhoneNumber(String phoneNumber);
}
