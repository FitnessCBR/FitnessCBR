package com.example.cbr__fitness.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import com.example.cbr__fitness.cbr.RetrievalUtil;
import com.example.cbr__fitness.data.ResultCaseUser;
import com.example.cbr__fitness.logic.CBRFitnessUtil;
import com.example.cbr__fitness.R;
import java.text.ParseException;
import java.util.ArrayList;

/**
 * @author Jobst-Julius Bartels
 */

// Diese Klasse stellt die Aktivität des CBR Planners bereit.
public class CBRPlanner extends AppCompatActivity implements View.OnClickListener {

    // Attribute der Klasse.
    private TextView txtUserWinAge;
    private TextView txtUserWinGender;
    private TextView txtUserWinDuration;
    private TextView txtUserWinWorkType;
    private TextView txtUserWinRes;
    private TextView txtViewPlannerHead;
    private TextView txtViewWinBodyType;
    private TextView textViewSliderAge;
    private TextView textViewSliderGender;
    private TextView textViewSliderDuration;
    private TextView textViewSliderWorkType;
    private TextView textViewSliderRestriction;
    private TextView textViewSliderBodyType;
    private Spinner spinnerExMuscle;
    private String[] spinnerItems;
    private SeekBar seekBarAge;
    private SeekBar seekBarGender;
    private SeekBar seekBarDuration;
    private SeekBar seekBarWorkType;
    private SeekBar seekBarBodyType;
    private SeekBar seekBarRes;
    private SeekBar.OnSeekBarChangeListener mlistener;
    private Button buttonRet;
    private ImageButton imgButtonLogo;
    private RetrievalUtil retrievalUtil;
    private CBRFitnessUtil cbrFitnessUtil;
    public static ArrayList<ResultCaseUser> resultList;
    private ArrayList<Integer> weightList;

    @Override
    // OnClick Methode der Klasse.
    public void onClick(View v) {
        switch (v.getId()) {

            // Durch Klicken des buttonRetrieval wird das Retrieval ausgeführt, die JavaLog-Datei geschriebeben und die nächste Aktivität gestartet.
            case R.id.buttonRetrieval:
                try {
                    resultList = retrievalUtil.retrieveUser(MainActivity.userLogged, spinnerExMuscle.getSelectedItem().toString(), weightList);
                    System.out.println(">>>PRE PRINT WEIGHTS");
                    printRetrieval(weightList);
                    Intent i = new Intent(CBRPlanner.this, CBRPlannerResult.class);
                    startActivity(i);
                    System.out.println(">>>END OF METHODE");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;

            // Durch Klicken des imgButtonLogos gelangt der Benutzer zurück zur vorherigen Aktivität.
            case R.id.imgButtonLogo:
                Intent i = new Intent(CBRPlanner.this, UserInterface.class);
                startActivity(i);
                break;
        }
    }

    @Override
    // Oncreate Methode der Klasse.
    protected void onCreate(Bundle savedInstanceState) {

        // Layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cbr_planner);

        // Initialisierung der Util-Klassen und anderen Attributen.
        String path = this.getFilesDir().getAbsolutePath() + "/";
        System.out.println(">>Log: Path for .prj file: " + path + " CBRPlanerAction");
        retrievalUtil = new RetrievalUtil(path);
        cbrFitnessUtil = new CBRFitnessUtil();
        weightList = new ArrayList<Integer>();

        // Layout-Attribute.
        buttonRet = findViewById(R.id.buttonRetrieval);
        imgButtonLogo = findViewById(R.id.imgButtonLogo);
        txtViewPlannerHead = findViewById(R.id.txtViewPlannerHead);
        txtUserWinAge = findViewById(R.id.txtUserWinAge);
        txtUserWinGender = findViewById(R.id.txtUserWinGender);
        txtUserWinDuration = findViewById(R.id.txtUserWinDuration);
        txtUserWinWorkType =  findViewById(R.id.txtUserWinWorktype);
        txtViewWinBodyType = findViewById(R.id.txtUserWinBodyType);
        txtUserWinRes = findViewById(R.id.txtUserWinRes);
        textViewSliderAge = findViewById(R.id.textViewSlider1);
        textViewSliderGender = findViewById(R.id.textViewSlider2);
        textViewSliderDuration = findViewById(R.id.textViewSlider3);
        textViewSliderWorkType = findViewById(R.id.textViewSlider4);
        textViewSliderBodyType = findViewById(R.id.textViewSlider5);
        textViewSliderRestriction =  findViewById(R.id.textViewSlider6);
        spinnerExMuscle = findViewById(R.id.spinnerExMuscle);
        seekBarAge = findViewById(R.id.seekBarAge);
        seekBarGender = findViewById(R.id.seekBarGender);
        seekBarDuration = findViewById(R.id.seekBarDuration);
        seekBarWorkType = findViewById(R.id.seekBarWorkType);
        seekBarBodyType = findViewById(R.id.seekBarBodyType);
        seekBarRes = findViewById(R.id.seekBarRes);

        // Initialisierung und Gewichtung der SeekBars.
        seekBarAge.setProgress(1);
        seekBarGender.setProgress(1);
        seekBarDuration.setProgress(1);
        seekBarWorkType.setProgress(1);
        seekBarBodyType.setProgress(1);
        seekBarRes.setProgress(1);

        // Initialisierung der Gewichtsliste.
        weightList.add(1);
        weightList.add(1);
        weightList.add(1);
        weightList.add(1);
        weightList.add(1);
        weightList.add(1);

        // Hinzufügen der Buttons und Spinner-Items.
        buttonRet.setOnClickListener(this);
        imgButtonLogo.setOnClickListener(this);
        spinnerItems = new String[]{"No Selection", "FullBody", "UpperBody", "LowerBody", "Chest", "Shoulders", "Back", "Arms", "Abs"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, spinnerItems);
        spinnerExMuscle.setAdapter(adapter);

        // TextWindow für die Query des Retrievals wird visualisiert.
        setTextWin();

        // OnChangeListener für die Seekbars der Gewichtung.
        mlistener = new SeekBar.OnSeekBarChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                switch (seekBar.getId()) {
                    case R.id.seekBarAge:
                        if(i == 0) {
                            textViewSliderAge.setText("Age: deactivated");
                        } else {
                            textViewSliderAge.setText("Age: " + i + "x weight");
                        }
                        weightList.set(0,i);
                        break;
                    case R.id.seekBarGender:
                        if(i == 0) {
                            textViewSliderGender.setText("Gender: deactivated");
                        } else {
                            textViewSliderGender.setText("Gender: " + i + "x weight");
                        }
                        weightList.set(1,i);
                        break;
                    case R.id.seekBarDuration:
                        if(i == 0) {
                            textViewSliderDuration.setText("Duration: deactivated");
                        } else {
                            textViewSliderDuration.setText("Duration: " + i + "x weight");
                        }
                        weightList.set(2,i);
                        break;
                    case R.id.seekBarWorkType:
                        if(i == 0) {
                            textViewSliderWorkType.setText("Workout Type: deactivated");
                        } else {
                            textViewSliderWorkType.setText("Workout Type: " + i + "x weight");
                        }
                        weightList.set(3,i);
                        break;
                    case R.id.seekBarBodyType:
                        if(i == 0) {
                            textViewSliderBodyType.setText("Body Type: deactivated");
                        } else {
                            textViewSliderBodyType.setText("Body Type: " + i + "x weight");
                        }
                        weightList.set(4,i);
                        break;
                    case R.id.seekBarRes:
                        if(i == 0) {
                            textViewSliderRestriction.setText("Restrictions: deactivated" );
                        } else {
                            textViewSliderRestriction.setText("Restrictions: " + i + "x weight");
                        }
                        weightList.set(5,i);
                        break;
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        };

        // SeekBars werden dem Listener hinzugefügt.
        seekBarAge.setOnSeekBarChangeListener(mlistener);
        seekBarGender.setOnSeekBarChangeListener(mlistener);
        seekBarDuration.setOnSeekBarChangeListener(mlistener);
        seekBarWorkType.setOnSeekBarChangeListener(mlistener);
        seekBarBodyType.setOnSeekBarChangeListener(mlistener);
        seekBarRes.setOnSeekBarChangeListener(mlistener);

    }

    @SuppressLint("SetTextI18n")
    // Die Daten des Retrieval-Query werden für den Benutzer visualisiert.
    public void setTextWin() {

        txtViewPlannerHead.setText("Start your CBR Planner here, " + MainActivity.userLogged.getUsername());
        txtUserWinAge.setText("Age: " + MainActivity.userLogged.getAge());
        txtUserWinGender.setText("Gender: " + MainActivity.userLogged.getGender());
        txtUserWinDuration.setText("Duration: " + MainActivity.userLogged.getDuration());
        txtUserWinWorkType.setText("Workout Type: " + MainActivity.userLogged.getWorktype());
        txtViewWinBodyType.setText("Body Type: " + MainActivity.userLogged.getBodyType());
        txtUserWinRes.setText("Restrictions: " + MainActivity.userLogged.getRes());
    }

    // Diese Methode schreibt die JavaLog-Datei für das ausgeführte Retrieval.
    public void printRetrieval( ArrayList<Integer> weightList) {
        System.out.println("STARGIN NEXT STAGe");
        cbrFitnessUtil.saveRetJavaLog("Performing Retrieval On Query:", CBRPlanner.this);
        cbrFitnessUtil.saveRetJavaLog("\n", CBRPlanner.this);
        cbrFitnessUtil.saveRetJavaLog(MainActivity.userLogged.getUserToStringCBR(), CBRPlanner.this);
        cbrFitnessUtil.saveRetJavaLog("weight for attribute age is: " + weightList.get(0), CBRPlanner.this);
        cbrFitnessUtil.saveRetJavaLog("\n", CBRPlanner.this);
        cbrFitnessUtil.saveRetJavaLog("weight for attribute gender is: " + weightList.get(1), CBRPlanner.this);
        cbrFitnessUtil.saveRetJavaLog("\n", CBRPlanner.this);
        cbrFitnessUtil.saveRetJavaLog("weight for attribute duration is: " + weightList.get(2), CBRPlanner.this);
        cbrFitnessUtil.saveRetJavaLog("\n", CBRPlanner.this);
        cbrFitnessUtil.saveRetJavaLog("weight for attribute workType is: " + weightList.get(3), CBRPlanner.this);
        cbrFitnessUtil.saveRetJavaLog("\n", CBRPlanner.this);
        cbrFitnessUtil.saveRetJavaLog("weight for attribute bodyType is: " + weightList.get(4), CBRPlanner.this);
        cbrFitnessUtil.saveRetJavaLog("\n", CBRPlanner.this);
        cbrFitnessUtil.saveRetJavaLog("weight for attribute restriction is: " + weightList.get(5), CBRPlanner.this);
        cbrFitnessUtil.saveRetJavaLog("\n", CBRPlanner.this);
        cbrFitnessUtil.saveRetJavaLog("Result of Retrieval Response:", CBRPlanner.this);
        cbrFitnessUtil.saveRetJavaLog("\n", CBRPlanner.this);
        for(ResultCaseUser rc : resultList) {
            cbrFitnessUtil.saveRetJavaLog(rc.ResultToString(), CBRPlanner.this);
        }
    }
}