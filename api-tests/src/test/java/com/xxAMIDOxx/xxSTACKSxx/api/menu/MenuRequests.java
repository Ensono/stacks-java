package com.xxAMIDOxx.xxSTACKSxx.api.menu;

import com.xxAMIDOxx.xxSTACKSxx.api.WebServiceEndPoints;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.annotations.Step;

public class MenuRequests {

    private static String menuUrl = WebServiceEndPoints.BASE_URL.getUrl().concat(WebServiceEndPoints.MENU.getUrl());

    @Step("Create a new menu")
    public void createMenu(String body) {
        SerenityRest.given()
                .contentType("application/json")
                .body(body)
                .when()
                .post(menuUrl);

    }

    @Step("Update the menu")
    public void updateMenu(String body, String id) {
        SerenityRest.given()
                .contentType("application/json")
                .body(body)
                .when()
                .put(menuUrl + "/" + id);
    }

    @Step("Get the menu")
    public void getMenu(String id) {
        SerenityRest.get(menuUrl.concat("/").concat(id));
    }

    @Step("Delete the menu")
    public static void deleteTheMenu(String id) {
        SerenityRest.given()
                .contentType("application/json")
                .when()
                .delete(menuUrl.concat("/").concat(id));
    }

    @Step("Get all menus")
    public void getAllMenus() {
        SerenityRest.get(menuUrl);
    }

    public static void getMenusBySearchTerm(String searchTerm) {
        SerenityRest.get(menuUrl.concat("?searchTerm=").concat(searchTerm));
    }

}
