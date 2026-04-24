package com.vlad_iglin.google_leads_transmitter.adapter.lead.out.persistence;

import com.vlad_iglin.google_leads_transmitter.domain.lead.Lead;
import com.vlad_iglin.google_leads_transmitter.port.LeadQueryPort;
import com.vlad_iglin.google_leads_transmitter.shared.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LeadQueryAdapter implements LeadQueryPort {

    private final LeadRepository repository;

    @Override
    public boolean exists(Lead lead) {
        return existsByEmail(lead.email()) || existsByPhone(lead.phoneNumber());
    }

    private boolean existsByEmail(String email) {
        if (StringUtils.isBlank(email)) return false;
        return repository.existsByEmail(email);
    }

    private boolean existsByPhone(String phone) {
        if (StringUtils.isBlank(phone)) return false;
        return repository.existsByPhoneNumber(phone);
    }

}
