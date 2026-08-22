package org.timer;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

// timer logivc
public class PomodoroTimer {

    private static final int SESSION_LENGTH = 10; // ins seconds
    private static final int BREAK_LENGTH = 5;


    private int secondsRemaining;

    private boolean workSession = true;

    private int completedSessions = 0;

    private final Timeline timeline;

    private final TimeListener timeListener;


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
        }
        else {
            workSession = true;
            secondsRemaining = SESSION_LENGTH;
        }
    }

    private void notifyListener(){


        if (timeListener != null) {

            timeListener.onTimerUpdate(
                    secondsRemaining,
                    workSession,
                    completedSessions
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
