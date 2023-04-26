Feature: Shared Contacts Deletion functionality
  Background:
    Given I am on the login page
    And I wait for 2 seconds
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
    When I click the "Contacts" link
    And I verify I am on the "Patronum - Contacts" page


  @43
  Scenario: Verify Add a filter button & filter option should be present for Shared Contacts link
    And I wait for 1 seconds
    And The following elements exist
      | add Add a filter |
      | Shared Contacts  |
      | Google Contacts  |
      | Contacts Sharing |
    And I click the "add Add a filter" button
    Then I verify following filter options are present
      | First name |
      | Last name  |
      | Email      |
      | Phone      |


  @44
  Scenario: verify the result by applying filter
    And I get the "Name" for "row 1" of the grid
    And I click the "add Add a filter" button
    And I select "First name" from filter
    And I enter that information into "Start with" field
    And I click the "Done" button
    And I wait for 1 seconds
    Then I verify display data as per search criteria
    And I click the "close" button
    And I get the "Name" for "row 1" of the grid
    And I click the "add Add a filter" button
    And I select "Last name" from filter
    And I enter that information into "Start with" field
    And I click the "Done" button
    And I wait for 1 seconds
    Then I verify display data as per search criteria
    And I click the "close" button
    And I get the "Email" for "row 1" of the grid
    And I click the "add Add a filter" button
    And I select "Email" from filter
    And I enter that information into "Start with" field
    And I click the "Done" button
    And I wait for 1 seconds
    Then I verify display data as per search criteria
    And I click the "close" button
    And I click the "add Add a filter" button
    And I select "Phone" from filter
    And I enter "044" in the "Start with" field
    And I click the "Done" button
    And I wait for 1 seconds
    Then I verify that value of "Phone" for "row 1" is "044"


  @45
  Scenario: verify the result by passing random string/special character in filter option
    And I click the "add Add a filter" button
    And I select "First name" from filter
    And I enter "abcd" in the "Start with" field
    And I click the "Done" button
    And I wait for 1 seconds
    Then There is no result
    And I click the "close" button
    And I click the "add Add a filter" button
    And I select "Last name" from filter
    And I enter "#@!$" in the "Start with" field
    And I click the "Done" button
    And I wait for 1 seconds
    Then There is no result


  @46
  Scenario: Master select functionality of Shared Contacts
    And I click "shareCheckbox-all" checkbox in the header
    And I wait for 1 seconds
    And I scroll down to the bottom of webpage
    And I wait for 1 seconds
    And I scroll down to the bottom of webpage
    Then I verify all visible records get selected


  @47
  Scenario: Verify user is able to Select random Shared Contacts
    And I select row 1 checkbox
    And I select row 3 checkbox
    And I select row 5 checkbox
    And I select row 6 checkbox
    And I select row 8 checkbox
    And I select row 10 checkbox
    And I select row 2 checkbox
    And I wait for 1 seconds
    Then I verify total groups selected should display at the top of grid


  @48
  Scenario: Verify Clear Selection functionality of Shared Contacts
    And I select row 1 checkbox
    And I click the "close" button
    And I wait for 1 seconds
    Then I verify row 1 checkbox is deselected
    And I click "shareCheckbox-all" checkbox in the header
    Then I verify all visible records get selected
    And I click the "close" button
    Then I verify all the selected records get deselected


  @49
  Scenario: Action column- Create,more_vert and delete button display for Shared Contacts
    And I move to row 1 in "Action" column in grid record
    And I wait for 1 seconds
    Then I verify following buttons are displayed in Action column
      | create         |
      | delete_outline |
      | more_vert      |


  @50
  Scenario: Verify total Contacts selected should display at the top of grid
    And I click "shareCheckbox-all" checkbox in the header
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


  @51
  Scenario: Verify the selected Contacts gets deselected by refreshing the page
    And I click "shareCheckbox-all" checkbox in the header
    Then I verify all visible records get selected
    And I refresh the page
    Then I verify all the selected records get deselected

  @52
  Scenario: verify user is able to delete single Shared Contact
    And I select row 7 checkbox
    And I store selected contacts
    And I click the "delete_outline" button
    And I wait for 1 seconds
    And I click the "OK" button
    And I wait for 1 seconds
    Then I verify "1 Contact Deleted Successfully" popup is displayed
    And I wait for 1 seconds
    Then I verify selected contacts get deleted from grid


  @53
  Scenario:Verify Validation popup "An approval request has been sent to the admin." for shared Contacts
    And I scroll down to the bottom of webpage
    And I wait for 2 seconds
    And I scroll up
    And I select following 15 rows
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
      | 20 |
      | 18 |
      | 19 |
      | 2  |
    Then I verify total groups selected should display at the top of grid
    And I click the "delete_outline" button
    And I wait for 1 seconds
    And I click the "OK" button
    And I wait for 2 seconds
    Then I verify "An approval request has been sent to the admin." popup is displayed


  @54
  Scenario: Verify user is able to delete up to 9 Shared Contacts directly
    And I scroll down to the bottom of webpage
    And I wait for 2 seconds
    And I scroll up
    And I select following 9 rows
      | 4  |
      | 3  |
      | 7  |
      | 5  |
      | 9  |
      | 11 |
      | 15 |
      | 12 |
      | 10 |
    And I store selected contacts
    Then I verify total groups selected should display at the top of grid
    And I store selected "Contact" of "Name" column
    And I click the "delete_outline" button
    And I wait for 1 seconds
    And I click the "OK" button
    And I wait for 1 seconds
    Then I verify "9 Contact Deleted Successfully" popup is displayed
    Then I verify selected contacts get deleted from grid


  @55
  Scenario: Verify the count of Shared Contacts selected for deletion must be equal to Action count column in Dashboard tab
    And I scroll down to the bottom of webpage
    And I wait for 2 seconds
    And I scroll up
    And I click "shareCheckbox-all" checkbox in the header
    Then I verify total groups selected should display at the top of grid
    And I click the "delete_outline" button
    And I wait for 1 seconds
    And I click the "OK" button
    And I verify the email for "Shared Contact - Deletion Request" was received
    When I click the "Dashboard" link
    Then I verify the count of groups selected for deletion,that exact count should be displayed in "Action Count" column in dashboard tab


  @56
  Scenario: Verify Approve and Decline buttons functionality without entering "I AGREE" in input field for Shared Contacts deletion
    And I scroll down to the bottom of webpage
    And I wait for 2 seconds
    And I scroll up
    And I select following 15 rows
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
      | 20 |
      | 18 |
      | 19 |
      | 2  |
    Then I verify total groups selected should display at the top of grid
    And I wait for 1 seconds
    And I click the "delete_outline" button
    And I click the "OK" button
    And I wait for 1 seconds
    Then I verify "An approval request has been sent to the admin." popup is displayed
    And I verify the email for "Shared Contact - Deletion Request" was received
    When I click the "Dashboard" link
    And I click the "edit" button of "Action" column of row 1
    Then I verify Approve Deletion Request Popup "is" displayed with "Please type \"I AGREE\"" input field,"Cancel" button,"Decline" and "Approve" button
    And I click the "Approve" button
    Then I verify "This field is required" error message is displayed
    And I click the "Cancel" button
    And I click the "edit" button of "Action" column of row 1
    And I wait for 2 seconds
    Then I verify "This field is required" error message is displayed


  @57
  Scenario:Verify Approve and Decline buttons functionality by entering "i agree"in input field for Shared Contacts
    And I scroll down to the bottom of webpage
    And I wait for 2 seconds
    And I scroll up
    And I select following 11 rows
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
    Then I verify total groups selected should display at the top of grid
    And I click the "delete_outline" button
    And I wait for 1 seconds
    And I click the "OK" button
    And I wait for 1 seconds
    Then I verify "An approval request has been sent to the admin." popup is displayed
    And I verify the email for "Shared Contact - Deletion Request" was received
    When I click the "Dashboard" link
    And I click the "edit" button of "Action" column of row 1
    Then I verify Approve Deletion Request Popup "is" displayed with "Please type \"I AGREE\"" input field,"Cancel" button,"Decline" and "Approve" button
    And I enter "i agree" in the "Please type \"I AGREE\"" field
    And I click the "Approve" button
    And I wait for 1 seconds
    Then I verify "Please type I AGREE" popup is displayed


  @58
  Scenario: Verify NEW record should be displayed in Deletion Requests grid for SHARED_CONTACT_DELETION
    And I scroll down to the bottom of webpage
    And I wait for 2 seconds
    And I scroll up
    And I select following 11 rows
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
    Then I verify total groups selected should display at the top of grid
    And I wait for 1 seconds
    And I click the "delete_outline" button
    And I wait for 1 seconds
    And I click the "OK" button
    And I click the "Dashboard" link
    Then I verify there is "NEW" deletion request in row 1 for "SHARED_CONTACT_DELETION"
    And I verify the email for "Shared Contact - Deletion Request" was received


  @59
  Scenario: Verify Delete Share Contact popup - Cancel button functionality
    And I select row 1 checkbox
    And I click the "delete_outline" button
    And I wait for 1 seconds
    And I verify popup of "Delete Share Contact" "is" displayed with "Cancel" & "OK" buttons
    And I wait for 1 seconds
    And I click the "Cancel" button
    And I wait for 1 seconds
    And I verify popup of "Delete Share Contact" "is not" displayed with "Cancel" & "OK" buttons


  @60
  Scenario: Verify Approve deletion request popup - cancel button functionality
    And I scroll down to the bottom of webpage
    And I wait for 2 seconds
    And I scroll up
    And I select following 11 rows
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
    Then I verify total groups selected should display at the top of grid
    And I wait for 1 seconds
    And I click the "delete_outline" button
    And I wait for 1 seconds
    And I click the "OK" button
    And I wait for 1 seconds
    And I click the "Dashboard" link
    And I click the "edit" button of "Action" column of row 1
    Then I verify Approve Deletion Request Popup "is" displayed with "Please type \"I AGREE\"" input field,"Cancel" button,"Decline" and "Approve" button
    And I click the "Cancel" button
    And I wait for 1 seconds
    Then I verify Approve Deletion Request Popup "is not" displayed with "Please type \"I AGREE\"" input field,"Cancel" button,"Decline" and "Approve" button
    And I verify the email for "Shared Contact - Deletion Request" was received


  @61
  Scenario: Verify when user click Approve button by passing "I AGREE" in field then status column is auto populate for Shared Contacts deletion
    And I scroll down to the bottom of webpage
    And I wait for 2 seconds
    And I scroll up
    And I select following 11 rows
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
    And I store selected contacts
    And I click the "delete_outline" button
    And I wait for 1 seconds
    And I click the "OK" button
    And I wait for 1 seconds
    When I click the "Dashboard" link
    And I click the "edit" button of "Action" column of row 1
    And I enter "I AGREE" in the "Please type \"I AGREE\"" field
    And I click the "Approve" button
    And I wait for 1 seconds
    And I refresh the page
    And I scroll horizontally
    Then I verify "Status" column for row 1 should be autopopulate
    And I verify the email for "Shared Contact - Deletion Request" was received

  Scenario: duplicate contacts deletion by importing csv file
#### T0 Create duplicate contacts export the grid contacts and import the exported file  ####
    And I scroll down to the bottom of webpage
    And I wait for 2 seconds
    And I scroll down to the bottom of webpage
    And I find the duplicate contacts and delete that contact
