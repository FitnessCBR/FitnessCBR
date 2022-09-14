package com.example.cbr__fitness.cbr;

import com.example.cbr__fitness.data.ExerciseList;
import com.example.cbr__fitness.data.User;

/**
 * @author Jobst-Julius Bartels
 */

// Diese Klasse stellt den Prozess für die Revise-Phase dar.
public class ReviseUtil {

    // Konstruktor der Klasse.
    public ReviseUtil() {

    }

    // Diese Methode dient zur Bereinigung der Exercise Liste.
    public ExerciseList cleanEx(ExerciseList exerciseList, String musclePrio) {

        ExerciseList newExList = new ExerciseList();

        // Definiert, welche Exercises bei der FullBody Priorität zugelassen sind.
        if(musclePrio.equalsIgnoreCase("FullBody")) {
            for (int i = 0; i < exerciseList.size(); i++) {
                newExList.add(exerciseList.get(i));
            }
        }

        // Definiert, welche Exercises bei der UpperBody Priorität zugelassen sind.
        if(musclePrio.equalsIgnoreCase("UpperBody")) {
            for (int i = 0; i < exerciseList.size(); i++) {
                if(exerciseList.get(i).getExMuscle().equalsIgnoreCase("Chest") ||
                        exerciseList.get(i).getExMuscle().equalsIgnoreCase("Back") ||
                        exerciseList.get(i).getExMuscle().equalsIgnoreCase("Abs")
                        || exerciseList.get(i).getExMuscle().equalsIgnoreCase("Tricep") ||
                        exerciseList.get(i).getExMuscle().equalsIgnoreCase("Bicep") ||
                        exerciseList.get(i).getExMuscle().equalsIgnoreCase("Shoulders")) {
                    newExList.add(exerciseList.get(i));
                }
            }
        }

        // Definiert, welche Exercises bei der LowerBody Priorität zugelassen sind.
        if(musclePrio.equalsIgnoreCase("LowerBody")) {
            for (int i = 0; i < exerciseList.size(); i++) {
                if(exerciseList.get(i).getExMuscle().equalsIgnoreCase("Legs")) {
                    newExList.add(exerciseList.get(i));
                }
            }
        }

        // Definiert, welche Exercises bei der Chest Priorität zugelassen sind.
        if(musclePrio.equalsIgnoreCase("Chest")) {
            for (int i = 0; i < exerciseList.size(); i++) {
                if(exerciseList.get(i).getExMuscle().equalsIgnoreCase("Chest") ||
                        exerciseList.get(i).getExMuscle().equalsIgnoreCase("Tricep") ||
                        exerciseList.get(i).getExMuscle().equalsIgnoreCase("Shoulders")) {
                    newExList.add(exerciseList.get(i));
                }
            }
        }

        // Definiert, welche Exercises bei der Legs Priorität zugelassen sind.
        if(musclePrio.equalsIgnoreCase("Legs")) {
            for (int i = 0; i < exerciseList.size(); i++) {
                if(exerciseList.get(i).getExMuscle().equalsIgnoreCase("Legs") ||
                        exerciseList.get(i).getExMuscle().equalsIgnoreCase("Abs")) {
                    newExList.add(exerciseList.get(i));
                }
            }
        }

        // Definiert, welche Exercises bei der Back Priorität zugelassen sind.
        if(musclePrio.equalsIgnoreCase("Back")) {
            for (int i = 0; i < exerciseList.size(); i++) {
                if(exerciseList.get(i).getExMuscle().equalsIgnoreCase("Back") ||
                        exerciseList.get(i).getExMuscle().equalsIgnoreCase("Bicep")) {
                    newExList.add(exerciseList.get(i));
                }
            }
        }

        // Definiert, welche Exercises bei der Abs Priorität zugelassen sind.
        if(musclePrio.equalsIgnoreCase("Abs")) {
            for (int i = 0; i < exerciseList.size(); i++) {
                if(exerciseList.get(i).getExMuscle().equalsIgnoreCase("Abs")) {
                    newExList.add(exerciseList.get(i));
                }
            }
        }

        // Definiert, welche Exercises bei der Arms Priorität zugelassen sind.
        if(musclePrio.equalsIgnoreCase("Arms")) {
            for (int i = 0; i < exerciseList.size(); i++) {
                if(exerciseList.get(i).getExMuscle().equalsIgnoreCase("Tricep") ||
                        exerciseList.get(i).getExMuscle().equalsIgnoreCase("Bicep")) {
                    newExList.add(exerciseList.get(i));
                }
            }
        }

        // Definiert, welche Exercises bei der Shoulders Priorität zugelassen sind.
        if(musclePrio.equalsIgnoreCase("Shoulders")) {
            for (int i = 0; i < exerciseList.size(); i++) {
                if(exerciseList.get(i).getExMuscle().equalsIgnoreCase("Tricep") ||
                        exerciseList.get(i).getExMuscle().equalsIgnoreCase("Shoulders")) {
                    newExList.add(exerciseList.get(i));
                }
            }
        }
        return newExList;
    }

    // Diese Methode dient zur Optimierung der Attribute der Exercises.
    public ExerciseList optimizeExSetts(ExerciseList exerciseList, User user) {


        // Für Beginner und Restriktions Profile.
        if(user.getRes().equalsIgnoreCase("Yes") ||
                user.getWorktype().equalsIgnoreCase("Beginner")) {
            for(int i = 0; i < exerciseList.size(); i++) {
                exerciseList.get(i).setExSetNumber("3");
                exerciseList.get(i).setExRep("15");
                exerciseList.get(i).setExBreak("120");
                exerciseList.get(i).setExTime("10");
                // ToDo Gewicht senken.
            }

        // Für Pro, Advanced und Mesomorph Profile.
        } else if((user.getWorktype().equalsIgnoreCase("Advanced") ||
                user.getWorktype().equalsIgnoreCase("Pro")) &&
                user.getBodyType().equalsIgnoreCase("Mesomorph")) {
            for(int i = 0; i < exerciseList.size(); i++) {
                exerciseList.get(i).setExSetNumber("4");
                exerciseList.get(i).setExRep("10");
                exerciseList.get(i).setExBreak("60");
                exerciseList.get(i).setExTime("6");
                // ToDo Gewicht erhöhen.
            }

        // Für Pro, Advanced und Advanced Endomorph.
        } else if((user.getWorktype().equalsIgnoreCase("Advanced") ||
                user.getWorktype().equalsIgnoreCase("Pro")) &&
                user.getBodyType().equalsIgnoreCase("Endomorph")) {
            for(int i = 0; i < exerciseList.size(); i++) {
                exerciseList.get(i).setExSetNumber("5");
                exerciseList.get(i).setExRep("20");
                exerciseList.get(i).setExBreak("30");
                exerciseList.get(i).setExTime("8");
                // ToDo Gewicht senken.
            }

        // Für Pro, Advanced und Ectomorph Profile.
        } else if((user.getWorktype().equalsIgnoreCase("Advanced") ||
                user.getWorktype().equalsIgnoreCase("Pro")) &&
                user.getBodyType().equalsIgnoreCase("Ectomorph")) {
            for(int i = 0; i < exerciseList.size(); i++) {
                exerciseList.get(i).setExSetNumber("7");
                exerciseList.get(i).setExRep("5");
                exerciseList.get(i).setExBreak("90");
                exerciseList.get(i).setExTime("10");
                // ToDo Gewicht erhöhen.
            }
        }
        return exerciseList;
    }
}
