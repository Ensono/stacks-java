@Functional
Feature: Create a new menu


#  Scenario: Create menu - Happy path
#    Given the application is running
#    And the following menu data:
#      | name                 | description      | tenantId                             | enabled |
#      | A new test menu name | Test Description | d211f1ee-6c54-4b01-90e6-d701748f0852 | true    |
#    When I create the menu
#    Then the menu was successfully created
#    And the returned status code is 201
#    When I search the created menu
#    Then the menu should include the following data:
#      | name                 | description      | tenantId                             | enabled |
#      | A new test menu name | Test Description | d211f1ee-6c54-4b01-90e6-d701748f0852 | true    |


  Scenario: Bad request for create menu - 404 error message
    Given the application is running
    And the following menu data:
      | name                        | description      | tenantId                             | enabled |
      | Test Menu - 404 Bad Request | Test Description | d211f1ee-6c54-4b01-90e6-d701748f0111 |         |
    When I create the menu
    Then the returned status code is 400


  Scenario: Bad request for create menu - invalid tenant Id
    Given the application is running
    And the following menu data:
      | name                          | description      | tenantId | enabled |
      | Test menu - invalid tenant Id | Test Description | d29      | true    |
    When I create the menu
    Then the returned status code is 400


  Scenario: Bad request for create menu - empty tenant Id
    Given the application is running
    And the following menu data:
      | name                        | description      | tenantId | enabled |
      | Test menu - empty tenant Id | Test Description |          | true    |
    When I create the menu
    Then the returned status code is 400

  @Ignore
  Scenario: Create menu with the same data - 409
    Given the application is running
    And the following menu data:
      | name           | description       | tenantId                             | enabled |
      | Test Menu Name | Test Description1 | d211f1ee-6c54-4b01-90e6-d701748f0111 | true    |
    When I create the menu
    Then the menu was successfully created
    And the returned status code is 201
    When the following menu data:
      | name              | description      | tenantId                             | enabled |
      | VZ test menu name | Test Description | d211f1ee-6c54-4b01-90e6-d701748f0852 | true    |
    And I create the menu
    Then the returned status code is 409
    And the 'menu already exist' message is returned
