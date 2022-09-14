package com.example.cbr__fitness.data;

import java.util.ArrayList;

/**
 * @author Jobst-Julius Bartels
 */

// Datenklasse für die Plan-Liste.
public class PlanList extends ArrayList<Plan> {

    // Default-Konstruktor.
    public PlanList() {

    }

    // Methode zur Überprüfung, ob ein Plan existiert.
    public boolean pExists(String PName) {
        boolean exists = false;
        for (int i = 0; i < this.size(); i++) {
            if (this.get(i).getpName().equalsIgnoreCase(PName)) {
                exists = true;
            }
        }
        return exists;
    }

    // Methode die einen bestimmten Plan entfernt.
    public void removePlan(String pName) {
        for (int i = 0; i < this.size(); i++) {
            if (this.get(i).getpName().matches(pName)) {
                this.remove(this.get(i));
            }
        }
    }

    // Methode zur Anfrage eines bestimmten Plan.
    public Plan getCertainPlan(String name) {
        Plan plan = new Plan();
        for(int i = 0; i < this.size(); i++) {
            if(this.get(i).getpName().equalsIgnoreCase(name)) {
                plan = this.get(i);
            }
        }
        return plan;
    }

    // Print-Methode der planList.
    public String pListToString() {
        String pListString = "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.size(); i++) {
            sb.append("Plan [pName="+ this.get(i).getpName() + "]");
            sb.append("[pEx="+ this.get(i).getpEx() + "]");
            sb.append("[pTime=" + this.get(i).pTime +"]");
            sb.append("[pMusclePrio=" + this.get(i).musclePrio +"]");
            sb.append("[pRating=" + this.get(i).pRating +"]");
            sb.append("\n");
            for (int j = 0; j < this.get(i).getpExList().size(); j++) {
                sb.append(this.get(i).getpExList().get(j).exToString());
            }
        }
        pListString = sb.toString();
        return pListString;
    }
}
