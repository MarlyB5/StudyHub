package org.modules;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.persistence.AppData;

import java.util.ArrayList;
import java.util.List;

public class ModuleManager {

    private final ObservableList<StudyModule> modules;
    private final AppData appData;

    public ModuleManager(AppData appData) {
        this.appData = appData;
        this.modules = FXCollections.observableArrayList();

        if (appData != null) {
            List<StudyModule> saved = appData.getStudyModules();
            if (saved == null) {
                saved = new ArrayList<>();
                appData.setStudyModules(saved);
            }
            modules.addAll(saved);
        }
    }

    public ObservableList<StudyModule> getModules() {
        return modules;
    }

    private void syncModulesToAppData() {
        if (appData != null) {
            appData.setStudyModules(new ArrayList<>(modules));
        }
    }

    public void addModule(StudyModule module) {
        if (module == null) return;
        if (moduleExists(module.getModuleCode())) return;
        modules.add(module);
        syncModulesToAppData();
    }

    public void removeModule(StudyModule module) {
        if (module == null) return;
        modules.remove(module);
        syncModulesToAppData();
    }

    public boolean moduleExists(String moduleCode) {
        if (moduleCode == null || moduleCode.isBlank()) return false;
        for (StudyModule module : modules) {
            String code = module.getModuleCode();
            if (code != null && code.equalsIgnoreCase(moduleCode)) {
                return true;
            }
        }
        return false;
    }
}