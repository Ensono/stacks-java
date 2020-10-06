@Functional
Feature: Delete category

  Background: Create a menu and category
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

#    create category
    * set category_body
      | path        | value                                                       |
      | name        | 'Meat plates (Automated Test Data)'                         |
      | description | 'This category contains all possible ways of meat cooking.' |

    Given url base_url.concat(category)
    And request category_body
    When method POST
    Then status 201
    * karate.set('category_id',response.id)
    * replace category_by_id_path
      | token         | value                 |
      | <menu_id>     | karate.get('menu_id') |
      | <category_id> | response.id           |

  @Smoke
  Scenario: Delete created category
    Given url base_url.concat(category_by_id_path)
    When method DELETE
    Then status 200

    #   Check the deleted category
    Given url base_url.concat(menu_by_id_path)
    When method GET
    Then status 200
    * match response.categories.size() == 0


  Scenario Outline: Remove a category - Bad request
    Given url base_url.concat(category).concat('/').concat(<categoryId>)
    When method DELETE
    Then status 404
    * replace category_does_not_exists
      | token          | value                 |
      | <menu_id>     | karate.get('menu_id') |
      | <category_id> | <categoryId>         |
    * match response.description contains category_does_not_exists

    Examples:
      | categoryId                            |
      | 'f91d2f8c-35cc-45dd-92b0-86ca548e0119' |


  Scenario Outline: Remove a category from the menu that does not exist
    Given url base_url.concat(menu).concat('/').concat(<id>).concat('/category/').concat(karate.get('category_id'))
    When method DELETE
    Then status 404
    * replace menu_does_not_exists
      | token     | value |
      | <menu_id> | <id>  |
    * match response.description contains menu_does_not_exists

    Examples:
      | id                                     |
      | 'f91d2f8c-35cc-45dd-92b0-86ca548e0119' |
