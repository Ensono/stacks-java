package com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.dto.requestDto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.validation.constraints.NotNull;

/**
 * DTO to map Menu Created.
 *
 * @author Suresh Krishnan
 * @author ArathyKrishna
 */
public class MenuCreateRequestDto {
  @JsonInclude(JsonInclude.Include.NON_NULL)
  @JsonPropertyOrder({
          "name",
          "description",
          "tenantId",
          "enabled"
  })

  @NotNull
  @JsonProperty("name")
  private String name;
  @NotNull
  @JsonProperty("description")
  private String description;
  @NotNull
  @JsonProperty("tenantId")
  private String tenantId;
  @NotNull
  @JsonProperty("enabled")
  private Boolean enabled;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public Boolean getEnabled() {
    return enabled;
  }

  public void setEnabled(Boolean enabled) {
    this.enabled = enabled;
  }
}
