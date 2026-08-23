package org.timer;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testutil.JavaFxTestUtils;

import static org.junit.jupiter.api.Assertions.*;

public class TimerViewerTest {

    @BeforeAll
    static void initFx() {
        JavaFxTestUtils.ensureInitialized();
    }

    @Test
    void createTimerBuildsExpectedControls() {
        TimerViewer viewer = new TimerViewer();
        final boolean[] backCalled = {false};
        VBox box = viewer.createTimer(() -> backCalled[0] = true);
        assertNotNull(box);
        assertTrue(box.getChildren().size() >= 8, "VBox should contain labels and buttons");

        boolean foundTitle = false;
        boolean foundStart = false;
        boolean foundPause = false;
        boolean foundReset = false;
        boolean foundBack = false;

        for (Node n : box.getChildren()) {
            if (n instanceof Label) {
                String text = ((Label) n).getText();
                if ("Pomodoro Timer".equals(text)) foundTitle = true;
            } else if (n instanceof Button) {
                String text = ((Button) n).getText();
                if ("Start".equals(text)) foundStart = true;
                if ("Pause".equals(text)) foundPause = true;
                if ("Reset".equals(text)) foundReset = true;
                if ("Back".equals(text)) foundBack = true;
            }
        }

        assertTrue(foundTitle, "Should contain a title label");
        assertTrue(foundStart, "Should contain Start button");
        assertTrue(foundPause, "Should contain Pause button");
        assertTrue(foundReset, "Should contain Reset button");
        assertTrue(foundBack, "Should contain Back button");
    }
}
