package com.labcomu.edu;

import com.labcomu.edu.client.OrcidGateway;
import com.labcomu.edu.resource.Organization;
import com.labcomu.edu.resource.Researcher;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("api/v1/edu")
@Validated
@RequiredArgsConstructor
public class EduController {
  private final EduService eduService;

  private final OrcidGateway orcidGateway;

  @GetMapping("organization/{url}")
  public Organization getOrganization(@NotNull @PathVariable String url) {
    return eduService.getOrganization(url);
  }

  @GetMapping("researcher/{orcid}")
  public Researcher getResearcher(@NotNull @PathVariable String orcid) {
    return orcidGateway.getResearcher(orcid);
  }
}
