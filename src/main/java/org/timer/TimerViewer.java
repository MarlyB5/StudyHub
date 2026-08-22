package org.timer;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class TimerViewer {

    public VBox createTimer(
            Runnable backAction
    ) {

        Label titleLabel = new Label("Pomodoro Timer");
        Label sessionLabel = new Label("Work Session");
        Label timerLabel = new Label("25:00");
        Label sessionsLabel = new Label("Sessions completed: 0");

        Button startButton = new Button("Start");
        Button pauseButton = new Button("Pause");
        Button resetButton = new Button("Reset");
        Button backButton = new Button("Back");

        // Duration controls (hidden by default)
        Label workLabel = new Label("Work duration (minutes):");
        TextField workMinutesField = new TextField("25");
        Label breakLabel = new Label("Break duration (minutes):");
        TextField breakMinutesField = new TextField("5");
        Button applyButton = new Button("Apply Durations");
        Button changeButton = new Button("Change Durations");

        PomodoroTimer timer = new PomodoroTimer(
                (secondsRemaining, workSession, completedSessions) -> {
                    timerLabel.setText(formatTime(secondsRemaining));
                    if (workSession) {
                        sessionLabel.setText("Work Session");
                    } else {
                        sessionLabel.setText("Break Session");
                    }
                    sessionsLabel.setText("Sessions completed: " + completedSessions);
                }
        );

        // Hide inputs and apply by default (and do not take layout space)
        workLabel.setVisible(false);
        workLabel.setManaged(false);
        workMinutesField.setVisible(false);
        workMinutesField.setManaged(false);
        breakLabel.setVisible(false);
        breakLabel.setManaged(false);
        breakMinutesField.setVisible(false);
        breakMinutesField.setManaged(false);
        applyButton.setVisible(false);
        applyButton.setManaged(false);

        // Actions
        startButton.setOnAction(event -> timer.start());
        pauseButton.setOnAction(event -> timer.pause());
        resetButton.setOnAction(event -> timer.reset());
        backButton.setOnAction(event -> backAction.run());

        changeButton.setOnAction(event -> {
            // Prefill from current timer values
            workMinutesField.setText(String.valueOf(timer.getSessionLengthMinutes()));
            breakMinutesField.setText(String.valueOf(timer.getBreakLengthMinutes()));
            // Show inputs and apply
            workLabel.setVisible(true);
            workLabel.setManaged(true);
            workMinutesField.setVisible(true);
            workMinutesField.setManaged(true);
            breakLabel.setVisible(true);
            breakLabel.setManaged(true);
            breakMinutesField.setVisible(true);
            breakMinutesField.setManaged(true);
            applyButton.setVisible(true);
            applyButton.setManaged(true);
            // Optionally disable change to prevent re-entry
            changeButton.setDisable(true);
        });

        applyButton.setOnAction(event -> {
            try {
                int workMinutes = Integer.parseInt(workMinutesField.getText());
                int breakMinutes = Integer.parseInt(breakMinutesField.getText());

                timer.setSESSION_LENGTH(workMinutes);
                timer.setBREAK_LENGTH(breakMinutes);
                timer.reset();

                // Hide inputs and re-enable change button
                workLabel.setVisible(false);
                workLabel.setManaged(false);
                workMinutesField.setVisible(false);
                workMinutesField.setManaged(false);
                breakLabel.setVisible(false);
                breakLabel.setManaged(false);
                breakMinutesField.setVisible(false);
                breakMinutesField.setManaged(false);
                applyButton.setVisible(false);
                applyButton.setManaged(false);
                changeButton.setDisable(false);

            } catch (NumberFormatException exception) {
                System.out.println("Please enter whole numbers");
            }
        });

        VBox timerLayout = new VBox(
                15,
                titleLabel,
                sessionLabel,
                timerLabel,
                sessionsLabel,
                changeButton,
                workLabel,
                workMinutesField,
                breakLabel,
                breakMinutesField,
                applyButton,
                startButton,
                pauseButton,
                resetButton,
                backButton
        );

        timerLayout.setAlignment(Pos.CENTER);
        timerLayout.setPadding(new Insets(20));

        return timerLayout;
    }

    private String formatTime(int totalSeconds) {
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }
}