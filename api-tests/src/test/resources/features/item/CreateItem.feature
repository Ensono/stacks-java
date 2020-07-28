@Functional
Feature: Add menu item


  Scenario: Create an item for the menu
    Given the following item data:
      | name       | description      | price | available |
      | Item Name4 | Item Description | 1.5   | true      |
    When I create an item for the following menu and category:
      | menuId     | f91d2f8c-35cc-45dd-92b0-86ca548e0119 |
      | categoryId | 1c4f223f-8c9a-47bd-9f06-381a658a41f5 |
    Then the item was successfully created
    And the returned status code is 201
    And the created item should include the following data:
      | name       | description      | price | available |
      | Item Name4 | Item Description | 1.5   | true      |


  Scenario: Create an item - 409 Code the item already exists
    Given the following item data:
      | name       | description       | price | available |
      | Item Name4 | Item Description4 | 1.5   | true      |
    When I create an item for the following menu and category:
      | menuId     | f91d2f8c-35cc-45dd-92b0-86ca548e0119 |
      | categoryId | 1c4f223f-8c9a-47bd-9f06-381a658a41f5 |
    Then the returned status code is 409
    And the 'item already exist' message is returned


  Scenario: Create an item - 400 Bad request for empty 'category id' field
    Given the following item data:
      | name           | description           | price | available |
      | Test Item Name | Test Item Description | 1.5   | true      |
    When I create an item for the following menu and category:
      | menuId     | f91d2f8c-35cc-45dd-92b0-86ca548e0119 |
      | categoryId |                                      |
    Then the returned status code is 400


  Scenario: Create an item - 400 Bad request for empty 'menu id' field
    Given the following item data:
      | name       | description | price | available |
      | Item Name4 | Item Name4  | 1.5   | true      |
    When I create an item for the following menu and category:
      | menuId     |                                      |
      | categoryId | 1c4f223f-8c9a-47bd-9f06-381a658a41f5 |
    Then the returned status code is 400


  Scenario: Create an item - 404 Code for the non-existent menu
    Given the following item data:
      | name       | description       | price | available |
      | Item Name4 | Item Description4 | 1.5   | true      |
    When I create an item for the following menu and category:
      | menuId     | f91d2f8c-35cc-45dd-92b0-86ca548e0119 |
      | categoryId | 1c4f223f-8c9a-47bd-9f06-381a658a41f5 |
    Then the returned status code is 409
    And the 'item already exist' message is returned


  Scenario: Create an item - 404 Code - the menu does not exist
    Given the following item data:
      | name       | description       | price | available |
      | Item Name4 | Item Description4 | 1.5   | true      |
    When I create an item for the following menu and category:
      | menuId     | f11d1f1c-11cc-11dd-11b0-86ca548e0119 |
      | categoryId | 1c4f223f-8c9a-47bd-9f06-381a658a41f5 |
    Then the returned status code is 404
    And the 'menu does not exist' message is returned


  Scenario: Create an item - 404 Code - the category does not exist
    Given the following item data:
      | name       | description       | price | available |
      | Item Name4 | Item Description4 | 1.5   | true      |
    When I create an item for the following menu and category:
      | menuId     | f91d2f8c-35cc-45dd-92b0-86ca548e0119 |
      | categoryId | 1c4f111f-1c9a-11bd-9f11-381a658a41f5 |
    Then the returned status code is 404
