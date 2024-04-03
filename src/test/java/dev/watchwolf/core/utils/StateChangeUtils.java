package dev.watchwolf.core.utils;

import java.util.concurrent.TimeoutException;
import java.util.function.Supplier;

public class StateChangeUtils {
    private static final int MS_BETWEEN_CALLS = 5;

    /**
     * Waits until `evaluate` becomes true, or the max time (`timeout`) is reached.
     * If timeout is 0, then it waits forever.
     * @param evaluate Expression that has to become true to leave
     * @param timeout Number of ms to wait
     * @param assertFailedMsg Message to print when the timeout raises
     * @throws TimeoutException Timeout reached
     */
    public static void pollForCondition(Supplier<Boolean> evaluate, int timeout, String assertFailedMsg) throws TimeoutException {
        int numCalls = (int)Math.ceil((float)timeout / MS_BETWEEN_CALLS);
        while (!evaluate.get()) {
            if (timeout > 0 && numCalls <= 0) throw new TimeoutException(assertFailedMsg);

            try {
                Thread.sleep(MS_BETWEEN_CALLS);
            } catch (InterruptedException ignore) {}

            numCalls--;
        }
        // succeed
    }

    public static void pollForCondition(Supplier<Boolean> evaluate, int timeout) throws TimeoutException {
        StateChangeUtils.pollForCondition(evaluate, timeout, "Max time waiting for condition to be true reached");
    }

    public static void pollForCondition(Supplier<Boolean> evaluate) {
        try {
            StateChangeUtils.pollForCondition(evaluate, 0);
        } catch (TimeoutException ignore) {} // never reached
    }

    /**
     * Waits until `evaluate` becomes true, or the max time (`timeout`) is reached.
     * If timeout is 0, then it waits forever.
     * @param evaluate Expression that has to become true to leave
     * @param timeout Number of ms to wait
     * @throws TimeoutException Timeout reached
     */
    public static void tryPollForCondition(Supplier<Boolean> evaluate, int timeout) {
        try {
            StateChangeUtils.pollForCondition(evaluate, timeout);
        } catch (TimeoutException ex) {} // keep going
    }
}
