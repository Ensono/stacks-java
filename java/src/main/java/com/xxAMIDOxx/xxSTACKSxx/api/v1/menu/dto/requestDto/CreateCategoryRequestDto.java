package com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.dto.requestDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * A DTO to map Create Category request.
 *
 * @author ArathyKrishna
 */

@Schema(name = "CreateCategoryRequest")
public class CreateCategoryRequestDto {

  @NotNull
  @JsonProperty("name")
  private String name;

  @NotNull
  @JsonProperty("description")
  private String description;

  public CreateCategoryRequestDto() {
  }

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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof CreateCategoryRequestDto)) return false;
    CreateCategoryRequestDto requestDto = (CreateCategoryRequestDto) o;
    return Objects.equals(name, requestDto.name)
            && Objects.equals(description, requestDto.description);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, description);
  }

  @Override
  public String toString() {
    return "CreateCategoryRequestDto{" + "name=" + name
            + ", description=" + description
            + '}';
  }
}
