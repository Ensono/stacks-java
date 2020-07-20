package com.xxAMIDOxx.xxSTACKSxx.menu.domain;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.xxAMIDOxx.xxSTACKSxx.builder.MenuBuilder.aMenu;

/**
 * @author ArathyKrishna
 */
public class MenuHelper {

    public static List<Menu> createMenus(int count) {
        List<Menu> menuList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            menuList.add(createMenu(i));
        }
        return menuList;
    }

    public static Menu createMenu(int counter) {
        return aMenu()
                .withDescription(counter + " MenuDTO Description")
                .withEnabled(true)
                .withName(counter + " MenuDTO")
                .withId(UUID.randomUUID().toString())
                .withRestaurantId(UUID.randomUUID().toString())
                .build();
    }
}
