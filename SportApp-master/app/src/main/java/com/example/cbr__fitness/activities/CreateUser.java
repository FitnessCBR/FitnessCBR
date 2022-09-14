package com.example.cbr__fitness.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.helper.widget.Flow;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.cbr__fitness.customListenerMethods.ColorChangeToggleListener;
import com.example.cbr__fitness.customViewElements.ColorChangeToggleButton;
import com.example.cbr__fitness.data.User;
import com.example.cbr__fitness.databasehelper.FitnessDBSqliteHelper;
import com.example.cbr__fitness.data.Limitation;
import com.example.cbr__fitness.enums.EquipmentEnum;
import com.example.cbr__fitness.enums.GenderEnum;
import com.example.cbr__fitness.enums.WorkoutEnum;
import com.example.cbr__fitness.logic.CBRFitnessUtil;
import com.example.cbr__fitness.R;
import com.example.cbr__fitness.logic.SharedPreferenceManager;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jobst-Julius Bartels
 */

// Diese Klasse stellt die Aktitivtät zur Erstellung eines Benutzers dar.
public class CreateUser extends AppCompatActivity {

    // Attribute der Klasse.
    private User user;
    private CBRFitnessUtil cbrFitnessUtil;
    private EditText editName;
    private EditText editPassword;
    private EditText editAge;
    private RadioGroup rgGender;
    private RadioGroup rgWorkType;
    private EditText editBodyWeight;
    private EditText editBodyHeight;
    private Flow resFlow;
    private Flow equipmentFlow;
    private ConstraintLayout constraintLayout;

    private List<ColorChangeToggleButton> toggleButtonsRestrictions;

    private List<ColorChangeToggleButton> toggleButtonsEquipment;

    private FitnessDBSqliteHelper helper;

    @Override
    // OnCreate Methode der Klasse.
    protected void onCreate(Bundle savedInstanceState) {

        //Layout.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_user_window);

        // Initialisierung der Util-Klassen und anderen Attributen.
        user = new User();
        cbrFitnessUtil = new CBRFitnessUtil();

        //Layout-Attribute.
        resFlow = findViewById(R.id.resFlowBox);
        equipmentFlow = findViewById(R.id.flow_choose_equipment_create_user);
        editName = findViewById(R.id.editTextUsername);
        editPassword = findViewById(R.id.editPassword);
        editAge = findViewById(R.id.editAge);
        rgGender = findViewById(R.id.rgGender);
        rgWorkType = findViewById(R.id.rgWorkoutType);
        editBodyWeight = findViewById(R.id.editBodyWeight);
        editBodyHeight = findViewById(R.id.editBodyHeight);
        Button createUserButton = findViewById(R.id.createUserButton);
        constraintLayout = findViewById(R.id.constLayout);

        createUserButton.setOnClickListener(v -> {
            if(checkInputs()) {
                if(!helper.checkUserNameAvailable(editName.getText().toString())) {
                    Toast.makeText(CreateUser.this, "Username already assigned!", Toast.LENGTH_SHORT).show();
                } else {
                    createUser();
                    finish();
                    Toast.makeText(CreateUser.this, "User created!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(CreateUser.this, "Empty Input fields!", Toast.LENGTH_SHORT).show();
            }
        });

        helper = new FitnessDBSqliteHelper(this);

        setUpRestrictionButtons();
        setUpEquipmentButtons();
    }

    // Diese Methode erstellt aus den Input-Felder einen neuen Benutzer.
    public void createUser() {

        int rbId;
        RadioButton rb;
        String username = editName.getText().toString();
        editName.getText().clear();

        user.setUserPassword(editPassword.getText().toString());
        String password = editPassword.getText().toString();
        editPassword.getText().clear();

        user.setAge(editAge.getText().toString());
        int age  = Integer.parseInt(editAge.getText().toString());
        editAge.getText().clear();

        rbId = rgGender.getCheckedRadioButtonId();
        rb = findViewById(rbId);
        int genderInt  = GenderEnum.valueOf(rb.getText().toString()).getID();
        user.setGender(rb.getText().toString());
        rgGender.clearCheck();

        rbId = rgWorkType.getCheckedRadioButtonId();
        rb = findViewById(rbId);
        int workoutInt = WorkoutEnum.valueOf(rb.getText().toString()).getID();
        user.setWorktype(rb.getText().toString());
        rgWorkType.clearCheck();

        int bodyWeight = Integer.parseInt(editBodyWeight.getText().toString());
        editBodyWeight.getText().clear();

        //gets the IDs of the chosen restrictions from the buttons.
        List<Integer> restrictionIDs = new ArrayList<>();
        for (ColorChangeToggleButton c : toggleButtonsRestrictions) {
            if (c.isChecked()) {
                restrictionIDs.add(c.getItemID());
            }
        }

        int bodyHeight = Integer.parseInt(editBodyHeight.getText().toString());
        editBodyHeight.getText().clear();

        List<Integer> equipmentIDs = new ArrayList<>();
        for (ColorChangeToggleButton c : toggleButtonsEquipment) {
            if (c.isChecked()) {
                equipmentIDs.add(c.getItemID());
            }
        }

        long id = helper.inputNewUserIntoDB(username, password, age, genderInt, bodyWeight, bodyHeight
                , workoutInt, restrictionIDs, equipmentIDs);

        SharedPreferenceManager.saveUserID((int)id, this);

        user.setPathData(user.getUsername() +".txt");
        cbrFitnessUtil.save(user.getPathData(),"", CreateUser.this);
    }

    // Diese Methode überprüft, ob die Eingaben der Input-Felder valide sind.
    public boolean checkInputs() {

        boolean check = true;
        if(editName.getText().toString().matches("")){
            check = false;
        } else if(editPassword.getText().toString().matches("")) {
            check = false;
        } else if(editAge.getText().toString().matches("")) {
            check = false;
        } else if(rgGender.getCheckedRadioButtonId() == -1) {
            check = false;
        } else if(rgWorkType.getCheckedRadioButtonId() == -1) {
            check = false;
        } else if(editBodyWeight.getText().toString().matches("")) {
            check = false;
        }
        return check;
    }

    private void setUpRestrictionButtons() {
        List<Limitation> limitations = helper.getLimitationsFromDB();
        for (Limitation l : limitations) {
            System.out.println("----------------------+ " + l.getName() + " ID; "+ l.getID());
        }
        toggleButtonsRestrictions = new ArrayList<>();
        for (Limitation l : limitations) {
            ColorChangeToggleButton t = new ColorChangeToggleButton(this, l.getID(), l.getName());
            t.setOnCheckedChangeListener(ColorChangeToggleListener::onCheckedChanged);

            toggleButtonsRestrictions.add(t);
            constraintLayout.addView(t);

            resFlow.addView(t);
        }
    }

    private void setUpEquipmentButtons() {
        List<EquipmentEnum> equipmentEnumList = helper.getEquipmentFromDB();

        toggleButtonsEquipment = new ArrayList<>();
        for (EquipmentEnum eqn : equipmentEnumList) {
            if (eqn.getID() != 1) { //"Keins" is equipment everyone should have thus its excluded here.
                ColorChangeToggleButton t = new ColorChangeToggleButton(this, eqn);
                t.setOnCheckedChangeListener(ColorChangeToggleListener::onCheckedChanged);

                toggleButtonsEquipment.add(t);
                constraintLayout.addView(t);

                equipmentFlow.addView(t);
            }
        }
    }
}