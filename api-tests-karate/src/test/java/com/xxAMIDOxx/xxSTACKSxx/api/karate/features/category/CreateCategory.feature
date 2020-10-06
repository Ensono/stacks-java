@Functional
Feature: Create a category

  Background: Create a menu
    * karate.call('classpath:CleanUpTestData.feature')
    * set menu_body
      | path        | value                                   |
      | tenantId    | '74b858a4-d00f-11ea-87d0-0242ac130003'  |
      | name        | 'Italian Cuisine (Automated Test Data)' |
      | description | 'The most delicious Italian dishes'     |
      | enabled     | true                                    |
    Given url base_url.concat(menu)
    And request menu_body
    When method POST
    Then status 201
    * karate.set('menu_id',response.id)
    * replace menu_by_id_path.menu_id = response.id
    * replace category.menu_id = karate.get('menu_id')


  @Smoke
  Scenario Outline: Create a category for menu
    * set category_body
      | path        | value         |
      | name        | <name>        |
      | description | <description> |
    Given url base_url.concat(category)
    And request category_body
    When method POST
    Then status 201
    * karate.set('category_id',response.id)

#   Check the created category
    Given url base_url.concat(menu_by_id_path)
    When method GET
    Then status 200
    * def category_list = []
    * def category_list = response.categories
    * match category_list.size() == 1
    * match category_list[0].name == <name>
    * match category_list[0].description == <description>

    Examples:
      | name                                | description                                                 |
      | 'Meat plates (Automated Test Data)' | 'This category contains all possible ways of meat cooking.' |


  Scenario Outline: Create a category that already exists
    * set category_body
      | path        | value         |
      | name        | <name>        |
      | description | <description> |
    Given url base_url.concat(category)
    And request category_body
    When method POST
    Then status 201
#  Create a category with the same data
    And request category_body
    When method POST
    Then status 409
    * match response.description contains "A category with the name <name> already exists"

    Examples:
      | name                                | description                                                 |
      | 'Meat plates (Automated Test Data)' | 'This category contains all possible ways of meat cooking.' |


  Scenario Outline: Bad request for creating category - '<field_name>' field is empty
    * set category_body
      | path        | value         |
      | name        | <name>        |
      | description | <description> |
    Given url base_url.concat(category)
    And request category_body
    When method POST
    Then status 400
    * match response.description contains "Invalid Request"

    Examples:
      | field_name  | name                                | description                                                 |
      | name        | 'Meat plates (Automated Test Data)' |                                                             |
      | description |                                     | 'This category contains all possible ways of meat cooking.' |


  Scenario Outline: Create a category for non-existing menu
    * set category_body
      | path        | value         |
      | name        | <name>        |
      | description | <description> |
    Given url base_url.concat(menu).concat('/').concat(<id>).concat('/category')
    And request category_body
    When method POST
    Then status 404
    * match response.description contains "A menu with id <id> does not exist."

    Examples:
      | name                                | description   | id                                     |
      | 'Meat plates (Automated Test Data)' | 'description' | '111e11aa-e1d1-1111-1111-81b0810e6b22' |
