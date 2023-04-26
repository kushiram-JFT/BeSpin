Feature: Profile Edit Testing

  Background:
    Given I am on the login page
#    And I login into the patronum app as "super user"
    And I wait for 1 seconds
    Then I verify Patronum home page is displayed
    When I click the "People" link
    And "Patronum - People" page is opened

  Scenario: Filtering people according to job-title,department,First Name,Last Name
    ##---Job -Title--##
    When I enter "Defender" in the "Search for Users" field
    And I click the search icon
    Then I verify results shown as per search criteria
    ##---Department--##
    When I enter "England" in the "Search for Users" field
    And I click the search icon
    Then I verify results shown as per search criteria
    ##---First Name--##
    When I enter "Alex" in the "Search for Users" field
    And I click the search icon
    Then I verify results shown as per search criteria
    ##---Last Name--##
    When I enter "Mclean" in the "Search for Users" field
    And I click the search icon
    Then I verify results shown as per search criteria
    ##---Number--##
    When I enter "123" in the "Search for Users" field
    And I click the search icon
    Then I verify results shown as per search criteria
    ##---Invalid Input--##
    When I enter "#$@%" in the "Search for Users" field
    And I click the search icon
    Then I verify "did not match anything." is shown
    ##---Entering Spaces--##
    When I enter "  " in the "Search for Users" field
    And I click the search icon
    Then I verify "did not match anything." is shown

  Scenario: Verify the existence of button in users profile for super admin
    When I open random user profile
    Then The following elements exist
      | profile                 |
      | groups                  |
      | calendars               |
      | contacts                |
      | files                   |
      | signature               |
      | settings                |
      | edit                    |
      | settings_backup_restore |
      | delete_outline          |
      | more_horiz              |

  Scenario: Verify the existence of fields,dropdown in user details form
    When I open random user profile
    And I open user details form
    Then The following elements exist
      | Alias            |
      | Domain           |
      | Phone            |
      | Address          |
      | Type             |
      | Employee ID      |
      | Job Title        |
      | Type of employee |
      | Organization     |
      | Department       |
      | Location         |
      | Cost Centre      |
      | Building id      |
      | Floor name       |
      | Floor section    |
      | Recovery email   |
      | Recovery phone   |
      | Pronouns         |
      | Working Hours    |

  Scenario: Verify entered information got added in the form
    When I open random user profile
    And I open user details form
    And I enter the following information into the form
      | Employee ID     | 123                |
      | Job title       | Defender           |
      | Type of employee| TEST QA            |
      | Location        | England            |
      | Building id     | 123                |
      | Floor name      | 3 floor            |
      | Pronouns        | He/She             |
      | Floor section   | 123                |
      | Working Hours   | None               |
    And I click the "OK" button
    Then I verify "User Details Updated Successfully" popup is displayed
    And I open user details form
    Then The record is added
      | Employee ID      |
      | Job title        |
      | Type of employee |
      | Location         |
      | Building id      |
      | Floor name       |
      | Floor section    |
      | Pronouns         |
      | Working Hours    |

  Scenario: Verify User profile More (...) button
    When I open random user profile
    And I wait for 1 seconds
    And I click the "more_horiz" button
    Then I verify following options are present
    | Suspend | Data transfer | Move | Hide | End Date | Emergency Offboard|

  Scenario: Verify Data transfer feature
    #TODO
    When I open random user profile
    And I wait for 1 seconds
    And I click the "more_horiz" button
    And I click the "Data transfer" option of the user profile
    And I click the Transfer ownership dropdown in Transfer ownership popup
    And I select "Drive" from transfer ownership
#    And I select "<random>" member from the list
    And I click the Drive dropdown in Transfer ownership popup
    And I select "Projects" from transfer ownership

  Scenario: Verify the Ability to Switch Between Multiple Profiles
    When I open random user profile
    And I click on random department user on users profile page
    Then I verify selected users profile get opened
    And I click on random department user on users profile page
    Then I verify selected users profile get opened

  Scenario: Verify a Normal User Cannot Access Certain Features
    And I Click on user icon
    And I click the "Sign Out" button
    And I open new tab and switch to that tab
    And I login into the patronum app as "standard user"
    Then The following elements does not exist
      | People  | Groups  | Contacts | Policies | Roles | Setting | notifications |
    When I open random user profile
    Then The following elements does not exist
      | profile  | groups  | calendars | contacts | files | signature | settings | edit | settings_backup_restore | delete_outline | more_horiz |

  Scenario: Verify a Normal User Can Edit Their Own Profile Information
    #TODO
    And I Click on user icon
    And I click the "Sign Out" button
    And I open new tab and switch to that tab
    And I login into the patronum app as "standard user"
    And I open the profile of that user
    Then The following elements does not exist
      | profile  | groups  | calendars | contacts | files | signature | settings | edit | settings_backup_restore | delete_outline | more_horiz |
    And I open user details form

  Scenario: Verify the Ability to Switch Between Multiple Profiles for Normal User
    And I Click on user icon
    And I click the "Sign Out" button
    And I open new tab and switch to that tab
    And I login into the patronum app as "standard user"
    When I open random user profile
    And I click on random department user on users profile page
    Then I verify selected users profile get opened
    And I click on random department user on users profile page
    Then I verify selected users profile get opened

  Scenario: Verify Ability to switch between chart view,card view,List view for Normal user and super user
    And I move to "view_comfy" button
    Then I see "Chart View" is display
    And I click the "view_comfy" button
    And I move to "view_day" button
    Then I see "Card View" is display
    And I click the "view_day" button
    And I move to "format_list_bulleted" button
    Then I see "List View" is display
    And I click the "format_list_bulleted" button
    And I move to "view_comfy" button
    Then I see "Chart View" is display

  Scenario: Privileges User
    And I Click on user icon
    And I click the "Sign Out" button
    And I open new tab and switch to that tab
    And I login into the patronum app as "privileges user"

    ##---- Verify a Privileges User Cannot Access Certain Features
    And The following elements exist
      | People        |
      | Groups        |
    When I open random user profile
    Then The following elements exist
      | profile                 |
      | groups                  |
      | edit                    |
      | settings_backup_restore |
      | delete_outline          |
      | more_horiz              |
    And I open user details form
    Then The following elements exist
      | Alias            |
      | Domain           |
      | Phone            |
      | Address          |
      | Type             |
      | Employee ID      |
      | Job Title        |
      | Type of employee |
      | Organization     |
      | Department       |
      | Location         |
      | Cost Centre      |
      | Building id      |
      | Floor name       |
      | Floor section    |
      | Recovery email   |
      | Recovery phone   |

    ##---- Verify entered information got added in the form
    And I enter the following information into the form
      | Employee ID     | 123                |
      | Job title       | Defender           |
      | Type of employee| TEST QA            |
      | Location        | England            |
      | Building id     | 123                |
      | Floor name      | 3 floor            |
      | Floor section   | 123                |
    And I click the "OK" button
    Then I verify "User Details Updated Successfully" popup is displayed
    And I open user details form
    Then The record is added
      | Employee ID      |
      | Job title        |
      | Type of employee |
      | Location         |
      | Building id      |
      | Floor name       |
      | Floor section    |