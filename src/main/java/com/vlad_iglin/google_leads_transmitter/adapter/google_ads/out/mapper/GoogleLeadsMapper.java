package com.vlad_iglin.google_leads_transmitter.adapter.google_ads.out.mapper;

import com.google.ads.googleads.v23.resources.ContactDetails;
import com.google.ads.googleads.v23.resources.LocalServicesLead;
import com.vlad_iglin.google_leads_transmitter.domain.lead.Lead;
import com.vlad_iglin.google_leads_transmitter.infrastructure.config.MapStructConfig;
import org.mapstruct.Mapper;

@Mapper(config = MapStructConfig.class)
public interface GoogleLeadsMapper {

    default Lead toLead(LocalServicesLead lead, String referralSource) {
        ContactDetails contactDetails = lead.getContactDetails();
        return new Lead(
                contactDetails.getDescriptorForType().getFullName(),
                contactDetails.getEmail(),
                contactDetails.getPhoneNumber(),
                referralSource
        );
    }

}
