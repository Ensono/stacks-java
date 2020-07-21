package com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Objects;

public class SearchMenuResult {

  @JsonProperty("pageSize")
  private Integer pageSize;

  @JsonProperty("pageNumber")
  private Integer pageNumber;

  @JsonProperty("results")
  private List<SearchMenuResultItem> results;

  public SearchMenuResult() {
  }

  public SearchMenuResult(Integer pageSize, Integer pageNumber,
                          List<SearchMenuResultItem> results) {
    this.pageSize = pageSize;
    this.pageNumber = pageNumber;
    this.results = results;
  }

  public Integer getPageSize() {
    return pageSize;
  }

  public Integer getPageNumber() {
    return pageNumber;
  }

  public List<SearchMenuResultItem> getResults() {
    return results;
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

  @Override
  public String toString() {
    return "SearchMenuResult{" +
            "pageSize=" + pageSize +
            ", pageNumber=" + pageNumber +
            ", results=" + results +
            '}';
  }
}

