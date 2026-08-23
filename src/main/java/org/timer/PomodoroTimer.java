package org.timer;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

// timer logic
public class PomodoroTimer {

    private int SESSION_LENGTH = 25 * 60; // in seconds
    private int BREAK_LENGTH = 5 * 60;     // in seconds

    private int secondsRemaining;
    private boolean workSession = true;
    private int completedSessions = 0;
    private int totalStudySeconds;


    private final Timeline timeline;
    private final TimeListener timeListener;

    // setters and getters (accept minutes, store seconds)
    public void setSESSION_LENGTH(int minutes) {
        if (minutes >= 1) {
            SESSION_LENGTH = minutes * 60;
        }
    }

    public void setBREAK_LENGTH(int minutes) {
        if (minutes >= 1) {
            BREAK_LENGTH = minutes * 60;
        }
    }

    public int getSessionLengthMinutes() {
        return SESSION_LENGTH / 60;
    }

    public int getBreakLengthMinutes() {
        return BREAK_LENGTH / 60;
    }

    // constructors
    public PomodoroTimer(TimeListener timeListener) {
        this.timeListener = timeListener;
        secondsRemaining = SESSION_LENGTH;

        timeline = new Timeline(
                new KeyFrame(
                        Duration.seconds(1),
                        event -> tick()
                )
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
    }

    private void tick() {
        secondsRemaining--;

        if (workSession) {
            totalStudySeconds ++;
        }

        if (secondsRemaining <= 0) {
            switchSession();
        }

        notifyListener();
    }

    private void switchSession() {
        if (workSession) {
            completedSessions++;
            workSession = false;
            secondsRemaining = BREAK_LENGTH;
        } else {
            workSession = true;
            secondsRemaining = SESSION_LENGTH;
        }
    }

    private void notifyListener() {
        if (timeListener != null) {
            timeListener.onTimerUpdate(
                    secondsRemaining,
                    workSession,
                    completedSessions,
                    totalStudySeconds
            );
        }
    }

    public void start() {
        timeline.play();
    }

    public void pause() {
        timeline.pause();
    }

    public void reset() {
        timeline.stop();
        workSession = true;
        secondsRemaining = SESSION_LENGTH;
        notifyListener();
    }
}
