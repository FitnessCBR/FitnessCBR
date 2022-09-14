package com.example.cbr__fitness.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.example.cbr__fitness.data.User;
import com.example.cbr__fitness.data.UserList;
import com.example.cbr__fitness.logic.CBRFitnessUtil;
import com.example.cbr__fitness.R;

/**
 * @author Jobst-Julius Bartels
 */

// Diese Klasse stellt die Aktivität für die Änderungen an dem Benutzerprofil dar.
public class UserSettings extends AppCompatActivity implements View.OnClickListener {

    // Attribute der Klasse.
    private User user;
    private UserList userList;
    private CBRFitnessUtil cbrFitnessUtil;
    private EditText editName;
    private EditText editPassword;
    private EditText editAge;
    private RadioGroup rgGender;
    private RadioGroup rgWorkType;
    private RadioGroup rgBodyType;
    private EditText editDuration;
    private RadioGroup rgRes;
    private ImageButton imgButtonLogo;
    private Button saveUser;

    @Override
    // OnClick Methode der Klasse.
    public void onClick(View v) {
        switch (v.getId()) {

            // Durch Klicken des saveUser Buttons werden die Änderungen abgespeichert und die nächste Aktivität gestartet.
            case R.id.saveUser:
                if(checkInputs()) {
                    finishAct();
                } else {
                    Toast.makeText(this, "Empty Input fields!", Toast.LENGTH_SHORT).show();
                }
                break;

            // Durch Klicken des imgButtonLogos gelangt der Benutzer zurück zur vorherigen Aktivität.
            case R.id.imgButtonLogo3:
                Intent i = new Intent(UserSettings.this, UserInterface.class);
                startActivity(i);
                break;
        }
    }

    @Override
    // OnCreate Methode der Klasse.
    protected void onCreate(Bundle savedInstanceState) {

        //Layout.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_settings);

        // Initialisierung der Util-Klassen und anderen Attributen.
        cbrFitnessUtil = new CBRFitnessUtil();
        user = new User();
        userList = new UserList();
        userList = MainActivity.userList;

        // Layout-Attribute.
        imgButtonLogo = findViewById(R.id.imgButtonLogo3);
        editName = findViewById(R.id.editTextUsername);
        editPassword = findViewById(R.id.editPassword);
        editAge = findViewById(R.id.editAge);
        rgGender = findViewById(R.id.rgGender);
        rgWorkType = findViewById(R.id.rgWorkoutType);
        rgBodyType = findViewById(R.id.editBodyWeight);
        editDuration = findViewById(R.id.editDuration);
        rgRes = findViewById(R.id.rgRes);
        saveUser = findViewById(R.id.saveUser);

        // Hinzufügen der OnCLickListener zu den Buttons.
        imgButtonLogo.setOnClickListener(this);
        saveUser.setOnClickListener(this);

        //Aktuelle Daten des Benutzers werden visualisiert.
        setUserSettings();
    }

    // Diese Methode übernimmt die neuen Einstellungen des Benutzers.
    public void applyUserChanges() {

        int rbId;
        RadioButton rb;
        user.setUsername(editName.getText().toString());
        editName.getText().clear();
        user.setUserPassword(editPassword.getText().toString());
        editPassword.getText().clear();
        user.setAge(editAge.getText().toString());
        editAge.getText().clear();
        rbId = rgGender.getCheckedRadioButtonId();
        rb = findViewById(rbId);
        user.setGender(rb.getText().toString());
        rgGender.clearCheck();
        rbId = rgWorkType.getCheckedRadioButtonId();
        rb = findViewById(rbId);
        user.setWorktype(rb.getText().toString());
        rgWorkType.clearCheck();
        rbId = rgBodyType.getCheckedRadioButtonId();
        rb = findViewById(rbId);
        user.setBodyType(rb.getText().toString());
        rgBodyType.clearCheck();
        user.setDuration(editDuration.getText().toString());
        editDuration.getText().clear();
        rbId = rgRes.getCheckedRadioButtonId();
        rb = findViewById(rbId);
        user.setRes(rb.getText().toString());
        user.setPathData(user.getUsername() + ".txt");
        rgRes.clearCheck();

        MainActivity.userLogged.setUsername(user.getUsername());
        MainActivity.userLogged.setUserPassword(user.getUserPassword());
        MainActivity.userLogged.setAge(user.getAge());
        MainActivity.userLogged.setGender(user.getGender());
        MainActivity.userLogged.setWorktype(user.getWorktype());
        MainActivity.userLogged.setBodyType(user.getBodyType());
        MainActivity.userLogged.setDuration(user.getDuration());
        MainActivity.userLogged.setRes(user.getRes());
        MainActivity.userLogged.setPathData(user.getPathData());
    }

    // Diese Methode löscht die alten Benutzereinstellungen und speichert die neuen anschließend ab.
    public void finishAct() {

        userList.removeUser(MainActivity.userLogged.getUsername());
        cbrFitnessUtil.save("userBase.txt", MainActivity.userList.UserListToString(), UserSettings.this);
        applyUserChanges();
        cbrFitnessUtil.save("userBase.txt", cbrFitnessUtil.load("userBase.txt", user.getUserToString(), UserSettings.this), UserSettings.this);

        Toast.makeText(this, "Changes saved!", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(UserSettings.this, UserInterface.class);
        startActivity(i);
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
        } else if(editDuration.getText().toString().matches("")) {
            check = false;
        } else if(rgGender.getCheckedRadioButtonId() == -1) {
            check = false;
        } else if(rgRes.getCheckedRadioButtonId() == -1) {
            check = false;
        } else if(rgWorkType.getCheckedRadioButtonId() == -1) {
            check = false;
        } else if(rgBodyType.getCheckedRadioButtonId() == -1) {
            check = false;
        }
        return check;
    }

    // Diese Methode visualisiert die aktuellen Einstellungen des Benutzers.
    public void setUserSettings() {

        editName.setText(MainActivity.userLogged.getUsername());
        editName.setEnabled(false);
        editPassword.setText(MainActivity.userLogged.getUserPassword());
        editAge.setText(MainActivity.userLogged.getAge());
        editDuration.setText(MainActivity.userLogged.getDuration());

        if(MainActivity.userLogged.getGender().equals("Male")) {
            rgGender.check(R.id.rbMale);
        } else if(MainActivity.userLogged.getGender().equals("Female")) {
            rgGender.check(R.id.rbFemale);
        } else {
            rgGender.check(R.id.rbDiverse);
        }

        if(MainActivity.userLogged.getWorktype().equals("Beginner")) {
            rgWorkType.check(R.id.rbBeginner);
        } else if(MainActivity.userLogged.getWorktype().equals("Advanced")) {
            rgWorkType.check(R.id.rbAdvanced);
        } else {
            rgWorkType.check(R.id.rbPro);
        }

        if(MainActivity.userLogged.getBodyType().equals("Ectomorph")) {
            rgBodyType.check(R.id.rbEctomorph);
        } else if(MainActivity.userLogged.getBodyType().equals("Mesomorph")) {
            rgBodyType.check(R.id.rbMesomorph);
        } else {
            rgBodyType.check(R.id.rbEndomorph);
        }

        if(MainActivity.userLogged.getRes().equals("Yes")) {
            rgRes.check(R.id.rbYes);
        } else {
            rgRes.check(R.id.rbNo);
        }
    }
}