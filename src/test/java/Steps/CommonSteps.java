package Steps;

import Base.BaseUtil;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

import java.util.List;
import java.util.Map;

/**
 * This class holds the step definitions of all the common archetype items (forms, grids, and KPIs). The code for all the steps is located in the {@link Base.CommonStepFunctions CommonStepFunctions} class.
 *
 * <p>If any changes need to be made to the code in a common step, they should be made in this class. Remove the call to CommonStepFunctions and add your code here.
 */
public class CommonSteps extends BaseUtil {
    // FormSteps Begins

    @When("I click the {string} button")
    public void iClickTheButton(String button) {
        commonStepFunctions.iClickTheButton(button);
    }

    @And("I select {string} from the {string} drop down")
    public void iSelectFromTheDropDown(String selection, String dropDown) {
        commonStepFunctions.iSelectFromTheDropDown(selection, dropDown);
    }

    @And("I edit the following drop downs")
    public void iEditTheFollowingDropDowns(List<Map<String, String>> table) {
        commonStepFunctions.iEditTheFollowingDropDowns(table);

    }

    @And("I get the value for the {string} drop down")
    public void iGetTheValueForTheDropDown(String dropDown) {
        commonStepFunctions.iGetTheValueForTheDropDown(dropDown);
    }

    @And("I enter {string} in the {string} field")
    public void iEnterInTheField(String text, String fieldName) {
        commonStepFunctions.iEnterInTheField(text, fieldName);
    }

    @And("I edit the following fields")
    public void iEditTheFollowingFields(List<Map<String, String>> table) {
        commonStepFunctions.iEditTheFollowingFields(table);
    }

    @And("I click {string} checkbox")
    public void iClickCheckbox(String checkBox) {
        commonStepFunctions.iClickCheckbox(checkBox);
    }

    @And("I select the {string} checkbox")
    public void iSelectCheckbox(String checkBox) {
        commonStepFunctions.iSelectCheckbox(checkBox);
    }

    // could possibly combine this with the previous stepdef as "I {string} the {string} checkbox"
    @And("I deselect the {string} checkbox")
    public void iDeselectCheckbox(String checkBox) {
        commonStepFunctions.iDeselectCheckbox(checkBox);
    }

    @Then("The {string} checkbox is {string}")
    public void checkboxSelected(String checkBox, String expectation) {
        commonStepFunctions.checkboxSelected(checkBox, expectation);
    }

    // GridSteps Begins
    @And("I get the {string} for {string}")
    public void iGetTheFor(String colName, String rowName) {
        commonStepFunctions.iGetTheFor(colName, rowName);
    }

    @When("I navigate to the {string} tab")
    public void iNavigateToTheTab(String tabName) {
        commonStepFunctions.iNavigateToTheTab(tabName);
    }

    @When("I select the {string} option from the the Viewing drop down")
    public void iSelectTheOptionFromTheTheViewingDropDown(String rowOption) {
        commonStepFunctions.iSelectTheOptionFromTheTheViewingDropDown(rowOption);
    }

    @Then("The number of rows displayed will be less than or equal to the number selected")
    public void theNumberOfRowsDisplayedWillBeLessThanOrEqualToTheNumberSelected() {
        commonStepFunctions.theNumberOfRowsDisplayedWillBeLessThanOrEqualToTheNumberSelected();
    }

    @And("I enter {string} into the {string} grid header")
    public void iEnterIntoTheGridHeader(String textEntry, String header) {
        commonStepFunctions.iEnterIntoTheGridHeader(textEntry, header);
    }

    @And("I get the text from the {string} grid header")
    public void iGetTheTextFromTheGridHeader(String headerName) {
        commonStepFunctions.iGetTheTextFromTheGridHeader(headerName);
    }

    @And("I select {string} from the {string} header in the grid")
    public void iSelectFromTheHeaderInTheGrid(String selection, String header) {
        commonStepFunctions.iSelectFromTheHeaderInTheGrid(selection, header);
    }

    @When("I get the number of records in the {string} tab")
    public void iGetTheNumberOfRecordsInTheTab(String tabName) {
        commonStepFunctions.iGetTheNumberOfRecordsInTheTab(tabName);
    }

    @And("I check if {string} is selected in the {string} header")
    public void iCheckIfIsSelectedInTheHeader(String selection, String headerName) {
        commonStepFunctions.iCheckIfIsSelectedInTheHeader(selection, headerName);

    }

    @And("I check if the following items are selected in the {string} header")
    public void iCheckIfTheFollowingItemsAreSelectedInTheHeader(String headerName, List<List<String>> table) {
        commonStepFunctions.iCheckIfTheFollowingItemsAreSelectedInTheHeader(headerName, table);
    }

    @And("I get the {string} for {string} of the grid")
    public void iGetTheForRowOfTheGrid(String columnName, String rowNumber) {
        commonStepFunctions.iGetTheForRowOfTheGrid(columnName, rowNumber);
    }

    @And("Verify the following headers are present")
    public void verifyTheFollowingHeadersArePresent(List<List<String>> table) {
        commonStepFunctions.verifyTheFollowingHeadersArePresent(table);
    }

    @When("I store the {string}")
    public void iStoreThe(String labelName) {
        commonStepFunctions.iStoreThe(labelName);
    }

    @And("The {string} button {string} displayed")
    public void theButtonDisplayed(String buttonName, String expectation) {
        commonStepFunctions.theButtonDisplayed(buttonName, expectation);
    }

    @Then("The following elements exist")
    public void theFollowingElementsExist(List<String> table) {
        commonFunctions.fieldsExist(table);
    }

    @And("I enter the following information into the form")
    public void iEnterTheFollowingInformationIntoTheForm(Map<String, String> table) {
        commonStepFunctions.iEnterTheFollowingInformationIntoTheForm(table);
    }
}
