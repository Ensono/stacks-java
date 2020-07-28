package com.xxAMIDOxx.xxSTACKSxx.api.menu;

import com.xxAMIDOxx.xxSTACKSxx.api.WebServiceEndPoints;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.annotations.Step;

public class MenuRequests {

    String menuUrl = WebServiceEndPoints.BASE_URL.getUrl() + WebServiceEndPoints.MENU.getUrl();

    @Step("Create a new menu")
    public void createMenu(String body) {
        SerenityRest.given()
                .contentType("application/json")
                .header("Content-Type", "application/json")
                .body(body)
                .when()
                .post(menuUrl);

    }

    @Step("Update the menu")
    public void updateMenu(String body, String id) {
        SerenityRest.given()
                .contentType("application/json")
                .header("Content-Type", "application/json")
                .body(body)
                .when()
                .put(menuUrl + "/" + id);
    }

    @Step("Get the menu")
    public void getMenu(String id) {
        SerenityRest.get(menuUrl.concat("/").concat(id));
    }
}
