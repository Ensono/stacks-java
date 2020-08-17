package com.xxAMIDOxx.xxSTACKSxx.api.menu;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xxAMIDOxx.xxSTACKSxx.api.ExceptionMessages;
import com.xxAMIDOxx.xxSTACKSxx.api.models.Menu;
import io.restassured.response.Response;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static net.serenitybdd.rest.SerenityRest.lastResponse;

public class MenuActions {

    private final static Logger LOGGER = LoggerFactory.getLogger(MenuActions.class);
    private final static ObjectMapper objectMapper = new ObjectMapper();


    public String getIdOfLastCreatedObject() {
        return String.valueOf(toJson(lastResponse().getBody().prettyPrint()).get("id"));
    }

    public String getRestaurantIdOfLastCreatedMenu() {
        return String.valueOf(toJson(lastResponse().getBody().prettyPrint()).get("restaurantId"));
    }

    public void check_exception_message(ExceptionMessages parameter, Response response) {
        JSONObject js = toJson(response.getBody().prettyPrint());
        String message = parameter.getMessage();

        Assert.assertTrue(js.get("description").toString().matches(message));
    }


    public static JSONObject toJson(String stringToParse) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(stringToParse);
        } catch (JSONException err) {
            LOGGER.warn(err.getMessage());
        }
        return jsonObject;
    }


    public Menu responseToMenu(Response response) {
        Menu actualMenu = null;
        try {
            actualMenu = objectMapper.readValue(response.body().print(), Menu.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return actualMenu;
    }


    public static Menu mapToMenu(Map<String, String> properties, String id) {
        return new Menu(id, UUID.fromString(properties.get("tenantId")), properties.get("name"),
                properties.get("description"), null, Boolean.parseBoolean(properties.get("enabled")));
    }


    public static String createUrlWithCriteria(List<List<String>> data) {
        StringBuilder finalPath = new StringBuilder();
        for (int i = 0; i < data.size(); i++) {

            String parameter = data.get(i).get(0);
            String value = data.get(i).get(1);

            finalPath.append(parameter).append("=").append(value);
            if (i != data.size() - 1) {
                finalPath.append("&");
            }
        }
        return finalPath.toString();
    }
}
