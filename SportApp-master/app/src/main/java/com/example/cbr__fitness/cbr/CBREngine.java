package com.example.cbr__fitness.cbr;

import de.dfki.mycbr.core.Project;
import de.dfki.mycbr.core.model.Concept;
import de.dfki.mycbr.io.CSVImporter;

/**
 * @author the cbr team
 * @author Jobst-Julius Bartels (edit)
 */

public class CBREngine {

    private static String data_path = System.getProperty("user.dir") + "\\cb\\";

    /* project specific: CBR Fitness. */
    // name of the project file
    // private static String projectName = "CBR_Fitness.prj";
    private static String projectName = "CBR_Fitness.prj";
    // name of the central concept
    private static String conceptNameUser = "person";
    // name of the central concept
    private static String conceptNameExercise = "exercise";
    // name of the csv containing the instances
    private static String csv = "user_caseBase.csv";
    // set the separators that are used in the csv file
    private static String columnseparator = ";";
    private static String multiplevalueseparator = ",";
    // name of the case base that should be used; the default name in myCBR is
    // CB_csvImport
    private static String casebaseUser = "person_case_base";
    private static String casebaseExercise = "exercise_case_base";

    // Getter for the Project meta data
    public static String getCaseBaseUser() {
        return casebaseUser;
    }

    public static void setCasebaseUser(String casebase) {
        CBREngine.casebaseUser = casebase;
    }

    // Getter for the Project meta data
    public static String getCasebaseExercise() {
        return casebaseExercise;
    }

    public static void setCasebaseExercise(String casebase) {
        CBREngine.casebaseExercise = casebase;
    }

    public static String getProjectName() {
        return projectName;
    }

    public static void setProjectName(String projectName) {
        CBREngine.projectName = projectName;
    }

    public static String getConceptNameUser() {
        return conceptNameUser;
    }

    public static void setConceptNameUser(String conceptName) {
        CBREngine.conceptNameUser = conceptName;
    }
    public static String getConceptNameExercise() {
        return conceptNameExercise;
    }

    public static void setConceptNameExercise(String conceptNameExercise) {
        CBREngine.conceptNameExercise = conceptNameExercise;
    }

    public static String getCsv() {
        return csv;
    }

    public static void setCsv(String csv) {
        CBREngine.csv = csv;
    }

    /**
     * This methods creates a myCBR project and loads the project from a .prj file
     */
    public Project createProjectFromPRJ(String path) {

        System.out.println("Trying to load prj file with : " + path + "" + projectName + " " + conceptNameUser + " ");

        Project project = null;

        try {

            project = new Project(path + projectName);

            // pause time
            while (project.isImporting()) {
                Thread.sleep(1000);
                System.out.print(".");
            }
            System.out.print("\n"); // console pretty print
        } catch (Exception ex) {

            System.out.println("Fehler beim Laden des Projektes");
        }
        return project;
    }

    /**
     * This methods creates a myCBR project and loads the cases in this project. The
     * specification of the project's location and according file names has to be
     * done at the beginning of this class.
     *
     * @return Project instance containing model, sims and cases (if available)
     */
    public Project createCBRProject() {

        Project project = null;
        try {
            // load new project
            project = new Project(data_path + projectName);
            System.out.println(">>> DATA_PATH: " + data_path);
            // create a concept and get the main concept of the project;
            // the name has to be specified at the beginning of this class
            while (project.isImporting()) {
                Thread.sleep(1000);
                System.out.print(".");
            }
            System.out.print("\n"); // console pretty print
            Concept concept = project.getConceptByID(conceptNameUser);
            // Initialize CSV Import
            CSVImporter csvImporter = new CSVImporter(data_path + csv, concept);
            // set the separators that are used in the csv file
            csvImporter.setSeparator(columnseparator); // column separator
            csvImporter.setSeparatorMultiple(multiplevalueseparator); // multiple value separator
            // prepare for import
            csvImporter.readData();
            csvImporter.checkData();
            csvImporter.addMissingValues();
            csvImporter.addMissingDescriptions();
            // do the import of the instances
            csvImporter.doImport();
            // wait until the import is done
            System.out.print("Importing ");
            while (csvImporter.isImporting()) {
                Thread.sleep(1000);
                System.out.print(".");
            }
            System.out.print("\n"); // console pretty print
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return project;
    }

    /**
     * This methods creates an EMPTY myCBR project. The specification of the
     * project's location and according file names has to be done at the beginning
     * of this class.
     *
     * @return Project instance containing model, sims and cases (if available)
     */
    public Project createemptyCBRProject() {

        Project project = null;
        try {
            // load new project
            project = new Project(data_path + projectName);
            // create a concept and get the main concept of the project;
            // the name has to be specified at the beginning of this class
            while (project.isImporting()) {
                Thread.sleep(1000);
                System.out.print(".");
            }
            System.out.print("\n"); // console pretty print
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return project;
    }
}
