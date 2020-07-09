package com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof SearchMenuResult)) return false;
    SearchMenuResult that = (SearchMenuResult) o;
    return Objects.equals(pageSize, that.pageSize) &&
            Objects.equals(pageNumber, that.pageNumber) &&
            Objects.equals(results, that.results);
  }

  @Override
  public int hashCode() {
    return Objects.hash(pageSize, pageNumber, results);
  }

}

