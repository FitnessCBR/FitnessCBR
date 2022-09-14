package com.example.cbr__fitness.data;

/**
 * @author Jobst-Julius Bartels
 */

// Datenklasse für die Beschreibung eines Falls.
public class ResultCaseExercise {

    // Variablen und Definition des Falls.
    private String caseName;
    private double sim;
    private Exercise exercise;

    // Konstruktor.
    public ResultCaseExercise(String caseName, double sim, Exercise exercise) {
        super();
        this.caseName = caseName;
        this.sim = sim;
        this.exercise = exercise;
    }

    // Setter-Methoden.
    public void setPlan(Exercise exercise) {
        this.exercise = exercise;
    }
    public void setCaseName(String caseName) {
        this.caseName = caseName;
    }
    public void setSim(double sim) {
        this.sim = sim;
    }

    // Getter-Methoden.
    public double getSim() {
        return sim;
    }
    public String getCaseName() {
        return caseName;
    }
    public Exercise getExercise() {
        return exercise;
    }

    // Print-Methode des Falls.
    public String ResultToString() {
        String cListString = "";
        StringBuilder sb = new StringBuilder();
        sb.append("Case [cName="+ this.getCaseName() + "]");
        sb.append("[cSim=" + this.getSim() +"]");
        sb.append("[cExName=" + this.exercise.getExName() +"]");
        sb.append("[cExMuscle=" + this.exercise.getExMuscle() +"]");
        sb.append("[cExBreak=" + this.exercise.getExBreak() +"]");
        sb.append("[cExRep=" + this.exercise.getExRep() +"]");
        sb.append("[cExSet=" + this.exercise.getExSetNumber() +"]");
        sb.append("[cExType=" + this.exercise.getExType() +"]");
        sb.append("[cExTime=" + this.exercise.getExTime() +"]");
        sb.append("[cExWeight=" + this.exercise.getExWeight() +"]");
        sb.append("[cExRating=" + this.exercise.getExRating() +"]");
        sb.append("\n");
        cListString = sb.toString();
        return cListString;
    }
}
