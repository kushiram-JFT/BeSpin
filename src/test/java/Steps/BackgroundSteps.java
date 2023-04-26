package Steps;

import Base.BaseUtil;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assume;
import org.testng.Assert;

public class BackgroundSteps extends BaseUtil {
    @Then("I {string} the test")
    public void forceTestResult(String expectation) {
        switch (expectation.toLowerCase()) {
            case "pass":
                Assert.assertTrue(true);
                break;
            case "fail":
                Assert.fail("This message is from the step that failed the test.");
                break;
            case "skip":
                Assume.assumeTrue("This message is from the step that skipped the test.", false);
                break;
            default:
        }
    }

}