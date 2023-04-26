package Base;

import Pages.BeSpin;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The CommonFunctions class holds the archetype functions for forms, grids and KPIs.
 *
 * <p>To use the functions from this class in your project, you will need to create a new class with your project name in the <i>Pages</i> package and extend this class, such as was done with the {@link BeSpin YourProject} class.
 */
public class CommonFunctions {
    private final WebDriver driver;
    private final JavascriptExecutor js;
    private final WebDriverWait wait;
    private static final List<File> fileTracker = new ArrayList<>();

    public CommonFunctions(WebDriver driver) {
        this.driver = driver;
        this.js = (JavascriptExecutor) this.driver;
        this.wait = new WebDriverWait(this.driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    public WebDriver getDriver() {
        return driver;
    }

    public JavascriptExecutor getJs() {
        return js;
    }

    public WebDriverWait getWait() {
        return wait;
    }

    public WebElement getGridPageNum() {
        return gridPageNum;
    }

    public WebElement getTotalPages() {
        return totalPages;
    }

    /**
     * Returns the web element of a button given its label.
     *
     * @param button The text of the button as it appears in element inspector
     *               without leading or trailing spaces
     * @return Returns a web element if button was found, otherwise returns null
     */
    public WebElement commonButton(String button) {
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
        return null;
    }

    /**
     * Find a button on the page by the name and clicks it.
     * Uses {@link #commonButtonClick(WebElement) commonButtonClick} to find element.
     *
     * @param button The name of the button as it displays in the element inspector
     *               without leading or trailing spaces
     * @return Returns true if the button exists, otherwise false
     */
    public boolean commonButtonClick(String button) {
        WebElement selectedBtn = commonButton(button);
        if (selectedBtn == null) return false;
        return commonButtonClick(selectedBtn);
    }

    /**
     * Clicks a button given the web element.
     *
     * @param button The web element of the button to be clicked
     * @return Returns true if the element exists, otherwise returns false
     */
    public boolean commonButtonClick(WebElement button) {

        try {
            js.executeScript("arguments[0].click()", button);
        } catch (ElementClickInterceptedException e) {
            clickErrorHandle(e.toString(), button);
        } catch (NoSuchElementException e) {
            return false;
        }
        return true;

    }

    /**
     * Clicks a link based on the link text.
     * Includes a check for {@link org.openqa.selenium.ElementClickInterceptedException #ElementClickInterceptedException}
     *
     * @param linkText The text of the link without leading or trailing spaces
     */
    public boolean commonLinkClick(String linkText) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(By.linkText(linkText))).click();
        } catch (ElementClickInterceptedException e) {
            clickErrorHandle(e.toString(), driver.findElement(By.linkText(linkText)));
        } catch (NoSuchElementException | TimeoutException e) {
            return false;
        }
        return true;
    }
    /**
     * Finds a text field on the page given the label.
     *
     * @param field The label of the field as it shows in the element inspector without leading or trailing spaces
     * @return Returns the web element of the field
     */
    public WebElement commonField(String field) {
        WebElement selectedField = null;
        List<WebElement> fields = driver.findElements(By.xpath(
                "//label[normalize-space(text())='" + field + "']/following-sibling::input"));
        fields.addAll(driver.findElements(By.xpath(
                "//label[translate(.,'\u00A0','') ='" + field + "']/following-sibling::input")));

        fields.addAll(driver.findElements(By.xpath(
                "//label[normalize-space()='" + field + "']/following-sibling::div/input")));

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
     * Enters given text into field selected by web element.
     *
     * @param field The web element of the field
     * @param text  The text to be entered into the field
     * @return
     */
    public boolean commonFieldEnter(WebElement field, String text) {
        try {
            field.click();
        } catch (NoSuchElementException e) {
            return false;
        } catch (ElementClickInterceptedException e) {
            clickErrorHandle(e.toString(), field);
        }
        // This statement checks if the user is using a Mac OS machine or not
        // then sends the appropriate hotkeys for select all to ensure any previous
        // text is deleted when the new text is entered.
        if (System.getProperty("os.name").startsWith("Mac")) {
            field.sendKeys(Keys.chord(Keys.COMMAND, "a"));
        } else {
            field.sendKeys(Keys.chord(Keys.LEFT_CONTROL, "a"));
        }

        field.sendKeys(text);
        return true;
    }

    /**
     * Enters given text into the field selected by label.
     *
     * @param field The label of the field
     * @param text  The text to be entered into the field
     * @return
     */
    public boolean commonFieldEnter(String field, String text) {
        WebElement selectedField = commonField(field);
        if (selectedField == null) return false;
        return commonFieldEnter(selectedField, text);
    }

    /**
     * Returns the current text in the field selected by the web element.
     *
     * @param field The web element of the field to be read
     * @return Returns a String of the text in the field or null if field cannot be found
     */
    public String commonFieldRead(WebElement field) {

        try {
            if (field.getAttribute("value") == null) {
                return field.getText();
            }
            return field.getAttribute("value");
        } catch (NullPointerException e) {
            return null;
        }
    }

    /**
     * Returns the current text in the field selected by the filed label.
     *
     * @param field The label of the field as it displays in the element inspector
     *              without leading and trailing spaces
     * @return Returns a String of the text in the field or null if field cannot be found
     */
    public String commonFieldRead(String field) {
        WebElement selectedField = commonField(field);

        if (selectedField == null) return null;
        return commonFieldRead(selectedField);
    }

    /**
     * Finds and returns the web element selector of a text box located by the label.
     *
     * @param textArea The label of the text box as it shows in the element inspector without leading or trailing spaces
     * @return Returns the web element if one was found, otherwise returns null
     */
    public WebElement commonTextArea(String textArea) {
        WebElement selectedTextArea = null;
        List<WebElement> textAreas = driver.findElements(By.xpath(
                "//label[normalize-space(text())='" + textArea + "']/following-sibling::textarea"));

        if (textAreas.size() > 0) {
            long startTime = System.currentTimeMillis();
            while (selectedTextArea == null && (System.currentTimeMillis() - startTime) < 5000) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ignored) {

                }
                for (WebElement txtA : textAreas) {
                    if (txtA.isDisplayed()) {
                        selectedTextArea = txtA;
                        break;
                    }
                }
            }
        }
        return selectedTextArea;
    }

    /**
     * Enters the given text into a text area input box identified by the web element.
     *
     * @param textArea Web element locator of the text area
     * @param text     Text to be typed into the text box
     * @return Returns true if able to find text box, otherwise returns false
     */
    public boolean commonTextAreaEnter(WebElement textArea, String text) {

        try {
            textArea.click();
        } catch (NoSuchElementException e) {
            return false;
        } catch (ElementClickInterceptedException e) {
            clickErrorHandle(e.toString(), textArea);
        }
        // This statement checks if the user is using a Mac OS machine or not
        // then sends the appropriate hotkeys for select all to ensure any previous
        // text is deleted when the new text is entered.
        if (System.getProperty("os.name").startsWith("Mac")) {
            textArea.sendKeys(Keys.chord(Keys.COMMAND, "a"));
        } else {
            textArea.sendKeys(Keys.chord(Keys.LEFT_CONTROL, "a"));
        }
        textArea.sendKeys(text);
        return true;
    }

    /**
     * Enters the given text into a text area input box identified by the label of the text box.
     *
     * @param textArea The label of the text box as it shows in the element inspector without leading or trailing spaces
     * @param text     Text to be typed into the text box
     * @return Returns true if able to find text box, otherwise returns false
     */
    public boolean commonTextAreaEnter(String textArea, String text) {
        WebElement selectedTextArea = commonTextArea(textArea);
        if (selectedTextArea == null) return false;
        return commonTextAreaEnter(selectedTextArea, text);
    }

    /**
     * Returns the current text in the text box selected by the web element.
     *
     * @param textArea The web element of the text box to be read
     * @return Returns a String of the text in the text box or null if text box cannot be found
     */
    public String commonTextAreaRead(WebElement textArea) {
        try {
            if (textArea.getAttribute("value") == null) {
                return textArea.getText();
            }
            return textArea.getAttribute("value");
        } catch (NullPointerException e) {
            return null;
        }

    }

    /**
     * Returns the current text in the text box selected by the label.
     *
     * @param textArea The label of the text box as it displays in the element inspector
     *                 without leading and trailing spaces
     * @return Returns a String of the text that displays in the text box or null if element could not be found
     */
    public String commonTextAreaRead(String textArea) {
        WebElement selectedTextArea = commonTextArea(textArea);
        if (selectedTextArea == null) return null;
        return commonTextAreaRead(selectedTextArea);

    }

    /**
     * Locates a drop down by the label and returns the web element locator for it.
     *
     * @param dropDown The label of the drop down as it displays in the element inspector
     *                 without leading or trailing spaces
     * @return Returns the web element locator if any were found, otherwise returns null
     */
    public WebElement commonDropDown(String dropDown) {
        WebElement selectedList = null;
        // This list is to compensate for the Time Tracking pop-up in Work Order Tracking
        List<WebElement> lists = driver.findElements(By.xpath(
                "//label[normalize-space()='" + dropDown + "']/following-sibling::div/select"));
        // This list will find the drop downs in the rest of the apps

        lists.addAll(driver.findElements(By.xpath(
                "//label[text()[normalize-space()='" + dropDown + "']]/following-sibling::select")));

        // following xpath used to find google apps dropdown on homepage
        lists.addAll(driver.findElements(By.xpath("//i[@class='mdc-icon-button material-icons mdc-top-app-bar__navigation-icon m-none mdc-ripple-upgraded mdc-ripple-upgraded--unbounded'][normalize-space()='"+ dropDown+"']")));

        if (lists.size() > 0) {
            long startTime = System.currentTimeMillis();
            while (selectedList == null && (System.currentTimeMillis() - startTime) < 5000) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ignored) {

                }
                for (WebElement lst : lists) {
                    if (lst.isDisplayed()) {
                        selectedList = lst;
                        break;
                    }
                }
            }
        }
        return selectedList;
    }

    /**
     * Finds a drop down by its web element and sets it to the given selection.
     *
     * @param dropDown  The web element locator of the drop down
     * @param selection The selection to be picked from the drop down
     * @return Returns true if drop down and selection are found, otherwise returns false
     */
    public boolean commonDropDownSelect(WebElement dropDown, String selection) {

        try {
            Select list = new Select(dropDown);
            list.selectByVisibleText(selection);
        } catch (org.openqa.selenium.NoSuchElementException e) {
            return false;
        }

        return true;
    }

    /**
     * Finds a drop down by its label and sets it to the given selection.
     *
     * @param dropDown  The label of the drop down as it displays in the element inspector
     *                  without leading or trailing spaces
     * @param selection The selection to be picked from the drop down
     * @return Returns true if drop down and selection are found, otherwise returns false
     */
    public boolean commonDropDownSelect(String dropDown, String selection) {
        WebElement selectedList = commonDropDown(dropDown);
        if (selectedList == null) return false;
        return commonDropDownSelect(selectedList, selection);


    }

    /**
     * Returns the current text in the drop down selected by the web element.
     *
     * @param dropDown The web element selector of the drop down
     * @return Returns a String of the text that displays in the drop down or null if element could not be found
     */
    public String commonDropDownRead(WebElement dropDown) {

        try {
            Select list = new Select(dropDown);
            return list.getFirstSelectedOption().getText();
        } catch (NullPointerException e) {
            return null;
        }

    }

    /**
     * Returns the current text in the drop down selected by the label.
     *
     * @param dropDown The label of the drop down as it displays in the element inspector
     *                 without leading and trailing spaces
     * @return Returns a String of the text that displays in the drop down or null if element could not be found
     */
    public String commonDropDownRead(String dropDown) {
        WebElement selectedList = commonDropDown(dropDown);
        if (selectedList == null) return null;
        return commonDropDownRead(selectedList);

    }


    public WebElement commonDate(String datePicker) {
        WebElement selectedPicker = null;
        List<WebElement> pickers = driver.findElements(By.xpath(
                "//label[normalize-space(text())='" + datePicker + "']/following-sibling::div//span[@class='glyphicon glyphicon-calendar']"));

        if (pickers.size() > 0) {
            long startTime = System.currentTimeMillis();
            while (selectedPicker == null && (System.currentTimeMillis() - startTime) < 5000) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ignored) {

                }
                for (WebElement pkr : pickers) {
                    if (pkr.isDisplayed()) {
                        selectedPicker = pkr;
                        break;
                    }
                }
            }
        }
        return selectedPicker;
    }

    /**
     * Sets the date for date pickers. Use this for date pickers that do not need time to be set.
     * For pickers that need time set use {@link #commonDatePick(String, Map) commonDatePick}
     *
     * @param datePicker Name of the date picker to manipulate.
     * @param day        Day of the month.
     * @param month      Full name of the month.
     * @param year       Four-digit year as a String.
     * @return Returns true if process completed with no errors, otherwise returns false.
     */
    public boolean commonDatePick(String datePicker, String day, String month, String year) {
        WebElement selectedPicker = commonDate(datePicker);
        return commonDatePick(selectedPicker, day, month, year);
    }

    /**
     * Sets the date for date pickers without a time component using a WebElement locator.
     * For finding the date picker with the name, use {@link #commonDatePick(String, String, String, String) commonDatePick}
     *
     * @param datePicker WebElement locator of the date picker.
     * @param day        Day of the month.
     * @param month      Full name of the month.
     * @param year       Four-digit year as a String.
     * @return Returns true if process completed with no errors, otherwise returns false.
     */
    public boolean commonDatePick(WebElement datePicker, String day, String month, String year) {
        month = month.substring(0, 1).toUpperCase() + month.substring(1).toLowerCase();
        if (datePicker == null) {
            return false;
        }

        try {
            wait.until(ExpectedConditions.visibilityOf(datePicker)).click();
        } catch (ElementClickInterceptedException e) {
            clickErrorHandle(e.toString(), datePicker);
        }

        BaseUtil.pageLoaded();

        WebElement pickerMonthYear;
        try {
            pickerMonthYear = driver.findElement(By.xpath("//div[@class='datepicker-days']/descendant::th[@class='datepicker-switch']"));
        } catch (NoSuchElementException e) {
            return false;
        }

        WebElement pickerYear = driver.findElement(By.xpath("//div[@class='datepicker-months']/descendant::th[@class='datepicker-switch']"));
        WebElement pickerNext = driver.findElement(By.xpath("//div[@class='datepicker-months']/descendant::th[@class='next']"));
        WebElement pickerPrev = driver.findElement(By.xpath("//div[@class='datepicker-months']/descendant::th[@class='prev']"));

        // Gets the currently displayed month and year from top of datepicker modal
        String currentMonthYear = pickerMonthYear.getText().replaceAll("\\s+", "");
        String curMonth = currentMonthYear.replaceAll("\\d", "");
        String curYear = currentMonthYear.replaceAll("\\D", "");

        // Compare the month and year displayed in the picker modal to the ones selected.
        // If either do not match, then the Month/Year element is clicked.
        if (!month.equalsIgnoreCase(curMonth) || !year.equalsIgnoreCase(curYear)) {
            pickerMonthYear.click();
            wait.until(ExpectedConditions.visibilityOf(pickerYear));
            // If the displayed year doesn't match, paginate until selected year is found.
            if (!pickerYear.getText().equalsIgnoreCase(year)) {
                int selectedYear = Integer.parseInt(year);
                long startTime = System.currentTimeMillis();
                while (!pickerYear.getText().equalsIgnoreCase(year) && (System.currentTimeMillis() - startTime) < 30000) {
                    int displayedYear = Integer.parseInt(pickerYear.getText());
                    if (displayedYear < selectedYear) {
                        pickerNext.click();
                    } else {
                        pickerPrev.click();
                    }
                }

            }
            // Find selected month and click it.
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
                    "//div[@class='datepicker-months']/descendant::span[contains(@class, 'month') " +
                            "and text()='" + month.substring(0, 3) + "']"))).click();


        }
        // Find selected day and click it.
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='datepicker-days']" +
                "/descendant::td[contains(@class, 'day') and not(contains(@class, 'new')) and " +
                "not(contains(@class, 'old'))][text()='" + day + "']"))).click();

        return true;
    }

    /**
     * Sets the date in date pickers that require time be included.
     * For date pickers that don't need time use {@link #commonDatePick(String, String, String, String) commonDatePick}
     *
     * @param datePicker Name of the date picker to be manipulated.
     * @param date       A Map containing date to enter. Checks for the following keys:
     *                   "Year", "Month", "Day", "Time", "AM / PM"
     * @return Returns true if process completed with no errors, otherwise returns false.
     */
    public boolean commonDatePick(String datePicker, Map<String, String> date) {
        WebElement selectedPicker = commonDate(datePicker);
        return commonDatePick(selectedPicker, date);
    }

    /**
     * Sets the date for date pickers that have a time component using a WebElement locator.
     * For finding the date picker with the name use {@link #commonDatePick(String, Map) commonDatePick}
     *
     * @param datePicker WebElement locator of the date picker.
     * @param date       A Map containing the date to enter. Checks for the following keys:
     *                   "Year", "Month", "Day", "Time", "AM / PM"
     * @return Returns true if process completed with no errors, otherwise returns false.
     */
    public boolean commonDatePick(WebElement datePicker, Map<String, String> date) {
        Map<String, String> selections = new HashMap<>(date);
        selections.put("Month", selections.get("Month").substring(0, 1).toUpperCase() + selections.get("Month").substring(1).toLowerCase());
        if (datePicker == null) {
            return false;
        }

        try {
            wait.until(ExpectedConditions.visibilityOf(datePicker)).click();
        } catch (ElementClickInterceptedException e) {
            clickErrorHandle(e.toString(), datePicker);
        }

        BaseUtil.pageLoaded();

        WebElement pickerMonthYear = null;
        List<WebElement> pMY = driver.findElements(By.xpath("//div[@class='datetimepicker-days']/descendant::th[@class='switch']"));
        for (WebElement ele : pMY) {
            if (ele.isDisplayed()) {
                pickerMonthYear = ele;
            }
        }

        String hour = selections.get("Time").replaceAll("(:\\d\\d)", "");
        WebElement pickerYear = null;
        WebElement pickerNext = null;
        WebElement pickerPrev = null;

        assert pickerMonthYear != null;
        // Gets the currently displayed month and year from top of datepicker modal
        String currentMonthYear = pickerMonthYear.getText().replaceAll("\\s+", "");
        String curMonth = currentMonthYear.replaceAll("\\d", "");
        String curYear = currentMonthYear.replaceAll("\\D", "");

        // Compare the month and year displayed in the picker modal to the ones selected.
        // If either do not match, then the Month/Year element is clicked.
        if (!selections.get("Month").equalsIgnoreCase(curMonth) || !selections.get("Year").equalsIgnoreCase(curYear)) {
            pickerMonthYear.click();
            List<WebElement> displayedYear = driver.findElements(By.xpath("//div[@class='datetimepicker-months']/descendant::th[@class='switch']"));
            for (WebElement ele : displayedYear) {
                if (ele.isDisplayed()) {
                    pickerYear = ele;
                }
            }
            List<WebElement> nextButtons = driver.findElements(By.xpath("//div[@class='datetimepicker-months']/descendant::th[@class='next']"));
            for (WebElement ele : nextButtons) {
                if (ele.isDisplayed()) {
                    pickerNext = ele;
                }
            }
            List<WebElement> prevButtons = driver.findElements(By.xpath("//div[@class='datetimepicker-months']/descendant::th[@class='prev']"));
            for (WebElement ele : prevButtons) {
                if (ele.isDisplayed()) {
                    pickerPrev = ele;
                }
            }
            assert pickerYear != null;

            // If the displayed year doesn't match, paginate until selected year is found.
            if (!pickerYear.getText().equalsIgnoreCase(selections.get("Year"))) {
                assert pickerPrev != null;
                assert pickerNext != null;
                int selectedYear = Integer.parseInt(selections.get("Year"));
                long startTime = System.currentTimeMillis();
                while (!pickerYear.getText().equalsIgnoreCase(selections.get("Year")) && (System.currentTimeMillis() - startTime) < 30000) {
                    int pickYr = Integer.parseInt(pickerYear.getText());
                    if (pickYr < selectedYear) {
                        pickerNext.click();
                    } else {
                        pickerPrev.click();
                    }
                }

            }
            List<WebElement> displayedMonths = driver.findElements(By.xpath(
                    "//div[@class='datetimepicker-months']/descendant::span[contains(@class, 'month') " +
                            "and text()='" + selections.get("Month").substring(0, 3) + "']"));
            for (WebElement ele : displayedMonths) {
                if (ele.isDisplayed()) {
                    ele.click();
                    break;
                }
            }

        }
        List<WebElement> displayedDays = driver.findElements(By.xpath("//div[@class='datetimepicker-days']" +
                "/descendant::td[contains(@class, 'day') and not(contains(@class, 'new')) and " +
                "not(contains(@class, 'old'))][text()='" + selections.get("Day") + "']"));
        for (WebElement ele : displayedDays) {
            if (ele.isDisplayed()) {
                ele.click();
            }
        }
        List<WebElement> pH = driver.findElements(By.xpath(
                "//div[@class='datetimepicker-hours']/descendant::legend[normalize-space()='" + selections.get("AM / PM") + "']/following-sibling::span[normalize-space()='" + hour + "']"));
        for (WebElement ele : pH) {
            if (ele.isDisplayed()) {
                ele.click();
                break;
            }
        }
        List<WebElement> pMi = driver.findElements(By.xpath(
                "//div[@class='datetimepicker-minutes']/descendant::legend[normalize-space()='" + selections.get("AM / PM") + "']/following-sibling::span[normalize-space()='" + selections.get("Time") + "']"));
        for (WebElement ele : pMi) {
            if (ele.isDisplayed()) {
                ele.click();
                break;
            }
        }

        return true;
    }


    public String commonDateRead(String datePicker) {
        WebElement selectedPicker = commonDate(datePicker);
        String date = selectedPicker.getText();
        // The returned webelement is the glyphicon-calendar. The contents are in the sibling's <input> element
        if (date.isEmpty()) {
            date = selectedPicker.findElement(By.xpath("./../../input")).getAttribute("value");
        }
        return date;
    }

    /**
     * Sets date to one in the future or past of the current date.
     * Locates date picker by name. To locate date picker by WebElement
     * user {@link #commonDateShift(WebElement, int, String, String, boolean) commonDateShift}
     *
     * @param datePicker Name of the date picker to be manipulated.
     * @param count      Number of units you want to shift the date by.
     * @param units      Type of units used to move date. Accepts "days", "months", and "years".
     * @param direction  Which way to move the date. Accepts "past" and "future".
     * @param hasTime    If the date picker needs to have time set, use true. Otherwise use false.
     * @return Returns true if process completed with no errors, otherwise returns false.
     */
    public boolean commonDateShift(String datePicker, int count, String units, String direction, boolean hasTime) {
        WebElement selectedPicker = commonDate(datePicker);
        if (hasTime) return commonDateShift(selectedPicker, count, units, direction, true);

        return commonDateShift(selectedPicker, count, units, direction, false);

    }

    /**
     * Sets date to one in the future or past of the current date.
     * Uses WebElement locator to find date picker. To find picker by name
     * use {@link #commonDateShift(String, int, String, String, boolean) commonDateShift}
     *
     * @param datePicker WebElement locator of the date picker to be manipulated.
     * @param count      Number of units you want to shift the date by.
     * @param units      Type of units used to move date. Accepts "days", "months", and "years".
     * @param direction  Which way to move the date. Accepts "past" and "future".
     * @param hasTime    If the date picker needs to have time set, use true. Otherwise use false.
     * @return Returns true if process completed with no errors, otherwise returns false.
     */
    public boolean commonDateShift(WebElement datePicker, int count, String units, String direction, boolean hasTime) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("d/MMMM/yyyy");
        LocalDateTime now = LocalDateTime.now();
        HashMap<String, Integer> period = new HashMap<>();
        period.put("years", 0);
        period.put("months", 0);
        period.put("days", 0);
        period.put(units.toLowerCase(), count);
        LocalDateTime adjust;
        if (direction.equalsIgnoreCase("past")) {
            adjust = now.minus(Period.of(period.get("years"), period.get("months"), period.get("days")));
        } else {
            adjust = now.plus(Period.of(period.get("years"), period.get("months"), period.get("days")));
        }
        String[] dateArr = adjust.format(format).split("/");
        Map<String, String> dateMap = new HashMap<>();
        dateMap.put("Day", dateArr[0]);
        dateMap.put("Month", dateArr[1]);
        dateMap.put("Year", dateArr[2]);
        dateMap.put("Time", "12:00");
        dateMap.put("AM / PM", "PM");
        if (hasTime) return commonDatePick(datePicker, dateMap);

        return commonDatePick(datePicker, dateMap.get("Day"), dateMap.get("Month"), dateMap.get("Year"));

    }


    public WebElement commonCheckBox(String checkBox) {
        List<WebElement> checkBoxes = driver.findElements(By.xpath(
                "//label[normalize-space(text())='" + checkBox + "']/input[@type='checkbox' and @class='badgebox']"));
        checkBoxes.addAll(driver.findElements(By.xpath(
                "//label[normalize-space(text())='" + checkBox + "']/following-sibling::*//input[@type='checkbox']")));
        checkBoxes.addAll(driver.findElements(By.xpath(
                "//label[normalize-space()='" + checkBox + "']/input[@type='checkbox']")));
        checkBoxes.addAll(driver.findElements(By.xpath("//label[text()='"+ checkBox +"']/preceding-sibling::div/input")));

        if (checkBoxes.size() > 0) {
            for (WebElement chkbx : checkBoxes) {
                if (chkbx.isEnabled()) {
                    return chkbx;
                }
            }
        }
        return null;
    }


    public boolean commonCheckBoxClick(String checkBox) {
        WebElement chkbx = commonCheckBox(checkBox);
        if (chkbx != null) {
            js.executeScript("arguments[0].click()", chkbx);
            return true;
        }
        return false;
    }


    public boolean commonCheckBoxSelected(String checkBox) {
        WebElement chkbx = commonCheckBox(checkBox);
        if (chkbx != null) {
            return chkbx.isSelected();
        }
        System.out.println("Could not find checkbox: " + checkBox);
        return false;
    }

    /**
     * Navigates to the indicated grid tab
     *
     * @param tabName The name of the tab as it displays on the page
     * @return Returns <code>true</code> if tab could be successfully navigated to, otherwise returns <code>false</code>
     */
    public boolean gridTab(String tabName) {
        BaseUtil.pageLoaded();
        // Find element with partial id match (id includes "tab")
        List<WebElement> tabs = driver.findElements(By.xpath("//li[contains(@id, \"tab\")]/a[normalize-space()=\"" + tabName + "\"]"));
        WebElement tabToClick = null;
        // Or maybe just click a link
        // WebElement tab = driver.findElement(By.linkText(tabName));
        for (WebElement tab : tabs) {
            if (tab.isDisplayed()) tabToClick = tab;
        }

        if (tabToClick == null) return false;

        try {
            tabToClick.click();
        } catch (ElementClickInterceptedException e) {
            clickErrorHandle(e.toString(), tabToClick);
        }
        BaseUtil.pageLoaded();
        return true;

    }

    WebElement gridPageNum;
    WebElement totalPages;

    /**
     * Returns either the number of pages in a specific grid, or what current page of
     * the grid is currently being displayed
     * <p>
     * //@param tabName The name of the tab as it displays on the page
     *
     * @param result Can be "current" or "total" depending on what return is expected
     * @return Returns current or total pages if successful, 0 if the grid is empty, and -1 if elements cannot be found
     */
//    public int gridPageNumber(String result) {
//        List<WebElement> currentPages = driver.findElements(By.className("pagination-panel-input"));
//        List<WebElement> totalPages = driver.findElements(By.className("pagination-panel-total"));
//
//        if (currentPages.isEmpty() && totalPages.isEmpty()) {
//            return -1;
//        }
//
//        WebElement totalPage = null;
//
//        for (WebElement checkElement : currentPages) {
//            if (checkElement.isDisplayed()) {
//                gridPageNum = checkElement;
//            }
//        }
//
//        for (WebElement checkElement : totalPages) {
//            if (checkElement.isDisplayed()) {
//                totalPage = checkElement;
//            }
//        }
//
//
//        String pageNum = gridPageNum.getAttribute("value");
//        if (result.equals("current")) {
//            return Integer.parseInt(pageNum);
//        } else if (result.equals("total")) {
//            return Integer.parseInt(totalPage.getText());
//        }
//
//        return 0;
//    }
    public int gridPageNumber(String result) {
        WebElement gridPaginate = getGridId("paginate");

        if (gridPaginate.getAttribute("style").equals("display: none;")) {
            return 0;
        }

        try {
            if (result.equals("current"))
                return Integer.parseInt(gridPaginate.findElement(By.xpath("descendant::input[contains(@class,'pagination-panel-input')]")).getAttribute("value"));

            if (result.equals("total"))
                return Integer.parseInt(gridPaginate.findElement(By.xpath("descendant::span[@class='pagination-panel-total']")).getText());
        } catch (NoSuchElementException ignored) {
        }
        return -1;
    }

    /**
     * Navigates to a random page in the current grid tab.
     * Requires there be a field to enter numbers in the grid pagination section to work
     *
     * @return returns the page number from the randomly selected page as an int
     */
    public int gridRandomPage() {
        int totalPages = gridPageNumber("total");
        WebElement pageNum = getGridId("paginate")
                .findElement(By.className("pagination-panel-input"));

        if (pageNum == null) return -1;

        if (totalPages == 1) {
            return totalPages;
        } else {
            int randomPage = (int) (Math.random() * (totalPages - 1)) + 1;
            pageNum.click();
            // This statement checks if the user is using a Mac OS machine or not
            // then sends the appropriate hotkeys for select all to ensure any previous
            // text is deleted when the new text is entered.
            if (System.getProperty("os.name").startsWith("Mac")) {
                pageNum.sendKeys(Keys.chord(Keys.COMMAND, "a"));
            } else {
                pageNum.sendKeys(Keys.chord(Keys.LEFT_CONTROL, "a"));
            }
            pageNum.sendKeys(Integer.toString(randomPage));
            pageNum.sendKeys(Keys.ENTER);
            return gridPageNumber("current");
        }

    }

    /**
     * Sets the page in the requested grid to the requested number
     *
     * @param pageNum The page number to be navigated to
     * @return Returns the new page number if it was successful, -1 if the requested page does not exist, 0 if the current page is the same as the requested page
     */
    public int gridSetPage(int pageNum) {
        int totalPages = gridPageNumber("total");
        int currPage = gridPageNumber("current");
        WebElement numField = getGridId("paginate").findElement(By.xpath("descendant::input[contains(@class,'pagination-panel-input')]"));
        if (totalPages < pageNum) return -1;
        if (currPage == pageNum) return 0;
        numField.click();
        // This statement checks if the user is using a Mac OS machine or not
        // then sends the appropriate hotkeys for select all to ensure any previous
        // text is deleted when the new text is entered.
        if (System.getProperty("os.name").startsWith("Mac")) {
            numField.sendKeys(Keys.chord(Keys.COMMAND, "a"));
        } else {
            numField.sendKeys(Keys.chord(Keys.LEFT_CONTROL, "a"));
        }
        numField.sendKeys(Integer.toString(pageNum));
        numField.sendKeys(Keys.ENTER);
        BaseUtil.pageLoaded();
        return gridPageNumber("current");
    }

    /**
     * Navigates to the next page of the current grid tab if it exists
     *
     * @return Returns <code>true</code> if it is possible to navigate to the next page, otherwise returns <code>false</code>
     */
    public boolean gridNextPage() {
        WebElement nextBtn = getGridId("paginate")
                .findElement(By.xpath("descendant::*[contains(@class, 'next')]"));
        if (nextBtn.getAttribute("class").contains("disabled")) return false;
        if (nextBtn.findElements(By.tagName("a")).isEmpty()) {
            nextBtn.click(); // Purchase Order, DBR, Loss Run, Submissions
        } else {
            nextBtn.findElement(By.tagName("a")).click(); // Policy Checking, Certificate Issuance
        }
        return true;
    }

    /**
     * Navigates to the previous page of the current grid tab if it exists
     *
     * @return Returns <code>true</code> if it is possible to navigate to the previous page, otherwise returns <code>false</code>
     */
    public boolean gridPrevPage() {
        WebElement prevBtn = getGridId("paginate")
                .findElement(By.xpath("descendant::*[contains(@class, 'prev')]"));
        if (prevBtn.getAttribute("class").contains("disabled")) return false;
        if (prevBtn.findElements(By.tagName("a")).isEmpty()) {
            prevBtn.click(); // Purchase Order, DBR, Loss Run, Submissions
        } else {
            prevBtn.findElement(By.tagName("a")).click(); // Policy Checking, Certificate Issuance
        }
        return true;
    }

    /**
     * Selects an option from the "Viewing" drop down of the grid header that changes the number of rows visible
     * for the current grid tab
     *
     * @param option Number of rows to display
     */
    public void gridViewSelection(String option) {
        System.out.println("Selecting " + option + " from Viewing dropdown");
        WebElement dropDown = getGridId("length").findElement(By.xpath("descendant::select"));
        Select rows = new Select(dropDown);
        dropDown.click();
        rows.selectByVisibleText(option);
    }

    /**
     * Returns the count of total records in the current grid tab
     *
     * @return Returns the total number of records as an int
     */
    public int gridRecordNumber() {

        String records = getGridId("info").getText();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < records.length(); i++) {
            if (Character.isDigit(records.charAt(i))) {
                stringBuilder.append(records.charAt(i));
            }
        }
        if (stringBuilder.length() == 0) {
            return 0;
        }

        return Integer.parseInt(stringBuilder.toString());
    }

    /**
     * Returns the number of rows being displayed in the current grid tab
     *
     * @return The number of visible rows as an int
     */
    public int gridRowCount() {
        List<WebElement> rows = getGridId("table").findElements(By.xpath("tbody/tr"));
        return rows.size();
    }

    /**
     * Gets the table element for the currently visible grid on the page.
     *
     * @return Returns WebElement with the ID of the currently visible grid
     */
    public WebElement getGridId(String type) {
        List<WebElement> grids;
        if (type.equalsIgnoreCase("table")) {
            grids = driver.findElements(By.xpath("//tbody[@id]"));
        } else {
            grids = driver.findElements(By.xpath("//div[contains(@id, '" + type.toLowerCase() + "')]"));
        }

        assert grids != null;
        for (WebElement tblEle : grids) {
            if (tblEle.isDisplayed() && tblEle.getAttribute("id").length() > 0) {
                return tblEle;
            }
        }
        return null;
    }

    /**
     * Will search the page for the specified grid header
     *
     * @param headerName The name of the header to be located
     * @return Returns <code>true</code> if header is found in grid, otherwise returns <code>false</code>
     */
    public boolean gridHeaderFind(String headerName) {
        List<WebElement> headers = driver.findElements(By.xpath("//th[contains(@aria-label, \"" + headerName + "\")]"));
        for (WebElement header : headers) {
            if (header.isDisplayed()) return true;
        }
        return false;
    }

    /**
     * Sort the column on the grid
     *
     * @param headerName <code>String</code> Name of the grid header to be sorted
     * @param sort       <code>String</code> Choose whether to sort by Ascending or Descending
     * @return <code>boolean</code> Returns true if sorting was successful, otherwise returns false
     */
    public boolean gridHeaderSort(String headerName, String sort) {
        String option = sort.equalsIgnoreCase("ascending") ? "filterHeader sorting_asc" : "filterHeader sorting_desc";
        WebElement selectedHeader = null;
        List<WebElement> headers = driver.findElements(By.xpath("" +
                "//*[contains(@class, 'filterHeader') and normalize-space()='" + headerName + "']"));

        if (headers.size() > 0) {
            long startTime = System.currentTimeMillis();
            while (selectedHeader == null && (System.currentTimeMillis() - startTime) < 5000) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ignored) {

                }
                for (WebElement hdr : headers) {
                    if (hdr.isDisplayed()) {
                        selectedHeader = hdr;
                        break;
                    }
                }
            }
        }

        if (selectedHeader == null) {
            return false;
        }
        String classInfo;
        selectedHeader.click();
        BaseUtil.pageLoaded();
        classInfo = selectedHeader.getAttribute("class");
        if (!classInfo.equals(option)) {
            selectedHeader.click();
            BaseUtil.pageLoaded();
            classInfo = selectedHeader.getAttribute("class");
            if (!classInfo.equals(option)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Finds and returns the web element locator for a text entry grid header by its name.
     *
     * @param headerName The name of the header as it displays on the page
     * @return Returns a <code>WebElement</code> for the grid header
     */
    public WebElement gridHeaderField(String headerName) {
        WebElement selectedHeader = null;
        List<WebElement> headers = driver.findElements(By.xpath("" +
                "//*[contains(@class, 'filterHeader') and normalize-space()='" + headerName + "']/../descendant::input"));

        if (headers.size() > 0) {
            long startTime = System.currentTimeMillis();
            while (selectedHeader == null && (System.currentTimeMillis() - startTime) < 5000) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ignored) {

                }
                for (WebElement hdr : headers) {
                    if (hdr.isDisplayed()) {
                        selectedHeader = hdr;
                        break;
                    }
                }
            }
        }
        return selectedHeader;
    }

    /**
     * Writes the given text into the text entry field of the specified grid header.
     *
     * @param headerName The name of the header as it displays on the page
     * @param input      A <code>String</code> of the text to be written into the grid header
     * @return Returns <code>true</code> if operation finished with no errors, otherwise returns <code>false</code>
     */
    public boolean gridHeaderFieldWrite(String headerName, String input) {
        WebElement selectedHeader = gridHeaderField(headerName);

        if (selectedHeader == null) {
            return false;
        }
        try {
            selectedHeader.click();

        } catch (ElementClickInterceptedException e) {
            clickErrorHandle(e.toString(), selectedHeader);
        }

        // This statement checks if the user is using a Mac OS machine or not
        // then sends the appropriate hotkeys for select all to ensure any previous
        // text is deleted when the new text is entered.
        if (System.getProperty("os.name").startsWith("Mac")) {
            selectedHeader.sendKeys(Keys.chord(Keys.COMMAND, "a"));
        } else {
            selectedHeader.sendKeys(Keys.chord(Keys.LEFT_CONTROL, "a"));
        }
        selectedHeader.sendKeys(input);
        selectedHeader.click();
        return true;
    }

    /**
     * Returns the text displayed in the text entry field of the specified grid header.
     *
     * @param headerName The name of the header as it displays on the page
     * @return Returns a <code>String</code> of the found text
     */
    public String gridHeaderFieldRead(String headerName) {
        WebElement selectedHeader = gridHeaderField(headerName);
        assert selectedHeader != null;
        if (selectedHeader.getAttribute("value") == null) {
            return selectedHeader.getText();
        }
        return selectedHeader.getAttribute("value");
    }

    /**
     * Finds the <code>WebElement</code> of the requested selector type header.
     *
     * @param headerName Name of the header as it displays on the page
     * @return Returns the selector's <code>WebElement</code>
     */
    public WebElement gridHeaderSelector(String headerName) {
        WebElement selectedHeader = null;
        List<WebElement> headers = driver.findElements(By.xpath("//*[contains(@class, 'filterHeader') and normalize-space()='" + headerName + "']/..//button"));

        if (headers.size() > 0) {
            long startTime = System.currentTimeMillis();
            while (selectedHeader == null && (System.currentTimeMillis() - startTime) < 5000) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ignored) {

                }
                for (WebElement hdr : headers) {
                    if (hdr.isDisplayed()) {
                        selectedHeader = hdr;
                        break;

                    }
                }
            }
        }

        return selectedHeader;
    }

    /**
     * Selects an option from a selector (drop down) grid header.
     *
     * @param headerName Name of the header as it displays on the page
     * @param selection  Name of the selection desired
     * @return Returns <code>true</code> if selection was possible, otherwise returns <code>false</code>
     */
    public boolean gridHeaderSelectorSelect(String headerName, String selection) {
        WebElement selectedHeader = gridHeaderSelector(headerName);

        if (selectedHeader == null) {
            return false;
        }

        try {
            selectedHeader.click();

        } catch (ElementClickInterceptedException e) {
            clickErrorHandle(e.toString(), selectedHeader);
        }
        try {
            selectedHeader.findElement(By.xpath("following-sibling::ul/li/descendant::input[@value='" + selection + "'] ")).click();
        } catch (NoSuchElementException e) {
            return false;
        }

        selectedHeader.click();

        return true;

    }

    /**
     * Returns currently selected items from a selector (drop down) type header in the grid.
     *
     * @param headerName Header name as it displays on the page
     * @return Returns an ArrayList of Strings containing all of the current selections
     */
    public ArrayList<String> gridHeaderSelectorRead(String headerName) {
        ArrayList<String> selections = new ArrayList<>();
        WebElement selectedHeader = gridHeaderSelector(headerName);
        if (selectedHeader == null) {
            return null;
        }
        selectedHeader.click();
        wait.until(ExpectedConditions.visibilityOf(selectedHeader.findElement(By.xpath("following-sibling::ul"))));
        List<WebElement> active = selectedHeader.findElements(By.xpath("following::li[@class='active']"));
        if (active.size() > 0) {
            for (WebElement element : active) {
                selections.add(element.getText().trim());
            }
        } else {
            selectedHeader.click();
            return null;
        }

        selectedHeader.click();
        return selections;
    }

    /**
     * Gets the <code>WebElement</code> of the specified date picker type grid header.
     *
     * @param header Header name as it displays on the page
     * @param fromTo The "from" or "to" date picker for that header
     * @return Returns the <code>WebElement</code> of either the "from" or "to" picker
     */
    public WebElement gridHeaderDate(String header, String fromTo) {
        WebElement selectedHeader = null;
//        List<WebElement> headers = driver.findElements(By.xpath(
//                "//th[normalize-space(@aria-label)='"+header+"']/descendant::input[contains(@placeholder, '"+fromTo+"')]"));
        List<WebElement> headers = driver.findElements(By.xpath(
                "//th[contains(@aria-label, '" + header + "')]/descendant::input[contains(@placeholder, '" + fromTo + "')]"));

        if (headers.size() > 0) {
            long startTime = System.currentTimeMillis();
            while (selectedHeader == null && (System.currentTimeMillis() - startTime) < 5000) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ignored) {

                }
                for (WebElement hdr : headers) {
                    if (hdr.isDisplayed()) {
                        selectedHeader = hdr;
                        break;
                    }
                }
            }
        } else {
            return null;
        }
        return selectedHeader;
    }

    /**
     * Sets the date for either the "from" or "to" option of a grid header date picker.
     * Use this option if the picker does not have a time selection.
     *
     * @param header Name of the header as it displays on the page
     * @param fromTo Either "from" or "to" depending on which option is to be used
     * @param day    The day without any leading zero (e.g., "5" not "05")
     * @param month  The full text name of the month with proper capitalization (e.g., "August")
     * @param year   The four-digit year (e.g., "2021")
     * @return Returns <code>true</code> if picking the date was successful, otherwise returns <code>false</code>
     */
    public boolean gridHeaderDateSelect(String header, String fromTo, String day, String month, String year) {
        WebElement selectedHeader = gridHeaderDate(header, fromTo);

        if (selectedHeader == null) {
            return false;
        }

        try {
            wait.until(ExpectedConditions.visibilityOf(selectedHeader)).click();
        } catch (ElementClickInterceptedException e) {
            clickErrorHandle(e.toString(), selectedHeader);
        }

        BaseUtil.pageLoaded();

        WebElement pickerMonthYear = driver.findElement(By.xpath("//div[@class='datepicker-days']/descendant::th[@class='datepicker-switch']"));
        WebElement pickerYear = driver.findElement(By.xpath("//div[@class='datepicker-months']/descendant::th[@class='datepicker-switch']"));
        WebElement pickerNext = driver.findElement(By.xpath("//div[@class='datepicker-months']/descendant::th[@class='next']"));
        WebElement pickerPrev = driver.findElement(By.xpath("//div[@class='datepicker-months']/descendant::th[@class='prev']"));

        String curMY = pickerMonthYear.getText().replaceAll("\\s+", "");
        String curMonth = curMY.replaceAll("\\d", "");
        String curYear = curMY.replaceAll("\\D", "");

        if (!month.equalsIgnoreCase(curMonth) || !year.equalsIgnoreCase(curYear)) {
            pickerMonthYear.click();
            wait.until(ExpectedConditions.visibilityOf(pickerYear));
            if (!pickerYear.getText().equalsIgnoreCase(year)) {
                int myYr = Integer.parseInt(year);
                long startTime = System.currentTimeMillis();
                while (!pickerYear.getText().equalsIgnoreCase(year) && (System.currentTimeMillis() - startTime) < 30000) {
                    int pickYr = Integer.parseInt(pickerYear.getText());
                    if (pickYr < myYr) {
                        pickerNext.click();
                    } else {
                        pickerPrev.click();
                    }
                }

            }
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
                    "//div[@class='datepicker-months']/descendant::span[contains(@class, 'month') " +
                            "and text()='" + month.substring(0, 3) + "']"))).click();


        }
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='datepicker-days']" +
                "/descendant::td[contains(@class, 'day') and not(contains(@class, 'new')) and " +
                "not(contains(@class, 'old'))][text()='" + day + "']"))).click();

        return true;

    }

    /**
     * Sets the date for either the "from" or "to" option of a grid header date picker.
     * Use this option if the date picker requires time to be selected.
     *
     * @param header     Name of the header as it displays on the page
     * @param selections A Map of Strings containing selections for
     *                   "Day", "Month", "Year", "Time", "AM / PM", and "From / To"
     * @return Returns <code>true</code> if date selection successful, otherwise returns <code>false</code>
     */
    public boolean gridHeaderDateSelect(String header, Map<String, String> selections) {
        WebElement selectedHeader = gridHeaderDate(header, selections.get("From / To"));

        if (selectedHeader == null) {
            return false;
        }

        try {
            wait.until(ExpectedConditions.visibilityOf(selectedHeader)).click();
        } catch (ElementClickInterceptedException e) {
            clickErrorHandle(e.toString(), selectedHeader);
        }

        BaseUtil.pageLoaded();

        WebElement pickerMonthYear = null;
        List<WebElement> pMY = driver.findElements(By.xpath("//div[@class='datetimepicker-days']/descendant::th[@class='switch']"));
        for (WebElement ele : pMY) {
            if (ele.isDisplayed()) {
                pickerMonthYear = ele;
            }
        }
        String hour = selections.get("Time").replaceAll("(:\\d\\d)", "");
        WebElement pickerYear = null;
        WebElement pickerNext = null;
        WebElement pickerPrev = null;

        assert pickerMonthYear != null;
        String curMY = pickerMonthYear.getText().replaceAll("\\s+", "");
        String curMonth = curMY.replaceAll("\\d", "");
        String curYear = curMY.replaceAll("\\D", "");

        if (!selections.get("Month").equalsIgnoreCase(curMonth) || !selections.get("Year").equalsIgnoreCase(curYear)) {
            pickerMonthYear.click();
            List<WebElement> pY = driver.findElements(By.xpath("//div[@class='datetimepicker-months']/descendant::th[@class='switch']"));
            for (WebElement ele : pY) {
                if (ele.isDisplayed()) {
                    pickerYear = ele;
                }
            }
            List<WebElement> pN = driver.findElements(By.xpath("//div[@class='datetimepicker-months']/descendant::th[@class='next']"));
            for (WebElement ele : pN) {
                if (ele.isDisplayed()) {
                    pickerNext = ele;
                }
            }
            List<WebElement> pP = driver.findElements(By.xpath("//div[@class='datetimepicker-months']/descendant::th[@class='prev']"));
            for (WebElement ele : pP) {
                if (ele.isDisplayed()) {
                    pickerPrev = ele;
                }
            }
            assert pickerYear != null;
            assert pickerPrev != null;
            assert pickerNext != null;
            if (!pickerYear.getText().equalsIgnoreCase(selections.get("Year"))) {
                int myYr = Integer.parseInt(selections.get("Year"));
                long startTime = System.currentTimeMillis();
                while (!pickerYear.getText().equalsIgnoreCase(selections.get("Year")) && (System.currentTimeMillis() - startTime) < 30000) {
                    int pickYr = Integer.parseInt(pickerYear.getText());
                    if (pickYr < myYr) {
                        pickerNext.click();
                    } else {
                        pickerPrev.click();
                    }
                }

            }
            List<WebElement> pM = driver.findElements(By.xpath(
                    "//div[@class='datetimepicker-months']/descendant::span[contains(@class, 'month') " +
                            "and text()='" + selections.get("Month").substring(0, 3) + "']"));
            for (WebElement ele : pM) {
                if (ele.isDisplayed()) {
                    ele.click();
                    break;
                }
            }

        }
        List<WebElement> pD = driver.findElements(By.xpath("//div[@class='datetimepicker-days']" +
                "/descendant::td[contains(@class, 'day') and not(contains(@class, 'new')) and " +
                "not(contains(@class, 'old'))][text()='" + selections.get("Day") + "']"));
        for (WebElement ele : pD) {
            if (ele.isDisplayed()) {
                ele.click();
            }
        }
        List<WebElement> pH = driver.findElements(By.xpath(
                "//div[@class='datetimepicker-hours']/descendant::legend[normalize-space()='" + selections.get("AM / PM") + "']/following-sibling::span[normalize-space()='" + hour + "']"));
        for (WebElement ele : pH) {
            if (ele.isDisplayed()) {
                ele.click();
                break;
            }
        }
        List<WebElement> pMi = driver.findElements(By.xpath(
                "//div[@class='datetimepicker-minutes']/descendant::legend[normalize-space()='" + selections.get("AM / PM") + "']/following-sibling::span[normalize-space()='" + selections.get("Time") + "']"));
        for (WebElement ele : pMi) {
            if (ele.isDisplayed()) {
                ele.click();
                break;
            }
        }

        return true;

    }

    /**
     * Finds and returns the WebElement of the grid cell that matches the requested column and row.
     *
     * @param tableRow    This is the row of the grid being requested. Set this to either the text in the first cell of the row
     *                    being requested (such as the work order number in purchase orders)
     *                    or the number of the row (such as "row 3")
     * @param tableColumn This is the column of the grid being requested. Set this to the header name for the column
     * @return Returns the WebElement of the matching column and row cell of the grid.
     */
    public WebElement pgridEntry(String tableRow, String tableColumn) {
        WebElement table = getGridId("table");
        WebElement column = null;
        WebElement row = null;
        String rowPath;
        String colPath;
        String title = table.getAttribute("id");

        assert table != null;
        column = table.findElement(By.xpath("//thead/descendant::*[normalize-space()='" + tableColumn + "']"));


        assert column != null;
        colPath = generateXPATH(column, "");
        Pattern colP = Pattern.compile("(?<=th\\[)(\\d+)(?=\\])");
        assert colPath != null;
        Matcher colM = colP.matcher(colPath);
        while (colM.find()) {
            colPath = colM.group();
        }

        if (tableRow.toLowerCase().contains("row")) {
            rowPath = tableRow.replaceAll("\\D", "");
        } else {
            row = table.findElement(By.xpath("tbody/descendant::td[normalize-space()='" + tableColumn + "']"));

            assert row != null;
            rowPath = generateXPATH(row, "");
            Pattern rowP = Pattern.compile("(?<=tr\\[)(\\d+)(?=\\])");
            assert rowPath != null;
            Matcher rowM = rowP.matcher(rowPath);
            while (rowM.find()) {
                rowPath = rowM.group();
            }
        }

        List<WebElement> results = driver.findElements(By.xpath("" +
                "//*[@id='" + title + "']/tbody/tr[" + rowPath + "]/td[" + colPath + "]"));
        results.addAll(driver.findElements(By.xpath("" +
                "//*[@id='" + title + "']/tr[" + rowPath + "]/td[" + colPath + "]")));

        for (WebElement resElement : results) {
            if (resElement.isDisplayed()) {
                return resElement;
            }
        }

        return null;
    }

    /**
     * <p>Returns the text from the specified cell in a grid.
     * <br>
     * <p>While similar to using <code>{@link #gridEntry(String, String)}.getText()</code>, this will return an empty String instead of causing a {@link NullPointerException} if the cell doesn't exist.
     *
     * @param tableRow    This is the row of the grid being requested. Set this to either the text in the first cell of the row
     *                    being requested (such as the work order number in purchase orders)
     *                    or the number of the row (such as "row 3")
     * @param tableColumn This is the column of the grid being requested. Set this to the header name for the column
     * @return Returns a <code>String</code> of the text in the specified cell, or an empty <code>String</code> if the cell can't be found
     */
    public String gridEntryText(String tableRow, String tableColumn) {
        WebElement element = gridEntry(tableRow, tableColumn);
        if (element == null) return "";
        return element.getText();

    }

    public String generateXPATH(WebElement childElement, String current) {
        String childTag = childElement.getTagName();
        if (childTag.equals("html")) {
            return "/html[1]" + current;
        }
        WebElement parentElement = childElement.findElement(By.xpath(".."));
        List<WebElement> childrenElements = parentElement.findElements(By.xpath("*"));
        int count = 0;
        for (int i = 0; i < childrenElements.size(); i++) {
            WebElement childrenElement = childrenElements.get(i);
            String childrenElementTag = childrenElement.getTagName();
            if (childTag.equals(childrenElementTag)) {
                count++;
            }
            if (childElement.equals(childrenElement)) {
                return generateXPATH(parentElement, "/" + childTag + "[" + count + "]" + current);
            }
        }
        return null;
    }

    public String verifyBackGroundColor() {
        try {
            return getGridId("table").findElement(By.xpath("tbody/tr[1]")).getCssValue("background-color");
        } catch (NoSuchElementException e) {
            return e.toString();
        }

    }

    public boolean clickResetButton() {
        List<WebElement> resetButtons = driver.findElements(By.className("search-clear"));
        for (WebElement button : resetButtons) {
            if (button.isDisplayed()) {
                try {
                    button.click();
                } catch (ElementClickInterceptedException e) {
                    clickErrorHandle(e.toString(), button);
                }
                return true;
            }
        }
        return false;
    }

    public boolean clickExportButton() {
        List<WebElement> exportButtons = driver.findElements(By.className("btnExport"));
        for (WebElement button : exportButtons) {
            if (button.isDisplayed()) {
                try {
                    button.click();
                } catch (ElementClickInterceptedException e) {
                    clickErrorHandle(e.toString(), button);
                }
                return true;
            }
        }
        return false;
    }

    /**
    If a click is attempted, but the element is blocked, this method is called. It will either wait for the blocking
     element to disappear and then re-attempt the click, or it will move to the element if it's blocked by the
     browser window and re-attempt the click.
     **/
    public void clickErrorHandle(String error, WebElement target) {
        String xPath = "";
        String selector = "";
        Pattern element = Pattern.compile("(?<=click: \\<)(.*?)(?=\\s*\\>)");
        Pattern type = Pattern.compile("^\\w+");
        Pattern tag = Pattern.compile("\\w+=+\"(.*?)\"");
        Matcher eleM = element.matcher(error);
        while (eleM.find()) {
            selector = eleM.group();
        }

        Matcher typeM = type.matcher(selector);
        while (typeM.find()) {
            xPath += "//" + typeM.group();
        }

        Matcher tagM = tag.matcher(selector);
        while (tagM.find()) {
            xPath += "[@" + tagM.group() + "]";
        }

        if (!xPath.equals("")) {
            List<WebElement> blockers = driver.findElements(By.xpath(xPath));
            try {
                wait.until(ExpectedConditions.invisibilityOfAllElements(blockers));
            } catch (TimeoutException ignored) {

            }
            target.click();
        } else {
            Actions actions = new Actions(driver);
            actions.moveToElement(target).click().perform();
        }

    }

    public WebElement getModal(String modalName) {
        List<WebElement> modals = driver.findElements(By.className("modal"));
        modals.addAll(driver.findElements(By.className("jconfirm")));
        modals.addAll(driver.findElements(By.className("swal-modal"))); // This is for the Export button modal

        for (WebElement modal : modals) {
            if (modal.isDisplayed()) {
                if (!modal.findElements(By.cssSelector("h3")).isEmpty() &&
                    modal.findElement(By.cssSelector("h3")).getText().contains(modalName))
                    return modal; // Eg. Add Discount modal

                if (!modal.findElements(By.className("jconfirm-title")).isEmpty() &&
                    modal.findElement(By.className("jconfirm-title")).getText().contains(modalName))
                    return modal; // Eg. Confirmation modals

                if (!modal.findElements(By.className("swal-title")).isEmpty() &&
                    modal.findElement(By.className("swal-title")).getText().contains(modalName))
                    return modal; // Eg. Are you sure Export modal

                if (!modal.findElements(By.className("swal-text")).isEmpty() &&
                    modal.findElement(By.className("swal-text")).getText().contains(modalName))
                    return modal; // Eg. File Downloaded Successfully modal

            }
        }
        return null;
    }

    public boolean modalButtonClick(String buttonName, String modalName) {
        List<WebElement> buttons = getModal(modalName).findElements(By.xpath(
                ".//button[normalize-space()='" + buttonName + "']"));
        if (buttons.size() > 0) {
            for (WebElement btn : buttons) {
                if (btn.isDisplayed() && btn.isEnabled()) {
                    btn.click();
                    return true;
                }
            }
        }
        return false;
    }

    // Todo: maybe figure out a way to combine this with commonField().
    public WebElement modalField(String fieldName, String modalName) {
        WebElement selectedField = null;
        // Note that these xpaths start with ".//" because they are starting from a node, not the driver.
        List<WebElement> fields = getModal(modalName).findElements(By.xpath(
                ".//label[normalize-space(text())='" + fieldName + "']/following-sibling::input"));
        fields.addAll(getModal(modalName).findElements(By.xpath(
                ".//label[translate(.,'\u00A0','') ='" + fieldName + "']/following-sibling::input")));

        // List to handle fields in the Time Tracking pop-up
        fields.addAll(getModal(modalName).findElements(By.xpath(
                ".//label[normalize-space()='" + fieldName + "']/following-sibling::div/input")));

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

    public String modalFieldRead(String fieldName, String modalName) {
        WebElement field = modalField(fieldName, modalName);
        try {
            if (field.getAttribute("value") == null) {
                return field.getText();
            }
            return field.getAttribute("value");
        } catch (NullPointerException e) {
            return null;
        }
    }

    // Todo: maybe figure out a way to combine this with commonDropdown().
    public WebElement modalDropdown(String dropdownName, String modalName) {
        WebElement selectedList = null;
        // This list is to compensate for the Time Tracking pop-up in Work Order Tracking
        List<WebElement> lists = getModal(modalName).findElements(By.xpath(
                ".//label[normalize-space()='" + dropdownName + "']/following-sibling::div/select"));
        // This list will find the drop downs in the rest of the apps

        lists.addAll(getModal(modalName).findElements(By.xpath(
                ".//label[text()[normalize-space()='" + dropdownName + "']]/following-sibling::select")));

        if (lists.size() > 0) {
            long startTime = System.currentTimeMillis();
            while (selectedList == null && (System.currentTimeMillis() - startTime) < 5000) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ignored) {

                }
                for (WebElement lst : lists) {
                    if (lst.isDisplayed()) {
                        selectedList = lst;
                        break;
                    }
                }
            }
        }
        return selectedList;
    }

    public String modalDropdownRead(String dropdownName, String modalName) {
        try {
            Select list = new Select(modalDropdown(dropdownName, modalName));
            return list.getFirstSelectedOption().getText();
        } catch (NullPointerException e) {
            return null;
        }
    }

    /**
     * This is used to read the values like "Rush/ Bulk:" at the top of Work Order detail pages.
     *
     * @param labelName
     * @return
     */
    public String commonLabelRead(String labelName) {
        WebElement selectedLabel = null;
        List<WebElement> labels = driver.findElements(By.xpath(
                "//label[normalize-space(text())='" + labelName + "']/following-sibling::div"));
        labels.addAll(driver.findElements(By.xpath(
                "//label[translate(.,'\u00A0','') ='" + labelName + "']/following-sibling::div")));

//        // List to handle fields in the Time Tracking pop-up
//        fields.addAll(driver.findElements(By.xpath(
//                "//label[normalize-space()='" + labelName + "']/following-sibling::div/input")));

        if (labels.size() > 0) {
            long startTime = System.currentTimeMillis();
            while (selectedLabel == null && (System.currentTimeMillis() - startTime) < 5000) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ignored) {
                }
                for (WebElement label : labels) {
                    if (label.isDisplayed()) {
                        return label.getText();
                    }
                }
            }
        }
        return null;
    }

    public int[] getCurrentTimerValue() {
        // This janky method is because someone had to put in a darn flip clock made of multiple elements.
        // Note: this will not work when the hours have three digits (which can happen).
        WebElement clock = getDriver().findElement(By.className("flip-clock-wrapper"));
        List<WebElement> clockComponents = clock.findElements(By.className("flip-clock-active"));
        int hour = Integer.parseInt(clockComponents.get(0).findElement(By.className("up")).getText() +
                clockComponents.get(1).findElement(By.className("up")).getText());
        int minute = Integer.parseInt(clockComponents.get(2).findElement(By.className("up")).getText() +
                clockComponents.get(3).findElement(By.className("up")).getText());
        int second = Integer.parseInt(clockComponents.get(4).findElement(By.className("up")).getText() +
                clockComponents.get(5).findElement(By.className("up")).getText());
        return new int[]{hour, minute, second};
    }

    // This is doubled from the BaseUtil version so it can be used by the second driver in iAdjustTheActiveTimerForThe()
    public boolean pageLoaded() {
        // wait for jQuery to load and catch if jQuery doesn't exist
        ExpectedCondition<Boolean> jQueryLoad = driver -> {
            try {
                return (Boolean) getJs().executeScript("return jQuery.active == 0");
            } catch (JavascriptException e) {
                // no jQuery present
                return true;
            }
        };

        // wait for Javascript to load
        ExpectedCondition<Boolean> jsLoad = driver -> getJs().executeScript("return document.readyState").toString().equals("complete");

        try {
            return getWait().until(jQueryLoad) && getWait().until(jsLoad);
        } catch (TimeoutException e) {
            System.err.println("Timeout exception occurred for pageLoaded");
        }
        return false;

    }

    /**
     * <p>Function to get the absolute path of a file in the attachments folder located at "<code>src/test/resources/attachments</code>" by passing the file name.
     * <p>Input is case-sensitive and only checks if the file contains the given text. Problems may occur if there are multiple files with similar names such as "My_File.pdf", "My_File_Two.pdf" and passing "My_File" as the input. Best practice is to use the full file name with file type suffix.
     *
     * @param name The name of the file as it appears in the folder, filetype suffix optional (e.g., "My_File.pdf" or "My_File")
     * @return A string of the absolute path of the file (e.g., "C:\Users\Tester\BeSpin\src\test\resources\attachments\sir_fluffington.jpg"
     */
    public String attachLocation(String name) {
        File attachments = new File("src/test/resources/attachments");
        if (!attachments.exists()) return "Folder not found at location \"src/test/resources/attachments\"";
        if (attachments.list().length < 1) return "No files in attachment folder";

        for (File file : attachments.listFiles()) {
            if (file.getName().contains(name)) return file.getAbsolutePath();
        }

        return name + " file not found in attachments folder";
    }

    /**
     * Creates a new <code>.txt</code> file in the src/test/resources/attachments/ folder and returns the file path as a <code>String</code>
     *
     * @param name Name of the file to be created using only valid filename characters.
     * @return Returns the file path as a String through {@link #attachLocation(String)}
     */
    public String attachCreate(String name) {

        try {
            File newAttach = new File("src/test/resources/attachments/" + name);
            if (newAttach.createNewFile()) fileTracker.add(newAttach);
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return attachLocation(name);
    }

    public WebElement gridEntry(String s, String colName) {
        return null;
    }

}
