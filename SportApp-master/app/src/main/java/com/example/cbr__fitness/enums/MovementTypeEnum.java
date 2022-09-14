package com.example.cbr__fitness.enums;

import java.util.HashMap;

public enum MovementTypeEnum {

    PUSH(1, "push"),
    PULL(2, "pull"),
    ISOMETRIC(3, "isometric");


    private int id;

    private String label;

    private static HashMap<Integer, MovementTypeEnum> movementTypeEnumList = new HashMap<>();

    static {
        for (MovementTypeEnum e : MovementTypeEnum.values()) {
            movementTypeEnumList.put(e.getId(), e);
        }
    }

    MovementTypeEnum(int id, String label){
        this.id = id;
        this.label = label;
    }

    public  String getLabel() {
        return label;
    }

    public int getId() {
        return id;
    }

    public static MovementTypeEnum getEnumById(int id) {
        return movementTypeEnumList.get(id);
    }
}
