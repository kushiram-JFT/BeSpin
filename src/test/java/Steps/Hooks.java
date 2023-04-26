package Steps;

import Base.BaseUtil;
import Base.CommonStepFunctions;
import Base.Emails;
import Pages.*;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.apache.commons.io.FileUtils;
import org.junit.Assume;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;

public class Hooks extends BaseUtil {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat timeFormat = new SimpleDateFormat("HH-mm-ss");

    // TestRail variables
    ArrayList<String> idList;
    String currentId;
    boolean skipped = false;

    public Hooks() {
    }

    @Before
    public void InitializeTest(Scenario scenario) {
        testRailStatus = "Test passed with no issues";
        scenarioName = scenario.getName();
        idList = (ArrayList<String>) scenario.getSourceTagNames();

        if (testRun.getUseTestRail()) {
            boolean caseFound = false;
            for (String testId : idList) {
                if (testRun.checkCaseId(testId)) {
                    currentId = testId;
                    caseFound = true;
                    break;
                }
            }

            if (!caseFound) {
                skipped = true;
                System.out.println("Case ID " + currentId + " was not in this run, skipping scenario");
                Assume.assumeTrue("Test ID " + currentId + " not in run, skipping", false);
            }
        }

        // Setting ChromeOptions to change the download folder to the one located in project directory
        ChromeOptions options = new ChromeOptions();
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("download.default_directory", dLFolder);
        options.setExperimentalOption("prefs", prefs);
        options.addArguments("--remote-allow-origins=*");
        try {
            final Properties config = new Properties();
            config.load(new FileInputStream(Thread.currentThread().getContextClassLoader().getResource("").getPath() + "config.properties"));
            if(config.getProperty("headless").equalsIgnoreCase("true")){
                options.addArguments("--headless");
                options.addArguments("window-size=1920,1080");
                options.addArguments("--no-sandbox");
                options.addArguments("--disable-dev-shm-usage");
            }
        } catch (IOException | NullPointerException e) {
            System.out.println("Did not find \"headless\" flag. Not running headless.");
        }
        System.out.println("Opening Chrome browser");
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        js = (JavascriptExecutor) driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        login = new Login(driver);
        emails = new Emails(login.getLogins().getProperty("gmail.email"), login.getLogins().getProperty("gmail.auth"));
        commonFunctions = new BeSpin(driver);
        commonStepFunctions = new CommonStepFunctions();

    }

    @After
    public void TearDownTest(Scenario scenario) {
        scenario.log("Scenario finished");
        String dateString = dateFormat.format(new Date());
        String timeString = timeFormat.format(new Date());
        int statusCode = switch (scenario.getStatus()){
            case PASSED -> 1;
            case SKIPPED -> 2;
            case PENDING -> 4;
            case UNDEFINED, AMBIGUOUS, FAILED, UNUSED -> 5;
        };
        if (scenario.isFailed()) {
            // TakesScreenshot
            String sName = scenarioName.replaceAll("[^\\d\\w]", "");

            File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            try {
                FileUtils.copyFile(srcFile, new File("target/screenshots/" + sName + "-" + dateString + "--" +  timeString + ".png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            // On test failure all items entered into valueStore are added to TestRail message and printed to console
            testRailStatus += "\nVALUES ENTERED DURING TEST:\n";
            valueStore.forEach((key, value) -> {
                testRailStatus += key + " - " + value + "\n";
                System.out.println(key + " - " + value);
            });
        }
        if (!skipped) {
            driver.quit();
            if (testRun.getUseTestRail()) {
                testRun.setStatus(currentId, statusCode, testRailStatus);
            }
        }

    }
}
