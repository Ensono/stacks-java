package com.xxAMIDOxx.xxSTACKSxx.menu.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/** @author ArathyKrishna */
public class MenuHelper {

  public static List<AzureMenu> createMenus(int count) {
    List<AzureMenu> menuList = new ArrayList<>();
    for (int i = 0; i < count; i++) {
      menuList.add(createMenu(i));
    }
    return menuList;
  }

  public static AzureMenu createMenu(int counter) {
    return new AzureMenu(
        UUID.randomUUID().toString(),
        UUID.randomUUID().toString(),
        counter + " Menu",
        counter + " Menu Description",
        new ArrayList<Category>(),
        true);
  }
}
