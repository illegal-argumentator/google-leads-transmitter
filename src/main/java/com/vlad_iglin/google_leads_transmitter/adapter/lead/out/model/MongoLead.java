package com.vlad_iglin.google_leads_transmitter.adapter.lead.out.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document
public class MongoLead {

    @Id
    private String id;

    private long leadId;

    private String fullName;

    @Indexed(unique = true)
    private String email;
    private String phoneNumber;

    private List<String> notes;

    private String referralSource;
    private String originAddressFull;
    private String branchId;

    private String creationDateTime;
}
