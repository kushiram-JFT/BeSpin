package Steps;

import Base.BaseUtil;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;

public class LoginSteps extends BaseUtil {

    @Given("I am on the login page")
    public void iAmOnTheLoginPage() {

        login.navigateToLogin();
        driver.manage().window().maximize();
    }

    @Given("I am on the {string} login page")
    public void iAmOnTheLoginPage(String environment) {
        login.navigateToLogin(environment);
        driver.manage().window().maximize();
    }

    @When("I enter a(n) {string} email address and {string} password")
    public void iEnterEmail(String userType, String passType) {
        if(!userType.equalsIgnoreCase("empty")) login.enterUserEmail(userType);
        if(!passType.equalsIgnoreCase("empty")) login.enterUserPassword(passType);
    }

    @Then("I see the {string} error message")
    public void iSeeTheErrorMessage(String errMsg) {
        System.out.println("Checking the error message.");
        String result = login.confirmErrorMsg();
        Assert.assertTrue(result.contains(errMsg), "Expected error message \""+errMsg+"\" not found\n" +
                "Actual message: \""+result+"\"");
        Assert.assertTrue(login.confirmOnLoginPage(), "No longer on login page after failed login attempt!");
        System.out.println("\""+result+"\" error message displayed.");
    }

    @Then("I see the empty {string} field warning")
    public void thereIsNoErrorDisplayed(String field) {
        System.out.println("There should be a warning on the empty field.");
        String warningMsg = "Please fill out this field.";
        //Note: these warnings are browser-specific. This code was written for Chrome.
        Assert.assertEquals(login.warningMsgDisplayed(field), warningMsg);
        Assert.assertFalse(login.errorMsgDisplayed(), "An error message was displayed, but was not expected to.");
        Assert.assertTrue(login.confirmOnLoginPage(), "Appear to not be on the Login page.");
    }

    @Then("I see the email error message")
    public void iSeeTheEmailErrorMessage() {
        // full message looks something like "Please include an '@' in the email address. 'carlwu_patracorp.com' is missing an '@'."
        //Note: these warnings are browser-specific. This code was written for Chrome.
        Assert.assertTrue(login.warningMsgDisplayed("email").contains("Please include an '@' in the email address."), "The warning message was not found, or had incorrect text.");
        Assert.assertTrue(login.confirmOnLoginPage(), "Appear to not be on the Login page.");
    }

    @And("I click the Sign In button")
    public void iClickTheSignInButton() {
        System.out.println("Clicking Sign In button\n");
        login.ClickSignIn();
    }

    @Then("I am redirected to login page")
    public void iAmRedirectedToLoginPage() {
        System.out.println("Checking the error message.");
        String expectedMsg = "You are now logged out";
        String actualMsg = login.confirmSuccessMsg();
        Assert.assertTrue(actualMsg.contains(expectedMsg), "The success message \""+expectedMsg+"\" could not be found.\n" +
                "Actual message: \""+actualMsg+"\"");
        Assert.assertTrue(login.confirmOnLoginPage(), "Appear to not be on the Login page.");
    }

    @Then("I will be taken to the apps page")
    public void iWillBeTakenToTheAppsPage() {
        Assert.assertTrue(login.onCorrectPage(), "Apps selection page not displayed");

    }

    @When("I click on the {string} tile")
    public void iClickOnTheTile(String tileName) {
        currentApp = tileName;
        System.out.println("Clicking on " + tileName + " tile");
        login.clickOnTile(tileName);
        pageLoaded();
    }

    @Then("I will be taken to the homepage for that app")
    public void iWillBeTakenToTheHomepageForThatApp() {
        pageLoaded();
        // TODO change 'true' in assertion to a function that uses a boolean to verify you have reached the app homepage
        Assert.assertTrue(true, "Homepage for " + currentApp + " not displayed");
        System.out.println("On homepage for " + currentApp);
    }

    @When("I enter the email and password for the {string}")
    public void iEnterTheEmailAndPasswordForThe(String user) {
        currentLogin = user;
        login.enterUserEmail(user);
        login.enterUserPassword(user);
    }


    @And("I reset the password for {string}")
    public void iResetThePasswordFor(String user) {
        if (user.contains("<gmail>")) {
            String email = login.getLogins().getProperty("gmail.email");
            if (user.contains("+")) {
                String modifier = user.substring(user.indexOf('+'));
                String[] eSplit = email.split("@");
                email = eSplit[0] + modifier + "@" + eSplit[1];
            }
            user = email;
        }
        System.out.println("Resetting password for \"" + user + "\".");
        Assert.assertTrue(commonFunctions.commonFieldEnter("Email address", user), "Could not find email address field!");
        driver.findElement(By.id("resetsubmit")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email_succ_msg")));

    }

    @And("I enter {string} email address")
    public void iEnterEmailAddress(String userType) {
        if(!userType.equalsIgnoreCase("empty")) login.enterUserEmail(userType);
    }

    @And("I enter {string} password")
    public void iEnterPassword(String passType) {
        if(!passType.equalsIgnoreCase("empty")) login.enterUserPassword(passType);
    }

    @Then("I verify Patronum home page is displayed")
    public void iVerifyPatronumHomePageIsDisplayed() {
        try {
            WebElement patronumLogo = driver.findElement(By.xpath("//section[@class='mdc-top-app-bar__section mdc-top-app-bar__section--align-start']//img[@class='img-responsive']"));
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//section[@class='mdc-top-app-bar__section mdc-top-app-bar__section--align-start']//img[@class='img-responsive']")));
            Assert.assertTrue(patronumLogo.isDisplayed(), "Home page is not displayed");
            System.out.println("On Home page");
        }catch (NoSuchElementException e){

        }
    }
}
