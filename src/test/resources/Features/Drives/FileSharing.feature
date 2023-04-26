@ignore
Feature: File Sharing Functionality

  Background:
    Given I am on the login page
    And I wait for 2 seconds
    Then I verify Patronum home page is displayed
    When I click the "Drives " link
    And I click the "File Sharing" button
    And I refresh the page
    And I wait for 1 seconds

  Scenario: Verify filer options are present
    And I click the "add Add a filter" button
    And I wait for 2 seconds
    Then I verify following filter options are present
      | Email    |
      | Domain   |
      | Internal |
      | External |
      | Personal |

  Scenario Outline: Verify "Email" & "Domain" filter functionality
    And I click the "add Add a filter" button
    And I wait for 1 seconds
    When I select "<selection>" from filter
    And I wait for 1 seconds
    Then I verify "<selection>" filter is displayed
    And I enter "<value>" in the "Start with" field
    And I click the "Done" button
    And I wait for 1 seconds
    Then I verify result that contains "<value>" should displayed

    Examples:
      |selection   | value                            |
      |Email       |<gmail>                           |
      |Domain      |demo.bespinlabs.com               |

  Scenario Outline: Verify Internal,External Filter functionality
    And I click the "add Add a filter" button
    And I wait for 1 seconds
    When I select "<selection>" from filter
    And I wait for 1 seconds
    Then I verify result that contains "<value>" should displayed

    Examples:
      |selection   | value                            |
      |Internal    |@demo.bespinlabs.com              |
#      External scenario may fail
      |External    |@dev.jellyfishtechnologies.com    |


  Scenario: Verify "Unshare All files" / "lock" button and Email functionality
    And I click the "add Add a filter" button
    And I wait for 1 seconds
    When I select "External" from filter
    And I wait for 2 seconds
    And I click the "lock" button of row 3
    And I wait for 1 seconds
    And I click the "Confirm" button
    And I wait for 2 seconds
    Then I verify "An approval request has been sent to the admin." popup is displayed
    And I verify the email for "File - Unshare Request" was received

  Scenario: verify Select All/checkbox-selectAllFileSharing check box functionality while scroll down
    When I click "checkbox-selectAllFileSharing" checkbox in the header
    And I wait for 1 seconds
    Then I verify all visible records get selected
    And I wait for 1 seconds
    And I scroll down to the bottom of webpage
    And I wait for 1 seconds
    Then I verify all visible records get selected

  Scenario: verify Uncheck Select All/checkbox-selectAllFileSharing check box functionality
    When I click "checkbox-selectAllFileSharing" checkbox in the header
    And I wait for 1 seconds
    Then I verify all visible records get selected
    When I uncheck "checkbox-selectAllFileSharing" checkbox in the header
    And I verify all the selected records get deselected
    And I wait for 1 seconds

  Scenario: Verify records should be deselected after "An approval request has been sent to admin" popup is displayed & Verify Email for File - Unshare Request has received to Admin
    When I click "checkbox-selectAllFileSharing" checkbox in the header
    Then I verify all visible records get selected
    And I wait for 1 seconds
    And I click the "lock" button
    And I wait for 1 seconds
    And I click the "Confirm" button
    And I wait for 2 seconds
    Then I verify "An approval request has been sent to the admin." popup is displayed
    And I wait for 1 seconds
    And I verify all the selected records get deselected
    Then I verify the email for "File - Unshare Request" was received

  Scenario: verify Clear Selection functionality
    And I select row 1 checkbox
    And I click the "close" button
    And I wait for 1 seconds
    Then I verify row 1 checkbox is deselected
    And I click "checkbox-selectAllFileSharing" checkbox in the header
    Then I verify all visible records get selected
    And I click the "close" button
    Then I verify all the selected records get deselected

  Scenario: Verify Confirm unsharing poup cancle button functionality
    When I click "checkbox-selectAllFileSharing" checkbox in the header
    And I wait for 1 seconds
    And I click the "lock" button
    And I wait for 1 seconds
    And I verify popup of "Confirm" "is" displayed with "Cancel" & "Confirm" buttons
    And I click the "Cancel" button
    And I wait for 1 seconds
    And I verify popup of "Confirm" "is not" displayed with "Cancel" & "Confirm" buttons

  Scenario: verify user is able to Select random users
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

  Scenario: verify the selected users gets deselected by refreshing the page
    And I click "checkbox-selectAllFileSharing" checkbox in the header
    Then I verify all visible records get selected
    And I refresh the page
    Then I verify all the selected records get deselected

  Scenario: verify user is able to unshared five file
    And I click the "add Add a filter" button
    And I wait for 1 seconds
    When I select "External" from filter
    And I wait for 2 seconds
    And I click on the row "3" user
    And I wait for 2 seconds
    Then I verify popup of "User Files" "is" displayed with "Cancel" & "Unshare" buttons
    And I wait for 1 seconds
    And I check row 6 popup checkbox of "userFilesMainTBody" grid
    And I check row 7 popup checkbox of "userFilesMainTBody" grid
    And I check row 8 popup checkbox of "userFilesMainTBody" grid
    And I check row 9 popup checkbox of "userFilesMainTBody" grid
    And I check row 10 popup checkbox of "userFilesMainTBody" grid
    Then I verify total groups selected should display at the top of grid
    And I wait for 1 seconds
    And I click the "Unshare" button
    And I wait for 2 seconds
    And I verify "Files unSharing started will notify when it will done" popup is displayed
    And I wait for 1 seconds
    When I click the "Dashboard" link
    Then I verify selected files unsharing is done

  Scenario: Verify the count of files selected for unsharing in dashboard tab
    And I click the "add Add a filter" button
    And I wait for 1 seconds
    When I select "External" from filter
    And I wait for 2 seconds
    And I click the "lock" button of row 3
    And I get the total file count for row 3
    And I wait for 1 seconds
    And I click the "Confirm" button
    And I wait for 2 seconds
    Then I verify "An approval request has been sent to the admin." popup is displayed
    And I verify the email for "File - Unshare Request" was received
    And I click the "Dashboard" link
    And I wait for 2 seconds
    Then I verify the count of files selected for unsharing,that exact count should be displayed in "Action Count" column in dashboard tab

  Scenario: Verify file unsharing for particular user
    And I click the "add Add a filter" button
    And I wait for 1 seconds
    When I select "External" from filter
    And I wait for 2 seconds
    And I click on the row "3" user
    And I wait for 1 seconds
    Then I verify popup of "User Files" "is" displayed with "Cancel" & "Unshare" buttons
    When I click "selectAllFiles" checkbox in the header
    Then I verify total groups selected should display at the top of grid
    When I click the "Unshare" button
    Then I verify "An approval request has been sent to the admin." popup is displayed
    And I verify the email for "File - Unshare Request" was received
    And I click the "Dashboard" link
    And I click the "edit" button of "Action" column of row 1
    Then I verify Approve Deletion Request Popup "is" displayed with "Please type \"I AGREE\"" input field,"Cancel" button,"Decline" and "Approve" button
    And I enter "I AGREE" in the "Please type \"I AGREE\"" field
    And I click the "Approve" button
    And I wait for 1 seconds
    Then I verify selected files unsharing is done

  Scenario: Verify validation message "Please select a file."
    And I click the "add Add a filter" button
    And I wait for 1 seconds
    When I select "External" from filter
    And I wait for 2 seconds
    And I click on the row "3" user
    And I wait for 1 seconds
    Then I verify popup of "User Files" "is" displayed with "Cancel" & "Unshare" buttons
    When I click the "Unshare" button
    Then I verify "Please select a file." popup is displayed