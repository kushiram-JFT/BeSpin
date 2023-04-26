@ignore
Feature: File Sharing Search Functionality

  Background:
    Given I am on the login page
    And I wait for 2 seconds
    Then I verify Patronum home page is displayed
    When I click the "Drives " link
    And I click the "Search" button
    And I refresh the page
    And I wait for 2 seconds

  Scenario: verify tabs & filter options in Search Functionality
    And The following elements exist
      | add Add a filter |
      | Shared Drives    |
      | File Sharing     |
      | Search           |
    When I click the "add Add a filter" button
    And I wait for 1 seconds
    Then I verify following filter options are present
      | Name                             |
      | File Id                          |
      | Owner                            |
      | Last Modified                    |
      | Orphan Files                     |
      | File Type                        |
      | Orphan Short Cuts                |
      | Multiple Parent File and Folders |
      | Shared Drive Files               |
      | Trash Files                      |
#      | Internal                         |
#      | External                         |

  Scenario Outline: Verify filters in search functionality
    When I click the "add Add a filter" button
    And I wait for 1 seconds
    And I select "<Filter>" from filter
    And I wait for 2 seconds

    Examples:
      | Filter                           |
      | Name                             |
      | File Id                          |
      | Owner                            |
      | Last Modified                    |
      | Orphan Files                     |
      | File Type                        |
      | Orphan Short Cuts                |
      | Multiple Parent File and Folders |
      | Shared Drive Files               |
      | Trash Files                      |
      | Internal                         |
      | External                         |


  Scenario Outline: Verify file type filter and user is able to select random files
    When I click the "add Add a filter" button
    And I select "File Type" from filter
    And I scroll up
    And I click the "Type" dropdown and select "<selection>" option
    And I wait for 1 seconds
    And I click the "Done" button
    And I wait for 2 seconds
    And I select following 2 rows
      | 1  |
      | 2  |
    Then I verify total groups selected should display at the top of grid
    Examples:
      | selection        |
      | Photo and Images |
      | Documents        |
      | Spreadsheets     |
#      | PDF              | currently data is not available for pdf

  Scenario Outline: Verify header checkbox functionality
    When I click the "add Add a filter" button
    And I select "File Type" from filter
    And I scroll up
    And I click the "Type" dropdown and select "<selection>" option
    And I wait for 1 seconds
    And I click the "Done" button
    And I wait for 2 seconds
    When I click the header checkbox
    And I wait for 1 seconds
    Then I verify all visible records get selected
    Examples:
      | selection        |
      | Photo and Images |
      | Documents        |
      | Spreadsheets     |

  Scenario: Verify Approval request functionality
#    TODO After bug fixed
#    Bug -> Approval request is not being sent to admin
#    It directly showing 0 files has been successfully deleted
    When I click the "add Add a filter" button
    And I select "File Type" from filter
    And I wait for 1 seconds
    And I scroll up
    And I wait for 1 seconds
    And I click the "Type" dropdown and select "Photo and Images" option
    And I wait for 1 seconds
    And I click the "Done" button
    And I wait for 2 seconds
    When I click the header checkbox
    And I wait for 1 seconds
    When I click the "delete_outline" button
    And I click the "OK" button
    And I wait for 1 seconds
    Then I verify approval request popup is displayed


  Scenario: verify the selected files gets deselected by refreshing the page
    When I click the "add Add a filter" button
    And I select "File Type" from filter
    And I scroll up
    And I click the "Type" dropdown and select "Photo and Images" option
    And I wait for 1 seconds
    And I click the "Done" button
    And I wait for 2 seconds
    When I click the header checkbox
    Then I verify all visible records get selected
    When I refresh the page
    Then I verify all the selected records get deselected

  Scenario: verify Clear Selection functionality
    When I click the "add Add a filter" button
    And I select "File Type" from filter
    And I scroll up
    And I click the "Type" dropdown and select "Photo and Images" option
    And I wait for 1 seconds
    And I click the "Done" button
    And I wait for 2 seconds
    When I click the header checkbox
    Then I verify all visible records get selected
    When I click the "close" button
    Then I verify all the selected records get deselected

  Scenario: verify user is able to delete single Contact
#    Bug -> Showing "0 file has been successfully deleted"
    When I click the "add Add a filter" button
    And I select "File Type" from filter
    And I scroll up
    And I click the "Type" dropdown and select "Photo and Images" option
    And I wait for 1 seconds
    And I click the "Done" button
    And I wait for 2 seconds
    And I select row 1 checkbox
    When I click the "delete_outline" button
    And I click the "OK" button
    And I wait for 1 seconds
    Then I verify "1 file has been successfully deleted" popup is displayed

  Scenario: Action column- delete button Functionality
#    Issue -> Snackbar popup is taking too much time to display
    When I click the "add Add a filter" button
    And I select "File Type" from filter
    And I scroll up
    And I click the "Type" dropdown and select "Photo and Images" option
    And I wait for 1 seconds
    And I click the "Done" button
    And I wait for 2 seconds
    And I move to row "2"
    And I wait for 1 seconds
    Then I verify following buttons are displayed in Action column
      | delete_outline |
    And I click the "delete_outline" button
    And I click the "OK" button
    And I wait for 1 seconds
    Then I verify "1 file has been successfully deleted" popup is displayed
