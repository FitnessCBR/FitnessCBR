package com.example.cbr__fitness.enums;

import java.util.HashMap;
import java.util.Map;

public enum ExerciseTypeEnum {

    multiJoint(2, "Multi Joint", "multiJoint"),
    singleJoint(1, "Single Joint", "singleJoint");

    private int ID;

    private String label;

    private String symbol;

    private static Map<Integer, ExerciseTypeEnum> exerciseTypeEnumMap = new HashMap<>();

    static {
        for (ExerciseTypeEnum e : ExerciseTypeEnum.values()) {
            exerciseTypeEnumMap.put(e.getID(), e);
        }
    }

    ExerciseTypeEnum(int ID, String label, String symbol) {
        this.ID = ID;
        this.label = label;
        this.symbol = symbol;
    }

    public static ExerciseTypeEnum getEnumByInt (int index) {
        return exerciseTypeEnumMap.get(index);
    }

    public int getID(){
        return ID;
    }

    public String getLabel(){ return label; }

    public String getSymbol() {
        return symbol;
    }
}
