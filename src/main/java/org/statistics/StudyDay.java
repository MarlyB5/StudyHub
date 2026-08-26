package org.statistics;

// Simple daily study record for statistics persistence
public class StudyDay {
    private String date; // ISO format: yyyy-MM-dd
    private int studiedSeconds;

    // No-arg constructor for Gson
    public StudyDay() {
    }

    public StudyDay(String date) {
        this.date = date;
        this.studiedSeconds = 0;
    }

    public StudyDay(String date, int studiedSeconds) {
        this.date = date;
        this.studiedSeconds = studiedSeconds;
    }

    public String getDate() {
        return date;
    }

    public int getStudiedSeconds() {
        return studiedSeconds;
    }

    public void addSecond() {
        this.studiedSeconds++;
    }

    public void addSeconds(int seconds) {
        if (seconds > 0) this.studiedSeconds += seconds;
    }
}