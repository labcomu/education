package com.labcomu.edu;

import com.labcomu.edu.client.OrcidGateway;
import com.labcomu.edu.client.OrgGateway;
import com.labcomu.edu.resource.Organization;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;

@Service
@Validated
@RequiredArgsConstructor
public class EduService {
    private final OrgGateway orgGateway;
    private final OrcidGateway orcidGateway;

    public Organization getOrganization(String url) {
        Organization organization = orgGateway.getOrganization(url);
        if (organization == null) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Não foi possível obter informações da organização");
        }

        organization.setResearchers(organization.getResearchers().stream().map(researcher -> orcidGateway.getResearcher(researcher.getOrcid())).toList());
        return organization;
    }
}
