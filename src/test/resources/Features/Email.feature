Feature: Email Functionality
  Background: Login Component
    Given I am on the login page
    And I wait for 2 seconds
    Then I verify Patronum home page is displayed


  @20
  Scenario: Verify Email for Groups - deletion request has received to Admin by applying filter
    When I click the "Groups" link
    And I wait for 1 seconds
    And I click the "add Add a filter" button
    And I select "Name" from filter
    And I enter "gupta" in the "Start with" field
    And I click the "Done" button
    And I select following 26 rows
      | 1  |
      | 3  |
      | 4  |
      | 6  |
      | 7  |
      | 5  |
      | 9  |
      | 11 |
      | 15 |
      | 12 |
      | 13 |
      | 10 |
      | 14 |
      | 16 |
      | 17 |
      | 18 |
      | 20 |
      | 19 |
      | 22 |
      | 21 |
      | 23 |
      | 24 |
      | 25 |
      | 26 |
      | 27 |
      | 28 |
    Then I verify total groups selected should display at the top of grid
    And I wait for 1 seconds
    And I click the "delete_outline" button
    And I wait for 1 seconds
    And I verify Delete group popup is displayed
    And I click the "Yes, delete this group" button
    And I wait for 1 seconds
    Then I verify "An approval request has been sent to the admin." popup is displayed
    And I verify the email for "Group - Deletion Request" was received

  @21
  Scenario: Verify Email for Groups - deletion request has received to Admin without applying filter
    When I click the "Groups" link
    And I wait for 1 seconds
    And I scroll down to the bottom of webpage
    And I select following 26 rows
      | 1  |
      | 3  |
      | 4  |
      | 6  |
      | 7  |
      | 5  |
      | 9  |
      | 11 |
      | 15 |
      | 12 |
      | 13 |
      | 10 |
      | 14 |
      | 16 |
      | 17 |
      | 18 |
      | 20 |
      | 19 |
      | 22 |
      | 21 |
      | 23 |
      | 24 |
      | 25 |
      | 26 |
      | 27 |
      | 28 |
    Then I verify total groups selected should display at the top of grid
    And I wait for 1 seconds
    And I click the "delete_outline" button
    And I wait for 1 seconds
    And I verify Delete group popup is displayed
    And I click the "Yes, delete this group" button
    And I wait for 1 seconds
    Then I verify "An approval request has been sent to the admin." popup is displayed
    And I verify the email for "Group - Deletion Request" was received

  @22
  Scenario: Verify Email for Policy - deletion request has received to Admin without applying filter
    When I click the "Policies" link
    And I wait for 1 seconds
    And I select following 20 rows
      | 1  |
      | 3  |
      | 4  |
      | 6  |
      | 7  |
      | 5  |
      | 9  |
      | 11 |
      | 15 |
      | 12 |
      | 13 |
      | 10 |
      | 14 |
      | 16 |
      | 17 |
      | 18 |
      | 20 |
      | 19 |
      | 22 |
      | 21 |
    Then I verify total groups selected should display at the top of grid
    And I wait for 1 seconds
    And I click the "delete_outline" button
    And I wait for 1 seconds
    And I verify "Delete Policy" popup is displayed with "Cancel" & "OK" buttons
    And I click the "OK" button
    And I wait for 1 seconds
    Then I verify "An approval request has been sent to the admin." popup is displayed
    And I verify the email for "Policy - Deletion Request" was received

  @23
  Scenario: Verify Email for Policy - deletion request has received to Admin by applying filter
    When I click the "Policies" link
    And I wait for 1 seconds
    And I click the "add Add a filter" button
    And I select "Name" from filter
    And I enter "TestPolicy" in the "Start with" field
    And I click the "Done" button
    And I select following 24 rows
      | 1  |
      | 3  |
      | 4  |
      | 6  |
      | 7  |
      | 5  |
      | 9  |
      | 11 |
      | 15 |
      | 12 |
      | 13 |
      | 10 |
      | 14 |
      | 16 |
      | 17 |
      | 18 |
      | 20 |
      | 19 |
      | 22 |
      | 21 |
      | 23 |
      | 24 |
      | 25 |
      | 26 |
    Then I verify total groups selected should display at the top of grid
    And I wait for 1 seconds
    And I click the "delete_outline" button
    And I wait for 1 seconds
    And I verify "Delete Policy" popup is displayed with "Cancel" & "OK" buttons
    And I click the "OK" button
    And I wait for 1 seconds
    Then I verify "An approval request has been sent to the admin." popup is displayed
    And I verify the email for "Policy - Deletion Request" was received