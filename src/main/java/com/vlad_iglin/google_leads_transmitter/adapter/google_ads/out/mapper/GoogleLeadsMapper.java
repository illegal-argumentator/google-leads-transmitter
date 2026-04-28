package com.vlad_iglin.google_leads_transmitter.adapter.google_ads.out.mapper;

import com.google.ads.googleads.v23.resources.ContactDetails;
import com.google.ads.googleads.v23.resources.LocalServicesLead;
import com.vlad_iglin.google_leads_transmitter.adapter.lead.out.props.LeadProps;
import com.vlad_iglin.google_leads_transmitter.domain.lead.Lead;
import com.vlad_iglin.google_leads_transmitter.shared.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GoogleLeadsMapper {

    private final LeadProps props;

    public Lead toLead(String branchId, LocalServicesLead lead) {
        ContactDetails contactDetails = lead.getContactDetails();
        String consumerName = lead.getContactDetails().getConsumerName();
        LeadProps.Account account = props.getByBranchId(branchId);
        return new Lead(
                lead.getId(),
                StringUtils.isBlank(consumerName) ? props.getName() : consumerName,
                contactDetails.getEmail(),
                contactDetails.getPhoneNumber(),
                props.getReferralSource(),
                new ArrayList<>(List.of("Lead Id: %s".formatted(lead.getId()))),
                lead.getResourceName(),
                lead.getCreationDateTime(),
                account.location(),
                account.branchId()
        );
    }

}
