package com.labcomu.edu.client;

import com.labcomu.edu.LogWriter;
import lombok.extern.slf4j.Slf4j;
import com.labcomu.edu.configuration.EduProperties;
import com.labcomu.edu.resource.Organization;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.reactive.function.client.WebClient;

import javax.validation.constraints.NotNull;

@Slf4j
@Component
@Validated
public class OrgGateway {
    private final String fetchOrganizationUrl;

    private final WebClient.Builder webClientBuilder;

    public OrgGateway(final WebClient.Builder webClientBuilder,
            final EduProperties properties) {
        this.webClientBuilder = webClientBuilder;
        this.fetchOrganizationUrl = properties.getUrl().getFetchOrganizationDetails();
    }

    @CircuitBreaker(name = "edu-service", fallbackMethod = "write_log")
    public Organization getOrganization(@NotNull final String url) {
        return webClientBuilder.build()
                .get()
                .uri(fetchOrganizationUrl, url)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Organization.class)
                .block();
    }

    public Organization write_log(String url, CallNotPermittedException e){
        log.info("Circuit Breaker Opened at {}, with error {}", url, e);
        LogWriter.writeLogCircuitBreaker(e);
        return null;
    }
}
