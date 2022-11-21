package com.labcomu.edu.client;

import com.labcomu.edu.configuration.EduProperties;
import com.labcomu.edu.resource.Organization;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.micrometer.core.lang.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.reactive.function.client.WebClient;

import javax.validation.constraints.NotNull;

@Component
@Validated
public class OrgGateway {
    private final String fetchOrganizationUrl;

    public static final String ORG_GATEWAY = "org-gateway";

    private final WebClient.Builder webClientBuilder;

    private Logger logger;

    public OrgGateway(final WebClient.Builder webClientBuilder,
            final EduProperties properties) {
        this.webClientBuilder = webClientBuilder;
        this.fetchOrganizationUrl = properties.getUrl().getFetchOrganizationDetails();

        this.logger = LoggerFactory.getLogger(OrgGateway.class);
    }

    @CircuitBreaker(name = ORG_GATEWAY, fallbackMethod = "fallbackGetOrganization")
    @RateLimiter(name = ORG_GATEWAY, fallbackMethod = "fallbackGetOrganizationTimeout")
    public Organization getOrganization(@NotNull final String url) {
        return webClientBuilder.build()
                .get()
                .uri(fetchOrganizationUrl, url)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Organization.class)
                .block();
    }

    public Organization fallbackGetOrganization(Exception e){
        this.logger.error("Não foi possível obter informações da organização.");
        return null;
    }

    public Organization fallbackGetOrganizationTimeout(Exception e){
        this.logger.error("A requisição durou mais de três segundos");
        return null;
    }
}
