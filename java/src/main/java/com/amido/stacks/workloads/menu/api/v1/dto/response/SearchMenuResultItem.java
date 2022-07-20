package com.amido.stacks.workloads.menu.api.v1.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SearchMenuResultItem {

  @JsonProperty("id")
  private UUID id;

  @JsonProperty("restaurantId")
  private UUID restaurantId;

  @JsonProperty("name")
  private String name;

  @JsonProperty("description")
  private String description;

  @JsonProperty("enabled")
  private Boolean enabled;
}
