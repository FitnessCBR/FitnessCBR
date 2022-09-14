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
import com.example.cbr__fitness.data.Plan;
import com.example.cbr__fitness.data.PlanList;
import com.example.cbr__fitness.logic.CBRFitnessUtil;
import com.example.cbr__fitness.R;

/**
 * @author Jobst-Julius Bartels
 */

// Diese Klasse stellt die Aktivität für die Änderung einer Exercise dar.
public class EditExercise extends AppCompatActivity implements View.OnClickListener {

    // Attribute der Klasse.
    private Exercise ex;
    private Plan plan;
    private PlanList planList;
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

            // Durch Klicken des createExButton werden die Änderungen der Exercise übernommen und abgespeichert.
            case R.id.createExButton:
                if(checkInputs()) {
                    finishWindow();
                } else {
                    Toast.makeText(this, "Empty Input fields!", Toast.LENGTH_SHORT).show();
                }
                break;

            // Durch Klicken des imgButtonLogos gelangt der Benutzer zurück zur vorherigen Aktivität.
            case R.id.imgButtonLogo:
                Intent i = new Intent(EditExercise.this, EditPlan.class);
                startActivity(i);
                break;
        }
    }

    @Override
    // OnCreate Methode der Klasse.
    protected void onCreate(Bundle savedInstanceState) {

        // Layout.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_exercise);

        // Initialisierung der Util-Klassen und anderen Attributen.
        ex = new Exercise();
        plan = new Plan();
        planList = new PlanList();
        cbrFitnessUtil = new CBRFitnessUtil();
        planList = cbrFitnessUtil.getUserPList(cbrFitnessUtil.loadAll(MainActivity.userLogged.getPathData(), EditExercise.this));
        plan = planList.get(ConfigurePlan.planNumber);
        ex = planList.get(ConfigurePlan.planNumber).getpExList().get(EditPlan.exNumber);

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

        // Spinner wird bestimmt.
        spinnerItems = new String[]{"No Selection", "Chest", "Shoulders", "Back", "Legs", "Bicep", "Tricep", "Abs"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, spinnerItems);
        spinnerExMuscle.setAdapter(adapter);

        // Deaktivierung der Input-Felder der Attribute: Name und MuscleTyp, da diese nicht verändert werden dürfen.
        editExName.setEnabled(false);
        spinnerExMuscle.setEnabled(false);

        // Hinzufügen der OnClickListener zu den Buttons.
        createExButton.setOnClickListener(this);
        imgButtonLogo.setOnClickListener(this);

        // Visualisiert die aktuellen Daten der Exercise.
        setExSetts();

    }
    // Diese Methode übernimmt die Änderungen der Exercise.
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
        plan.getpExList().add(ex);
    }

    // Diese Methode visualisiert die aktuellen Daten der Exercise.
    public void setExSetts() {

        editExName.setText(ex.getExName());
        editExSetNumber.setText(ex.getExSetNumber());
        editExRep.setText(ex.getExRep());
        editExBreak.setText(ex.getExBreak());
        editExRating.setText(ex.getExRating());
        editExTime.setText(ex.getExTime());
        editExWeight.setText(ex.getExWeight());
        spinnerExMuscle.setSelection(ex.getExMuscleInt());
        if(ex.getExType().equals("Machine")) {
            rgExType.check(R.id.rbMachine);
        } else if(ex.getExType().equals("Free")) {
            rgExType.check(R.id.rbFree);
        } else {
            rgExType.check(R.id.rbBodyweight);
        }
    }
    // Diese Methode aktualisiert die Änderungen an dem Datensatz.
    public void finishWindow() {

        plan.getpExList().removeExercise(ex.getExName());
        createEx();
        planList.set(ConfigurePlan.planNumber, plan);
        cbrFitnessUtil.save(MainActivity.userLogged.getPathData(),planList.pListToString(),EditExercise.this);
        Intent i = new Intent(EditExercise.this, EditPlan.class);
        startActivity(i);
        Toast.makeText(this, "Changes saved!", Toast.LENGTH_SHORT).show();
    }
    // Diese Methode überprüft, ob die Eingaben der Input-Felder valide sind.
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