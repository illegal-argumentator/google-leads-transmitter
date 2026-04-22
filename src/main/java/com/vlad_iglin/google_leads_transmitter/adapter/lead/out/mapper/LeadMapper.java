package com.vlad_iglin.google_leads_transmitter.adapter.lead.out.mapper;

import com.vlad_iglin.google_leads_transmitter.adapter.lead.out.model.MongoLead;
import com.vlad_iglin.google_leads_transmitter.adapter.smartmoving.out.dto.SaveLeadRequest;
import com.vlad_iglin.google_leads_transmitter.domain.lead.Lead;
import com.vlad_iglin.google_leads_transmitter.infrastructure.config.MapStructConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(config = MapStructConfig.class)
public interface LeadMapper {

    MongoLead toEntity(Lead lead);

    @Mapping(target = "notes", source = "notes", qualifiedByName = "listToString")
    SaveLeadRequest toRequest(Lead lead);

    @Named("listToString")
    default String listToString(List<String> notes) {
        if (notes == null || notes.isEmpty()) {
            return null;
        }
        return String.join("\n\n", notes);
    }

}
