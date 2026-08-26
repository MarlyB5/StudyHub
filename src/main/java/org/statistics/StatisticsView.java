package org.statistics;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;
import org.modules.StudyModule;
import org.persistence.AppData;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

public class StatisticsView {

    private AppData appData;
    private Runnable backAction;

    private VBox root;

    // Sections
    private VBox summaryBox;
    private VBox chartBox;
    private VBox byModuleBox;
    private Label mostStudiedLabel;

    private StatisticsService service;

    public VBox createStatistics(AppData appData, Runnable backAction) {
        this.appData = appData;
        this.backAction = backAction;
        this.service = new StatisticsService(appData);

        Label header = new Label("STATISTICS");

        summaryBox = new VBox(4);
        chartBox = new VBox(8);
        byModuleBox = new VBox(4);
        mostStudiedLabel = new Label("");

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> { if (this.backAction != null) this.backAction.run(); });

        root = new VBox(12,
                header,
                summaryBox,
                new Separator(),
                new Label("Study Time — Last 7 Days"),
                chartBox,
                new Separator(),
                new Label("Study by Module"),
                byModuleBox,
                new Label("Most Studied Module"),
                mostStudiedLabel,
                backButton
        );
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(20));

        refreshStatistics();
        return root;
    }

    public void refreshStatistics() {
        if (root == null) return;
        System.out.println("[DEBUG] Refreshing statistics view");
        buildSummary();
        buildLastSevenDaysChart();
        buildByModule();
        buildMostStudied();
    }

    private void buildSummary() {
        summaryBox.getChildren().clear();
        int todaySec = service.getTodayStudySeconds();
        int weekSec = service.getThisWeekStudySeconds();
        int lifeSec = service.getLifetimeStudySeconds();
        int pom = service.getCompletedPomodoros();

        summaryBox.getChildren().addAll(
                new Label("Today"), new Label(formatStudyTime(todaySec)),
                new Label("This Week"), new Label(formatStudyTime(weekSec)),
                new Label("Lifetime"), new Label(formatStudyTime(lifeSec)),
                new Label("Pomodoros"), new Label(String.valueOf(pom))
        );
    }

    private void buildLastSevenDaysChart() {
        chartBox.getChildren().clear();

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Hours");

        BarChart<String, Number> chart = new BarChart<>(xAxis, yAxis);
        chart.setLegendVisible(false);

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        List<StudyDay> last7 = service.getLastSevenDaysFilled();
        for (StudyDay d : last7) {
            String label = toShortDayLabel(d.getDate());
            double hours = Math.max(0, d.getStudiedSeconds()) / 3600.0;
            series.getData().add(new XYChart.Data<>(label, hours));
        }
        chart.getData().add(series);
        chart.setPrefWidth(480);
        chart.setPrefHeight(260);
        chartBox.getChildren().add(chart);
    }

    private void buildByModule() {
        byModuleBox.getChildren().clear();
        if (appData == null || appData.getStudyModules() == null || appData.getStudyModules().isEmpty()) {
            byModuleBox.getChildren().add(new Label("No study modules added yet."));
            return;
        }
        for (StudyModule m : appData.getStudyModules()) {
            if (m == null) continue;
            String code = m.getModuleCode();
            byModuleBox.getChildren().add(new Label((code == null ? "?" : code) + " — " + formatStudyTime(m.getStudiedSeconds())));
        }
    }

    private void buildMostStudied() {
        StudyModule top = service.getMostStudiedModule();
        if (top == null || top.getStudiedSeconds() <= 0) {
            mostStudiedLabel.setText("No module study data yet.");
        } else {
            String code = top.getModuleCode() == null ? "?" : top.getModuleCode();
            String name = top.getModuleName();
            String line1 = name == null || name.isBlank() ? code : code + " - " + name;
            String line2 = formatStudyTime(top.getStudiedSeconds()) + " studied";
            mostStudiedLabel.setText(line1 + "\n" + line2);
        }
    }

    private String formatStudyTime(int totalSeconds) {
        if (totalSeconds < 0) totalSeconds = 0;
        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        return hours + "h " + minutes + "m";
    }

    private String toShortDayLabel(String isoDate) {
        try {
            LocalDate d = LocalDate.parse(isoDate);
            String day = d.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.getDefault());
            // e.g., Mon 26
            return day + " " + d.getDayOfMonth();
        } catch (Exception e) {
            return isoDate;
        }
    }
}
