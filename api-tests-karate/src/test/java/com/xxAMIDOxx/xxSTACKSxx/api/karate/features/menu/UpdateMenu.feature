@Functional
Feature: Update a menu

  Background: Create menu and Category for future items
     # Create a menu
    * set menu_body
      | path        | value                                   |
      | tenantId    | '74b858a4-d00f-11ea-87d0-0242ac130003'  |
      | name        | 'Italian Cuisine (Automated Test Data)' |
      | description | 'The most delicious Italian dishes'     |
      | enabled     | true                                    |
    * def created_menu = karate.call(read('classpath:CreateGenericData.feature'), {body:menu_body, url:base_url.concat(menu)})
    * karate.set('menu_id',created_menu.id)

  @Smoke
  Scenario Outline: Update menu
    * replace menu_by_id_path.menu_id = karate.get('menu_id')
    * set menu_body
      | path        | value         |
      | tenantId    | <tenantId>    |
      | name        | <name>        |
      | description | <description> |
      | enabled     | <enabled>     |
    Given url base_url.concat(menu_by_id_path)
    And header Authorization = auth.bearer_token
    And request menu_body
    When method PUT
    Then status 200
     # Check the updated menu
    Given url base_url.concat(menu_by_id_path)
    And header Authorization = auth.bearer_token
    When method GET
    Then status 200
    And response.name == "<name>"
    And response.description == "<description>"
    And response.tenantId == "<tenantId>"
    And response.enabled == <enabled>
    * karate.call(read('classpath:DeleteCreatedMenus.feature'), {menuId:karate.get('menu_id')})

    Examples:
      | tenantId                               | name                                      | description                            | enabled |
      | '74b858a4-d00f-11ea-87d0-0242ac130003' | 'Updated Menu Name (Automated Test Data)' | 'The most delicious vegetarian dishes' | true    |


  Scenario Outline: Update menu - empty mandatory fields
    * replace menu_by_id_path.menu_id = karate.get('menu_id')
    * set menu_body
      | path        | value         |
      | tenantId    | <tenantId>    |
      | name        | <name>        |
      | description | <description> |
      | enabled     | <enabled>     |
    Given url base_url.concat(menu_by_id_path)
    And header Authorization = auth.bearer_token
    And request menu_body
    When method PUT
    Then status 400
    * match response.description contains "Invalid Request"
    * karate.call(read('classpath:DeleteCreatedMenus.feature'), {menuId:karate.get('menu_id')})

    Examples:
      | tenantId                               | name                                      | description                            | enabled |
      | '74b858a4-d00f-11ea-87d0-0242ac130003' | ''                                        | 'The most delicious vegetarian dishes' | true    |
      | '74b858a4-d00f-11ea-87d0-0242ac130003' | 'Updated Menu Name (Automated Test Data)' | ''                                     | true    |
      | '74b858a4-d00f-11ea-87d0-0242ac130003' | 'Updated Menu Name (Automated Test Data)' | 'The most delicious vegetarian dishes' | ''      |


  Scenario Outline: Update menu -  Resource not found
    * replace menu_by_id_path.menu_id = <menuId>
    * set menu_body
      | path        | value                                     |
      | tenantId    | '74b858a4-d00f-11ea-87d0-0242ac130003'    |
      | name        | 'Updated Menu Name (Automated Test Data)' |
      | description | 'The most delicious vegetarian dishes'    |
      | enabled     | true                                      |
    Given url base_url.concat(menu_by_id_path)
    And header Authorization = auth.bearer_token
    And request menu_body
    When method PUT
    Then status 404
    * replace menu_does_not_exists
      | token     | value    |
      | <menu_id> | <menuId> |
    * match response.description contains menu_does_not_exists
    Examples:
      | menuId                                 |
      | '5cb0819d-1e7b-446c-b062-da6a8c593b00' |
