package com.xxAMIDOxx.xxSTACKSxx.api.stepdefinitions;

import com.xxAMIDOxx.xxSTACKSxx.api.menu.MenuRequests;
import com.xxAMIDOxx.xxSTACKSxx.api.models.Menu;
import com.xxAMIDOxx.xxSTACKSxx.api.models.ResponseWrapper;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.cucumber.suiteslicing.SerenityTags;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static net.serenitybdd.rest.SerenityRest.lastResponse;
import static net.serenitybdd.rest.SerenityRest.restAssuredThat;


public class Hooks {

    private final static Logger LOGGER = LoggerFactory.getLogger(Hooks.class);


    public static void deleteAllMenusFromPreviousRun() {
        MenuRequests.getMenusBySearchTerm("(Automated Test Data)");
        ResponseWrapper responseWrapper = lastResponse().body().as(ResponseWrapper.class);
        List<Menu> listOfMenusToDelete = responseWrapper.getResults();

        for (Menu currentMenu : listOfMenusToDelete) {
            MenuRequests.deleteTheMenu(currentMenu.getId());
            restAssuredThat(response -> response.statusCode(200));

            LOGGER.info(String.format("The menu with '%s' id was successfully deleted.", currentMenu.getId()));
        }
    }


    @Before
    public void before() {
        SerenityTags.create().tagScenarioWithBatchingInfo();
    }


    @After("@DeleteCreatedMenu")
    public void afterFirst() {
        String menuId = String.valueOf(Serenity.getCurrentSession().get("MenuId"));
        if (!menuId.isEmpty()) {
            MenuRequests.deleteTheMenu(menuId);
            LOGGER.info(String.format("The menu with '%s' id was successfully deleted.", menuId));
        }
    }
}

