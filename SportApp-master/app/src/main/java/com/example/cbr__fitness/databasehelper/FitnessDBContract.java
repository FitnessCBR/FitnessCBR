package com.example.cbr__fitness.databasehelper;

import android.provider.BaseColumns;

/**
 * This class stores the SQL commands for the SQLITE DB used for the local storage of the app
 */
public class FitnessDBContract {
    //to prevent instantiation
    private FitnessDBContract(){}

    public static class UserEntry implements BaseColumns {
        public static final String TABLE_NAME = "user";
        public static final String COLUMN_NAME_UID = "uid";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_PASSWORD = "password";
        public static final String COLUMN_NAME_AGE ="age";
        public static final String COlUMN_NAME_WEIGHT = "weight";
        public static final String COlUMN_NAME_HEIGHT = "height";
        public static final String COLUMN_NAME_GENDER = "gender";
        public static final String COLUMN_NAME_TRAINING_TYPE = "training_type";
        public static final String COLUMN_NAME_IS_ACTIVE = "is_active";
    }

    public static final String SQL_CREATE_USER = "CREATE TABLE " + UserEntry.TABLE_NAME
            + " (" + UserEntry.COLUMN_NAME_UID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            UserEntry.COLUMN_NAME_NAME + " TEXT NOT NULL DEFAULT 'Nameless'," +
            UserEntry.COLUMN_NAME_PASSWORD + " TEXT NOT NULL DEFAULT 'PWLess'," +
            UserEntry.COLUMN_NAME_AGE + " INTEGER NOT NULL DEFAULT(20)," +
            UserEntry.COlUMN_NAME_WEIGHT + " REAL NOT NULL DEFAULT(75)," +  //weight and height is used to recreate BMI on user creation
            UserEntry.COlUMN_NAME_HEIGHT + " REAL NOT NULL DEFAULT(175)," +
            UserEntry.COLUMN_NAME_GENDER + " INTEGER NOT NULL DEFAULT(1)," +    //0=male 1=female 2=diverse
            UserEntry.COLUMN_NAME_TRAINING_TYPE + " INTEGER NOT NULL DEFAULT(1)," + //0=amateur 1=intermediate 2=professional
            UserEntry.COLUMN_NAME_IS_ACTIVE + " INTEGER NOT NULL DEFAULT(0)" +
            ")";

    public static final String SQL_DELETE_USER = "DROP TABLE IF EXISTS " + UserEntry.TABLE_NAME;

    public static class ExerciseEntry implements BaseColumns {
        public static final String TABLE_NAME = "exercise";
        public static final String COLUMN_NAME_EID = "eid";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_PRIME_MUSCLE = "prime_muscle";
        public static final String COLUMN_NAME_SECONDARY_MUSCLE = "secondary_muscle";
        public static final String COLUMN_NAME_EXPLANATION = "explanation";
        public static final String COLUMN_NAME_EXERCISE_TYPE ="exercise_type";
        public static final String COLUMN_NAME_ILLUSTRATION_LINK = "illustration_link";
        public static final String COLUMN_NAME_EQUIPMENT = "equipment";
        public static final String COLUMN_NAME_DURATION_REP = "duration_rep";
        public static final String COLUMN_NAME_MOVEMENT_TYPE = "movement_type";
        public static final String COLUMN_NAME_IS_EXPLOSIVE = "is_explosive";
        public static final String COLUMN_NAME_IS_BODYWEIGHT = "is_bodyweight";
    }

    public static final String SQL_CREATE_EXERCISE = "CREATE TABLE " + ExerciseEntry.TABLE_NAME
            + " (" + ExerciseEntry.COLUMN_NAME_EID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            ExerciseEntry.COLUMN_NAME_TITLE + " TEXT NOT NULL DEFAULT 'NamelessExercise'," +
            ExerciseEntry.COLUMN_NAME_PRIME_MUSCLE + " INTEGER NOT NULL DEFAULT (1)," +
            ExerciseEntry.COLUMN_NAME_SECONDARY_MUSCLE + " INTEGER NOT NULL DEFAULT (1)," +
            ExerciseEntry.COLUMN_NAME_EXERCISE_TYPE + " INTEGER NOT NULL DEFAULT (1)," +
            ExerciseEntry.COLUMN_NAME_EXPLANATION + " TEXT," +
            ExerciseEntry.COLUMN_NAME_ILLUSTRATION_LINK + " TEXT," +
            ExerciseEntry.COLUMN_NAME_EQUIPMENT + " INTEGER NOT NULL DEFAULT (1)," +
            ExerciseEntry.COLUMN_NAME_DURATION_REP + " INTEGER NOT NULL DEFAULT (5)," +//as time in mm:ss
            ExerciseEntry.COLUMN_NAME_MOVEMENT_TYPE + " INTEGER NOT NULL DEFAULT (1)," +
            ExerciseEntry.COLUMN_NAME_IS_EXPLOSIVE + " INTEGER NOT NULL DEFAULT (0)," +
            ExerciseEntry.COLUMN_NAME_IS_BODYWEIGHT + " INTEGER NOT NULL DEFAULT (0)," +
            "FOREIGN KEY(" + ExerciseEntry.COLUMN_NAME_EQUIPMENT + ") REFERENCES " + EquipmentEntry.TABLE_NAME + "(" + EquipmentEntry.COLUMN_NAME_EQ_ID + ")"+
            ")";

    public static final String SQL_DELETE_EXERCISE = "DROP TABLE IF EXISTS " + ExerciseEntry.TABLE_NAME;

    public static class PlanExerciseRelationEntry implements BaseColumns {
        public static final String TABLE_NAME = "plan_exercise_rel";
        public static final String COLUMN_NAME_PID = "pid";
        public static final String COLUMN_NAME_EID = "eid";
        public static final String COLUMN_NAME_SETS = "sets";
        public static final String COLUMN_NAME_REPS = "reps";
        public static final String COLUMN_NAME_BREAK = "break";
        public static final String COLUMN_NAME_WEIGHT = "weight";
    }

    public static final String SQL_CREATE_PLAN_EXERCISE_RELATION = "CREATE TABLE " + PlanExerciseRelationEntry.TABLE_NAME
            + " (" +PlanExerciseRelationEntry.COLUMN_NAME_PID + " INTEGER NOT NULL," +
            PlanExerciseRelationEntry.COLUMN_NAME_EID + " INTEGER NOT NULL," +
            PlanExerciseRelationEntry.COLUMN_NAME_SETS + " INTEGER NOT NULL DEFAULT (3)," +
            PlanExerciseRelationEntry.COLUMN_NAME_REPS + " INTEGER NOT NULL DEFAULT (10)," +
            PlanExerciseRelationEntry.COLUMN_NAME_BREAK + " INTEGER NOT NULL DEFAULT (30)," + //in seconds
            PlanExerciseRelationEntry.COLUMN_NAME_WEIGHT + " INTEGER NOT NULL DEFAULT (0)," + //Integer where 0 represents body weight.
            "FOREIGN KEY(" + PlanExerciseRelationEntry.COLUMN_NAME_PID + ") REFERENCES " + PlanEntry.TABLE_NAME + "(" + PlanEntry.COLUMN_NAME_PID + "), " +
            "FOREIGN KEY(" + PlanExerciseRelationEntry.COLUMN_NAME_EID + ") REFERENCES " + ExerciseEntry.TABLE_NAME + "(" + ExerciseEntry.COLUMN_NAME_EID + "), " +
            "PRIMARY KEY (" + PlanExerciseRelationEntry.COLUMN_NAME_PID + ", " + PlanExerciseRelationEntry.COLUMN_NAME_EID + ")" +
            ")";

    public static final String SQL_DELETE_PLAN_EXERCISE_RELATION = "DROP TABLE IF EXISTS " + PlanExerciseRelationEntry.TABLE_NAME;

    public static class PlanEntry implements BaseColumns {
        public static final String TABLE_NAME = "plan";
        public static final String COLUMN_NAME_PID = "pid";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_GOAL = "goal";
        public static final String COLUMN_NAME_MUSCLE_GROUP = "muscle_group";
        public static final String COLUMN_NAME_RATING = "rating";
        public static final String COLUMN_NAME_DELETED = "deleted";
    }

    public static final String SQL_CREATE_PLAN = "CREATE TABLE " + PlanEntry.TABLE_NAME
            + " (" + PlanEntry.COLUMN_NAME_PID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            PlanEntry.COLUMN_NAME_NAME + " TEXT NOT NULL DEFAULT 'NamelessPlan'," +
            PlanEntry.COLUMN_NAME_GOAL + " INTEGER NOT NULL DEFAULT(0)," +
            PlanEntry.COLUMN_NAME_MUSCLE_GROUP + " INTEGER NOT NULL DEFAULT(0)," +
            PlanEntry.COLUMN_NAME_RATING + " INTEGER NOT NULL DEFAULT(3)," +
            PlanEntry.COLUMN_NAME_DELETED + " INTEGER NOT NULL DEFAULT(0)" +
            ")";

    public static final String SQL_DELETE_PLAN = "DROP TABLE IF EXISTS " + PlanEntry.TABLE_NAME;

    public static class UserPlanRelation implements BaseColumns {
        public static final String TABLE_NAME = "user_plan_rel";
        public static final String COLUMN_NAME_UID = "uid";
        public static final String COLUMN_NAME_PID = "pid";
    }

    public static final String SQL_CREATE_USER_PLAN_RELATION = "CREATE TABLE " + UserPlanRelation.TABLE_NAME
            + " (" +
            UserPlanRelation.COLUMN_NAME_UID + " INTEGER NOT NULL," +
            UserPlanRelation.COLUMN_NAME_PID + " INTEGER NOT NULL," +
            "FOREIGN KEY(" + UserPlanRelation.COLUMN_NAME_UID + ") REFERENCES " + UserEntry.TABLE_NAME + "(" + UserEntry.COLUMN_NAME_UID + "), " +
            "FOREIGN KEY(" + UserPlanRelation.COLUMN_NAME_PID + ") REFERENCES " + PlanEntry.TABLE_NAME + "(" + PlanEntry.COLUMN_NAME_PID + "), " +
            "PRIMARY KEY (" + UserPlanRelation.COLUMN_NAME_PID + ", " + UserPlanRelation.COLUMN_NAME_UID + ")" +
            ")";

    public static final String SQL_DELETE_USER_PLAN_RELATION = "DROP TABLE IF EXISTS " + UserPlanRelation.TABLE_NAME;

    public static class LimitationsEntry implements BaseColumns {
        public static final String TABLE_NAME = "limitations";
        public static final String COLUMN_NAME_LID = "lid";
        public static final String COLUMN_NAME_LIMITATION = "limitation";
    }

    public static final String SQL_CREATE_LIMITATIONS = "CREATE TABLE " + LimitationsEntry.TABLE_NAME
            + " (" +
            LimitationsEntry.COLUMN_NAME_LID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            LimitationsEntry.COLUMN_NAME_LIMITATION + " TEXT NOT NULL UNIQUE" +
            ")";

    public static final String SQL_DELETE_LIMITATIONS = "DROP TABLE IF EXISTS " + LimitationsEntry.TABLE_NAME;

    public static class LimitationsUserRelation implements BaseColumns {
        public static final String TABLE_NAME = "limitations_user_rel";
        public static final String COLUMN_NAME_LID = "lid";
        public static final String COLUMN_NAME_UID= "uid";
    }

    public static final String SQL_CREATE_LIMITATION_USER_RELATION = "CREATE TABLE " + LimitationsUserRelation.TABLE_NAME
            + " (" +
            LimitationsUserRelation.COLUMN_NAME_UID + " INTEGER NOT NULL," +
            LimitationsUserRelation.COLUMN_NAME_LID + " INTEGER NOT NULL," +
            "FOREIGN KEY(" + LimitationsUserRelation.COLUMN_NAME_UID + ") REFERENCES " + UserEntry.TABLE_NAME + "(" + UserEntry.COLUMN_NAME_UID + "), " +
            "FOREIGN KEY(" + LimitationsUserRelation.COLUMN_NAME_LID + ") REFERENCES " + LimitationsEntry.TABLE_NAME + "(" + LimitationsEntry.COLUMN_NAME_LID + "), " +
            "PRIMARY KEY (" + LimitationsUserRelation.COLUMN_NAME_LID + ", " + LimitationsUserRelation.COLUMN_NAME_UID + ")" +
            ")";

    public static final String SQL_DELETE_LIMITATION_USER_RELATION = "DROP TABLE IF EXISTS " + LimitationsUserRelation.TABLE_NAME;

    public static class RollEntry implements BaseColumns {
        public static final String TABLE_NAME ="roll";
        public static final String COLUMN_NAME_RID ="rid";
        public static final String COLUMN_NAME_ROLL ="roll";
    }

    public static final String SQL_CREATE_ROLL = "CREATE TABLE " + RollEntry.TABLE_NAME
            + " (" +
            RollEntry.COLUMN_NAME_RID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            RollEntry.COLUMN_NAME_ROLL + " TEXT NOT NULL UNIQUE" +
            ")";

    public static final String SQL_DELETE_ROLL = "DROP TABLE IF EXISTS " + LimitationsUserRelation.TABLE_NAME;

    public static class RollUserRelation implements BaseColumns {
        public static final String TABLE_NAME = "roll_user_rel";
        public static final String COLUMN_NAME_RID = "rid";
        public static final String COLUMN_NAME_UID= "uid";
    }

    public static final String SQL_CREATE_ROLL_USER_RELATION = "CREATE TABLE " + RollUserRelation.TABLE_NAME
            + " (" +
            RollUserRelation.COLUMN_NAME_UID + " INTEGER NOT NULL," +
            RollUserRelation.COLUMN_NAME_RID + " INTEGER NOT NULL," +
            "FOREIGN KEY(" + RollUserRelation.COLUMN_NAME_UID + ") REFERENCES " + UserEntry.TABLE_NAME + "(" + UserEntry.COLUMN_NAME_UID + "), " +
            "FOREIGN KEY(" + RollUserRelation.COLUMN_NAME_RID + ") REFERENCES " + RollEntry.TABLE_NAME + "(" + RollEntry.COLUMN_NAME_RID + "), " +
            "PRIMARY KEY (" + RollUserRelation.COLUMN_NAME_RID + ", " + RollUserRelation.COLUMN_NAME_UID + ")" +
            ")";

    public static final String SQL_DELETE_ROLL_USER_RELATION = "DROP TABLE IF EXISTS " + RollUserRelation.TABLE_NAME;

    public static class EquipmentEntry implements BaseColumns {
        public static final String TABLE_NAME = "equipment";
        public static final String COLUMN_NAME_EQ_ID = "eq_id";
        public static final String COLUMN_NAME_LABEL= "label";
    }

    public static final String SQL_CREATE_EQUIPMENTS = "CREATE TABLE " + EquipmentEntry.TABLE_NAME
            + " (" +
            EquipmentEntry.COLUMN_NAME_EQ_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            EquipmentEntry.COLUMN_NAME_LABEL + " TEXT NOT NULL UNIQUE" +
            ")";

    public static final String SQL_DELETE_EQUIPMENT = "DROP TABLE IF EXISTS " + EquipmentEntry.TABLE_NAME;


    public static class UserEquipmentRelation implements BaseColumns {
        public static final String TABLE_NAME = "user_equipment_rel";
        public static final String COLUMN_NAME_UID = "uid";
        public static final String COLUMN_NAME_EQ_ID= "eq_id";
    }

    public static final String SQL_CREATE_USER_EQUIPMENT_RELATION = "CREATE TABLE " + UserEquipmentRelation.TABLE_NAME
            + " (" +
            UserEquipmentRelation.COLUMN_NAME_UID + " INTEGER NOT NULL," +
            UserEquipmentRelation.COLUMN_NAME_EQ_ID + " INTEGER NOT NULL," +
            "FOREIGN KEY(" + UserEquipmentRelation.COLUMN_NAME_UID + ") REFERENCES " + UserEntry.TABLE_NAME + "(" + UserEntry.COLUMN_NAME_UID + "), " +
            "FOREIGN KEY(" + UserEquipmentRelation.COLUMN_NAME_EQ_ID + ") REFERENCES " + EquipmentEntry.TABLE_NAME + "(" + EquipmentEntry.COLUMN_NAME_EQ_ID + "), " +
            "PRIMARY KEY (" + UserEquipmentRelation.COLUMN_NAME_EQ_ID + ", " + UserEquipmentRelation.COLUMN_NAME_UID + ")" +
            ")";

    public static final String SQL_DELETE_USER_EQUIPMENT_RELATION ="DROP TABLE IF EXISTS " + UserEquipmentRelation.TABLE_NAME;


    //Use only ID 1 to 9 where 1 is none which everyone has
    public static final String SQL_INSERT_USER_EQUIPMENT_RELATION  = "INSERT INTO "
            + UserEquipmentRelation.TABLE_NAME + " " +
            "(" + UserEquipmentRelation.COLUMN_NAME_UID + "," + UserEquipmentRelation.COLUMN_NAME_EQ_ID + ") " +
            "VALUES (1,1), (1,2), (1,3), (1,9)" +
            ",(2,1), (2,9)" +
            ",(5,1), (5,4),  (5,9)" +
            ",(3,1), (3,5), (3,6), (3,8)" +
            ",(4,1), (4,2), (4,7), (4,6)" +
            ",(6,1), (6,3), (6,8)" +
            ",(7,1), (7,10)" +
            ",(8,1)" +
            ",(9,1), (9,5)" +
            ",(10,1), (10,4), (10,7)" +
            ",(11,1), (11,2)" +
            ",(12,1), (12,2)" +
            ",(13,1), (13,2), (13,10)" +
            ",(14,1), (14,2), (14,7), (14,8), (14, 9)" +
            ",(15,1), (15,4), (15,5), (15,6)" +
            ",(16,1), (16,2)" +
            ",(17,1)" +
            ",(18,1)" +
            ",(19,1), (19,2)" +
            ",(20,1), (20,2), (20,5), (20,7), (20,8),(20, 9)" +
            ",(21,1)" +
            ",(22,1), (22,2)" +
            ",(23,1), (23,2), (23,10)" +
            ",(24,1), (24,10)" +
            ",(25,1), (25,9), (25,10)" +
            ";";

    public static final String SQL_INSERT_EQUIPMENT = "INSERT INTO "
            + EquipmentEntry.TABLE_NAME + " (" + EquipmentEntry.COLUMN_NAME_LABEL + ") " +
            "VALUES ('Keins'), ('Fitness Studio Mitgliedschaft'), ('Hanteln'), ('Theraband'), ('Kettelbell'), ('Klimmzugstange')" +
            ",('Hantelbank'), ('Langhantel'), ('DipBars'), ('Bench');";

    public static final String SQL_INSERT_LIMITATIONS= "INSERT INTO "
            + LimitationsEntry.TABLE_NAME + " (" + LimitationsEntry.COLUMN_NAME_LIMITATION + ") " +
            "VALUES ('Elbows'), ('Wrists'), ('Shoulder'), ('Knees'), ('Spine'), ('Hüften')" +
            ", ('Herzschrittmacher'), ('Diabetis');";

    public static final String SQL_INSERT_ROLLS ="INSERT INTO "
            + RollEntry.TABLE_NAME + " (" + RollEntry.COLUMN_NAME_ROLL + ") " +
            "VALUES ('User'), ('Expert'), ('Admin');";

    public static final String SQL_INSERT_TEST_USER = "INSERT INTO "
            + UserEntry.TABLE_NAME + "(" + UserEntry.COLUMN_NAME_NAME
            +"," + UserEntry.COLUMN_NAME_PASSWORD
            +"," + UserEntry.COLUMN_NAME_AGE
            +"," + UserEntry.COLUMN_NAME_GENDER
            +"," + UserEntry.COLUMN_NAME_TRAINING_TYPE
            +"," + UserEntry.COlUMN_NAME_WEIGHT
            +"," + UserEntry.COlUMN_NAME_HEIGHT
            + ") VALUES ('Alex', 'Alex', 25, 1, 1, 72, 180)" +
            ",('Tom', 'Tom', 23, 1, 1, 90, 185)" +
            ",('Jenny', 'Jenny', 26, 2, 2, 65, 165)" +
            ",('Jeremy', 'Jeremy', 35, 1, 3, 100, 190)" +
            ",('Markus', 'Markus', 56, 1, 1, 85, 173)" +
            ",('Torsten', 'Torsten', 25, 1, 1, 120, 185)" +
            ",('Lukas', 'Lukas', 24, 1, 2, 105, 178)" +
            ",('Tim', 'Tim', 34, 1, 3, 90, 182)" +
            ",('Tobi', 'Tobi', 29, 1, 2, 68, 169)" +
            ",('Karl', 'Karl', 18, 1, 2, 73, 172)" + // 10
            ",('James', 'James', 19, 1, 1, 84, 186)" +
            ",('Jake', 'Jake', 21, 1, 1, 91, 162)" +
            ",('Markus', 'Markus', 42, 1, 3, 103, 176)" +
            ",('Lars', 'Lars', 39, 1, 2, 75, 165)" +
            ",('Lara', 'Lara', 18, 2, 2, 53, 160)" +
            ",('Luna', 'Luna', 26, 2, 1, 82, 183)" +
            ",('Jessika', 'Jessika', 24, 2, 1, 46, 149)" +
            ",('Aria', 'Aria', 21, 2, 1, 103, 173)" +
            ",('Mira', 'Mira', 31, 2, 1, 61, 162)" +
            ",('Lisa', 'Lisa', 19, 2, 2, 72, 168)" + //20
            ",('Nina', 'Nina', 23, 2, 1, 58, 159)" +
            ",('Jin', 'Jin', 33, 2, 2, 78, 173)" +
            ",('An', 'An', 46, 2, 2, 55, 153)" +
            ",('Hanna', 'Hanna', 51, 2, 1, 94, 176)" +
            ",('Mara', 'Mara', 43, 2, 1, 80, 162)" +  //25
            ",('Alex2', 'Alex2', 20, 1, 1, 93, 188)" +
            ",('Alexa', 'Alexa', 25, 2, 1, 70, 178)" +
            ",('Alme', 'Alme', 30, 1, 1, 95, 190)" +
            ",('Ter', 'Ter', 35, 1, 1, 72, 179)" +
            ",('Tre', 'Tre', 25, 1, 3, 72, 181)" +  //30
            ",('Sche', 'Sche', 25, 1, 1, 102, 181)" +
            ",('Kra', 'Kra', 25, 1, 1, 72, 210)" +
            ",('Ove', 'Ove', 26, 1, 1, 160, 181)" +
            ",('Mart', 'Mart', 23, 1, 1, 187, 175)" +
            ",('Otr', 'Otr', 19, 2, 1, 143, 161)" + //35
            ",('Lia', 'Lia', 23, 2, 1, 168, 155)" +
            ";";

    public static final String SQL_INSERT_USER_ROLL = "INSERT INTO "
            + RollUserRelation.TABLE_NAME + "(" + RollUserRelation.COLUMN_NAME_UID + ", " + RollUserRelation.COLUMN_NAME_RID +")"
            + "VALUES (1, 1), (1, 2), (1, 3)" +
            ", (2, 1)" +
            ", (3, 1)" +
            ", (4, 1)" +
            ", (5, 1)" +
            ", (6, 1)" +
            ", (7, 1)" +
            ", (8, 1)" +
            ", (9, 1)" +
            ", (10, 1)" + //10
            ", (11, 1)" +
            ", (12, 1)" +
            ", (13, 1)" +
            ", (14, 1)" +
            ", (15, 1)" + //15
            ", (16, 1)" +
            ", (17, 1)" +
            ", (18, 1)" +
            ", (19, 1)" +
            ", (20, 1)" + //20
            ", (21, 1)" +
            ", (22, 1)" +
            ", (23, 1)" +
            ", (24, 1)" +
            ", (25, 1)" + //25
            ", (26, 1)" +
            ", (27, 1)" +
            ", (28, 1)" +
            ", (29, 1)" +
            ", (30, 1)" +
            ", (31, 1)" +
            ", (32, 1)" +
            ", (33, 1)" +
            ", (34, 1)" +
            ", (35, 1)" +
            ", (36, 1)" +
            ";";

    public static final String SQL_INSERT_USER_LIMITATION_REL = "INSERT INTO "
            + LimitationsUserRelation.TABLE_NAME + "(" + LimitationsUserRelation.COLUMN_NAME_UID
            + ", " + LimitationsUserRelation.COLUMN_NAME_LID + ") " +
            "VALUES (1, 3),(4, 2), (4, 4), (5, 5), (5, 4), (6,4), (8,2), (8, 3), (12,5), (13, 3)" +
            ",(18, 1), (18,2), (23, 1), (26, 3), (28, 3), (28, 1), (33, 4), (35, 4);";
    //The line breaks are used to enable scrolling int the text fields as to not hide text behind the editing buttons.
    public static final String SQL_INSERT_EXERCISES = "INSERT INTO "
            + ExerciseEntry.TABLE_NAME + "(" + ExerciseEntry.COLUMN_NAME_TITLE
            +"," + ExerciseEntry.COLUMN_NAME_DURATION_REP
            +"," + ExerciseEntry.COLUMN_NAME_EXERCISE_TYPE
            +"," + ExerciseEntry.COLUMN_NAME_EQUIPMENT
            +"," + ExerciseEntry.COLUMN_NAME_EXPLANATION
            +"," + ExerciseEntry.COLUMN_NAME_PRIME_MUSCLE
            +"," + ExerciseEntry.COLUMN_NAME_SECONDARY_MUSCLE
            +"," + ExerciseEntry.COLUMN_NAME_ILLUSTRATION_LINK
            +"," + ExerciseEntry.COLUMN_NAME_MOVEMENT_TYPE
            +"," + ExerciseEntry.COLUMN_NAME_IS_EXPLOSIVE
            +"," + ExerciseEntry.COLUMN_NAME_IS_BODYWEIGHT
            + ") VALUES ('Push-Up', 3, 2, 1, 'Begeben sie sich in eine liegende Position mit dem " +
            "Gesicht nach unten, platzieren sie ihre Arme schulterbreit unter den Schultern und" +
            " ihre Fuesse huefstbreit. Spannen sie ihre Bauchmuseln und ihre Glutes an. Druecken sie" +
            "sich mit ihren Armen nach oben und lassen sich dann wieder Runter.\n1. Die Arme durchdrücken \n" +
            "2. Die Hände nach vorne \n3. Den Bauch angespannt lassen \n4. Hüfte leicht nach vorne \n5. Füße auf die Zehenspitzen" +
            " Achten sie auf einen geraden Ruecken.\n\n\n\n', 4, 6, 'mens_healt_push_up', 1, 0, 1)" +
            ",('Clapping Push-Up', 3, 2, 1, 'Begeben sie sich in eine liegende Position mit dem " +
            "Gesicht nach unten, platzieren sie ihre Arme schulterbreit unter den Schultern und" +
            " ihre Fuesse huefstbreit. Spannen sie ihre Bauchmuseln und ihre Glutes an. Druecken sie" +
            "sich schnell nach oben, so dass sie bevor sie wieder absinken in die Haende klatschen" +
            " koennen. Nach dem klatschen die Arme wieder in die Ausgangspositions um den abstieg " +
            "abzufangen. Achten sie auf einen geraden Ruecken.', 4, 6, 'usersetts', 1, 1, 1)" +
            ",('Dip', 5, 2, 9, 'Beidseitig die Stangen greifen, eine gerade Haltung einnehmen, dann" +
            "den Koerper langsam und kontrolliert durch das Biegen der Arme senken und anschliessend" +
            "wieder hochdruecken', 2, 4, 'usersetts', 1, 0, 1)" +
            ",('Bench Dip', 5, 2, 10, 'Platzieren sie sich mit dem Rücken zu einer Bank, setzen " +
            "sie sich davor, platzieren ihre Hände mit den Fingern nach vorne und drücken sich" +
            "nur mit den Armen hoch.', 2, 4, 'usersetts', 1, 0, 1)" +
            ",('Biceps Curls', 3, 1, 3, 'Stehen sie gerade, nehmen sie eine Handel, halten sie den " + //5
            "Arm mit der Hanel an ihrer Seite, die Daumen nach außen, heben sie die Handel bis " +
            "zum höchsten Punk und senken sie die Hantel wiede.', 1, 14, 'usersetts', 2, 0, 0)" +
            ",('Bench Press', 6, 2, 7, 'Stehen sie gerade, nehmen sie eine Handel, halten sie den " +
            "Arm mit der Hanel an ihrer Seite, die Daumen nach außen, heben sie die Handel bis " +
            "zum höchsten Punk und senken sie die Hantel wiede.', 4, 6, 'usersetts', 1, 0, 0)" +
            ",('Archer Push-Up', 4, 2, 1, 'ArcherPushUp.', 4, 6, 'LINK', 1, 0, 1)" +
            ",('Cable Lateral-Raise', 5, 1, 2, 'Stehen seitlich ziehen.', 6, 14, 'usersetts', 2, 0, 0)"+
            ",('Dumbbell Lateral Raise', 3, 2, 3, 'Seitlich hanteln heben.', 6, 11, 'usersetts', 2, 0, 0)" +
            ",('Dumbbell Front Raise', 3, 2, 3, 'Frontal hanteln heben.', 6, 11, 'usersetts', 2, 0, 0)" +  //10
            ",('Bent-Over Dumbbell Fly', 4, 2, 3, 'Vorbeugen hanteln seitlich heben.', 6, 11, 'usersetts', 2, 0, 0)" +
            ",('Upright Dumbbell Raise', 3, 2, 3, 'Frontal hanteln heben. Ellbogen außen.', 6, 11, 'usersetts', 2, 0,0)" +
            ",('Machine Shoulder Press', 4, 2, 2, 'Machine Schulter Vordruecken.', 6, 2, 'usersetts', 1, 0,0)" +
            ",('Steated Barebell Military Press', 3, 2, 8, 'Hantel ueber Kopf druecken.', 6, 1, 'LINK', 1, 0,0)" +
            ",('Barebell Shrug ', 3, 1, 8, 'Gerade stehen, Schultern heben.', 11, 14, 'usersetts', 2, 0,0)" + //15
            ",('Machine Chest Press', 4, 1, 2, 'Machine arme nach Vorne drücken.', 4, 2, 'usersetts', 2, 0,0)" +
            ",('Machine Chest Fly ', 3, 1, 2, 'Maschine Arm frontal zusammenführen.', 4, 6, 'usersetts', 1, 0,0)" +
            ",('Lying Chest Fly ', 3, 1, 3, 'Auf Rücken Hanteln oben zusammenführen.', 4, 6, 'usersetts', 1, 0,0)" +
            ",('Dumbbell Bench Press ', 3, 1, 3, 'Gerade stehen, Schultern heben.', 4, 6, 'usersetts', 1, 0,0)" +
            ",('Machine Seateted Row ', 4, 2, 2, 'Gerade stehen, Schultern heben.', 15, 11, 'usersetts', 2, 0,0)" + //20
            ",('Lat-Pulldown ', 4, 2, 2, 'Stange von oben herrunter ziehen.', 15, 1, 'usersetts', 2, 0,0)" +
            ",('Asissted Pull-Up', 5, 2, 2, 'Asisstet Pullup Machine nutzen.', 15, 1, 'usersetts', 2, 0, 1)" +
            ",('Pull Up', 4, 2, 6, 'Pull Up', 15, 1, 'usersetts', 2, 0, 1)" +
            ",('Dumbbell single arm Row', 2, 2, 3, 'Vorbeugen, arm seitlich geradte hochiehen.', 15, 1, 'usersetts', 2, 0 ,1)" +
            ",('Machine Seated Triceps Push-Down', 3, 2, 2, 'user Machine', 2, 14, 'usersetts', 1, 0, 0)" +  //25
            ",('Cable Triceps Extension', 3, 2, 2, 'user Machine', 2, 14, 'usersetts', 1, 0, 0)" +
            ",('Dumbbell Hammer Curl', 2, 2, 3, 'user Machine', 1, 3, 'usersetts', 2, 0, 0)" + //Strength training page 205
            ",('Diamond Push Up', 2, 2, 1, 'Diamond Push Up', 2, 4, 'usersetts', 1, 0, 1)" +
            ",('Pull Up Superman', 3, 2, 1, 'Auf bauch legen, Oberkörper anheben, Pull Up movement', 11, 12, 'usersetts', 2, 0, 1)" +
            ",('Back Crunch', 2, 1, 1, 'Auf bauch legen, Oberkörper und beine hochziehehn', 12, 15, 'usersetts', 2, 0, 1)" +   //30
            ",('Rotational Plank', 3, 2, 1, 'Plank, dann einen Arm Zur decke Körper drehen', 5, 6, 'usersetts', 2, 0, 1)" +
            ",('Triceps Extension', 2, 2, 1, 'Plank mit Armen auf Tisch, nur druch Triceps hochdrücken', 2, 5, 'usersetts', 1, 0, 1)" +
            ",('Short-Lever Inverted Curl', 2, 2, 1, 'Unter Tisch mit Armen hochziehen', 1, 6, 'usersetts', 2, 0, 1)" +
            ",('Biceps Chin Up', 4, 2, 6, 'Hände so an Stange, dass Per Bizeps gezogen wird', 1, 15, 'usersetts', 2, 0, 1)" +
            ",('Three Point Bench Dip', 3, 2, 1, 'Stühle so dass Arme auf einem und Füße auf dem anderen', 2, 6, 'usersetts', 1, 0, 1)" +  //35
            ",('Push Back', 3, 2, 1, 'Vorbäugen, mit Glutes in der Höhe, nach vorne mit dem Gesicht zum Boden, wegrdücken', 6, 11, 'usersetts', 1, 0, 1)" +
            ",('Peak Push Up', 3, 2, 1, 'Füße auf Stuhl, Glutes höchstr  Punkt rücken Gerade, push up Bewegung', 6, 2, 'usersetts', 1, 0, 1)" +
            ",('Crunch', 2, 2, 1, 'Auf Boden, nur durch Bauch Oberkörper hochheben', 5, 14, 'usersetts', 2, 0, 1)" +
            ",('Side Crunch', 2, 2, 1, 'Auf dem Rücken Beine Angewinklent nach links und rechts absenken', 5, 14, 'usersetts', 1, 0, 1)" +
            ",('Back Crunch', 2, 2, 1, 'Reverse Crunch auf dem Bauch liegend', 15, 10, 'usersetts', 2, 0, 1)" +  //40
            ",('Side Plank', 1, 2, 1, 'Seitliche Plan, Hüften senken und Anheben', 5, 14, 'usersetts', 2, 0, 1)" +
            ",('Sumo Squad', 3, 2, 1, 'Squad, Knie nach ausßen.', 9, 10, 'usersetts', 1, 0, 1)" +
            ",('Squad', 3, 2, 1, 'Squad.', 9, 10, 'usersetts', 1, 0, 1)" +
            ",('Pistol Squat', 4, 2, 1, 'Einbeiniger Squad bis zum Boden, Arme ausgestreckt', 9, 8, 'usersetts', 1, 0, 1)" +
            ",('Dumbbell Overhead Press', 3, 2, 3, 'Dumbells on shoulder, press upwards', 6, 15, 'usersetts', 1, 0, 0)" + //45
            ",('Leg Press', 4, 2, 2, 'Sitzen, Gewicht mit Beinen drücken.', 10, 9, 'usersetts', 1, 0, 0)" +
            ",('Lying Leg Curl', 3, 2, 2, 'Liegend auf bauch mit beinen Gewichte heben', 8, 7, 'usersetts', 2, 0, 0)" +
            ",('Calve Raise', 2, 1, 1, 'Fußspitze auf kante, hochdrücken', 7, 14, 'usersetts', 1, 0, 1)" +
            ",('Barbell Bent Over Row', 3, 2, 3, 'Vorbeugen, gearder Rücken Hantel hochziehen.', 11, 15, 'usersetts', 2, 0, 0)" +
            ",('Cable Preacer Curls', 2, 1, 2, 'Vorbeugen, gearder Rücken Hantel hochziehen.', 1, 14, 'usersetts', 2, 0, 0)" + //50
            ",('Lying Triceps Extension', 3, 1, 3, 'Auf Rücken mit Trieps  Hantel bewegen.', 2, 14, 'usersetts', 1, 0, 1)" +
            ",('Sitting Leg Curl', 3, 1, 2, 'Sitzend mit beinen Gewichte heben', 8, 7, 'usersetts', 2, 0, 0)" +
            ";";

    public static final String SQL_INSERT_PLAN = "INSERT INTO "
            + PlanEntry.TABLE_NAME + "(" + PlanEntry.COLUMN_NAME_NAME
            +"," + PlanEntry.COLUMN_NAME_GOAL
            +"," + PlanEntry.COLUMN_NAME_MUSCLE_GROUP
            +"," + PlanEntry.COLUMN_NAME_RATING
            + ") VALUES ('BeginnerFullBodyMass', 3, 7, 5)," +
            "('BeginnerUpperBody', 3, 8, 5)" +
            ",('BeginnerLowerBody', 3, 9, 5)" +
            ",('BeginnerUpperBodySecond', 3, 2, 2)" +
            ",('ArmMaxStrength', 1, 1, 4)" + //5
            ",('BackWorkout', 2, 6, 1)" +
            ",('PlanWholeStrength', 1, 7, 2)" +
            ",('GoalMaxStrengthUpper', 1, 8, 5)" +
            ",('GoalEnduranceUpper', 2, 8, 5)" +
            ";";

    public static final String SQL_INSERT_PLAN_EXERCISE_RELATION = "INSERT INTO "
            + PlanExerciseRelationEntry.TABLE_NAME + "(" + PlanExerciseRelationEntry.COLUMN_NAME_PID
            +"," + PlanExerciseRelationEntry.COLUMN_NAME_EID
            +"," + PlanExerciseRelationEntry.COLUMN_NAME_BREAK
            +"," + PlanExerciseRelationEntry.COLUMN_NAME_REPS
            +"," + PlanExerciseRelationEntry.COLUMN_NAME_SETS
            +"," + PlanExerciseRelationEntry.COLUMN_NAME_WEIGHT
            +") VALUES (1, 19, 60, 10, 3, 5)" +
            ",(1, 21, 50, 10, 3, 5)" +
            ",(1, 45, 70, 10, 3, 5)" +
            ",(1, 46, 70, 10, 3, 15)" +
            ",(1, 47, 60, 10, 3, 10)" +
            ",(1, 26, 60, 10, 3, 5)" +
            ",(1, 5, 80, 10, 3, 5)" +
            ",(1, 48, 40, 12, 3, 0)" +
            ",(1, 38, 40, 12, 3, 0)" +
            ",(2, 16, 70, 10, 3, 5)" +
            ",(2, 4, 80, 10, 3, 15)" +
            ",(2, 49, 70, 12, 3, 10)" +
            ",(2, 21, 70, 12, 3, 5)" +
            ",(2, 45, 60, 10, 3, 5)" +
            ",(2, 9, 80, 10, 3, 5)" +
            ",(2, 50, 80, 10, 3, 8)" +
            ",(2, 51, 80, 10, 3, 5)" +
            ",(2, 38, 40, 12, 3, 0)" +
            ",(3, 46, 70, 10, 3, 20)" +
            ",(3, 47, 65, 10, 3, 10)" +
            ",(3, 52, 80, 10, 3, 7)" +
            ",(3, 48, 40, 12, 3, 0)" +
            ",(3, 42, 60, 10, 3, 0)" +
            ",(4, 1, 20, 12, 3, 0)" +
            ",(4, 3, 15, 12, 3, 0)" +
            ",(4, 5, 15, 12, 3, 25)" +
            ",(4, 20, 20, 10, 3, 35)" +
            ",(4, 11, 15, 10, 3, 15)" +
            ",(5, 28, 180, 15, 4, 0)" +
            ",(5, 5, 180, 6, 2, 40)" +
            ",(5, 27, 160, 5, 2, 30)" +
            ",(6, 20, 50, 20, 4, 20)" +
            ",(6, 21, 60, 15, 3, 30)" +
            ",(6, 29, 60, 20, 4, 0)" +
            ",(6, 30, 60, 20, 4, 0)" +
            ",(7, 23, 180, 12, 4, 0)" +
            ",(7, 6, 150, 6, 4, 55)" +
            ",(7, 27, 160, 5, 3, 0)" +
            ",(7, 20, 160, 5, 4, 50)" +
            ",(8, 1, 150, 12, 4, 0)" +
            ",(8, 3, 150, 15, 4, 0)" +
            ",(8, 23, 160, 10, 3, 0)" +
            ",(8, 25, 160, 5, 4, 35)" +
            ",(8, 5, 160, 5, 4, 45)" +
            ",(8, 38, 160, 20, 4, 0)" +
            ",(9, 6, 30, 24, 2, 15)" +
            ",(9,11, 25, 25, 2, 10)" +
            ",(9, 20, 35, 20, 2, 20)" +
            ",(9, 25, 30, 20, 2, 10)" +
            ",(9, 5, 30, 25, 2, 15)" +
            ",(9, 14, 35, 20, 2, 20)" +
            ";";

    public static final String SQL_INSERT_PLAN_USER_RELATION = "INSERT INTO "
            + UserPlanRelation.TABLE_NAME + "(" + UserPlanRelation.COLUMN_NAME_UID + "," + UserPlanRelation.COLUMN_NAME_PID
            + ") VALUES (1,1),(1,2)" +
            ",(2,3)" +
            ",(3,4)" +
            ",(4,5)" +
            ",(5,6),(5,7)" +
            ",(26,9)" +
            ",(2,8)" +
            ";";
}
