package Steps;

import Base.BaseUtil;
import Pages.BeSpin;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.MoveTargetOutOfBoundsException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.System.*;
import static org.openqa.selenium.support.locators.RelativeLocator.with;

public class StepDefs extends BaseUtil {


    @And("I open the URL {string}")
    public void iOpenTheURL(String url) {

        driver.get(url);
        pageLoaded();
    }

    @When("I Click on user icon")
    public void iClickOnUserIcon() {
        System.out.println("Clicking User Icon");
        login.ClickUserIcon();
    }

    @And("I click Logout button")
    public void iClickLogoutButton() {
        System.out.println("Clicking Logout button");
        login.ClickLogOut();
    }

    @And("I wait for {string} seconds")
    public void iWaitForSeconds(String seconds) throws InterruptedException {
        int timeToWait = Integer.parseInt(seconds) * 1000;
        Thread.sleep(timeToWait);
    }

    @And("I take a screenshot")
    public void iTakeAScreenshot() {
        String dateString = dateFormat.format(new Date());
        String timeString = timeFormat.format((new Date()));
        System.out.println("attempting to add to file: " + filePath + "\\screenshots\\" + BaseUtil.scenarioName + " " + dateString + " " + timeString + ".png");

        File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(srcFile, new File(filePath + "\\screenshots\\" + BaseUtil.scenarioName + dateString + timeString + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @And("I get the {string} info from valueStore")
    public void iGetTheInfoFromValueStore(String storeItem) throws InterruptedException {
        js.executeScript("alert('" + storeItem + " in valueStore: " + valueStore.get(storeItem) + "')");
        Thread.sleep(5000);
        driver.switchTo().alert().accept();
        System.out.println(storeItem + " in valueStore: " + valueStore.get(storeItem));
    }

    @And("I clear the valueStore")
    public void iClearTheValueStore() {
        valueStore.clear();
        editedValues.clear();
    }

    @And("I click on the {string} link")
    public void iClickOnTheLink(String linkText) {
        Set<String> tabs = driver.getWindowHandles();
        // Storing the current tab in the valueStore as "hometab" so it can be returned to at a later step if necessary
        valueStore.put("hometab", driver.getWindowHandle());
        System.out.println("Clicking the \"" + linkText + "\" link.");
        Assert.assertTrue(commonFunctions.commonLinkClick(linkText), "Could not find or click the \"" + linkText + "\" link.");
        pageLoaded();
        // Checking if a new tab was opened and switching to it.
        if (driver.getWindowHandles().size() > tabs.size()) {
            driver.getWindowHandles().forEach(handle -> {
                if (!tabs.contains(handle)) driver.switchTo().window(handle);
            });
        }

    }

    @And("I add {string} key and {string} value to valueMap")
    public void iAddKeyAndValueToValueMap(String key, String value) {
        valueStore.put(key, value);
    }

    @And("I print out the valueStore")
    public void iPrintOutTheValueStore() {
        valueStore.forEach((key, value) -> System.out.println(key + " - " + value));
    }

    @And("I fail the test")
    public void iFailTheTest() {
        Assert.fail("Failing test for testing purpose");
    }


    @And("I add the following items to the valueMap")
    public void iAddTheFollowingItemsToTheValueMap(Map<String, String> table) {
        valueStore.putAll(table);
    }

    @And("I wait for {int} seconds")
    public void iWaitForSeconds(int seconds) throws InterruptedException {
        int timeToWait = seconds * 1000;
        Thread.sleep(timeToWait);
    }

    @And("I open Work Order {string}")
    public void iOpenWorkOrder(String woNum) {
        int rows = commonFunctions.gridRowCount();
        commonFunctions.gridHeaderFieldWrite("Work Order #", woNum);
        long timer = System.currentTimeMillis();
        while (commonFunctions.gridRowCount() == rows && (System.currentTimeMillis() - timer) < 5000) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                break;
            }
        }
        commonFunctions.gridEntry("row 1", "Work Order #").findElement(By.tagName("a")).click();
        pageLoaded();
    }

    @And("I click the {string} link")
    public void iClickTheLink(String link) {
        System.out.println("Clicking " + link + " link");
        try {
            Actions actions = new Actions(driver);
            actions.moveToElement(driver.findElement(By.xpath("//span[text()='" + link + "']"))).click().build().perform();
        } catch (MoveTargetOutOfBoundsException e) {

        }
        pageLoaded();
    }

    @And("I wait for the page to load")
    public void iWaitForThePageToLoad() {
        pageLoaded();
    }

    @And("I click {string} checkbox in the header")
    public void iClickCheckboxInTheHeader(String checkbox) {
        System.out.println("Clicking " + checkbox + " checkbox");
        WebElement selectAllGroupsCheckbox = driver.findElement(By.xpath("//input[@id='"+ checkbox +"']"));
        selectAllGroupsCheckbox.click();
    }

    @And("I select {string} from filter")
    public void iSelectFromFilter(String filterOption) {
        List<WebElement> filterList = driver.findElements(By.xpath("//ul[@class='mdc-list']//li[normalize-space()='" + filterOption + "']"));
        if (filterList.size() > 0){
            for (WebElement filter : filterList){
                if (filter.isDisplayed()) {
                    filter.click();
                    System.out.println("Select \"" + filterOption + "\" option");
                    break;
                }
            }
        }else {
            Assert.fail("Filter option: " + filterOption + " is not found");
        }

    }

    @Then("There are results")
    public void thereAreResults() {
        System.out.println("Checking that some values were returned by the search.");
        Assert.assertTrue(commonFunctions.gridRowCount() >= 1);
        System.out.println("Total Records: " + commonFunctions.gridRowCount());
    }

    @And("I verify that value of {string} for {string} is {string}")
    public void iVerifyThatValueOfForIs(String columnName, String rowNumber, String value) {
        headerChoice = columnName;
        headerInfo = commonFunctions.gridEntry(rowNumber, columnName).getText();
        System.out.println("I expect " + value + " and I found " + headerInfo);
        Assert.assertTrue(headerInfo.contains(value), "Expected " + value + " but found " + headerInfo);
    }

    @And("I click the {string} button of filter")
    public void iClickTheButtonOfFilter(String button) {
        WebElement closeButton = driver.findElement(By.xpath("//div[@class='mdc-chip']//i[text()='" + button + "']"));
        if (closeButton.isDisplayed()){
            closeButton.click();
        }else {
            System.out.println(button+ " button is not found");
        }
//        if (commonFunctions.commonButton(button).isDisplayed()) {
//            commonFunctions.commonButtonClick(button);
//        }
    }

    @And("I scroll down to the bottom of webpage")
    public void iScrollDownToTheBottomOfWebpage() {
        System.out.println("Scrolling down");
        js.executeScript("window.scrollTo(0, document.querySelector('#content').scrollTop=21377)");
    }

    @And("I move to row {int} in {string} column in grid record")
    public void iMoveToRowInColumnInGridRecord(int rowNumber, String colName) {
        String number = String.valueOf(rowNumber);
        Actions actions = new Actions(driver);
        actions.moveToElement(commonFunctions.gridEntry("row" + number, colName)).build().perform();
    }

    @Then("There is no result")
    public void thereIsNoResult() {
        Assert.assertTrue(commonFunctions.gridRowCount() == 0, "There are results");
        System.out.println("There is no result");
    }

    @Then("I verify all visible records get selected")
    public void iVerifyAllVisibleRecordsGetSelected() {
        SoftAssert softAssert = new SoftAssert();
        int gridRow = commonFunctions.gridRowCount();
        for (int i=1; i<=gridRow; i++) {
            softAssert.assertTrue(commonFunctions.commonCheckBoxSelected(i),"checkbox for row number "+ i +" not selected");
            System.out.println("Checkbox for row "+ i +" is selected");
        }
        softAssert.assertAll();
    }

    @And("I select row {int} checkbox")
    public void iSelectRowCheckbox(int row) {
//        if (!commonFunctions.commonCheckBoxSelected(row)) {
            Assert.assertTrue(commonFunctions.commonCheckBoxClick(row), "Checkbox for Row " + row + " is not selected");
            System.out.println("Checkbox for Row " + row + " is selected");
//        }
    }

    @Then("I verify row {int} checkbox is deselected")
    public void iVerifyRowCheckboxIsDeselected(int row) {
        Assert.assertFalse(commonFunctions.commonCheckBoxSelected(row));
        System.out.println("Checkbox for Row "+ row +" is deselected");
    }

    @Then("I verify all the selected records get deselected")
    public void iVerifyAllTheSelectedRecordsGetDeselected() {
        SoftAssert softAssert = new SoftAssert();
        int gridRow = commonFunctions.gridRowCount();
        for(int i=1; i<=gridRow; i++){
            softAssert.assertFalse(commonFunctions.commonCheckBoxSelected(i),"Checkbox for Row "+ i +" is Still selected");
            System.out.println("Checkbox for Row "+ i +" is deselected");
        }
        softAssert.assertAll();
    }

    @Then("I verify following buttons are displayed in Action column")
    public void iVerifyFollowingButtonsAreDisplayedInActionColumn(List<String> buttons) {
        for (String button : buttons){
            Assert.assertTrue(commonFunctions.commonButton(button).isDisplayed() && commonFunctions.commonButton(button).isEnabled(),button  + " button is not displayed");
            System.out.println(button  + " button is displayed");
        }
    }

    @Then("I verify total groups selected should display at the top of grid")
    public void iVerifyTotalGroupsSelectedShouldDisplayAtTheTopOfGrid() {
        try {
            List<WebElement> elements = driver.findElements(By.xpath("//tr[@class='reset-selection-row reset-selection-bg']//span[contains(@class,'msm-info')]"));
            if (elements.size() > 0){
                for (WebElement selected : elements){
                    if (selected.isDisplayed()){
                        String totalSelectedCount = selected.getText().replaceAll("\\D", "");
                        System.out.println("Total selected Groups: " + totalSelectedCount);
                        valueStore.put("Total Selected Groups", totalSelectedCount);
                    }
                }
            }
        } catch (NoSuchElementException e) {
            e.printStackTrace();
        }
    }

    @And("I scroll up")
    public void iScrollUp() {
        js.executeScript("window.scrollTo(0, document.querySelector('#content').scrollTop=-21377)");
    }

    @Then("I verify group deleted successfully popup is displayed")
    public void iVerifyGroupDeletedSuccessfullyPopupIsDisplayed() {
        WebElement groupDeletedSuc = driver.findElement(By.xpath("//div[@class='mdc-snackbar__label msm-text']"));
        if (groupDeletedSuc.isDisplayed()) {
            Assert.assertTrue(groupDeletedSuc.getText().contains("group deleted successfully.") || groupDeletedSuc.getText().contains("groups deleted successfully."));
            System.out.println("Groups deleted successfully popup displayed");
        }else {
            Assert.fail("Groups deleted successfully popup is not displayed");
        }
    }

    @And("I verify Delete group popup is displayed")
    public void iVerifyDeleteGroupPopupIsDisplayed() {
        WebElement deleteGroupPopup = driver.findElement(By.xpath("//div[@id='delete-group-alert-popup']//div[@class='mdc-dialog__surface']"));
        if (deleteGroupPopup.isDisplayed()){
            Assert.assertTrue(deleteGroupPopup.getText().contains("Are you sure, you want to delete this group?"));
            System.out.println("Delete Group Popup is displayed");
        }else {
            Assert.fail("Delete Group Popup is not displayed");
        }
    }

    @And("I select following {int} rows")
    public void iSelectFollowingGroupsRows(int count,List<Integer> rowNumber) {
        SoftAssert softAssert = new SoftAssert();
        valueStore.put("SelectCount", String.valueOf(count));
        if (rowNumber.size() == count) {
            rowNumber.forEach(row -> {
                if (commonFunctions.commonCheckBox(row) != null) {
                    commonFunctions.commonCheckBoxClick(row);
                    softAssert.assertTrue(commonFunctions.commonCheckBoxSelected(row));
                    System.out.println("Checkbox for row " + row + " is selected");
                }
            });
        }else {
            softAssert.fail("List size: "+ rowNumber.size() +" that we are passing is not equal to actual count: "+ count +"");
        }
        softAssert.assertAll();
    }

    @Then("I verify the count of groups selected for deletion,that exact count should be displayed in {string} column in dashboard tab")
    public void iVerifyTheCountOfGroupsSelectedForDeletionThatExactCountShouldBeDisplayedInDashboardInSettingTab(String column) {
        String count = commonFunctions.gridEntry("row 1", column).getText();
        int gridCount = Integer.parseInt(count);
        int selectedGroupsCount = Integer.parseInt(valueStore.get("Total Selected Groups"));
        Assert.assertEquals(gridCount,selectedGroupsCount,"Count of selected groups "+ selectedGroupsCount +" and count in "+ column +" column is "+ gridCount +" not equal, Test case fail");
        System.out.println("Count of selected groups: "+ selectedGroupsCount +" and count in "+ column +" column is: "+ gridCount +" equal, Test case pass");
    }

    @And("I click the {string} button of {string} column of row {int}")
    public void iClickTheButtonOfColumnOfRow(String button, String colName, int row) {
        String s = String.valueOf(row);
        WebElement element = commonFunctions.gridEntry("row" + s, colName);
        if (element.isDisplayed()){
            commonFunctions.editButtonClick(row);
        }
    }

    @Then("I verify {string} column for row {int} should be autopopulate")
    public void iVerifyColumnForRowShouldBeAutopopulate(String colName, int rowNumber) {
        String s = String.valueOf(rowNumber);
        WebElement element = commonFunctions.gridEntry("row" + s, colName);
        Pattern pattern = Pattern.compile("[a-zA-Z0-9][a-zA-Z0-9_.]*@[a-zA-Z0-9]+([.][a-zA-Z0-9]+)+");
        Matcher matcher = pattern.matcher(element.getText());
        String emailID = "";
        if (matcher.find()){
            emailID = matcher.group();
        }
        if (element != null && login.getLogins().getProperty("gmail.email").equals(emailID)){
            System.out.println(colName+" column is auto populate with body: "+ element.getText()+"");
        }else {
            Assert.fail(colName+" column is empty");
        }
    }

    @And("I print the total deletion request")
    public void iPrintTheTotalDeletionRequest() {
        int totalDeletionRequests = commonFunctions.gridRowCount();
        valueStore.put("TotalDeletionRequests",String.valueOf(totalDeletionRequests));
        System.out.println("Total Deletion Requests: "+ totalDeletionRequests +"");
    }

    @Then("I verify {string} error message is displayed")
    public void iVerifyErrorMessageIsDisplayed(String message) {
        String actualError = commonFunctions.errorMessageDisplay();
        Assert.assertTrue(actualError.contains(message));
        System.out.println("Error message: (\""+ message +"\") is displayed");
    }

    @Then("I verify {string} popup is displayed")
    public void iVerifyPopupIsDisplayed(String text) {
        if (text.contains("<policy name>")){
            String replacement = valueStore.get("Grid Header Input");
            text = text.replaceAll("<policy name>","\""+replacement+"\"");
        }
        try {
            commonFunctions.waitForSnackbarPopUp(text);
            WebElement snackBarPopUp = driver.findElement(By.xpath("//div[@id='snackbar-mdc-main']//div[@class='mdc-snackbar__label msm-text']"));
            if (snackBarPopUp.isDisplayed()) {
                Assert.assertTrue(snackBarPopUp.getText().contains(text) , "Expected Popup message is not displayed\n" +
                        "Actual snackbar popup is : \" "+ snackBarPopUp.getText() +"\" ");
                System.out.println(text + " Popup is displayed");
            } else {
                Assert.fail(text + " Popup is not displayed");
            }
        }catch (Exception e){
            //do nothing
        }

    }

    @And("I verify {string} column for row {int} should be auto populate with {string} message")
    public void iVerifyColumnForRowShouldBeAutoPopulateWithMessage(String colName, int rowNumber, String message) {
        commonFunctions.waitForStatusToChange();
        String s = String.valueOf(rowNumber);
        WebElement element = commonFunctions.gridEntry("row"+s, colName);
        if (element != null){
            Assert.assertTrue(element.getText().contains(message),message+" message is not displayed");
            System.out.println(message+" message is displayed in "+ colName +" column");
        }else {
            Assert.fail(colName+" column is null for row "+s);
        }
    }

    @And("I scroll horizontally")
    public void iScrollHorizontally() {
        js.executeScript("document.querySelector(\"div[id='my-dialog-content-deletion-request-list'] div[class='custom-mdc-table xs-scroll-tbl mdc-data-table msm-border-style1']\").scrollLeft=1000");
    }

    @Then("I verify Delete group popup is get closed")
    public void iVerifyDeleteGroupPopupIsGetClosed() {
        WebElement deleteGroupPopup = driver.findElement(By.xpath("//div[@id='delete-group-alert-popup']//div[@class='mdc-dialog__surface']"));
        Assert.assertFalse(deleteGroupPopup.isDisplayed(),"Delete group popup is not get closed");
        System.out.println("Delete group popup is get closed");
    }

    @Then("I verify following filter options are present")
    public void iVerifyFollowingFilterOptionsArePresent(List<String> filters) {
        SoftAssert softAssert = new SoftAssert();
        filters.forEach(filter -> {
            WebElement filterOption = driver.findElement(By.xpath("//ul[@class='mdc-list']//li[normalize-space()='" + filter + "']"));
            softAssert.assertTrue(filterOption.isDisplayed(),filterOption.getText()+" filter option is not present");
            System.out.println("\"" +filterOption.getText()+ "\" filter option is present");
        });
        softAssert.assertAll();
    }

    @And("I verify {string} popup is displayed with {string} & {string} buttons")
    public void iVerifyPopupIsDisplayedWithButtons(String popupName, String cancelButton, String OkButton) {
        WebElement popupElement = driver.findElement(By.xpath("//form[@id='delete_contact_alert_form']"));
        if (popupElement.isDisplayed()){
            Assert.assertTrue(popupElement.getText().contains(popupName) && commonFunctions.commonButton(cancelButton).isDisplayed() &&  commonFunctions.commonButton(OkButton).isDisplayed(),popupName+" popup is not displayed with "+ cancelButton +" & "+ OkButton +" buttons");
            System.out.println(popupName+" popup is not displayed with "+ cancelButton +" & "+ OkButton +" buttons");
        }else {
            Assert.fail(popupName+" popup is not displayed");
        }
    }

    @And("I verify popup of {string} {string} displayed with {string} & {string} buttons")
    public void iVerifyPopupOfIsDisplayedWithButtons(String popupName, String condition, String cancelButton, String okButton) {
        try {
            WebElement policyDeletePopup = driver.findElement(By.xpath("//h2[normalize-space()='" + popupName + "']"));
            switch (condition.toLowerCase()) {
                case "is":
                    if (policyDeletePopup.isDisplayed() && policyDeletePopup.getText().contains(popupName)) {
                        Assert.assertTrue(commonFunctions.commonButton(cancelButton).isDisplayed() && commonFunctions.commonButton(okButton).isDisplayed(), cancelButton + " & " + okButton + " buttons are displayed on delete policy popup");
                        System.out.println(cancelButton + " & " + okButton + " buttons are displayed on "+ popupName +" popup");
                        break;
                    }
                case "is not":
                    if (!policyDeletePopup.isDisplayed()) {
                        System.out.println(popupName + " popup is not displayed");
                        break;
                    }
            }
        }catch (NoSuchElementException ignored){

        }
    }

    @And("I store selected {string} of {string} column")
    public void iStoreSelectedOfColumn(String selectedElements, String colName) {
        List<String> list = new ArrayList<>();
        int gridRow = commonFunctions.gridRowCount();
        for (int i=1; i<=gridRow; i++) {
            String s = String.valueOf(i);
            if (commonFunctions.commonCheckBoxSelected(i)){
                list.add(commonFunctions.gridEntry("row"+s,colName).getText());
            }
        }
        System.out.println(list+" before delete");
        valueListStringStore.put(colName,list);
    }

    @Then("I verify selected groups are deleted from {string} column and from grid")
    public void iVerifySelectedGroupsAreDeletedFromColumnAndFromGrid(String colName) {
        List<String> list = new ArrayList<>();
        int gridRow = commonFunctions.gridRowCount();
        for (int i = 1; i <= gridRow; i++) {
            String s = String.valueOf(i);
            list.add(commonFunctions.gridEntry("row" + s, colName).getText());
        }
        System.out.println(list+" After delete");
        List<String> deletedGroups = valueListStringStore.get(colName);
        Assert.assertFalse(commonFunctions.CheckIfDeletedGroupIdNotExists(list, deletedGroups),"Deleted groups exists in the grid");
        System.out.println("Selected groups are get deleted");

    }

    @Then("I verify there is {string} deletion request in row {int} for {string}")
    public void iVerifyThereIsDeletionRequestInRowFor(String status, int rowNumber, String policyDeletion) {
        String s = String.valueOf(rowNumber);
        WebElement element = commonFunctions.gridEntry("row"+s, "Status");
        String initiatedBy = commonFunctions.gridEntry("row"+s, "Initiated By").getText();
        String type = commonFunctions.gridEntry("row"+s, "Type").getText();
        if (element != null){
            Assert.assertTrue(element.getText().contains(status) && type.contains(policyDeletion));
            System.out.println("There is new request for deletion in row "+s+" Initiated By: "+initiatedBy+" for "+policyDeletion);
        }else {
            Assert.fail("There is no new request for deletion in row "+s);
        }
    }

    @Then("I verify Approve Deletion Request Popup {string} displayed with {string} input field,{string} button,{string} and {string} button")
    public void iVerifyApproveDeletionRequestPopupDisplayedWithIAGREEInputFieldButtonAndButton(String condition, String inputField, String button1, String button2, String button3) {
        WebElement deletionRequestPopup = driver.findElement(By.xpath("//form[@id='approve_deletion_request_form']"));
        switch (condition.toLowerCase()) {
            case "is":
                if (deletionRequestPopup.isDisplayed()) {
                    if (commonFunctions.commonField(inputField).isDisplayed() && commonFunctions.commonButton(button1).isDisplayed() && commonFunctions.commonButton(button2).isDisplayed() && commonFunctions.commonButton(button3).isDisplayed()) {
                        System.out.println("Approve Deletion Request Popup "+ condition +" displayed with " + (inputField) + " field, " + (button1) + " button, " + (button2) + " button and " + (button3) + " button");
                        break;
                    }
                }
            case "is not":
                if (!deletionRequestPopup.isDisplayed()){
                    System.out.println("Approve Deletion Request Popup "+ condition +" displayed with " + (inputField) + " field, " + (button1) + " button, " + (button2) + " button and " + (button3) + " button");
                    break;
                }
            default:
                System.out.println("Enter valid condition");

        }
    }

    @And("I verify I am on the {string} page")
    public void iVerifyIAmOnThePage(String pageTitle) {
        String currentPageTitle = driver.getTitle();
        Assert.assertEquals(currentPageTitle,pageTitle, "Page titles are different");
        System.out.println("On the "+ currentPageTitle +" page");
    }

    @Then("I verify there is no record selected")
    public void iVerifyThereIsNoRecordSelected() {
        int rows = commonFunctions.gridRowCount();
        for (int i=1; i<=rows; i++){
            Assert.assertTrue(commonFunctions.commonCheckBoxSelected(i),"Checkbox for row "+ i +" selected");
        }
        System.out.println("There is no record selected");
    }

    @And("I verify that document is stored on local drive")
    public void iVerifyThatDocumentIsStoredOnLocalDrive() {
        File downloaded_File = BeSpin.getLastModified();
        System.out.println(downloaded_File + " has been downloaded successfully");
        Assert.assertTrue(downloaded_File.exists(), downloaded_File + " file is not stored in local drive");
        System.out.println(downloaded_File + " document is stored in local drive.");
        downloaded_File.delete();
    }

    @And("I store selected contacts")
    public void iStoreSelectedContacts() {
        List<String> list = new ArrayList<>();
        int gridRow = commonFunctions.gridRowCount();
        for (int i=1; i<=gridRow; i++) {
            if (commonFunctions.commonCheckBoxSelected(i)){
                list.add(commonFunctions.commonCheckBox(i).getAttribute("id"));
            }
        }
        System.out.println(list+" Item selected for deletion");
        valueListStringStore.put("Contacts",list);
    }

    @Then("I verify selected contacts get deleted from grid")
    public void iVerifySelectedContactsGetDeletedFromGrid() {
        List<String> list = new ArrayList<>();
        int gridRow = commonFunctions.gridRowCount();
        for (int i = 1; i <= gridRow; i++) {
            list.add(commonFunctions.commonCheckBox(i).getAttribute("id"));
        }
        System.out.println(list+" After delete");
        List<String> deletedGroups = valueListStringStore.get("Contacts");
        Assert.assertFalse(commonFunctions.CheckIfDeletedGroupIdNotExists(list, deletedGroups),"Deleted groups exists in the grid");
        System.out.println("Selected groups are get deleted");

    }

    @Then("I verify drive type filter popup is displayed")
    public void iVerifyDriveTypeFilterPopupIsDisplayed() {
        Assert.assertTrue(commonFunctions.driveTypeFilterPopUp.isDisplayed(), "Drive Type filter popup is not displayed");
        System.out.println("Drive Type filter popup is displayed");
    }

    @And("I click the drive type dropdown icon")
    public void iClickTheDriveTypeDropdownIcon() {
        System.out.println("Clicking on drive type dropdown icon");
        Actions actions = new Actions(driver);
        actions.moveToElement(commonFunctions.driveTypeDropDownIcon).click().build().perform();
//        js.executeScript("arguments[0].click();",commonFunctions.driveTypeDropDownIcon);
    }

    @Then("I verify drive type filter popup is closed")
    public void iVerifyDriveTypeFilterPopupIsClosed() {
        Assert.assertFalse(commonFunctions.driveTypeFilterPopUp.isDisplayed(), "Drive Type filter popup is opened");
        System.out.println("Drive Type filter popup is closed");
    }

    @And("I click on the {string}")
    public void iClickOnThe(String drive) {
        System.out.println("Clicking on "+ drive +" drive");
        WebElement driveElement = driver.findElement(By.xpath("//li[contains(@class,'mdc-list-item shared-drive')]/..//span[text()[normalize-space()='"+ drive +"']]"));
        js.executeScript("arguments[0].click();",driveElement);
        commonFunctions.sleepTime(2);
    }


    @When("I right click on the drive file")
    public void iRightClickOnTheDriveFile() {
        System.out.println("Right clicking on Arrow right button");
        try {
            WebElement arrowRightButtonForRowOne = driver.findElement(By.xpath("//table[1]/tbody[1]/tr[3]/td[2]"));
            commonFunctions.rightClick(arrowRightButtonForRowOne);
        }catch (NoSuchElementException e){
            e.printStackTrace();
        }
    }

    @Then("The following list elements exist")
    public void theFollowingListElementsExist(List<String> items) {
       List<WebElement> list = driver.findElements(By.xpath("//ul[@id='contextMenu']//li"));
       items.forEach(item -> {
           for (WebElement ele : list){
               if (ele.getText().equals("")){
                   break;
               }else if (ele.getText().contains(item)){
                   System.out.println(item+": option is present in the list");
               }else {
                   Assert.fail(item+": option is not present in the list");
               }
           }
       });
    }

    @Then("I verify options are present in list")
    public void iVerifyOptionsArePresentInList() {
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue (commonFunctions.shareListOption.isDisplayed(),commonFunctions.shareListOption.getText()+": option is not present");
        System.out.println(commonFunctions.shareListOption.getText()+": option is present");
        softAssert.assertTrue (commonFunctions.removeListOption.isDisplayed(),commonFunctions.renameListOption.getText()+": option is not present");
        System.out.println(commonFunctions.removeListOption.getText()+": option is present");
        softAssert.assertTrue (commonFunctions.addShortcutListOption.isDisplayed(),commonFunctions.addShortcutListOption.getText()+": option is not present");
        System.out.println(commonFunctions.addShortcutListOption.getText()+": option is present");
        softAssert.assertTrue (commonFunctions.addListOption.isDisplayed(),commonFunctions.addListOption.getText()+": option is not present");
        System.out.println(commonFunctions.addListOption.getText()+": option is present");
        softAssert.assertTrue (commonFunctions.copyFoldersListOption.isDisplayed(),commonFunctions.copyFoldersListOption.getText()+": option is not present");
        System.out.println(commonFunctions.copyFoldersListOption.getText()+": option is present");
        softAssert.assertTrue (commonFunctions.makeACopyListOption.isDisplayed(),commonFunctions.makeACopyListOption.getText()+": option is not present");
        System.out.println(commonFunctions.makeACopyListOption.getText()+": option is present");
        softAssert.assertTrue (commonFunctions.createNewFolderListOption.isDisplayed(),commonFunctions.createNewFolderListOption.getText()+": option is not present");
        System.out.println(commonFunctions.createNewFolderListOption.getText()+": option is present");
        softAssert.assertTrue (commonFunctions.removeListOption.isDisplayed(),commonFunctions.removeListOption.getText()+": option is not present");
        System.out.println(commonFunctions.removeListOption.getText()+": option is present");
        softAssert.assertAll();
    }

    @And("I click on the share option")
    public void iClickOnTheShareOption() {
        js.executeScript("arguments[0].click();",commonFunctions.shareListOption);
        commonFunctions.sleepTime(5);
    }

    @And("I verify {string} alert displayed with {string} & {string} buttons")
    public void iVerifyAlertDisplayedWithButtons(String alert, String cancelButton, String okButton) {
        try {
            WebElement alertPopUp = driver.findElement(By.xpath(
                    "//h2[@id='my-dialog-title-manageAccess_SharedDriveFiles_popup']"));
            wait.until(ExpectedConditions.visibilityOf(alertPopUp));
            Assert.assertTrue(alertPopUp.isDisplayed() && alertPopUp.getText().equals(alert),alert+": Alert is not displayed");
            System.out.println(alert+": Alert is displayed");
            Assert.assertTrue(commonFunctions.commonButton(cancelButton).isDisplayed()
                    && commonFunctions.commonButton(cancelButton).isEnabled()
                    && commonFunctions.commonButton(okButton).isDisplayed()
                    && commonFunctions.commonButton(okButton).isEnabled(), "Buttons are not displayed");
            System.out.println(cancelButton+" & "+ okButton +" buttons are displayed");
        }catch (NoSuchElementException | TimeoutException e){
            e.printStackTrace();
        }
    }

    @And("I move to {string} file and perform right click operation")
    public void iMoveToFile(String file) {
        valueStore.put("File",file);
        WebElement fileName = driver.findElement(By.xpath("//span[contains(text(),'"+ valueStore.get("File") +"')]"));
        Actions actions = new Actions(driver);
        actions.moveToElement(fileName).contextClick(fileName).build().perform();
    }

    @Then("I verify {string} is Added for file access")
    public void iVerifyIsAddedForFileAccess(String member) {
        WebElement memberAddElement = driver.findElement(By.xpath(
                "//p[@class='media-heading item-avatar_name']//span[@data-toggle='tooltip'][normalize-space()='"+ member +"']"));
        Assert.assertTrue(memberAddElement.isDisplayed()
                && memberAddElement.getText().equalsIgnoreCase(member),member+" member is not added for file Access");
        System.out.println(member+" member is added for file Access");
    }

    @And("I click on the first option")
    public void iClickOnTheFirstOption() {
        try {
            WebElement memberAddElement = driver.findElement(By.xpath("//li[@class='select2-results__option select2-results__option--highlighted']//p[@class='media-heading']"));
            new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.elementToBeClickable(memberAddElement)).click();
            valueStore.put("MemberData",memberAddElement.getText());
        }catch (StaleElementReferenceException | NoSuchElementException  e){
//            WebElement memberAddElement = driver.findElement(By.xpath("//li[@class='select2-results__option select2-results__option--highlighted']//p[@class='media-heading']"));
//            new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.elementToBeClickable(memberAddElement)).click();
//            valueStore.put("MemberData",memberAddElement.getText());
        }

    }

    @Then("I verify duplicate member is not get added")
    public void iVerifyDuplicateMemberIsNotGetAdded() {
        List<WebElement> data = driver.findElements(By.xpath("//tbody[@class='mdc-data-table__content']//tr//p"));
        int count =0;
        for (WebElement ele : data){
            if (ele.getText().equalsIgnoreCase(valueStore.get("MemberData"))){
                count++;
            }
        }
        if (count == 2){
            Assert.fail("Duplicate Member found");
        }
        if (count <= 1){
            System.out.println("No duplicate member found");
        }
    }

    @Then("I verify {string} alert is closed")
    public void iVerifyAlertIsClosed(String alert) {
        try {
            WebElement alertPopUp = driver.findElement(By.xpath(
                    "//h2[@id='my-dialog-title-manageAccess_SharedDriveFiles_popup']"));
            Assert.assertFalse(alertPopUp.isDisplayed() && alertPopUp.getText().equals(alert),alert+": Alert is not closed");
            System.out.println(alert+": Alert is closed");
        }catch (NoSuchElementException e){
            e.printStackTrace();
        }
    }

    @And("I move to first list option")
    public void iMoveToFirstListOption() {
        WebElement element = driver.findElement(By.xpath("//table[@id='sharedDrive_members_container']/tbody/tr[1]/td[1]"));
        Actions actions = new Actions(driver);
        actions.moveToElement(element).build().perform();
    }

    @And("I click the row {int} permission button")
    public void iClickTheRow(int row) {{
        WebElement button = driver.findElement(By.xpath(
                "//tbody//tr["+ row +"]//button[@class=' mdl-button mdl-js-button af_dropdown_button mdl-button-shadow-none resulting-permission main-icon-btn mdc-btn-dropdown-absolute permission-selectable m-t-15']"));
        button.click();
    }
    }

    @Then("I verify following list is present")
    public void iVerifyFollowingListIsPresent(List<String> table) {
        SoftAssert softAssert = new SoftAssert();
        table.forEach(item -> {
            softAssert.assertTrue(commonFunctions.commonLink(item).isDisplayed(),item+" : link is not present");
           System.out.println(item+" : link is present");
       });
        softAssert.assertAll();
    }

    @And("I select {string} from permission list option")
    public void iSelectFromPermissionListOption(String linkText) {
        System.out.println("Selecting "+ linkText +" option from permission list");
        commonFunctions.commonLink(linkText).click();
    }

    @When("I move to row {int}")
    public void iMoveToRow(int row) {
        WebElement tableRow = driver.findElement(By.xpath(
                "//tbody/tr[@class='mdc-data-table__row hoverable-custom child-border-1']["+ row +"]"));
        Actions actions = new Actions(driver);
        actions.moveToElement(tableRow).build().perform();
        System.out.println("Move to row : "+ row +"");
    }

    @And("I click on the Rename option")
    public void iClickOnTheRenameOption() {
        js.executeScript("arguments[0].click();",commonFunctions.renameListOption);
        commonFunctions.sleepTime(2);
    }

    @And("I enter {string} in the Rename field")
    public void iEnterInTheRenameField(String text) {
        if (!commonFunctions.commonFieldRead(commonFunctions.renameInputField).isEmpty()){
            commonFunctions.renameInputField.clear();
        }
        String random = new SimpleDateFormat("HH:mm:ss").format(new Date());
        if (text.contains("<random>")){
            text = text.replaceAll("<random>","Renamed "+random);
        }
        System.out.println("Entering \"" + text + "\" into Rename input field");
        commonFunctions.renameInputField.sendKeys(text);
        valueStore.put("Rename",text);
    }

    @Then("I verify Rename alert displayed")
    public void iVerifyRenameAlertDisplayed() {
        Assert.assertTrue(commonFunctions.renameAlertPopUp.isDisplayed()
                && commonFunctions.commonButton("Cancel").isDisplayed()
                && commonFunctions.commonButton("OK").isDisplayed(),"Rename Alert is not displayed");
        System.out.println("Rename Alert is displayed");
    }

    @Then("I verify Rename alert is closed")
    public void iVerifyRenameAlertIsClosed() {
        Assert.assertFalse(commonFunctions.renameAlertPopUp.isDisplayed(),"Rename Alert is still opened");
        System.out.println("Rename Alert is closed");
    }

    @Then("I verify {string} filter is displayed")
    public void iVerifyFilterIsDisplayed(String filter) {
        try {
            WebElement filterEle = driver.findElement(By.xpath(
                    "//div[@class='mdc-card filter-main gc-card-filter']//span[normalize-space()='"+ filter +"']"));

            Assert.assertTrue(filterEle.isDisplayed() && filterEle.getText().equalsIgnoreCase(filter),filter+" filter is not displayed");
            System.out.println(filter+" filter is displayed");
        }catch (NoSuchElementException e){
            e.printStackTrace();
        }
    }

    @Then("I verify result that contains {string} should displayed")
    public void iVerifyResultThatContainsShouldDisplayed(String userEmail) {
        pageLoaded();
        if (userEmail.contains("<gmail>")){
            userEmail = login.getLogins().getProperty("gmail.email");
        }
        int count = commonFunctions.gridRowCount();
        for (int i=1; i<=count; i++){
            String s = String.valueOf(i);
            String resultText = commonFunctions.gridEntryText("row"+s,"Shared With");
            Assert.assertTrue(resultText.contains(userEmail),"displayed result not contains : "+userEmail);
        }
        System.out.println("Displayed only "+userEmail+" results");
    }

    @Then("I verify result that contains {string} should not displayed")
    public void iVerifyResultThatContainsShouldNotDisplayed(String input) {
        pageLoaded();
        String resultText = null;
        int count = commonFunctions.gridRowCount();
        for (int i=1; i<=count; i++){
            String s = String.valueOf(i);
            resultText = commonFunctions.gridEntryText("row"+s,"Shared With");
            String[] arr = resultText.split("\n");
            Assert.assertFalse(resultText.contains(input),"displayed result contains : "+input);
            System.out.println("Displayed results : "+arr[1]);
        }
    }

    @And("I click the {string} button of row {int}")
    public void iClickTheButtonOfRow(String button, int row) {
        WebElement buttonEle = driver.findElement(By.xpath("//table[@id='fileSharingTable']//tbody//tr["+row+"]//button"));
        if (buttonEle.getText().equalsIgnoreCase(button)){
            System.out.println("Clicking on "+ button +" button");
            js.executeScript("arguments[0].click();",buttonEle);
        }
        valueStore.put("Row", String.valueOf(row));
    }

    @Then("I verify An approval request popup is displayed")
    public void iVerifyAnApprovalRequestPopupIsDisplayed() {
        //creating this separate method because approval request is not being sent to admin.This is a bug
        String expectation = "An approval request has been sent to the admin.";
        WebElement snackBarPopUp = driver.findElement(By.xpath("//div[@id='snackbar-mdc-main']//div[@class='mdc-snackbar__label msm-text']"));
        if (snackBarPopUp.isDisplayed()) {
            Assert.assertTrue(snackBarPopUp.getText().contains(expectation) , "Popup message is not correct. Displayed popup text : "+snackBarPopUp.getText());
            System.out.println(expectation + " Popup is displayed");
        } else {
            Assert.fail(expectation + " Popup is not displayed");
        }
    }

    @When("I uncheck {string} checkbox in the header")
    public void iUncheckCheckboxInTheHeader(String checkbox) {
        WebElement selectAllGroupsCheckbox = driver.findElement(By.xpath("//input[@id='"+ checkbox +"']"));
        if (selectAllGroupsCheckbox.isSelected()){
            selectAllGroupsCheckbox.click();
            System.out.println("Unchecked "+ checkbox +" checkbox");
        }
    }

    @And("I click on the row {string} user")
    public void iClickOnTheRowUser(String row) {
        WebElement userIcon = driver.findElement(By.xpath("//*[@id='fileSharingAppend']/tr["+ row +"]/td[2]/div/div[1]/div/a"));
        userIcon.click();
    }

    @When("I click on the select all checkbox of user files popup")
    public void iClickOnTheSelectAllCheckboxOfUserFilesPopup() {
        WebElement selectAll = driver.findElement(By.xpath("//*[@id='sharingUserFileContainer']/table/thead/tr/th[1]/div/input"));
        selectAll.click();
    }

    @And("I click on the Add a shortcut option")
    public void iClickOnTheAddAShortcutOption() {
        if (commonFunctions.addShortcutListOption.isDisplayed()){
            commonFunctions.addShortcutListOption.click();
            System.out.println("Clicks Add a shortcut option");
        }
    }

    @And("I enter {string} into Enter names or Emails field")
    public void iEnterIntoEnterNamesOrEmailsField(String text) {
        WebElement namesOrEmails = driver.findElement(By.id("select2-add_file-container"));
        if (namesOrEmails.isDisplayed()) {
            namesOrEmails.click();
        }
        WebElement ipnutField = driver.findElement(By.xpath(
                "//span[@class='select2-search select2-search--dropdown']//input[@role='textbox']"));
        ipnutField.sendKeys(text);
        commonFunctions.sleepTime(5);

    }

    @Then("I verify {string} popup {string} displayed")
    public void iVerifyPopupDisplayed(String popupText, String condition) {
        WebElement element = driver.findElement(By.xpath("//h2[text()='"+ popupText +"']"));
        switch (condition.toLowerCase()){
            case "is" -> {
                Assert.assertTrue(element.isDisplayed());
                Assert.assertTrue(element.getText().equalsIgnoreCase(popupText),"Expected popup text is not found\n"+
                        "Actual popup text : \""+ element.getText() + "\" ");
                System.out.println(popupText+" popup is displayed");
                break;
                }
            case "is not" -> {Assert.assertFalse(element.isDisplayed(),popupText+" popup is not closed");
                System.out.println(popupText+" popup is closed");
                break;
            }
            default -> System.out.println("Select valid condition");
        }
    }

    @And("I click on the Create New Folder option")
    public void iClickOnTheCreateNewFolderOption() {
        js.executeScript("arguments[0].click();",commonFunctions.createNewFolderListOption);
        commonFunctions.pageLoaded();
        System.out.println("Clicks Create new Folder option");

    }

    @And("I click on the Remove option")
    public void iClickOnTheRemoveOption() {
        js.executeScript("arguments[0].click();",commonFunctions.removeListOption);
        commonFunctions.pageLoaded();
        System.out.println("Clicks Remove option");
    }

    @When("I click the header checkbox")
    public void iClickTheHeaderCheckbox() {
        WebElement headerCheckbox = driver.findElement(By.xpath(
                "//*[@id=\"context-dropdown-menu-people-file\"]/thead/tr/th[1]/div/input"));
        if (headerCheckbox != null){
            js.executeScript("arguments[0].click();",headerCheckbox);
            System.out.println("Clicks header checbox");
        }

    }

    @And("I click the {string} dropdown and select {string} option")
    public void iClickTheDropdownAndSelectOption(String dropdown, String selection) {
        try {
            WebElement typedrpdwn = driver.findElement(By.xpath(
                    "//*[@id=\"fs-filter-list\"]/div[5]/div[2]/div/div[1]"));

            js.executeScript("arguments[0].click();",typedrpdwn);
            System.out.println("Clicks \""+ dropdown +"\" Dropdown");
            commonFunctions.sleepTime(2);

            WebElement select = driver.findElement(By.xpath("//ul[@class='mdc-list']//li//span[text()='"+ selection +"']"));
            if (select.isDisplayed() && select.getText().equalsIgnoreCase(selection)){
                js.executeScript("arguments[0].click();",select);
                System.out.println("Select \" "+ select.getText() +" \" option");
            }else {
                Assert.fail(selection+" option is not found");
            }
        }catch (ElementNotInteractableException e){
            e.printStackTrace();
        }

    }

    @Then("I verify approval request popup is displayed")
    public void iVerifyApprovalRequestPopupIsDisplayed() {
        String expectedValidation = "An approval request has been sent to the admin.";
        WebElement snackbar = driver.findElement(By.xpath("//*[@id=\"snackbar-mdc-main\"]//div[@class='mdc-snackbar__label msm-text']"));
        wait.until(ExpectedConditions.visibilityOf(snackbar));
        Assert.assertTrue(snackbar.isDisplayed() && snackbar.getText().equalsIgnoreCase(expectedValidation),"Expected snackbar popup is not displayed\n"+
                "Actual snackbar popup is : \" "+ snackbar.getText() +" \"");
        System.out.println("\" "+ expectedValidation + "\" snackbar popup is displayed");
    }

    @And("I move to row {string}")
    public void iMoveToRow(String row) {
        commonFunctions.moveToThatElement(driver.findElement(By.xpath("//*[@id='searchFileAppend']/tr["+ row +"]/td[2]")));
    }

    @Then("I verify selected files unsharing is done")
    public void iVerifySelectedFilesUnsharingIsDone() {
        WebElement table = driver.findElement(By.id("LiveTask-table-for-dashboard"));
        js.executeScript("arguments[0].scrollIntoView();",table);
        commonFunctions.sleepTime(2);
        int actualPercentage = commonFunctions.taskPercentage();
        String progressCount = commonFunctions.taskProgressCount();
        String[] arr = progressCount.split("/");
        int selected = Integer.parseInt(valueStore.get("Total Selected Groups"));
        Assert.assertTrue(Integer.parseInt(arr[0]) == selected
                && Integer.parseInt(arr[1]) == selected
                && actualPercentage == 100,"Actual Files unshared: " + Integer.parseInt(arr[0]) + "\n" +
                "Expected files to be unshared : " + Integer.parseInt(arr[1]) + "");
        System.out.println("Files unshared successfully\n" +
                "Progress Count : "+ progressCount +"\n" +
                "Task Percentage : "+ actualPercentage);
    }

    @And("I check row {int} popup checkbox of {string} grid")
    public void iCheckRowPopupCheckboxOfGrid(int row, String tableID) {
        if (!commonFunctions.popUpCheckBoxClick(tableID,row)) {
            Assert.assertTrue(commonFunctions.popUpCheckBoxClick(tableID,row), "Checkbox for Row " + row + " is not selected");
            System.out.println("Checkbox for Row " + row + " is selected");
        }
    }

    @Then("I verify the count of files selected for unsharing,that exact count should be displayed in {string} column in dashboard tab")
    public void iVerifyTheCountOfFilesSelectedForUnsharingThatExactCountShouldBeDisplayedInColumnInDashboardTab(String column) {
        int gridCount = commonFunctions.actionCountInDashboardTab();
        Assert.assertEquals(gridCount,Integer.parseInt(valueStore.get("FileCount")),"Total File Count : "+ valueStore.get("FileCount") +"\n" +
                "And count in "+ column +" column is "+ gridCount +" not equal, Test case fail");
        System.out.println("Total File Count : "+ valueStore.get("FileCount") +"\n" +
                "And count in "+ column +" column is: "+ gridCount +" equal, Test case pass");
    }

    @And("I get the total file count for row {int}")
    public void iGetTheTotalFileCountForRow(int row) {
        int fileCount = commonFunctions.getTotalFilesCount(row);
        valueStore.put("FileCount", String.valueOf(fileCount));
    }

    @And("I enter that information into {string} field")
    public void iEnterThatInformationIntoField(String field) {
        Assert.assertTrue(commonFunctions.commonFieldEnter(field,valueStore.get("Grid Header Input")));
    }

    @Then("I verify display data as per search criteria")
    public void iVerifyDisplayDataAsPerSearchCriteria() {
        int gridRow = commonFunctions.gridRowCount();
        for (int i = 1; i <= gridRow; i++) {
            String s = String.valueOf(i);
            WebElement gridElement = commonFunctions.gridEntry("row" + s, valueStore.get("Grid Header Name"));
            Assert.assertTrue(gridElement.getText().contains(valueStore.get("Grid Header Input")), "Displayed data not as per search criteria");
        }
        out.println("Displayed data as per search criteria");
    }

    @Then("I verify all visible records of that user get selected")
    public void iVerifyAllVisibleRecordsOfThatUserGetSelected() {
        SoftAssert softAssert = new SoftAssert();
        int rows = commonFunctions.gridRowCount("userFilesMainTBody");
        for (int i=1; i<=rows; i++) {
            softAssert.assertTrue(commonFunctions.popUpCheckBoxSelected("userFilesMainTBody",i),"checkbox for row number "+ i +" not selected");
            System.out.println("Checkbox for row "+ i +" is selected");
        }
        softAssert.assertAll();
    }

    @Then("I verify the successful validation message")
    public void iVerifyTheSuccessfulValidationMessage() {
        //This method is for verifying the functionality of Rename option of shared drives
        String validation = valueStore.get("File")+" renamed to " + valueStore.get("Rename") + " successfully !";
        WebElement snackBar = driver.findElement(By.xpath("//*[@id=\"snackbar-mdc-main\"]/descendant::div[@class='mdc-snackbar__label msm-text']"));
        wait.until(ExpectedConditions.visibilityOf(snackBar));
        Assert.assertTrue(snackBar.isDisplayed() && snackBar.getText().equalsIgnoreCase(validation));
        out.println(validation+" : validation displayed");
    }

    @And("I click on the {string} for the {string}")
    public void iClickOnTheForThe(String arrow, String fileName) {
        WebElement file = driver.findElement(By.xpath("//li[contains(@class,'mdc-list-item shared-drive')]/..//span[text()[normalize-space()='"+ fileName +"']]"));
        WebElement arrowRight = driver.findElement(with(By.xpath("//i[normalize-space()='"+ arrow +"']")).toLeftOf(file));
        arrowRight.click();
    }

    @And("I restore the changes")
    public void iRestoreTheChanges() throws InterruptedException {
        iMoveToFile(valueStore.get("Rename"));
        iClickOnTheRenameOption();
        iVerifyRenameAlertDisplayed();
        iEnterInTheRenameField("Commercials");
        commonStepFunctions.iClickTheButton("OK");
        iWaitForSeconds(1);
        iVerifyTheSuccessfulValidationMessage();

    }

    @And("I perform right click operation to create new folder")
    public void iPerformRightClickOperationToCreateNewFolder() {
        WebElement element = driver.findElement(By.xpath("//*[@id=\"context-dropdown-menu-people-file-shared-drive\"]/thead/descendant::th[normalize-space()='Name']"));
        commonFunctions.rightClick(element);
    }

    @And("I right click on the {string} for the {string}")
    public void iRightClickOnTheForThe(String arrow, String folder) {
        WebElement file = driver.findElement(By.xpath("//li[contains(@class,'mdc-list-item shared-drive')]/..//span[text()[normalize-space()='"+ folder +"']]"));
        WebElement arrowRight = driver.findElement(with(By.xpath("//i[normalize-space()='"+ arrow +"']")).toLeftOf(file));
        Actions actions = new Actions(driver);
        actions.contextClick(arrowRight).build().perform();
        valueStore.put("FolderName",folder);
    }

    @And("I move row {int} folder and right click")
    public void iMoveRowFolderAndRightClick(int row) {
        WebElement moveTo = driver.findElement(By.xpath("//*[@id=\"level1Container1A3k3iVIuo75pzQJ42ank7bfTY93_34ol\"]/td/div/table/tbody/tr["+row+"]/td[2]/span[2]/span"));
        out.println("folder Name : " + moveTo.getText());
        commonFunctions.moveToThatElement(moveTo);
        commonFunctions.rightClick(moveTo);
        valueStore.put("FolderToBeDeleted",moveTo.getText());
    }

    @And("I verify new folder has created")
    public void iVerifyNewFolderHasCreated() {
        pageLoaded();
        WebElement folderElement = driver.findElement(By.xpath(
                "//span[normalize-space(text())='"+ valueStore.get("Folder Name") +"']"));

        Assert.assertEquals(folderElement.getText(), valueStore.get("Folder Name"),valueStore.get("Folder Name")+" Folder isn't created");
        out.println(valueStore.get("Folder Name")+" Folder has created");
    }

    @And("I verify folder is got deleted")
    public void iVerifyFolderIsGotDeleted() {
        pageLoaded();
        WebElement folder = commonFunctions.isFolderDeleted(valueStore.get("FolderToBeDeleted"));
        Assert.assertNull(folder,valueStore.get("FolderToBeDeleted")+" folder is not get deleted");
        out.println(valueStore.get("FolderToBeDeleted")+" folder is get deleted");
    }

    @When("I right click on the {string} drive")
    public void iRightClickOnTheDrive(String drive) {
        System.out.println("Right Clicking on "+ drive +" drive");
        WebElement driveElement = driver.findElement(By.xpath("//li[contains(@class,'mdc-list-item shared-drive')]/..//span[text()[normalize-space()='"+ drive +"']]"));
        new Actions(driver).contextClick(driveElement).build().perform();
        commonFunctions.sleepTime(1);
    }

    @And("I click on the Add Members And Manage Access")
    public void iClickOnTheAddMembersAndManageAccess() {
        WebElement targetElement = null;
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"contextMenu_sharedDrive\"]/li[normalize-space()='Add Members And Manage Access.']")));
            targetElement = driver.findElement(By.xpath("//*[@id=\"contextMenu_sharedDrive\"]/li[normalize-space()='Add Members And Manage Access.']"));
            targetElement.click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//footer[@id='SharedDrive_popup_button']//button[normalize-space()='Save']")));
        }catch (NoSuchElementException | TimeoutException e){
            e.printStackTrace();
        }catch (ElementClickInterceptedException e){
            commonFunctions.clickErrorHandle(e.toString(),targetElement);
        }
    }

    @And("I remove added member from drive")
    public void iRemoveAddedMemberFromDrive() {
        commonFunctions.sleepTime(2);
        List<WebElement> rows = driver.findElements(By.xpath("//*[@id=\"sharedDrive_members_container\"]/tbody/tr"));
        int rowpath = 0;
        int count = 1;
        for (WebElement row : rows){
            String text = row.getText().split("\\n")[0];
            String member = valueStore.get("AddedMember").split("\\n")[0];
            if (text.contains(member)){
                rowpath = count;
                break;
            }else {
                count++;
            }
        }
        out.println("Removing "+ valueStore.get("AddedMember").split("\\n")[0] +" member from drive");
        WebElement closeBtn = driver.findElement(By.xpath("//*[@id=\"sharedDrive_members_container\"]/tbody/tr["+ rowpath +"]/td[2]/div/button"));
        closeBtn.click();
    }

    @And("I click on the Make a Copy option")
    public void iClickOnTheMakeACopyOption() {
        Assert.assertTrue(commonFunctions.clickMakeACopy());
        commonFunctions.pageLoaded();
    }

    @And("I verify copy file should display in respective peoples drive")
    public void iVerifyCopyFileShouldDisplayInRespectivePeoplesDrive() {
        String input = valueStore.get("AddedMember").split("\\n")[0];
        //Searching the User in Search bar
        Assert.assertTrue(commonFunctions.commonFieldEnter("Search for Users",input));
        commonFunctions.commonField("Search for Users").sendKeys(Keys.ENTER);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"view-userList\"]/descendant::div[contains(@class,'demo-card__primary')and h2[normalize-space()='"+ input +"']]")));
        //Clicking the Search User
        commonFunctions.commonElement("//*[@id=\"view-userList\"]/descendant::h2[normalize-space()='"+ input +"']").click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button/span[normalize-space()='files']")));
        //Clicking the files option in User's profile
        Assert.assertTrue(commonFunctions.commonButtonClick("files"));
        commonFunctions.sleepTime(3);
        //Expanding the My Drive of User
        iClickTheArrowDownForThe("My Drive");
        commonFunctions.sleepTime(1);
        //Verifying the presence of file/folder in users Drive
        Assert.assertTrue(driver.findElements(By.xpath("//*[@id=\"context-dropdown-menu-people-file\"]/tbody/descendant::span/span[normalize-space()='"+ valueStore.get("FolderName") +"']")).size() > 0,valueStore.get("FolderName")+" Folder is not displayed");
        out.println(valueStore.get("FolderName")+" Folder is displayed");
    }

    @And("I click the arrow down for the {string}")
    public void iClickTheArrowDownForThe(String drive) {
        WebElement arrowDownBtn = null;
        try {
            arrowDownBtn = driver.findElement(By.xpath("//span[normalize-space()='" + drive + "']/preceding-sibling::i[contains(@class,'more-less fa fa-angle-down')]"));
            arrowDownBtn.click();
        } catch (StaleElementReferenceException e) {
            e.printStackTrace();
        } catch (ElementClickInterceptedException ele) {
            commonFunctions.clickErrorHandle(ele.toString(), arrowDownBtn);
        }

    }

    @And("I click the Add Policy button")
    public void iClickTheAddPolicyButton() {
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@data-original-title='Add Policy']"))).click();
    }

    @And("I open the created policy")
    public void iOpenTheCreatedPolicy() {
        String linkText = valueStore.get("Policy Name");
        if (linkText != null){
            commonFunctions.commonLinkClick(linkText);
            out.println("Details are opened for Policy : " + linkText);

        }

    }

    @And("I click the provision button")
    public void iClickTheProvisionButton() {
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"executorIcon\"]/button[1]"))).click();
        out.println("Click the Provision button");
    }

    @Then("I verify contacts for selected member is get added")
    public void iVerifyContactsForSelectedMemberIsGetAdded() {
        WebElement chechbox = driver.findElement(By.xpath("//*[@id=\"contactContainer\"]/tbody/tr[1]/td[1]/div/div/input"));
        new Actions(driver).moveToElement(chechbox).click().build().perform();
        commonFunctions.sleepTime(1);
        iClickTheSaveButtonOnPopup();
        commonFunctions.sleepTime(2);
        iVerifyPopupIsDisplayed("Label added successfully");
        String expected = valueStore.get("AddedMember").split("\\n")[0];
        String actual = driver.findElement(By.xpath("//tbody[contains(@class,'table__content policyContacttbodyRows')]/tr[1]/td[3]")).getText();
        Assert.assertEquals(actual,expected,"Contacts for "+ expected +" member is not Added");
        out.println("Contacts for "+ expected +" member is Added");
    }

    @And("I click the save button on popup")
    public void iClickTheSaveButtonOnPopup() {
        WebElement cancelBtn = commonFunctions.commonButton("Cancel");
        WebElement saveBtn = driver.findElement(with(By.xpath("//button[normalize-space()='Save']")).toRightOf(cancelBtn));
        saveBtn.click();
    }

    @And("I create {int} policies")
    public void iCreatePolicies(int number) {
        for (int i=1; i<=number; i++){
            commonFunctions.sleepTime(2);
            iClickTheAddPolicyButton();
            commonFunctions.sleepTime(1);
            commonStepFunctions.iEnterInTheField("Test Policy <random>", "Policy Name");
            commonFunctions.commonButtonClick("Save");
            commonFunctions.sleepTime(3);
            iClickTheLink("Policies");
            commonFunctions.sleepTime(3);
        }
    }

    @And("I click the deprovision button")
    public void iClickTheDeprovisionButton() {
        try {
            WebElement deprovision = driver.findElement(By.xpath("//button[@id='policy_executor_icon']//span[@class='mdl-button__ripple-container']"));
            new Actions(driver).moveToElement(deprovision).click().build().perform();
        }catch (NoSuchElementException e){
            Assert.fail(e.toString());
        }
    }

    @And("wait for file to be download")
    public void waitForFileToBeDownload() {
        File downloads = new File(dLFolder);
        long timer = System.currentTimeMillis();
        while (Arrays.asList(downloads.list()).toString().contains(".crdownload") && (System.currentTimeMillis() - timer) < 120000) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    @And("I click the import button on shared contact page")
    public void iClickTheImportButtonOnSharedContactPage() {
        try {
            WebElement deprovision = driver.findElement(By.xpath("//button[@id='importSharedContact']//span[@class='mdl-button__ripple-container']"));
            new Actions(driver).moveToElement(deprovision).click().build().perform();
            commonFunctions.sleepTime(5);
        }catch (NoSuchElementException e){
            Assert.fail(e.toString());
        }
    }

    @And("I choose {string} account")
    public void iChooseAccount(String account) {
        Set<String> windows = driver.getWindowHandles();
        ArrayList<String> arrayList = new ArrayList<>(windows);
        String parentWindow = arrayList.get(0);
        String childWindow = arrayList.get(1);
        driver.switchTo().window(childWindow);
        try {
            driver.findElement(By.xpath("//div[text()='Email or phone']/preceding::input")).sendKeys(login.getLogins().getProperty("email.validuser"));
            commonFunctions.commonButtonClick("Next");
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[text()='Enter your password']/preceding::input[@type='password']")));
            driver.findElement(By.xpath("//div[text()='Enter your password']/preceding::input[@type='password']")).sendKeys(login.getLogins().getProperty("pass.validuser"));
            commonFunctions.commonButtonClick("Next");
            commonFunctions.sleepTime(20);
        }catch (NoSuchElementException ignored){

        }

//        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"view_container\"]//ul")));
//        List<WebElement> accounts = driver.findElements(By.xpath("//*[@id=\"view_container\"]//ul/li"));
//        int row = 0;
//        int count = 1;
//        for (WebElement acc : accounts){
//            if (acc.getText().contains(account)){
//                row = count;
//                break;
//            }else {
//                count++;
//            }
//        }
//        driver.findElement(By.xpath("//*[@id=\"view_container\"]//ul/li["+ row +"]")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(),'Upload')]")));
    }

    @And("I Upload the exported file")
    public void iUploadTheExportedFile() {
        File downloaded_File = BeSpin.getLastModified();
        String path = commonFunctions.attachLocation(downloaded_File.getName());
        driver.findElement(By.xpath("//div[@id=':x']/div[contains(text(),'Select a file from your device')]")).sendKeys(path);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[contains(text(),'Upload')]")));
    }

    @And("I find the duplicate contacts and delete that contact")
    public void iFindTheDuplicateContactsAndDeleteThatContact() {
        // Create a set to store the unique contacts
        Set<String> uniqueContacts = new HashSet<>();

        // Create a set to store the duplicate contacts
        Set<String> duplicateContacts = new HashSet<>();
        String contactName = null;
        int rows = commonFunctions.gridRowCount();
        for (int i=1; i<=rows; i++){
            String s = String.valueOf(i);
            contactName = commonFunctions.gridEntryText("row"+s,"Name");
            // Check if the contact name is already in the set of unique contacts
            if (!uniqueContacts.add(contactName)) {
                // If the contact name is already in the set, add it to the set of duplicate contacts
                duplicateContacts.add(contactName);
                //Selecting the duplicate contacts for deletion
                Assert.assertTrue(commonFunctions.commonCheckBoxClick(Integer.parseInt(s)));
            }
        }
        out.println("Duplicate contacts "+ duplicateContacts);
        //Clicking the delete button
        Assert.assertTrue(commonFunctions.commonButtonClick("delete_outline"));
        Assert.assertTrue(commonFunctions.commonButtonClick("OK"));
        commonFunctions.sleepTime(2);

        if (duplicateContacts.size() >= 10){
            iVerifyPopupIsDisplayed("An approval request has been sent to the admin.");
            iClickTheLink("Dashboard");
            iClickTheButtonOfColumnOfRow("edit","Action",1);
            commonStepFunctions.iEnterInTheField("I AGREE", "Please type \"I AGREE\"");
            commonFunctions.commonButtonClick("Approve");
            commonFunctions.sleepTime(1);
            driver.navigate().refresh();
            iScrollHorizontally();
            iVerifyColumnForRowShouldBeAutopopulate("Status",1);
            iVerifyColumnForRowShouldBeAutoPopulateWithMessage("Message",1,"Successfull");
            iClickTheLink("Contacts");
            commonFunctions.sleepTime(1);
        }
        int count = 0;
        int filteredRows = commonFunctions.gridRowCount();
        for (int i=1; i<=filteredRows; i++){
            String s = String.valueOf(i);
            String filteredContactName = commonFunctions.gridEntryText("row"+s,"Name");
            if (duplicateContacts.contains(filteredContactName)){
                count++;
            }
        }
        if (count > duplicateContacts.size()){
            Assert.fail("Duplicate contacts are not get deleted");
        }else {
            out.println("Duplicate contacts are get deleted");
        }
    }

    @Then("I verify {string} validation message")
    public void iVerifyValidationMessage(String validation) {
        WebElement validationMsg = driver.findElement(By.xpath("//label[normalize-space()='"+ validation +"']"));
        Assert.assertTrue(validationMsg.getText().contains(validation));

    }

    @And("I open the details for selected row")
    public void iOpenTheDetailsForSelectedRow() {
        String linkText = valueStore.get("Grid Header Input");
        if (linkText != null){
            commonFunctions.commonLinkClick(linkText);
            pageLoaded();
        }
    }

    @And("I select {string} from condition list dropdown")
    public void iSelectFromConditionListDropdown(String selection) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("conditionDropDown")));
        driver.findElement(By.id("conditionDropDown")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class,'mdc-menu-surface--open')]/ul[@class='mdc-list']")));
        WebElement select = driver.findElement(By.xpath("//div[contains(@class,'mdc-menu-surface--open')]/ul[@class='mdc-list']/li[@data-value='"+ selection +"']"));
        try {
            select.click();
        }catch (ElementClickInterceptedException e){
            commonFunctions.clickErrorHandle(e.toString(),select);
        }
    }

    @And("I add following members to the workflow")
    public void iAddFollowingMembersToTheWorkflow(List<String> table) {
        for (int i=1; i<= table.size(); i++){
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("conditionDropDown")));
            WebElement dropDown = driver.findElement(By.xpath("(//*[@id=\"filterForm\"]//label[normalize-space()='Attributes'])["+ i +"]"));
            try {
                dropDown.click();
            }catch (ElementClickInterceptedException ex){
                commonFunctions.clickErrorHandle(ex.toString(),dropDown);
            }
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class,'mdc-menu-surface--open')]/ul[@class='mdc-list']")));
            WebElement select = driver.findElement(By.xpath("//div[contains(@class,'mdc-menu-surface--open')]/ul[@class='mdc-list']/li[@data-value='emails']"));
            try {
                select.click();
            }catch (ElementClickInterceptedException e){
                commonFunctions.clickErrorHandle(e.toString(),select);
            }
            pageLoaded();
            commonFunctions.sleepTime(1);

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("conditionDropDown")));
            WebElement dropDown2 = driver.findElement(By.xpath("(//*[@id=\"filterForm\"]//label[normalize-space()='Condition'])["+ i +"]"));
            try {
                dropDown.click();
            }catch (ElementClickInterceptedException ex){
                commonFunctions.clickErrorHandle(ex.toString(),dropDown2);
            }
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class,'mdc-menu-surface--open')]/ul[@class='mdc-list']")));
            WebElement select2 = driver.findElement(By.xpath("//div[contains(@class,'mdc-menu-surface--open')]/ul[@class='mdc-list']/li[@data-value='contains']"));
            wait.until(ExpectedConditions.elementToBeClickable(select2));
            try {
                js.executeScript("arguments[0].click();",select2);
            }catch (ElementClickInterceptedException e){
                commonFunctions.clickErrorHandle(e.toString(),select2);
            }
            WebElement input = driver.findElement(By.xpath("(//label[normalize-space()='Value']/preceding::div[@class='mdc-layout-grid__cell mdc-layout-grid__cell--span-3 jobcon']/div/input)["+ i +"]"));
            input.sendKeys(table.get(i - 1));
            commonStepFunctions.iClickTheButton("OR");
            commonFunctions.sleepTime(1);
        }
        //Strong Added members for later use
        valueListStringStore.put("Member Added To Policy",table);

    }

    @When("I open new tab and switch to that tab")
    public void iOpenNewTabAndSwitchToThatTab() {
        driver.switchTo().newWindow(WindowType.TAB);
        Set<String> handles = driver.getWindowHandles();
        ArrayList<String> availableWindows = new ArrayList<>(handles);
        String pWindow = availableWindows.get(0);
        String cWindow = availableWindows.get(1);
        driver.switchTo().window(pWindow);
        driver.close();
        driver.switchTo().window(cWindow);
    }


    @And("I click on the {string} table column")
    public void iClickOnTheTableColumn(String tableColumn) {
        WebElement header1 = driver.findElement(By.xpath("//thead/descendant::*[normalize-space()='" + tableColumn + "']"));
        commonFunctions.rightClick(header1);
    }

    @And("I click on the Copy Folders option")
    public void iClickOnTheCopyFoldersOption() {
        js.executeScript("arguments[0].click();",commonFunctions.copyFoldersListOption);
        commonFunctions.pageLoaded();
        System.out.println("Clicks Copy Folders option");
    }

    @And("I wait for the shared drive file container to be visible")
    public void iWaitForTheSharedDriveFileContainerToBeVisible() {
        //This method is created to wait till the shared drive file container becomes visible
        WebElement element = driver.findElement(By.id("sharedDriveFileContainer"));
        wait.until(ExpectedConditions.domAttributeToBe(element,"style","display: inline-flex;"));
    }

    @And("I get the folder name for {string}")
    public void iGetTheFolderNameFor(String row) {
        String folderName = commonFunctions.gridEntry(row,"Name").getText();
        String name = folderName.split(" ")[2];
        out.println("Folder NAme : "+name);
        valueStore.put("Folder Name",name);
    }

    @Then("I verify selected folder is displayed in drive")
    public void iVerifySelectedFolderIsDisplayedInDrive() {
        Assert.assertTrue(driver.findElements(By.xpath("//*[@id=\"context-dropdown-menu-people-file-shared-drive\"]/tbody/descendant::span[normalize-space(text())='"+ valueStore.get("Folder Name") +"']")).size() > 0,"Folder \""+ valueStore.get("Folder Name") +"\" is not displayed in the drive");
        out.println("Folder \""+ valueStore.get("Folder Name") +"\" is displayed in the drive");
    }
    @When("I refresh the page")
    public void iRefreshThePage() {
        System.out.println("Refreshing page");
        driver.navigate().refresh();
    }

    // People Section
    @And("{string} page is opened")
    public void pageIsOpened(String expectation) {
        String actual = driver.getTitle();
        Assert.assertEquals(actual, expectation,"Expectation is :"+ expectation + " but found : " +actual);
    }

    @And("I click the search icon")
    public void iClickTheSearchIcon() {
        Assert.assertTrue(commonFunctions.searchIconClick(),"Could not clicked on search icon");
        commonFunctions.pageLoaded();
    }
    @Then("I verify results shown as per search criteria")
    public void iVerifyResultsShownAsPerSearchCriteria() {
        List<WebElement> users = driver.findElements(By.xpath("//div[contains(@class,'col-xs-12 w33 msm-card-item')]"));
        System.out.println("Total search results : " + users.size());
        users.forEach(user ->{
            String resultText = user.getText().toLowerCase();
            Assert.assertTrue(resultText.contains(valueStore.get("Search for Users").toLowerCase()));
        });
    }

    @Then("I verify {string} is shown")
    public void iVerifyIsShown(String errValidation) {
        WebElement validation = driver.findElement(By.xpath("//div[contains(text(),'" + errValidation + "')]"));
        Assert.assertTrue(validation.isDisplayed() && validation.getText().contains(errValidation));
    }

    @When("I open random user profile")
    public void iOpenRandomUserProfile() {
        String user = commonFunctions.getRandomUser().getText();
        Assert.assertTrue(commonFunctions.commonFieldEnter("Search for Users",user));
        iClickTheSearchIcon();
        System.out.println("Opening details of : " + user);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"view-userList\"]/descendant::h2[normalize-space()='"+ user +"']")));
        commonFunctions.commonElement("//*[@id=\"view-userList\"]/descendant::h2[normalize-space()='"+ user +"']").click();
        commonFunctions.pageLoaded();

    }

    @And("I open user details form")
    public void iOpenUserDetailsForm() {
        commonFunctions.sleepTime(1);
        Assert.assertTrue(commonFunctions.userProfileEditBtnClick(),"edit button could not found");
        System.out.println("Clicked on edit button");
        commonFunctions.pageLoaded();
    }

    @Then("The record is added")
    public void theRecordIsAdded(List<String> fields) {
        fields.forEach(key -> {
            boolean foundItem = false;
            if (commonFunctions.commonField(key) != null) {
                foundItem = true;
                String found = commonFunctions.commonFieldRead(key);
                if (key.contains("Date")) {
                    SimpleDateFormat formatActual = new SimpleDateFormat("MM/dd/yyyy h:mm aa", Locale.ENGLISH);
                    SimpleDateFormat formatExpected = new SimpleDateFormat("dd-MM-yyyy HHmmss");
                    try {
                        Date expectedDate = formatExpected.parse(BaseUtil.valueStore.get(key));
                        String reformattedExpectedDate = formatActual.format(expectedDate);
                        if (key.equals("Mail Date")) {
                            reformattedExpectedDate = reformattedExpectedDate.substring(0, 11) + "3:15 PM";
                        }
                        System.out.println("Comparing actual (\"" + found + "\") to expected (\"" + reformattedExpectedDate + "\") in \"" + key + "\" field");
                        Assert.assertEquals(found, reformattedExpectedDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("Comparing actual (\"" + found + "\") to expected (\"" + BaseUtil.valueStore.get(key) + "\") in \"" + key + "\" field");
                    Assert.assertEquals(found, BaseUtil.valueStore.get(key));
                }
            } else if (commonFunctions.commonDropDown(key) != null) {
                foundItem = true;
                String found = commonFunctions.commonDropDownRead(key);
                System.out.println("Comparing actual (\"" + found + "\") to expected (\"" + BaseUtil.valueStore.get(key) + "\") in \"" + key + "\" drop down");
                Assert.assertEquals(found, BaseUtil.valueStore.get(key));
            } else if (commonFunctions.commonTextArea(key) != null) {
                foundItem = true;
                String found = commonFunctions.commonTextAreaRead(key);
                System.out.println("Comparing actual (\"" + found + "\") to expected (\"" + BaseUtil.valueStore.get(key) + "\") in \"" + key + "\" test area");
                Assert.assertEquals(found, BaseUtil.valueStore.get(key));
            }
            Assert.assertTrue(foundItem, "Could not find " + key + " field, drop down, or text area!");
        });
        System.out.println("Record has been successfully read");
    }

    @Then("I verify following options are present")
    public void iVerifyFollowingOptionsArePresent(List<List<String>> table) {
        // Created to verify User profiles More (...) button options
        commonFunctions.sleepTime(1);
        List<String> list = table.get(0);
        list.forEach(option -> {
            WebElement element = driver.findElement(By.xpath("//*[@id=\"profile-header\"]//ul/li/span[normalize-space()='"+ option +"']"));
            Assert.assertTrue(element.isDisplayed());
            out.println(option + " Option is displayed");
        });
    }

    @And("I click the {string} option of the user profile")
    public void iClickTheOptionOfTheUserProfile(String option) {
        Assert.assertTrue(commonFunctions.userProfileListOptionClick(option));
        out.println("Clicked on "+ option +" option");
        commonFunctions.pageLoaded();
    }

    @And("I select {string} from transfer ownership")
    public void iSelectFromTransferOwnership(String option) {
        // Created to select option from the transfer ownership dropdown for the Data transfer feature
        commonFunctions.sleepTime(1);
        WebElement filter = driver.findElement(By.xpath("//li[contains(@class,'mdc-ripple-upgraded')][normalize-space()='"+ option +"']"));
        filter.click();
        System.out.println("Select \"" + option + "\" option");
    }

    @And("I click the Drive dropdown in Transfer ownership popup")
    public void iClickTheDriveDropdownInTransferOwnershipPopup() {
        Assert.assertTrue(commonFunctions.clickDriveTransferOwnershipPopup());
        commonFunctions.pageLoaded();
    }

    @And("I click the Transfer ownership dropdown in Transfer ownership popup")
    public void iClickTheTransferOwnershipDropdownInTransferOwnershipPopup() {
        // Created to click the Transfer ownership dropdown
        out.println("Clicking Transfer ownership dropdown");
        driver.findElement(By.xpath("//*[@id=\"data-transfer-option\"]")).click();
    }

    @And("I click on random department user on users profile page")
    public void iClickOnRandomDepartmentUserOnUsersProfilePage() {
        commonFunctions.sleepTime(1);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("employmentInfo")));
        WebElement randomUser = commonFunctions.getRandomDepartmentUser();
        String randomUserName = null;
        try {
            commonFunctions.moveToThatElement(randomUser);
            randomUserName = randomUser.findElement(By.xpath("following::div/div[contains(@class,'tooltip-inner')]")).getText();
            randomUser.click();
        }catch (ElementClickInterceptedException e){
            commonFunctions.clickErrorHandle(e.toString(),randomUser);
        }
        valueStore.put("Random User Name",randomUserName);
        commonFunctions.pageLoaded();
    }

    @Then("I verify selected users profile get opened")
    public void iVerifySelectedUsersProfileGetOpened() {
        String profileName = driver.findElement(By.xpath("//*[@id=\"profile-header\"]//h2")).getText();
        Assert.assertTrue(profileName.contains(valueStore.get("Random User Name")));
        out.println(profileName +" Users profile got opened");
    }

    @And("I login into the patronum app as {string}")
    public void iLoginIntoThePatronumAppAs(String user) {
        String url = null;
        if (user.equalsIgnoreCase("super admin")){
            url = login.getLogins().getProperty("email.superuser");
        }else if (user.equalsIgnoreCase("standard user")){
            url = login.getLogins().getProperty("email.standarduser");
        }else if (user.equalsIgnoreCase("privileges user")){
            url = login.getLogins().getProperty("email.privilegesuser");
        }
        String env = login.getConfig().getProperty("environment");
        System.out.println("Navigating to " + env.toUpperCase() + " login page");
        String getUrl = login.getConfig().getProperty(env + ".url").split("fake")[0];
        driver.get(getUrl+"fake/"+url);
        commonFunctions.pageLoaded();
        commonFunctions.sleepTime(2);
    }

    @Then("The following elements does not exist")
    public void theFollowingElementsDoesNotExist(List<List<String>> values) {
        BaseUtil.pageLoaded();
        List<String> table = values.get(0);
        SoftAssert softAssert = new SoftAssert();
        table.forEach(element -> {
            boolean linkExists = false;
            System.out.println("Checking the element does not existence : " + element);
            if (commonFunctions.commonField(element) == null ||
                    commonFunctions.commonDropDown(element) == null ||
                    commonFunctions.commonTextArea(element) == null ||
                    commonFunctions.commonButton(element) == null ||
                    commonFunctions.commonCheckBox(element) == null) {
                linkExists = true;
            } else {
                try {
                    // following xpath used to find the common links present on home page
                    linkExists = driver.findElements(By.xpath("//a/descendant::span[normalize-space()='" + element + "']")) == null;

                    //following xpath used to find the link icon present on home page
                    linkExists = driver.findElements(By.xpath("//i[normalize-space()='" + element + "']")) == null;
                } catch (org.openqa.selenium.NoSuchElementException ignored) {
                }
            }
            softAssert.assertTrue(linkExists, "Found " + element + " field!");

        });
        softAssert.assertAll();
    }

    @And("I open the profile of that user")
    public void iOpenTheProfileOfThatUser() {
        String user = commonFunctions.getLoggedInUserName();
        Assert.assertTrue(commonFunctions.commonFieldEnter("Search for Users",user));
        iClickTheSearchIcon();
        System.out.println("Opening details of : " + user);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"view-userList\"]/descendant::h2[normalize-space()='"+ user +"']")));
        commonFunctions.commonElement("//*[@id=\"view-userList\"]/descendant::h2[normalize-space()='"+ user +"']").click();
        commonFunctions.pageLoaded();
    }

    @Then("I see {string} is display")
    public void iSeeIsDisplay(String view) {
        String actualView = commonFunctions.getToolTipText();
        Assert.assertEquals(actualView, view);
        out.println(actualView +" is displayed");
    }

    @And("I move to {string} button")
    public void iMoveToButton(String element) {
        commonFunctions.moveToThatElement(commonFunctions.commonButton(element));
    }

    @When("I select {string} member from Enter Name or Emails dropdown")
    public void iSelectMemberFromEnterNameOrEmailsDropdown(String selection) {
        Assert.assertTrue(commonFunctions.clickEnterNameOrEmails());
        commonFunctions.sleepTime(3);
        Assert.assertTrue(commonFunctions.selectMemberFromEnterNamesOrEmails(selection));
        valueStore.put("AddedMember",selection);
    }

    /**
     * Selects the option from the path dropdown present in file Deprovision process
     * @param selection
     */
    @And("I select {string} from path dropdown")
    public void iSelectFromPathDropdown(String selection) {
        Assert.assertTrue(commonFunctions.transferOwnershipPathInFileDeprovisionTo(selection));
        out.println("Selected "+ selection +" from the path dropdown");
        valueStore.put("path",selection);


    }
    @Then("I Should verify in {string} present in Attribute")
    public void i_should_verify_in_present_in_attribute(String value) {
        out.println("attribute value" +commonFunctions.AttributeDropdown());
        Assert.assertEquals(commonFunctions.AttributeDropdown(),value);
        out.println("selected "+ value + " is present");


//        WebElement attr = driver.findElement(By.xpath("//div[contains(text(),'Executor')]"));
//        String  actual= attr.getText();
//        Assert.assertEquals(actual,string);

    }
    @Then("I Should verify in {string} present in Assets")
    public void i_should_verify_in_present_in_assets(String value) {
        Assert.assertEquals(value,commonFunctions.assetsDropdown());
        out.println("selected "+ value + " is present");
//        WebElement attr = driver.findElement(By.xpath("//div[contains(text(),'Calendar')]"));
//        String  actual= attr.getText();
//        Assert.assertEquals(actual,string);



    }
    @Then("I Should verify in {string} present in Enter names or emails")
    public void i_should_verify_in_present_in_enter_names_or_emails(String value) {
        Assert.assertEquals(value,commonFunctions.emailDropdown());
        out.println("selected "+ value + " is present");

//        WebElement attr = driver.findElement(By.xpath("//span[contains(text(),'Harry Kane')]"));
//        String  actual= attr.getText();
//        Assert.assertEquals(actual,string);

    }

    @Then("i verify the Executor present in workflow Action")
    public void iVerifyTheExecutorPresentInWorkflowAction() {
        out.println(commonFunctions.emailDropdown());
     out.println(valueStore.get("Executor").split("\\n")[0]);
        Assert.assertEquals(valueStore.get("Executor").split("\\n")[0] ,commonFunctions.emailDropdown());

    }

    @Then("I verify {string} should be present")
    public void iVerifyShouldBePresent(String logo) {

        Assert.assertTrue(driver.findElement(By.id("profile_main_img")).isDisplayed(),"or logo not present");
        out.println("patronum " + logo + "is present in home page");


    }

    @And("I enter {string} in  {string} field")
    public void iEnterInField(String textArea, String text) {
       Assert.assertTrue(commonFunctions.commonTextAreaEnter(textArea,text));
       out.println("value enter in:" + textArea);

    }
}


