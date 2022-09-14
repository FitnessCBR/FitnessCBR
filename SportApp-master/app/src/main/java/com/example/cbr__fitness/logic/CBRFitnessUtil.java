package com.example.cbr__fitness.logic;


import android.content.Context;
import com.example.cbr__fitness.data.Exercise;
import com.example.cbr__fitness.data.ExerciseList;
import com.example.cbr__fitness.data.Plan;
import com.example.cbr__fitness.data.PlanList;
import com.example.cbr__fitness.data.User;
import com.example.cbr__fitness.data.UserList;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Jobst-Julius Bartels
 */

// Diese Klasse stellt die allgemeine Funktionen für die Datenverarbeitung bereit.
public class CBRFitnessUtil {

    // Default-Konstruktor.
    public CBRFitnessUtil() {

    }

    // Diese Methode extrahiert aus einem String die userList.
    public UserList getUserList(String content) {
        UserList userList = new UserList();
        User user = new User();
        int counter = 0;
        ArrayList<String> rawData = convertStringToList(content);
        for (int i = 0; i < rawData.size(); i++) {
            counter++;
            if (counter == 1) {
                user.setUsername(rawData.get(i));
            }
            if (counter == 2) {
                user.setUserPassword(rawData.get(i));
            }
            if (counter == 3) {
                user.setAge(rawData.get(i));
            }
            if (counter == 4) {
                user.setGender(rawData.get(i));
            }
            if (counter == 5) {
                user.setWorktype(rawData.get(i));
            }
            if (counter == 6) {
                user.setBodyType(rawData.get(i));
            }

            if (counter == 7) {
                user.setDuration(rawData.get(i));
            }
            if (counter == 8) {
                user.setRes(rawData.get(i));
            }
            if (counter == 9) {
                user.setPathData(rawData.get(i));
                userList.add(user);
                user = new User();
                counter = 0;
            }
        }
        return userList;
    }

    // Diese Methode extrahiert aus einem String die exerciseList.
    public ExerciseList getExList(String content) {
        ExerciseList exList = new ExerciseList();
        Exercise ex = new Exercise();
        int counter = 0;
        ArrayList<String> rawData = convertStringToList(content);
        for (int i = 0; i < rawData.size(); i++) {
            counter++;
            if (counter == 1) {
                ex.setExName(rawData.get(i));
            }
            if (counter == 2) {
                ex.setExSetNumber(rawData.get(i));
            }
            if (counter == 3) {
                ex.setExRep(rawData.get(i));
            }
            if (counter == 4) {
                ex.setExBreak(rawData.get(i));
            }
            if (counter == 5) {
                ex.setExWeight(rawData.get(i));
            }
            if (counter == 6) {
                ex.setExMuscle(rawData.get(i));
            }
            if (counter == 7) {
                ex.setExTime(rawData.get(i));
            }
            if (counter == 8) {
                ex.setExType(rawData.get(i));
            }
            if (counter == 9) {
                ex.setExRating(rawData.get(i));
                exList.add(ex);
                ex = new Exercise();
                counter = 0;
            }
        }
        return exList;
    }

    // Diese Methode extrahiert aus einem String die planList.
    public PlanList getUserPList(String content) {
        PlanList pList = new PlanList();
        Plan plan = new Plan();
        ExerciseList exList = new ExerciseList();
        Exercise ex = new Exercise();
        boolean boolPlan = true;
        int counter = 0;
        ArrayList<String> rawData = convertPStringToList(content);
        for (int i = 0; i < rawData.size(); i++) {
            if(rawData.get(i).matches("pName")) {
                boolPlan = true;
            } else if (rawData.get(i).matches("exName")) {
                boolPlan = false;
            } else {
                counter++;
            }
            if(boolPlan) {
                if (counter == 1) {
                    plan.setpName(rawData.get(i));
                }
                if (counter == 2) {
                    plan.setpEx(rawData.get(i));
                }
                if (counter == 3) {
                    plan.setpTime(rawData.get(i));
                }
                if (counter == 4) {
                    plan.setMusclePrio(rawData.get(i));
                }
                if (counter == 5) {
                    plan.setpRating(rawData.get(i));
                    counter = 0;
                }
            } else {
                if (counter == 1) {
                    ex.setExName(rawData.get(i));
                }
                if (counter == 2) {
                    ex.setExSetNumber(rawData.get(i));
                }
                if (counter == 3) {
                    ex.setExRep(rawData.get(i));
                }
                if (counter == 4) {
                    ex.setExBreak(rawData.get(i));
                }
                if (counter == 5) {
                    ex.setExWeight(rawData.get(i));
                }
                if (counter == 6) {
                    ex.setExMuscle(rawData.get(i));
                }
                if (counter == 7) {
                    ex.setExTime(rawData.get(i));
                }
                if (counter == 8) {
                    ex.setExType(rawData.get(i));
                }
                if (counter == 9) {
                    ex.setExRating(rawData.get(i));
                    exList.add(ex);
                    ex = new Exercise();
                    counter = 0;
                    if(rawData.size() == i + 1 ) {
                        plan.setpExList(exList);
                        exList = new ExerciseList();
                        pList.add(plan);
                        plan = new Plan();
                    } else if(rawData.get(i + 1).matches("pName")) {
                        plan.setpExList(exList);
                        exList = new ExerciseList();
                        pList.add(plan);
                        plan = new Plan();
                    }
                }
            }

        }
        return pList;
    }

    // Diese Methode convertiert nach dem definierten Pattern die Strings.
    public ArrayList<String> convertStringToList(String content) {
        ArrayList<String> data = new ArrayList<String>();
        Matcher m = Pattern.compile("\\[(.*?)=(.*?)\\]").matcher(content);
        while(m.find()) {
            data.add(m.group(2));
        }
        return data;
    }

    // Diese Methode convertiert nach dem definierten Pattern die Strings eines Plans.
    public ArrayList<String> convertPStringToList(String content) {
        ArrayList<String> data = new ArrayList<String>();
        Matcher m = Pattern.compile("\\[(.*?)=(.*?)\\]").matcher(content);
        while(m.find()) {
            if(m.group(1).matches("pName")) {
                data.add(m.group(1));
            } else if(m.group(1).matches("exName")) {
                data.add(m.group(1));
            }
            data.add(m.group(2));
        }
        return data;
    }

    // Diese Methode speichert einen String auf das Device.
    public void save(String fileName, String content, Context ctx) {
        String FILE_NAME = fileName;
        int MODE_PRIVATE = 0;
        FileOutputStream fos = null;
        try {
            fos = ctx.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            fos.write(content.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    // Diese Methode speichert den Log der Revise-Phase.
    public void saveRevJavaLog(String content, Context ctx) {
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd").format(new Date());
        String FILE_NAME = "Revise_JavaLog_" + timeStamp + ".txt";
        FileOutputStream fos = null;
        try {
            fos = ctx.openFileOutput(FILE_NAME, Context.MODE_APPEND);
            fos.write(content.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Diese Methode speichert den Log der Retrieval-Phase.
    public void saveRetJavaLog(String content, Context ctx) {
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd").format(new Date());
        String FILE_NAME = "Retrieval_JavaLog_" + timeStamp + ".txt";
        FileOutputStream fos = null;
        try {
            fos = ctx.openFileOutput(FILE_NAME, Context.MODE_APPEND);
            fos.write(content.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Diese Methode läd eine bestimmte Datei und aktualisiert dessen Inhalt.
    public String load(String fileName, String content, Context cxt) {
        String newContent ="";
        String FILE_NAME = fileName;
        FileInputStream fis = null;
        try {
            fis = cxt.openFileInput(FILE_NAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;
            while ((text = br.readLine()) != null) {
                sb.append(text).append("\n");
            }
            sb.append(content);
            newContent = sb.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return newContent;
    }

    // Diese Methode läd den gesamten Inhalt der Datei.
    public String loadAll(String fileName, Context ctx) {
        String content= "";
        String FILE_NAME = fileName;
        FileInputStream fis = null;
        try {
            fis = ctx.openFileInput(FILE_NAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;
            while ((text = br.readLine()) != null) {
                sb.append(text).append("\n");
            }
            content = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return content;
    }

    // Diese Methode überprüft, ob eine bestimmte Datei existiert.
    public boolean fileExists(String filename, Context context) {
        File file = context.getFileStreamPath(filename);
        if(file == null || !file.exists()) {
            return false;
        }
        return true;
    }
}
