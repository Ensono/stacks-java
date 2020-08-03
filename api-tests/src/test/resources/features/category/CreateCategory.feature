@DeleteCreatedMenu
@Functional
Feature: Create categories

  Background: Create menu in the background before the scenarios
     # 1. Create menu
    Given the application is running
    And the following menu data:
      | name      | description    | tenantId                             | enabled |
      | Le Renoir | French Cuisine | d211f1ee-6c56-4b01-90e6-d701748f5656 | true    |
    When I create the menu
    Then the menu was successfully created
    And the returned status code is 201

  @Smoke
  Scenario: Create the category - Happy path
     # 2. Create category
    Given the following category data:
      | name          | description                                  |
      | Fish Category | This compartment contain all fish delicacies |
    When I create a new category for the existing menu
    Then the category was successfully created
    And the returned status code is 201
    And the created category should include the following data:
      | name          | description                                  |
      | Fish Category | This compartment contain all fish delicacies |


  Scenario: Create category that already exist
    Given the following category data:
      | name          | description                                  |
      | Meat Category | This compartment contain all meat delicacies |
    When I create a new category for the existing menu
    Then the category was successfully created
    And the returned status code is 201
    When the following category data:
      | name          | description                                  |
      | Meat Category | This compartment contain all meat delicacies |
    When I create a new category for the existing menu
    And the returned status code is 409


  Scenario: Bad request for create category - 'name' field is empty
    Given the following category data:
      | name | description                      |
      |      | Description of category created2 |
    When I create a new category for the existing menu
    And the returned status code is 400


  Scenario: Bad request for create category - 'description' field is empty
    Given the following category data:
      | name              | description |
      | Surprise Category |             |
    When I create a new category for the existing menu
    And the returned status code is 400


  Scenario: Create category for non-existing menu
    Given the following category data:
      | name                      | description                      |
      | Name of category created2 | Description of category created2 |
    When I create a new category for the menu with "11e11aa-e1d1-1111-1111-81b0810e6b22" id
    And the returned status code is 404
    Then the 'menu does not exist' message is returned
