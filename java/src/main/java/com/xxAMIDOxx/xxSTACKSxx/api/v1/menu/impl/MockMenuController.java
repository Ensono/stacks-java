package com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.MenuController;
import com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.SearchMenuResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class MockMenuController implements MenuController {

    @Override
    public ResponseEntity<SearchMenuResult> searchMenu(String searchTerm, UUID restaurantId, Integer pageSize, Integer pageNumber) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return new ResponseEntity<>(objectMapper.readValue(
                String.format("{\"pageNumber\" : %d,\"pageSize\" : %d,\"results\" : [ {  \"name\" : \"name\",  \"description\" : \"description\",  \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",  \"restaurantId\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",  \"enabled\" : true}, {  \"name\" : \"name\",  \"description\" : \"description\",  \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",  \"restaurantId\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",  \"enabled\" : true} ]}", pageSize, pageNumber),
                SearchMenuResult.class),
                HttpStatus.NOT_IMPLEMENTED);
    }
}
