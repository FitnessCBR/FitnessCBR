package com.example.cbr__fitness.logic;

import android.content.Context;

import com.example.cbr__fitness.data.ExerciseList;
import com.example.cbr__fitness.data.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.List;

import de.dfki.mycbr.util.Pair;

public class LogUtil {

    public static void logUserSimilarity(Context context, User user, List<Pair<User, Double>> results) {
        File file = new File(context.getFilesDir() + "/loggingUserSimilarity.txt");
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(new FileOutputStream(file, true));
            writer.append("> ").append(user.toString());
            writer.println("");
            for (Pair<User, Double> p : results) {
                writer.append("\t").append(p.getFirst().toString())
                        .append(" SIM: ").append(Double.toString(p.getSecond()));
                writer.println("");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    public static void LogPlanSimilarity(Context context, String text) {
        File file = new File(context.getFilesDir() + "/loggingUserSimilarity.txt");
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(new FileOutputStream(file, true));
            writer.println("");
            writer.append(text);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

}
