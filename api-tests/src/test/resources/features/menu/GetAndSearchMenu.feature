@Functional
Feature: Search menu
  Description - These are the scenarios related to getting menu read API

  Scenario: Search menu by multiple criteria
    Given the menu list is not empty
    When I search the menu by criteria
      | pageSize   | 1 |
      | pageNumber | 3 |
    Then the returned status code is 200


  Scenario: Get menu by id
    Given the menu list is not empty
    When I search the menu by:
      | id                                   |
      | f91d2f8c-35cc-45dd-92b0-86ca548e0119 |
    Then the returned status code is 200


  Scenario: Search menu by search term
    Given the menu list is not empty
    When I search the menu by criteria
      | searchTerm | Test |
    Then the returned status code is 200


  Scenario: Search menu by invalid id
    Given the menu list is not empty
    When I search the menu by:
      | id            |
      | InvalidMenuID |
    Then the returned status code is 400


  Scenario: Search menu by non-existent id
    Given the menu list is not empty
    When I search the menu by:
      | id                                   |
      | f91d2f8c-35cc-45dd-11b1-11ca548e1111 |
    Then the returned status code is 404
    And the 'menu does not exist' message is returned


  Scenario: Search menu by incorrect id format
    Given the menu list is not empty
    When I search the menu by:
      | id  |
      | abc |
    Then the returned status code is 400


  Scenario: Search menu by invalid name
    Given the menu list is not empty
    When I search the menu by:
      | name        |
      | InvalidName |
    Then the returned status code is 400


  Scenario: Search menu by restaurant Id
    Given the menu list is not empty
    When I search the menu by criteria
      | restaurantId | d290f1ee-6c54-4b01-90e6-d701748f0851 |
    Then the returned status code is 200
    And the menu should include the following details:
      | id                                   | restaurantId                         | name                 | description                 | enabled |
      | 08068766-ecb7-4034-ba48-485adba2d228 | d290f1ee-6c54-4b01-90e6-d701748f0851 | Name of menu created | Description of menu created | true    |


  Scenario: Get menu by page size
    Given the menu list is not empty
    When I search the menu by criteria
      | pageSize | 2 |
    And the returned status code is 200
    Then the 2 items are returned


  Scenario: Get menu by page size - invalid page size data
    Given the menu list is not empty
    When I search the menu by criteria
      | pageSize | abc |
    Then the returned status code is 400
