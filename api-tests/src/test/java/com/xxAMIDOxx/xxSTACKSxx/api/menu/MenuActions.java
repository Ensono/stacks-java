package com.xxAMIDOxx.xxSTACKSxx.api.menu;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xxAMIDOxx.xxSTACKSxx.api.ExceptionMessages;
import com.xxAMIDOxx.xxSTACKSxx.api.models.Menu;
import io.restassured.response.Response;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static net.serenitybdd.rest.SerenityRest.lastResponse;

public class MenuActions {

    ObjectMapper objectMapper = new ObjectMapper();


    public String getIdOfLastCreatedObject() {
        return String.valueOf(toJson(lastResponse().getBody().prettyPrint()).get("id"));
    }


    public void check_exception_message(ExceptionMessages parameter) {
        JSONObject js = toJson(lastResponse().getBody().prettyPrint());
        String message = parameter.getMessage();

        Assert.assertTrue(js.get("description").toString().matches(message));
    }


    public JSONObject toJson(String stringToParse) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(stringToParse);
        } catch (JSONException err) {
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


    public Menu mapToMenu(Map<String, String> properties, String id) {
        return new Menu(id, UUID.fromString(properties.get("tenantId")), properties.get("name"),
                properties.get("description"), null, Boolean.parseBoolean(properties.get("enabled")));
    }


    public String createUrlWithCriteria(List<List<String>> data) {
        String finalPath = "";
        for (int i = 0; i < data.size(); i++) {

            String parameter = data.get(i).get(0);
            String value = data.get(i).get(1);

            finalPath += parameter + "=" + value;
            if (i != data.size() - 1) {
                finalPath = finalPath + "&";
            }
        }
        return finalPath;
    }
}
