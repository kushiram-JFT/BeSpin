package Base;

import Pages.Login;
import io.cucumber.java.en.Then;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import java.io.File;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class CommonStepFunctions extends BaseUtil {

    public CommonStepFunctions() {
    }

    // FormSteps Begins

    public void iClickTheButton(String button) {
        commonFunctions.pageLoaded();
        Assert.assertTrue(commonFunctions.commonButtonClick(button), button + " button could not be found on page");
        commonFunctions.pageLoaded();
        System.out.println("Clicking " + button + " button.");
    }


    public void iSelectFromTheDropDown(String selection, String dropDown) {
        BaseUtil.pageLoaded();
        System.out.println("Selecting " + selection + " from " + dropDown + " drop down.");
        Assert.assertTrue(commonFunctions.commonDropDownSelect(dropDown, selection),
                "Could not select " + selection + " from the " + dropDown + " dropdown");

        valueStore.put(dropDown, selection);
    }


    public void iEditTheFollowingDropDowns(List<Map<String, String>> table) {
        Map<String, String> dropDowns = table.get(0);
        dropDowns.forEach((key, value) -> {
            System.out.println("Selecting " + value + " from " + key + " drop down");
            Assert.assertTrue(commonFunctions.commonDropDownSelect(key, value),
                    "Could not select " + value + " from " + key + " drop down");
            editedValues.put(key, value);
            valueStore.put(key, value);
        });

    }


    public void iGetTheValueForTheDropDown(String dropDown) {
        String value = commonFunctions.commonDropDownRead(dropDown);
        Assert.assertNotNull(value, "Could not read from " + dropDown + " drop down");

        System.out.println("Entry in " + dropDown + " drop down: " + value);
    }


    public void iEnterInTheField(String text, String fieldName) {
        String trackEntry = text;
        String dateString = dateFormat.format(new Date());

        String timeString = timeFormat.format(new Date());

        String random = new SimpleDateFormat("HH:mm:ss").format(new Date());


        if (trackEntry.contains("<current date>")) {
            trackEntry = trackEntry.replaceAll("<current date>", dateString + " " + timeString);
        }
        if (trackEntry.contains("<random>")) {
            trackEntry = trackEntry.replaceAll("<random>", random);

        }
        if (trackEntry.contains("<gmail>")) {
            String email = login.getLogins().getProperty("gmail.email");
            if (trackEntry.contains("+")) {
                String modifier = trackEntry.substring(trackEntry.indexOf('+'));
                String[] eSplit = email.split("@");
                email = eSplit[0] + modifier + "@" + eSplit[1];
            }
            trackEntry = email;
        }

        System.out.println("Entering " + trackEntry + " into " + fieldName + " field.");

        Assert.assertTrue(commonFunctions.commonFieldEnter(fieldName, trackEntry),
                "Field labeled " + fieldName + " is not present on page!");

        valueStore.put(fieldName, trackEntry);
    }


    public void iEditTheFollowingFields(List<Map<String, String>> table) {
        String dateString = dateFormat.format(new Date());
        String timeString = timeFormat.format(new Date());
        String random = new SimpleDateFormat("DDDyyHHmmss").format(new Date());
        Map<String, String> fields = table.get(0);
        fields.forEach((key, value) -> {
            if (value.contains("<current date>")) {
                value = value.replaceAll("<current date>", dateString + " " + timeString);
            }
            if (value.contains("<random>")) {
                value = value.replaceAll("<random>", random);
            }
            if (value.contains("<gmail>")) {
                String email = login.getLogins().getProperty("gmail.email");
                if (value.contains("+")) {
                    String modifier = value.substring(value.indexOf('+'));
                    String[] eSplit = email.split("@");
                    email = eSplit[0] + modifier + "@" + eSplit[1];
                }
                value = email;
            }
            System.out.println("Entering " + value + " into " + key + " field");
            Assert.assertTrue(commonFunctions.commonFieldEnter(key, value), "Could not find " + key + " field!");
            editedValues.put(key, value);
            valueStore.put(key, value);
        });
    }

    public void iClickCheckbox(String checkBox) {
        System.out.println("Clicking " + checkBox + " check box");
        Assert.assertTrue(commonFunctions.commonCheckBoxClick(checkBox), checkBox + " checkbox could not be found!");
    }


    public void iSelectCheckbox(String checkBox) {
        BaseUtil.pageLoaded();
        System.out.println("Selecting the " + checkBox + " checkbox.");
        if (!commonFunctions.commonCheckBoxSelected(checkBox)) {
            commonFunctions.commonCheckBoxClick(checkBox);
            BaseUtil.pageLoaded();
        }
        Assert.assertTrue(commonFunctions.commonCheckBoxSelected(checkBox), checkBox + " checkbox was not selected OR could not be found!");
    }

    // could possibly combine this with the previous stepdef as "I {string} the {string} checkbox"

    public void iDeselectCheckbox(String checkBox) {
        BaseUtil.pageLoaded();
        System.out.println("Selecting the " + checkBox + " checkbox.");
        if (commonFunctions.commonCheckBoxSelected(checkBox)) {
            commonFunctions.commonCheckBoxClick(checkBox);
            BaseUtil.pageLoaded();
        }
        Assert.assertNotNull(commonFunctions.commonCheckBox(checkBox), checkBox + " checkbox could not be found!");
        Assert.assertFalse(commonFunctions.commonCheckBoxSelected(checkBox), checkBox + " checkbox was selected, but was expected to be deselected.");
    }


    public void checkboxSelected(String checkBox, String expectation) {
        // expectation should be "selected" or "deselected" (or "checked" or "unchecked")
        boolean boolExpect = false;
        if (expectation.equalsIgnoreCase("selected") || expectation.equalsIgnoreCase("checked")) {
            boolExpect = true;
        }

        BaseUtil.pageLoaded();
        System.out.println("Checking that the " + checkBox + " checkbox is " + expectation + ".");
        if (commonFunctions.commonCheckBox(checkBox) != null) {
            Assert.assertEquals(commonFunctions.commonCheckBoxSelected(checkBox), boolExpect);
        } else {
            Assert.fail(checkBox + " checkbox could not be found!");
        }
    }

    // GridSteps Begins

    public void iGetTheFor(String colName, String rowName) {
        BaseUtil.pageLoaded();
        System.out.println(colName + " for " + rowName + " is: " + commonFunctions.gridEntry(rowName, colName));
    }

    public void iNavigateToTheTab(String tabName) {

        Assert.assertTrue(commonFunctions.gridTab(tabName), "Unable to navigate to " + tabName + " tab!");
        System.out.println("On " + tabName + " tab");
    }


    public void iSelectTheOptionFromTheTheViewingDropDown(String rowOption) {
        pageLoaded();
        int currentPage = commonFunctions.gridPageNumber("current");
        if (currentPage == 0) {
            System.out.println("Cannot change grid size as there are no entries in the grid");
        } else {
            rowCount = Integer.parseInt(rowOption);
            commonFunctions.gridViewSelection(rowOption);
            pageLoaded();
        }
    }


    public void theNumberOfRowsDisplayedWillBeLessThanOrEqualToTheNumberSelected() {
        int currentPage = commonFunctions.gridPageNumber("current");
        if (currentPage == 0) {
            System.out.println("No rows to count as there are no entries in the grid");
        } else {
            int recordCount = commonFunctions.gridRecordNumber();
            int visibleRows = commonFunctions.gridRowCount();
            Assert.assertTrue(visibleRows == rowCount || visibleRows == recordCount,
                    "Rows displayed (" + visibleRows + ") does not equal selection from Viewing dropdown (" + rowCount + "), or record count (" + recordCount + ")");
            System.out.println("Number of records currently displayed is " + visibleRows + " of a possible " + recordCount);
        }
    }


    public void iEnterIntoTheGridHeader(String textEntry, String header) {
        System.out.println("Searching for " + textEntry + " in " + header + " column");
        valueStore.put("Grid Header Name", header);
        valueStore.put("Grid Header Input", textEntry);
        commonFunctions.gridHeaderFieldWrite(header, textEntry);
        BaseUtil.pageLoaded();
    }

    public void iGetTheTextFromTheGridHeader(String headerName) {
        String headerText = commonFunctions.gridHeaderFieldRead(headerName);
        System.out.println("Text in " + headerName + " header: " + headerText);
        valueStore.put(headerText, headerText);
    }

    public void iSelectFromTheHeaderInTheGrid(String selection, String header) {
        System.out.println("Selecting " + selection + " from the " + header + " header");
        valueStore.put("Grid Header Name", header);
        valueStore.put("Grid Header Input", selection);
        Assert.assertTrue(commonFunctions.gridHeaderSelectorSelect(header, selection),
                "Could not find " + selection + " option in " + header + " grid header");
        BaseUtil.pageLoaded();
    }


    public void iGetTheNumberOfRecordsInTheTab(String tabName) {
        BaseUtil.pageLoaded();
        commonFunctions.gridTab(tabName);
        gridRecords = commonFunctions.gridRecordNumber();
        System.out.println("Number of records in " + tabName + " grid: " + gridRecords);
    }


    public void iCheckIfIsSelectedInTheHeader(String selection, String headerName) {
        ArrayList<String> entries = commonFunctions.gridHeaderSelectorRead(headerName);
        Assert.assertNotNull(entries, "No active selections in " + headerName + " list!");
        Assert.assertTrue(entries.contains(selection), selection + " not selected in " + headerName + " list!");
        System.out.println(selection + " is currently selected in " + headerName + " list");

    }


    public void iCheckIfTheFollowingItemsAreSelectedInTheHeader(String headerName, List<List<String>> table) {
        List<String> items = table.get(0);
        ArrayList<String> entries = commonFunctions.gridHeaderSelectorRead(headerName);
        Assert.assertNotNull(entries, "No active selections in " + headerName + " list!");
        for (String entry : items) {
            Assert.assertTrue(entries.contains(entry), entry + " not selected in " + headerName + " list!");
            System.out.println(entry + " is currently selected in " + headerName + " list");
        }
    }

    public void iGetTheForRowOfTheGrid(String columnName, String rowNumber) {
        // columnName is the header of the grid
        // rowNumber is the index (starting at 1) of the row you want to view (eg. "row 3")
        valueStore.put("Grid Header Name", columnName);
        valueStore.put("Grid Header Input", commonFunctions.gridEntry(rowNumber, columnName).getText());
        System.out.println("The info in " + rowNumber + " for " + columnName + " was " + valueStore.get("Grid Header Input"));
    }

    public void verifyTheFollowingHeadersArePresent(List<List<String>> table) {
        List<String> headers = table.get(0);
        BaseUtil.pageLoaded();
        for (String header : headers) {
            Assert.assertTrue(commonFunctions.gridHeaderFind(header),
                    header + " header not visible in the grid!");
            System.out.println(header + " header verified on grid");
        }
    }

    public void iStoreThe(String labelName) {
        System.out.println("Storing the " + labelName + " for later use.");
        valueStore.put(labelName, commonFunctions.commonLabelRead(labelName));
    }

    public void theButtonDisplayed(String buttonName, String expectation) {
        if (expectation.toLowerCase().contains("not")) {
            Assert.assertNull(commonFunctions.commonButton(buttonName),
                    "The " + buttonName + " button was displayed, but should not have been.");
        } else {
            Assert.assertNotNull(commonFunctions.commonButton(buttonName),
                    "The " + buttonName + " button was not displayed, but should have been.");
        }
    }

    public void iEnterTheFollowingInformationIntoTheForm(Map<String, String> table) {
        String dateString = dateFormat.format(new Date());
        String timeString = timeFormat.format(new Date());
        String random = new SimpleDateFormat("DDDyyHHmmss").format(new Date());
        table.forEach((key, value) -> {
            boolean foundItem = false;
            WebElement element = null;
            if (value.contains("<current date>")) {
                value = value.replaceAll("<current date>", dateString + " " + timeString);
            }
            if (value.contains("<random>")) {
                value = value.replaceAll("<random>", random);
            }
            if (value.contains("<gmail>")) {
                String email = login.getLogins().getProperty("gmail.email");
                if (value.contains("+")) {
                    String modifier = value.substring(value.indexOf('+'));
                    String[] eSplit = email.split("@");
                    email = eSplit[0] + modifier + "@" + eSplit[1];
                }
                value = email;
            }
            if ((element = commonFunctions.commonField(key)) != null) {
                System.out.println("Entering \"" + value + "\" into \"" + key + "\" field");
                commonFunctions.commonFieldEnter(element, value);
                foundItem = true;
            } else if ((element = commonFunctions.commonDropDown(key)) != null) {
                System.out.println("Selecting \"" + value + "\" value from \"" + key + "\" drop down");
                Assert.assertTrue(commonFunctions.commonDropDownSelect(element, value), "Could not select \"" + value + "\" value from \"" + key + "\" drop down");
                foundItem = true;
            } else if (commonFunctions.commonTextAreaEnter(key, value)) {
                System.out.println("Entering \"" + value + "\" into \"" + key + "\" text area");
                foundItem = true;
            }
            Assert.assertTrue(foundItem, "Could not find " + key + " field, drop down, or text area!");
            valueStore.put(key, value);
        });
    }


}
