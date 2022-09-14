package com.example.cbr__fitness.data;

import com.example.cbr__fitness.enums.EquipmentEnum;
import com.example.cbr__fitness.enums.ExerciseTypeEnum;
import com.example.cbr__fitness.enums.MovementTypeEnum;
import com.example.cbr__fitness.enums.MuscleEnum;
import com.example.cbr__fitness.interfaces.ToCaseCsvInterface;

/**
 * @author Jobst-Julius Bartels
 */

// Datenklasse für die Übung (engl. exercise).
public class Exercise implements ToCaseCsvInterface {

    // Membervariablen.

    private String exName;
    private String exSetNumber;
    private String exRep;
    private String exBreak;
    private String exWeight;
    private String exMuscle;
    private String exTime;
    private String exType;
    private String exRating;

    //determines the plan this belongs to
    private int planID;
    private int exerciseID;
    private String name;
    private int setNumber;
    private int repNumber;
    private int breakTime;
    private int weight;
    private MuscleEnum muscle;
    private MuscleEnum secondaryMuscle;
    private int repTime;
    private ExerciseTypeEnum type;
    private String description;
    private String imagePath;
    private EquipmentEnum equipment;
    private MovementTypeEnum movementType;
    boolean isBodyweight;
    private boolean isExplosive;

    public String getName() {
        return name;
    }

    public int getSetNumber() {
        return setNumber;
    }

    public int getRepNumber() {
        return repNumber;
    }

    public int getBreakTime() {
        return breakTime;
    }

    public int getWeight() {
        return weight;
    }

    public MuscleEnum getMuscle() {
        return muscle;
    }

    public int getRepTime() {
        return repTime;
    }

    public ExerciseTypeEnum getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public boolean getIsExplosive() {
        return isExplosive;
    }

    public boolean isBodyweight() {
        return isBodyweight;
    }

    public int getPlanID() {
        return planID;
    }


    // Enumerations.
    public enum ExType {
        Machine, Bodyweight, Free
    }
    public enum ExMuscle {
        Chest, Shoulders, Back, Bicep, Tricep, Legs, Abs
    }
    private ExType exTypeEnum;
    private ExMuscle exMuscleEnum;

    // Default-Konstruktor.
    public Exercise(){
    }
    @Deprecated
    public Exercise(String exName, String exSetNumber, String exRep, String exBreak, String exWeight
            , String exMuscle, String exTime, String exType, String exRating){
        this.exName = exName;
        this.exSetNumber = exSetNumber;
        this.exRep = exRep;
        this.exBreak = exBreak;
        this.exWeight = exWeight;
        this.exMuscle = exMuscle;
        this.exTime = exTime;
        this.exType = exType;
        this.exRating = exRating;
    }

    // Konstruktor.
    public Exercise(int plan_id, int exerciseID , String name, int setNumber, int repNumber
            , int breakTime, int weight, MuscleEnum muscle, MuscleEnum secondaryMuscle, int repTime
            , ExerciseTypeEnum type, EquipmentEnum equipment, String description
            , MovementTypeEnum movementType, boolean isExplosive, boolean isBodyweight, String imagePath) {
        this.planID = plan_id;
        this.exerciseID = exerciseID;
        this.name = name;
        this.setNumber = setNumber;
        this.repNumber = repNumber;
        this.breakTime = breakTime;
        this.weight = weight;
        this.muscle = muscle;
        this.secondaryMuscle = secondaryMuscle;
        this.repTime = repTime;
        this.type = type;
        this.equipment = equipment;
        this.description = description;
        this.movementType = movementType;
        this.isExplosive = isExplosive;
        this.isBodyweight = isBodyweight;
        this.imagePath = imagePath;
    }

    public Exercise(int exerciseID , String name, MuscleEnum muscle, MuscleEnum secondaryMuscle, int repTime
            , ExerciseTypeEnum type, EquipmentEnum equipment, String description
            , MovementTypeEnum movementType, boolean isExplosive, boolean isBodyweight, String imagePath) {
        this(-1, exerciseID, name, 0, 0, 0, 0, muscle
                , secondaryMuscle, repTime, type, equipment, description, movementType
                , isExplosive, isBodyweight, imagePath);
    }

    public void setBreakTime(int breakTime) {
        this.breakTime = breakTime;
    }

    public void setRepNumber(int repNumber) {
        this.repNumber = repNumber;
    }

    public void setSetNumber(int setNumber) {
        this.setNumber = setNumber;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getExerciseID() {
        return exerciseID;
    }

    public String getImagePath() {
        return imagePath;
    }

    public MuscleEnum getSecondaryMuscle(){
        return secondaryMuscle;
    }

    public int getDuration() {
        int duration = setNumber * breakTime;
        duration += setNumber * repNumber * repTime;
        return duration;
    }

    public void setPlanID(int planID) {
        this.planID = planID;
    }

    public EquipmentEnum getEquipment() {
        return equipment;
    }

    public MovementTypeEnum getMovementType() {
        return movementType;
    }
    // "exerciseID;primary_muscle;secondary_muscle;is_multijoint;equipment;movement_type;is_explosive;is_bodyweight\n"
    public String toCsvCase() {
        return  exerciseID + ";" + muscle.getSymbol() + ";" + secondaryMuscle.getSymbol()
                + ";" + (type == ExerciseTypeEnum.multiJoint) + ";" + equipment.getSymbol() + ";" + movementType.getLabel()
                + ";" + isExplosive + ";" + isBodyweight
                + "\n";
    }

    // Setter-Methoden.
    public void setExName(String exName) {
        this.exName = exName;
    }
    public void setExSetNumber(String exSetNumber) {
        this.exSetNumber = exSetNumber;
    }
    public void setExRep(String exRep) {
        this.exRep = exRep;
    }
    public void setExBreak(String exBreak) {
        this.exBreak = exBreak;
    }
    public void setExWeight(String exWeight) {
        this.exWeight = exWeight;
    }
    public void setExMuscle(String exMuscle) {
        this.exMuscle = exMuscle;
    }
    public void setExType(String exType) {
        this.exType = exType;
    }
    public void setExTime(String exTime) {
        this.exTime = exTime;
    }
    public void setExRating(String exRating) {
        this.exRating = exRating;
    }


    // Getter-Methoden.
    public ExType getExTypeEnum() {
        if(this.exType.matches("Machine")) {
            this.exTypeEnum = ExType.Machine;
        }  else if(this.exType.matches("Bodyweight")) {
            this.exTypeEnum = ExType.Bodyweight;
        } else {
            this.exTypeEnum = ExType.Free;
        }
        return this.exTypeEnum;
    }
    public ExMuscle getExMuscleEnum() {
        if(this.exMuscle.matches("Chest")) {
            this.exMuscleEnum = Exercise.ExMuscle.Chest;
        }  else if(this.exMuscle.matches("Back")) {
            this.exMuscleEnum = Exercise.ExMuscle.Back;
        } else if (this.exMuscle.matches("Shoulders")){
            this.exMuscleEnum = ExMuscle.Shoulders;
        } else if(this.exMuscle.matches("Bicep")) {
            this.exMuscleEnum = ExMuscle.Bicep;
        } else if (this.exMuscle.matches("Tricep")){
            this.exMuscleEnum = ExMuscle.Tricep;
        } else if (this.exMuscle.matches("Abs")){
            this.exMuscleEnum = ExMuscle.Abs;
        } else if (this.exMuscle.matches("Legs")){
            this.exMuscleEnum = Exercise.ExMuscle.Legs;
        }
        return this.exMuscleEnum;
    }
    public String getExName() {
        return exName;
    }
    public String getExSetNumber() {
        return exSetNumber;
    }
    public String getExRep() {
        return exRep;
    }
    public String getExBreak() {
        return exBreak;
    }
    public String getExWeight() {
        return exWeight;
    }
    public String getExMuscle() {
        return exMuscle;
    }
    public int getExMuscleInt() {
        int spinnerNumber = 0;
        if(this.exMuscle.matches("Chest")) {
            spinnerNumber = 1;
        } else if(this.exMuscle.matches("Shoulders")) {
            spinnerNumber = 2;
        } else if(this.exMuscle.matches("Back")) {
            spinnerNumber = 3;
        } else if(this.exMuscle.matches("Legs")) {
            spinnerNumber = 4;
        } else if(this.exMuscle.matches("Bicep")) {
            spinnerNumber = 5;
        } else if(this.exMuscle.matches("Tricep")) {
            spinnerNumber = 6;
        } else if(this.exMuscle.matches("Abs")) {
            spinnerNumber = 7;
        }
        return spinnerNumber;
    }
    public String getExTime() {
        return exTime;
    }
    public String getExType() {
        return exType;
    }
    public String getExRating() {
        return exRating;
    }

    // Print-Methode einer Exercise.
    public String exToString() {
        String exString = "";
        StringBuilder sb = new StringBuilder();
            sb.append("Exercise [exName="+ this.getExName() + "]");
            sb.append("[exSetNumber=" + this.getExSetNumber() +"]");
            sb.append("[exRep=" + this.getExRep() +"]");
            sb.append("[exBreak=" + this.getExBreak() +"]");
            sb.append("[exWeight=" + this.getExWeight() +"]");
            sb.append("[exMuscle=" + this.getExMuscle() +"]");
            sb.append("[exTime=" + this.getExTime() +"]");
            sb.append("[exType=" + this.getExType() +"]");
            sb.append("[exRating=" + this.getExRating() +"]");
            sb.append("\n");
        exString = sb.toString();
        return exString;
    }
}
