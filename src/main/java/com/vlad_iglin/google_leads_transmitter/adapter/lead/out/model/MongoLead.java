package com.vlad_iglin.google_leads_transmitter.adapter.lead.out.model;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Document
public class MongoLead {

    @Id
    private String id;

    private String fullName;

    @Indexed(unique = true)
    private String email;

    private String phoneNumber;

    private String referralSource;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;
}
