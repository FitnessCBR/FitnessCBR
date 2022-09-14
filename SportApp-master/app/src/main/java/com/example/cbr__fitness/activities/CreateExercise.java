package com.example.cbr__fitness.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import com.example.cbr__fitness.data.Exercise;
import com.example.cbr__fitness.data.ExerciseList;
import com.example.cbr__fitness.logic.CBRFitnessUtil;
import com.example.cbr__fitness.R;

/**
 * @author Jobst-Julius Bartels
 */

// Diese Klasse stellt die Aktivität zur Erstellung einer Exercise dar.
public class CreateExercise extends AppCompatActivity implements View.OnClickListener {

    // Attribute der Klasse.
    private Exercise ex;
    private ExerciseList exerciseBaseList;
    private CBRFitnessUtil cbrFitnessUtil;
    private EditText editExName;
    private EditText editExSetNumber;
    private EditText editExRep;
    private EditText editExBreak;
    private EditText editExWeight;
    private EditText editExTime;
    private RadioGroup rgExType;
    private EditText editExRating;
    private Spinner spinnerExMuscle;
    private Button createExButton;
    private ImageButton imgButtonLogo;
    private String[] spinnerItems;

    @Override
    // OnClick Methode der Klasse.
    public void onClick(View v) {
        switch (v.getId()) {

            // Durch Klicken des createExButtons wird die Übung dem System hinzugefügt..
            case R.id.createExButton:
                if(checkInputs()) {
                    if(exerciseBaseList.exExists(editExName.getText().toString())) {
                        Toast.makeText(this, "Exercise name already assigned!", Toast.LENGTH_SHORT).show();
                    } else {
                        createEx();
                        cbrFitnessUtil.save("exerciseBase.txt", cbrFitnessUtil.load("exerciseBase.txt", ex.exToString(), CreateExercise.this), CreateExercise.this);
                        finish();
                        Toast.makeText(this, "Exercise created!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Empty Input fields!", Toast.LENGTH_SHORT).show();
                }
                break;

            // Durch Klicken des imgButtonLogos gelangt der Benutzer zurück zur vorherigen Aktivität.
            case R.id.imgButtonLogo:
                Intent i = new Intent(CreateExercise.this, ConfigurePlan.class);
                startActivity(i);
                break;
        }
    }

    @Override
    // Oncreate Methode der Klasse.
    protected void onCreate(Bundle savedInstanceState) {

        //Layout.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_exercise);

        // Initialisierung der Util-Klassen und anderen Attributen.
        ex = new Exercise();
        exerciseBaseList = new ExerciseList();
        cbrFitnessUtil = new CBRFitnessUtil();
        exerciseBaseList = MainActivity.exerciseBaseList;

        //Layout-Attribute.
        editExName = findViewById(R.id.editTxtExName);
        editExSetNumber = findViewById(R.id.editTxtExSetNumber);
        editExRep = findViewById(R.id.editTxtExRep);
        editExBreak = findViewById(R.id.editTxtExBreak);
        editExWeight = findViewById(R.id.editTxtExWeight);
        editExTime = findViewById(R.id.editTxtExTime);
        rgExType = findViewById(R.id.rgExType);
        editExRating = findViewById(R.id.editTextExRating);
        spinnerExMuscle = findViewById(R.id.spinnerExMuscle);
        createExButton = findViewById(R.id.createExButton);
        imgButtonLogo = findViewById(R.id.imgButtonLogo);

        // Initialisierung des Spinners.
        spinnerItems = new String[]{"No Selection", "Chest", "Shoulders", "Back", "Legs", "Bicep", "Tricep", "Abs"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, spinnerItems);
        spinnerExMuscle.setAdapter(adapter);

        // Hinzufügen der OnCLickListener zu den Buttons.
        createExButton.setOnClickListener(this);
        imgButtonLogo.setOnClickListener(this);

    }

    // Diese Methode sammelt alle Daten aus den Input-Feldern und erstellt eine neue Übung.
    public void createEx() {

        int rbId;
        RadioButton rb;
        ex.setExName(editExName.getText().toString());
        editExName.getText().clear();
        ex.setExSetNumber(editExSetNumber.getText().toString());
        editExSetNumber.getText().clear();
        ex.setExRep(editExRep.getText().toString());
        editExRep.getText().clear();
        ex.setExBreak(editExBreak.getText().toString());
        editExBreak.getText().clear();
        ex.setExWeight(editExWeight.getText().toString());
        editExWeight.getText().clear();
        ex.setExTime(editExTime.getText().toString());
        editExTime.getText().clear();
        ex.setExRating(editExRating.getText().toString());
        editExRating.getText().clear();
        rbId = rgExType.getCheckedRadioButtonId();
        rb = findViewById(rbId);
        ex.setExType(rb.getText().toString());
        rgExType.clearCheck();
        ex.setExMuscle(spinnerExMuscle.getSelectedItem().toString());
        spinnerExMuscle.setSelection(-1);
        exerciseBaseList.add(ex);
    }

    // Diese Methode überprüft, ob der Inhalt der Input-Felder valide ist.
    public boolean checkInputs() {

        boolean check = true;
        if(editExName.getText().toString().matches("")){
            check = false;
        } else if(editExSetNumber.getText().toString().matches("")) {
            check = false;
        } else if(editExRep.getText().toString().matches("")) {
            check = false;
        } else if(editExBreak.getText().toString().matches("")) {
            check = false;
        } else if(editExWeight.getText().toString().matches("")) {
            check = false;
        } else if(editExTime.getText().toString().matches("")) {
            check = false;
        } else if(spinnerExMuscle.getSelectedItem().toString().matches("No Selection")) {
            check = false;
        } else if(editExRating.getText().toString().matches("")) {
            check = false;
        } else if(rgExType.getCheckedRadioButtonId() == -1) {
            check = false;
        }
        return check;
    }
}