package com.example.cbr__fitness.enums;

import com.example.cbr__fitness.interfaces.EnumInterface;

import java.util.HashMap;
import java.util.Map;

/**
 * This class shows the different goals a training can aim to achieve, this does not correlate to
 * trained muscle groups.
 */
public enum GoalEnum implements EnumInterface {
    MAX_STRENGTH(1, "Maximalkraft"),
    STRENGTH_ENDURANCE(2, "Kraftausdauer"),
    MUSCLE_MASS(3, "Muskelmasse"),
    POWER (4, "Power")
    ;

    private  int ID;

    private String label;

    private static Map<Integer, GoalEnum> goalEnumMap = new HashMap<>();

    static {
        for (GoalEnum goalEnum : GoalEnum.values()) {
            goalEnumMap.put(goalEnum.getID(), goalEnum);
        }
    }


    GoalEnum(int ID, String label) {
        this.ID = ID;
        this.label = label;
    }

    public int getID(){
        return ID;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public EnumInterface[] valuesList() {
        return GoalEnum.values();
    }

    public static GoalEnum getEnumByID(int id){
        return goalEnumMap.get(id);
    }
}
