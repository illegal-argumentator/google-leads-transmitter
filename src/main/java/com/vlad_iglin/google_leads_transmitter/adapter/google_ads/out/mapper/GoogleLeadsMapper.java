package com.vlad_iglin.google_leads_transmitter.adapter.google_ads.out.mapper;

import com.google.ads.googleads.v23.resources.ContactDetails;
import com.google.ads.googleads.v23.resources.LocalServicesLead;
import com.vlad_iglin.google_leads_transmitter.adapter.lead.out.props.LeadProps;
import com.vlad_iglin.google_leads_transmitter.domain.lead.Lead;
import com.vlad_iglin.google_leads_transmitter.infrastructure.config.MapStructConfig;
import com.vlad_iglin.google_leads_transmitter.shared.StringUtils;
import org.mapstruct.Mapper;

@Mapper(config = MapStructConfig.class)
public interface GoogleLeadsMapper {

    default Lead toLead(LocalServicesLead lead, LeadProps props) {
        ContactDetails contactDetails = lead.getContactDetails();
        String consumerName = lead.getContactDetails().getConsumerName();
        LeadProps.Account account = props.getByCustomerId(Lead.getCustomerId(lead.getResourceName()));
        return new Lead(
                StringUtils.isBlank(consumerName) ? props.getName() : consumerName,
                contactDetails.getEmail(),
                contactDetails.getPhoneNumber(),
                props.getReferralSource(),
                lead.getResourceName(),
                lead.getCreationDateTime(),
                account.location(),
                account.branchId()
        );
    }

}
