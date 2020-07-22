package com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.dto.requestDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import javax.validation.Valid;

/**
 * Dto to map Update menu request.
 *
 * @author ArathyKrishna
 */
public class UpdateMenuRequestDto {

  @JsonProperty("name")
  @Valid
  private String name;
  @JsonProperty("description")
  @Valid
  private String description;
  @JsonProperty("enabled")
  @Valid
  private boolean enabled;
  @JsonProperty("restaurantId")
  @Valid
  private String restaurantId;

  /**
   * No args constructor for use in serialization.
   */
  public UpdateMenuRequestDto() {
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

  public boolean getEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public String getRestaurantId() {
    return restaurantId;
  }

  public void setRestaurantId(String restaurantId) {
    this.restaurantId = restaurantId;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this).append("name", name)
            .append("description", description)
            .append("enabled", enabled)
            .append("restaurantId", restaurantId)
            .toString();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(name).append(description)
            .append(restaurantId).append(enabled).toHashCode();
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) {
      return true;
    }
    if (!(other instanceof UpdateMenuRequestDto)) {
      return false;
    }
    UpdateMenuRequestDto rhs = ((UpdateMenuRequestDto) other);
    return new EqualsBuilder().append(name, rhs.name)
            .append(description, rhs.description)
            .append(restaurantId, rhs.restaurantId)
            .append(enabled, rhs.enabled)
            .isEquals();
  }

}
