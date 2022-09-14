package com.example.cbr__fitness.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.cbr__fitness.cbr.RetrievalUtil;
import com.example.cbr__fitness.cbr.ReviseUtil;
import com.example.cbr__fitness.data.ExerciseList;
import com.example.cbr__fitness.data.Plan;
import com.example.cbr__fitness.data.PlanList;
import com.example.cbr__fitness.data.ResultCaseExercise;
import com.example.cbr__fitness.logic.CBRFitnessUtil;
import com.example.cbr__fitness.R;
import java.text.ParseException;
import java.util.ArrayList;

/**
 * @author Jobst-Julius Bartels
 */

// Diese Klasse stellt die Aktivität der Configuration der Pläne bereit.
public class ConfigurePlan extends AppCompatActivity implements View.OnClickListener {

    // Attribute der Klasse.
    private TextView textNewP;
    private TextView txtPlanWinName;
    private TextView txtPlanWinTime;
    private TextView txtPlanWinEx;
    private TextView txtPlanWinMusclePrio;
    private TextView txtPlanWinRating;
    private TextView txtButtonEx1;
    private TextView txtButtonEx2;
    private TextView txtButtonEx3;
    private TextView txtButtonEx4;
    private Button pButton1;
    private Button pButton2;
    private Button pButton3;
    private Button pButton4;
    private ImageButton editButton1;
    private ImageButton editButton2;
    private ImageButton editButton3;
    private ImageButton editButton4;
    private ImageButton delButton1;
    private ImageButton delButton2;
    private ImageButton delButton3;
    private ImageButton delButton4;
    private ImageButton newPlanButton;
    private ImageButton newExButton;
    private ImageButton imgButtonLogo;
    private ImageButton optiButton;
    private ImageView logoButton1;
    private ImageView logoButton2;
    private ImageView logoButton3;
    private ImageView logoButton4;
    private CBRFitnessUtil cbrFitnessUtil;
    private Intent i;
    private PlanList userPlanList;
    private String pName;
    public static int planNumber;
    private ArrayList<ResultCaseExercise> resultList;
    private RetrievalUtil retrievalUtil;
    private ReviseUtil reviseUtil;

    @SuppressLint("SetTextI18n")
    @Override
    // OnCLick Methode der Klasse.
    public void onClick(View v) {
        switch (v.getId()) {

            // Durch das Klicken des pButton1 wird der erste Plan ausgewählt.
            case R.id.pButton1:
                 txtPlanWinName.setText(userPlanList.get(0).getpName());
                 txtPlanWinTime.setText(userPlanList.get(0).getpTime() + " Minutes");
                 txtPlanWinEx.setText(userPlanList.get(0).getpEx() + " Exercises");
                 txtPlanWinMusclePrio.setText(userPlanList.get(0).getMusclePrio());
                 txtPlanWinRating.setText(userPlanList.get(0).getpRating() + " Stars");
                 optiButton.setEnabled(true);
                 planNumber = 0;
                break;

            // Durch das Klicken des pButton2 wird der zweite Plan ausgewählt.
            case R.id.pButton2:
                txtPlanWinName.setText(userPlanList.get(1).getpName());
                txtPlanWinTime.setText(userPlanList.get(1).getpTime() + " Minutes");
                txtPlanWinEx.setText(userPlanList.get(1).getpEx() + " Exercises");
                txtPlanWinMusclePrio.setText(userPlanList.get(1).getMusclePrio());
                txtPlanWinRating.setText(userPlanList.get(1).getpRating() + " Stars");
                optiButton.setEnabled(true);
                planNumber = 1;
                break;

            // Durch das Klicken des pButton3 wird der dritte Plan ausgewählt.
            case R.id.pButton3:
                txtPlanWinName.setText(userPlanList.get(2).getpName());
                txtPlanWinTime.setText(userPlanList.get(2).getpTime() + " Minutes");
                txtPlanWinEx.setText(userPlanList.get(2).getpEx() + " Exercises");
                txtPlanWinMusclePrio.setText(userPlanList.get(2).getMusclePrio());
                txtPlanWinRating.setText(userPlanList.get(2).getpRating() + " Stars");
                optiButton.setEnabled(true);
                planNumber = 2;
                break;

            // Durch das Klicken des pButton4 wird der vierte Plan ausgewählt.
            case R.id.pButton4:
                txtPlanWinName.setText(userPlanList.get(3).getpName());
                txtPlanWinTime.setText(userPlanList.get(3).getpTime() + " Minutes");
                txtPlanWinEx.setText(userPlanList.get(3).getpEx() + " Exercises");
                txtPlanWinMusclePrio.setText(userPlanList.get(3).getMusclePrio());
                txtPlanWinRating.setText(userPlanList.get(3).getpRating() + " Stars");
                optiButton.setEnabled(true);
                planNumber = 3;
                break;

            // Durch das Klicken des editButton1 wird die Aktivität zur Editierung eines Plans gestartet.
            case R.id.editButton1:
                planNumber = 0;
                i = new Intent(ConfigurePlan.this, EditPlan.class);
                startActivity(i);
                break;

            // Durch das Klicken des editButton2 wird die Aktivität zur Editierung eines Plans gestartet.
            case R.id.editButton2:
                planNumber = 1;
                i = new Intent(ConfigurePlan.this, EditPlan.class);
                startActivity(i);
                break;

            // Durch das Klicken des editButton3 wird die Aktivität zur Editierung eines Plans gestartet.
            case R.id.editButton3:
                planNumber = 2;
                i = new Intent(ConfigurePlan.this, EditPlan.class);
                startActivity(i);
                break;

            // Durch das Klicken des editButton4 wird die Aktivität zur Editierung eines Plans gestartet.
            case R.id.editButton4:
                planNumber = 3;
                i = new Intent(ConfigurePlan.this, EditPlan.class);
                startActivity(i);
                break;

            // Durch das Klicken des delButton1 wird der entsprechende Plan aus dem Benutzerprofil gelöscht.
            case R.id.delButton1:
                pName = userPlanList.get(0).getpName();
                userPlanList.removePlan(pName);
                MainActivity.userLogged.getPlanList().removePlan(pName);
                cbrFitnessUtil.save(MainActivity.userLogged.getPathData(), userPlanList.pListToString(), ConfigurePlan.this);
                delButton1.setVisibility(View.INVISIBLE);
                editButton1.setVisibility(View.INVISIBLE);
                pButton1.setVisibility(View.INVISIBLE);
                logoButton1.setVisibility(View.INVISIBLE);
                txtButtonEx1.setVisibility(View.INVISIBLE);
                updateCreatePlanButton();
                finish();
                startActivity(new Intent(this, ConfigurePlan.class));
                overridePendingTransition(0, 0);
                break;

            // Durch das Klicken des delButton2 wird der entsprechende Plan aus dem Benutzerprofil gelöscht.
            case R.id.delButton2:
                pName = userPlanList.get(1).getpName();
                userPlanList.removePlan(pName);
                MainActivity.userLogged.getPlanList().removePlan(pName);
                cbrFitnessUtil.save(MainActivity.userLogged.getPathData(), userPlanList.pListToString(), ConfigurePlan.this);
                delButton2.setVisibility(View.INVISIBLE);
                editButton2.setVisibility(View.INVISIBLE);
                pButton2.setVisibility(View.INVISIBLE);
                logoButton2.setVisibility(View.INVISIBLE);
                txtButtonEx2.setVisibility(View.INVISIBLE);
                updateCreatePlanButton();
                finish();
                startActivity(new Intent(this, ConfigurePlan.class));
                overridePendingTransition(0, 0);
                break;

            // Durch das Klicken des delButton3 wird der entsprechende Plan aus dem Benutzerprofil gelöscht.
            case R.id.delButton3:
                pName = userPlanList.get(2).getpName();
                userPlanList.removePlan(pName);
                MainActivity.userLogged.getPlanList().removePlan(pName);
                cbrFitnessUtil.save(MainActivity.userLogged.getPathData(), userPlanList.pListToString(), ConfigurePlan.this);
                delButton3.setVisibility(View.INVISIBLE);
                editButton3.setVisibility(View.INVISIBLE);
                pButton3.setVisibility(View.INVISIBLE);
                logoButton3.setVisibility(View.INVISIBLE);
                txtButtonEx3.setVisibility(View.INVISIBLE);
                updateCreatePlanButton();
                finish();
                startActivity(new Intent(this, ConfigurePlan.class));
                overridePendingTransition(0, 0);
                break;

            // Durch das Klicken des delButton4 wird der entsprechende Plan aus dem Benutzerprofil gelöscht.
            case R.id.delButton4:
                pName = userPlanList.get(3).getpName();
                userPlanList.removePlan(pName);
                MainActivity.userLogged.getPlanList().removePlan(pName);
                cbrFitnessUtil.save(MainActivity.userLogged.getPathData(), userPlanList.pListToString(), ConfigurePlan.this);
                delButton4.setVisibility(View.INVISIBLE);
                editButton4.setVisibility(View.INVISIBLE);
                pButton4.setVisibility(View.INVISIBLE);
                logoButton4.setVisibility(View.INVISIBLE);
                txtButtonEx4.setVisibility(View.INVISIBLE);
                updateCreatePlanButton();
                finish();
                startActivity(new Intent(this, ConfigurePlan.class));
                overridePendingTransition(0, 0);
                break;

            // Durch das Klicken des delButton1 wird die Aktivität zur Erstellung eines neuen Plans gestartet.
            case R.id.newPlanButton:
                i = new Intent(ConfigurePlan.this, CreatePlan.class);
                startActivity(i);
                break;

            // Durch das Klicken des delButton1 wird die Aktivität zur Erstellung einer neuen Übung gestartet.
            case R.id.newExButton:
                i = new Intent(ConfigurePlan.this, CreateExercise.class);
                startActivity(i);
                break;

            // Durch Klicken des imgButtonLogos gelangt der Benutzer zurück zur vorherigen Aktivität.
            case R.id.imgButtonLogo3:
                i = new Intent(ConfigurePlan.this, UserInterface.class);
                startActivity(i);
                break;

            // Durch Klicken des imgButtonLogos wird der Optimierungsprozess des entsprechenden Plans gestartet.
            case R.id.optiButton:
                try {
                    optimizePlan(userPlanList.get(planNumber));
                    Toast.makeText(this, "Optimization done", Toast.LENGTH_SHORT).show();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                finish();
                startActivity(new Intent(this, ConfigurePlan.class));
                overridePendingTransition(0, 0);
                break;
        }
    }

    @Override
    // OnCreate Methode der Klasse.
    protected void onCreate(Bundle savedInstanceState) {
        //Layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.configure_plan);

        // Initialisierung der Util-Klassen und anderen Attributen.
        cbrFitnessUtil = new CBRFitnessUtil();
        userPlanList = new PlanList();
        userPlanList = cbrFitnessUtil.getUserPList(cbrFitnessUtil.loadAll(MainActivity.userLogged.getPathData(), ConfigurePlan.this));
        resultList = new ArrayList<ResultCaseExercise>();
        String path = this.getFilesDir().getAbsolutePath() + "/";
        retrievalUtil = new RetrievalUtil(path);
        reviseUtil = new ReviseUtil();
        pName = "";

        // Layout-Attribute.
        txtPlanWinEx = findViewById(R.id.txtPlanWinEx);
        txtPlanWinName = findViewById(R.id.txtPlanWinName);
        txtPlanWinRating = findViewById(R.id.txtPlanWinRating);
        txtPlanWinMusclePrio = findViewById(R.id.txtPlanWinMusclePrio);
        txtPlanWinTime = findViewById(R.id.txtPlanWinTime);
        txtButtonEx1 = findViewById(R.id.txtButton1);
        txtButtonEx2 = findViewById(R.id.txtButton2);
        txtButtonEx3 = findViewById(R.id.txtButton3);
        txtButtonEx4 = findViewById(R.id.txtButton4);
        textNewP = findViewById(R.id.textNewP);
        pButton1 = findViewById(R.id.pButton1);
        pButton2 = findViewById(R.id.pButton2);
        pButton3 = findViewById(R.id.pButton3);
        pButton4 = findViewById(R.id.pButton4);
        editButton1 = findViewById(R.id.editButton1);
        editButton2 = findViewById(R.id.editButton2);
        editButton3 = findViewById(R.id.editButton3);
        editButton4 = findViewById(R.id.editButton4);
        delButton1 = findViewById(R.id.delButton1);
        delButton2 = findViewById(R.id.delButton2);
        delButton3 =  findViewById(R.id.delButton3);
        delButton4 = findViewById(R.id.delButton4);
        newPlanButton = findViewById(R.id.newPlanButton);
        newExButton = findViewById(R.id.newExButton);
        imgButtonLogo = findViewById(R.id.imgButtonLogo3);
        optiButton = findViewById(R.id.optiButton);
        logoButton1 = findViewById(R.id.logoButton1);
        logoButton2 = findViewById(R.id.logoButton2);
        logoButton3 = findViewById(R.id.logoButton3);
        logoButton4 = findViewById(R.id.logoButton4);

        // OnClickListener werden den Buttons hinzugefügt.
        imgButtonLogo.setOnClickListener(this);
        pButton1.setOnClickListener(this);
        pButton2.setOnClickListener(this);
        pButton3.setOnClickListener(this);
        pButton4.setOnClickListener(this);
        editButton1.setOnClickListener(this);
        editButton2.setOnClickListener(this);
        editButton3.setOnClickListener(this);
        editButton4.setOnClickListener(this);
        delButton1.setOnClickListener(this);
        delButton2.setOnClickListener(this);
        delButton3.setOnClickListener(this);
        delButton4.setOnClickListener(this);
        newPlanButton.setOnClickListener(this);
        newExButton.setOnClickListener(this);
        optiButton.setOnClickListener(this);
        optiButton.setEnabled(false);

        // Das Label für die Buttons der Pläne wird gesetzt.
        setButtonStrings();

        // Der Button zur Erstellung eines Plans wird aktualisiert.
        updateCreatePlanButton();
    }

    @SuppressLint("SetTextI18n")
    // Diese Methode erstellt die Labels für die Buttons der Pläne des Benutzers.
    public void setButtonStrings() {

        for(int i = 0; i < userPlanList.size(); i++) {
            if(i == 0) {
                pButton1.setText(userPlanList.get(i).getpName());
                pButton1.setVisibility(View.VISIBLE);
                txtButtonEx1.setText(userPlanList.get(i).getpEx() + " Exercises");
                txtButtonEx1.setVisibility(View.VISIBLE);
                delButton1.setVisibility(View.VISIBLE);
                if(userPlanList.get(i).getpName().contains("_revised")) {
                    editButton1.setVisibility(View.INVISIBLE);
                } else {
                    editButton1.setVisibility(View.VISIBLE);
                }
                logoButton1.setVisibility(View.VISIBLE);
            } else if(i == 1) {
                pButton2.setText(userPlanList.get(i).getpName());
                pButton2.setVisibility(View.VISIBLE);
                txtButtonEx2.setText(userPlanList.get(i).getpEx() + " Exercises");
                txtButtonEx2.setVisibility(View.VISIBLE);
                delButton2.setVisibility(View.VISIBLE);
                if(userPlanList.get(i).getpName().contains("_revised")) {
                    editButton2.setVisibility(View.INVISIBLE);
                } else {
                    editButton2.setVisibility(View.VISIBLE);
                }
                logoButton2.setVisibility(View.VISIBLE);
            } else if(i == 2) {
                pButton3.setText(userPlanList.get(i).getpName());
                pButton3.setVisibility(View.VISIBLE);
                txtButtonEx3.setText(userPlanList.get(i).getpEx() + " Exercises");
                txtButtonEx3.setVisibility(View.VISIBLE);
                delButton3.setVisibility(View.VISIBLE);
                if(userPlanList.get(i).getpName().contains("_revised")) {
                    editButton3.setVisibility(View.INVISIBLE);
                } else {
                    editButton3.setVisibility(View.VISIBLE);
                }
                logoButton3.setVisibility(View.VISIBLE);
            } else if(i == 3) {
                pButton4.setText(userPlanList.get(i).getpName());
                pButton4.setVisibility(View.VISIBLE);
                txtButtonEx4.setText(userPlanList.get(i).getpEx() + " Exercises");
                txtButtonEx4.setVisibility(View.VISIBLE);
                delButton4.setVisibility(View.VISIBLE);
                if(userPlanList.get(i).getpName().contains("_revised")) {
                    editButton4.setVisibility(View.INVISIBLE);
                } else {
                    editButton4.setVisibility(View.VISIBLE);
                }
                logoButton4.setVisibility(View.VISIBLE);
            }
        }
    }

    @SuppressLint("SetTextI18n")
    // Diese Methode aktualisiert den Button fürs Erstellen eines neuen Plans
    public void updateCreatePlanButton() {

        if(userPlanList.size() >= 4) {
            newPlanButton.setEnabled(false);
            textNewP.setText("Max. Plansize reached");
            newPlanButton.setVisibility(View.INVISIBLE);
        } else {
            newPlanButton.setEnabled(true);
            textNewP.setText("New Plan");
            newPlanButton.setVisibility(View.VISIBLE);
        }
    }

    // Diese Methode stellt den Prozess zur Optimierung(Revise) eines Plans bereit und schreibt simultan das JavaLog.
    public void optimizePlan(Plan plan) throws ParseException {

        ExerciseList exList;
        cbrFitnessUtil.saveRevJavaLog("Performing Revise on Plan:", ConfigurePlan.this);
        cbrFitnessUtil.saveRevJavaLog("\n", ConfigurePlan.this);
        cbrFitnessUtil.saveRevJavaLog(plan.pToString(), ConfigurePlan.this);

        // Der Plan wird von falschen Exercises bereinigt.
        exList = reviseUtil.cleanEx(plan.getpExList(), plan.getMusclePrio());
        cbrFitnessUtil.saveRevJavaLog("Performing Clean Up", ConfigurePlan.this);
        cbrFitnessUtil.saveRevJavaLog("\n", ConfigurePlan.this);
        cbrFitnessUtil.saveRevJavaLog(exList.exListToString(), ConfigurePlan.this);

        // Die Exercises des aktuellen Plans werden mit denen aus der CBR-CB ausgetauscht.
        exList = exchangeEx(exList);

        // Der Plan wird an das vorhandene Wissens über personalisierte Trainingspläne angepasst.
        exList = reviseUtil.optimizeExSetts(exList, MainActivity.userLogged);
        cbrFitnessUtil.saveRevJavaLog("Exercises Optimization", ConfigurePlan.this);
        cbrFitnessUtil.saveRevJavaLog("\n", ConfigurePlan.this);
        cbrFitnessUtil.saveRevJavaLog(exList.exListToString(), ConfigurePlan.this);

        // Das Ergebnis des Tausches wird geloggt.
        cbrFitnessUtil.saveRevJavaLog("Plan Result ", ConfigurePlan.this);
        cbrFitnessUtil.saveRevJavaLog("\n", ConfigurePlan.this);
        String pName = plan.getpName();

        // Name wird hier mit revised gekennzeichnet.
        // ToDo Entfernung dieser Ergänzung nach Prototyp-Phase.
        if(!pName.contains("_revised")) {
            plan.setpName(pName + "_" + MainActivity.userLogged.getUsername() + "_revised");
        }
        plan.setpEx(String.valueOf(exList.size()));
        plan.setpTime(String.valueOf(exList.getTotalPlanTime()));
        plan.getpExList().clear();
        for(int i = 0; i < exList.size(); i++) {
            plan.getpExList().add(exList.get(i));
        }
        cbrFitnessUtil.saveRevJavaLog(plan.pToString(), ConfigurePlan.this);

        // Der neue Plan wird dem Benutzerprofil hinzugefügt.
        MainActivity.userLogged.getPlanList().remove(planNumber);
        MainActivity.userLogged.getPlanList().add(plan);
        cbrFitnessUtil.save(MainActivity.userLogged.getPathData(), userPlanList.pListToString(), ConfigurePlan.this);
    }

    // Diese Methode ist für den Tausch der Exercises verantwortlich.
    public ExerciseList exchangeEx(ExerciseList exerciseList) throws ParseException {

        ExerciseList exList = new ExerciseList();
        for(int i = 0; i < exerciseList.size(); i++) {
            resultList.clear();

            // Es wird ein Retrieval auf Basis der aktuellen Exercise ausgeführt.
            resultList = retrievalUtil.retrieveExercise(exerciseList.get(i));
            cbrFitnessUtil.saveRevJavaLog("Performing Retrieval on Query", ConfigurePlan.this);
            cbrFitnessUtil.saveRevJavaLog("\n", ConfigurePlan.this);
            cbrFitnessUtil.saveRevJavaLog(exerciseList.get(i).exToString(), ConfigurePlan.this);

            // Es wird überprüft, ob die Exercise bereits vorhanden ist, falls nicht, wird sie der neuen Liste hinzugefügt.
            for(int j  = 0; j < resultList.size(); j++) {
                if(exerciseList.exExists(resultList.get(j).getExercise().getExName())|| exList.exExists(resultList.get(j).getExercise().getExName())) {
                    System.out.println("Übung ist bereits vorhanden");
                } else {
                    cbrFitnessUtil.saveRevJavaLog("Change with", ConfigurePlan.this);
                    cbrFitnessUtil.saveRevJavaLog("\n", ConfigurePlan.this);
                    cbrFitnessUtil.saveRevJavaLog(resultList.get(j).ResultToString(), ConfigurePlan.this);

                    // Hinzufügen der neuen Exercise.
                    exList.add(resultList.get(j).getExercise());
                    break;
                }
            }
        }
        return exList;
    }
}