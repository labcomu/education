package com.labcomu.edu.client;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.labcomu.edu.EduService;
import com.labcomu.edu.configuration.EduProperties;
import com.labcomu.edu.exceptions.ResponseGatewayException;
import com.labcomu.edu.resource.Organization;
import com.labcomu.edu.resource.Researcher;

@Component
@Validated
public class OrgGateway {
	private final String fetchOrganizationUrl;

	private final WebClient.Builder webClientBuilder;
	private Logger LOG = LoggerFactory.getLogger(EduService.class);

	public OrgGateway(final WebClient.Builder webClientBuilder, final EduProperties properties) {
		this.webClientBuilder = webClientBuilder;
		this.fetchOrganizationUrl = properties.getUrl().getFetchOrganizationDetails();
	}

	public Organization getOrganization(@NotNull final String url) throws ResponseGatewayException {
		ExecutorService executor = Executors.newCachedThreadPool();
		Callable<Organization> task = new Callable<Organization>() {
			public Organization call() {
				return webClientBuilder.build().get().uri(fetchOrganizationUrl, url).accept(MediaType.APPLICATION_JSON)
						.retrieve().bodyToMono(Organization.class).block();
			}
		};
		Future<Organization> future = executor.submit(task);
		try {
			Object result = future.get(3, TimeUnit.SECONDS);
			return (Organization) result;
		} catch (WebClientResponseException |  ExecutionException | InterruptedException e) {
			throw new ResponseGatewayException("Serviço indisponível", HttpStatus.SERVICE_UNAVAILABLE);
		} catch (TimeoutException ex) {
			LOG.error("Timeout: OrgService demorou mais de 3s para responder.");
			Organization organization = new Organization();
			organization.setResearchers(new ArrayList<Researcher>());
			return organization;
		}
	}
}
