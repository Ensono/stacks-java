package com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class CreateMenuRequest {

  @JsonProperty("name")
  @NotBlank
  private String name = null;

  @JsonProperty("description")
  @NotBlank
  private String description = null;

  @JsonProperty("tenantId")
  @NotNull
  private UUID tenantId = null;

  @JsonProperty("enabled")
  @NotNull
  private Boolean enabled = null;
}
