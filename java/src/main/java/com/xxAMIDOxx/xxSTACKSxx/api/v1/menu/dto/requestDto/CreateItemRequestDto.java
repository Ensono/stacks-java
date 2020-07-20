package com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.dto.requestDto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import javax.validation.constraints.NotNull;

/**
 * Dto to map Menu  item request
 *
 * @author ArathyKrishna
 */
@Schema(name = "CreateItemRequest")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "name",
        "description",
        "price",
        "available"
})
public class CreateItemRequestDto {

  @NotNull
  @JsonProperty("name")
  private String name;
  @NotNull
  @JsonProperty("description")
  private String description;
  @NotNull
  @JsonProperty("price")
  private double price;
  @NotNull
  @JsonProperty("available")
  private boolean available;

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

  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }

  public boolean isAvailable() {
    return available;
  }

  public void setAvailable(boolean available) {
    this.available = available;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this).append("name", name)
            .append("description", description)
            .append("price", price)
            .append("available", available).toString();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(name).append(available)
            .append(description).append(price).toHashCode();
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) {
      return true;
    }
    if ((other instanceof CreateItemRequestDto) == false) {
      return false;
    }
    CreateItemRequestDto rhs = ((CreateItemRequestDto) other);
    return new EqualsBuilder().append(name, rhs.name)
            .append(available, rhs.available).append(description, rhs.description)
            .append(price, rhs.price).isEquals();
  }

}
