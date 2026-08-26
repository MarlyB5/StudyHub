package org.statistics;

import org.modules.StudyModule;
import org.persistence.AppData;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class StatisticsService {

    private final AppData appData;

    public StatisticsService(AppData appData) {
        this.appData = appData;
    }

    public int getTodayStudySeconds() {
        if (appData == null || appData.getStudyHistory() == null) return 0;
        String today = LocalDate.now().toString();
        int sum = 0;
        for (StudyDay d : appData.getStudyHistory()) {
            if (d != null && Objects.equals(today, d.getDate())) {
                sum += Math.max(0, d.getStudiedSeconds());
            }
        }
        return sum;
    }

    public int getThisWeekStudySeconds() {
        if (appData == null || appData.getStudyHistory() == null) return 0;
        LocalDate today = LocalDate.now();
        LocalDate start = today.with(DayOfWeek.MONDAY);
        LocalDate end = today.with(DayOfWeek.SUNDAY);
        int sum = 0;
        for (StudyDay d : appData.getStudyHistory()) {
            if (d == null || d.getDate() == null || d.getDate().isBlank()) continue;
            try {
                LocalDate date = LocalDate.parse(d.getDate());
                if ((date.isEqual(start) || date.isAfter(start)) && (date.isEqual(end) || date.isBefore(end))) {
                    sum += Math.max(0, d.getStudiedSeconds());
                }
            } catch (Exception ignored) {
                // ignore malformed dates
            }
        }
        return sum;
    }

    // Returns exactly 7 entries, oldest -> newest, filling zeros for missing days (do not persist those)
    public List<StudyDay> getLastSevenDaysFilled() {
        List<StudyDay> out = new ArrayList<>();
        LocalDate today = LocalDate.now();
        for (int i = 6; i >= 0; i--) {
            LocalDate day = today.minusDays(i);
            String key = day.toString();
            StudyDay match = null;
            if (appData != null && appData.getStudyHistory() != null) {
                for (StudyDay d : appData.getStudyHistory()) {
                    if (d != null && key.equals(d.getDate())) { match = d; break; }
                }
            }
            if (match == null) {
                match = new StudyDay(key, 0);
            }
            out.add(match);
        }
        return out;
    }

    public StudyModule getMostStudiedModule() {
        if (appData == null || appData.getStudyModules() == null || appData.getStudyModules().isEmpty()) return null;
        return appData.getStudyModules()
                .stream()
                .max(Comparator.comparingInt(m -> m == null ? 0 : Math.max(0, m.getStudiedSeconds())))
                .orElse(null);
    }

    public int getLifetimeStudySeconds() {
        return appData == null ? 0 : Math.max(0, appData.getTotalStudySeconds());
    }

    public int getCompletedPomodoros() {
        return appData == null ? 0 : Math.max(0, appData.getCompletedWorkSessions());
    }
}