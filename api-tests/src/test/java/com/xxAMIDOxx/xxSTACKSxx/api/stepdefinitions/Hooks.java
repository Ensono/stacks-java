package com.xxAMIDOxx.xxSTACKSxx.api.stepdefinitions;

import com.xxAMIDOxx.xxSTACKSxx.api.menu.MenuRequests;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.cucumber.suiteslicing.SerenityTags;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Hooks {

  private static final Logger LOGGER = LoggerFactory.getLogger(Hooks.class);

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
