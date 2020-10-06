@Functional
Feature: Delete item

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

#    create an item
    * set item_body
      | path        | value                                               |
      | name        | 'Tender chicken breast with aubergine and tomatoes' |
      | description | 'Juicy chicken breast'                              |
      | price       | 8.5                                                 |
      | available   | true                                                |
    * replace category_by_id_path
      | token         | value                     |
      | <menu_id>     | karate.get('menu_id')     |
      | <category_id> | karate.get('category_id') |
    Given url base_url.concat(category_by_id_path).concat(items)
    And request item_body
    When method POST
    Then status 201
    * karate.set('item_id',response.id)


  @Smoke
  Scenario: Delete created item
    * replace item_by_id_path
      | token         | value                     |
      | <menu_id>     | karate.get('menu_id')     |
      | <category_id> | karate.get('category_id') |
      | <item_id>     | karate.get('item_id')     |

    #   Delete the created item
    Given url base_url.concat(item_by_id_path)
    When method DELETE
    Then status 200

    #   Check the Deleted item
    Given url base_url.concat(menu_by_id_path)
    When method GET
    Then status 200
    * def categories_list = response.categories
    * match categories_list[0].items.size() == 0


  Scenario Outline: Remove an item that does not exist
    * replace item_by_id_path
      | token         | value                     |
      | <menu_id>     | karate.get('menu_id')     |
      | <category_id> | karate.get('category_id') |
      | <item_id>     | <itemId>                  |
    #   Delete the item
    Given url base_url.concat(item_by_id_path)
    When method DELETE
    Then status 404
    * replace item_does_not_exists
      | token         | value                     |
      | <menu_id>     | karate.get('menu_id')     |
      | <category_id> | karate.get('category_id') |
      | <item_id>     | <itemId>                  |
    * match response.description contains item_does_not_exists
    Examples:
      | itemId                                 |
      | 'f91d2f8c-35cc-45dd-92b0-86ca548e0119' |


  Scenario Outline: Remove an item for a category that does not exist
    * replace item_by_id_path
      | token         | value                 |
      | <menu_id>     | karate.get('menu_id') |
      | <category_id> | <categoryId>          |
      | <item_id>     | karate.get('item_id') |

    #   Delete the created item
    Given url base_url.concat(item_by_id_path)
    When method DELETE
    Then status 404
    * replace category_does_not_exists
      | token         | value                 |
      | <menu_id>     | karate.get('menu_id') |
      | <category_id> | <categoryId>          |
    * match response.description contains category_does_not_exists
    Examples:
      | categoryId                             |
      | 'f91d2f8c-35cc-45dd-92b0-86ca548e0119' |


  Scenario Outline: Remove an item for a menu that does not exist
    * replace item_by_id_path
      | token         | value                     |
      | <menu_id>     | <menuId>                  |
      | <category_id> | karate.get('category_id') |
      | <item_id>     | karate.get('item_id')     |

    #   Delete the item
    Given url base_url.concat(item_by_id_path)
    When method DELETE
    Then status 404
    * replace menu_does_not_exists
      | token     | value    |
      | <menu_id> | <menuId> |
    * match response.description contains menu_does_not_exists
    Examples:
      | menuId                                 |
      | 'f91d2f8c-35cc-45dd-92b0-86ca548e0119' |


  Scenario Outline: Bad Request - invalid item id
    * replace item_by_id_path
      | token         | value                     |
      | <menu_id>     | karate.get('menu_id')     |
      | <category_id> | karate.get('category_id') |
      | <item_id>     | <itemId>                  |

    #   Delete the item
    Given url base_url.concat(item_by_id_path)
    When method DELETE
    Then status <status_code>

    Examples:
      | itemId            | status_code |
      | 'InvalidIdFormat' | 400         |
      | ''                | 405         |
