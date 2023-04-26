Feature: Filter Functionality

  Background: Login Component
    Given I am on the login page
    And I wait for 5 seconds
    Then I verify Patronum home page is displayed
    When I click the "Groups" link

  @82
  Scenario: Groups- All filter option functionality
    And I click the "add Add a filter" button
    And I select "Name" from filter
    And I enter "gupta" in the "Start with" field
    And I click the "Done" button
    And I click the "close" button of filter
    And I click the "add Add a filter" button
    And I select "Orphan Groups" from filter
    And I click the "close" button of filter
    And I click the "add Add a filter" button
    And I select "External Members" from filter
    And I click the "close" button of filter
    And I click the "add Add a filter" button
    And I select "Owner" from filter
    And I enter "gupta" in the "Start with" field
    And I click the "Done" button
    And I click the "close" button of filter

  @83
  Scenario: Groups- (Name) option filter
    And I click the "add Add a filter" button
    And I select "Name" from filter
    And I enter "gupta" in the "Start with" field
    And I click the "Done" button
    Then I verify that value of "Groups" for "row 1" is "gupta"
    And I click "selectAllGroups" checkbox in the header
    Then I verify total groups selected should display at the top of grid


  @84
  Scenario: Groups- (Name) option filter-verify results by entering random string/special characters in start with field
    And I click the "add Add a filter" button
    And I select "Name" from filter
    And I enter "abcd" in the "Start with" field
    And I click the "Done" button
    Then There is no result


  @85
  Scenario: Master select functionality of Groups
    And I click "selectAllGroups" checkbox in the header
    And I wait for 1 seconds
    And I scroll down to the bottom of webpage
    And I wait for 1 seconds
    And I scroll down to the bottom of webpage
    Then I verify all visible records get selected


  @86
  Scenario: Verify user is able to Select random groups
    And I select row 1 checkbox
    And I select row 3 checkbox
    And I select row 5 checkbox
    And I select row 6 checkbox
    And I select row 8 checkbox
    And I select row 10 checkbox
    And I select row 2 checkbox
    And I select row 12 checkbox
    And I select row 15 checkbox
    And I select row 14 checkbox
    And I select row 11 checkbox
    And I wait for 1 seconds
    Then I verify total groups selected should display at the top of grid


  @87
  Scenario: Clear Selection functionality
    And I select row 1 checkbox
    And I click the "close" button
    And I wait for 1 seconds
    Then I verify row 1 checkbox is deselected
    And I click "selectAllGroups" checkbox in the header
    Then I verify all visible records get selected
    And I click the "close" button
    Then I verify all the selected records get deselected


  @88
  Scenario: Action column-copy and delete button display
    And I move to row 2 in "Groups" column in grid record
    And I wait for 1 seconds
    Then I verify following buttons are displayed in Action column
      | file_copy_outline |
      | delete_outline    |


  @89
  Scenario: Verify total groups selected should display at the top of grid
    And I click "selectAllGroups" checkbox in the header
    And I scroll down to the bottom of webpage
    And I wait for 2 seconds
    And I scroll down to the bottom of webpage
    And I wait for 2 seconds
    And I scroll down to the bottom of webpage
    And I wait for 2 seconds
    And I scroll down to the bottom of webpage
    And I wait for 2 seconds
    And I scroll up
    And I wait for 1 seconds
    Then I verify total groups selected should display at the top of grid


  @90
  Scenario: Verify the selected groups gets deselected by refreshing the page
    And I click "selectAllGroups" checkbox in the header
    Then I verify all visible records get selected
    And I refresh the page
    Then I verify all the selected records get deselected