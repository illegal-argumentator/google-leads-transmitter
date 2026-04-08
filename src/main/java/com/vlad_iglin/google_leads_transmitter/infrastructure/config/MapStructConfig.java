package com.vlad_iglin.google_leads_transmitter.infrastructure.config;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.MapperConfig;
import org.mapstruct.ReportingPolicy;

@MapperConfig(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.WARN,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface MapStructConfig {
}