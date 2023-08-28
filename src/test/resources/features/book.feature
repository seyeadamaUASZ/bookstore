Feature: Verify all books

  Scenario: client make to call all books
    When the client calls api/v1/book
    Then the client receives status code of 200
    And the client receives response data

