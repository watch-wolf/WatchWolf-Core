package dev.watchwolf.core.utils;

import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.engine.TestSource;
import org.junit.platform.engine.support.descriptor.MethodSource;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestIdentifier;

public class GlobalTestingListener implements TestExecutionListener {
    public String getName(TestIdentifier i) {
        String name = i.getDisplayName();
        TestSource ts = i.getSource().orElse(null);
        if (ts instanceof MethodSource) {
            MethodSource m = (MethodSource)ts;
            name = m.getJavaClass().getSimpleName() + "#" + m.getMethodName();
        }
        return name;
    }

    @Override
    public void executionStarted(TestIdentifier i) {
        if (i.getSource().orElse(null) instanceof MethodSource) {
            // method has started
            log("> " + getName(i));
        }
    }

    @Override
    public void executionFinished(TestIdentifier i, TestExecutionResult r) {
        if (i.getSource().orElse(null) instanceof MethodSource) {
            // method has finished
            log("< " + getName(i));
        }
    }

    /*@Override
    public void testPlanExecutionStarted(TestPlan testPlan) {
        log("# testPlanExecutionStarted: " + testPlan.getRoots());
    }*/

    /*@Override
    public void testPlanExecutionFinished(TestPlan testPlan) {
        log("# testPlanExecutionFinished: " + testPlan.getRoots());
    }*/

    void log(String message) {
        System.out.println(message);
    }
}
