package com.xxAMIDOxx.xxSTACKSxx.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.microsoft.azure.spring.data.cosmosdb.core.mapping.Document;
import com.microsoft.azure.spring.data.cosmosdb.core.mapping.PartitionKey;
import org.springframework.data.annotation.Id;

import javax.validation.Valid;
import java.util.List;

@Document(collection = "Menu")
public class Menu implements Comparable<Menu> {

  @Id
  @PartitionKey
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("Name")
  private String name = null;

  @JsonProperty("Description")
  private String description = null;

  @JsonProperty("Categories")
  @Valid
  private List<Category> categories = null;

  @JsonProperty("Enabled")
  private Boolean enabled = null;

  public Menu() {
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public List<Category> getCategories() {
    return categories;
  }

  public void setCategories(List<Category> categories) {
    this.categories = categories;
  }

  public Boolean getEnabled() {
    return enabled;
  }

  public void setEnabled(Boolean enabled) {
    this.enabled = enabled;
  }

  @Override
  public int compareTo(Menu o) {
    return this.getId().compareTo(o.getId());
  }
}
