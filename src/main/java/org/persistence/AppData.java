package org.persistence;

import org.playlist.Song;
import org.modules.StudyModule;
import org.todo.Task;
import org.statistics.StudyDay;

import java.util.ArrayList;
import java.util.List;

public class AppData {

    private int workDurationMinutes = 25;
    private int breakDurationMinutes = 5;

    private int completedWorkSessions = 0;

    private int totalStudySeconds = 0;
    private List<Song> playlistSongs = new ArrayList<>();
    // Distinguish first-run defaults from deliberately empty saved playlist
    private boolean playlistInitialized = false;

    private List<StudyModule> studyModules = new ArrayList<>();

    // New: tasks for To-Do feature
    private List<Task> tasks = new ArrayList<>();

    // New: daily study history for statistics
    private List<StudyDay> studyHistory = new ArrayList<>();


    public int getWorkDurationMinutes() {
        return workDurationMinutes;
    }

    public void setWorkDurationMinutes(int workDurationMinutes) {
        this.workDurationMinutes = workDurationMinutes;
    }


    public int getBreakDurationMinutes() {
        return breakDurationMinutes;
    }

    public void setBreakDurationMinutes(int breakDurationMinutes) {
        this.breakDurationMinutes = breakDurationMinutes;
    }


    public int getCompletedWorkSessions() {
        return completedWorkSessions;
    }

    public void setCompletedWorkSessions(int completedWorkSessions) {
        this.completedWorkSessions = completedWorkSessions;
    }


    public int getTotalStudySeconds() {
        return totalStudySeconds;
    }

    public void setTotalStudySeconds(int totalStudySeconds) {
        this.totalStudySeconds = totalStudySeconds;
    }

    public List<Song> getPlaylistSongs() {
        return playlistSongs;
    }

    public void setPlaylistSongs(List<Song> playlistSongs) {
        this.playlistSongs = playlistSongs;
    }

    public boolean isPlaylistInitialized() {
        return playlistInitialized;
    }

    public void setPlaylistInitialized(boolean playlistInitialized) {
        this.playlistInitialized = playlistInitialized;
    }

    public List<StudyModule> getStudyModules() {
        return studyModules;
    }

    public void setStudyModules(List<StudyModule> studyModules) {
        this.studyModules = studyModules;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public List<StudyDay> getStudyHistory() {
        return studyHistory;
    }

    public void setStudyHistory(List<StudyDay> studyHistory) {
        this.studyHistory = studyHistory;
    }
}