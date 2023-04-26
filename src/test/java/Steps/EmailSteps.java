package Steps;

import Base.BaseUtil;
import io.cucumber.java.en.And;
import org.testng.Assert;

import javax.mail.MessagingException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailSteps extends BaseUtil {


    @And("I send an email with the following information")
    public void iSendAnEmailWithTheFollowingInformation(Map<String, String> emailDetails) {
        HashMap<String, String> emailContent = new HashMap<>(parseEmailContents(emailDetails));
        try {
            emails.sendEmail(emailContent);
        } catch (MessagingException | IOException e) {
            Assert.fail("Sending email failed with the following message:\n" + e);
        }
        System.out.println("Email with subject \"" + emailContent.get("Subject") + "\" sent to " + emailContent.get("To") + " from " + login.getLogins().getProperty("gmail.email"));
    }

    @And("I verify the email for {string} was received")
    public void iVerifyTheEmailForWasReceived(String emailType) {
        System.out.println("Retrieving email for \"" + emailType + "\" from " + login.getLogins().getProperty("gmail.email"));
        String subject = "";
        Pattern linkPattern = null;
        switch (emailType.toLowerCase()) {

            case "policy - deletion request" -> {
                subject = "Policy - Deletion Request";
            }

            case "group - deletion request" -> {
                subject = "Group - Deletion Request";
                linkPattern = Pattern.compile("[a-zA-Z0-9][a-zA-Z0-9_.]*@[a-zA-Z0-9]+([.][a-zA-Z0-9]+)+");
            }
            case "shared contact - deletion request" -> {
                subject = "Domain Shared Contact - Deletion Request";
            }

            case "google contact - deletion request" -> {
                subject = "Google Contact - Deletion Request";
            }
            case "file - unshare request" -> {
                subject = "File - Unshare Request";
            }
            default -> Assert.fail("No subject found for " + emailType + " email type");
        }
        int msgCount = emails.inboxMessageCount();
        System.out.println("message count: " + msgCount);
        Assert.assertNotEquals(msgCount, -1, "Could not retrieve number of messages from inbox");
        String emailMessage = emails.getEmailBySubject(subject);
        if (emailMessage == null) {
            System.out.println("Message not found, waiting for up to one minute for message");
            long startTime = System.currentTimeMillis();
            while (emails.inboxMessageCount() == msgCount && (System.currentTimeMillis() - startTime) < 60000) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ignored) {

                }
            }
            emailMessage = emails.getEmailBySubject(subject);
        }
        Assert.assertNotNull(emailMessage,
                "Email with subject \"" + subject + "\" could not be found after waiting for one minute");
        System.out.println(emailMessage.replaceAll("<[^>]*>", "").replaceAll("&nbsp;", ""));
//        assert linkPattern != null;
//        Matcher msgBody = linkPattern.matcher(emailMessage);
//        while (msgBody.find()) {
//            String msgLink = msgBody.group();
//            System.out.println("found following link: " + msgLink.replaceAll("<[^>]*>","").replaceAll("&nbsp;",""));
//    }
    }

    @And("I open the link from the email")
    public void iOpenTheLinkFromTheEmail() {
        driver.manage().deleteAllCookies();
        driver.get(valueStore.get("Email Link"));
    }

    private HashMap<String, String> parseEmailContents(Map<String, String> emailContent) {
        HashMap<String, String> output = new HashMap<>(emailContent);
        String random = new SimpleDateFormat("DDDyyHHmmss").format(new Date());
        output.forEach((key, value) -> {
            if (value.contains("<random>")) {
                value = value.replaceAll("<random>", random);
            }
            if (value.contains("<#>")) {
                value = value.replaceAll("<#>", valueStore.get("Grid Header Input"));
            }
            output.put(key, value);
        });

        String subject = output.get("Subject");
        subject = subject.substring(subject.indexOf('}') + 1).trim();
        System.out.println("Saving Email subject for later use: " + subject);
        valueStore.put("Email Subject", subject);
        return output;
    }

}
