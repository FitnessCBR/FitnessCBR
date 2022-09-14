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

// Die Klasse stellt die Aktivität zur Änderung eines Plans dar.
public class EditPlan extends AppCompatActivity implements View.OnClickListener {

    // Attribute der Klasse.
    private ListView listViewEx;
    private ArrayAdapter arrayAdapter;
    private ExerciseList exList;
    private ArrayList<String> arrayListExStrings;
    private ArrayList<String> arrayListExPStrings;
    private ListView listViewPlan;
    private ArrayAdapter arrayAdapterP;
    private CBRFitnessUtil cbrFitnessUtil;
    private EditText editTxtPName;
    private EditText editTxtPTime;
    private EditText editTxtPRating;
    private TextView textEx;
    private Spinner spinnerExMuscle;
    private String[] spinnerItems;
    private Plan plan;
    private PlanList planList;
    private PlanList userPlanList;
    private PlanList userBasePList;
    private ImageButton imgButtonLogo;
    private Button createPButton;
    public static int exNumber;

    @Override
    // OnClick Methode der Klasse.
    public void onClick(View v) {
        switch (v.getId()) {

            // Durch Klicken des createPButtons werden die Planänderung abgespeichert und die nächste Aktivität gestartet.
            case R.id.createPButton:
                if(checkInputs()) {
                    if(plan.getpExList().size() < 3) {
                        Toast.makeText(this, "Minium of 3 exercise allowed!", Toast.LENGTH_SHORT).show();
                    } else {
                            finishWindow();
                    }
                } else {
                    Toast.makeText(this, "Empty Input fields!", Toast.LENGTH_SHORT).show();
                }
                break;

            // Durch Klicken des imgButtonLogos gelangt der Benutzer zurück zur vorherigen Aktivität.
            case R.id.imgButtonLogo:
                Intent i = new Intent(EditPlan.this, ConfigurePlan.class);
                startActivity(i);
                break;
        }
    }

    @Override
    // OnCreate Methode der Klasse.
    protected void onCreate(Bundle savedInstanceState) {

        // Layout.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_plan);

        // Initialisierung der Util-Klassen und anderen Attributen.
        cbrFitnessUtil = new CBRFitnessUtil();
        arrayListExStrings = new ArrayList<String>();
        arrayListExPStrings = new ArrayList<String>();
        exList = new ExerciseList();
        plan = new Plan();
        userBasePList = new PlanList();
        planList = new PlanList();
        userPlanList = cbrFitnessUtil.getUserPList(cbrFitnessUtil.loadAll(MainActivity.userLogged.getPathData(), EditPlan.this));
        userBasePList = cbrFitnessUtil.getUserPList(cbrFitnessUtil.loadAll(MainActivity.userLogged.getPathData(), EditPlan.this));
        plan = userPlanList.get(ConfigurePlan.planNumber);

        // Layout-Attribute.
        editTxtPName = findViewById(R.id.editTxtPName);
        editTxtPTime = findViewById(R.id.editTxtPTime);
        editTxtPRating = findViewById(R.id.editTxtPRating);
        listViewPlan = findViewById(R.id.listviewExPlan);
        listViewEx = findViewById(R.id.listviewEx);
        textEx = findViewById(R.id.txtAllExPlan);
        spinnerExMuscle = findViewById(R.id.spinnerExMuscle);
        imgButtonLogo = findViewById(R.id.imgButtonLogo);
        createPButton = findViewById(R.id.createPButton);

        // ViewListen werden bestimmt.
        exList = MainActivity.exerciseBaseList;
        setViewList(exList);
        Collections.sort(arrayListExStrings);
        addExToUserPlanView();
        planList = MainActivity.planBaseList;

        // Spinner werden geladen.
        spinnerItems = new String[]{"No Selection", "FullBody", "UpperBody", "LowerBody", "Chest", "Shoulders", "Back","Legs", "Arms", "Abs"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, spinnerItems);
        spinnerExMuscle.setAdapter(adapter);
        spinnerExMuscle.setEnabled(false);

        // ViewListen werden geladen.
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayListExStrings);
        arrayAdapterP = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayListExPStrings);
        listViewEx.setAdapter(arrayAdapter);
        listViewPlan.setAdapter(arrayAdapterP);

        // Hinzufügen der OnClickListener zu den Buttons.
        imgButtonLogo.setOnClickListener(this);
        createPButton.setOnClickListener(this);

        // Input-FeldName wird deaktiviert.
        editTxtPName.setEnabled(false);

        // Bisherige Daten des Plans werden visualisiert.
        setPlanSetts();

        // OnItemClickListener für die ViewListe der Exercises
        listViewEx.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                // Wenn die Liste leer ist, kann die Exercise direkt hinzugefügt werden.
                if(plan.getpExList().isEmpty()) {
                    for(int k = 0; k < exList.size(); k++) {
                        if(arrayListExStrings.get(i).matches(exList.get(k).getExName())) {
                            plan.getpExList().add(exList.get(k));
                            arrayListExPStrings.add(exList.get(k).getExName());
                        }
                    }

                    // UI Update
                    arrayAdapterP.notifyDataSetChanged();
                    textEx.setText("Current exercises: " + plan.getpExList().size());
                    Toast.makeText(EditPlan.this,arrayListExStrings.get(i) + " has been added!",Toast.LENGTH_SHORT).show();

                // Überprüfung, ob Exercise bereits im aktuellen Plan enthalten ist.
                } else if(plan.getpExList().exExists(arrayListExStrings.get(i))) {
                    Toast.makeText(EditPlan.this,arrayListExStrings.get(i) + " is already in the Plan!",Toast.LENGTH_SHORT).show();
                } else {

                    // Limit von 20 Exercises pro Plan darf nicht überschritten werden.
                    if(plan.getpExList().size() == 20) {
                        Toast.makeText(EditPlan.this,"Limit of exercises!",Toast.LENGTH_SHORT).show();

                    // Wenn das Limit nicht überschritten wurde, darf die Exercise hinzugefügt werden.
                    } else {
                        for(int k = 0; k < exList.size(); k++) {
                            if(arrayListExStrings.get(i).matches(exList.get(k).getExName())) {
                                plan.getpExList().add(exList.get(k));
                                arrayListExPStrings.add(exList.get(k).getExName());
                            }
                        }

                        // UI Update
                        arrayAdapterP.notifyDataSetChanged();
                        textEx.setText("Current exercises: " + plan.getpExList().size());
                        Toast.makeText(EditPlan.this,arrayListExStrings.get(i) + " has been added!",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        // Wenn länger auf die Exercise der aktuellen Liste geklickt wird, gelangt der Benutzer zur Exercise-Änderung Aktivität.
        listViewPlan.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                if(exApplied(i)) {
                    exNumber = i;
                    Intent j = new Intent(EditPlan.this, EditExercise.class);
                    startActivity(j);
                } else {
                    Toast.makeText(EditPlan.this,"Please apply changes!",Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });

        // Wenn normal auf die Exercise der aktuellen Liste geklickt wird, wird die Exercise entfernt.
        listViewPlan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                plan.getpExList().remove(plan.getpExList().get(i));
                arrayAdapterP.notifyDataSetChanged();
                textEx.setText("Current exercises: " + plan.getpExList().size());
                Toast.makeText(EditPlan.this,arrayListExPStrings.get(i) + " has been removed!",Toast.LENGTH_SHORT).show();
                arrayListExPStrings.remove(i);
            }
        });
    }

    // Diese Methode dient zur Vorbereitung der ViewList.
    public void setViewList(ArrayList<Exercise> arrayListEx) {

        for(int i = 0; i < arrayListEx.size(); i++) {
            arrayListExStrings.add(arrayListEx.get(i).getExName());
        }

    }

    // Diese Methode übernimmt die Änderungen und fügt sie dem Benutzerprofil hinzu.
    public void createPlan() {

        plan.setpName(editTxtPName.getText().toString());
        plan.setpEx(Integer.toString(plan.getpExList().size()));
        plan.setpTime(editTxtPTime.getText().toString());
        plan.setpRating(editTxtPRating.getText().toString());
        plan.setpExList(plan.getpExList());
        plan.setMusclePrio(spinnerExMuscle.getSelectedItem().toString());
        spinnerExMuscle.setSelection(-1);
        MainActivity.userLogged.getPlanList().add(plan);
    }

    // Diese Methode überprüft, ob die Eingabe der Input-Felder valide ist.
    public boolean checkInputs() {

        boolean check = true;
        if(editTxtPName.getText().toString().matches("")){
            check = false;
        } else if(editTxtPTime.getText().toString().matches("")) {
            check = false;
        } else if(editTxtPRating.getText().toString().matches("")) {
            check = false;
        } else if(plan.getpExList().isEmpty()) {
            check = false;
        }
        return check;
    }

    // Diese Methode nimmt die Speicherungen an den Datensätzen vor.
    public void finishWindow() {

        userPlanList.removePlan(plan.getpName());
        cbrFitnessUtil.save(MainActivity.userLogged.getPathData(), userPlanList.pListToString(), EditPlan.this);
        planList.removePlan(plan.getpName());
        MainActivity.userLogged.getPlanList().removePlan(plan.getpName());
        MainActivity.userLogged.getPlanList().add(plan);
        createPlan();
        cbrFitnessUtil.save(MainActivity.userLogged.getPathData(), cbrFitnessUtil.load(MainActivity.userLogged.getPathData(),plan.pToString() ,EditPlan.this), EditPlan.this);
        userBasePList = cbrFitnessUtil.getUserPList(cbrFitnessUtil.loadAll(MainActivity.userLogged.getPathData(), EditPlan.this));
        Toast.makeText(this, "Changes saved!", Toast.LENGTH_SHORT).show();
    }

    // Diese Methode visualisiert die aktuellen Daten des Plans.
    @SuppressLint("SetTextI18n")
    public void setPlanSetts() {

        editTxtPName.setText(plan.getpName());
        textEx.setText("Current exercises: " + plan.getpEx());
        editTxtPTime.setText(plan.getpTime());
        System.out.println(plan.getMusclePrioInt());
        spinnerExMuscle.setSelection(plan.getMusclePrioInt());
        editTxtPRating.setText(plan.getpRating());
    }

    // Diese Methode läd die ViewList der aktuellen Exercises.
    public void addExToUserPlanView() {

        for(int i = 0; i < plan.getpExList().size(); i++) {
            arrayListExPStrings.add(plan.getpExList().get(i).getExName());
        }
    }

    // Diese Methode überprüft, ob der ausgewählte Plan auch tatsächlich teil des aktuellen Plans ist.
    public boolean exApplied(int i) {

        boolean search = false;
        for(int k = 0; k < userBasePList.get(ConfigurePlan.planNumber).getpExList().size(); k++) {
            if(arrayListExPStrings.get(i).equalsIgnoreCase(userBasePList.get(ConfigurePlan.planNumber).getpExList().get(k).getExName())) {
                search = true;
            }
        }
        return search;
    }
}