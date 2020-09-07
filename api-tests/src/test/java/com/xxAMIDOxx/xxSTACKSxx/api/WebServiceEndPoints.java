package com.xxAMIDOxx.xxSTACKSxx.api;

public enum WebServiceEndPoints {
  BASE_URL("https://prod-java-api.prod.amidostacks.com/api"),
  CATEGORY("/category"),
  STATUS("/health"),
  MENU("/v1/menu"),
  MENU_V2("/v2/menu"),
  ITEMS("/items");

  private final String url;

  WebServiceEndPoints(String url) {
    this.url = url;
  }

  public String getUrl() {
    return url;
  }
}
