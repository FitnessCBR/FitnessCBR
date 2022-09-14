package com.example.cbr__fitness.customListenerMethods;

import com.example.cbr__fitness.data.User;
import com.example.cbr__fitness.databasehelper.CsvDataHelper;
import com.example.cbr__fitness.interfaces.ToCaseCsvInterface;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

public class ToCsvButtonListener {

    public static boolean writeClassToCaseCsv(List<ToCaseCsvInterface> cases , File targetFile){
        boolean success = false;

        if (targetFile.exists()) {
            targetFile.delete();
        }
        try (PrintWriter writer = new PrintWriter(targetFile)) {
            //input the first, possible only input to determine the first line of the csv file
            writer.print(CsvDataHelper.getFirstLine(cases.get(0)));
            for (ToCaseCsvInterface c : cases) {
                System.out.println("Case Output! ");
                writer.print(c.toCsvCase());
            }
            success = true;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return success;
    }
}
