package org.todo;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.modules.StudyModule;
import org.persistence.AppData;

import java.time.LocalDate;

public class TodoView {

    private TodoManager todoManager;
    private ComboBox<StudyModule> moduleSelector;
    private TextField titleField;
    private DatePicker dueDatePicker;
    private ComboBox<TaskPriority> prioritySelector;
    private ListView<Task> taskListView;
    private Label statusLabel;

    public VBox createTodoView(AppData appData, Runnable backAction) {
        Label title = new Label("Tasks");

        // Manager and list wiring
        if (todoManager == null) {
            todoManager = new TodoManager(appData);
        }

        titleField = new TextField();
        titleField.setPromptText("e.g. Complete Problem Sheet 3");

        moduleSelector = new ComboBox<>();
        moduleSelector.setPromptText("Select a module (optional)");
        reloadModules(appData);

        Button clearModuleButton = new Button("Clear Module");
        clearModuleButton.setOnAction(e -> moduleSelector.getSelectionModel().clearSelection());

        dueDatePicker = new DatePicker();
        dueDatePicker.setPromptText("Optional due date");

        prioritySelector = new ComboBox<>();
        prioritySelector.getItems().addAll(TaskPriority.EXTRA_IMPORTANT, TaskPriority.MODERATELY_IMPORTANT, TaskPriority.NOT_IMPORTANT);
        prioritySelector.setValue(TaskPriority.MODERATELY_IMPORTANT);

        Button addButton = new Button("Add Task");
        Button removeButton = new Button("Remove Selected Task");
        Button backButton = new Button("Back");

        statusLabel = new Label("");

        taskListView = new ListView<>();
        taskListView.setItems(todoManager.getTasks());
        taskListView.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(Task item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    CheckBox cb = new CheckBox();
                    cb.setSelected(item.isCompleted());
                    cb.selectedProperty().addListener((obs, was, is) -> {
                        todoManager.setTaskCompleted(item, is);
                    });
                    String topLine = item.getTitle();
                    if (item.getModuleCode() != null && !item.getModuleCode().isBlank()) {
                        topLine += " — " + item.getModuleCode();
                    }
                    Label top = new Label(topLine);

                    String dueLine;
                    LocalDate due = item.getDueDate();
                    if (due != null) {
                        dueLine = "Due: " + due.toString();
                    } else {
                        dueLine = "No due date";
                    }
                    Label bottom = new Label(dueLine + "    Priority: " + item.getPriority().displayName());

                    VBox textBox = new VBox(2, top, bottom);
                    HBox row = new HBox(10, cb, textBox);
                    row.setAlignment(Pos.CENTER_LEFT);
                    setGraphic(row);
                    setText(null);
                }
            }
        });

        addButton.setOnAction(e -> {
            String titleText = titleField.getText() == null ? "" : titleField.getText().trim();
            if (titleText.isBlank()) {
                statusLabel.setText("Please enter a task title");
                return;
            }
            StudyModule selected = moduleSelector.getSelectionModel().getSelectedItem();
            String moduleCode = selected == null ? null : selected.getModuleCode();
            LocalDate due = dueDatePicker.getValue();
            TaskPriority pr = prioritySelector.getValue() == null ? TaskPriority.MODERATELY_IMPORTANT : prioritySelector.getValue();
            Task task = new Task(titleText, false, moduleCode, due, pr);
            todoManager.addTask(task);
            statusLabel.setText("Task added");
            titleField.clear();
            dueDatePicker.setValue(null);
            moduleSelector.getSelectionModel().clearSelection();
            prioritySelector.setValue(TaskPriority.MODERATELY_IMPORTANT);
        });

        removeButton.setOnAction(e -> {
            Task selected = taskListView.getSelectionModel().getSelectedItem();
            if (selected == null) {
                statusLabel.setText("Please select a task to remove");
                return;
            }
            todoManager.removeTask(selected);
            statusLabel.setText("Task removed");
        });

        backButton.setOnAction(e -> {
            if (backAction != null) backAction.run();
        });

        VBox layout = new VBox(10,
                title,
                new Label("Task:"),
                titleField,
                new Label("Module:"),
                moduleSelector,
                clearModuleButton,
                new Label("Due Date:"),
                dueDatePicker,
                new Label("Priority:"),
                prioritySelector,
                addButton,
                new Separator(),
                taskListView,
                removeButton,
                statusLabel,
                backButton
        );
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);
        return layout;
    }

    // Called before showing, to reflect latest modules and tasks
    public void refresh(AppData appData) {
        reloadModules(appData);
        // The list view is already bound to manager's list, which was constructed from AppData.
        // If tasks in AppData were changed elsewhere in the future, a re-sync method could be added.
    }

    private void reloadModules(AppData appData) {
        if (moduleSelector == null) return;
        moduleSelector.getItems().clear();
        if (appData != null && appData.getStudyModules() != null) {
            moduleSelector.getItems().addAll(appData.getStudyModules());
        }
    }
}
