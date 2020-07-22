package com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.dto.responseDto;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Dto to map UpdatedMenu Response.
 *
 * @author ArathyKrishna
 */
public class MenuUpdateResponseDto {

  private String id;

  private String name;

  private String description;

  private String restaurantId;

  private boolean enabled;

  public MenuUpdateResponseDto() {
  }

  public MenuUpdateResponseDto(String id, String name, String description,
                               String restaurantId, boolean enabled) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.restaurantId = restaurantId;
    this.enabled = enabled;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
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

  public String getRestaurantId() {
    return restaurantId;
  }

  public void setRestaurantId(String restaurantId) {
    this.restaurantId = restaurantId;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
            .append("id", id)
            .append("name", name)
            .append("description", description)
            .append("enabled", enabled)
            .append("restaurantId", restaurantId)
            .toString();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(id).append(name).append(description)
            .append(restaurantId).append(enabled).toHashCode();
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) {
      return true;
    }
    if (!(other instanceof MenuUpdateResponseDto)) {
      return false;
    }
    MenuUpdateResponseDto rhs = ((MenuUpdateResponseDto) other);
    return new EqualsBuilder().append(id, rhs.id)
            .append(name, rhs.name)
            .append(description, rhs.description)
            .append(restaurantId, rhs.restaurantId)
            .append(enabled, rhs.enabled)
            .isEquals();
  }
}
