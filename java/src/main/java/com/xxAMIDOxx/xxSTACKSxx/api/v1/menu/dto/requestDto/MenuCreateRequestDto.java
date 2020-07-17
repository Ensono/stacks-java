package com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.dto.requestDto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * DTO to map Menu Created.
 *
 * @author Suresh Krishnan
 * @author ArathyKrishna
 */
@Schema(name = "CreateMenuRequest")
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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof MenuCreateRequestDto)) return false;
    MenuCreateRequestDto requestDto = (MenuCreateRequestDto) o;
    return Objects.equals(name, requestDto.name)
            && Objects.equals(description, requestDto.description)
            && Objects.equals(tenantId, requestDto.tenantId)
            && Objects.equals(enabled, requestDto.enabled);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, description, tenantId, enabled);
  }

  @Override
  public String toString() {
    return "MenuCreateRequestDto{" + "name=" + name
            + ", description=" + description
            + ", tenantId=" + tenantId
            + ", enabled=" + enabled
            + '}';
  }
}
