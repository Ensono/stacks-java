package com.xxAMIDOxx.xxSTACKSxx.api.category;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xxAMIDOxx.xxSTACKSxx.api.models.Category;
import io.restassured.response.Response;

import java.util.Map;

public class CategoryActions {

    ObjectMapper objectMapper = new ObjectMapper();

    public Category mapToCategory(Map<String, String> properties, String id) {
        return new Category(id, properties.get("name"), properties.get("description"), null);
    }

    public Category responseToCategory(Response response) {
        Category actualCategory = null;
        try {
            actualCategory = objectMapper.readValue(response.body().print(), Category.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return actualCategory;
    }
}
