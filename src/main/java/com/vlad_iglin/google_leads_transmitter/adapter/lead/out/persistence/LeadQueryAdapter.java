package com.vlad_iglin.google_leads_transmitter.adapter.lead.out.persistence;

import com.vlad_iglin.google_leads_transmitter.adapter.lead.out.mapper.LeadMapper;
import com.vlad_iglin.google_leads_transmitter.domain.lead.Lead;
import com.vlad_iglin.google_leads_transmitter.port.LeadQueryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class LeadQueryAdapter implements LeadQueryPort {

    private final LeadRepository repository;

    private final LeadMapper mapper;

    @Override
    public List<Lead> getAll() {
        return repository.findAll().stream().map(mapper::toLead).toList();
    }

}
