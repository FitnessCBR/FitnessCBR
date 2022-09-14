package com.example.cbr__fitness.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
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
import com.example.cbr__fitness.logic.CBRFitnessUtil;
import com.example.cbr__fitness.R;
import java.util.ArrayList;

/**
 * @author Jobst-Julius Bartels
 */

// Diese Klasse stellt die Aktivität für die Ansicht des Retrieval-Ergebnisses bereit.
public class CBRPlannerResult extends AppCompatActivity implements View.OnClickListener {

    // Attribute der Klasse.
    private ListView listViewP;
    private ArrayAdapter arrayAdapter;
    private ArrayList<String> arrayListPStrings;
    private int planNum;
    private CBRFitnessUtil cbrFitnessUtil;
    private TextView textResultHead;
    private TextView txtCaseWinPName;
    private TextView txtCaseWinPSim;
    private TextView txtCaseWinPTime;
    private TextView txtCaseWinPEx;
    private TextView txtCaseWinPRating;
    private TextView txtCaseWinPMusclePrio;
    private ImageButton imgButtonLogo;
    private Button selectCButton;

    @Override
    // OnClick Methode der Klasse.
    public void onClick(View v) {
        switch (v.getId()) {

            // Durch Klicken des SelectCButtons wird der ausgewählte Plan dem Benutzerprofil hinzugefügt.
            case R.id.selectCButton:
                if(planNum == -1) {
                    Toast.makeText(this, "Please select a Plan", Toast.LENGTH_SHORT).show();
                } else {
                    if(MainActivity.userLogged.getPlanList().pExists(CBRPlanner.resultList.get(planNum).getPlan().getpName())) {
                        Toast.makeText(this, "Plan already assigned!", Toast.LENGTH_SHORT).show();
                    } else {
                        finishWindow();
                    }
                }
                break;

            // Durch Klicken des imgButtonLogos gelangt der Benutzer zurück zur vorherigen Aktivität.
            case R.id.imgButtonLogo:
                Intent i = new Intent(CBRPlannerResult.this, CBRPlanner.class);
                startActivity(i);
                break;
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    // Oncreate Methode der Klasse.
    protected void onCreate(Bundle savedInstanceState) {

        // Layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cbr_planner_result);

        // Initialisierung der Util-Klasse und anderen Attributen.
        cbrFitnessUtil = new CBRFitnessUtil();
        arrayListPStrings = new ArrayList<String>();
        planNum = -1;

        // Layout-Attribute.
        textResultHead = findViewById(R.id.textResultHead);
        txtCaseWinPName = findViewById(R.id.txtCaseWinPName);
        txtCaseWinPSim = findViewById(R.id.txtCaseWinSim);
        txtCaseWinPTime = findViewById(R.id.txtCaseWinPTime);
        txtCaseWinPEx = findViewById(R.id.txtCaseWinPEx);
        txtCaseWinPRating = findViewById(R.id.txtCaseWinPRating);
        txtCaseWinPMusclePrio = findViewById(R.id.txtCaseWinPMusclePrio);
        listViewP = findViewById(R.id.listviewP);
        selectCButton = findViewById(R.id.selectCButton);
        imgButtonLogo = findViewById(R.id.imgButtonLogo);

        // Hinzufügen der Button zu den Listener.
        imgButtonLogo.setOnClickListener(this);
        selectCButton.setOnClickListener(this);

        // Überschrift wird bestimmt.
        textResultHead.setText("Your Result " + MainActivity.userLogged.getUsername());

        // Bereitstellung einer ListView für das Ergebnis des Retrievals.
        setViewList();
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayListPStrings);
        listViewP.setAdapter(arrayAdapter);

        // OnItemClickListener der ListView: Durch das Klicken des Items, werden die entsprechenden Daten visualisiert.
        listViewP.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                planNum = i;
                txtCaseWinPName.setText("Name: " + CBRPlanner.resultList.get(i).getPlan().getpName());
                txtCaseWinPSim.setText("Similarity: " + CBRPlanner.resultList.get(i).getSim());
                txtCaseWinPTime.setText("Time: " + CBRPlanner.resultList.get(i).getPlan().getpTime());
                txtCaseWinPEx.setText("Exercises: " + CBRPlanner.resultList.get(i).getPlan().getpEx());
                txtCaseWinPRating.setText("Rating: " + CBRPlanner.resultList.get(i).getPlan().getpRating());
                txtCaseWinPMusclePrio.setText("Muscle Priority: " + CBRPlanner.resultList.get(i).getPlan().getMusclePrio());
            }
        });
    }

    // Methode zur Übergabe der erhaltenen Pläne des Retrievals.
    public void setViewList() {

        for (int i = 0; i < CBRPlanner.resultList.size(); i++) {
            arrayListPStrings.add(CBRPlanner.resultList.get(i).getPlan().getpName());
        }
    }

    // Methode zur Abspeicherung des neuen Plans und Starten der nächsten Aktivität.
    public void finishWindow() {

        cbrFitnessUtil.save(MainActivity.userLogged.getPathData(), cbrFitnessUtil.load(MainActivity.userLogged.getPathData(),CBRPlanner.resultList.get(planNum).getPlan().pToString() ,CBRPlannerResult.this), CBRPlannerResult.this);
        MainActivity.userLogged.getPlanList().add(CBRPlanner.resultList.get(planNum).getPlan());
        Toast.makeText(this, "Plan added!", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(CBRPlannerResult.this, UserInterface.class);
        startActivity(i);
    }

}