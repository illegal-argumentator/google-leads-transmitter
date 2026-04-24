package com.vlad_iglin.google_leads_transmitter.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class ExecutorConfig {

    @Value("${thread.limit}")
    private int THREADS;

    @Bean
    ExecutorService executorService() {
        return Executors.newFixedThreadPool(Math.min(THREADS, Runtime.getRuntime().availableProcessors()));
    }

}
