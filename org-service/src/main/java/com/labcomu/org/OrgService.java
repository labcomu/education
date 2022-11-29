package com.labcomu.org;

import java.util.Optional;

import javax.validation.constraints.NotNull;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.labcomu.org.domain.Organization;
import com.labcomu.org.domain.mapper.ResearcherMapper;
import com.labcomu.org.domain.repository.OrganizationRepository;
import com.labcomu.org.domain.repository.ResearcherRepository;
import com.labcomu.org.resource.ResourceOrganization;
import com.labcomu.org.resource.ResourceResearcher;
import com.labcomu.org.resource.mapper.ResourceOrganizationMapper;
import com.labcomu.org.resource.mapper.ResourceResearcherMapper;

import lombok.RequiredArgsConstructor;

@Service
@Validated
@RequiredArgsConstructor
public class OrgService {
    private final ResearcherMapper researcherMapper;
    private final ResourceResearcherMapper resourceResearcherMapper;
    private final ResourceOrganizationMapper resourceOrganizationMapper;
    private final OrganizationRepository organizationRepository;
    private final ResearcherRepository researcherRepository;

    public Optional<ResourceOrganization> getOrganization(@NotNull final String url) {
        Optional<Organization> organization = organizationRepository.findByUrl(url);

        // load researchers (lazy)
        organization.ifPresent(Organization::getResearchers);

        return organization.map(resourceOrganizationMapper::map);
    }

    public Optional<ResourceResearcher> createResearcher(@NotNull final String url, @NotNull ResourceResearcher resourceResearcher) {
        if (researcherRepository.existsByOrcid(resourceResearcher.getOrcid()))
            return Optional.empty();

        organizationRepository.findByUrl(url).map(resourceOrganizationMapper::map).ifPresent(resourceResearcher::setOrganization);

        return Optional.of(resourceResearcherMapper.map(researcherRepository.save(researcherMapper.map(resourceResearcher))));
    }

}
