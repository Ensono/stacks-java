@Functional
Feature: Update Menu


  Scenario: Update menu - happy path
    Given the application is running
    And the following menu data:
      | name  | description  | tenantId                             | enabled |
      | name8 | Description8 | d211f1ee-6c54-4b01-90e6-d701748f0852 | true    |
    When I create the menu
    Then the menu was successfully created
    And the returned status code is 201

    When I update the menu with the following data:
      | name                       | description                  | tenantId                             | enabled |
      | Update the test menu name7 | Update the test description7 | d211f1ee-6c54-4b01-90e6-d701748f0852 | false   |
    Then the returned status code is 200
    When I search the updated menu
    Then the menu should include the following data:
      | name                       | description                  | tenantId                             | enabled |
      | Update the test menu name7 | Update the test description7 | d211f1ee-6c54-4b01-90e6-d701748f0852 | false   |


  Scenario: Update the non-existing menu
    Given the application is running
    When I update the menu for "a0da8282-bfc6-4cc8-9f83-a111ad0e111c" id with data:
      | name | description      | tenantId                             | enabled |
      | test | Test Description | d211f1ee-6c54-4b01-90e6-d701748f0852 | true    |
    Then the returned status code is 404
    And the 'menu does not exist' message is returned


  Scenario: Bad request for update menu - empty 'name' field
    Given the menu list is not empty
    When I search the menu by:
      | id                                   |
      | f91d2f8c-35cc-45dd-92b0-86ca548e0119 |
    Then the returned status code is 200
    When I update the menu with the following data:
      | name | description                  | tenantId                             | enabled |
      |      | Updated the test description | d211f1ee-6c54-4b01-90e6-d701748f0852 | false   |
    Then the returned status code is 400


  Scenario: Bad request for update menu - empty 'description' field
    Given the menu list is not empty
    When I search the menu by:
      | id                                   |
      | f91d2f8c-35cc-45dd-92b0-86ca548e0119 |
    Then the returned status code is 200
    When I update the menu with the following data:
      | name       | description | tenantId                             | enabled |
      | A new name |             | d211f1ee-6c54-4b01-90e6-d701748f0852 | false   |
    Then the returned status code is 400


  Scenario: Bad request for update menu - empty 'enabled' field
    Given the menu list is not empty
    When I search the menu by:
      | id                                   |
      | f91d2f8c-35cc-45dd-92b0-86ca548e0119 |
    Then the returned status code is 200
    When I update the menu with the following data:
      | name       | description                  | tenantId                             | enabled |
      | A new name | Updated the test description | d211f1ee-6c54-4b01-90e6-d701748f0852 |         |
    Then the returned status code is 400
