package org.modules;

public class UCDModuleData {

    private final String moduleCode;
    private final String moduleName;

    private final double autonomousHours;
    private final double nonAutonomousHours;
    private final double totalHours;


    public UCDModuleData(
            String moduleCode,
            String moduleName,
            double autonomousHours,
            double nonAutonomousHours,
            double totalHours
    ) {
        this.moduleCode = moduleCode;
        this.moduleName = moduleName;
        this.autonomousHours = autonomousHours;
        this.nonAutonomousHours = nonAutonomousHours;
        this.totalHours = totalHours;
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


    public double getNonAutonomousHours() {
        return nonAutonomousHours;
    }


    public double getTotalHours() {
        return totalHours;
    }
}