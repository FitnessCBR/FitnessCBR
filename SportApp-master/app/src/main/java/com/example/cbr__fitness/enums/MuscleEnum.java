package com.example.cbr__fitness.enums;

import com.example.cbr__fitness.interfaces.EnumInterface;

import java.util.HashMap;
import java.util.Map;

public enum MuscleEnum implements EnumInterface {

    BICEPS(1, "Biceps", "biceps"),
    TRICEPS(2, "Triceps", "triceps"),
    FOREARMS(3, "Unterarme", "forearms"),
    CHEST(4, "Brust", "chest"),
    ABS(5, "Bauchmuskeln", "abdominals"),
    FRONTAL_SHOULDER(6, "Vordere Schultern", "frontal_shoulders"),
    CALVES(7, "Waden", "calves"),
    HAMSTRINGS(8, "Hinterer Oberschenkel", "hamstrings"),
    QUADRICEPS(9, "Vorderer Oberschenkel", "quadriceps"),
    GLUTS(10, "Gluteus", "glutes"),
    UPPER_BACK(11, "Oberer Ruecken", "upper_back"),
    MIDDLE_BACK(15, "Mittlerer Rücken", "middle_back"),
    LOWER_BACK(12, "Unterer Ruecken", "lower_back"),
    SIDE_ABS(13, "Seitliche Bauchmuskeln", "side_abs"),
    NONE(14, "Keine Auswahl", "none"),
    BACK_SHOULDER(15, "Hintere Schultern", "back_shoulders")
    ;

    private static Map<Integer, MuscleEnum> muscleEnumMap = new HashMap<>();

    static {
        for (MuscleEnum muscleEnum : MuscleEnum.values()) {
            muscleEnumMap.put(muscleEnum.getID(), muscleEnum);
        }
    }

    private int ID;

    private String label;

    private String symbol;

    /**
     * Constructor to enable the association of integers and enum values.
     * @param ID
     */
    MuscleEnum(int ID, String label, String symbol) {
        this.ID = ID;
        this.label = label;
        this.symbol = symbol;
    }

    public int getID(){
        return ID;
    }

    @Override
    public String getLabel() {
        return this.label;
    }

    public String getSymbol() {
        return symbol;
    }

    @Override
    public EnumInterface[] valuesList() {
        return MuscleEnum.values();
    }

    public static MuscleEnum getEnumByInt (int index) {
        return muscleEnumMap.get(index);
    }
}
