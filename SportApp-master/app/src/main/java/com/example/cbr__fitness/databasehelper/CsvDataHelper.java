package com.example.cbr__fitness.databasehelper;

import com.example.cbr__fitness.customListenerMethods.ToCsvButtonListener;
import com.example.cbr__fitness.data.Exercise;
import com.example.cbr__fitness.data.User;
import com.example.cbr__fitness.interfaces.ToCaseCsvInterface;

public class CsvDataHelper {

    public static final String userCsvName = "personCsv.csv";

    public static final String firstLinePersonCsv = "userID;age;gender;weight;height;BMI;training_type\n";

    public static final String exerciseCsvName = "exerciseCsv.csv";

    public static final String firstLineExerciseCsv = "exerciseID;primary_muscle;secondary_muscle" +
            ";is_multijoint;equipment;movement_type;is_explosive;is_bodyweight\n";

    public static String getFirstLine(ToCaseCsvInterface object) {
        if (object instanceof User) {
            return firstLinePersonCsv;
        } else {
            return firstLineExerciseCsv;
        }
    }
}
