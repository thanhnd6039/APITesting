Feature: End to End the tests
  Description: The purpose of these tests are to cover End to End happy flows for customer

  Background: User generates token for authorization
    Given I am an authorized user

    Scenario: Authorized user is able to add and remove a book
      Given A list of books are available
      When I add a book to my reading list
      Then The book is added
      When I remove a book from my reading list
      Then The book is removed
