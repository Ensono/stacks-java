package com.amido.stacks.menu.api.v1.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchMenuResult {

  @JsonProperty("pageSize")
  private Integer pageSize;

  @JsonProperty("pageNumber")
  private Integer pageNumber;

  @JsonProperty("results")
  private List<SearchMenuResultItem> results;

  /**
   * TODO Should have count and total included in response. @JsonProperty("count") private Integer
   * count; @JsonProperty("total") private Integer total;
   */
}
