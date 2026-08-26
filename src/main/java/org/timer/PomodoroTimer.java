package org.timer;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import org.persistence.AppData;
import org.statistics.StudyDay;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
    private final StudyTimeListener studyTimeListener;
    private final AppData appData;


    // setters and getters (accept minutes, store seconds)
    public void setWorkDurationMinutes(int minutes) {
        if (minutes >= 1) {
            SESSION_LENGTH = minutes * 60;
            if (workSession) {
                // If currently on work session, reset remaining to new length to reflect change on reset
                secondsRemaining = SESSION_LENGTH;
            }
            // persist
            if (appData != null) appData.setWorkDurationMinutes(minutes);
        }
    }

    public void setBreakDurationMinutes(int minutes) {
        if (minutes >= 1) {
            BREAK_LENGTH = minutes * 60;
            if (!workSession) {
                secondsRemaining = BREAK_LENGTH;
            }
            if (appData != null) appData.setBreakDurationMinutes(minutes);
        }
    }

    public int getWorkDurationMinutes() {
        return SESSION_LENGTH / 60;
    }

    public int getBreakDurationMinutes() {
        return BREAK_LENGTH / 60;
    }

    // Backward-compatible getters
    public int getSessionLengthMinutes() { return getWorkDurationMinutes(); }
    public int getBreakLengthMinutes() { return getBreakDurationMinutes(); }

    // constructor
    public PomodoroTimer(TimeListener timeListener, StudyTimeListener studyTimeListener, AppData appData) {
        this.timeListener = timeListener;
        this.studyTimeListener = studyTimeListener;
        this.appData = appData;

        // initialize from AppData if available
        if (appData != null) {
            SESSION_LENGTH = Math.max(1, appData.getWorkDurationMinutes()) * 60;
            BREAK_LENGTH = Math.max(1, appData.getBreakDurationMinutes()) * 60;
            completedSessions = Math.max(0, appData.getCompletedWorkSessions());
            totalStudySeconds = Math.max(0, appData.getTotalStudySeconds());
        }

        secondsRemaining = SESSION_LENGTH;

        timeline = new Timeline(
                new KeyFrame(
                        Duration.seconds(1),
                        event -> tick()
                )
        );
        timeline.setCycleCount(Timeline.INDEFINITE);

        // notify initial state to listener
        notifyListener();
    }

    private void tick() {
        secondsRemaining--;

        if (workSession) {
            totalStudySeconds++;
            if (appData != null) {
                appData.setTotalStudySeconds(totalStudySeconds);
                // Record into daily history for today
                try {
                    List<StudyDay> history = appData.getStudyHistory();
                    if (history == null) {
                        history = new ArrayList<>();
                        appData.setStudyHistory(history);
                    }
                    String today = LocalDate.now().toString();
                    StudyDay todayEntry = null;
                    for (StudyDay d : history) {
                        if (d != null && today.equals(d.getDate())) {
                            todayEntry = d;
                            break;
                        }
                    }
                    if (todayEntry == null) {
                        todayEntry = new StudyDay(today, 0);
                        history.add(todayEntry);
                        System.out.println("[DEBUG] Created new StudyDay for " + today);
                    }
                    todayEntry.addSecond();
                } catch (Exception ignored) {
                    // Defensive: statistics should never break the timer
                }
            }
            if (studyTimeListener != null) {
                studyTimeListener.onStudySecond();
            }
        }

        if (secondsRemaining <= 0) {
            switchSession();
        }

        notifyListener();
    }

    private void switchSession() {
        if (workSession) {
            completedSessions++;
            if (appData != null) appData.setCompletedWorkSessions(completedSessions);
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
