Feature: Policies Deletion functionality
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
    When I click the "Policies" link
    And I verify I am on the "Patronum - Policy" page

  @25
  Scenario: verify Add a filter button for Policies
    And I wait for 1 seconds
    And The following elements exist
      | add Add a filter |
    And I click the "add Add a filter" button
    Then I verify following filter options are present
      | Name   |

  @26
  Scenario: verify the result by applying filter
    And I wait for 1 seconds
    And I click the "add Add a filter" button
    And I select "Name" from filter
    And I enter "TestPolicy" in the "Start with" field
    And I click the "Done" button
    And I wait for 1 seconds
    Then I verify that value of "Policy Name" for "row 1" is "TestPolicy"


  @27
  Scenario: Master select checkbox functionality of Policies
    And I click "selectAllPolicyList" checkbox in the header
    And I wait for 1 seconds
    And I scroll down to the bottom of webpage
    And I wait for 2 seconds
    And I scroll down to the bottom of webpage
    And I wait for 2 seconds
    Then I verify all visible records get selected

  @28
  Scenario: verify user is able to Select random Policies
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

  @29
  Scenario: Clear Selection functionality for policies
    And I select row 1 checkbox
    And I click the "close" button
    And I wait for 1 seconds
    Then I verify row 1 checkbox is deselected
    And I click "selectAllPolicyList" checkbox in the header
    Then I verify all visible records get selected
    And I click the "close" button
    Then I verify all the selected records get deselected

  @30
  Scenario: Action column -> copy and delete button visibility for policies
    And I move to row 2 in "Policy Name" column in grid record
    And I wait for 1 seconds
    Then I verify following buttons are displayed in Action column
      | file_copy_outline |
      | delete_outline    |
      | create_outline    |

  @31
  Scenario: Verify total Policies selected should display at the top of grid
    And I click "selectAllPolicyList" checkbox in the header
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


  @32
  Scenario: verify the selected Policies gets deselected by refreshing the page
    And I click "selectAllPolicyList" checkbox in the header
    Then I verify all visible records get selected
    And I refresh the page
    Then I verify all the selected records get deselected

  @92
  Scenario: Create Multiple policy
    And I create 7 policies


  @33
  Scenario: verify user is able to delete single policy
    And I click the "add Add a filter" button
    And I select "Name" from filter
    And I enter "TestPolicy" in the "Start with" field
    And I click the "Done" button
    And I wait for 1 seconds
    And I select row 1 checkbox
    And I store selected "Policy" of "Policy Name" column
    And I click the "delete_outline" button
    And I wait for 1 seconds
    And I verify popup of "Delete Policy" "is" displayed with "Cancel" & "OK" buttons
    And I click the "OK" button
    And I wait for 1 seconds
    Then I verify "1 policy removed successfully." popup is displayed
    And I click the "close" button of filter
    And I click the "add Add a filter" button
    And I select "Name" from filter
    And I enter "TestPolicy" in the "Start with" field
    And I click the "Done" button
    And I wait for 1 seconds
    Then I verify selected groups are deleted from "Policy Name" column and from grid


  @34
  Scenario: verify user is able to delete 2 policies directly
    And I wait for 1 seconds
    And I select following 2 rows
      | 1  |
      | 3  |
    Then I verify total groups selected should display at the top of grid
    And I store selected "Policy" of "Policy Name" column
    And I click the "delete_outline" button
    And I wait for 1 seconds
    And I verify popup of "Delete Policy" "is" displayed with "Cancel" & "OK" buttons
    And I click the "OK" button
    And I wait for 1 seconds
    Then I verify "2 policies removed successfully." popup is displayed
    Then I verify selected groups are deleted from "Policy Name" column and from grid

  @35
  Scenario: Verify the count of policies selected for deletion must be equal to Action count column in dashboard tab
    And I wait for 1 seconds
    And I click "selectAllPolicyList" checkbox in the header
    Then I verify total groups selected should display at the top of grid
    And I click the "delete_outline" button
    And I wait for 1 seconds
    And I verify popup of "Delete Policy" "is" displayed with "Cancel" & "OK" buttons
    And I click the "OK" button
    And I wait for 1 seconds
    Then I verify "An approval request has been sent to the admin." popup is displayed
    And I verify the email for "Policy - Deletion Request" was received
    When I click the "Dashboard" link
    Then I verify the count of groups selected for deletion,that exact count should be displayed in "Action Count" column in dashboard tab


  @36
  Scenario: Verify the count of policies selected for deletion must be equal to Action count column in dashboard tab by applying filter
    And I wait for 1 seconds
    And I click the "add Add a filter" button
    And I select "Name" from filter
    And I enter "TestPolicy" in the "Start with" field
    And I click the "Done" button
    And I wait for 1 seconds
    And I click "selectAllPolicyList" checkbox in the header
    Then I verify total groups selected should display at the top of grid
    And I click the "delete_outline" button
    And I verify popup of "Delete Policy" "is" displayed with "Cancel" & "OK" buttons
    And I wait for 1 seconds
    And I click the "OK" button
    And I wait for 1 seconds
    Then I verify "An approval request has been sent to the admin." popup is displayed
    And I verify the email for "Policy - Deletion Request" was received
    When I click the "Dashboard" link
    Then I verify the count of groups selected for deletion,that exact count should be displayed in "Action Count" column in dashboard tab
    And I print the total deletion request


  @37
  Scenario: verify Approve and Decline buttons functionality without entering "I AGREE" in input field for policies deletion
    And I click the "add Add a filter" button
    And I select "Name" from filter
    And I enter "TestPolicy" in the "Start with" field
    And I click the "Done" button
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
    And I wait for 1 seconds
    And I click the "delete_outline" button
    And I verify popup of "Delete Policy" "is" displayed with "Cancel" & "OK" buttons
    And I click the "OK" button
    And I wait for 1 seconds
    Then I verify "An approval request has been sent to the admin." popup is displayed
    And I verify the email for "Policy - Deletion Request" was received
    When I click the "Dashboard" link
    And I click the "edit" button of "Action" column of row 1
    Then I verify Approve Deletion Request Popup "is" displayed with "Please type \"I AGREE\"" input field,"Cancel" button,"Decline" and "Approve" button
    And I click the "Approve" button
    Then I verify "This field is required" error message is displayed
    And I click the "Cancel" button
    And I click the "edit" button of "Action" column of row 1
    And I wait for 1 seconds
    Then I verify "This field is required" error message is displayed


  @38
  Scenario:verify Approve and Decline buttons functionality by entering "i agree"in input field for policies deletion
    And I click the "add Add a filter" button
    And I select "Name" from filter
    And I enter "TestPolicy" in the "Start with" field
    And I click the "Done" button
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
    And I click the "delete_outline" button
    And I wait for 1 seconds
    And I click the "OK" button
    And I wait for 1 seconds
    Then I verify "An approval request has been sent to the admin." popup is displayed
    And I verify the email for "Policy - Deletion Request" was received
    When I click the "Dashboard" link
    And I click the "edit" button of "Action" column of row 1
    Then I verify Approve Deletion Request Popup "is" displayed with "Please type \"I AGREE\"" input field,"Cancel" button,"Decline" and "Approve" button
    And I enter "i agree" in the "Please type \"I AGREE\"" field
    And I click the "Approve" button
    And I wait for 1 seconds
    Then I verify "Please type I AGREE" popup is displayed


  @39
  Scenario: verify when user click Approve button by passing "I AGREE" in field then status column is auto populate & Message column is auto populate with Successfull message for policies deletion
    And I click the "add Add a filter" button
    And I select "Name" from filter
    And I enter "TestPolicy" in the "Start with" field
    And I click the "Done" button
    And I wait for 1 seconds
    And I select following 6 rows
      | 2  |
      | 3  |
      | 4  |
      | 6  |
      | 7  |
      | 5  |
    Then I verify total groups selected should display at the top of grid
    And I store selected "Policy" of "Policy Name" column
    And I click the "delete_outline" button
    And I wait for 1 seconds
    And I click the "OK" button
    And I wait for 1 seconds
    Then I verify "An approval request has been sent to the admin." popup is displayed
    And I verify the email for "Policy - Deletion Request" was received
    When I click the "Dashboard" link
    And I click the "edit" button of "Action" column of row 1
    And I enter "I AGREE" in the "Please type \"I AGREE\"" field
    And I click the "Approve" button
    And I wait for 1 seconds
    And I refresh the page
    And I scroll horizontally
    Then I verify "Status" column for row 1 should be autopopulate
    And I verify "Message" column for row 1 should be auto populate with "Successfull" message
    And I click the "Policies" link
    And I click the "add Add a filter" button
    And I select "Name" from filter
    And I enter "TestPolicy" in the "Start with" field
    And I click the "Done" button
    Then I verify selected groups are deleted from "Policy Name" column and from grid

  @40
  Scenario: verify NEW record should be displayed in Deletion Requests grid for Policy_Deletion
    And I click the "add Add a filter" button
    And I select "Name" from filter
    And I enter "TestPolicy" in the "Start with" field
    And I click the "Done" button
    And I wait for 1 seconds
    And I select following 3 rows
      | 2  |
      | 3  |
      | 4  |
    Then I verify total groups selected should display at the top of grid
    And I wait for 1 seconds
    And I click the "delete_outline" button
    And I wait for 1 seconds
    And I verify popup of "Delete Policy" "is" displayed with "Cancel" & "OK" buttons
    And I click the "OK" button
    And I wait for 1 seconds
    Then I verify "An approval request has been sent to the admin." popup is displayed
    And I click the "Dashboard" link
    Then I verify there is "NEW" deletion request in row 1 for "POLICY_DELETION"


  @41
  Scenario: Verify Policy Delete popup Cancel button Functionality
    And I select row 1 checkbox
    And I click the "delete_outline" button
    And I wait for 1 seconds
    And I verify popup of "Delete Policy" "is" displayed with "Cancel" & "OK" buttons
    And I wait for 1 seconds
    And I click the "Cancel" button
    And I wait for 1 seconds
    And I verify popup of "Delete Policy" "is not" displayed with "Cancel" & "OK" buttons


  @42
  Scenario: verify Approve deletion request popup - cancel button functionality
    And I click the "add Add a filter" button
    And I select "Name" from filter
    And I enter "TestPolicy" in the "Start with" field
    And I click the "Done" button
    And I wait for 1 seconds
    And I select following 3 rows
      | 2  |
      | 3  |
      | 4  |
    Then I verify total groups selected should display at the top of grid
    And I wait for 1 seconds
    And I click the "delete_outline" button
    And I wait for 1 seconds
    And I verify popup of "Delete Policy" "is" displayed with "Cancel" & "OK" buttons
    And I click the "OK" button
    And I wait for 1 seconds
    Then I verify "An approval request has been sent to the admin." popup is displayed
    And I verify the email for "Policy - Deletion Request" was received
    And I click the "Dashboard" link
    And I click the "edit" button of "Action" column of row 1
    Then I verify Approve Deletion Request Popup "is" displayed with "Please type \"I AGREE\"" input field,"Cancel" button,"Decline" and "Approve" button
    And I click the "Cancel" button
    And I wait for 1 seconds
    Then I verify Approve Deletion Request Popup "is not" displayed with "Please type \"I AGREE\"" input field,"Cancel" button,"Decline" and "Approve" button

  @91
  Scenario: Verify Add Policy Feature and Contact Provisioning
    And I click the Add Policy button
    And I enter "Test Policy <random>" in the "Policy Name" field
    And I click the "Save" button
    And I wait for 2 seconds
    And I click the "contacts" button
    And I click the provision button
    And I select "Stephen O'Donnell" member from Enter Name or Emails dropdown
    And I wait for 4 seconds
    Then I verify contacts for selected member is get added

  @93
  Scenario: Verify duplicate policy should not get created
    When I get the "Policy Name" for "row 1" of the grid
    And I click the Add Policy button
    And I enter that information into "Policy Name" field
    And I click the "Save" button
    And I wait for 1 seconds
    Then I verify "A policy with this name <policy name> already exists." popup is displayed

  Scenario:Verify "Please select executor." validation message
    And I click the Add Policy button
    And I enter "Test Policy <random>" in the "Policy Name" field
    And I click the "Save" button
    And I wait for 2 seconds
    And I click the "calendars" button
    And I click the deprovision button
    And I wait for 1 seconds
    Then I verify popup of "Deprovision Please select actions for user deprovisioning" "is" displayed with "Cancel" & "OK" buttons
    And I click the "OK" button
    Then I verify "Please select executor." validation message
    And I select the "Transfer primary calendar event ownership to executor" checkbox
    And I select "Stephen O'Donnell" member from Enter Name or Emails dropdown
    And I click the "OK" button

  Scenario: Adding member to policy workflow
    When I get the "Policy Name" for "row 1" of the grid
    And I open the details for selected row
    And I click the "workflow" button
    And I click the arrow down for the "Filters"
    And I add following members to the workflow
       | stuart.armstrong@demo.bespinlabs.com |
      | alessia.russo@demo.bespinlabs.com    |
      | kenneth.mclean@demo.bespinlabs.com   |
      | fran.kirby@demo.bespinlabs.com       |
    And I click the "Save" button

    Scenario: To check the policies details
      And I click the Add Policy button
      And I enter "Test Policy <random>" in the "Policy Name" field
      And I click the "Save" button
      And I wait for 1 seconds
      And I click the "calendars" button
      And I click the deprovision button
      And I wait for 1 seconds
      Then I verify popup of "Deprovision Please select actions for user deprovisioning" "is" displayed with "Cancel" & "OK" buttons
      And I click the "OK" button
      Then I verify "Please select executor." validation message
      And I select the "Remove user as attendee" checkbox
      And I select "<random>" member from Enter Name or Emails dropdown
      And I click the "OK" button
      And I click the "workflow" button
      And I wait for 1 seconds
      And I click the arrow down for the "Actions"
      And I wait for 2 seconds
      Then I Should verify in "Executor" present in Attribute
      Then I Should verify in "Calendar" present in Assets
      Then i verify the Executor present in workflow Action

    Scenario: To test add policy button with description
      And I click the Add Policy button
      And I enter "test policy" in the "Policy Name" field
      And I enter "testing" in  "Description" field
      And I click the "Save" button
      Then I verify "logo" should be present




















