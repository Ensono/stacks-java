@Functional
Feature: Create categories

  @Ignore
  Scenario: Create category for an existing menu
    Given the menu list is not empty
    When I search the menu by:
      | id                                   |
      | f91d2f8c-35cc-45dd-92b0-86ca548e0119 |
    Then the returned status code is 200

    Given the fallowing category data:
      | name                       | description                       |
      | Name of category created16 | Description of category created16 |
    When I create a new category for the existing menu
    Then the category was successfully created
    And the returned status code is 201

#    When I check the new category in the menu
#    Then the category should include the following data:
#      | name                       | description                       |
#      | Name of category created16 | Description of category created16 |

  @Ignore
  Scenario: Create category that already exist
    Given the menu list is not empty
    When I search the menu by:
      | id                                   |
      | bffeb312-ac48-460a-95a9-3b3956c8a117 |
    Then the returned status code is 200
    Given the fallowing category data:
      | name                      | description                      |
      | Name of category created2 | Description of category created2 |
    When I create a new category for the existing menu
    Then the category was successfully created
    And the returned status code is 201
    When the fallowing category data:
      | name                      | description                      |
      | Name of category created2 | Description of category created2 |
    When I create a new category for the menu with "bffeb312-ac48-460a-95a9-3b3956c8a117" id
    And the returned status code is 409


  Scenario: Bad request for create category - 'name' field is empty
    Given the menu list is not empty
    When I search the menu by:
      | id                                   |
      | bffeb312-ac48-460a-95a9-3b3956c8a117 |
    Then the returned status code is 200
    Given the fallowing category data:
      | name | description                      |
      |      | Description of category created2 |
    When I create a new category for the existing menu
    And the returned status code is 400


  Scenario: Bad request for create category - 'description' field is empty
    Given the menu list is not empty
    When I search the menu by:
      | id                                   |
      | bffeb312-ac48-460a-95a9-3b3956c8a117 |
    Then the returned status code is 200
    Given the fallowing category data:
      | name      | description |
      | Test Name |             |
    When I create a new category for the existing menu
    And the returned status code is 400


  Scenario: Create category for non-existing menu
    Given the fallowing category data:
      | name                      | description                      |
      | Name of category created2 | Description of category created2 |
    When I create a new category for the menu with "11e11aa-e1d1-1111-1111-81b0810e6b22" id
    And the returned status code is 404
    Then the 'menu does not exist' message is returned
