package com.xxAMIDOxx.xxSTACKSxx.api.item;

import com.xxAMIDOxx.xxSTACKSxx.api.WebServiceEndPoints;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.annotations.Step;

public class ItemRequests {

  String menuUrl = WebServiceEndPoints.BASE_URL.getUrl().concat(WebServiceEndPoints.MENU.getUrl());

  @Step("Create a new item")
  public void createItem(String body, String menuID, String categoryID) {
    SerenityRest.given()
        .contentType("application/json")
        .body(body)
        .when()
        .post(
            menuUrl
                .concat("/")
                .concat(menuID)
                .concat(WebServiceEndPoints.CATEGORY.getUrl())
                .concat("/")
                .concat(categoryID)
                .concat(WebServiceEndPoints.ITEMS.getUrl()));
  }

  @Step("Update the item")
  public void updateItem(String body, String menuID, String categoryID, String itemID) {
    SerenityRest.given()
        .contentType("application/json")
        .body(body)
        .when()
        .put(
            menuUrl
                .concat("/")
                .concat(menuID)
                .concat(WebServiceEndPoints.CATEGORY.getUrl())
                .concat("/")
                .concat(categoryID)
                .concat(WebServiceEndPoints.ITEMS.getUrl())
                .concat("/")
                .concat(itemID));
  }

  @Step("Delete the item")
  public void deleteTheItem(String menuID, String categoryID, String itemID) {
    SerenityRest.given()
        .contentType("application/json")
        .when()
        .delete(
            menuUrl
                .concat("/")
                .concat(menuID)
                .concat(WebServiceEndPoints.CATEGORY.getUrl())
                .concat("/")
                .concat(categoryID)
                .concat(WebServiceEndPoints.ITEMS.getUrl())
                .concat("/")
                .concat(itemID));
  }
}
