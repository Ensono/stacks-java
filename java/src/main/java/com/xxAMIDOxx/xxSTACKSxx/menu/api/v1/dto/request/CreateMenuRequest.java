package com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
public class CreateMenuRequest {

  @JsonProperty("name")
  @NotEmpty
  private String name = null;

  @JsonProperty("description")
  @NotEmpty
  private String description = null;

  @JsonProperty("tenantId")
  @NotNull
  private UUID tenantId = null;

  @JsonProperty("enabled")
  @NotNull
  private Boolean enabled = null;
}
