package com.vlad_iglin.google_leads_transmitter.adapter.lead.out.persistence;

import com.vlad_iglin.google_leads_transmitter.adapter.lead.out.mapper.LeadMapper;
import com.vlad_iglin.google_leads_transmitter.domain.lead.Lead;
import com.vlad_iglin.google_leads_transmitter.port.LeadCommandPort;
import com.vlad_iglin.google_leads_transmitter.shared.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
class LeadCommandAdapter implements LeadCommandPort {

    private final LeadRepository repository;
    private final LeadMapper mapper;

    @Override
    public boolean save(Lead lead) {
        try {
            if (existsByEmail(lead.email()) || existsByPhone(lead.phoneNumber())) return false;
            repository.save(mapper.toEntity(lead));
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    @Override
    public void delete(Lead lead) {
        if (!StringUtils.isBlank(lead.email())) {
            repository.deleteByEmail(lead.email());
        } else if (!StringUtils.isBlank(lead.phoneNumber())) {
            repository.deleteByPhoneNumber(lead.phoneNumber());
        }
    }

    private boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }

    private boolean existsByPhone(String phone) {
        return repository.existsByPhoneNumber(phone);
    }

}
