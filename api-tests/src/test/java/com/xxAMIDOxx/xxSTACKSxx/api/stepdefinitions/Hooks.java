package com.xxAMIDOxx.xxSTACKSxx.api.stepdefinitions;

import com.xxAMIDOxx.xxSTACKSxx.api.menu.MenuRequests;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.cucumber.suiteslicing.SerenityTags;
import net.thucydides.core.annotations.Steps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Hooks {

    private final static Logger LOGGER = LoggerFactory.getLogger(Hooks.class);

    @Steps
    MenuRequests menuRequests;

    @Before
    public void before() {
        SerenityTags.create().tagScenarioWithBatchingInfo();
    }


    @After("@DeleteCreatedMenu")
    public void afterFirst() {
        String menuId = String.valueOf(Serenity.getCurrentSession().get("MenuId"));
        if (!menuId.isEmpty()) {
            menuRequests.deleteTheMenu(menuId);
            LOGGER.info(String.format("The menu with '%s' id was successfully deleted.", menuId));
        }
    }
}

