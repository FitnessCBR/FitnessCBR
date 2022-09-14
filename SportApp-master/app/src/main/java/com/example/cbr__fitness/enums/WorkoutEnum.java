package com.example.cbr__fitness.enums;

import java.util.HashMap;
import java.util.Map;

public enum WorkoutEnum {

    Advanced(1, "advanced"),
    Beginner(2, "beginner"),
    Pro(3, "pro");

    private int ID;

    private String label;

    private static Map<Integer, WorkoutEnum> workoutEnumMap = new HashMap<>();

    private static Map<String, WorkoutEnum> workoutEnumMapString = new HashMap<>();

    static {
        for (WorkoutEnum e : WorkoutEnum.values()) {
            workoutEnumMap.put(e.getID(), e);
        }
    }

    static {
        for (WorkoutEnum e : WorkoutEnum.values()) {
            workoutEnumMapString.put(e.getLabel(), e);
        }
    }

    WorkoutEnum(int ID, String label) {
        this.ID = ID;
        this.label = label;
    }

    public int getID(){
        return ID;
    }

    public String getLabel() {
        return label;
    }

    public static WorkoutEnum getEnumById (int id) {
        return workoutEnumMap.get(id);
    }

    public static WorkoutEnum getEnumByString (String label) {
        return workoutEnumMapString.get(label);
    }
}
