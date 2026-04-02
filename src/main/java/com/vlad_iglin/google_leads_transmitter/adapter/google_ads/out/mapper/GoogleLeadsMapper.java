package com.vlad_iglin.google_leads_transmitter.adapter.google_ads.out.mapper;

import com.google.ads.googleads.v23.resources.ContactDetails;
import com.google.ads.googleads.v23.resources.LocalServicesLead;
import com.google.ads.googleads.v23.services.GoogleAdsRow;
import com.vlad_iglin.google_leads_transmitter.domain.lead.Lead;
import com.vlad_iglin.google_leads_transmitter.infrastructure.MapStructConfig;
import org.mapstruct.Mapper;

@Mapper(config = MapStructConfig.class)
public interface GoogleLeadsMapper {

    default Lead toLead(GoogleAdsRow row, String referralSource) {
        LocalServicesLead localService = row.getLocalServicesLead();
        ContactDetails contactDetails = localService.getContactDetails();
        return new Lead(
                contactDetails.getDescriptorForType().getFullName(),
                contactDetails.getEmail(),
                contactDetails.getPhoneNumber(),
                referralSource
        );
    }

}
