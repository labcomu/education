package com.labcomu.edu.client;

import javax.validation.constraints.NotNull;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.labcomu.edu.configuration.EduProperties;
import com.labcomu.edu.exceptions.ResponseGatewayException;
import com.labcomu.edu.resource.Organization;

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

    public Organization getOrganization(@NotNull final String url) throws ResponseGatewayException {
    	try {
	        return webClientBuilder.build()
	                .get().uri(fetchOrganizationUrl, url)
	                .accept(MediaType.APPLICATION_JSON)
	                .retrieve().bodyToMono(Organization.class).block();
    	}
        catch(WebClientResponseException e) {
        	throw new ResponseGatewayException("Serviço indisponível", HttpStatus.SERVICE_UNAVAILABLE);
        }
    }
}
