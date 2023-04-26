package Base;

import com.gurock.testrail.APIClient;
import com.gurock.testrail.APIException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class TestRailAPI {

    private final boolean useTestRail;
    private HashMap<String, String> testCaseIds;
    private long testRunId;
    private final APIClient client;
    private final int projectId;
    private final Properties login = new Properties();
    private final Properties config = new Properties();

    public TestRailAPI() {
        this.client = new APIClient("https://bespinlabs.testrail.io/");
        try {
            this.login.load(new FileInputStream(Thread.currentThread().getContextClassLoader().getResource("").getPath() + "logins.properties"));
            this.config.load(new FileInputStream(Thread.currentThread().getContextClassLoader().getResource("").getPath() + "config.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.useTestRail = config.getProperty("testrail.use").equalsIgnoreCase("true");
        this.projectId = Integer.parseInt(config.getProperty("testrail.projectId"));
        this.testRunId = config.getProperty("testrail.id").isEmpty() ? 0 : Long.parseLong(config.getProperty("testrail.id"));
        if (useTestRail) {
            client.setUser(login.getProperty("email.testrail"));
            client.setPassword(login.getProperty("pass.testrail"));
            if (this.testRunId == 0) {
                String dateString = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
                String runTitle = config.getProperty("testrail.title") + " - " + dateString;
                this.testCaseIds = createRun(config.getProperty("environment").toUpperCase() + " " + runTitle);
            } else {
                this.testCaseIds = getRunById(testRunId);
            }

        }


    }


    public boolean getUseTestRail() {
        return useTestRail;
    }

    /**
     * Check if the current test case is in the TestRail run
     *
     * @param testId The test case ID with the @ symbol (e.g., @1234)
     * @return
     */
    public boolean checkCaseId(String testId) {
        return testCaseIds.containsKey(testId);

    }

    /**
     * This function is used to retrieve previously created test runs by their IDs
     *
     * @param run The ID of the run to be retrieved for testing
     */
    public HashMap<String, String> getRunById(long run) {
        testRunId = run;
        HashMap<String, String> result = new HashMap<>();
        JSONArray tests = getItems("tests");
        ArrayList<String> testIds = new ArrayList<>();
        for (Object test : tests) {
            HashMap data = (HashMap) test;
            result.put("@" + data.get("case_id").toString(), data.get("id").toString());
            testIds.add("@" + data.get("case_id").toString());
        }
        System.out.println("Initializing test run: \n" + testRunId + "\n" +
                "With the following testIds: \n" + testIds);
        return result;
    }

    /**
     * This function creates a new test run with the given title and returns a HashMap of the test cases.
     * If a test run with the same title exists, it will not create a new one and instead return the test
     * cases from the current one.
     *
     * @param runTitle The title to be used for the test run
     * @return Returns a HashMap of the test cases in the test run
     */
    public HashMap<String, String> createRun(String runTitle) {
        long existingRun = 0;
        if ((existingRun = getRunByTitle(runTitle)) > 0) return getRunById(existingRun);
        ArrayList<Long> caseIds = new ArrayList<>();
        String[] sections = config.getProperty("testrail.sections").isEmpty() ? null : config.getProperty("testrail.sections").replaceAll(" ", "").split(",");
        JSONArray cases = getItems("cases");
        JSONObject run = new JSONObject();


        for (Object item : cases) {
            JSONObject testCase = (JSONObject) item;
            String id = testCase.get("section_id").toString();
            boolean automated = testCase.get("custom_automation_type") != null && testCase.get("custom_automation_type").toString().equals("1");
            if (sections != null && Arrays.asList(sections).contains(id) && automated) {
                caseIds.add((Long) testCase.get("id"));
            } else if (sections == null && automated) {
                caseIds.add((Long) testCase.get("id"));

            }

        }
        try {

            HashMap data = new HashMap();
            data.put("name", runTitle);
            data.put("include_all", false);
            data.put("case_ids", caseIds);

            run = (JSONObject) client.sendPost("add_run/" + this.projectId, data);


        } catch (APIException | IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return getRunById((Long) run.get("id"));

    }

    /**
     * Sets the status and adds a comment to the test case after that test case has been run
     *
     * @param currentTest The ID of the current case being run
     * @param status      The status ID to be passed as an int (e.g., 1 is Pass, 5 is Fail)
     * @param comment     The comment to be added to the case
     */
    public void setStatus(String currentTest, int status, String comment) {
        HashMap data = new HashMap();
        data.put("status_id", status);
        data.put("comment", comment);
        try {
            client.sendPost("add_result/" + testCaseIds.get(currentTest), data);
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    /**
     * Finds a test run by the title and returns the ID
     *
     * @param runTitle The title of the test run to search for
     * @return Returns the ID of the test run as a long or -1 if no run was found
     */
    private long getRunByTitle(String runTitle) {
        JSONArray runs = getItems("runs");

        for (Object item : runs) {
            JSONObject test = (JSONObject) item;
            if (test.get("name").toString().equals(runTitle)) {
                return (Long) test.get("id");
            }
        }
        return -1;
    }

    /**
     * Makes bulk calls for TestRail returning a JSONArray.
     * Currently works with cases, runs, tests, and projects
     *
     * @param type The type of call to make accepts "cases", "runs", "tests", and "projects"
     * @return Returns a JSONArray of the items found
     */
    private JSONArray getItems(String type) {
        JSONObject itemsObj = null;
        JSONArray itemsArr = new JSONArray();
        boolean next;
        String nextString = "";
        String uriString = switch (type.toLowerCase(Locale.ROOT)) {
            case "cases" -> "get_cases/" + this.projectId;
            case "runs" -> "get_runs/" + this.projectId + "/&is_completed=0";
            case "tests" -> "get_tests/" + this.testRunId;
            case "projects" -> "get_projects/&is_completed=0";
            default -> null;
        };

        do {
            try {
                itemsObj = (JSONObject) client.sendGet(uriString + nextString);
            } catch (IOException | APIException e) {
                e.printStackTrace();
            }
            assert itemsObj != null;
            HashMap<String, String> links = new HashMap<>((HashMap<String, String>) itemsObj.get("_links"));
            next = links.get("next") != null;
            if (next) {
                nextString = links.get("next").substring(links.get("next").indexOf('&'));
            }
            itemsArr.addAll((JSONArray) itemsObj.get(type));
        } while (next);

        return itemsArr;
    }

}
