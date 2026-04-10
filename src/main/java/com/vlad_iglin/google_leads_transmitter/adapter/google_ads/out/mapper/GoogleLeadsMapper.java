package com.vlad_iglin.google_leads_transmitter.adapter.google_ads.out.mapper;

import com.google.ads.googleads.v23.resources.ContactDetails;
import com.google.ads.googleads.v23.resources.LocalServicesLead;
import com.vlad_iglin.google_leads_transmitter.domain.lead.Lead;
import com.vlad_iglin.google_leads_transmitter.infrastructure.config.MapStructConfig;
import com.vlad_iglin.google_leads_transmitter.shared.StringUtils;
import org.mapstruct.Mapper;

@Mapper(config = MapStructConfig.class)
public interface GoogleLeadsMapper {

    default Lead toLead(LocalServicesLead lead, String referralSource, String defaultLeadName) {
        ContactDetails contactDetails = lead.getContactDetails();
        String consumerName = lead.getContactDetails().getConsumerName();
        return new Lead(
                StringUtils.isBlank(consumerName) ? defaultLeadName : consumerName,
                contactDetails.getEmail(),
                contactDetails.getPhoneNumber(),
                referralSource,
                lead.getResourceName(),
                lead.getCreationDateTime()
        );
    }

}
