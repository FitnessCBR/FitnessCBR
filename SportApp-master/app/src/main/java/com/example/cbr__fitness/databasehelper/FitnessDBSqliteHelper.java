package com.example.cbr__fitness.databasehelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.AbstractWindowedCursor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;

import com.example.cbr__fitness.data.Exercise;
import com.example.cbr__fitness.data.ExerciseList;
import com.example.cbr__fitness.data.Limitation;
import com.example.cbr__fitness.data.User;
import com.example.cbr__fitness.enums.EquipmentEnum;
import com.example.cbr__fitness.enums.ExerciseTypeEnum;
import com.example.cbr__fitness.enums.GenderEnum;
import com.example.cbr__fitness.enums.LimitationEnum;
import com.example.cbr__fitness.enums.MovementTypeEnum;
import com.example.cbr__fitness.enums.MuscleEnum;
import com.example.cbr__fitness.enums.WorkoutEnum;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.dfki.mycbr.core.casebase.Instance;
import de.dfki.mycbr.core.similarity.Similarity;
import de.dfki.mycbr.util.Pair;

public class FitnessDBSqliteHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "FitnessDB.db";
    public static final int DATABASE_VERSION = 1;

    public FitnessDBSqliteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        System.out.println("ON CREATE IST CALLED");
        db.execSQL(FitnessDBContract.SQL_CREATE_USER);
        db.execSQL(FitnessDBContract.SQL_CREATE_EXERCISE);
        db.execSQL(FitnessDBContract.SQL_CREATE_PLAN);
        db.execSQL(FitnessDBContract.SQL_CREATE_ROLL);
        db.execSQL(FitnessDBContract.SQL_CREATE_LIMITATIONS);
        db.execSQL((FitnessDBContract.SQL_CREATE_EQUIPMENTS));
        db.execSQL(FitnessDBContract.SQL_CREATE_USER_PLAN_RELATION);
        db.execSQL(FitnessDBContract.SQL_CREATE_PLAN_EXERCISE_RELATION);
        db.execSQL(FitnessDBContract.SQL_CREATE_LIMITATION_USER_RELATION);
        db.execSQL(FitnessDBContract.SQL_CREATE_ROLL_USER_RELATION);
        db.execSQL(FitnessDBContract.SQL_CREATE_USER_EQUIPMENT_RELATION);

        db.execSQL(FitnessDBContract.SQL_INSERT_LIMITATIONS);
        db.execSQL(FitnessDBContract.SQL_INSERT_EQUIPMENT);
        db.execSQL(FitnessDBContract.SQL_INSERT_ROLLS);
        db.execSQL(FitnessDBContract.SQL_INSERT_EXERCISES);
        db.execSQL(FitnessDBContract.SQL_INSERT_TEST_USER);
        db.execSQL(FitnessDBContract.SQL_INSERT_PLAN);
        db.execSQL(FitnessDBContract.SQL_INSERT_USER_LIMITATION_REL);
        db.execSQL(FitnessDBContract.SQL_INSERT_PLAN_USER_RELATION);
        db.execSQL(FitnessDBContract.SQL_INSERT_USER_EQUIPMENT_RELATION);
        db.execSQL(FitnessDBContract.SQL_INSERT_USER_ROLL);
        db.execSQL(FitnessDBContract.SQL_INSERT_PLAN_EXERCISE_RELATION);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(FitnessDBContract.SQL_DELETE_EXERCISE);
        db.execSQL(FitnessDBContract.SQL_DELETE_USER);
        db.execSQL(FitnessDBContract.SQL_DELETE_PLAN);
        db.execSQL(FitnessDBContract.SQL_DELETE_ROLL);
        db.execSQL(FitnessDBContract.SQL_DELETE_EQUIPMENT);
        db.execSQL(FitnessDBContract.SQL_DELETE_LIMITATIONS);
        db.execSQL(FitnessDBContract.SQL_DELETE_PLAN_EXERCISE_RELATION);
        db.execSQL(FitnessDBContract.SQL_DELETE_USER_PLAN_RELATION);
        db.execSQL(FitnessDBContract.SQL_DELETE_LIMITATION_USER_RELATION);
        db.execSQL(FitnessDBContract.SQL_DELETE_ROLL_USER_RELATION);
        db.execSQL(FitnessDBContract.SQL_DELETE_USER_EQUIPMENT_RELATION);
        onCreate(db);
    }

    public List<Limitation> getLimitationsFromDB() {
        SQLiteDatabase db = this.getReadableDatabase();

        List<Limitation> limitations = new ArrayList<>();

        String[] projection = {
                FitnessDBContract.LimitationsEntry.COLUMN_NAME_LID,
                FitnessDBContract.LimitationsEntry.COLUMN_NAME_LIMITATION
        };

        String sortOrder = FitnessDBContract.LimitationsEntry.COLUMN_NAME_LID + " DESC";

        Cursor cursor = doAndroidQuery(db, FitnessDBContract.LimitationsEntry.TABLE_NAME
                , projection, null, null, null, null, sortOrder);

        try {
            while(cursor.moveToNext()){
                //System.out.println("COL 0: " + cursor.getInt(0) + " COL 1 " + cursor.getString(1));
                limitations.add(new Limitation(cursor.getString(1), cursor.getInt(0)));
            }
        } finally {
            cursor.close();
            db.close();
        }
        return limitations;
    }

    public long inputNewUserIntoDB (String username, String password, int age, int gender,
                                   int weight, int height,int trainingType, List<Integer> restrictionsIDs,
                                    List<Integer> equipmentIDs) {
        SQLiteDatabase db  = this.getWritableDatabase();

        ContentValues userValues = new ContentValues();

        userValues.put(FitnessDBContract.UserEntry.COLUMN_NAME_NAME, username);
        userValues.put(FitnessDBContract.UserEntry.COLUMN_NAME_PASSWORD, password);
        userValues.put(FitnessDBContract.UserEntry.COLUMN_NAME_AGE, age);
        userValues.put(FitnessDBContract.UserEntry.COLUMN_NAME_GENDER, gender);
        userValues.put(FitnessDBContract.UserEntry.COlUMN_NAME_WEIGHT, weight);
        userValues.put(FitnessDBContract.UserEntry.COlUMN_NAME_HEIGHT, height);
        userValues.put(FitnessDBContract.UserEntry.COLUMN_NAME_TRAINING_TYPE, trainingType);

        long id  = db.insert(FitnessDBContract.UserEntry.TABLE_NAME, null, userValues);

        for (int r : restrictionsIDs) {
            ContentValues limitValues = new ContentValues();
            limitValues.put(FitnessDBContract.LimitationsUserRelation.COLUMN_NAME_UID, id);
            limitValues.put(FitnessDBContract.LimitationsUserRelation.COLUMN_NAME_LID, r);

            db.insert(FitnessDBContract.LimitationsUserRelation.TABLE_NAME, null, limitValues);
        }
        equipmentIDs.add(1); //"Keins" is default added to every user as everyone should have no eq.
        for (int r :equipmentIDs) {
            ContentValues equipmentValues = new ContentValues();
            equipmentValues.put(FitnessDBContract.UserEquipmentRelation.COLUMN_NAME_EQ_ID, r);
            equipmentValues.put(FitnessDBContract.UserEquipmentRelation.COLUMN_NAME_UID, id);

            db.insert(FitnessDBContract.UserEquipmentRelation.TABLE_NAME, null, equipmentValues);
        }

        db.close();
        return id;
    }

    public boolean checkUserNameAvailable(String userName) {
        boolean available;

        SQLiteDatabase db = this.getReadableDatabase();

        String selection = FitnessDBContract.UserEntry.COLUMN_NAME_NAME + " = ?";
        String[] selectionArgs = { userName };

        Cursor cursor = doAndroidQuery(db, FitnessDBContract.UserEntry.TABLE_NAME, null
                , selection, selectionArgs, null, null, null);

        available = (cursor.getCount() == 0);

        cursor.close();
        db.close();

        return available;
    }

    public int loginUser(String userName, String password) {
        int userID = -1;

        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                FitnessDBContract.UserEntry.COLUMN_NAME_UID
        };

        String selection = FitnessDBContract.UserEntry.COLUMN_NAME_NAME + " = ? AND " +
                FitnessDBContract.UserEntry.COLUMN_NAME_PASSWORD + " = ?";
        String[] selectionArgs = { userName, password };

        Cursor cursor = doAndroidQuery(db, FitnessDBContract.UserEntry.TABLE_NAME, projection
                , selection, selectionArgs, null, null, null);

        if (cursor.moveToFirst()) {
            userID = cursor.getInt(0);
        }

        cursor.close();
        db.close();

        return userID;
    }

    /**
     * Returns a query with seven columns: uid, pid, pid, name, goal, sub_goal, rating
     */
    private static final String SQL_GET_PLANS_BY_USER_ID = "SELECT * FROM "
            + FitnessDBContract.UserPlanRelation.TABLE_NAME + " rel INNER JOIN "
            + FitnessDBContract.PlanEntry.TABLE_NAME + " plans ON "
            + "rel." + FitnessDBContract.UserPlanRelation.COLUMN_NAME_PID + "="
            + "plans." + FitnessDBContract.PlanEntry.COLUMN_NAME_PID + " WHERE "
            + "rel." + FitnessDBContract.UserPlanRelation.COLUMN_NAME_UID + " = ?";
    /**
     * Raw query for the retrieval of all exercises related to a number of plans
     * Order of fields: pid, eid, sets, reps, break, weight, eid, title, prime_muscle
     * secondary_muscle, exercise_type, explanation, illustration_link, equipment, duration_rep
     */
    private static final String SQL_GET_EXERCISES_BY_PLANS = "SELECT * FROM "
        + FitnessDBContract.PlanExerciseRelationEntry.TABLE_NAME + " rel INNER JOIN "
        + FitnessDBContract.ExerciseEntry.TABLE_NAME + " exercise ON "
        + "rel." + FitnessDBContract.PlanExerciseRelationEntry.COLUMN_NAME_EID + "="
        + "exercise." + FitnessDBContract.ExerciseEntry.COLUMN_NAME_EID
        + " WHERE " + FitnessDBContract.PlanExerciseRelationEntry.COLUMN_NAME_PID + " IN (";

    /**
     * This method gets all the Plans a user has including the exercises belonging to them.
     * @param userId The user the data is required for
     * @return The list of all plans the user got
     */
    public List<ExerciseList> getExerciseListsByUser (int userId) {

        SQLiteDatabase db = this.getReadableDatabase();

        List<ExerciseList> exercises = getExerciseListsByUser(db, userId, false);

        db.close();
        return exercises;
    }

    public List<Pair<ExerciseList, Double>> getPlansForCBRUsers(List<Pair<User, Double>> users) {
        List<Pair<ExerciseList, Double>> plans = new ArrayList<>();

        SQLiteDatabase db  = getReadableDatabase();

        for (Pair<User, Double> p : users) {
            List<ExerciseList>  temp = getExerciseListsByUser(db, p.getFirst().getUid(), true);
            for (ExerciseList e : temp  ) {
                e.setuID(p.getFirst().getUid());
                plans.add(new Pair<>(e, p.getSecond()));
            }
        }

        db.close();
        return plans;
    }

    public List<ExerciseList> getExerciseListsByUser (SQLiteDatabase db, int uID, boolean includeDeleted) {
        List<ExerciseList> exercises = new ArrayList<>();
        String[] values = {String.valueOf(uID)};
        String query = SQL_GET_PLANS_BY_USER_ID;
        if (!includeDeleted) {
            query += " AND plans." + FitnessDBContract.PlanEntry.COLUMN_NAME_DELETED + " = ?";
            values = new String[]{String.valueOf(uID), "0"};
        }
        Cursor cursor = db.rawQuery(query ,values);

        if (cursor.getCount() > 0) {
            while(cursor.moveToNext()) {
                //Gets plan ID, goal, sub goal and the name in that order
                exercises.add(new ExerciseList(cursor.getInt(2)
                        , cursor.getInt(4), cursor.getInt(5)
                        ,cursor.getString(3)));
            }
        }

        String completeQuery = completePlanExerciseQuery(exercises);
        String[] parameters = new String[exercises.size()];
        for (int i = 0; i < exercises.size(); i++) {
            parameters[i] = String.valueOf(exercises.get(i).getPlan_id());
            System.out.println("PARAMETER: " + parameters[i]);
        }
        Cursor exerciseCursor = db.rawQuery(completeQuery,parameters);
        //System.out.println("CURSOR COUT: " + exerciseCursor.getCount() + " STATEMENT: " + completeQuery);
        if (exerciseCursor.getCount() > 0) {
            List<Exercise> exerciseList = cursorToExercises(exerciseCursor);
            for (ExerciseList el : exercises) {
                for (Exercise e : exerciseList) {
                    if (e.getPlanID() == el.getPlan_id()) {
                        el.addExercise(e);
                    }
                }
            }
        }
        exerciseCursor.close();
        cursor.close();

        return exercises;
    }

    public void deletePlans (List<Integer> plansToDelete) {
        if (plansToDelete.size() > 0) {

            SQLiteDatabase db = getWritableDatabase();

            for (Integer i : plansToDelete) {
                ContentValues values = new ContentValues();
                values.put(FitnessDBContract.PlanEntry.COLUMN_NAME_DELETED, 1);

                db.update(FitnessDBContract.PlanEntry.TABLE_NAME, values
                        , FitnessDBContract.PlanEntry.COLUMN_NAME_PID + " = ?"
                        ,new String[]{Integer.toString(i)});
            }

            db.close();
        }
    }

    /**
     * Creates exercises from a cursor assumes an inner join on plan_exercises and exercises.
     *  Order of fields: pid, eid, sets, reps, break, weight, eid, title, prime_muscle, secondary_muscle,exercise_type
     *  explanation, illustration_link, equipment, duration_rep, movement_type, is_explosive, is_bodyweight
     * @param cursor A cursor containing all the data for exercises
     * @return  A list with all the exercises the cursor held
     */
    private List<Exercise> cursorToExercises (Cursor cursor) {
        List<Exercise> exercises = new ArrayList<>();

        while(cursor.moveToNext()) {
             /*   System.out.println("COLUMNS: " + cursor.getColumnCount()
                        + " NAMES: " + cursor.getColumnName(0) + ", "  //int
                        + cursor.getColumnName(1) + ", "               //int
                        + cursor.getColumnName(2) + ", "               //int
                        + cursor.getColumnName(3) + ", "               //int
                        + cursor.getColumnName(4) + ", "               //int
                        + cursor.getColumnName(5) + ", "            //String
                        + cursor.getColumnName(6) + ", "               //int
                        + cursor.getColumnName(7) + ", "            //String
                        + cursor.getColumnName(8) + ", "               //int
                        + cursor.getColumnName(9) + ", "               //int
                        + cursor.getColumnName(10) + ", "              //int
                        + cursor.getColumnName(11) + ", "           //String
                        + cursor.getColumnName(12) + ", "           //String
                        + cursor.getColumnName(13) + ", "              //int
                        + cursor.getColumnName(14) + ", ");            //int */
            //pid, eid, sets, reps, break, weight, eid, title, prime_muscle, exercise_type
            // explanation, illustration_link, equipment, duration_rep
            exercises.add(new Exercise(cursor.getInt(0), cursor.getInt(1)
                    , cursor.getString(7), cursor.getInt(2)
                    , cursor.getInt(3), cursor.getInt(4)
                    , cursor.getInt(5), MuscleEnum.getEnumByInt(cursor.getInt(8))
                    , MuscleEnum.getEnumByInt(cursor.getInt(9))
                    , cursor.getInt(14), ExerciseTypeEnum.getEnumByInt(cursor.getInt(10))
                    , EquipmentEnum.getEnumById(cursor.getInt(13))
                    , cursor.getString(11),MovementTypeEnum.getEnumById(cursor.getInt(15))
                    , (cursor.getInt(16) == 1), (cursor.getInt(17) == 1)// done as 0 represents false as would be the expression.
                            , cursor.getString(12)));
        }

        return exercises;
    }

    private String completePlanExerciseQuery (List<ExerciseList> exercises) {
        boolean isFirst = true;
        StringBuilder completeExerciseQuery = new StringBuilder(SQL_GET_EXERCISES_BY_PLANS);
        for (int i = 0; i < exercises.size(); i++) {
            if (isFirst) {
                completeExerciseQuery.append("?");
                isFirst = false;
            } else {
                completeExerciseQuery.append(",?");
            }
        }
        completeExerciseQuery.append( ")");
        return completeExerciseQuery.toString();
    }

    /**
     * Gets a list of all currently considered equipment items needed for workouts.
     * Order: eq_id(int), label(String)
     * @return A list containing all the equipment types currently in the database.
     */
    public List<EquipmentEnum> getEquipmentFromDB() {
        SQLiteDatabase db = this.getReadableDatabase();

        List<EquipmentEnum> equipmentEnumList = new ArrayList<>();

        String sortOrder = FitnessDBContract.EquipmentEntry.COLUMN_NAME_EQ_ID + " DESC";

        Cursor cursor = doAndroidQuery(db, FitnessDBContract.EquipmentEntry.TABLE_NAME, null
                , null, null , null, null, sortOrder);

        try {
            while(cursor.moveToNext()){
                //System.out.println("COL 0: " + cursor.getInt(0) + " COL 1 " + cursor.getString(1));
                equipmentEnumList.add(EquipmentEnum.getEnumById(cursor.getInt(0)));
            }
            for (EquipmentEnum eenum : equipmentEnumList) {
                System.out.println("EQUIPMENT: " + eenum.getLabel() + " ID: " + eenum.getID());
            }
        } finally {
            cursor.close();
            db.close();
        }
        return equipmentEnumList;
    }

    /**
     * Gets a complete user by the id.
     * (uid),(Name),(age),(weight),(height), (gender), (trainingtype)
     * @param id The id of the user to get.
     * @return The completed user matching the id.
     */
    public User getUserById(int id) {
        User user = null;

        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                FitnessDBContract.UserEntry.COLUMN_NAME_UID,
                FitnessDBContract.UserEntry.COLUMN_NAME_NAME,
                FitnessDBContract.UserEntry.COLUMN_NAME_AGE,
                FitnessDBContract.UserEntry.COlUMN_NAME_WEIGHT,
                FitnessDBContract.UserEntry.COlUMN_NAME_HEIGHT,
                FitnessDBContract.UserEntry.COLUMN_NAME_GENDER,
                FitnessDBContract.UserEntry.COLUMN_NAME_TRAINING_TYPE
        };


        String selection = FitnessDBContract.UserEntry.COLUMN_NAME_UID + " = ? ";
        String[] selectionArgs = { Integer.toString(id) };

        Cursor cursorUser = doAndroidQuery(db, FitnessDBContract.UserEntry.TABLE_NAME, projection
                , selection, selectionArgs, null, null, null);

        if (cursorUser.moveToFirst()) {
          /*  System.out.println("COLUMNS: " + cursorUser.getColumnCount()
                   + " NAMES: " + cursorUser.getColumnName(0) + ", "  //int
                    + cursorUser.getColumnName(1) + ", "               //int
                    + cursorUser.getColumnName(2) + ", "               //int
                    + cursorUser.getColumnName(3) + ", "               //int
                    + cursorUser.getColumnName(4) + ", "               //int
                    + cursorUser.getColumnName(5) + ", "               //int
                    + cursorUser.getColumnName(6)); */
            user = new User(cursorUser.getInt(0), cursorUser.getString(1)
                    ,cursorUser.getInt(2), GenderEnum.getEnumById(cursorUser.getInt(5))
                    , cursorUser.getInt(3), cursorUser.getInt(4)
                    , WorkoutEnum.getEnumById(cursorUser.getInt(6)));
        }

        cursorUser.close();
        if (user != null) {
            List<LimitationEnum> limitations = getLimitationsByUserId(db, id);
            user.setLimitations(limitations);
            List<EquipmentEnum> equipmentEnumList = getEquipmentsByUserId(db, id);
            user.setEquipments(equipmentEnumList);
            List<Integer> rollList = getRollsByUserId(db, id);
            user.setRolls(rollList);
        }

        db.close();

        return user;
    }

    public List<Integer> getRollsByUserId (int id) {
        SQLiteDatabase db = getReadableDatabase();

        List<Integer> rolls = getRollsByUserId(db, id);

        db.close();

        return rolls;
    }

    public List<Integer> getRollsByUserId (SQLiteDatabase db, int id) {
        List<Integer> rolls = new ArrayList<>();

        String[] projection = {
                FitnessDBContract.RollUserRelation.COLUMN_NAME_RID
        };

        String selection = FitnessDBContract.RollUserRelation.COLUMN_NAME_UID + " = ? ";
        String[] selectionArgs = { Integer.toString(id) };

        Cursor cursor = doAndroidQuery(db, FitnessDBContract.RollUserRelation.TABLE_NAME, projection
                , selection, selectionArgs, null, null, null);

        while (cursor.moveToNext()) {
            rolls.add(cursor.getInt(0));
        }
        System.out.println("<<<<<<<<<<: " + rolls.size() + " FOR USED: " + id);
        cursor.close();
        return rolls;
    }

    /**
     * Used to enable other methods to call the query with their own instance of DB as not to open
     * multiple possibly unnecessary DB connections. Android calls the operations of opening one
     * expensive.
     * @param id the id of a user to get the limitations of
     * @return the list of all the limitations the user has.
     */
    public List<LimitationEnum> getLimitationsByUserId(int id) {
        SQLiteDatabase db = getReadableDatabase();
        List<LimitationEnum> limitations = getLimitationsByUserId(db, id);
        db.close();
        return limitations;
    }

    public void getLimitationsForListOfUser (List<Pair<User, Double>> users){
        SQLiteDatabase db = getReadableDatabase();
        for (Pair<User, Double> u : users) {
            List<LimitationEnum> limitations = getLimitationsByUserId(db, u.getFirst().getUid());
            u.getFirst().setLimitations(limitations);
        }
        db.close();
    }

    public List<LimitationEnum> getLimitationsByUserId(SQLiteDatabase db, int id) {
        List<LimitationEnum> limitations = new ArrayList<>();

        String[] projection = {
                FitnessDBContract.LimitationsUserRelation.COLUMN_NAME_LID
        };

        String selection = FitnessDBContract.LimitationsUserRelation.COLUMN_NAME_UID + " = ?";

        String[] selectionArgs = {Integer.toString(id)};

        Cursor cursor = doAndroidQuery(db,FitnessDBContract.LimitationsUserRelation.TABLE_NAME
                , projection, selection, selectionArgs, null, null, null);

        while(cursor.moveToNext()) {
            limitations.add(LimitationEnum.getEnumByID(cursor.getInt(0)));
            System.out.println(LimitationEnum.getEnumByID(cursor.getInt(0)).getLabel());
        }

        cursor.close();
        return limitations;
    }

    public List<EquipmentEnum> getEquipmentsByUserId(SQLiteDatabase db, int id) {
        List<EquipmentEnum> equipmentEnumList = new ArrayList<>();

        String[] projection = {FitnessDBContract.UserEquipmentRelation.COLUMN_NAME_EQ_ID};
        String selection = FitnessDBContract.UserEquipmentRelation.COLUMN_NAME_UID + " = ?";
        String[] selectionArgs = {Integer.toString(id)};

        Cursor cursor = doAndroidQuery(db, FitnessDBContract.UserEquipmentRelation.TABLE_NAME
                , projection, selection, selectionArgs, null, null, null);

        while(cursor.moveToNext()){
            equipmentEnumList.add(EquipmentEnum.getEnumById(cursor.getInt(0)));
        }

        cursor.close();

        return equipmentEnumList;
    }

    public void updateExerciseData(int exerciseID, int planID, int sets, int reps, int weight, int breakTime) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FitnessDBContract.PlanExerciseRelationEntry.COLUMN_NAME_REPS, reps);
        values.put(FitnessDBContract.PlanExerciseRelationEntry.COLUMN_NAME_SETS, sets);
        values.put(FitnessDBContract.PlanExerciseRelationEntry.COLUMN_NAME_BREAK, breakTime);
        values.put(FitnessDBContract.PlanExerciseRelationEntry.COLUMN_NAME_WEIGHT, weight);

        db.update(FitnessDBContract.PlanExerciseRelationEntry.TABLE_NAME, values
                , FitnessDBContract.PlanExerciseRelationEntry.COLUMN_NAME_EID + " = ? AND "
                        + FitnessDBContract.PlanExerciseRelationEntry.COLUMN_NAME_PID + " = ?"
                ,new String[]{Integer.toString(exerciseID), Integer.toString(planID)});

        db.close();
    }

    public List<User> getAllUser () {
        List<User> allUser = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        String[] projection = {
                FitnessDBContract.UserEntry.COLUMN_NAME_UID,
                FitnessDBContract.UserEntry.COLUMN_NAME_NAME,
                FitnessDBContract.UserEntry.COLUMN_NAME_AGE,
                FitnessDBContract.UserEntry.COLUMN_NAME_GENDER,
                FitnessDBContract.UserEntry.COlUMN_NAME_HEIGHT,
                FitnessDBContract.UserEntry.COlUMN_NAME_WEIGHT,
                FitnessDBContract.UserEntry.COLUMN_NAME_TRAINING_TYPE
        };
        Cursor cursor = doAndroidQuery(db, FitnessDBContract.UserEntry.TABLE_NAME, projection
                , null, null, null, null, null);

        while(cursor.moveToNext()) {
            allUser.add(new User(cursor.getInt(0), cursor.getString(1)
                    , cursor.getInt(2),GenderEnum.getEnumById(cursor.getInt(3))
                    , cursor.getInt(5), cursor.getInt(4)
                    , WorkoutEnum.getEnumById(cursor.getInt(6))));
        }

        cursor.close();
        db.close();
        return allUser;
    }

    public List<Exercise> getAllExercises(int planId) {
        List<Exercise> allExercises = new ArrayList<>() ;
        SQLiteDatabase db = getReadableDatabase();

        String[] projection = {
            FitnessDBContract.ExerciseEntry.COLUMN_NAME_EID,
            FitnessDBContract.ExerciseEntry.COLUMN_NAME_TITLE,
            FitnessDBContract.ExerciseEntry.COLUMN_NAME_PRIME_MUSCLE,
            FitnessDBContract.ExerciseEntry.COLUMN_NAME_SECONDARY_MUSCLE,
            FitnessDBContract.ExerciseEntry.COLUMN_NAME_EXERCISE_TYPE,
            FitnessDBContract.ExerciseEntry.COLUMN_NAME_EXPLANATION,
            FitnessDBContract.ExerciseEntry.COLUMN_NAME_MOVEMENT_TYPE,
            FitnessDBContract.ExerciseEntry.COLUMN_NAME_EQUIPMENT,
            FitnessDBContract.ExerciseEntry.COLUMN_NAME_ILLUSTRATION_LINK,
            FitnessDBContract.ExerciseEntry.COLUMN_NAME_DURATION_REP,
            FitnessDBContract.ExerciseEntry.COLUMN_NAME_IS_EXPLOSIVE,
            FitnessDBContract.ExerciseEntry.COLUMN_NAME_IS_BODYWEIGHT,
        };

        Cursor cursor = doAndroidQuery(db,FitnessDBContract.ExerciseEntry.TABLE_NAME, projection, null
                , null, null, null, null);
        System.out.println(cursor);
        while (cursor.moveToNext()) {
            System.out.println("HIT AN EXERCISE");
            allExercises.add(new Exercise(planId, cursor.getInt(0), cursor.getString(1)
                    , 0, 0, 0, 0, MuscleEnum.getEnumByInt(cursor.getInt(2))
                    , MuscleEnum.getEnumByInt(cursor.getInt(3)), cursor.getInt(9)
                    , ExerciseTypeEnum.getEnumByInt(cursor.getInt(4))
                    , EquipmentEnum.getEnumById(cursor.getInt(7)), cursor.getString(5)
                    , MovementTypeEnum.getEnumById(cursor.getInt(6))
                    , (cursor.getInt(10)==1), (cursor.getInt(11)==1)
                    , cursor.getString(8)));
        }

        cursor.close();
        db.close();
        return  allExercises;
    }

    public void addPlanToUser(ExerciseList plan, int userId){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues valuesPlan = new ContentValues();
        valuesPlan.put(FitnessDBContract.PlanEntry.COLUMN_NAME_GOAL, plan.getGoal().getID());
        valuesPlan.put(FitnessDBContract.PlanEntry.COLUMN_NAME_MUSCLE_GROUP, plan.getMuscle_group().getID());
        valuesPlan.put(FitnessDBContract.PlanEntry.COLUMN_NAME_NAME, plan.getPlan_name());
        valuesPlan.put(FitnessDBContract.PlanEntry.COLUMN_NAME_RATING, 0);

        long id = db.insert(FitnessDBContract.PlanEntry.TABLE_NAME, null,valuesPlan);

        ContentValues valuesPlanExerciseIDs = new ContentValues();
        for (Exercise e : plan.getExercises()) {
            valuesPlanExerciseIDs.put(FitnessDBContract.PlanExerciseRelationEntry.COLUMN_NAME_EID, e.getExerciseID());
            valuesPlanExerciseIDs.put(FitnessDBContract.PlanExerciseRelationEntry.COLUMN_NAME_PID, id);
            valuesPlanExerciseIDs.put(FitnessDBContract.PlanExerciseRelationEntry.COLUMN_NAME_WEIGHT, e.getWeight());
            valuesPlanExerciseIDs.put(FitnessDBContract.PlanExerciseRelationEntry.COLUMN_NAME_BREAK, e.getBreakTime());
            valuesPlanExerciseIDs.put(FitnessDBContract.PlanExerciseRelationEntry.COLUMN_NAME_SETS, e.getSetNumber());
            valuesPlanExerciseIDs.put(FitnessDBContract.PlanExerciseRelationEntry.COLUMN_NAME_REPS, e.getRepNumber());

            db.insert(FitnessDBContract.PlanExerciseRelationEntry.TABLE_NAME, null, valuesPlanExerciseIDs);
        }

        ContentValues valuesUserPlanRelation = new ContentValues();
        valuesUserPlanRelation.put(FitnessDBContract.UserPlanRelation.COLUMN_NAME_PID, id);
        valuesUserPlanRelation.put(FitnessDBContract.UserPlanRelation.COLUMN_NAME_UID, userId);

        db.insert(FitnessDBContract.UserPlanRelation.TABLE_NAME, null, valuesUserPlanRelation);

        db.close();
    }

    /**
     * This method takes in the result of a CBR query for exercises and completes it by getting
     * the complete exercises from the DB. It is handled here to prevent from opening and
     * closing the db connection multiple times.
     * @param result
     * @return
     */
    public List<Pair<Exercise, Double>> completeExerciseQueryResults (List<Pair<Integer, Double>> result) {
        List<Pair<Exercise, Double>> asList = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();

        for (Pair<Integer, Double> p : result) {
            asList.add(new Pair<>(getExerciseByID(db, p.getFirst()), p.getSecond()));
        }

        db.close();
        return  asList;
    }

    public Exercise getExerciseByID (SQLiteDatabase db, int exerciseID) {
        Exercise exercise = null;
        String[] projection = {
                FitnessDBContract.ExerciseEntry.COLUMN_NAME_EID,
                FitnessDBContract.ExerciseEntry.COLUMN_NAME_TITLE,
                FitnessDBContract.ExerciseEntry.COLUMN_NAME_PRIME_MUSCLE,
                FitnessDBContract.ExerciseEntry.COLUMN_NAME_SECONDARY_MUSCLE,
                FitnessDBContract.ExerciseEntry.COLUMN_NAME_EXERCISE_TYPE,
                FitnessDBContract.ExerciseEntry.COLUMN_NAME_EXPLANATION,
                FitnessDBContract.ExerciseEntry.COLUMN_NAME_ILLUSTRATION_LINK,
                FitnessDBContract.ExerciseEntry.COLUMN_NAME_EQUIPMENT,
                FitnessDBContract.ExerciseEntry.COLUMN_NAME_DURATION_REP,
                FitnessDBContract.ExerciseEntry.COLUMN_NAME_MOVEMENT_TYPE,
                FitnessDBContract.ExerciseEntry.COLUMN_NAME_IS_EXPLOSIVE,
                FitnessDBContract.ExerciseEntry.COLUMN_NAME_IS_BODYWEIGHT
        };

        String selection = FitnessDBContract.ExerciseEntry.COLUMN_NAME_EID + " = ?";
        String[] selectionParams = {Integer.toString(exerciseID)};


        Cursor cursor = doAndroidQuery(db, FitnessDBContract.ExerciseEntry.TABLE_NAME, projection
                , selection, selectionParams, null, null, null);

        if (cursor.moveToFirst()) {
            exercise = new Exercise(cursor.getInt(0), cursor.getString(1)
                    , MuscleEnum.getEnumByInt(cursor.getInt(2))
                    , MuscleEnum.getEnumByInt(cursor.getInt(3))
                    , cursor.getInt(8), ExerciseTypeEnum.getEnumByInt(cursor.getInt(4))
                    , EquipmentEnum.getEnumById(cursor.getInt(7)), cursor.getString(5)
                    , MovementTypeEnum.getEnumById(cursor.getInt(9))
                    , (cursor.getInt(10) == 1), (cursor.getInt(11) == 1)
                    , cursor.getString(6));
        }

        cursor.close();
        return exercise;
    }

    public void updatePlanExerciseRelation (int pid, Exercise newExercise, Exercise oldExercise) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FitnessDBContract.PlanExerciseRelationEntry.COLUMN_NAME_EID, newExercise.getExerciseID());
        values.put(FitnessDBContract.PlanExerciseRelationEntry.COLUMN_NAME_REPS, newExercise.getRepNumber());
        values.put(FitnessDBContract.PlanExerciseRelationEntry.COLUMN_NAME_SETS, newExercise.getSetNumber());
        values.put(FitnessDBContract.PlanExerciseRelationEntry.COLUMN_NAME_BREAK, newExercise.getBreakTime());
        values.put(FitnessDBContract.PlanExerciseRelationEntry.COLUMN_NAME_WEIGHT, newExercise.getWeight());

        int update = db.update(FitnessDBContract.PlanExerciseRelationEntry.TABLE_NAME, values
                , FitnessDBContract.PlanExerciseRelationEntry.COLUMN_NAME_EID + " = ? AND "
                        + FitnessDBContract.PlanExerciseRelationEntry.COLUMN_NAME_PID + " = ?"
                , new String[]{Integer.toString(oldExercise.getExerciseID()), Integer.toString(pid)});
        db.close();
    }

    /**
     * Returns a query with seven columns: uid, pid, pid, name, goal, sub_goal, rating
     */
    private static final String SQL_GET_PLAN_BY_ID= "SELECT * FROM "
            + FitnessDBContract.UserPlanRelation.TABLE_NAME + " rel INNER JOIN "
            + FitnessDBContract.PlanEntry.TABLE_NAME + " plans ON "
            + "rel." + FitnessDBContract.UserPlanRelation.COLUMN_NAME_PID + "="
            + "plans." + FitnessDBContract.PlanEntry.COLUMN_NAME_PID + " WHERE "
            + "rel." + FitnessDBContract.UserPlanRelation.COLUMN_NAME_UID + " = ? AND "
            + "plans." + FitnessDBContract.UserPlanRelation.COLUMN_NAME_PID + " = ?";

    public ExerciseList getExerciseListByID(int pid, int uid, int eid) {
        SQLiteDatabase db = getReadableDatabase();

        System.out.println("Getting new plan for :" + pid + " udi: " + uid);

        String[] values = { Integer.toString(uid), Integer.toString(pid) };

        ExerciseList plan = null;

        Cursor cursor = db.rawQuery(SQL_GET_PLAN_BY_ID, values);
        while(cursor.moveToNext()) {
            plan = new ExerciseList(cursor.getInt(2)
                    , cursor.getInt(4), cursor.getInt(5)
                    ,cursor.getString(3));
        }
        cursor.close();

        String completedQuery = SQL_GET_EXERCISES_BY_PLANS;
        completedQuery += "?)";
        Cursor exerciseQuery = db.rawQuery(completedQuery, new String[]{Integer.toString(plan.getPlan_id())});
        List<Exercise> exercises = cursorToExercises(exerciseQuery);

        for (Exercise e : exercises) {
            plan.addExercise(e);
        }
        exerciseQuery.close();
        db.close();
        return plan;
    }

    public void updateUserProfile(int userID, List<Integer> addedEquipment, List<Integer> removedEquipment
            , List<Integer> addedLimitations, List<Integer> removedLimitations, int age, int weight
            , int height, int gender, int workoutType) {
        SQLiteDatabase db = getWritableDatabase();

        for (Integer i : addedEquipment) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(FitnessDBContract.UserEquipmentRelation.COLUMN_NAME_UID, userID);
            contentValues.put(FitnessDBContract.UserEquipmentRelation.COLUMN_NAME_EQ_ID, i);
            db.insert(FitnessDBContract.UserEquipmentRelation.TABLE_NAME, null, contentValues);
        }

        for (Integer i : addedLimitations) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(FitnessDBContract.LimitationsUserRelation.COLUMN_NAME_UID, userID);
            contentValues.put(FitnessDBContract.LimitationsUserRelation.COLUMN_NAME_LID, i);
            db.insert(FitnessDBContract.LimitationsUserRelation.TABLE_NAME, null, contentValues);
        }

        for (Integer i : removedEquipment) {
            String where = FitnessDBContract.UserEquipmentRelation.COLUMN_NAME_UID + " = ? AND "
                    + FitnessDBContract.UserEquipmentRelation.COLUMN_NAME_EQ_ID + " = ?";
            db.delete(FitnessDBContract.UserEquipmentRelation.TABLE_NAME, where
                    , new String[]{ Integer.toString(userID), Integer.toString(i)});
        }

        for (Integer i : removedLimitations) {
            String where = FitnessDBContract.LimitationsUserRelation.COLUMN_NAME_UID + " = ? AND "
                    + FitnessDBContract.LimitationsUserRelation.COLUMN_NAME_LID + " = ?";
            db.delete(FitnessDBContract.LimitationsUserRelation.TABLE_NAME, where
                    , new String[]{ Integer.toString(userID), Integer.toString(i)});
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(FitnessDBContract.UserEntry.COlUMN_NAME_HEIGHT, height);
        contentValues.put(FitnessDBContract.UserEntry.COLUMN_NAME_AGE, age);
        contentValues.put(FitnessDBContract.UserEntry.COlUMN_NAME_WEIGHT, weight);
        contentValues.put(FitnessDBContract.UserEntry.COLUMN_NAME_GENDER, gender);
        contentValues.put(FitnessDBContract.UserEntry.COLUMN_NAME_TRAINING_TYPE, workoutType);

        String where = FitnessDBContract.UserEntry.COLUMN_NAME_UID + " = ?";

        db.update(FitnessDBContract.UserEntry.TABLE_NAME, contentValues, where, new String[]{Integer.toString(userID)} );

        db.close();
    }

    public List<Exercise> getAllPossibleExercisesForUser(int muscleGroupID, List<EquipmentEnum> equipments) {
        SQLiteDatabase db = getReadableDatabase();
        boolean first = true;

        StringBuilder selection = new StringBuilder(FitnessDBContract.ExerciseEntry.COLUMN_NAME_EQUIPMENT + " IN(");
        for (EquipmentEnum e : equipments) {
            if (first) {
                selection.append("?");
                first = false;
            } else {
                selection.append(",?");
            }
        }
        selection.append(")");

        String[] selectionArgs = new String[equipments.size()];
        for (int i = 0; i < equipments.size(); i++) {
            selectionArgs[i] = Integer.toString(equipments.get(i).getID());
        }

        Cursor cursor = doAndroidQuery(db, FitnessDBContract.ExerciseEntry.TABLE_NAME, null, selection.toString()
                , selectionArgs, null ,null,null);

        List<Exercise> exercises = cursorToBasicExercise(cursor);

        cursor.close();
        db.close();
        return  exercises;
    }
    /**
     * Order of fields: pid, eid, sets, reps, break, weight, eid, title, prime_muscle, secondary_muscle,exercise_type
     * explanation, illustration_link, equipment, duration_rep, movement_type, is_explosive, is_bodyweight
     */
    public List<Exercise> cursorToBasicExercise(Cursor cursor){
        List<Exercise> exercises = new ArrayList<>();

        while (cursor.moveToNext()) {

            /* System.out.println("COLUMNS: " + cursor.getColumnCount() + " For Exercises "
                    + " NAMES: " + cursor.getColumnName(0) + ", "  //int
                    + cursor.getColumnName(1) + ", "               //int
                    + cursor.getColumnName(2) + ", "               //int
                    + cursor.getColumnName(3) + ", "               //int
                    + cursor.getColumnName(4) + ", "               //int
                    + cursor.getColumnName(5) + ", "            //String
                    + cursor.getColumnName(6) + ", "               //int
                    + cursor.getColumnName(7) + ", "            //String
                    + cursor.getColumnName(8) + ", "               //int
                    + cursor.getColumnName(9) + ", "               //int
                    + cursor.getColumnName(10) + ", "              //int
                    + cursor.getColumnName(11) + ", "           //String
             );*/

            // eid, title, prime_muscle, secondary_muscle, exercise_type, explanation, illustration_link
            // , equipment, duration_rep, movement_type, is_explosive, is_bodyweight,
 // done as 0 represents false as would be the expression.
            exercises.add(new Exercise(0, cursor.getInt(0)
                    , cursor.getString(1), 0, 0, 0, 0
                    , MuscleEnum.getEnumByInt(cursor.getInt(2))
                    , MuscleEnum.getEnumByInt(cursor.getInt(3))
                    , cursor.getInt(8), ExerciseTypeEnum.getEnumByInt(cursor.getInt(4))
                    , EquipmentEnum.getEnumById(cursor.getInt(7))
                    , cursor.getString(5), MovementTypeEnum.getEnumById(cursor.getInt(9))
                    , (cursor.getInt(10) == 1), (cursor.getInt(11) == 1)
                    , cursor.getString(6)));
        }

        return exercises;
    }


    /**
     * Method to aggregate the DB.query calls that can be done via the android provided SQL helper.
     * @param db the db to run the query on
     * @param tableName the table to run the query on
     * @param projection the projection for the query
     * @param selection the select statement of the query
     * @param selectionArgs the values for the select statement
     * @param groupBy the group by statement
     * @param having the having statement
     * @param orderBy the order by statement
     * @return a cursor resulting from the constructed query
     */
    private Cursor doAndroidQuery(@NonNull SQLiteDatabase db, String tableName, String[] projection
            , String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        return db.query(
                tableName,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                groupBy,                   // don't group the rows
                having,                   // don't filter by row groups
                orderBy// The sort order
        );
    }
}
