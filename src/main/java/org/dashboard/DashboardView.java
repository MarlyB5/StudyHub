package org.dashboard;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import org.modules.StudyModule;
import org.persistence.AppData;
import org.todo.Task;

import java.util.ArrayList;
import java.util.List;

public class DashboardView {

    private AppData appData;
    private Runnable backAction;

    private VBox root;

    // Sections
    private VBox studySummaryBox;
    private VBox taskSummaryBox;
    private VBox cardsContainer; // existing Study Progress cards
    private VBox tasksPreviewBox;

    // Section titles
    private Label studySummaryTitle;
    private Label taskSummaryTitle;
    private Label studyProgressTitle;
    private Label tasksPreviewTitle;

    public VBox createDashboard(AppData appData, Runnable backAction) {
        this.appData = appData;
        this.backAction = backAction;

        Label header = new Label("Dashboard");

        studySummaryTitle = new Label("STUDY SUMMARY");
        studySummaryBox = new VBox(4);

        taskSummaryTitle = new Label("TASK SUMMARY");
        taskSummaryBox = new VBox(4);

        studyProgressTitle = new Label("STUDY PROGRESS");
        cardsContainer = new VBox(10);

        tasksPreviewTitle = new Label("TASKS TO DO");
        tasksPreviewBox = new VBox(4);

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> {
            if (this.backAction != null) {
                this.backAction.run();
            }
        });

        root = new VBox(
                12,
                header,
                studySummaryTitle,
                studySummaryBox,
                taskSummaryTitle,
                taskSummaryBox,
                new Label("------------------------"),
                studyProgressTitle,
                cardsContainer,
                new Label("------------------------"),
                tasksPreviewTitle,
                tasksPreviewBox,
                backButton
        );
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(20));

        refreshProgress();
        return root;
    }

    // Keeps existing method name so Main does not need changes
    public void refreshProgress() {
        if (root == null) return;

        // Debug: show counts
        int moduleCount = (appData != null && appData.getStudyModules() != null) ? appData.getStudyModules().size() : 0;
        int taskCount = (appData != null && appData.getTasks() != null) ? appData.getTasks().size() : 0;
        System.out.println("Refreshing dashboard: modules=" + moduleCount + ", tasks=" + taskCount);

        // Rebuild all sections
        buildStudySummary();
        buildTaskSummary();
        buildStudyProgress();
        buildTasksPreview();
    }

    private void buildStudySummary() {
        studySummaryBox.getChildren().clear();
        int totalSeconds = (appData != null) ? appData.getTotalStudySeconds() : 0;
        int completedPomodoros = (appData != null) ? appData.getCompletedWorkSessions() : 0;

        Label totalStudy = new Label("Total Study Time");
        Label totalStudyValue = new Label(formatStudyTime(totalSeconds));

        Label pomLabel = new Label("Pomodoros Completed");
        Label pomValue = new Label(String.valueOf(completedPomodoros));

        studySummaryBox.getChildren().addAll(totalStudy, totalStudyValue, pomLabel, pomValue);
    }

    private void buildTaskSummary() {
        taskSummaryBox.getChildren().clear();
        int remaining = 0;
        int completed = 0;

        List<Task> tasks = (appData != null) ? appData.getTasks() : null;
        if (tasks != null) {
            for (Task t : tasks) {
                if (t != null && isCompleted(t)) {
                    completed++;
                } else if (t != null) {
                    remaining++;
                }
            }
        }

        Label remainingLabel = new Label("Remaining Tasks");
        Label remainingValue = new Label(String.valueOf(remaining));

        Label completedLabel = new Label("Completed Tasks");
        Label completedValue = new Label(String.valueOf(completed));

        taskSummaryBox.getChildren().addAll(remainingLabel, remainingValue, completedLabel, completedValue);
    }

    private void buildStudyProgress() {
        cardsContainer.getChildren().clear();
        List<StudyModule> modules = (appData != null) ? appData.getStudyModules() : null;
        if (modules == null || modules.isEmpty()) {
            Label empty1 = new Label("No study modules added yet.");
            Label empty2 = new Label("Add a module to start tracking your progress.");
            VBox box = new VBox(4, empty1, empty2);
            box.setAlignment(Pos.CENTER_LEFT);
            cardsContainer.getChildren().add(box);
            return;
        }
        for (StudyModule module : modules) {
            cardsContainer.getChildren().add(createModuleCard(module));
        }
    }

    private void buildTasksPreview() {
        tasksPreviewBox.getChildren().clear();

        List<Task> allTasks = (appData != null) ? appData.getTasks() : null;
        if (allTasks == null || allTasks.isEmpty()) {
            tasksPreviewBox.getChildren().add(new Label("No tasks to do."));
            return;
        }

        // Filter incomplete tasks
        List<Task> incomplete = new ArrayList<>();
        for (Task t : allTasks) {
            if (t != null && !isCompleted(t)) {
                incomplete.add(t);
            }
        }

        if (incomplete.isEmpty()) {
            tasksPreviewBox.getChildren().add(new Label("All tasks completed!"));
            return;
        }

        int max = Math.min(5, incomplete.size());
        for (int i = 0; i < max; i++) {
            Task t = incomplete.get(i);
            String title = safe(t.getTitle());
            String code = safe(t.getModuleCode());
            String text = title;
            if (!code.isBlank()) {
                text += " — " + code;
            }
            tasksPreviewBox.getChildren().add(new Label("\u2610 " + text)); // ☐ box
        }

        int remaining = incomplete.size() - max;
        if (remaining > 0) {
            tasksPreviewBox.getChildren().add(new Label("+ " + remaining + " more tasks"));
        }
    }

    private VBox createModuleCard(StudyModule module) {
        Label title = new Label(module.getModuleCode() + " - " + module.getModuleName());

        double studiedHours = module.getStudiedHours();
        double targetHours = module.getTargetHours();
        double progress = module.getProgress(); // expected in 0.0..1.0 (model caps if needed)

        Label hours = new Label(String.format("Studied: %.1f h / %.1f h", studiedHours, targetHours));

        ProgressBar bar = new ProgressBar(progress);
        bar.setPrefWidth(320);

        int percent = (int) Math.round(progress * 100);
        Label pct = new Label(percent + "%");

        VBox card = new VBox(6, title, hours, bar, pct);
        card.setPadding(new Insets(10));
        card.setAlignment(Pos.CENTER_LEFT);
        return card;
    }

    private String formatStudyTime(int totalSeconds) {
        if (totalSeconds < 0) totalSeconds = 0;
        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        return hours + "h " + minutes + "m";
    }

    // Defensive helpers
    private boolean isCompleted(Task t) {
        try {
            // Prefer an isCompleted() getter if present
            return t.isCompleted();
        } catch (Throwable ignored) {
            return false;
        }
    }

    private String safe(String s) {
        return (s == null) ? "" : s.trim();
    }
}
