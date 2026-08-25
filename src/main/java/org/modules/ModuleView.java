package org.modules;

import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import javafx.scene.layout.VBox;
import org.persistence.AppData;

public class ModuleView {

    private ModuleManager moduleManager;

    public VBox createModuleView(
            AppData appData,
            Runnable backAction
    ) {

        Label titleLabel =
                new Label("Study Modules");

        Label moduleCodeLabel =
                new Label("Module Code");

        TextField moduleCodeField =
                new TextField();

        moduleCodeField.setPromptText(
                "e.g. COMP30860"
        );

        Label moduleNameLabel =
                new Label("Module Name");

        TextField moduleNameField =
                new TextField();

        moduleNameField.setPromptText(
                "e.g. Web Development"
        );


        Label autonomousHoursLabel =
                new Label(
                        "Autonomous Study Hours"
                );

        TextField autonomousHoursField =
                new TextField();

        autonomousHoursField.setPromptText(
                "e.g. 75"
        );

        Label contactHoursLabel =
                new Label("Contact Hours");

        TextField contactHoursField =
                new TextField();

        contactHoursField.setPromptText(
                "e.g. 25"
        );


        CheckBox includeContactCheckBox =
                new CheckBox(
                        "Include contact hours in target"
                );

        Button searchUcdButton =
                new Button("Search UCD");

        Button addButton =
                new Button("Add Module");

        Button removeButton =
                new Button("Remove Module");

        Button backButton =
                new Button("Back");

        Label statusLabel =
                new Label("");

        ListView<StudyModule> moduleList =
                new ListView<>();

        if (moduleManager == null) {
            moduleManager = new ModuleManager(appData);
        }

        moduleList.setItems(
                moduleManager.getModules()
        );

        moduleList.setPrefHeight(200);

        searchUcdButton.setOnAction(event -> {

            String moduleCode =
                    moduleCodeField
                            .getText()
                            .trim();

            if (moduleCode.isBlank()) {

                statusLabel.setText(
                        "Please enter a module code"
                );

                return;
            }


            UCDModuleService service =
                    new UCDModuleService();

            UCDModuleData data =
                    service.findModule(
                            moduleCode
                    );

            if (data == null) {

                statusLabel.setText(
                        "Could not find module"
                );

                return;
            }

            moduleCodeField.setText(
                    data.getModuleCode()
            );

            moduleNameField.setText(
                    data.getModuleName()
            );

            autonomousHoursField.setText(
                    String.valueOf(
                            data.getAutonomousHours()
                    )
            );

            contactHoursField.setText(
                    String.valueOf(
                            data.getNonAutonomousHours()
                    )
            );

            statusLabel.setText(
                    "Module found"
            );
        });

        addButton.setOnAction(event -> {

            try {

                // Get text from input fields

                String code =
                        moduleCodeField
                                .getText()
                                .trim();

                String name =
                        moduleNameField
                                .getText()
                                .trim();


                // Convert hour text into numbers

                double autonomousHours =
                        Double.parseDouble(
                                autonomousHoursField
                                        .getText()
                        );

                double contactHours =
                        Double.parseDouble(
                                contactHoursField
                                        .getText()
                        );


                // Check checkbox

                boolean includeContact =
                        includeContactCheckBox
                                .isSelected();

                if (
                        code.isBlank()
                                || name.isBlank()
                ) {

                    statusLabel.setText(
                            "Please enter a module code and name"
                    );

                    return;
                }


                if (
                        autonomousHours < 0
                                || contactHours < 0
                ) {

                    statusLabel.setText(
                            "Hours cannot be negative"
                    );

                    return;
                }


                if (
                        moduleManager
                                .moduleExists(code)
                ) {

                    statusLabel.setText(
                            "That module has already been added"
                    );

                    return;
                }

                StudyModule module =
                        new StudyModule(
                                name,
                                code,
                                includeContact,
                                autonomousHours,
                                contactHours,
                                0
                        );


                moduleManager.addModule(
                        module
                );


                statusLabel.setText(
                        "Module added"
                );

                moduleCodeField.clear();

                moduleNameField.clear();

                autonomousHoursField.clear();

                contactHoursField.clear();

                includeContactCheckBox
                        .setSelected(false);


            } catch (
                    NumberFormatException exception
            ) {

                statusLabel.setText(
                        "Please enter valid numbers for hours"
                );
            }
        });

        removeButton.setOnAction(event -> {

            StudyModule selected =
                    moduleList
                            .getSelectionModel()
                            .getSelectedItem();


            if (selected == null) {

                statusLabel.setText(
                        "Please select a module to remove"
                );

                return;
            }


            moduleManager.removeModule(
                    selected
            );


            statusLabel.setText(
                    "Module removed"
            );
        });

        // BACK BUTTON

        backButton.setOnAction(event -> {

            if (backAction != null) {

                backAction.run();
            }
        });


        // LAYOUT
        VBox layout =
                new VBox(
                        10,

                        titleLabel,

                        moduleCodeLabel,
                        moduleCodeField,
                        searchUcdButton,

                        moduleNameLabel,
                        moduleNameField,

                        autonomousHoursLabel,
                        autonomousHoursField,

                        contactHoursLabel,
                        contactHoursField,

                        includeContactCheckBox,

                        addButton,
                        removeButton,

                        statusLabel,

                        moduleList,

                        backButton
                );


        layout.setPadding(
                new Insets(20)
        );

        layout.setAlignment(
                Pos.CENTER
        );


        return layout;
    }
}