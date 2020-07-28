package com.xxAMIDOxx.xxSTACKSxx.api.stepdefinitions;

import com.xxAMIDOxx.xxSTACKSxx.api.ExceptionMessages;
import com.xxAMIDOxx.xxSTACKSxx.api.item.ItemActions;
import com.xxAMIDOxx.xxSTACKSxx.api.item.ItemRequests;
import com.xxAMIDOxx.xxSTACKSxx.api.menu.MenuActions;
import com.xxAMIDOxx.xxSTACKSxx.api.menu.MenuRequests;
import com.xxAMIDOxx.xxSTACKSxx.api.models.Item;
import com.xxAMIDOxx.xxSTACKSxx.api.models.Menu;
import com.xxAMIDOxx.xxSTACKSxx.api.templates.FieldValues;
import com.xxAMIDOxx.xxSTACKSxx.api.templates.MergeFrom;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.core.Serenity;
import net.thucydides.core.annotations.Steps;
import org.junit.Assert;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static net.serenitybdd.rest.SerenityRest.lastResponse;
import static net.serenitybdd.rest.SerenityRest.restAssuredThat;

public class ItemStepDefinitions {

    @Steps
    ItemRequests itemRequest;

    @Steps
    MenuRequests menuRequest;

    @Steps
    MenuActions menuActions;

    String menuId;
    String categoryId;
    String itemBody;
    String itemId;

    @Given("the following item data:")
    public void the_following_item_data(List<Map<String, String>> itemDetails) throws IOException {
        itemBody = MergeFrom.template("templates/item.json")
                .withDefaultValuesFrom(FieldValues.in("templates/standard-item.properties"))
                .withFieldsFrom(itemDetails.get(0));

        Serenity.setSessionVariable("Item").to(itemBody);
    }

    @When("I create an item for the following menu and category:")
    public void create_item_for_menu_and_category(DataTable table) {
        List<List<String>> data = table.asLists(String.class);
        menuId = data.get(0).get(1);
        categoryId = data.get(1).get(1);

        itemRequest.createItem(itemBody, menuId, categoryId);
    }


    @Then("the item was successfully created")
    public void the_item_was_successfully_created() {
        restAssuredThat(response -> response.statusCode(201));
        itemId = menuActions.getIdOfLastCreatedObject();
    }


    @Then("the created item should include the following data:")
    public void the_created_item_contain_correct_data(List<Map<String, String>> itemDetails) {
        menuRequest.getMenu(menuId);

        Item expectedItem = ItemActions.mapToItem(itemDetails.get(0), itemId);
        Menu actualMenu = menuActions.responseToMenu(lastResponse());

        List<Item> actualItems = actualMenu.getCategories()
                .stream()
                .filter(cat -> cat.getId().equals(categoryId))
                .flatMap(category -> category.getItems().stream())
                .collect(Collectors.toList());

        Assert.assertTrue(actualItems.contains(expectedItem));
    }

    @Then("the 'item already exist' message is returned")
    public void i_check_the_menu_already_exist_message() {
        menuActions.check_exception_message(ExceptionMessages.ITEM_ALREADY_EXISTS, lastResponse());
    }
}
