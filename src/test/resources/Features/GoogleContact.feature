Feature: Google Contacts Deletion functionality
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
    And I click the "Google Contacts" button
    And I wait for 1 seconds
    And I refresh the page

  @62
  Scenario: verify the result by passing random string/special character in filter option
    And I click the "add Add a filter" button
    And I wait for 1 seconds
    And I select "First name" from filter
    And I wait for 1 seconds
    And I enter "cdab" in the "Start with" field
    And I click the "Done" button
    And I wait for 1 seconds
    Then There is no result
    And I click the "close" button
    And I click the "add Add a filter" button
    And I select "Last name" from filter
    And I enter "#@!$" in the "Start with" field
    And I click the "Done" button
    And I wait for 5 seconds
    Then There is no result

  @63
  Scenario: Master select functionality of Google Contacts
    And I click "checkbox-selectAllContact" checkbox in the header
    And I scroll down to the bottom of webpage
    And I wait for 2 seconds
    Then I verify all visible records get selected

  @64
  Scenario: verify user is able to Select random Google Contacts
    And I select row 1 checkbox
    And I select row 2 checkbox
    And I select row 3 checkbox
    And I select row 5 checkbox
    And I select row 6 checkbox
    And I select row 8 checkbox
    And I select row 10 checkbox
    And I wait for 1 seconds
    Then I verify total groups selected should display at the top of grid


  @65
  Scenario: verify Clear Selection functionality
    And I select row 1 checkbox
    And I click the "close" button
    And I wait for 1 seconds
    Then I verify row 1 checkbox is deselected
    And I click "checkbox-selectAllContact" checkbox in the header
    Then I verify all visible records get selected
    And I click the "close" button
    Then I verify all the selected records get deselected

  @66
  Scenario: Action column- Create,more_vert and delete button display for Google Contacts
    And I move to row 1 in "Action" column in grid record
    And I wait for 2 seconds
    Then I verify following buttons are displayed in Action column
      | create         |
      | delete_outline |
      | more_vert      |

  @67
  Scenario: Verify total Google Contacts selected should display at the top of grid
    And I click "checkbox-selectAllContact" checkbox in the header
    And I scroll down to the bottom of webpage
    And I wait for 2 seconds
    And I scroll down to the bottom of webpage
    And I wait for 2 seconds
    And I scroll down to the bottom of webpage
    And I wait for 2 seconds
    And I scroll down to the bottom of webpage
    And I wait for 2 seconds
    And I scroll up
    Then I verify total groups selected should display at the top of grid

  @68
  Scenario: verify the selected Google Contacts gets deselected by refreshing the page
    And I click "checkbox-selectAllContact" checkbox in the header
    Then I verify all visible records get selected
    And I refresh the page
    Then I verify all the selected records get deselected

  @69
  Scenario: verify user is able to delete single Google Contact
    # scenario may get failed due to snackbar popup is taking too much time to display
    And I select row 12 checkbox
    And I store selected contacts
    And I click the "delete_outline" button
    And I wait for 1 seconds
    And I click the "OK" button
    And I wait for 1 seconds
    Then I verify "1 contact has been successfully deleted" popup is displayed
    And I wait for 1 seconds
    Then I verify selected contacts get deleted from grid

  @70
  Scenario: Verify "An approval request has been sent to the admin." popup is displayed when  google contacts deleted above 2
    # scenario may get failed due to snackbar popup is taking too much time to display
    And I select following 4 rows
      | 1  |
      | 3  |
      | 4  |
      | 2  |
    Then I verify total groups selected should display at the top of grid
    And I click the "delete_outline" button
    And I click the "OK" button
    And I wait for 2 seconds
    Then I verify "An approval request has been sent to the admin." popup is displayed

  @71
  Scenario: Verify user is able to delete 2 Google contacts directly
    # scenario may get failed due to snackbar popup is taking too much time to display
    And I select following 2 rows
      | 13  |
      | 14  |
    And I store selected contacts
    Then I verify total groups selected should display at the top of grid
    And I click the "delete_outline" button
    And I click the "OK" button
    And I wait for 1 seconds
    Then I verify "2 contact has been successfully deleted" popup is displayed
    Then I verify selected contacts get deleted from grid

  @72
  Scenario: Verify the count of Google Contacts selected for deletion must be equal to Action count column in Dashboard tab
    And I click "checkbox-selectAllContact" checkbox in the header
    And I scroll down to the bottom of webpage
    And I wait for 2 seconds
    And I scroll up
    Then I verify total groups selected should display at the top of grid
    And I click the "delete_outline" button
    And I click the "OK" button
    And I wait for 1 seconds
    Then I verify "An approval request has been sent to the admin." popup is displayed
    When I click the "Dashboard" link
    And I wait for 1 seconds
    Then I verify the count of groups selected for deletion,that exact count should be displayed in "Action Count" column in dashboard tab
    And I verify the email for "Google Contact - Deletion Request" was received

  @73
  Scenario: Verify Approve and Decline buttons functionality without entering "I AGREE" in input field for Google Contacts deletion
    And I select following 5 rows
      | 1  |
      | 3  |
      | 4  |
      | 6  |
      | 7  |
    Then I verify total groups selected should display at the top of grid
    And I wait for 1 seconds
    And I click the "delete_outline" button
    And I click the "OK" button
    And I wait for 1 seconds
    Then I verify "An approval request has been sent to the admin." popup is displayed
    And I verify the email for "Google Contact - Deletion Request" was received
    When I click the "Dashboard" link
    And I click the "edit" button of "Action" column of row 1
    Then I verify Approve Deletion Request Popup "is" displayed with "Please type \"I AGREE\"" input field,"Cancel" button,"Decline" and "Approve" button
    And I click the "Approve" button
    Then I verify "This field is required" error message is displayed
    And I click the "Cancel" button
    And I click the "edit" button of "Action" column of row 1
    And I wait for 1 seconds
    Then I verify "This field is required" error message is displayed

  @74
  Scenario: Verify Approve and Decline buttons functionality by entering "i agree"in input field for Google contact deletion
    And I select following 6 rows
      | 2  |
      | 3  |
      | 4  |
      | 6  |
      | 7  |
      | 5  |
    Then I verify total groups selected should display at the top of grid
    And I click the "delete_outline" button
    And I wait for 1 seconds
    And I click the "OK" button
    And I wait for 1 seconds
    Then I verify "An approval request has been sent to the admin." popup is displayed
    And I verify the email for "Google Contact - Deletion Request" was received
    When I click the "Dashboard" link
    And I click the "edit" button of "Action" column of row 1
    Then I verify Approve Deletion Request Popup "is" displayed with "Please type \"I AGREE\"" input field,"Cancel" button,"Decline" and "Approve" button
    And I enter "i agree" in the "Please type \"I AGREE\"" field
    And I click the "Approve" button
    And I wait for 1 seconds
    Then I verify "Please type I AGREE" popup is displayed

  @75
  Scenario: Verify NEW record should be displayed in Deletion Requests grid for GOOGLE_CONTACT_DELETION
    And I scroll down to the bottom of webpage
    And I wait for 2 seconds
    And I scroll up
    And I select following 5 rows
      | 2  |
      | 3  |
      | 4  |
      | 6  |
      | 8  |
    Then I verify total groups selected should display at the top of grid
    And I click the "delete_outline" button
    And I wait for 1 seconds
    And I click the "OK" button
    And I wait for 1 seconds
    Then I verify "An approval request has been sent to the admin." popup is displayed
    And I click the "Dashboard" link
    Then I verify there is "NEW" deletion request in row 1 for "GOOGLE_CONTACT_DELETION"
    And I verify the email for "Google Contact - Deletion Request" was received

  @76
  Scenario: Verify Delete Google Contact popup Cancel button functionality
    And I select row 1 checkbox
    And I click the "delete_outline" button
    And I wait for 1 seconds
    And I verify popup of "Delete Google Contact" "is" displayed with "Cancel" & "OK" buttons
    And I wait for 1 seconds
    And I click the "Cancel" button
    And I wait for 1 seconds
    And I verify popup of "Delete Google Contact" "is not" displayed with "Cancel" & "OK" buttons


  @77
  Scenario: Verify Approve deletion request popup cancel button functionality
    And I select following 6 rows
      | 2  |
      | 3  |
      | 4  |
      | 6  |
      | 7  |
      | 5  |
    Then I verify total groups selected should display at the top of grid
    And I wait for 1 seconds
    And I click the "delete_outline" button
    And I wait for 1 seconds
    And I click the "OK" button
    And I wait for 1 seconds
    Then I verify "An approval request has been sent to the admin." popup is displayed
    And I click the "Dashboard" link
    And I click the "edit" button of "Action" column of row 1
    Then I verify Approve Deletion Request Popup "is" displayed with "Please type \"I AGREE\"" input field,"Cancel" button,"Decline" and "Approve" button
    And I click the "Cancel" button
    And I wait for 1 seconds
    Then I verify Approve Deletion Request Popup "is not" displayed with "Please type \"I AGREE\"" input field,"Cancel" button,"Decline" and "Approve" button
    And I verify the email for "Google Contact - Deletion Request" was received


  @78
  Scenario: Verify when user click Approve button by passing "I AGREE" in field then status column is auto populate for Google Contacts deletion
    And I scroll down to the bottom of webpage
    And I wait for 2 seconds
    And I scroll up
    And I select following 5 rows
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
    Then I verify "An approval request has been sent to the admin." popup is displayed
    When I click the "Dashboard" link
    And I click the "edit" button of "Action" column of row 1
    And I enter "I AGREE" in the "Please type \"I AGREE\"" field
    And I click the "Approve" button
    And I wait for 1 seconds
    Then I verify "Status" column for row 1 should be autopopulate
    And I click the "Contacts" link
    And I click the "Google Contacts" button
    Then I verify selected contacts get deleted from grid
    And I verify the email for "Google Contact - Deletion Request" was received


  @79
  Scenario: Verify Shared Contact and Google Contact tab functionality
    And I click the "Shared Contacts" button
    And I click "shareCheckbox-all" checkbox in the header
    And I verify all visible records get selected
    And I wait for 1 seconds
    And I click the "Google Contacts" button
    When I click "checkbox-selectAllContact" checkbox in the header
    And I wait for 1 seconds
    And I verify all visible records get selected
    And I refresh the page
    Then I verify all the selected records get deselected

  @80
  Scenario: Verify contacts gets deleted when navigate through shared contacts and google contacts button
    When I select row 10 checkbox
    And I store selected contacts
    And I click the "delete_outline" button
    And I click the "OK" button
    Then I verify "1 contact has been successfully deleted" popup is displayed
    And I refresh the page
    Then I verify selected contacts get deleted from grid
    And I click the "Shared Contacts" button
    And I select row 10 checkbox
    And I store selected contacts
    And I click the "delete_outline" button
    And I wait for 1 seconds
    And I click the "OK" button
    And I wait for 2 seconds
    Then I verify selected contacts get deleted from grid

  @81
  Scenario: Export button functionality
    When I click "checkbox-selectAllContact" checkbox in the header
    And I click the "cloud_download" button
    And I wait for 1 seconds
    Then I verify popup of "Export Contacts" "is" displayed with "Cancel" & "OK" buttons
    And I click the "OK" button
    And I wait for 2 seconds
    Then I verify that document is stored on local drive
    And I click the "Shared Contacts" button
    And I click "shareCheckbox-all" checkbox in the header
    And I click the "cloud_download" button
    Then I verify popup of "Export Contacts" "is" displayed with "Cancel" & "OK" buttons
    And I click the "OK" button
    And I wait for 2 seconds
    Then I verify that document is stored on local drive