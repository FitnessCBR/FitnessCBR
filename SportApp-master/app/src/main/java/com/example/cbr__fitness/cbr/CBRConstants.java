package com.example.cbr__fitness.cbr;

import android.content.Context;
import android.os.Bundle;
import android.util.Pair;

import com.example.cbr__fitness.data.Exercise;
import com.example.cbr__fitness.data.ExerciseList;
import com.example.cbr__fitness.data.User;
import com.example.cbr__fitness.databasehelper.FitnessDBSqliteHelper;
import com.example.cbr__fitness.enums.EquipmentEnum;
import com.example.cbr__fitness.enums.GenderEnum;
import com.example.cbr__fitness.enums.GoalEnum;
import com.example.cbr__fitness.enums.LimitationEnum;
import com.example.cbr__fitness.enums.MuscleEnum;
import com.example.cbr__fitness.enums.MuscleGroupEnum;
import com.example.cbr__fitness.enums.WorkoutEnum;
import com.example.cbr__fitness.logic.LogUtil;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import de.dfki.mycbr.core.similarity.config.AmalgamationConfig;

public class CBRConstants {

    public static final double MATCHING_MUSCLE_GROUPS = 1.0;

    public static final double LEG_OTHER_MUSCLE_GROUP = 0.0;

    public static final double STOMACH_OTHER_MUSCLE_GROUP = 0.0;

    public static final double ARM_CHEST_MUSCLE_GROUP = 0.4;

    public static final double CHEST_SHOULDER_MUSCLE_GROUP = 0.6;

    public static final double ARM_SHOULDER_MUSCLE_GROUP = 0.5;
    //Assuming roughly 7 min for an exercise and representation in seconds.
    //Double is used to get the result into a Double space as needed for similarity
    public static final double MAX_DURATION_DIFFERENCE = 900.0;

    //-------------------------- WEIGHTS PERSON CBR------------------------------------
    public static final double WEIGHT_AGE = 2;

    public static final double WEIGHT_LIMITATION = 18;

    public static final double WEIGHT_TRAINING_TYPE = 6;

    public static final double WEIGHT_BMI = 3;

    public static final double WEIGHT_GENDER = 4;

    public static final double WEIGHT_HEIGHT = 2;

    public static final double WEIGHT_WEIGHT = 2;
    //-------------------------- WEIGHTS PERSON END--------------------------------
    public static final double WEIGHT_DURATION = 5;

    public static final double WEIGHT_GOAL = 3;

    public static final double WEIGHT_EQUIPMENT_PLAN = 3;
    //-------------------------- EQ WEIGHTS-------------------------
    public static final double WEIGHT_EXERCISE_TYPE = 8; //Multi /Single joint

    public static final double WEIGHT_IS_EXPLOSIVE = 7;

    public static final double WEIGHT_MOVEMENT_TYPE = 4; // Push pull isometisch

    public static final double WEIGHT_PRIMARY_MUSCLE = 20;

    public static final double WEIGHT_SECONDARY_MUSCLE = 2;

    public static final double WEIGHT_IS_BODYWEIGHT = 8;

    public static final double WEIGHT_EQUIPMENT = 15;

    public static final double USER_SIMILARITY_THRESHOLD = 0.3;

    public static final double EXERCISE_LIST_SIMILARITY_THRESHOLD = 0.2;

    public static final int MAX_EQUIPMENT_DIFFERENCE = 5;

    public static final double WEIGHT_PERSON = 0.5;

    public static HashMap<Pair<LimitationEnum, LimitationEnum>, Double> LIMITATION_SIMILARITIES;

    public static HashMap<Pair<GoalEnum, GoalEnum>, Double> GOAL_SIMILARITIES;

    public static List<EquipmentEnum> equipmentsOfSportStudios = Arrays.asList(EquipmentEnum.Hantel, EquipmentEnum.Hantelbank
            , EquipmentEnum.KETTELBELL, EquipmentEnum.Klimmzugstange, EquipmentEnum.Langhantel);

    public static final AmalgamationConfig PERSON_AMALGAMATION_TYPE = AmalgamationConfig.WEIGHTED_SUM;

    static {
        LIMITATION_SIMILARITIES = new HashMap<>();
        LIMITATION_SIMILARITIES.put(new Pair<>(LimitationEnum.WRISTS, LimitationEnum.ELBOWS), 0.5);
        LIMITATION_SIMILARITIES.put(new Pair<>(LimitationEnum.ELBOWS, LimitationEnum.WRISTS ), 0.5);
        LIMITATION_SIMILARITIES.put(new Pair<>(LimitationEnum.ELBOWS, LimitationEnum.SHOULDER), 0.5);
        LIMITATION_SIMILARITIES.put(new Pair<>(LimitationEnum.SHOULDER, LimitationEnum.ELBOWS ), 0.5);
        LIMITATION_SIMILARITIES.put(new Pair<>(LimitationEnum.HIPS, LimitationEnum.KNEES), 0.3);
        LIMITATION_SIMILARITIES.put(new Pair<>(LimitationEnum.KNEES, LimitationEnum.HIPS), 0.3);
        //Adding the diagonal of the similarity matrix
        for (LimitationEnum e : LimitationEnum.values()) {
            LIMITATION_SIMILARITIES.put(new Pair<>(e, e), 1.0);
        }
        GOAL_SIMILARITIES = new HashMap<>();
        GOAL_SIMILARITIES.put(new Pair<>(GoalEnum.MAX_STRENGTH, GoalEnum.MUSCLE_MASS), 0.6);
        GOAL_SIMILARITIES.put(new Pair<>(GoalEnum.MUSCLE_MASS, GoalEnum.MAX_STRENGTH), 0.6);
        GOAL_SIMILARITIES.put(new Pair<>(GoalEnum.MAX_STRENGTH, GoalEnum.STRENGTH_ENDURANCE), 0.6);
        GOAL_SIMILARITIES.put(new Pair<>(GoalEnum.STRENGTH_ENDURANCE, GoalEnum.MAX_STRENGTH), 0.6);
        GOAL_SIMILARITIES.put(new Pair<>(GoalEnum.MUSCLE_MASS, GoalEnum.STRENGTH_ENDURANCE), 0.6);
        GOAL_SIMILARITIES.put(new Pair<>(GoalEnum.STRENGTH_ENDURANCE, GoalEnum.MUSCLE_MASS), 0.6);
        for (GoalEnum g : GoalEnum.values()) {
            GOAL_SIMILARITIES.put(new Pair<>(g,g), 1.0);
        }
    }

    public static double getMuscleGroupSimilarityMultiplier(MuscleGroupEnum request, MuscleGroupEnum query){
        if (request == query) { //if the request and the case match return 1
            return MATCHING_MUSCLE_GROUPS;
        } else {
            return 0;
        }
    }

    public static double getSimilarityForDuration (double targetDuration, double queriedDuration) {
        double sim = 0.0;
        //Done to prevent a division of
            //Assuming a linear reduction in similarity reaching 0 as MAX_DURATION_DIFFERENCE is reached.
            double temp = (MAX_DURATION_DIFFERENCE - (Math.abs(targetDuration - queriedDuration)));
            if (temp > 0) {
                sim = temp /MAX_DURATION_DIFFERENCE;
            }
        return  sim * WEIGHT_DURATION;
    }

    public static double getCompleteWeightSumPerson() {
        return getCompleteWeightSumMyCBRPerson() + WEIGHT_LIMITATION;
    }
    public static double getCompleteWeightSumMyCBRPerson() {
        return WEIGHT_AGE + WEIGHT_WEIGHT + WEIGHT_BMI + WEIGHT_GENDER + WEIGHT_HEIGHT + WEIGHT_TRAINING_TYPE;
    }
    public static double getCompleteWeightSumPlan() {
        return WEIGHT_GOAL + WEIGHT_EQUIPMENT_PLAN + WEIGHT_DURATION;
    }

    public static double getCompleteWeights () {
        return getCompleteWeightSumPerson() + getCompleteWeightSumPlan();
    }

    public static double LimitationSimilarity(List<LimitationEnum> logged, List<LimitationEnum> queried) {
        double result = (LimitationEnum.values().length * 1.0);
        for (LimitationEnum e : logged) {
            if (!queried.contains(e)) {
                result -=1;
            }
        }
        for (LimitationEnum e : queried) {
            if (!logged.contains(e)) {
                result -= 1;
            }
        }
        return  result/LimitationEnum.values().length;
    }

    public static double LimitationSimilarityRelativeToLimitationCount(List<LimitationEnum> logged
            , List<LimitationEnum> queried){
        double result = Math.max(logged.size(), queried.size()); //setting result to max length
        double base = Math.max(logged.size(), queried.size());
        if (logged.size() > 0 || queried.size() > 0) {
            List<LimitationEnum> most = logged.size() >= queried.size() ? logged : queried;
            List<LimitationEnum> least = logged.size() < queried.size() ? logged : queried;
            for (LimitationEnum e : most) {
                if (!least.contains(e)) {
                    result -= 1;
                }
            }
            base = Math.max(logged.size(), queried.size());
            System.out.println(">>>> LIMITATION SIM : " + result/base);
        } else {
            result = 1;
            base = 1;
        }

        return result/base;
    }

    public static double getEquipmentSimilarity(List<EquipmentEnum> logged, List<EquipmentEnum> queried) {
        double result = MAX_EQUIPMENT_DIFFERENCE; //Assume Sim of 1 if each of the Equipments
        List<EquipmentEnum> unsupported = getListOfUnsupportedEquipments(logged, queried);
        System.out.println(">>>> SIZE OF UNSUPPORTED: " + unsupported.size());
        //Return similarity based on number of equipments required of a plan but not owned by a user
        return  (unsupported.size() >= result ? 0 : (result - unsupported.size())/MAX_EQUIPMENT_DIFFERENCE)
                * WEIGHT_EQUIPMENT_PLAN;
    }

    public static double getGoalSimilarity(GoalEnum target, GoalEnum query) {
        double goalSim;
        Double goalSimTemp = GOAL_SIMILARITIES.get(new Pair<>(target, query));
        goalSim = goalSimTemp!= null ? goalSimTemp : 0 ;

        return goalSim * WEIGHT_GOAL;
    }

    public static List<EquipmentEnum> getListOfUnsupportedEquipments(List<EquipmentEnum> logged
            , List<EquipmentEnum> queried) {
        List<EquipmentEnum> unsupportedEquipments = new ArrayList<>();
        boolean loggedContainsSportStudio = logged.contains(EquipmentEnum.FitnessStudio);
        for (EquipmentEnum e : queried) {
            System.out.println("QUERY: " + logged.contains(e)
                    + " Logged has: " + loggedContainsSportStudio + " is in St " + equipmentsOfSportStudios.contains(e)
                    + " FOR : " + e.getLabel());
            if (!logged.contains(e) && !(loggedContainsSportStudio && equipmentsOfSportStudios.contains(e))) {
                unsupportedEquipments.add(e);
            }
        }
        return  unsupportedEquipments;
    }

    public static List<de.dfki.mycbr.util.Pair<User, Double>> getUserFromCBR(User user, RetrievalUtil util, FitnessDBSqliteHelper helper) {

        List<de.dfki.mycbr.util.Pair<User, Double>> users = util.retrieveUser(user);
        //to finish the similarity of users, get the limitations for each returned user from the cbr query.
        helper.getLimitationsForListOfUser(users);
        Iterator<de.dfki.mycbr.util.Pair<User,Double>> iterator = users.iterator();
        while (iterator.hasNext()) {
            de.dfki.mycbr.util.Pair<User,Double> pair = iterator.next();

            double limitationSim = CBRConstants.LimitationSimilarityRelativeToLimitationCount(user.getLimitations()
                    , pair.getFirst().getLimitations());
            System.out.println(">>> LIMIT SIM FUER: " + pair.getFirst().getUid() + " VALUE: " + limitationSim);
            adjustPersonSimilarity(pair, limitationSim);
            /**
             * We reduce the returned user count by requiring a certain similarity
             * No limitation based on number, its unclear at this point how many plans these
             * users have.
             */

            if (pair.getSecond() < CBRConstants.USER_SIMILARITY_THRESHOLD) {
                iterator.remove();
            }
        }
        //Sort the user by their similarity, top to bottom, to enable cutting off those that do not meet a certain threshold.
        return users.stream()
                .sorted(Comparator.comparingDouble(de.dfki.mycbr.util.Pair<User,Double>::getSecond).reversed()).collect(Collectors.toList());
    }

    private static void adjustPersonSimilarity(de.dfki.mycbr.util.Pair<User, Double> user, double limitationSim) {
        double pastSim = user.getSecond();
        System.out.println(">>>> SIM PRE LIMITATION: " + pastSim);
        pastSim *= CBRConstants.getCompleteWeightSumMyCBRPerson(); //denormalize the sum by multiplying with the sum of weights used in the CBR query
        pastSim += CBRConstants.WEIGHT_LIMITATION * limitationSim;
        System.out.println(">>>> SIM AFTER LIMITATION: " + pastSim/CBRConstants.getCompleteWeightSumPerson());
        user.setSecond(pastSim/CBRConstants.getCompleteWeightSumPerson());
    }
    public static void adaptExerciseAfter(List<Exercise> similarExercises, Exercise exchanged
            , Context context, int[] initialValues) {

        int sumSets = 0;
        int sumReps = 0;
        int sumWeight =0;
        int sumBreakTime = 0;
        if (similarExercises.size() > 0) {
            for (Exercise e : similarExercises) {
                LogUtil.LogPlanSimilarity(context, "\t\t\t>Taking Values from: " + e.getName()
                        + "\n\t\t\tSets: " + e.getSetNumber() + " Reps: "
                        + e.getRepNumber() + " Weight: " + e.getWeight()
                        + " Break Time: " + e.getBreakTime());
                sumReps += e.getRepNumber();
                sumSets += e.getSetNumber();
                sumWeight += e.getWeight();
                sumBreakTime += e.getBreakTime();
            }
        } else {
            System.out.println("<<<<<<<<<<<<<<<<<<<< NOT WORKING");
            sumSets = initialValues[0];
            sumReps = initialValues[1];
            if (!exchanged.isBodyweight()) {
                sumWeight = initialValues[3];
            }
            sumBreakTime = initialValues[2];
        }
        exchanged.setWeight(similarExercises.size() == 0 ? sumWeight : sumWeight/similarExercises.size());
        exchanged.setSetNumber(similarExercises.size() == 0 ? sumSets : sumSets/similarExercises.size());
        exchanged.setRepNumber(similarExercises.size() == 0 ? sumReps : sumReps/similarExercises.size());
        exchanged.setBreakTime(similarExercises.size() == 0 ? sumBreakTime : sumBreakTime/similarExercises.size());
    }

    /**
     * Array will be sets, reps, break, weight
     * @param goal
     * @param experience
     * @return
     */
    public static int[] initExerciseValues (GoalEnum goal, WorkoutEnum experience) {
        int[] values = {2,8,120,10};;
        switch(goal) {
            case MUSCLE_MASS:
                switch(experience) {
                    case Beginner:
                        values = new int[] {2,8,120,10};
                        break;
                    case Advanced:
                        values = new int[] {3,9,140,15};
                        break;
                    case Pro:
                        values = new int[] {4,10,180,20};
                        break;
                }
                break;
            case MAX_STRENGTH:
                switch(experience) {
                    case Beginner:
                        values = new int[] {3,4,240,15};
                        break;
                    case Advanced:
                        values = new int[] {4,5,260,20};
                        break;
                    case Pro:
                        values = new int[] {5,5,300,30};
                        break;
                }
                break;
            case STRENGTH_ENDURANCE:
                switch(experience) {
                    case Beginner:
                        values = new int[] {1,20,30,5};
                        break;
                    case Advanced:
                        values = new int[] {2,20,50,8};
                        break;
                    case Pro:
                        values = new int[] {2,25,60,10};
                        break;
                }
                break;
            case POWER:
                switch(experience) {
                    case Beginner:
                        values = new int[] {3,4,180,5};
                        break;
                    case Advanced:
                        values = new int[] {4,6,220,10};
                        break;
                    case Pro:
                        values = new int[] {5,6,280,15};
                        break;
                }
                break;
        }
        return values;
    }
}
