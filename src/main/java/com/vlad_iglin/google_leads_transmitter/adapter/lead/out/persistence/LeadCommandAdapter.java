package com.vlad_iglin.google_leads_transmitter.adapter.lead.out.persistence;

import com.vlad_iglin.google_leads_transmitter.adapter.lead.out.mapper.LeadMapper;
import com.vlad_iglin.google_leads_transmitter.domain.lead.Lead;
import com.vlad_iglin.google_leads_transmitter.port.LeadCommandPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class LeadCommandAdapter implements LeadCommandPort {

    private final LeadRepository repository;
    private final LeadMapper mapper;

    @Override
    public boolean save(Lead lead) {
        if (existsByEmail(lead.email()) || existsByPhone(lead.phoneNumber())) return false;
        repository.save(mapper.toEntity(lead));
        return true;
    }

    private boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }

    private boolean existsByPhone(String phone) {
        return repository.existsByPhoneNumber(phone);
    }

}
