package Pages;

import Base.BaseUtil;
import Base.CommonFunctions;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import java.io.File;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static Base.BaseUtil.driver;
import static Base.BaseUtil.valueStore;

/**
 * This class extends {@link CommonFunctions} and is meant to be the class used to store the functions for your project.
 *
 * The first step is to use the refactor ability in Intellij to rename this class to match your project name.
 *
 * Once that is done, all common functions will use this class and any changes required to common function code should be done by overriding the method in this class.
 */
public class BeSpin extends CommonFunctions {
    public BeSpin(WebDriver driver) {
        super(driver);
    }

    public void fieldsExist(List<String> table) {
        BaseUtil.pageLoaded();
        SoftAssert softAssert = new SoftAssert();
        table.forEach(value -> {
            boolean linkExists = false;
            System.out.println("Checking the existence of: " + value);
            if (commonField(value) != null) {
                linkExists = true;
            } else if (commonDropDown(value) != null) {
                linkExists = true;
            } else if (commonTextArea(value) != null) {
                linkExists = true;
            } else if (commonButton(value) != null) {
                linkExists = true;
            } else if (commonCheckBox(value) != null) {
                linkExists = true;
            } else {
                try {
                    // following xpath used to find the common links present on home page
                    linkExists = getDriver().findElements(By.xpath("//a/descendant::span[normalize-space()='" + value + "']")) != null;

                    //following xpath used to find the link icon present on home page
                    linkExists = getDriver().findElements(By.xpath("//i[normalize-space()='" + value + "']")) != null;
                } catch (org.openqa.selenium.NoSuchElementException ignored) {
                }
            }
            softAssert.assertTrue(linkExists, "Could not find " + value + " field!");

        });
        softAssert.assertAll();
    }
    public WebElement commonCheckBox(int row){
        List<WebElement> checkBoxes = getDriver().findElements(By.xpath("//tbody/tr[" + row + "]/td[1]/div[1]/input[1]"));
        if (checkBoxes.size() > 0) {
            for (WebElement chkbx : checkBoxes) {
                if (chkbx.isEnabled()) {
                    return chkbx;
                }
            }
        }
        return null;
    }
    public boolean commonCheckBoxClick(int row) {
        WebElement chkbx = commonCheckBox(row);
        if (chkbx != null) {
            getJs().executeScript("arguments[0].click();", chkbx);
            return true;
        }
        return false;
    }
    public boolean commonCheckBoxSelected(int row) {
        WebElement chkbx = commonCheckBox(row);
        if (chkbx != null) {
            return chkbx.isSelected();
        }
        System.out.println("Could not find checkbox: " + row);
        return false;
    }

    public WebElement editButton(int rowNumber){
        String s = String.valueOf(rowNumber);
        List<WebElement> buttons = getDriver().findElements(By.xpath("//tbody/tr[" + s + "]/td[5]"));
        if (buttons.size() > 0){
            for (WebElement button : buttons){
                if (button.isDisplayed()){
                    return button;
                }
            }
        }
        return null;
    }

    public boolean editButtonClick(int rowNumber){
        WebElement btn = editButton(rowNumber);
        if (btn != null){
            btn.click();
            return true;
        }
        return false;
    }

    public boolean CheckIfDeletedGroupIdNotExists(List<String> list, List<String> deletedGroups) {
        assert !list.isEmpty();
        assert !deletedGroups.isEmpty();
        boolean isExist = false;
        for (String deletedGroup:deletedGroups){
            if(list.contains(deletedGroup))
            {
                isExist = true;
                break;
            }
        }

        return isExist;
    }

    public static File getLastModified() {
        File directory = new File(BaseUtil.dLFolder);
        File[] files = directory.listFiles(File::isFile);
        long lastModifiedTime = Long.MIN_VALUE;
        File chosenFile = null;

        if (files != null) {
            for (File file : files) {
                if (file.lastModified() > lastModifiedTime) {
                    chosenFile = file;
                    lastModifiedTime = file.lastModified();
                }
            }
        }
        return chosenFile;
    }

    public void waitForSnackbarPopUp(String text){
        try {
            WebElement snackBarPopUp = getDriver().findElement(By.xpath("//div[@id='snackbar-mdc-main']//div[@class='mdc-snackbar__label msm-text']"));
            WebDriverWait webDriverWait = new WebDriverWait(getDriver(), Duration.ofSeconds(2));
            webDriverWait.until(ExpectedConditions.textToBePresentInElement(snackBarPopUp,text));
            Assert.assertTrue(snackBarPopUp.getText().equalsIgnoreCase(text),"popup not displayed");
        }catch (TimeoutException | NoSuchElementException e){
            waitForSnackbarPopUp(text);
        }
    }

    public void waitForStatusToChange(){
        try {
            WebDriverWait webDriverWait = new WebDriverWait(getDriver(), Duration.ofSeconds(5));
            webDriverWait.until(ExpectedConditions.textToBe(By.xpath("//tbody/tr[1]/td[7]/label[1]"),"Successfull"));
        }catch (TimeoutException | NoSuchElementException e){
            getDriver().navigate().refresh();
            waitForStatusToChange();
        }
    }

    // Drives Functionality Element initializing
    @FindBy(how = How.XPATH, using = "//ul[@id='sc-filter-list']//div[@class='mdc-card filter-main status-card-filter']//span[normalize-space()='Drive Type']")
    public WebElement driveTypeFilterPopUp;
    @FindBy(how = How.XPATH, using = "//div[text()='Internal']")
    public WebElement driveTypeDropDownIcon;

    public void sleepTime(long seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (Exception ignored) {

        }
    }

    public void rightClick(WebElement webElement){
        Actions actions = new Actions(getDriver());
        actions.contextClick(webElement).build().perform();
    }

    /**
     * Moves the mouse cursor to the center of the given element using the Actions class.
     *
     * @param webElement the element to move the mouse cursor to.
     */
    public void moveToThatElement(WebElement webElement){
        Actions actions = new Actions(getDriver());
        actions.moveToElement(webElement).build().perform();
    }

    @FindBy(how = How.XPATH, using = "//li[@class='mdc-list-item msm-calendar-item dialog-button-mdc share-sharedDrive mdc-ripple-upgraded']")
    public WebElement shareListOption;
    @FindBy(how = How.XPATH, using = "//li[@class='mdc-list-item msm-calendar-item dialog-button-mdc rename mdc-ripple-upgraded']")
    public WebElement renameListOption;
    @FindBy(how = How.XPATH, using = "//li[@class='mdc-list-item msm-calendar-item dialog-button-mdc shortcutToSharedDrive mdc-ripple-upgraded']")
    public WebElement addShortcutListOption;
    @FindBy(how = How.XPATH, using = "//li[@class='mdc-list-item msm-calendar-item dialog-button-mdc sharedDriveMoveTo mdc-ripple-upgraded']")
    public WebElement addListOption;
    @FindBy(how = How.XPATH, using = "//li[@class='mdc-list-item msm-calendar-item dialog-button-mdc sharedDriveCopyFolder mdc-ripple-upgraded']")
    public WebElement copyFoldersListOption;
    @FindBy(how = How.XPATH, using = "//li[@class='mdc-list-item msm-calendar-item dialog-button-mdc file_copy mdc-ripple-upgraded']")
    public WebElement makeACopyListOption;
    @FindBy(how = How.XPATH, using = "//li[@eventname='createNewFolder_peopleSection']")
    public WebElement createNewFolderListOption;
    @FindBy(how = How.XPATH, using = "//li[@class='mdc-list-item msm-calendar-item dialog-button-mdc deletePeopleFile contextMenu mdc-ripple-upgraded']")
    public WebElement removeListOption;
    @FindBy(how = How.ID, using = "input-rename-file-newName")
    public WebElement renameInputField;
    @FindBy(how = How.XPATH, using = "//form[@id='msm_rename_file_form']//h2[@id='my-dialog-title-renameFile']")
    public WebElement renameAlertPopUp;

    //form[@id='msm_rename_file_form']//h2[@id='my-dialog-title-renameFile']


    /** This Method is used to find permission options in Shared Drives
    *@param linkText The text of the link without leading or trailing spaces
     **/
    public WebElement commonLink(String linkText){
        List<WebElement> links = getDriver().findElements(By.xpath(
                "//span[normalize-space()='done "+ linkText +"']"));

        if (links.size() > 0){
            for (WebElement link : links){
                if (link.isDisplayed()){
                    return link;
                }
            }
        }
        return null;
    }
    public WebElement popUpCheckBox(String tableID, int row){
        List<WebElement> popUpCheckBox = getDriver().findElements(By.xpath(
                "//tbody[@id='"+ tableID +"']/tr[" + row + "]/td[1]/div[1]/input[1]"));

        if (popUpCheckBox.size() > 0) {
            for (WebElement chkbx : popUpCheckBox) {
                if (chkbx.isEnabled()) {
                    return chkbx;
                }
            }
        }
        return null;
    }

    public boolean popUpCheckBoxClick(String tableID, int row) {
        WebElement chkbx = popUpCheckBox(tableID,row);
        if (chkbx != null) {
            getJs().executeScript("arguments[0].click();", chkbx);
            return true;
        }
        return false;
    }
    public boolean popUpCheckBoxSelected(String tableID, int row) {
        WebElement chkbx = popUpCheckBox(tableID,row);
        if (chkbx != null) {
            return chkbx.isSelected();
        }
        System.out.println("Could not find checkbox: " + row);
        return false;
    }
    @FindBy(how = How.XPATH, using = "//*[@id=\"LiveTask-table-for-dashboard\"]/tbody/tr[1]/th[3]/b")  //Dashboard task Progress Count
    public WebElement taskProgressCount;

    @FindBy(how = How.XPATH, using = "(//div[@class='task-percentage'])[1]")
    public WebElement taskPercentage;


    public int taskPercentage(){
        String perc = taskPercentage.getText();
        System.out.println("Task Percentage : "+ perc);
        String[] percentage = perc.split("\\.");
        assert percentage != null;
        return Integer.parseInt(percentage[0]);
    }

    public String taskProgressCount(){
        String progressCount = null;
        if (taskProgressCount.isDisplayed()){
            progressCount = taskProgressCount.getText();
        }
        return progressCount;
    }

    public int getTotalFilesCount(int row){
        WebElement totalCount = null;
        try {
            totalCount = getDriver().findElement(By.xpath("//*[@id=\"unShareFile_confirm_popup_Append\"]/tr/td[2]/a/span"));
        }catch (NoSuchElementException e){
            e.printStackTrace();
        }
        return Integer.parseInt(totalCount.getText());
    }
    public int gridRowCount(String tableID){
        return getDriver().findElements(By.xpath("//*[@id='"+ tableID +"']//tr")).size();
    }

    public int actionCountInDashboardTab(){
        return Integer.parseInt(gridEntryText("row 1","Action Count"));
    }
    public String errorMessageDisplay(){
        List<WebElement> errors = getDriver().findElements(By.xpath(
                "//label[@class='error'][contains(normalize-space(),'This field is required')]"));

        if (errors.size() > 0){
            for (WebElement error : errors){
                if (error.isDisplayed()){
                    return error.getText();
                }
            }
        }
        return null;
    }

    public WebElement isFolderDeleted(String folder){
        List<WebElement> elements = getDriver().findElements(By.xpath("//span[normalize-space(text())='"+ folder +"']"));

        if (elements.size() > 0){
            for (WebElement element : elements){
                if (element.isDisplayed()){
                    return element;
                }
            }
        }
        return null;
    }

    public boolean clickMakeACopy(){
        try {
            getJs().executeScript("arguments[0].click()", makeACopyListOption);
        } catch (ElementClickInterceptedException e) {
            clickErrorHandle(e.toString(), makeACopyListOption);
        } catch (NoSuchElementException e) {
            return false;
        }
        return true;
    }

    public boolean clickEnterNameOrEmails(){
        List<WebElement> elements = getDriver().findElements(By.xpath("//span[contains(@class,'select2-selection select2-selection')]"));

        elements.removeIf(element -> !element.isDisplayed());
        if (!elements.isEmpty()){
            WebElement ele = elements.get(elements.size() - 1);
            ele.click();
            return true;
        }else return false;
    }

    public WebElement commonElement(String xpath) {
        List<WebElement> elements = driver.findElements(By.xpath(xpath));
        if (elements.size() > 0) {
            for (WebElement btn : elements) {
                if (btn.isDisplayed() && btn.isEnabled()) {
                    return btn;
                }
            }
        }
        return null;
    }

    @Override
    public boolean commonButtonClick(WebElement button) {

        try {
            getJs().executeScript("arguments[0].click()", button);
        } catch (ElementClickInterceptedException e) {
            clickErrorHandle(e.toString(), button);
        } catch (NoSuchElementException e) {
            return false;
        } catch (StaleElementReferenceException e){
            e.printStackTrace();
        }
        return true;

    }


    @Override
    public WebElement commonButton(String button) {
        try {
            List<WebElement> buttons = driver.findElements(By.xpath(
                    "//button[normalize-space()='" + button + "']"));
            //xpath used to find the notification icon and google app button
            buttons.addAll(driver.findElements(By.xpath("//div[@class='mdc-chip']//i[text()='"+ button +"']")));

            //following xpath is used to locate Approve Deletion Request popup buttons
            buttons.addAll(driver.findElements(By.xpath("//input[@value='"+ button +"']")));

            //Xpath is used to handle Drives filter option -> Drives Type
            buttons.addAll(driver.findElements(By.xpath("//li[contains(text(),'"+ button +"')]")));

            buttons.addAll(driver.findElements(By.xpath("//i[normalize-space()='"+ button +"']")));
            if (buttons.size() > 0) {
                for (WebElement btn : buttons) {
                    if (btn.isDisplayed() &&  btn.isEnabled()) {
                        return btn;
                    }
                }
            }
        }catch (StaleElementReferenceException e){
            e.printStackTrace();
        }

        return null;
    }

    public boolean searchIconClick(){
        WebElement elementToBeClicked = null;
        try {
            elementToBeClicked = getDriver().findElement(By.cssSelector(".mdc-icon-button.material-icons.m-none.search-inline-searchIcon"));
            elementToBeClicked.click();
        }catch (ElementClickInterceptedException e){
            clickErrorHandle(e.toString(),elementToBeClicked);
        }catch (NoSuchElementException exc){
            return false;
        }
        return true;
    }

    /**
     * This method returns a randomly selected WebElement object representing a user from a list of visible users on the current web page.
     *
     * @return a randomly selected WebElement representing a user from the list of visible users on the current web page
     */
    public WebElement getRandomUser(){
        List<WebElement> users = getDriver().findElements(By.xpath("//*[@id=\"view-userList\"]/descendant::div/h2[contains(@class,'TextTruncateMediaRight')]"));
        System.out.println("Visible Users : "+ users.size());
        List<WebElement> nonEmptyUsers = new ArrayList<>();

        for (WebElement user : users){
            String userName = user.getText();
            if (!userName.isEmpty()){
                nonEmptyUsers.add(user);
            }
        }

        int randomIndex = new Random().nextInt(nonEmptyUsers.size());
        return nonEmptyUsers.get(randomIndex);
    }


    /**
     * Clicks the Edit button present on the Users Profile Page.
     *
     * @return true if the click was successful, false otherwise
     */
    public boolean userProfileEditBtnClick(){
        try {
            WebElement editButton = commonElement("//*[@id=\"employmentInfo\"]/descendant::button");
            editButton.click();
        } catch (NoSuchElementException e){
            return false;
        }
        return true;
    }

    @Override
    public WebElement commonField(String field) {
        WebElement selectedField = null;
        List<WebElement> fields = driver.findElements(By.xpath(
                "//label[normalize-space(text())='" + field + "']/following-sibling::input"));
        fields.addAll(driver.findElements(By.xpath(
                "//label[translate(.,'\u00A0','') ='" + field + "']/following-sibling::input")));

        // List to handle fields in the Time Tracking pop-up
        fields.addAll(driver.findElements(By.xpath(
                "//label[normalize-space()='" + field + "']/following-sibling::div/input")));

        //following xpath is used to handle filter input box
        fields.addAll(driver.findElements(By.xpath("//label[normalize-space(text())='"+ field +"']/preceding::input[@id='tf-outlined']")));

        //following xpath is used to find Approve Deletion Request Popup input field
        fields.addAll(driver.findElements(By.xpath("//label[normalize-space(text())='"+ field +"']/preceding::input[@id='tf-deletionCode']")));
        //Xpath used to handle input field which has placeholder
        fields.addAll(driver.findElements(By.xpath("//input[contains(@placeholder,'"+ field +"')]")));
        fields.addAll(driver.findElements(By.xpath("//label[normalize-space(text())='"+ field +"']/preceding::input[@id='tf-createNewFolder-name']")));
        fields.addAll(driver.findElements(By.cssSelector("input#policyName")));
        fields.addAll(driver.findElements(By.xpath("//textarea[@id='policyDes']")));

        fields.addAll(driver.findElements(By.xpath("//label[normalize-space()='"+ field +"']/parent::div/parent::div/preceding-sibling::input[contains(@id,'tf-editUserOrGroupProfile')]")));


        if (fields.size() > 0) {
            long startTime = System.currentTimeMillis();
            while (selectedField == null && (System.currentTimeMillis() - startTime) < 5000) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ignored) {
                }
                for (WebElement fld : fields) {
                    if (fld.isDisplayed()) {
                        selectedField = fld;
                        break;
                    }
                }
            }
        }
        return selectedField;
    }

    /**
     *This method clicks on a given option when the Option list is visible on clicking of More (...) button present on Users Profile Page.
     *
     * @param option the text of the option to be clicked
     * @return true if the click was successful, false otherwise
     */

    public boolean userProfileListOptionClick(String option){
        WebElement optionClicked = null;
        try {
            optionClicked = driver.findElement(By.xpath("//*[@id=\"profile-header\"]//ul/li/span[normalize-space()='"+ option +"']"));
            optionClicked.click();
        }catch (ElementClickInterceptedException e){
            clickErrorHandle(e.toString(),optionClicked);
        }catch (NoSuchElementException exc){
            return false;
        }
        return true;
    }

    /**
     *This method clicks on the "Drive" dropdown button on the Transfer Ownership popup if it is visible.
     *
     * @return A boolean value indicating whether the button was clicked or not.
     */

    public boolean clickDriveTransferOwnershipPopup(){
        try {
            driver.findElement(By.xpath("//*[@id=\"my-dialog-content-user_data_transfer_popup\"]/div[2]/div/div[2]/div/div[1]/div")).click();
        }catch (NoSuchElementException e){
            return false;
        }
        return true;
    }

    /**
     * This method returns a random user from the list of department users on the user's profile page.
     *
     * @return A WebElement representing the randomly selected user.
     */
    public WebElement getRandomDepartmentUser() {
        // Find all the department users on the page using the XPath selector
        List<WebElement> departmentUsers = driver.findElements(By.xpath("//ul[@class='mdc-list people-dept-list']//li"));

        // Print the total number of department users found on the page
        System.out.println("Total Department Users: " + departmentUsers.size());

        // Generate a random index to select a random user from the list of department users
        int randomIndex = new Random().nextInt(departmentUsers.size());

        // Return the WebElement representing the randomly selected user
        return departmentUsers.get(randomIndex);
    }

    /**
     * Gets the name of the currently logged in user
     *
     * @return the name of the currently logged in user
     */
    public String getLoggedInUserName(){
        WebElement userProfile = null;
        WebElement userName = null;
        try {
            userProfile = driver.findElement(By.id("userProfile"));
            moveToThatElement(userProfile);
            userName = driver.findElement(By.xpath("//*[@id=\"app-bar\"]/descendant::div[contains(@class,'tooltip-inner')]/span/b"));
        }catch (NoSuchElementException e){
            e.printStackTrace();
        }

        assert userName != null;
        return userName.getText();
    }

    /**
     * This method selects a member from "Enter names or emails..." dropdown based on the given selection criteria.
     * If the selection contains "<random>", a member will be selected at random from the list.
     * If the selection does not contain "<random>", the member with the given name will be selected.
     *
     * @param selection The selection criteria for choosing a member from the list
     */
    public boolean selectMemberFromEnterNamesOrEmails(String selection) {
        try {
            // Find all the members in the list
            List<WebElement> members = driver.findElements(By.xpath("//ul[contains(@class,'select2-results__options')]/li"));
            System.out.println("Total Members present in list : " + members.size());

            if (selection.contains("<random>")) {
                // If selection contains "<random>", select a random member from the list
                String select = String.valueOf(new Random().nextInt(members.size()));
                WebElement elementToBeSelected = driver.findElement(By.xpath("//ul[contains(@class,'select2-results__options')]/li[" + select + "]"));
                selection = elementToBeSelected.getText();
                elementToBeSelected.click();
                System.out.println("Selected member from the list : " + selection);
            } else {
                // Select the member with the given name from the list
                System.out.println("Selecting " + selection + " from the list");
                List<WebElement> elements = driver.findElements(By.xpath("//ul[contains(@class,'select2-results__options')]/li"));
                for (WebElement element : elements) {
                    getWait().until(ExpectedConditions.refreshed(ExpectedConditions.elementToBeClickable(element)));

                    if (element.getText().split("\\n")[0].contains(selection)) {
                        element.click();
                        System.out.println("Successfully selected member: " + selection);
                        break;
                    }

                }
            }


        } catch (NoSuchElementException | TimeoutException e) {
            System.out.println("Exception occurred while selecting member from the list: " + e.getMessage());
            return false;
        }
        valueStore.put("Executor",selection);
        return true;
    }

    /**
     * Get the text of the tooltip displayed on the element.
     *
     * @return The text of the tooltip.
     */
    public String getToolTipText(){
        return driver.findElement(By.xpath("//div[@class='tooltip-inner']")).getText();
    }

    public boolean transferOwnershipPathInFileDeprovisionTo(String user){
        WebElement pathDropdown = driver.findElement(By.id("transfer_ownership_path"));
        try {
            pathDropdown.click();
        } catch (ElementClickInterceptedException e){
            clickErrorHandle(e.toString(),pathDropdown);
        }
        WebElement select = null;
        getWait().until(ExpectedConditions.visibilityOf(driver.findElement(By.id("macroSuggestionMenu_transferOwnershipPath"))));
        List<WebElement> selections = driver.findElements(By.xpath("//*[@id=\"macroSuggestionMenu_transferOwnershipPath\"]/ul/li/span[normalize-space(text())='"+ user +"']"));
        if (selections.size() > 0){
            long startTime = System.currentTimeMillis();
            while (select == null && (System.currentTimeMillis() - startTime) < 5000) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ignored) {
                }

                for (WebElement selection : selections) {
                    if (selection.isDisplayed()) {
                        select = selection;
                    }
                }
            }
        }
        try {
            select.click();
        } catch (ElementClickInterceptedException e){
            clickErrorHandle(e.toString(),select);
        } catch (NoSuchElementException ex){
            return false;
        }

        return true;
    }

    /**
     * this method is used for dropdown in policies module in Attribute assets and email dropdown
     *
     * @return
     */
    public String AttributeDropdown() {

        return    driver.findElement(By.xpath("//div[@class='mdc-select d-flex mdc-select--outlined approvalSelect']/input/following-sibling::i/following-sibling::div[@class='mdc-select__selected-text']")).getText();


    }

    public String assetsDropdown () {
     return    driver.findElement(By.xpath("//div[@class='mdc-select d-flex mdc-select--outlined asset']/input/following-sibling::i/following-sibling::div[@class='mdc-select__selected-text']")).getText();


    }

        public String emailDropdown (){
            return    driver.findElement(By.xpath("//body/div[2]/main[1]/div[1]/div[2]/div[1]/div[1]/div[1]/main[1]/section[1]/div[1]/div[1]/div[1]/div[2]/div[3]/div[2]/form[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[3]/div[1]")).getText();


    }
    public String OverrideDropdown () {
        return    driver.findElement(By.xpath("//div[@class='mdc-select d-flex mdc-select--outlined approver_override']/input/following-sibling::i/following-sibling::div[@class='mdc-select__selected-text']")).getText();
    }

}
