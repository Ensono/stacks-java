package com.xxAMIDOxx.xxSTACKSxx.api.v1.menu;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;
import java.util.List;

public class SearchMenuResult {

  @JsonProperty("pageSize")
  private Integer pageSize = null;

  @JsonProperty("pageNumber")
  private Integer pageNumber = null;

  @JsonProperty("results")
  @Valid
  private List<SearchMenuResultItem> results = null;

  public SearchMenuResult() {
  }

  public SearchMenuResult(Integer pageSize, Integer pageNumber, @Valid List<SearchMenuResultItem> results) {
    this.pageSize = pageSize;
    this.pageNumber = pageNumber;
    this.results = results;
  }

  public Integer getPageSize() {
    return pageSize;
  }

  public void setPageSize(Integer pageSize) {
    this.pageSize = pageSize;
  }

  public Integer getPageNumber() {
    return pageNumber;
  }

  public void setPageNumber(Integer pageNumber) {
    this.pageNumber = pageNumber;
  }

  public List<SearchMenuResultItem> getResults() {
    return results;
  }

  public void setResults(List<SearchMenuResultItem> results) {
    this.results = results;
  }
}

