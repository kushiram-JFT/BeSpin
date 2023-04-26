package Base;


import io.cucumber.core.plugin.JsonFormatter;
import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.event.EventPublisher;
import io.cucumber.plugin.event.PickleStepTestStep;
import io.cucumber.plugin.event.TestCaseFinished;
import io.cucumber.plugin.event.TestStepFinished;

public class FailedStepReport implements ConcurrentEventListener {
    private final CleanAppendable out;
    private JsonFormatter test;

    public FailedStepReport(Appendable out) {
        this.out = new CleanAppendable(out);
    }

    private void stepResult(TestStepFinished step) {
        String result = step.getResult().getStatus().toString();
        if (step.getTestStep() instanceof PickleStepTestStep testStep) {
            switch (step.getResult().getStatus()) {
                case FAILED -> {
                    out.println("Step name: " + testStep.getStep().getText());
                    out.println("Status is: " + result);
                    BaseUtil.testRailStatus = "Test failed at step: " + "\n" + testStep.getStep().getText() + "\n" +
                            "With error: " + "\n" + step.getResult().getError();
                }
                case SKIPPED -> {
                    if (step.getResult().getError() != null) {
                        out.println("Step name: " + testStep.getStep().getText());
                        out.println("Status is: " + result);
                        BaseUtil.testRailStatus = "Test skipped at step: " + "\n" + testStep.getStep().getText() + "\n" +
                                "Reason for skip: " + "\n" + step.getResult().getError();
                    }

                }
            }
        }
    }

    private void caseResult(TestCaseFinished testCase) {


        out.println(String.join(",", (CharSequence) testCase.getTestCase().getTestSteps()));

    }

    @Override
    public void setEventPublisher(EventPublisher eventPublisher) {
        eventPublisher.registerHandlerFor(TestStepFinished.class, this::stepResult);
//        eventPublisher.registerHandlerFor(TestCaseFinished.class, this::caseResult);
    }
}
