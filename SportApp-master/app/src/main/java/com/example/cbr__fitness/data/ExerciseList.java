package com.example.cbr__fitness.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.cbr__fitness.enums.EquipmentEnum;
import com.example.cbr__fitness.enums.GoalEnum;
import com.example.cbr__fitness.enums.MuscleGroupEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jobst-Julius Bartels
 */

// Datenklasse für die Exercise-Liste.
public class ExerciseList extends ArrayList<Exercise> implements Parcelable {

    private int plan_id;

    private String plan_name;

    private int goal;

    private int muscle_group;
    /**
     * Only used and set for tracking of CBR results during testing.
     */
    private int uID;

    private List<Exercise> exercises;

    // Default-Konstruktor.
    public ExerciseList () {

    }

    public ExerciseList (int plan_id, int goal, int muscle_group, String name) {
        exercises = new ArrayList<>();
        this.plan_id = plan_id;
        this.goal = goal;
        this.muscle_group = muscle_group;
        this.plan_name = name;
    }
    public ExerciseList(String name) {
        this.plan_name = name;
    }

    protected ExerciseList(Parcel in) {
        plan_id = in.readInt();
        plan_name = in.readString();
        goal = in.readInt();
        muscle_group = in.readInt();
    }

    public static final Creator<ExerciseList> CREATOR = new Creator<ExerciseList>() {
        @Override
        public ExerciseList createFromParcel(Parcel in) {
            return new ExerciseList(in);
        }

        @Override
        public ExerciseList[] newArray(int size) {
            return new ExerciseList[size];
        }
    };

    public void setPlan_name(String plan_name) {
        this.plan_name = plan_name;
    }

    public String getPlan_name() {
        return plan_name;
    }

    public GoalEnum getGoal() {return GoalEnum.getEnumByID(goal);}

    public void setGoal(int goal) {this.goal = goal;}

    public int getPlan_id() {
        return plan_id;
    }

    public void setPlan_id(int plan_id) {
        this.plan_id = plan_id;
    }

    public void setuID (int uID) {
        this.uID = uID;
    }

    public int getuID(){
        return uID;
    }

    public String toLogOutput() {
        return "ID: " + plan_id + " NAME: " + plan_name + " USER: " + uID + " MUSCLE: "
                + getMuscle_group().getLabel() + " GOAL: " + getGoal().getLabel() + " EQUIPMENT: "
                + neededEquipmentToString();
    }

    public MuscleGroupEnum getMuscle_group() {
        return MuscleGroupEnum.getEnumByID(muscle_group);
    }

    public void setMuscle_group(int muscle_group) {
        this.muscle_group = muscle_group;
    }

    public String neededEquipmentToString () {
        StringBuilder builder = new StringBuilder();
        for (EquipmentEnum e :getNeededEquipment()) {
            builder.append(", " + e.getLabel());
        }

        return builder.toString();
    }

    public int getDuration() {
        int duration = 0;
        for (Exercise e : exercises) {
            duration += e.getDuration();
        }
        return duration;
    }

    public List<EquipmentEnum> getNeededEquipment () {
        List<EquipmentEnum> equipment  = new ArrayList<>();

        for (Exercise e : exercises) {
            equipment.add(e.getEquipment());
        }

        return equipment;
    }

    public List<Integer> getAllExerciseIDs () {
        List<Integer> allIds = new ArrayList<>();

        for (Exercise e : exercises) {
            allIds.add(e.getExerciseID());
        }

        return allIds;
    }

    public List<Exercise> getExercises() {
        return exercises;
    }

    public void addExercise(Exercise e) {
        exercises.add(e);
    }

    // Methode zur Überprüfung, ob eine Exercise existiert.
    public boolean exExists(String exName) {
        boolean exists = false;
        for (int i = 0; i < this.size(); i++) {
            if (this.get(i).getExName().equalsIgnoreCase(exName)) {
                exists = true;
            }
        }
        return exists;
    }

    // Methode zur Anfrage einer bestimmten Exercise.
    public Exercise getCertainExercise(String name) {
        Exercise exercise = new Exercise();
        for(int i = 0; i < this.size(); i++) {
            if(this.get(i).getExName().equalsIgnoreCase(name)) {
                exercise = this.get(i);
            }
        }
        return exercise;
    }

    // Methode zur Bestimmung der gesamten Zeit eines Plans.
    public int getTotalPlanTime () {
        int time = 0;
        for ( int i = 0; i < this.size(); i++) {
            time+= Integer.valueOf(this.get(i).getExTime());
        }
        return time;
    }

    // Methode die eine bestimmte Exercise entfernt.
    public void removeExercise(String pName) {
        for (int i = 0; i < this.size(); i++) {
            if (this.get(i).getExName().matches(pName)) {
                this.remove(this.get(i));
            }
        }
    }

    // Print-Methode der exerciseList.
    public String exListToString() {
        String exListString = "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.size(); i++) {
            sb.append("Exercise [exName="+ this.get(i).getExName() + "]");
            sb.append("[exSetNumber=" + this.get(i).getExSetNumber() +"]");
            sb.append("[exRep=" + this.get(i).getExRep() +"]");
            sb.append("[exBreak=" + this.get(i).getExBreak() +"]");
            sb.append("[exWeight=" + this.get(i).getExWeight() +"]");
            sb.append("[exMuscle=" + this.get(i).getExMuscle() +"]");
            sb.append("[exTime=" + this.get(i).getExTime() +"]");
            sb.append("[exType=" + this.get(i).getExType() +"]");
            sb.append("[exRating=" + this.get(i).getExRating() +"]");
            sb.append("\n");
        }
        exListString = sb.toString();
        return exListString;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(plan_id);
        dest.writeString(plan_name);
        dest.writeInt(goal);
        dest.writeInt(muscle_group);
    }
}
