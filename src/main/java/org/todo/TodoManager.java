package org.todo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.persistence.AppData;

import java.util.ArrayList;
import java.util.List;

public class TodoManager {

    private final ObservableList<Task> tasks;
    private final AppData appData;

    public TodoManager(AppData appData) {
        this.appData = appData;
        this.tasks = FXCollections.observableArrayList();

        if (appData != null) {
            List<Task> saved = appData.getTasks();
            if (saved == null) {
                saved = new ArrayList<>();
                appData.setTasks(saved);
            }
            // Normalize legacy tasks: ensure priority non-null (defaults to MODERATELY_IMPORTANT)
            for (Task t : saved) {
                if (t != null) {
                    t.setPriority(t.getPriority());
                }
            }
            tasks.addAll(saved);
        }
    }

    public ObservableList<Task> getTasks() {
        return tasks;
    }

    private void syncTasksToAppData() {
        if (appData != null) {
            appData.setTasks(new ArrayList<>(tasks));
        }
    }

    public void addTask(Task task) {
        if (task == null) return;
        tasks.add(task);
        syncTasksToAppData();
    }

    public void removeTask(Task task) {
        if (task == null) return;
        tasks.remove(task);
        syncTasksToAppData();
    }

    public void setTaskCompleted(Task task, boolean completed) {
        if (task == null) return;
        task.setCompleted(completed);
        // No need to replace item; mutate existing so ListView reflects; then sync
        syncTasksToAppData();
    }
}
