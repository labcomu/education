package com.labcomu.edu;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.labcomu.edu.client.OrcidGateway;
import com.labcomu.edu.client.OrgGateway;
import com.labcomu.edu.exceptions.ResponseGatewayException;
import com.labcomu.edu.resource.Organization;

import lombok.RequiredArgsConstructor;

@Service
@Validated
@RequiredArgsConstructor
public class EduService {

	private Logger LOG = LoggerFactory.getLogger(EduService.class);

	private final OrgGateway orgGateway;
	private final OrcidGateway orcidGateway;

	public Optional<Organization> getOrganization(String url) {
		Optional<Organization> organization = Optional.empty();
		try {
			organization = Optional.ofNullable(orgGateway.getOrganization(url));
		} catch (ResponseGatewayException e) {
			LOG.error(e.getMessage() + ": " + e.getStatus().name());
			LOG.warn("Retornando valor vazio");
			return organization;
		}

		organization.get().setResearchers(
				organization.get().getResearchers().stream()
				.map(researcher -> {
					return orcidGateway.getResearcher(researcher.getOrcid(), 0);
				}).toList());

		return organization;
	}
}
