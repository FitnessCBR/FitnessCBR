package com.example.cbr__fitness.data;

/**
 * @author Jobst-Julius Bartels
 */

// Datenklasse für die Beschreibung eines Falls.
public class ResultCaseUser {

    // Variablen und Definition des Falls.
    private String caseName;
    private double sim;
    private Plan plan;
    private String age;
    private String gender;
    private String workType;
    private String duration;
    private String res;
    private String bodyType;

    // Default-Konstruktor.
    public ResultCaseUser(){

    }

    // Konstruktor.
    public ResultCaseUser(String caseName, double sim, Plan plan, String age, String gender, String workType, String bodyType, String duration, String res) {
        super();
        this.caseName = caseName;
        this.sim = sim;
        this.plan = plan;
        this.age = age;
        this.gender = gender;
        this.duration = duration;
        this.workType = workType;
        this.bodyType = bodyType;
        this.res = res;
    }


    // Setter-Methoden.
    public void setGender(String gender) {
        this.gender = gender;
    }
    public void setWorkType(String workType) {
        this.workType = workType;
    }
    public void setDuration(String duration) {
        this.duration = duration;
    }
    public void setRes(String res) {
        this.res = res;
    }
    public void setAge(String age) {
        this.age = age;
    }
    public void setCaseName(String caseName) {
        this.caseName = caseName;
    }
    public void setSim(double sim) {
        this.sim = sim;
    }
    public void setPlan(Plan plan) {
        this.plan = plan;
    }
    public void setBodyType(String bodyType) {
        this.bodyType = bodyType;
    }

    // Getter-Methoden.
    public String getCaseName() {
        return caseName;
    }
    public double getSim() {
        return sim;
    }
    public Plan getPlan() {
        return plan;
    }
    public String getAge() {
        return age;
    }
    public String getGender() {
        return gender;
    }
    public String getWorkType() {
        return workType;
    }
    public String getDuration() {
        return duration;
    }
    public String getRes() {
        return res;
    }
    public String getBodyType() {
        return bodyType;
    }

    // Print-Methode des Falls.
    public String ResultToString() {
        String cListString = "";
        StringBuilder sb = new StringBuilder();
        sb.append("Case [cName="+ this.getCaseName() + "]");
        sb.append("[cSim=" + this.getSim() +"]");
        sb.append("[cPlan=" + this.plan.getpName() +"]");
        sb.append("[cAge=" + this.age +"]");
        sb.append("[cGender=" + this.gender +"]");
        sb.append("[cDuration=" + this.duration +"]");
        sb.append("[cWorkType=" + this.workType +"]");
        sb.append("[cBodyType=" + this.bodyType +"]");
        sb.append("[cRes=" + this.res +"]");
        sb.append("\n");
        cListString = sb.toString();
        return cListString;
    }
}
