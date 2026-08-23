package org.testutil;

import javafx.application.Platform;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Utility to ensure the JavaFX toolkit is initialized for tests.
 */
public final class JavaFxTestUtils {
    private static volatile boolean initialized = false;

    private JavaFxTestUtils() {}

    public static void ensureInitialized() {
        if (initialized) return;
        synchronized (JavaFxTestUtils.class) {
            if (initialized) return;
            try {
                // Try to start JavaFX platform. If it's already started, an IllegalStateException is thrown.
                CountDownLatch latch = new CountDownLatch(1);
                Platform.startup(latch::countDown);
                // Wait briefly for startup to complete
                try {
                    latch.await(5, TimeUnit.SECONDS);
                } catch (InterruptedException ignored) {
                }
            } catch (IllegalStateException alreadyStarted) {
                // Platform already started — nothing to do.
            }
            initialized = true;
        }
    }
}
