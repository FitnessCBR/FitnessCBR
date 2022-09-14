package com.example.cbr__fitness.cbr;

import android.media.audiofx.DynamicsProcessing;

import com.example.cbr__fitness.activities.MainActivity;
import com.example.cbr__fitness.data.Exercise;
import com.example.cbr__fitness.data.ExerciseList;
import com.example.cbr__fitness.data.ResultCaseExercise;
import com.example.cbr__fitness.data.ResultCaseUser;
import com.example.cbr__fitness.data.User;
import com.example.cbr__fitness.databasehelper.FitnessDBSqliteHelper;
import com.example.cbr__fitness.enums.EquipmentEnum;
import com.example.cbr__fitness.enums.ExerciseTypeEnum;
import com.example.cbr__fitness.enums.GenderEnum;
import com.example.cbr__fitness.enums.GoalEnum;
import com.example.cbr__fitness.enums.MuscleEnum;
import com.example.cbr__fitness.enums.MuscleGroupEnum;
import com.example.cbr__fitness.enums.WorkoutEnum;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import de.dfki.mycbr.core.DefaultCaseBase;
import de.dfki.mycbr.core.Project;
import de.dfki.mycbr.core.casebase.Attribute;
import de.dfki.mycbr.core.casebase.Instance;
import de.dfki.mycbr.core.casebase.MultipleAttribute;
import de.dfki.mycbr.core.casebase.SymbolAttribute;
import de.dfki.mycbr.core.model.AttributeDesc;
import de.dfki.mycbr.core.model.BooleanDesc;
import de.dfki.mycbr.core.model.Concept;
import de.dfki.mycbr.core.model.FloatDesc;
import de.dfki.mycbr.core.model.IntegerDesc;
import de.dfki.mycbr.core.model.StringDesc;
import de.dfki.mycbr.core.model.SymbolDesc;
import de.dfki.mycbr.core.retrieval.Retrieval;
import de.dfki.mycbr.core.retrieval.Retrieval.RetrievalMethod;
import de.dfki.mycbr.core.similarity.Similarity;
import de.dfki.mycbr.util.Pair;

/**
 * @author Jobst-Julius Bartels
 */

// Diese Klasse stellt den Retrieval-Prozess dar.
public class RetrievalUtil {

    // CBR-System abhängige Attribute.
    private static CBREngine engine = new CBREngine();
    private static Project project;
    private static DefaultCaseBase cbUser;
    private static DefaultCaseBase cbExercise;
    private static Concept myConceptUser;
    private static Concept myConceptExercise;
    public static DefaultCaseBase getCbUser() {
        return cbUser;
    }
    public static DefaultCaseBase getCbExercise() {
        return cbExercise;
    }
    public static Concept getMyConceptUser() {
        return myConceptUser;
    }
    public static Concept getMyConceptExercise() {
        return myConceptExercise;
    }

    // Konstruktor der Klasse.
    public RetrievalUtil(String path) {
        this.project = engine.createProjectFromPRJ(path);
        this.cbUser = (DefaultCaseBase) project.getCaseBases().get(CBREngine.getCaseBaseUser());
        this.cbExercise = (DefaultCaseBase) project.getCaseBases().get(CBREngine.getCasebaseExercise());
        this.myConceptUser = project.getConceptByID(CBREngine.getConceptNameUser());
        this.myConceptExercise = project.getConceptByID(CBREngine.getConceptNameExercise());
    }

    public List<Pair<Exercise, Double>> retrieveExercise(FitnessDBSqliteHelper helper, Exercise exercise
        , List<EquipmentEnum> equipmentList, List<Integer> allIds) {

        List<Pair<Exercise, Double>> similarExercises = new ArrayList<>();

        System.out.println("CaseBase: " + cbExercise.getCases());

        for (AttributeDesc des : myConceptExercise.getAllAttributeDescs().values()) {
            System.out.println(des.getExpType() + "NAME: " + des.getName());
        }
        Retrieval ret = new Retrieval(myConceptExercise, cbExercise);
        ret.setRetrievalMethod(RetrievalMethod.RETRIEVE_SORTED);
        Instance query = ret.getQueryInstance();

        SymbolDesc equipment = (SymbolDesc) myConceptExercise.getAllAttributeDescs().get("equipment");
        myConceptExercise.getActiveAmalgamFct().setWeight(equipment, CBRConstants.WEIGHT_EQUIPMENT);
        IntegerDesc exerciseID = (IntegerDesc) myConceptExercise.getAllAttributeDescs().get("exerciseID");
        myConceptExercise.getActiveAmalgamFct().setActive(exerciseID, false);
        BooleanDesc exerciseType = (BooleanDesc) myConceptExercise.getAllAttributeDescs().get("is_multijoint");
        myConceptExercise.getActiveAmalgamFct().setWeight(exerciseType, CBRConstants.WEIGHT_EXERCISE_TYPE);
        BooleanDesc isExplosive = (BooleanDesc) myConceptExercise.getAllAttributeDescs().get("is_explosive");
        myConceptExercise.getActiveAmalgamFct().setWeight(isExplosive, CBRConstants.WEIGHT_IS_EXPLOSIVE);
        SymbolDesc movementType = (SymbolDesc) myConceptExercise.getAllAttributeDescs().get("movement_type");
        myConceptExercise.getActiveAmalgamFct().setWeight(movementType, CBRConstants.WEIGHT_MOVEMENT_TYPE);
        SymbolDesc primaryMuscle = (SymbolDesc) myConceptExercise.getAllAttributeDescs().get("primary_muscle");
        myConceptExercise.getActiveAmalgamFct().setWeight(primaryMuscle, CBRConstants.WEIGHT_PRIMARY_MUSCLE);
        SymbolDesc secondaryMuscle = (SymbolDesc) myConceptExercise.getAllAttributeDescs().get("secondary_muscle");
        myConceptExercise.getActiveAmalgamFct().setWeight(secondaryMuscle, CBRConstants.WEIGHT_SECONDARY_MUSCLE);
        BooleanDesc isBodyweight = (BooleanDesc) myConceptExercise.getAllAttributeDescs().get("is_bodyweight");
        myConceptExercise.getActiveAmalgamFct().setWeight(isBodyweight, CBRConstants.WEIGHT_IS_BODYWEIGHT);

        try {
            LinkedList<Attribute> attributes = new LinkedList<>();
            for (EquipmentEnum e : equipmentList) {
                attributes.add(equipment.getAttribute(e.getSymbol()));
            }
            MultipleAttribute<AttributeDesc> equipments = new MultipleAttribute<>(equipment, attributes);
            boolean added = query.addAttribute(equipment, equipments);
            query.addAttribute(exerciseType, (exercise.getType()== ExerciseTypeEnum.multiJoint));
            query.addAttribute(isExplosive, exercise.getIsExplosive());
            query.addAttribute(movementType, exercise.getMovementType().getLabel());
            query.addAttribute(primaryMuscle, exercise.getMuscle().getSymbol());
            query.addAttribute(secondaryMuscle, exercise.getSecondaryMuscle().getSymbol());
            query.addAttribute(isBodyweight, exercise.isBodyweight());
        } catch (ParseException exe) {
            System.out.println("Parse Excep: " + exe);
        }

        ret.start();
        List<Pair<Instance, Similarity>> result = ret.getResult();


        if (result.size() > 0) {
            List<Pair<Integer, Double>> exerciseIDs = new ArrayList<>();
            for (Pair<Instance, Similarity> p : result) {
                if (Integer.parseInt(p.getFirst().getAttributes().get(exerciseID).getValueAsString())
                        != exercise.getExerciseID()
                        && !allIds.contains(Integer.parseInt(p.getFirst().getAttributes()
                        .get(exerciseID).getValueAsString()))) {
                    exerciseIDs.add(new Pair<>(
                            Integer.parseInt(p.getFirst().getAttributes().get(exerciseID).getValueAsString())
                            , p.getSecond().getValue()));
                }
            }
            similarExercises = helper.completeExerciseQueryResults(exerciseIDs);
            similarExercises.removeIf(temp -> !equipmentList.contains(temp.getFirst().getEquipment()));
        }
        //Order the exercises left for consideration.
        return similarExercises.stream()
                .sorted(Comparator.comparingDouble(Pair<Exercise,Double>::getSecond).reversed())
                .collect(Collectors.toList());
    }

    public List<Pair<User, Double>> retrieveUser(User loggedUser) {
        List<Pair<User, Double>> similarUser = new ArrayList<>();
        System.out.println("Casebase: " + cbUser.getCases());

        System.out.println(myConceptUser.getName() + "  2. CONCEPT: ");

        for (AttributeDesc des: myConceptUser.getAllAttributeDescs().values()) {
            System.out.println(des.getExpType() + " NAME: " + des.getName());
        }

        Retrieval ret = new Retrieval(myConceptUser, cbUser);
        ret.setRetrievalMethod(RetrievalMethod.RETRIEVE_SORTED);
        Instance query = ret.getQueryInstance();

        // Query aus der Casebase.
        IntegerDesc age = (IntegerDesc) myConceptUser.getAllAttributeDescs().get("age");
        myConceptUser.getActiveAmalgamFct().setWeight(age, CBRConstants.WEIGHT_AGE);
        IntegerDesc height = (IntegerDesc) myConceptUser.getAllAttributeDescs().get("height");
        myConceptUser.getActiveAmalgamFct().setWeight(height, CBRConstants.WEIGHT_HEIGHT);
        SymbolDesc gender = (SymbolDesc) myConceptUser.getAllAttributeDescs().get("gender");
        myConceptUser.getActiveAmalgamFct().setWeight(gender, CBRConstants.WEIGHT_GENDER);
        SymbolDesc training_type = (SymbolDesc) myConceptUser.getAllAttributeDescs().get("training_type");
        myConceptUser.getActiveAmalgamFct().setWeight(training_type, CBRConstants.WEIGHT_TRAINING_TYPE);
        IntegerDesc weight = (IntegerDesc) myConceptUser.getAllAttributeDescs().get("weight");
        myConceptUser.getActiveAmalgamFct().setWeight(weight, CBRConstants.WEIGHT_WEIGHT);
        FloatDesc bmi = (FloatDesc) myConceptUser.getAllAttributeDescs().get("BMI");
        myConceptUser.getActiveAmalgamFct().setWeight(bmi, CBRConstants.WEIGHT_BMI);
        IntegerDesc userID = (IntegerDesc) myConceptUser.getAllAttributeDescs().get("userID");
        myConceptUser.getActiveAmalgamFct().setActive(userID, false);
        myConceptUser.getActiveAmalgamFct().setType(CBRConstants.PERSON_AMALGAMATION_TYPE);

        try  {
            query.addAttribute(age, age.getAttribute(loggedUser.getAgeN()));
            query.addAttribute(height, height.getAttribute(loggedUser.getHeight()));
            query.addAttribute(gender, gender.getAttribute(loggedUser.getGenderN().getLabel()));
            query.addAttribute(training_type, training_type.getAttribute(loggedUser.getWorkoutTypeN().getLabel()));
            query.addAttribute(weight, weight.getAttribute(loggedUser.getWeightN()));
            query.addAttribute(bmi, bmi.getAttribute(loggedUser.getBMI()));
        } catch (ParseException ece) {
            System.out.println("Parse Excep: " + ece);
        }
        ret.start();
        List<Pair<Instance, Similarity>> result = ret.getResult();

        if (result.size() > 0) {
            for (Pair<Instance, Similarity> p : result) {
                if (Integer.parseInt(p.getFirst().getAttributes().get(userID).getValueAsString()) != loggedUser.getUid()) {
                    similarUser.add(new Pair<>(new User(
                            Integer.parseInt(p.getFirst().getAttributes().get(userID).getValueAsString())
                            , "empty"
                            , Integer.parseInt(p.getFirst().getAttributes().get(age).getValueAsString())
                            , GenderEnum.getEnumByString(p.getFirst().getAttributes().get(gender).getValueAsString())
                            , Integer.parseInt(p.getFirst().getAttributes().get(weight).getValueAsString())
                            , Integer.parseInt(p.getFirst().getAttributes().get(height).getValueAsString())
                            , WorkoutEnum.getEnumByString(p.getFirst().getAttributes().get(training_type).getValueAsString()))
                            , p.getSecond().getValue()));
                }
                System.out.println("USER: "
                        + Integer.parseInt(p.getFirst().getAttributes().get(userID).getValueAsString())
                        + " " + p.getSecond().getValue());
            }
        }
        return similarUser;
    }

    @Deprecated
    // Methode für das Retrieval des Users.
    public ArrayList<ResultCaseUser> retrieveUser(User user, String musclePrio, ArrayList<Integer> weightList) throws ParseException {

        // ArrayList zum Speichern der Retrieval-Ergebnisse.
        ArrayList<ResultCaseUser> resultCaseUsers = new ArrayList<ResultCaseUser>();
        System.out.println("Casebase: " + cbUser.getCases());

        // Erstellen der Retrieval- & Query-Instanz.
        Retrieval ret = new Retrieval(myConceptUser, cbUser);
        ret.setRetrievalMethod(RetrievalMethod.RETRIEVE_SORTED);
        Instance query = ret.getQueryInstance();

        // Query aus der Casebase.
        SymbolDesc age = (SymbolDesc) myConceptUser.getAllAttributeDescs().get("age");
        SymbolDesc duration = (SymbolDesc) myConceptUser.getAllAttributeDescs().get("duration");
        SymbolDesc gender = (SymbolDesc) myConceptUser.getAllAttributeDescs().get("gender");
        SymbolDesc workType = (SymbolDesc) myConceptUser.getAllAttributeDescs().get("workType");
        SymbolDesc bodyType = (SymbolDesc) myConceptUser.getAllAttributeDescs().get("bodyType");
        SymbolDesc restriction = (SymbolDesc) myConceptUser.getAllAttributeDescs().get("restriction");

        // Attribute werden bestimmt.
        query.addAttribute(age, age.getAttribute(user.getAgeEnum().toString()));
        query.addAttribute(duration, duration.getAttribute(user.getDurationEnum().toString()));
        query.addAttribute(gender, gender.getAttribute(user.getGenderEnum().toString()));
        query.addAttribute(workType, workType.getAttribute(user.getWorkTypeEnum().toString()));
        query.addAttribute(bodyType, bodyType.getAttribute(user.getBodyTypeEnum().toString()));
        query.addAttribute(restriction, restriction.getAttribute(user.getResEnum().toString()));

        // Plan für Ergebnis, wird für die Suche deaktiviert!
        StringDesc plan = (StringDesc) myConceptUser.getAllAttributeDescs().get("plan");
        setWeightForAttr(query, plan, 0);

        // Gewichtung wird gesetzt.
        setWeightForAttr(query, age, weightList.get(0));
        setWeightForAttr(query, gender, weightList.get(1));
        setWeightForAttr(query, duration, weightList.get(2));
        setWeightForAttr(query, workType, weightList.get(3));
        setWeightForAttr(query, bodyType, weightList.get(4));
        setWeightForAttr(query, restriction, weightList.get(5));

        // Retrieval starten und Ergebnisse speichern.
        ret.start();
        List<Pair<Instance, Similarity>> result = ret.getResult();

        // Speichern der Ergebnisse in Arraylist.
        if (result.size() > 0) {
            for (int i = 0; i < result.size(); i++) {
                ResultCaseUser rc = new ResultCaseUser(result.get(i).getFirst().getName(),
                        result.get(i).getSecond().getValue(),
                        MainActivity.planBaseList.getCertainPlan(result.get(i).getFirst().getAttributes().get(plan).toString()),
                        result.get(i).getFirst().getAttributes().get(age).toString(),
                        result.get(i).getFirst().getAttributes().get(gender).toString(),
                        result.get(i).getFirst().getAttributes().get(workType).toString(),
                        result.get(i).getFirst().getAttributes().get(bodyType).toString(),
                        result.get(i).getFirst().getAttributes().get(duration).toString(),
                        result.get(i).getFirst().getAttributes().get(restriction).toString());
                if(isPlanValuable(rc, musclePrio)){
                    resultCaseUsers.add(rc);
                } else {
                    System.out.println("Plan wurde abgelehnt!");
                }
            }
        }
        return resultCaseUsers;
    }

    @Deprecated
    // Methode für das Retrieval der Exercise.
    public ArrayList<ResultCaseExercise> retrieveExercise(Exercise exercise) throws ParseException {

        // ArrayList zum Speichern der Retrieval-Ergebnisse.
        ArrayList<ResultCaseExercise> resultCaseExercises = new ArrayList<ResultCaseExercise>();
        System.out.println("Casebase: " + cbExercise.getCases());

        // Erstellen der Retrieval- & Query-Instanz.
        Retrieval ret = new Retrieval(myConceptExercise, cbExercise);
        ret.setRetrievalMethod(RetrievalMethod.RETRIEVE_SORTED);
        Instance query = ret.getQueryInstance();

        // Query aus der Casebase.
        IntegerDesc breakTime = (IntegerDesc) myConceptExercise.getAllAttributeDescs().get("breakTime");
        SymbolDesc exType = (SymbolDesc) myConceptExercise.getAllAttributeDescs().get("exType");
        SymbolDesc muscle = (SymbolDesc) myConceptExercise.getAllAttributeDescs().get("muscle");
        StringDesc name = (StringDesc) myConceptExercise.getAllAttributeDescs().get("name");
        IntegerDesc rating = (IntegerDesc) myConceptExercise.getAllAttributeDescs().get("rating");
        IntegerDesc rep = (IntegerDesc) myConceptExercise.getAllAttributeDescs().get("rep");
        IntegerDesc setNumber = (IntegerDesc) myConceptExercise.getAllAttributeDescs().get("setNumber");
        IntegerDesc time = (IntegerDesc) myConceptExercise.getAllAttributeDescs().get("time");
        IntegerDesc weight = (IntegerDesc) myConceptExercise.getAllAttributeDescs().get("weight");

        // Attribute werden bestimmt.
        query.addAttribute(breakTime, breakTime.getAttribute(exercise.getExBreak()));
        query.addAttribute(exType, exType.getAttribute(exercise.getExTypeEnum().toString()));
        query.addAttribute(muscle, muscle.getAttribute(exercise.getExMuscleEnum().toString()));
        query.addAttribute(name, name.getAttribute(exercise.getExName()));
        query.addAttribute(rating, rating.getAttribute(exercise.getExRating()));
        query.addAttribute(setNumber, setNumber.getAttribute(exercise.getExSetNumber()));
        query.addAttribute(time, time.getAttribute(exercise.getExTime()));
        query.addAttribute(weight, weight.getAttribute(exercise.getExWeight()));
        query.addAttribute(rep, rep.getAttribute(exercise.getExRep()));

        // Gewichtung der Attribute.
        setWeightForAttr(query, name, 0);
        setWeightForAttr(query, rating, 0);
        setWeightForAttr(query, weight, 0);

        // Retrieval starten und Ergebnisse speichern.
        ret.start();
        List<Pair<Instance, Similarity>> result = ret.getResult();

        // Speichern der Ergebnisse in Arraylist.
        if (result.size() > 0) {
            for (int i = 0; i < result.size(); i++) {
                ResultCaseExercise rc = new ResultCaseExercise(result.get(i).getFirst().getName(),
                        result.get(i).getSecond().getValue(),
                        MainActivity.exerciseBaseList.getCertainExercise(result.get(i).getFirst().getAttributes().get(name).toString()));
                if(isExerciseValuable(rc, exercise.getExMuscle())){
                    resultCaseExercises.add(rc);
                } else {
                    System.out.println("Plan wurde abgelehnt!");
                }
            }
        }
        return resultCaseExercises;
    }


    // Methode um einen Fall des User der CB hinzuzufügen.
    public void addCaseUser(ResultCaseUser rcUser)
            throws ParseException {

        // Name des neuen Falles.
        int caseId = cbUser.getCases().size() + 1;
        String id = "User #" + caseId;

        // Erstellen des Falles.
        Instance caze = new Instance(myConceptUser, id);

        // Hinzufügen der Attribute.
        SymbolDesc age = (SymbolDesc) myConceptUser.getAllAttributeDescs().get("age");
        SymbolDesc duration = (SymbolDesc) myConceptUser.getAllAttributeDescs().get("duration");
        SymbolDesc gender = (SymbolDesc) myConceptUser.getAllAttributeDescs().get("gender");
        SymbolDesc workType = (SymbolDesc) myConceptUser.getAllAttributeDescs().get("workType");
        SymbolDesc bodyType = (SymbolDesc) myConceptUser.getAllAttributeDescs().get("bodyType");
        SymbolDesc restriction = (SymbolDesc) myConceptUser.getAllAttributeDescs().get("restriction");
        StringDesc plan = (StringDesc) myConceptUser.getAllAttributeDescs().get("plan");

        // Attribute werden bestimmt.
        caze.addAttribute(age, age.getAttribute(rcUser.getAge()));
        caze.addAttribute(duration, duration.getAttribute(rcUser.getDuration()));
        caze.addAttribute(gender, gender.getAttribute(rcUser.getGender()));
        caze.addAttribute(workType, workType.getAttribute(rcUser.getWorkType()));
        caze.addAttribute(bodyType, bodyType.getAttribute(rcUser.getBodyType()));
        caze.addAttribute(restriction, restriction.getAttribute(rcUser.getRes()));
        caze.addAttribute(plan, plan.getAttribute(rcUser.getPlan().getpName()));

        // Hinzufügen des Falles zur Fallbasis.
        this.cbUser.addCase(caze);
        project.save();
    }

    // Methode um einen Fall der Exercise der CB hinzuzufügen.
    public void addCaseEx(Exercise exercise)
            throws ParseException {

        // Name des neuen Falles.
        int caseId = cbExercise.getCases().size() + 1;
        String id = "Exercise #" + caseId;

        // Erstellen des Falles.
        Instance caze = new Instance(myConceptExercise, id);

        // Hinzufügen der Attribute.
        IntegerDesc breakTime = (IntegerDesc) myConceptExercise.getAllAttributeDescs().get("breakTime");
        SymbolDesc exType = (SymbolDesc) myConceptExercise.getAllAttributeDescs().get("exType");
        SymbolDesc muscle = (SymbolDesc) myConceptExercise.getAllAttributeDescs().get("muscle");
        StringDesc name = (StringDesc) myConceptExercise.getAllAttributeDescs().get("name");
        IntegerDesc rating = (IntegerDesc) myConceptExercise.getAllAttributeDescs().get("rating");
        IntegerDesc rep = (IntegerDesc) myConceptExercise.getAllAttributeDescs().get("rep");
        IntegerDesc setNumber = (IntegerDesc) myConceptExercise.getAllAttributeDescs().get("setNumber");
        IntegerDesc time = (IntegerDesc) myConceptExercise.getAllAttributeDescs().get("time");
        IntegerDesc weight = (IntegerDesc) myConceptExercise.getAllAttributeDescs().get("weight");

        // Attribute werden bestimmt.
        caze.addAttribute(breakTime, breakTime.getAttribute(exercise.getExBreak()));
        caze.addAttribute(exType, exType.getAttribute(exercise.getExTypeEnum().toString()));
        caze.addAttribute(muscle, muscle.getAttribute(exercise.getExMuscleEnum().toString()));
        caze.addAttribute(name, name.getAttribute(exercise.getExName()));
        caze.addAttribute(rating, rating.getAttribute(exercise.getExRating()));
        caze.addAttribute(rep, rep.getAttribute(exercise.getExRep()));
        caze.addAttribute(setNumber, setNumber.getAttribute(exercise.getExSetNumber()));
        caze.addAttribute(time, time.getAttribute(exercise.getExTime()));
        caze.addAttribute(weight, weight.getAttribute(exercise.getExWeight()));

        // Hinzufügen des Falles zur Fallbasis.
        this.cbExercise.addCase(caze);
        project.save();
    }

    // Diese Methode dient zur Gewichtung der Attribute.
    private void setWeightForAttr(Instance instance, AttributeDesc attr, int weight) {

        // Wert wird definiert.
        if(weight == 2) {
            weight = 20;
        }
        if(weight == 3) {
            weight = 30;
        }
        if(weight != 1) {

            // Attribute werden durch Amalgam-Funktion gewichtet.
            for (AttributeDesc desc : instance.getAttributes().keySet()) {
                if (desc.getName().contains(attr.getName())) {
                    myConceptUser.getActiveAmalgamFct().setWeight(desc, weight);
                    if (weight == 0) {
                        myConceptUser.getActiveAmalgamFct().setActive(desc, false);
                    }
                    break;
                }
            }
        }
    }

    // Diese Methode dient zur Filterung der erhaltenen Pläne (Lösungen).
    private boolean isPlanValuable(ResultCaseUser rc, String musclePrio) {
        if(rc.getSim() < 0.5) {
            return false;
        }
        if(!musclePrio.equalsIgnoreCase("No Selection")) {
            if(!musclePrio.equalsIgnoreCase(rc.getPlan().getMusclePrio())) {
                return false;
            }
        }
        return true;
    }

    // Diese Methode dient zur Filterung der erhaltenen Exercises (Lösungen).
    private boolean isExerciseValuable(ResultCaseExercise rc, String muscleType) {
        if(rc.getSim() < 0.8) {
            return false;
        }
        if(!muscleType.equalsIgnoreCase(rc.getExercise().getExMuscle())) {
            return false;
        }
        return true;
    }

    // Diese Methode gibt die Größe der Fallbasis wieder.
    public int getExCount() {
        int count = cbExercise.getCases().size();
        return count;
    }

}
