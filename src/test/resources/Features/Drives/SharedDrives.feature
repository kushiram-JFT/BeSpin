@ignore
Feature: Shared Drives Functionality

  Background:
    Given I am on the login page
    And I wait for 2 seconds
    Then I verify Patronum home page is displayed
    When I click the "Drives " link
    And I refresh the page

  Scenario: verify Add a filter button is displayed & filter option should be present when user click on Add Filter button for Drives link
    And I wait for 1 seconds
    And The following elements exist
      | add Add a filter |
      | Shared Drives    |
      | File Sharing     |
      | Search           |
    And I click the "add Add a filter" button
    Then I verify following filter options are present
      | Shared Drive name  |
      | Drive Type         |
      | Date Created       |
      | No managers        |
      | No members         |

  Scenario: Verify Drive Type filter Option
    And I click the "add Add a filter" button
    And I select "Drive Type" from filter
    Then I verify drive type filter popup is displayed
    And I click the drive type dropdown icon
    And I wait for 1 seconds
    Then The following elements exist
      | Internal  |
      | External  |
    And I click the "External" button
    And I wait for 1 seconds

  Scenario: Verify filter option popup get closed
    And I click the "add Add a filter" button
    And I select "Drive Type" from filter
    And I wait for 1 seconds
    Then I verify drive type filter popup is displayed
    And I click the "clear" button
    And I wait for 1 seconds
    Then I verify drive type filter popup is closed

  Scenario: Verify User is able to select random files in Drives
    #Enter odd numbers for rows selection
    #Entering even numbers will fail the test case because in DOM there are two <tr> tags
    And I click on the "Templates"
    And I scroll down to the bottom of webpage
    And I select row 3 checkbox
    And I select row 5 checkbox
    And I select row 7 checkbox

  Scenario: verify options for Drives -> when user right clicks
    And I click on the "Templates"
    And I wait for 1 seconds
    When I right click on the drive file
    Then I verify options are present in list

  Scenario: verify share option functionality
    And I click on the "Templates"
    And I wait for 1 seconds
    And I move to "Automation Testing Do Not Delete" file and perform right click operation
    And I click on the share option
    And I verify "Share with others" alert displayed with "Cancel" & "Save" buttons
    And I wait for 1 seconds
    And I enter "Alex Greenwood" in the "Enter names or emails..." field
    And I wait for 5 seconds
    And I click on the first option
    And I click the "Save" button
    Then I verify "Permission updated successfully" popup is displayed
    And I wait for 2 seconds
    And I move to "Automation Testing Do Not Delete" file and perform right click operation
    And I click on the share option
    Then I verify "Alex Greenwood" is Added for file access

  Scenario:verify duplicate member is not get added for file permission
    And I click on the "Templates"
    And I wait for 1 seconds
    And I move to "Automation Testing Do Not Delete" file and perform right click operation
    And I click on the share option
    And I verify "Share with others" alert displayed with "Cancel" & "Save" buttons
    And I wait for 1 seconds
    And I enter "Alex Greenwood" in the "Enter names or emails..." field
    And I wait for 5 seconds
    And I click on the first option
    And I click the "Save" button
    Then I verify "Permission updated successfully" popup is displayed
    And I wait for 2 seconds
    And I move to "Automation Testing Do Not Delete" file and perform right click operation
    And I click on the share option
    Then I verify duplicate member is not get added

  Scenario: verify Share with others alert - Cancel button
    And I click on the "Templates"
    And I wait for 1 seconds
    And I move to "Automation Testing Do Not Delete" file and perform right click operation
    And I click on the share option
    And I verify "Share with others" alert displayed with "Cancel" & "Save" buttons
    And I click the "Cancel" button
    And I wait for 1 seconds
    Then I verify "Share with others" alert is closed

  Scenario: verify permission list options
    And I click on the "Templates"
    And I wait for 1 seconds
    And I move to "Automation Testing Do Not Delete" file and perform right click operation
    And I click on the share option
    And I click the row 2 permission button
    And I wait for 2 seconds
    Then I verify following list is present
      | Manager           |
      | Content Manager   |
      | Contributor       |
      | Commenter         |
      | Viewer            |
    And I wait for 1 seconds

  Scenario Outline:verify user is able to change permission
    And I click on the "Templates"
    And I wait for 1 seconds
    And I move to "Automation Testing Do Not Delete" file and perform right click operation
    And I click on the share option
    And I click the row 2 permission button
    And I wait for 2 seconds
    And I select "<Permission>" from permission list option
    And I wait for 1 seconds
    And I click the "Save" button
    Then I verify "Permission updated successfully" popup is displayed

    Examples:
    | Permission        |
    | Content Manager   |
    | Contributor       |
    | Commenter         |
    | Viewer            |
#    | Manager           |  //Organiser role is only valid for shared drives

  Scenario: Verify delete option visible when user Hover over on file
    And I click on the "Templates"
    And I wait for 1 seconds
    When I move to row 2
    Then I verify following buttons are displayed in Action column
      | delete_outline    |

  Scenario: Verify Rename file Functionality
    And I click on the "Templates"
    And I wait for 1 seconds
    And I click on the "arrow_right" for the "Client Folder"
    And I wait for 1 seconds
    And I move to "Commercials" file and perform right click operation
    And I click on the Rename option
    Then I verify Rename alert displayed
    And I enter "<random>" in the Rename field
    And I click the "OK" button
    And I wait for 1 seconds
    Then I verify the successful validation message
    And I restore the changes

  Scenario: Verify Rename popup Cancel button functionality
    And I click on the "Templates"
    And I wait for 1 seconds
    And I click on the "arrow_right" for the "Client Folder"
    And I wait for 1 seconds
    And I move to "Commercials" file and perform right click operation
    And I click on the Rename option
    Then I verify Rename alert displayed
    And I click the "Cancel" button
    And I wait for 1 seconds
    Then I verify Rename alert is closed

#  Scenario: Verify Add a shortcut -> Add button functionality
##    TODO After bug fixed
#    #Bug -> Add button not working
#    And I click on the "Archived Users"
#    And I wait for 1 seconds
#    And I move to "gupta_10012 suresh_10012" file and perform right click operation
#    And I click on the Add a shortcut option
#    And I wait for 2 seconds
#    And I enter "Paul Lees" into Enter names or Emails field
#    And I click on the first option
#    And I wait for 4 seconds
#    And I check row 3 popup checkbox
#    And I click the "Add" button
#    Then I verify "Permission updated successfully" popup is displayed

  Scenario: Verify Add a shortcut -> Cancel button functionality
    And I click on the "Templates"
    And I wait for 1 seconds
    And I click on the "arrow_right" for the "Client Folder"
    And I wait for 1 seconds
    And I move to "Commercials" file and perform right click operation
    And I click on the Add a shortcut option
    And I wait for 3 seconds
    Then I verify "Add Users files" popup "is" displayed
    And I wait for 1 seconds
    When I click the "Cancel" button
    And I wait for 1 seconds
    Then I verify "Add Users files" popup "is not" displayed

  Scenario: Verify Copy Folders functionality
#    TODO
    And I click on the "Projects"
    And I wait for 1 seconds
    And I click on the "Name" table column
    And I click on the Copy Folders option
    And I select "Templates" member from Enter Name or Emails dropdown
    And I wait for 2 seconds
#    And I wait for the shared drive file container to be visible
    And I check row 1 popup checkbox of "mainTBody" grid
    And I get the folder name for "row 1"
    And I click the "Add" button
    And I refresh the page
    And I click on the "Projects"
    Then I verify selected folder is displayed in drive

  Scenario: Verify Create New Folder -> OK button Functionality
    And I click on the "Templates"
    And I wait for 1 seconds
    And I right click on the "arrow_right" for the "Automation Testing Do Not Delete"
    And I click on the Create New Folder option
    And I wait for 2 seconds
    And I enter "Automation <current date>" in the "Folder Name" field
    And I click the "OK" button
    And I wait for 1 seconds
#    Then I verify "New Folder created successfully" popup is displayed
    And I refresh the page
    And I click on the "Templates"
    And I wait for 1 seconds
    And I click on the "arrow_right" for the "Automation Testing Do Not Delete"
    And I verify new folder has created

  Scenario: Verify Create New Folder -> cancel button Functionality
    And I click on the "Templates"
    And I wait for 1 seconds
    And I right click on the "arrow_right" for the "Automation Testing Do Not Delete"
    And I click on the Create New Folder option
    And I wait for 2 seconds
    Then I verify "New folder" popup "is" displayed
    And I wait for 1 seconds
    And I click the "Cancel" button
    And I wait for 1 seconds
    Then I verify "New folder" popup "is not" displayed

  Scenario: Verify "This field is required" validation for Create New Folder functionality
    And I click on the "Templates"
    And I wait for 1 seconds
    And I right click on the "arrow_right" for the "Automation Testing Do Not Delete"
    And I click on the Create New Folder option
    And I wait for 1 seconds
    And I click the "OK" button
    And I wait for 1 seconds
    Then I verify "This field is required." error message is displayed


  Scenario: Verify Remove button Functionality
    And I click on the "Templates"
    And I wait for 1 seconds
    And I click on the "arrow_right" for the "Automation Testing Do Not Delete"
    And I wait for 1 seconds
    And I move row 1 folder and right click
    And I click on the Remove option
    And I wait for 2 seconds
    Then I verify "Delete Drive Files" popup "is" displayed
    And I click the "OK" button
    Then I verify "1 file has been successfully deleted" popup is displayed
    And I refresh the page
    And I click on the "Templates"
    And I wait for 1 seconds
    And I click on the "arrow_right" for the "Automation Testing Do Not Delete"
    And I verify folder is got deleted

  Scenario: Verify Add Members And Manage Access functionality
    When I right click on the "Jellyfish Technologies - Collaboration Drive" drive
    And I click on the Add Members And Manage Access
    And I select "<random>" member from Enter Name or Emails dropdown
    And I click the "Save" button
    Then I verify "Permission updated successfully" popup is displayed
#    And I right click on the "Templates" drive

  Scenario: Verify remove Members from drive functionality
    When I right click on the "Jellyfish Technologies - Collaboration Drive" drive
    And I click on the Add Members And Manage Access
    And I select "<random>" member from Enter Name or Emails dropdown
    And I click the "Save" button
    Then I verify "Permission updated successfully" popup is displayed
    And I wait for 1 seconds
    And I right click on the "Jellyfish Technologies - Collaboration Drive" drive
    And I click on the Add Members And Manage Access
    And I remove added member from drive
    And I click the "Save" button
    Then I verify "Permission updated successfully" popup is displayed

  Scenario: Verify Make a Copy Functionality
    And I click on the "Templates"
    And I wait for 1 seconds
    And I right click on the "arrow_right" for the "Automation Testing Do Not Delete"
    And I click on the Make a Copy option
    And I select "<random>" member from Enter Name or Emails dropdown
    And I click the "Save" button
    Then I verify "Copy operation done with 1 success and 0 errors" popup is displayed
    And I verify copy file should display in respective peoples drive

  Scenario: Verify Make a Copy Functionality for multiple files
    #TODO

  Scenario: Verify Copy Folders Functionality for multiple files
    #TODO