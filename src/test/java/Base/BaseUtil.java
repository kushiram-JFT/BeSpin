package Base;

import Pages.Login;
import Pages.BeSpin;
import org.openqa.selenium.JavascriptException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseUtil {
    // Driver variables
    public static WebDriver driver;
    public static JavascriptExecutor js;
    public static WebDriverWait wait;

    public static String scenarioName;

    // TestRail variables:
    public static TestRailAPI testRun = new TestRailAPI();
    public static String testRailStatus;

    // StepDef variables to allow transfer of information between StepDef functions
    // Variables to track location and movement
    public static String currentLogin;
    public static String currentApp;
    // Values used by Step Definitions
    public static Map<String, String> valueStore = new HashMap<>();
    public static Map<String, List<String>> valueListStringStore = new HashMap<>();
    public static HashMap<String, String> editedValues = new HashMap<>();
    // Variables for grid
    public static String workOrderNumber;
    public static int rowCount;
    public static int gridRecords;
    public static String headerInfo;
    public static String headerChoice;
    // Date time variables
    public static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    public static SimpleDateFormat timeFormat = new SimpleDateFormat("HHmmss");

    // Class initializers
    public static Login login;
    public static Emails emails;
    public static BeSpin commonFunctions;
    public static CommonStepFunctions commonStepFunctions;


    // filePath is currently set to C:\Users\<USER_NAME>. Feel free to change it
    // filePath is used to determine where screenshots and reports are saved on your system
    public static String filePath = System.getProperty("user.home") + "\\Desktop\\QA-Archetype-Files";

    // Location of attachment on your system
    public static String attachPath(String relativePath) {
        File file = new File(relativePath);
        return file.getAbsolutePath();
    }

    // Folder where files downloaded during tests will be stored
    public static String dLFolder = attachPath("src/test/resources/downloads");


    public static boolean pageLoaded() {
        // wait for jQuery to load and catch if jQuery doesn't exist
        ExpectedCondition<Boolean> jQueryLoad = driver -> {
            try {
                return (Boolean) js.executeScript("return jQuery.active == 0");
            } catch (JavascriptException e) {
                // no jQuery present
                return true;
            }
        };

        // wait for Javascript to load
        ExpectedCondition<Boolean> jsLoad = driver -> js.executeScript("return document.readyState").toString().equals("complete");

        try {
            return wait.until(jQueryLoad) && wait.until(jsLoad);
        } catch (TimeoutException e) {
            System.err.println("Timeout exception occurred for pageLoaded");
        }
        return false;

    }

}
