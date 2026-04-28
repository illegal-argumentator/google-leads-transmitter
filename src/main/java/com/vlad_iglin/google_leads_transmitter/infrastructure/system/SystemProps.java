package com.vlad_iglin.google_leads_transmitter.infrastructure.system;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Getter
@Setter
@Configuration
@ConfigurationProperties("system")
public class SystemProps {

    private Leads leads;

    public record Leads(
            boolean saveToDb,
            boolean saveToCrm
    ) { }

    @PostConstruct
    void init() {
        log.info("App leads initialized with configurations: save-to-db - {}, save-to-crm - {}.", leads.saveToDb, leads.saveToCrm);
    }
}
