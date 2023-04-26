Feature: Groups Deletion functionality
  Background: Login component
    Given I am on the login page
    And I wait for 1 seconds
#    When I click on the "Sign in with Google" link
#    And I wait for 1 seconds
#    And I enter "valid user" email address
#    And I click the "Next" button
#    And I wait for 2 seconds
#    And I enter "valid user" password
#    And I click the "Next" button
#    And I wait for 2 seconds
#    And I login into the patronum app as "Super Admin"
    Then I verify Patronum home page is displayed
#    And The following elements exist
#      | People        |
#      | Groups        |
#      | Contacts      |
#      | Policies      |
#      | Roles         |
#      | Setting       |
#      | notifications |
#      | apps          |
    When I click the "Groups" link
    And I verify I am on the "Patronum - Groups" page
    And I click the "add Add a filter" button
    And I select "Name" from filter
    And I enter "gupta" in the "Start with" field
    And I click the "Done" button
    And I wait for 1 seconds

  @7
  Scenario: Verify Single group deletion
    And I select row 1 checkbox
    And I store selected "Groups" of "Groups" column
    And I click the "delete_outline" button
    And I verify Delete group popup is displayed
    And I click the "Yes, delete this group" button
    Then I verify "1 group deleted successfully." popup is displayed
    And I wait for 2 seconds
    And I click the "close" button of filter
    And I click the "add Add a filter" button
    And I select "Name" from filter
    And I enter "gupta" in the "Start with" field
    And I click the "Done" button
    And I wait for 1 seconds
    Then I verify selected groups are deleted from "Groups" column and from grid

  @8
  Scenario: Verify "An approval request has been sent to the admin." popup is displayed when user delete groups more than 10
    And I select following 11 rows
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
      | 10 |
    Then I verify total groups selected should display at the top of grid
    And I wait for 2 seconds
    And I click the "delete_outline" button
    And I verify Delete group popup is displayed
    And I click the "Yes, delete this group" button
    Then I verify "An approval request has been sent to the admin." popup is displayed
    And I wait for 1 seconds
    When I click the "Dashboard" link
    And I click the "edit" button of "Action" column of row 1
    Then I verify Approve Deletion Request Popup "is" displayed with "Please type \"I AGREE\"" input field,"Cancel" button,"Decline" and "Approve" button
    And I enter "I AGREE" in the "Please type \"I AGREE\"" field
    And I click the "Decline" button
    Then I verify "Status" column for row 1 should be autopopulate

  @9
  Scenario: Verify Dashboard link functionality
    When I click the "Dashboard" link
    And I wait for 2 seconds
    And I print the total deletion request

  @10
  Scenario: Verify the count of groups selected for deletion must be equal to Action count column in Dashboard tab
    And I wait for 1 seconds
    And I click "selectAllGroups" checkbox in the header
    Then I verify total groups selected should display at the top of grid
    And I click the "delete_outline" button
    And I wait for 1 seconds
    And I click the "Yes, delete this group" button
    And I wait for 2 seconds
    When I click the "Dashboard" link
    Then I verify the count of groups selected for deletion,that exact count should be displayed in "Action Count" column in dashboard tab

  @11
  Scenario: Verify the count of groups selected for deletion must be equal to Action count column in Dashboard tab by applying filter
    And I click "selectAllGroups" checkbox in the header
    Then I verify total groups selected should display at the top of grid
    And I click the "delete_outline" button
    And I wait for 1 seconds
    And I click the "Yes, delete this group" button
    And I wait for 2 seconds
    When I click the "Dashboard" link
    Then I verify the count of groups selected for deletion,that exact count should be displayed in "Action Count" column in dashboard tab
    And I print the total deletion request

  @12
  Scenario: verify Approve and Decline buttons functionality without entering "I AGREE" in input field
    And I select following 11 rows
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
      | 10 |
    Then I verify total groups selected should display at the top of grid
    And I wait for 2 seconds
    And I click the "delete_outline" button
    And I click the "Yes, delete this group" button
    Then I verify "An approval request has been sent to the admin." popup is displayed
    And I wait for 1 seconds
    When I click the "Dashboard" link
    And I click the "edit" button of "Action" column of row 1
    Then I verify Approve Deletion Request Popup "is" displayed with "Please type \"I AGREE\"" input field,"Cancel" button,"Decline" and "Approve" button
    And I click the "Approve" button
    Then I verify "This field is required" error message is displayed
    And I click the "Cancel" button
    And I click the "edit" button of "Action" column of row 1
    And I wait for 1 seconds
    Then I verify "This field is required" error message is displayed

  @13
  Scenario:verify Approve and Decline buttons functionality by entering "i agree"in input field
    And I wait for 1 seconds
    And I select following 11 rows
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
      | 10 |
    Then I verify total groups selected should display at the top of grid
    And I wait for 2 seconds
    And I click the "delete_outline" button
    And I click the "Yes, delete this group" button
    Then I verify "An approval request has been sent to the admin." popup is displayed
    And I wait for 1 seconds
    When I click the "Dashboard" link
    And I click the "edit" button of "Action" column of row 1
    Then I verify Approve Deletion Request Popup "is" displayed with "Please type \"I AGREE\"" input field,"Cancel" button,"Decline" and "Approve" button
    And I enter "i agree" in the "Please type \"I AGREE\"" field
    And I click the "Approve" button
    And I wait for 1 seconds
    Then I verify "Please type I AGREE" popup is displayed

  @14
  Scenario: verify "groups deleted successfully." popup is displayed when user delete less than 10 groups and groups should be deleted
    And I select following 8 rows
      | 1  |
      | 3  |
      | 4  |
      | 6  |
      | 7  |
      | 5  |
      | 9  |
      | 11 |
    Then I verify total groups selected should display at the top of grid
    And I wait for 2 seconds
    And I click the "delete_outline" button
    And I click the "Yes, delete this group" button
    And I wait for 2 seconds
    Then I verify "8 groups deleted successfully." popup is displayed


  @15
  Scenario: verify when user click Approve button by passing "I AGREE" in field then status column is auto populate & Message column is auto populate with Successfull message
    And I select following 13 rows
      | 2  |
      | 3  |
      | 4  |
      | 6  |
      | 7  |
      | 5  |
      | 9  |
      | 11 |
      | 15 |
      | 12 |
      | 10 |
      | 13 |
      | 14 |
    Then I verify total groups selected should display at the top of grid
    And I wait for 2 seconds
    And I click the "delete_outline" button
    And I verify Delete group popup is displayed
    And I click the "Yes, delete this group" button
    Then I verify "An approval request has been sent to the admin." popup is displayed
    And I wait for 1 seconds
    When I click the "Dashboard" link
    And I click the "edit" button of "Action" column of row 1
    Then I verify Approve Deletion Request Popup "is" displayed with "Please type \"I AGREE\"" input field,"Cancel" button,"Decline" and "Approve" button
    And I enter "I AGREE" in the "Please type \"I AGREE\"" field
    And I click the "Approve" button
    And I wait for 1 seconds
    And I refresh the page
    And I scroll horizontally
    Then I verify "Status" column for row 1 should be autopopulate
    And I verify "Message" column for row 1 should be auto populate with "Successfull" message


  @16
  Scenario: Action column delete button functionality
    And I move to row 2 in "Groups" column in grid record
    And I wait for 1 seconds
    Then I verify following buttons are displayed in Action column
      | file_copy_outline |
      | delete_outline    |
    And I click the "delete_outline" button
    And I wait for 1 seconds
    And I verify Delete group popup is displayed
    And I click the "Yes, delete this group" button
    And I wait for 1 seconds
    Then I verify "1 group deleted successfully." popup is displayed


  @17
  Scenario: verify NEW record should be displayed in Deletion Requests grid
    And I select following 12 rows
      | 2  |
      | 3  |
      | 4  |
      | 6  |
      | 7  |
      | 5  |
      | 9  |
      | 11 |
      | 15 |
      | 12 |
      | 10 |
      | 13 |
    Then I verify total groups selected should display at the top of grid
    And I wait for 2 seconds
    And I click the "delete_outline" button
    And I verify Delete group popup is displayed
    And I click the "Yes, delete this group" button
    Then I verify "An approval request has been sent to the admin." popup is displayed
    And I wait for 1 seconds
    And I click the "Dashboard" link
    Then I verify there is "NEW" deletion request in row 1 for "GROUP_DELETION"

  @18
  Scenario: verify Delete Groups popup  Cancel button functionality
    And I select row 1 checkbox
    And I click the "delete_outline" button
    And I verify Delete group popup is displayed
    And I click the "Cancel" button
    And I wait for 1 seconds
    Then I verify Delete group popup is get closed

  @19
  Scenario:verify selected groups are get deleted from grid
    And I select following 13 rows
      | 2  |
      | 3  |
      | 4  |
      | 6  |
      | 7  |
      | 5  |
      | 9  |
      | 11 |
      | 15 |
      | 12 |
      | 10 |
      | 13 |
      | 14 |
    Then I verify total groups selected should display at the top of grid
    And I store selected "Groups" of "Groups" column
    And I wait for 1 seconds
    And I click the "delete_outline" button
    And I wait for 1 seconds
    And I verify Delete group popup is displayed
    And I click the "Yes, delete this group" button
    Then I verify "An approval request has been sent to the admin." popup is displayed
    And I wait for 1 seconds
    When I click the "Dashboard" link
    And I click the "edit" button of "Action" column of row 1
    Then I verify Approve Deletion Request Popup "is" displayed with "Please type \"I AGREE\"" input field,"Cancel" button,"Decline" and "Approve" button
    And I enter "I AGREE" in the "Please type \"I AGREE\"" field
    And I click the "Approve" button
    And I wait for 1 seconds
    And I refresh the page
    And I scroll horizontally
    Then I verify "Status" column for row 1 should be autopopulate
    And I verify "Message" column for row 1 should be auto populate with "Successfull" message
    And I click the "Groups" link
    And I click the "add Add a filter" button
    And I select "Name" from filter
    And I enter "gupta" in the "Start with" field
    And I click the "Done" button
    Then I verify selected groups are deleted from "Groups" column and from grid

  @24
  Scenario: verify filter options For Groups
    And I click the "close" button
    And I wait for 1 seconds
    And I click the "add Add a filter" button
    And I wait for 1 seconds
    Then I verify following filter options are present
      | Name              |
      | Orphan Groups     |
      | External Members  |
      | Owner             |
