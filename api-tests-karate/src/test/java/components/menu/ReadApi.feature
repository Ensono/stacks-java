@Functional
Feature: Read API

  Scenario: Check Application Status
    Given url base_url.concat(status)
    And header Authorization = auth.bearer_token
    When method GET
    Then status 200