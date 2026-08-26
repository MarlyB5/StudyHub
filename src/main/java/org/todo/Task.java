package org.todo;

import java.time.LocalDate;

// Simple Task model for To-Do feature
public class Task {
    private String title;
    private boolean completed;
    private String moduleCode; // optional association to a StudyModule by code

    // Store due date as ISO string (YYYY-MM-DD) for Gson compatibility
    private String dueDateIso; // nullable

    private TaskPriority priority; // nullable on legacy loads → treat as MODERATELY_IMPORTANT

    // No-arg constructor for Gson
    public Task() {
    }

    public Task(String title, boolean completed, String moduleCode) {
        this.title = title;
        this.completed = completed;
        this.moduleCode = moduleCode;
        this.priority = TaskPriority.MODERATELY_IMPORTANT; // sensible default
    }

    public Task(String title, boolean completed, String moduleCode, LocalDate dueDate, TaskPriority priority) {
        this.title = title;
        this.completed = completed;
        this.moduleCode = moduleCode;
        setDueDate(dueDate); // sets ISO or null
        this.priority = (priority == null) ? TaskPriority.MODERATELY_IMPORTANT : priority;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public String getModuleCode() {
        return moduleCode;
    }

    public void setModuleCode(String moduleCode) {
        this.moduleCode = moduleCode;
    }

    // Due date helpers
    public LocalDate getDueDate() {
        if (dueDateIso == null || dueDateIso.isBlank()) return null;
        try {
            return LocalDate.parse(dueDateIso);
        } catch (Exception e) {
            return null;
        }
    }

    public void setDueDate(LocalDate date) {
        this.dueDateIso = (date == null) ? null : date.toString();
    }

    public String getDueDateIso() {
        return dueDateIso;
    }

    public void setDueDateIso(String iso) {
        this.dueDateIso = (iso == null || iso.isBlank()) ? null : iso;
    }

    public TaskPriority getPriority() {
        return priority == null ? TaskPriority.MODERATELY_IMPORTANT : priority;
    }

    public void setPriority(TaskPriority priority) {
        this.priority = priority == null ? TaskPriority.MODERATELY_IMPORTANT : priority;
    }

    @Override
    public String toString() {
        String prefix = (completed ? "\u2611 " : "\u2610 ");
        String base = prefix + title + ((moduleCode == null || moduleCode.isBlank()) ? "" : " — " + moduleCode);
        TaskPriority pr = getPriority();
        String prText = (pr != null) ? (" [" + pr.displayName() + "]") : "";
        return base + prText;
    }
}
