# All node features have a common sign-in feature. You can drop this feature file into
# any other automation project (and update the the tags for each scenario as necessary)
  # You will need to create some users and a file with some credentials called loginsDev.properties
  # It should contain:
  # # Super Users
  # email.superuser=*your company email*
  # email.superuser=*your development password*
  #
  ## Other login credentials
  # email.invalidUser={fake email}<current date>@BeSpin.com
  # pass.invalidPassword={fake password}
  # email.validUser={email}<current date>@BeSpin.com
  # pass.validPassword={valid password}
  ## "invalid format" means an email without an @ symbol
  # email.incorrectUser={email without @ symbol}
@ignore
Feature: Login Functionality
  Background: Landing Page Login
    Given I am on the login page
    And I wait for 1 seconds

  @XXXX
  Scenario: Login to node application With InvalidUserName and ValidPassword.
    When I enter an "invalid user" email address and "valid user" password
    And I click the "Sign in" button
    Then I see the "Login failed: Invalid user or password. Remaining Attempts:" error message

  @XXXX
  Scenario: Login to node application With Valid User and inValid password
    When I enter a "valid user" email address and "invalid user" password
    And I click the "Sign in" button
    Then I see the "Login failed: Invalid user or password. Remaining Attempts:" error message

  @XXXX
  Scenario: Login to node application With inValid User and inValid password
    When I click on the "Sign in with Google" link
    And I wait for 1 seconds
    When I enter "invalid user" email address
    And I wait for 1 seconds
    And I click the "Next" button
    Then I see the "Couldn't find your Google Account" error message
    And I wait for 1 seconds

  @XXXX
  Scenario: Login to node application With inactive User.
    When I enter a "inactive user" email address and "inactive user" password
    And I click the "Sign in" button
    Then I see the "Sorry! Access denied for this account" error message

  @XXXX
  Scenario: Login to node application With Empty UserName And Password
   When I enter an "empty" email address and "valid user" password
   And I click the "Sign in" button
   Then I see the empty "email" field warning

  @XXXX
  Scenario: Login to node application With valid userName And Empty Password.
  When I enter a "valid user" email address and "empty" password
  And I click the "Sign in" button
  Then I see the empty "password" field warning

  @XXXX
  Scenario: Login to node application With empty username and empty password.
  When I enter a "empty" email address and "empty" password
  And I click the "Sign in" button
  Then I see the empty "email" field warning

  @XXXX
  Scenario: Login to node application With incorrect format of username & valid/invalid password
    When I enter a "incorrect user" email address and "valid user" password
    And I click the "Sign in" button
    Then I see the email error message

  @XXXX
  Scenario: Login to node application With valid UserName And validPassword.
    When I click on the "Sign in with Google" link
    And I wait for 1 seconds
    And I enter "valid user" email address
    And I click the "Next" button
    And I wait for 2 seconds
    And I enter "valid user" password
    And I click the "Next" button
    And I wait for 5 seconds
    Then I verify Patronum home page is displayed
    And The following elements exist
      | People        |
      | Groups        |
      | Contacts      |
      | Drives        |
      | Policies      |
      | Roles         |
      | Setting       |
      | notifications |
      | Google Apps   |
    And I click the "Groups" link
    And I click the "People" link
    And I click the "Policies" link
    And I click the "Roles" link
    And I click the "Setting" link
    And I click the "Drives " link
    And I click the "Contacts" link





  @XXXX
  Scenario: VerifyLogout functionality
     When I enter the email and password for the "Super User"
     And I click the "Sign in" button
     Then I will be taken to the apps page
     When I Click on user icon
     And I click Logout button
     Then I am redirected to login page

