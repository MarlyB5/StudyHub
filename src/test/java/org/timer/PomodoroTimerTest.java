package org.timer;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testutil.JavaFxTestUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

public class PomodoroTimerTest {

    private Method tickMethod;

    @BeforeAll
    static void initFx() {
        JavaFxTestUtils.ensureInitialized();
    }

    @BeforeEach
    void setupReflection() throws NoSuchMethodException {
        tickMethod = PomodoroTimer.class.getDeclaredMethod("tick");
        tickMethod.setAccessible(true);
    }

    private void invokeTick(PomodoroTimer timer) {
        try {
            tickMethod.invoke(timer);
        } catch (IllegalAccessException | InvocationTargetException e) {
            fail(e);
        }
    }

    @Test
    void settersAcceptMinutesAndResetApplies() {
        AtomicInteger lastSeconds = new AtomicInteger(-1);
        AtomicReference<Boolean> isWork = new AtomicReference<>();
        PomodoroTimer timer = new PomodoroTimer((sec, work, completed) -> {
            lastSeconds.set(sec);
            isWork.set(work);
        });

        timer.setSESSION_LENGTH(1); // 60s
        timer.setBREAK_LENGTH(1);   // 60s
        timer.reset();

        assertEquals(60 - 0, lastSeconds.get());
        assertTrue(isWork.get());
    }

    @Test
    void tickCountsDownAndSwitchesSessionsAndIncrementsCompleted() {
        final AtomicInteger seconds = new AtomicInteger();
        final AtomicReference<Boolean> work = new AtomicReference<>();
        final AtomicInteger completed = new AtomicInteger();

        PomodoroTimer timer = new PomodoroTimer((sec, isWork, done) -> {
            seconds.set(sec);
            work.set(isWork);
            completed.set(done);
        });

        timer.setSESSION_LENGTH(1); // 60 sec
        timer.setBREAK_LENGTH(1);   // 60 sec
        timer.reset();

        // Simulate 60 ticks to end work session
        for (int i = 0; i < 60; i++) {
            invokeTick(timer);
        }
        // After hitting 0, switchSession() sets break and increments completed
        assertFalse(work.get(), "Should be in break session after work session completes");
        assertEquals(1, completed.get(), "Completed sessions should increment after finishing a work session");
        assertEquals(60, seconds.get(), "Break should start with full break length in seconds");

        // Simulate 60 ticks to end break session
        for (int i = 0; i < 60; i++) {
            invokeTick(timer);
        }
        assertTrue(work.get(), "Should switch back to work session after break ends");
        assertEquals(1, completed.get(), "Completed sessions should not increment on finishing a break");
        assertEquals(60, seconds.get(), "Work should restart with full session seconds");
    }

    @Test
    void startPauseDoNotThrow() {
        PomodoroTimer timer = new PomodoroTimer((s, w, c) -> {});
        assertDoesNotThrow(timer::start);
        assertDoesNotThrow(timer::pause);
        assertDoesNotThrow(timer::reset);
    }
}
