package com.example.cbr__fitness.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.helper.widget.Flow;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.example.cbr__fitness.R;
import com.example.cbr__fitness.customListenerMethods.ColorChangeToggleListener;
import com.example.cbr__fitness.customViewElements.ColorChangeToggleButton;
import com.example.cbr__fitness.enums.GoalEnum;
import com.example.cbr__fitness.enums.MuscleGroupEnum;
import com.example.cbr__fitness.interfaces.EnumInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GenerateExerciseList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GenerateExerciseList extends Fragment {

    private List<ColorChangeToggleButton> muscleGroupButtons;

    private List<ColorChangeToggleButton> workoutGoalButton;

    private ConstraintLayout layout;

    private Flow muscleGroupFlow;

    private Flow workoutGoalFlow;

    private Button generateButton;

    private EditText timeEditText;

    boolean enumsChosen;

    boolean textFitting;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public GenerateExerciseList() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GenerateExerciseList.
     */
    // TODO: Rename and change types and number of parameters
    public static GenerateExerciseList newInstance(String param1, String param2) {
        GenerateExerciseList fragment = new GenerateExerciseList();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_generate_exercise_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        muscleGroupButtons = new ArrayList<>();
        workoutGoalButton = new ArrayList<>();

        enumsChosen();

        layout = view.findViewById(R.id.fragment_generate_exercise_list);

        generateButton = view.findViewById(R.id.button_generate_workout_generate);

        timeEditText = view.findViewById(R.id.edit_text_target_duration_generate);
        timeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() < 2) {
                    timeEditText.setError("Der Plan sollte mindestens 10 Minuten dauern.");
                    textFitting = false;
                } else {
                    try {
                        Integer.parseInt(s.toString());
                        textFitting = true;
                    } catch (NumberFormatException ex) {
                        timeEditText.setError("Bitte nur Zahlen eingeben.");
                        textFitting = false;
                    }
                }
                buttonEnabled();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        timeEditText.setText(Integer.toString(45));

        generateButton.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt("group", getCheckedMuscleGroup().getID());
            bundle.putInt("goal", getCheckedWorkoutGoal().getID());
            bundle.putInt("duration", Integer.parseInt(timeEditText.getText().toString()));
            enumsChosen = false;
            Navigation.findNavController(view).navigate
                    (R.id.action_fragment_generate_exercise_list_to_fragment_show_cbr_result, bundle);
        });

        muscleGroupFlow = view.findViewById(R.id.flow_choose_muscle_group_generate);
        createSwitchButtonsFromEnums(MuscleGroupEnum.values(), muscleGroupFlow, muscleGroupButtons);

        workoutGoalFlow = view.findViewById(R.id.flow_choose_muscle_goal_generate);
        createSwitchButtonsFromEnums(GoalEnum.values(), workoutGoalFlow, workoutGoalButton);

        buttonEnabled();
    }

    private void createSwitchButtonsFromEnums(EnumInterface[] enums, Flow flow, List<ColorChangeToggleButton> buttonList){
        for (EnumInterface e : enums) {
            ColorChangeToggleButton t = new ColorChangeToggleButton(getActivity(), e);
            t.setId(View.generateViewId());
            t.setOnCheckedChangeListener((buttonView, isChecked) -> {
                List<ColorChangeToggleButton> buttons;
                if (muscleGroupButtons.contains(buttonView)) {
                    buttons = muscleGroupButtons;
                } else {
                    buttons = workoutGoalButton;
                }
                ColorChangeToggleListener.onCheckedChanged(buttonView,isChecked);
                ColorChangeToggleListener.onCheckedChangedSingleButtonChecked(buttonView,isChecked, buttons);
            });
            t.setOnClickListener(v -> {
                enumsChosen = enumsChosen();
                buttonEnabled();
            });
            buttonList.add(t);
            layout.addView(t);;

            flow.addView(t);
        }
    }

    private void buttonEnabled() {
        if (enumsChosen && textFitting) {
            generateButton.setEnabled(true);
        } else {
            generateButton.setEnabled(false);
        }
    }

    private boolean enumsChosen() {
        boolean group = false;
        boolean goal = false;
        for (ColorChangeToggleButton c : workoutGoalButton) {
            if (c.isChecked()) {
                goal = c.isChecked();
            }
        }
        for (ColorChangeToggleButton c : muscleGroupButtons) {
            if (c.isChecked()) {
                group = c.isChecked();
            }
        }
        return group && goal;
    }

    private MuscleGroupEnum getCheckedMuscleGroup() {
        MuscleGroupEnum mEnum = null;
        for (ColorChangeToggleButton button : muscleGroupButtons) {
            if (button.isChecked() && button.getEnumE() instanceof MuscleGroupEnum) {
                mEnum = (MuscleGroupEnum) button.getEnumE();
            }
        }
        return mEnum;
    }

    private GoalEnum getCheckedWorkoutGoal () {
        GoalEnum wEnum = null;

        for (ColorChangeToggleButton button : workoutGoalButton) {
            if (button.isChecked() && button.getEnumE() instanceof GoalEnum) {
                wEnum = (GoalEnum) button.getEnumE();
            }
        }
        return wEnum;
    }
}