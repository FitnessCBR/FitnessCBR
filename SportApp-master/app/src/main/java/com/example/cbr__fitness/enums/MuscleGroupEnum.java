package com.example.cbr__fitness.enums;

import com.example.cbr__fitness.interfaces.EnumInterface;

import java.util.HashMap;
import java.util.Map;

public enum MuscleGroupEnum implements EnumInterface {
    ARMS(1,"Arme"),
    CHEST(2, "Brust"),
    SHOULDER(3, "Schultern"),
    ABS(4, "Bachmuskeln"),
    LEGS(5, "Beine"),
    BACK(6, "Rücken"),
    UPPER_BODY(8, "Oberkörper"),
    LOWER_BODY(9, "Unterkörper"),
    WHOLE(7, "Ganzkörper");

    private String label;

    private int ID;

    private static Map<Integer, MuscleGroupEnum> muscleGroupEnumMap = new HashMap<>();

    static {
        for (MuscleGroupEnum m : MuscleGroupEnum.values()) {
            muscleGroupEnumMap.put(m.getID(), m);
        }
    }

    MuscleGroupEnum (int ID, String label) {
        this.ID = ID;
        this.label = label;
    }

    public int getID() {
        return ID;
    }

    public String getLabel() {
        return label;
    }

    public static MuscleGroupEnum getEnumByID(int id){
        return muscleGroupEnumMap.get(id);
    }

    @Override
    public EnumInterface[] valuesList() {
        return MuscleGroupEnum.values();
    }

}
