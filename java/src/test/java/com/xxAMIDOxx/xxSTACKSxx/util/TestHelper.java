package com.xxAMIDOxx.xxSTACKSxx.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xxAMIDOxx.xxSTACKSxx.model.Menu;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.xxAMIDOxx.xxSTACKSxx.builder.MenuBuilder.aMenu;

/**
 * @author ArathyKrishna
 */
public class TestHelper {

    public static String toJson(ObjectMapper mapper, Object o) throws JsonProcessingException {
        return mapper.writeValueAsString(o);
    }

    public static String getBaseURL(int port) {
        return String.format("http://localhost:%d", port);
    }

    public static List<Menu> createMenus(int count) {
        List<Menu> menuList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            menuList.add(createMenu(i));
        }
        return menuList;
    }

    public static Menu createMenu(int counter) {
        return aMenu()
                .withDescription(counter + " Menu Description")
                .withEnabled(true)
                .withName(counter + " Menu")
                .withId(UUID.randomUUID().toString())
                .withRestaurantId(UUID.randomUUID())
                .build();
    }
}
