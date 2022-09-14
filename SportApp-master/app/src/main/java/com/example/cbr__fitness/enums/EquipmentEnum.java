package com.example.cbr__fitness.enums;

import com.example.cbr__fitness.databasehelper.FitnessDBContract;
import com.example.cbr__fitness.interfaces.EnumInterface;

import java.util.HashMap;
import java.util.Map;

/**
 * Enums representing the different equipments exercises can require
 * ('Keins'), ('Fitness Studio Mitgliedschaft'), ('Hanteln'), ('Theraband'), ('Dumbells'), ('Klimmzugstange')
 *  ('Hantelbank'), ('Langhantel')
 */
public enum EquipmentEnum implements EnumInterface {

    KEINS(1, "Keins", "none"),
    FitnessStudio(2, "Fitness Studio", "fitness_studio"),
    Hantel(3, "Hantel", "hantel"),
    Theraband(4, "Theraband", "theraband"),
    KETTELBELL(5, "Kettelbell", "kettelbell"),
    Klimmzugstange(6, "Klimmzugstange", "klimmzugstange"),
    Hantelbank(7, "Hantelbank", "hantelbank"),
    Langhantel(8, "Langhantel", "langhantel"),
    DipBars(9, "Dip Bars", "dip_bars"),
    BENCH(10, "Bench", "bench")
    ;

    private static Map<Integer, EquipmentEnum> equipmentEnumMap = new HashMap<>();


    static {
        for (EquipmentEnum equipmentEnum : EquipmentEnum.values()) {
            equipmentEnumMap.put(equipmentEnum.getID(), equipmentEnum);
        }
    }

    private int ID;

    private String label;

    private  String symbol;

    EquipmentEnum(int ID, String label, String symbol) {
        this.ID = ID;
        this.label = label;
        this.symbol = symbol;
    }

    public int getID() {
        return ID;
    }

    public String getLabel() {
        return label;
    }

    public String getSymbol() {
        return symbol;
    }

    @Override
    public EnumInterface[] valuesList() {
        return  EquipmentEnum.values();
    }

    public static EquipmentEnum getEnumById(int ID) {
        return equipmentEnumMap.get(ID);
    }
}
