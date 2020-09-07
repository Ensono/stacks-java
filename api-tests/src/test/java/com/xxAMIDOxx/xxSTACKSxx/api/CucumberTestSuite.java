package com.xxAMIDOxx.xxSTACKSxx.api;

import com.xxAMIDOxx.xxSTACKSxx.api.stepdefinitions.Hooks;
import io.cucumber.junit.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

@RunWith(CucumberWithSerenity.class)
@CucumberOptions(
    plugin = {"pretty", "html:target/cucumber"},
    features = "src/test/resources/features",
    tags = "(not @Ignore) and (@Smoke or @Regression or @Functional)")
public class CucumberTestSuite {

  @BeforeClass
  public static void setup() {
    System.out.println("Delete all data from previous automated test");
    Hooks.deleteAllMenusFromPreviousRun();
  }
}
