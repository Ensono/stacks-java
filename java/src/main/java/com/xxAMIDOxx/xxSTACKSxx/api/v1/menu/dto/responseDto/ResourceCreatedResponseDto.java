package com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.dto.responseDto;

/**
 * @author Suresh Krishnan
 * @author ArathyKrishna
 */
public class ResourceCreatedResponseDto {

  private String id;

  public ResourceCreatedResponseDto(String id) {
    this.id = id;
  }

  private ResourceCreatedResponseDto() {
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }
}
