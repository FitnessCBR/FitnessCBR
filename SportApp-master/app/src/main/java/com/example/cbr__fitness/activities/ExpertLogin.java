package com.example.cbr__fitness.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.cbr__fitness.cbr.RetrievalUtil;
import com.example.cbr__fitness.data.Exercise;
import com.example.cbr__fitness.data.Plan;
import com.example.cbr__fitness.data.PlanList;
import com.example.cbr__fitness.data.ResultCaseUser;
import com.example.cbr__fitness.data.User;
import com.example.cbr__fitness.data.UserList;
import com.example.cbr__fitness.logic.CBRFitnessUtil;
import com.example.cbr__fitness.R;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @author Jobst-Julius Bartels
 */

// Diese Klasse stellt die Aktivität für den Expertenmodus dar.
public class ExpertLogin extends AppCompatActivity implements View.OnClickListener{

    // Attribute der Klasse.
    private ListView listViewP;
    private ListView listViewEx;
    private ArrayAdapter arrayAdapter;
    private ArrayAdapter arrayAdapter2;
    private ArrayList<String> arrayListPStrings;
    private ArrayList<String> arrayListExStrings;
    private PlanList planList;
    private UserList userList;
    private int userNum;
    private int exNum;
    private CBRFitnessUtil cbrFitnessUtil;
    private RetrievalUtil retrievalUtil;
    private ImageButton imgButtonLogo;
    private Button selectCButton;
    private Button selectCButton2;
    private TextView selectedPlanText;
    private TextView selectedExText;

    @Override
    // OnClick Methode der Klasse.
    public void onClick(View v) {
        switch (v.getId()) {

            // Durch das Klicken des selectCButton ein neuer Fall der User CB hinzugefügt.
            case R.id.selectCButton:
                try {
                    addUsertoCB(loadHashMap());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;

            // Durch das Klicken des selectCButton2 ein neuer Fall der Exercise CB hinzugefügt.
            case R.id.selectCButton2:
                try {
                    addExtoCB();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;

            // Durch das Klicken des imageButtonLogin wird der Login-Prozess gestartet.
            case R.id.imgButtonLogo:
                Intent i = new Intent(ExpertLogin.this, MainActivity.class);
                startActivity(i);
                break;
        }
    }

    @Override
    // OnCreate Methode der Klasse.
    protected void onCreate(Bundle savedInstanceState) {

        //Layout.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expert_login);

        // Initialisierung der Util-Klassen und anderen Attributen.
        String path = this.getFilesDir().getAbsolutePath() + "/";
        retrievalUtil = new RetrievalUtil(path);
        cbrFitnessUtil = new CBRFitnessUtil();
        arrayListPStrings = new ArrayList<String>();
        arrayListExStrings = new ArrayList<String>();
        planList = new PlanList();
        planList = MainActivity.planBaseList;

        //Layout-Attribute
        listViewP = findViewById(R.id.listviewP);
        listViewEx = findViewById(R.id.listviewEx);
        selectedPlanText = findViewById(R.id.selectedPlanText);
        selectedExText = findViewById(R.id.selectedExText);
        imgButtonLogo = findViewById(R.id.imgButtonLogo);
        selectCButton = findViewById(R.id.selectCButton);
        selectCButton2 = findViewById(R.id.selectCButton2);

        // OnClickListener werden den Buttons hinzugefügt.
        selectCButton.setOnClickListener(this);
        selectCButton2.setOnClickListener(this);
        imgButtonLogo.setOnClickListener(this);

        // Pläne der User werden geladen.
        loadUserPlans();

        // Die ViewList für die neuen User Fälle wird bestimmt.
        setViewListP(loadHashMap());

        // Die ViewList für die neuen Exercise Fälle wird bestimmt.
        setViewListEx();

        // ListView werden gesetzt.
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayListPStrings);
        listViewP.setAdapter(arrayAdapter);
        arrayAdapter2 = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayListExStrings);
        listViewEx.setAdapter(arrayAdapter2);

        // OnItemClickListener für die User ListView
        listViewP.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                userNum = i;
                // Auswahl wird visualisiert.
                selectedPlanText.setText(arrayListPStrings.get(i));
            }
        });
        // OnItemClickListener für die User ListView
        listViewEx.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                exNum = i;
                // Auswahl wird visualisiert.
                selectedExText.setText(arrayListExStrings.get(i));
            }
        });

    }
    // Diese Methode bestimmt die ViewList für die User durch eine HashMap.
    public void setViewListP(HashMap<String, String> hMap) {

        Set set = hMap.entrySet();
        Iterator iterator = set.iterator();
        while(iterator.hasNext()) {
            Map.Entry mentry = (Map.Entry)iterator.next();
            arrayListPStrings.add(mentry.getKey().toString());
        }
    }
    // Diese Methode bestimmt die ViewList für die Exercises.
    public void setViewListEx() {

        for(int i = retrievalUtil.getExCount(); i < MainActivity.exerciseBaseList.size(); i++) {
            arrayListExStrings.add(MainActivity.exerciseBaseList.get(i).getExName());
        }
    }

    // Diese Methode erstellt eine HashMap, welche für die Zuordnung der Pläne zu den User dient.
    public HashMap<String, String> loadHashMap() {

        HashMap<String, String> hMap = new HashMap<String, String>();
        for(int i = 0; i < MainActivity.userList.size(); i++) {
            if(MainActivity.userList.get(i).getPlanList().isEmpty()) {
                System.out.println("Liste ist leer!");
            } else {
                for(int j = 0; j < MainActivity.userList.get(i).getPlanList().size(); j++) {
                    if(MainActivity.userList.get(i).getPlanList().get(j).getpName().contains("_revised")) {
                        hMap.put(MainActivity.userList.get(i).getPlanList().get(j).getpName(), MainActivity.userList.get(i).getUsername());
                    }
                }
            }
        }
        return hMap;
    }
    // Diese Methode läd aus dem Datensatz der User die jeweiligen Pläne des einzelnen User, welche in einem seperaten Datensatz gespeichert sind.
    public void loadUserPlans() {

        for(int i = 0; i < MainActivity.userList.size(); i++) {
            MainActivity.userList.get(i).setPlanList(cbrFitnessUtil.getUserPList(cbrFitnessUtil.loadAll(MainActivity.userList.get(i).getPathData(), ExpertLogin.this)));
        }
    }
    // Diese Methode fügt den ausgewählten Fall der User CB hinzu.
    public void addUsertoCB(HashMap<String, String> hMap) throws ParseException {

        ResultCaseUser rcUser = new ResultCaseUser();
        User user = new User();
        Plan plan = new Plan();

        // Der User ( Fallbeschreibung) wird bestimmt.
        user = MainActivity.userList.getCertainUser(hMap.get(arrayListPStrings.get(userNum)));

        // Attribute des Users werden dem Fall hinzugefügt.
        rcUser.setAge(user.getAgeEnum().toString());
        rcUser.setDuration(user.getDurationEnum().toString());
        rcUser.setGender(user.getGender());
        rcUser.setWorkType(user.getWorktype());
        rcUser.setBodyType(user.getBodyType());
        rcUser.setRes(user.getRes());

        // Der entsprechende Plan für den User wird bestimmt.
        plan = user.getPlanList().getCertainPlan(arrayListPStrings.get(userNum));

        // (Prototyp): Der Name des Plans wird mit retain ergänzt, damit eine spätere Zuordnung erleichtert wird
        // ToDo Entfernung dieser Ergänzung nach Prototyp-Phase.
        user.getPlanList().getCertainPlan(arrayListPStrings.get(userNum)).setpName(plan.getpName().replace("revised", "retain"));

        // Der Plan ( Problemlösung) wird dem Fall hinzugefügt.
        rcUser.setPlan(plan);

        // Falls der Plan in dieser Form in dem System noch nicht existiert, wird er hinzugefügt.
        if(MainActivity.planBaseList.pExists(arrayListPStrings.get(userNum))) {
            Toast.makeText(this, "Plan already in planBaseList", Toast.LENGTH_SHORT).show();
        } else {

            // Neuer Plan wird gespeichert.
            cbrFitnessUtil.save("planBase.txt", cbrFitnessUtil.load("planBase.txt", plan.pToString(), ExpertLogin.this), ExpertLogin.this);

            // Änderung des Plannamens erfolgt ebenfalls für den User Datensatz.
            cbrFitnessUtil.save(user.getPathData(), user.getPlanList().pListToString(), ExpertLogin.this);
        }

        // Der Fall wird der User CB hinzugefügt.
        retrievalUtil.addCaseUser(rcUser);
        Toast.makeText(this, "Case added to CB", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, ExpertLogin.class));
        overridePendingTransition(0, 0);

    }
    // Diese Methode dient zur Speicherung des Falls in die Exercise CB.
    public void addExtoCB() throws ParseException {

        Exercise exercise = new Exercise();

        // Ausgewählte Exercise wird bestimmt.
        exercise = MainActivity.exerciseBaseList.getCertainExercise(arrayListExStrings.get(exNum));

        // Fall wird der Exercise CB hinzugefügt.
        retrievalUtil.addCaseEx(exercise);
        Toast.makeText(this, "Case added to CB", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, ExpertLogin.class));
        overridePendingTransition(0, 0);
    }
}