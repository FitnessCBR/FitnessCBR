package com.example.cbr__fitness.data;

/**
 * @author Jobst-Julius Bartels
 */

// Datenklasse für den Plan eines Benutzers.
public class Plan {

    //Membervariablen.
    String pName;
    String pEx;
    String pTime;
    String musclePrio;
    String pRating;
    ExerciseList pExList;

    // Enumeration.
    public enum MusclePrio {
        FullBody, UpperBody, LowerBody, Chest, Shoulders, Back, Legs, Arms, Abs
    }
    MusclePrio musclePrioEnum;

    // Default-Konstruktor.
    public Plan() {

    }

    // Konstruktor.
    public Plan(String pName, String pEx, String pTime,String pRating ,ExerciseList pExList, String musclePrio) {
        this.pName = pName;
        this.pEx = pEx;
        this.pTime = pTime;
        this.pRating = pRating;
        this.pExList = pExList;
        this.musclePrio = musclePrio;

    }

    // Setter_Methoden.
    public void setpName(String pName) {
        this.pName = pName;
    }
    public void setpEx(String pEx) {
        this.pEx = pEx;
    }
    public void setpTime(String pTime) {
        this.pTime = pTime;
    }
    public void setpRating(String pRating) {
        this.pRating = pRating;
    }
    public void setpExList(ExerciseList pExList) {
        this.pExList = pExList;
    }
    public void setMusclePrio(String musclePrio) {
        this.musclePrio = musclePrio;
    }

    // Getter-Methoden.
    public String getpName() {
        return pName;
    }
    public String getpEx() {
        return this.pEx;
    }
    public String getpTime() {
        return pTime;
    }
    public String getpRating() {
        return pRating;
    }
    public ExerciseList getpExList() {
        return pExList;
    }
    public MusclePrio getMusclePrioEnum() {
        if(this.musclePrio.matches("FullBody")) {
            this.musclePrioEnum = MusclePrio.FullBody;
        }  else if(this.musclePrio.matches("UpperBody")) {
            this.musclePrioEnum = MusclePrio.UpperBody;
        }  else if(this.musclePrio.matches("Shoulders")) {
            this.musclePrioEnum = MusclePrio.Shoulders;
        } else if(this.musclePrio.matches("Chest")) {
            this.musclePrioEnum = MusclePrio.Chest;
        } else if(this.musclePrio.matches("Arms")) {
            this.musclePrioEnum = MusclePrio.Arms;
        } else if(this.musclePrio.matches("Back")) {
            this.musclePrioEnum = MusclePrio.Back;
        } else if(this.musclePrio.matches("Abs")) {
            this.musclePrioEnum = MusclePrio.Abs;
        } else if(this.musclePrio.matches("LowerBody")) {
            this.musclePrioEnum = MusclePrio.LowerBody;
        }else if(this.musclePrio.matches("Legs")) {
            this.musclePrioEnum = MusclePrio.Legs;
        }
        return this.musclePrioEnum;
    }
    public int getMusclePrioInt() {
        int spinnerNumber = 0;
        if(this.musclePrio.matches("FullBody")) {
            spinnerNumber = 1;
        } else if(this.musclePrio.matches("UpperBody")) {
            spinnerNumber = 2;
        } else if(this.musclePrio.matches("LowerBody")) {
            spinnerNumber = 3;
        } else if(this.musclePrio.matches("Chest")) {
            spinnerNumber = 4;
        } else if(this.musclePrio.matches("Shoulders")) {
            spinnerNumber = 5;
        } else if(this.musclePrio.matches("Back")) {
            spinnerNumber = 6;
        } else if(this.musclePrio.matches("Legs")) {
            spinnerNumber = 7;
        } else if(this.musclePrio.matches("Arms")) {
            spinnerNumber = 8;
        } else if(this.musclePrio.matches("Abs")) {
            spinnerNumber = 9;
        }
        return spinnerNumber;
    }
    public String getMusclePrio() {
        return musclePrio;
    }

    // Print-Methode eines Plans.
    public String pToString() {
        String pListString = "";
        StringBuilder sb = new StringBuilder();
            sb.append("Plan [pName="+ this.getpName() + "]");
            sb.append("[pEx=" + this.getpEx() +"]");
            sb.append("[pTime=" + this.pTime +"]");
            sb.append("[pMusclePrio=" + this.musclePrio +"]");
            sb.append("[pRating=" + this.pRating +"]");
            sb.append("\n");
            for (int i = 0; i < this.getpExList().size(); i++) {
                sb.append(this.getpExList().get(i).exToString());
            }
        pListString = sb.toString();
        return pListString;
    }

}
