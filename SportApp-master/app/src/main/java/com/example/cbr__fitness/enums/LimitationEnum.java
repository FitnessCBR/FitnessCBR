package com.example.cbr__fitness.enums;

import com.example.cbr__fitness.interfaces.EnumInterface;

import java.util.HashMap;
import java.util.Map;

public enum LimitationEnum implements EnumInterface {
    ELBOWS(1, "Ellenbogen"),
    WRISTS(2, "Handgelenke"),
    SHOULDER(3, "Schultern"),
    KNEES(4,"Knie"),
    SPINE(5, "Wirbelsäule"),
    HIPS(6, "Hüften"),
    PACEMAKER(7, "Herzschrittmacher"),
    DIABETES(8, "Diabetes")
    ;

    private int ID;

    private String label;

    private static Map<Integer, LimitationEnum> limitationEnumMap = new HashMap<>();

    static {
        for (LimitationEnum e : LimitationEnum.values()) {
            limitationEnumMap.put(e.getID(), e);
        }
    }

    LimitationEnum(int ID, String label) {
        this.ID = ID;
        this.label = label;
    }

    public static LimitationEnum getEnumByID (int id) {
        return limitationEnumMap.get(id);
    }

    @Override
    public int getID() {
        return ID;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public EnumInterface[] valuesList() {
        return LimitationEnum.values();
    }
}
