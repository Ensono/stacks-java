@Functional
Feature: Create Items

  Background: Create menu and Category for future items
    * karate.call('classpath:CleanUpTestData.feature')
# Create a menu
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


# Create category
    * replace category.menu_id = karate.get('menu_id')
    * set category_body
      | path        | value                                                       |
      | name        | 'Meat plates (Automated Test Data)'                         |
      | description | 'This category contains all possible ways of meat cooking.' |
    Given url base_url.concat(category)
    And request category_body
    When method POST
    Then status 201
    * karate.set('category_id',response.id)


  @Smoke
  Scenario Outline: Create Item
    * set item_body
      | path        | value          |
      | name        | <name>         |
      | description | <description>  |
      | price       | <price>        |
      | available   | <availability> |
    * replace category_by_id_path
      | token         | value                     |
      | <menu_id>     | karate.get('menu_id')     |
      | <category_id> | karate.get('category_id') |
#    Create item for category
    Given url base_url.concat(category_by_id_path).concat(items)
    And request item_body
    When method POST
    Then status 201
    * def item_id = response.id
#    Check the created item
    * replace menu_by_id_path.menu_id = karate.get('menu_id')
    Given url base_url.concat(menu_by_id_path)
    When method GET
    Then status 200
    * def items_list = []
    * def items_list = response.categories[0].items
    * match items_list.size() == 1
    * match items_list[0].name == <name>
    * match items_list[0].description == <description>
    * match items_list[0].available == <availability>

    Examples:
      | name                                                | description            | price | availability |
      | 'Tender chicken breast with aubergine and tomatoes' | 'Juicy chicken breast' | 8.5   | true         |


  Scenario Outline: Create an item - 409 Code the item already exists
    * set item_body
      | path        | value          |
      | name        | <name>         |
      | description | <description>  |
      | price       | <price>        |
      | available   | <availability> |
    * replace category_by_id_path
      | token         | value                     |
      | <menu_id>     | karate.get('menu_id')     |
      | <category_id> | karate.get('category_id') |
#    Create item for category
    Given url base_url.concat(category_by_id_path).concat(items)
    And request item_body
    When method POST
    Then status 201
    * def item_id = response.id
#    Create the same item twice
    And request item_body
    When method POST
    Then status 409
    * match response.description contains "An item with the name <name> already exists"

    Examples:
      | name                                                | description            | price | availability |
      | 'Tender chicken breast with aubergine and tomatoes' | 'Juicy chicken breast' | 8.5   | true         |


  Scenario Outline: Create an item - 400 Bad request for invalid '<field_name>' field
    * set item_body
      | path        | value          |
      | name        | <name>         |
      | description | <description>  |
      | price       | <price>        |
      | available   | <availability> |
    * replace category_by_id_path
      | token         | value                     |
      | <menu_id>     | karate.get('menu_id')     |
      | <category_id> | karate.get('category_id') |
#    Create item for category
    Given url base_url.concat(category_by_id_path).concat(items)
    And request item_body
    When method POST
    Then status 400
    And match response.description contains "Invalid Request"

    Examples:
      | field_name   | name             | description            | price | availability | categoryId                             |
      | name         | ''               | 'Juicy chicken breast' | 8.5   | true         | 'c4099d5b-314e-4a37-bb4b-526aff00cb09' |
      | description  | 'Tender chicken' | ''                     | 8.5   | true         | 'c4099d5b-314e-4a37-bb4b-526aff00cb09' |
      | price        | 'Tender chicken' | 'Juicy chicken breast' |       | true         | 'c4099d5b-314e-4a37-bb4b-526aff00cb09' |
      | availability | 'Tender chicken' | 'Juicy chicken breast' | 8.5   |              | 'c4099d5b-314e-4a37-bb4b-526aff00cb09' |


  Scenario Outline: Create an item - 404 Code - the menu does not exist
    * set item_body
      | path        | value          |
      | name        | <name>         |
      | description | <description>  |
      | price       | <price>        |
      | available   | <availability> |
    * replace category_by_id_path
      | token         | value                     |
      | <menu_id>     | <id>                      |
      | <category_id> | karate.get('category_id') |
#    Create item for category
    Given url base_url.concat(category_by_id_path).concat(items)
    And request item_body
    When method POST
    Then status 404
    * match response.description contains "A menu with id <id> does not exist."

    Examples:
      | name             | description            | price | availability | id                                     |
      | 'Tender chicken' | 'Juicy chicken breast' | 8.5   | true         | 'c4099d5b-314e-4a37-bb4b-526aff00cb09' |


  Scenario Outline: Scenario: Create an item - 404 Code - the category does not exist
    * set item_body
      | path        | value          |
      | name        | <name>         |
      | description | <description>  |
      | price       | <price>        |
      | available   | <availability> |
    * replace category_by_id_path
      | token         | value                 |
      | <menu_id>     | karate.get('menu_id') |
      | <category_id> | <id>                  |
#    Create item for category
    Given url base_url.concat(category_by_id_path).concat(items)
    And request item_body
    When method POST
    Then status 404
    * match response.description contains "A category with the id <id> does not exist"

    Examples:
      | name             | description            | price | availability | id                                     |
      | 'Tender chicken' | 'Juicy chicken breast' | 8.5   | true         | 'c1111d5b-314e-4a37-bb4b-526aff00cb09' |
