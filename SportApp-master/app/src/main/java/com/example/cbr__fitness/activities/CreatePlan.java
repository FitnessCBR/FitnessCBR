package com.example.cbr__fitness.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.example.cbr__fitness.data.Exercise;
import com.example.cbr__fitness.data.ExerciseList;
import com.example.cbr__fitness.data.Plan;
import com.example.cbr__fitness.data.PlanList;
import com.example.cbr__fitness.logic.CBRFitnessUtil;
import com.example.cbr__fitness.R;
import java.util.ArrayList;
import java.util.Collections;

/**
 * @author Jobst-Julius Bartels
 */

// Diese Klasse stell die Aktivität zur Erstellung eines Plans dar.
public class CreatePlan extends AppCompatActivity implements View.OnClickListener {

    // Attribute der Klasse
    private ListView listViewEx;
    private ArrayAdapter arrayAdapter;
    private ExerciseList exerciseBaseList;
    private ArrayList<String> arrayListExStrings;
    private ArrayList<String> arrayListExPStrings;
    private ListView listViewPlan;
    private ArrayAdapter arrayAdapterP;
    private ExerciseList userExList;
    private CBRFitnessUtil cbrFitnessUtil;
    private EditText editTxtPName;
    private EditText editTxtPTime;
    private EditText editTxtPRating;
    private TextView textEx;
    private Spinner spinnerExMuscle;
    private String[] spinnerItems;
    private Plan plan;
    private PlanList planList;
    private ImageButton imgButtonLogo;
    private Button createPButton;

    @Override
    // OnClick Methode der Klasse.
    public void onClick(View v) {
        switch (v.getId()) {

            // Durch Klicken des createPButtons wird der Plan abgespeichert und die nächste Aktivität gestartet.
            case R.id.createPButton:
                if(checkInputs()) {
                    if(userExList.size() < 3) {
                        Toast.makeText(this, "Minium of 3 exercise allowed!", Toast.LENGTH_SHORT).show();
                    } else {
                        if(MainActivity.userLogged.getPlanList().pExists(editTxtPName.getText().toString()) || planList.pExists(editTxtPName.getText().toString())) {
                            Toast.makeText(this, "Plan name already assigned!", Toast.LENGTH_SHORT).show();
                        } else{
                            finishWindow();
                        }
                    }
                } else {
                    Toast.makeText(this, "Empty Input fields!", Toast.LENGTH_SHORT).show();
                }
                break;

            // Durch Klicken des imgButtonLogos gelangt der Benutzer zurück zur vorherigen Aktivität.
            case R.id.imgButtonLogo:
                Intent i = new Intent(CreatePlan.this, ConfigurePlan.class);
                startActivity(i);
                break;
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    // Oncreate Methode der Klasse.
    protected void onCreate(Bundle savedInstanceState) {

        //Layout.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_plan);

        // Initialisierung der Util-Klassen und anderen Attributen.
        cbrFitnessUtil = new CBRFitnessUtil();
        arrayListExStrings = new ArrayList<String>();
        arrayListExPStrings = new ArrayList<String>();
        exerciseBaseList = new ExerciseList();
        userExList = new ExerciseList();
        plan = new Plan();
        planList = new PlanList();
        exerciseBaseList = MainActivity.exerciseBaseList;
        planList = MainActivity.planBaseList;

        //Layout-Attribute.
        editTxtPName = findViewById(R.id.editTxtPName);
        editTxtPTime = findViewById(R.id.editTxtPTime);
        editTxtPRating = findViewById(R.id.editTxtPRating);
        listViewPlan = findViewById(R.id.listviewExPlan);
        listViewEx = findViewById(R.id.listviewEx);
        textEx = findViewById(R.id.txtAllExPlan);
        spinnerExMuscle = findViewById(R.id.spinnerExMuscle);
        imgButtonLogo = findViewById(R.id.imgButtonLogo);
        createPButton = findViewById(R.id.createPButton);

        // Spinner-Elemente werden gesetzt.
        spinnerItems = new String[]{"No Selection", "FullBody", "UpperBody", "LowerBody", "Chest", "Shoulders", "Back", "Legs", "Arms", "Abs"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, spinnerItems);
        spinnerExMuscle.setAdapter(adapter);
        // Hinzufügen der OnClickListener zu den Buttons.
        imgButtonLogo.setOnClickListener(this);
        createPButton.setOnClickListener(this);
        //Deaktivierung des Rating Input-Felds.
        editTxtPRating.setEnabled(false);

        //ViewList werden gesetzt.
        setViewList(exerciseBaseList);
        Collections.sort(arrayListExStrings);
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayListExStrings);
        arrayAdapterP = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayListExPStrings);
        listViewEx.setAdapter(arrayAdapter);
        listViewPlan.setAdapter(arrayAdapterP);

        // TextView für die Anzahl der bisher hinzugefügten Exercises.
        textEx.setText("Current exercises: " + userExList.size());

        // OnItemClickListener für das Auswählen der Exercises.
        listViewEx.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                // Wenn die Liste leer ist, kann die Exercise direkt hinzugefügt werden.
                if(userExList.isEmpty()) {
                    for(int k = 0; k < exerciseBaseList.size(); k++) {
                        if(arrayListExStrings.get(i).matches(exerciseBaseList.get(k).getExName())) {

                            // Exercise wird hinzugefügt.
                            userExList.add(exerciseBaseList.get(k));
                            arrayListExPStrings.add(exerciseBaseList.get(k).getExName());
                        }
                    }
                    // DataSet wird aktualisiert.

                    arrayAdapterP.notifyDataSetChanged();
                    textEx.setText("Current exercises: " + userExList.size());
                    Toast.makeText(CreatePlan.this,arrayListExStrings.get(i) + " has been added!",Toast.LENGTH_SHORT).show();

                // Es wird überprüft, ob die Exercise bereits enthalten ist.
                } else if(userExList.exExists(arrayListExStrings.get(i))) {
                    Toast.makeText(CreatePlan.this,arrayListExStrings.get(i) + " is already in the Plan!",Toast.LENGTH_SHORT).show();
                } else {
                    if(userExList.size() == 20) {
                        Toast.makeText(CreatePlan.this,"Limit of exercises!",Toast.LENGTH_SHORT).show();
                    } else {
                        for(int k = 0; k < exerciseBaseList.size(); k++) {
                            if(arrayListExStrings.get(i).matches(exerciseBaseList.get(k).getExName())) {

                                // Exercise wird hinzugefügt.
                                userExList.add(exerciseBaseList.get(k));
                                arrayListExPStrings.add(exerciseBaseList.get(k).getExName());
                            }
                        }

                        // DataSet wird aktualisiert.
                        arrayAdapterP.notifyDataSetChanged();
                        textEx.setText("Current exercises: " + userExList.size());
                        Toast.makeText(CreatePlan.this,arrayListExStrings.get(i) + " has been added!",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        // OnItemCLickListener für das entfernen der Exercise aus der aktuellen Liste des Benutzers.
        listViewPlan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            userExList.remove(userExList.get(i));
            arrayAdapterP.notifyDataSetChanged();
            textEx.setText("Current exercises: " + userExList.size());
            Toast.makeText(CreatePlan.this,arrayListExPStrings.get(i) + " has been removed!",Toast.LENGTH_SHORT).show();
            arrayListExPStrings.remove(i);
            }
        });

    }

    // Methode zur Befüllung der ViewList.
    public void setViewList(ArrayList<Exercise> arrayListEx) {

        for(int i = 0; i < arrayListEx.size(); i++) {
            arrayListExStrings.add(arrayListEx.get(i).getExName());
        }
    }

    // Diese Methode sammelt alle notwendigen Daten aus den Input-Feldern und erstellt einen neuen Plan.
    public void createPlan() {

        plan.setpName(editTxtPName.getText().toString());
        editTxtPName.getText().clear();
        plan.setpEx(Integer.toString(userExList.size()));
        plan.setpTime(editTxtPTime.getText().toString());
        editTxtPTime.getText().clear();
        plan.setpRating("0");
        editTxtPRating.getText().clear();
        plan.setpExList(userExList);
        plan.setMusclePrio(spinnerExMuscle.getSelectedItem().toString());
        spinnerExMuscle.setSelection(-1);
        MainActivity.userLogged.getPlanList().add(plan);
        MainActivity.planBaseList.add(plan);
    }

    // Diese Methode überprüft, ob die Eingabe der Input-Felder valide sind.
    public boolean checkInputs() {

        boolean check = true;
        if(editTxtPName.getText().toString().matches("")){
            check = false;
        } else if(editTxtPTime.getText().toString().matches("")) {
            check = false;
        } else if(spinnerExMuscle.getSelectedItem().toString().matches("No Selection")) {
            check = false;
        } else if(userExList.isEmpty()) {
            check = false;
        }
        return check;
    }

    // Diese Methode fügt dem Profil des Benutzers den zuvor erstellen Plan hinzu und startet die nächste Aktivität.
    public void finishWindow() {

        createPlan();
        cbrFitnessUtil.save(MainActivity.userLogged.getPathData(), cbrFitnessUtil.load(MainActivity.userLogged.getPathData(),plan.pToString() ,CreatePlan.this), CreatePlan.this);
        Intent i = new Intent(CreatePlan.this, ConfigurePlan.class);
        startActivity(i);
        Toast.makeText(this, "Plan created!", Toast.LENGTH_SHORT).show();
    }

}