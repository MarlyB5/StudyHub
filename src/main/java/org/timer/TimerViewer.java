package org.timer;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class TimerViewer {

    public VBox createTimer(
            Runnable backAction
    ) {

        Label titleLabel =
                new Label("Pomodoro Timer");

        Label sessionLabel =
                new Label("Work Session");

        Label timerLabel =
                new Label("25:00");

        Label sessionsLabel =
                new Label("Sessions completed: 0");

        Button startButton =
                new Button("Start");

        Button pauseButton =
                new Button("Pause");

        Button resetButton =
                new Button("Reset");

        Button backButton =
                new Button("Back");


        PomodoroTimer timer =
                new PomodoroTimer(
                        (secondsRemaining, workSession, completedSessions) -> {

                            timerLabel.setText(
                                    formatTime(secondsRemaining)
                            );

                            if (workSession) {

                                sessionLabel.setText(
                                        "Work Session"
                                );

                            } else {

                                sessionLabel.setText(
                                        "Break Session"
                                );
                            }

                            sessionsLabel.setText(
                                    "Sessions completed: "
                                            + completedSessions
                            );
                        }
                );


        startButton.setOnAction(
                event -> timer.start()
        );

        pauseButton.setOnAction(
                event -> timer.pause()
        );

        resetButton.setOnAction(
                event -> timer.reset()
        );

        backButton.setOnAction(
                event -> backAction.run()
        );


        VBox timerLayout =
                new VBox(
                        15,
                        titleLabel,
                        sessionLabel,
                        timerLabel,
                        sessionsLabel,
                        startButton,
                        pauseButton,
                        resetButton,
                        backButton
                );


        timerLayout.setAlignment(
                Pos.CENTER
        );

        timerLayout.setPadding(
                new Insets(20)
        );


        return timerLayout;
    }


    private String formatTime(
            int totalSeconds
    ) {

        int minutes =
                totalSeconds / 60;

        int seconds =
                totalSeconds % 60;


        return String.format(
                "%02d:%02d",
                minutes,
                seconds
        );
    }
}