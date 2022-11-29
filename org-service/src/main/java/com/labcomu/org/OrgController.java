package com.labcomu.org;

import javax.validation.constraints.NotNull;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.labcomu.faultinjection.annotation.Delay;
import com.labcomu.org.resource.ResourceOrganization;
import com.labcomu.org.resource.ResourceResearcher;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/org")
@Validated
@RequiredArgsConstructor
public class OrgController {
  private static final int CONFLICT = 409;
  private final OrgService service;

  @GetMapping("organization/{url}")
  @Delay(value=5, threshold=0.9)
  public ResponseEntity<ResourceOrganization> getOrganization(@NotNull @PathVariable String url) {
    return ResponseEntity.of(service.getOrganization(url));
  }

  @PostMapping("organization/researcher/{url}")
  public ResponseEntity<ResourceResearcher> createResearcher(@NotNull @PathVariable String url, @NotNull @RequestBody ResourceResearcher researcher) {
      return service.createResearcher(url, researcher).map(ResponseEntity::ok).orElse(ResponseEntity.status(CONFLICT).build());
  }

}
