package com.xxAMIDOxx.xxSTACKSxx.api.category;

import com.xxAMIDOxx.xxSTACKSxx.api.WebServiceEndPoints;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.annotations.Step;

public class CategoryRequests {

    String menuUrl = WebServiceEndPoints.BASE_URL.getUrl() + WebServiceEndPoints.MENU.getUrl();

    @Step("Create a new category")
    public void createCategory(String body, String menuID) {
        SerenityRest.given()
                .contentType("application/json")
                .header("Content-Type", "application/json")
                .body(body)
                .when()
                .post(menuUrl.concat("/").concat(menuID).concat(WebServiceEndPoints.CATEGORY.getUrl()));
    }

    @Step("Update the category")
    public void updateCategory(String body, String menuID, String categoryID) {
        SerenityRest.given()
                .contentType("application/json")
                .header("Content-Type", "application/json")
                .body(body)
                .when()
                .put(menuUrl.concat("/").concat(menuID).concat(WebServiceEndPoints.CATEGORY.getUrl())
                        .concat("/").concat(categoryID));
    }

    @Step("Delete the category")
    public void deleteTheCategory(String menuID, String categoryID) {
        SerenityRest.given()
                .contentType("application/json")
                .when()
                .delete(menuUrl.concat("/").concat(menuID)
                        .concat(WebServiceEndPoints.CATEGORY.getUrl())
                        .concat("/").concat(categoryID));
    }
}
