package com.vlad_iglin.google_leads_transmitter.adapter.lead.out.mapper;

import com.vlad_iglin.google_leads_transmitter.adapter.lead.out.model.MongoLead;
import com.vlad_iglin.google_leads_transmitter.domain.lead.Lead;
import com.vlad_iglin.google_leads_transmitter.infrastructure.MapStructConfig;
import org.mapstruct.Mapper;

@Mapper(config = MapStructConfig.class)
public interface LeadMapper {

    MongoLead toEntity(Lead lead);

}
