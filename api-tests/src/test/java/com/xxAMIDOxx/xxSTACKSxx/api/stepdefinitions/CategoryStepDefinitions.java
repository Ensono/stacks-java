package com.xxAMIDOxx.xxSTACKSxx.api.stepdefinitions;

import com.xxAMIDOxx.xxSTACKSxx.api.WebServiceEndPoints;
import com.xxAMIDOxx.xxSTACKSxx.api.category.CategoryActions;
import com.xxAMIDOxx.xxSTACKSxx.api.category.CategoryRequests;
import com.xxAMIDOxx.xxSTACKSxx.api.menu.MenuActions;
import com.xxAMIDOxx.xxSTACKSxx.api.models.Category;
import com.xxAMIDOxx.xxSTACKSxx.api.models.Menu;
import com.xxAMIDOxx.xxSTACKSxx.api.templates.FieldValues;
import com.xxAMIDOxx.xxSTACKSxx.api.templates.MergeFrom;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.annotations.Steps;
import org.junit.Assert;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static net.serenitybdd.rest.SerenityRest.lastResponse;
import static net.serenitybdd.rest.SerenityRest.restAssuredThat;

public class CategoryStepDefinitions {

    @Steps
    private CategoryRequests categoryRequests;

    @Steps
    private MenuActions menuActions;

    private final String MENU_URL = WebServiceEndPoints.BASE_URL.getUrl() + WebServiceEndPoints.MENU.getUrl();

    private String categoryBody;
    private String menuId;
    private String categoryId;

    @When("the following category data:")
    public void create_the_category_body(List<Map<String, String>> categoryDetails) throws IOException {
        categoryBody = MergeFrom.template("templates/category.json")
                .withDefaultValuesFrom(FieldValues.in("templates/standard-category.properties"))
                .withFieldsFrom(categoryDetails.get(0));

        Serenity.setSessionVariable("CategoryBody").to(categoryBody);
    }

    @When("I create a new category for the existing menu")
    public void i_create_the_category_for_existing_menu() {
        menuId = menuActions.getIdOfLastCreatedObject();
        categoryRequests.createCategory(categoryBody, menuId);
    }

    @Then("the category was successfully created")
    public void the_category_was_successfully_created() {
        restAssuredThat(response -> response.statusCode(201));
        categoryId = menuActions.getIdOfLastCreatedObject();
    }

    @When("I create a new category for the menu with {string} id")
    public void i_create_a_new_category_for_the_menu_with_id(String menu_id) {
        categoryRequests.createCategory(categoryBody, menu_id);
    }

    @Then("the created category should include the following data:")
    public void the_category_should_include_the_following_data(List<Map<String, String>> categoryDetails) {
        SerenityRest.get(MENU_URL.concat("/".concat(menuId)));
        Category expectedCategory = CategoryActions.mapToCategory(categoryDetails.get(0), categoryId);

        Menu actualMenu = menuActions.responseToMenu(lastResponse());
        Assert.assertTrue(actualMenu != null && actualMenu.getCategories().contains(expectedCategory));
    }
}
