package com.example.cbr__fitness.enums;

import java.util.HashMap;
import java.util.Map;

public enum GenderEnum {

    Female(2, "female"),
    Male(1, "male"),
    Diverse(3, "diverse");

    private int ID;

    private String label;

    private static Map<Integer, GenderEnum> genderEnumMap = new HashMap<>();

    private static Map<String, GenderEnum> genderEnumMapString = new HashMap<>();

    static {
        for (GenderEnum e : GenderEnum.values()) {
            genderEnumMap.put(e.getID(), e);
        }
    }

    static {
        for (GenderEnum e : GenderEnum.values()) {
            genderEnumMapString.put(e.getLabel(), e);
        }
    }

    GenderEnum(int ID, String label) {
        this.ID = ID;
        this.label = label;
    }

    public int getID(){
        return ID;
    }

    public String getLabel() {
        return label;
    }

    public static GenderEnum getEnumById (int id) {
        return genderEnumMap.get(id);
    }

    public static GenderEnum getEnumByString (String label) {
        return genderEnumMapString.get(label);
    }
}
