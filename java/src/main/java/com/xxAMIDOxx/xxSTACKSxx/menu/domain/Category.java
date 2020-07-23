package com.xxAMIDOxx.xxSTACKSxx.menu.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Category {

  private String id;

  private String name;

  private String description;

  private List<Item> items;
}
