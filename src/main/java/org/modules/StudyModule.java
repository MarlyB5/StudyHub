package org.modules;

public class StudyModule {

    private String moduleName;
    private String moduleCode;

    private boolean includeContactHours;

    private double autonomousHours;
    private double contactHours;

    private int studiedSeconds;

    public double getTargetHours() {

        if (includeContactHours) {

            return autonomousHours
                    + contactHours;
        }

        return autonomousHours;
    }

    public void addStudySeconds(int seconds) {

        if (seconds > 0) {
            studiedSeconds += seconds;
        }
    }

    public void addStudySecond() {
        studiedSeconds++;
    }
    public double getStudiedHours() {
        return studiedSeconds / 3600.0;
    }

    public double getProgress() {

        double targetHours =
                getTargetHours();

        if (targetHours <= 0) {
            return 0;
        }

        double progress =
                getStudiedHours()
                        / targetHours;

        return Math.min(
                progress,
                1.0
        );
    }

    @Override
    public String toString() {
        return moduleCode + " - " + moduleName;
    }

    public StudyModule(){

    }


    public StudyModule(String moduleName, String moduleCode, boolean includeContactHours, double autonomousHours, double contactHours, int studiedSeconds) {
        this.moduleName = moduleName;
        this.moduleCode = moduleCode;
        this.includeContactHours = includeContactHours;
        this.autonomousHours = autonomousHours;
        this.contactHours = contactHours;
        this.studiedSeconds = studiedSeconds;
    }

    public String getModuleCode() {
        return moduleCode;
    }

    public String getModuleName() {
        return moduleName;
    }

    public double getAutonomousHours() {
        return autonomousHours;
    }

    public double getContactHours() {
        return contactHours;
    }

    public boolean isIncludeContactHours() {
        return includeContactHours;
    }

    public int getStudiedSeconds() {
        return studiedSeconds;
    }
}
