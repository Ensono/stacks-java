package com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.dto.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class CreateMenuRequest {

  @JsonProperty("name")
  private String name = null;

  @JsonProperty("description")
  private String description = null;

  @JsonProperty("tenantId")
  private UUID tenantId = null;

  @JsonProperty("enabled")
  private Boolean enabled = null;

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public UUID getTenantId() {
    return tenantId;
  }

  public Boolean getEnabled() {
    return enabled;
  }
}
