package com.example.cbr__fitness.fragments;

import android.media.audiofx.DynamicsProcessing;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.helper.widget.Flow;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.cbr__fitness.R;
import com.example.cbr__fitness.customViewElements.ColorChangeToggleButton;
import com.example.cbr__fitness.data.User;
import com.example.cbr__fitness.databasehelper.FitnessDBSqliteHelper;
import com.example.cbr__fitness.enums.EquipmentEnum;
import com.example.cbr__fitness.enums.GenderEnum;
import com.example.cbr__fitness.enums.LimitationEnum;
import com.example.cbr__fitness.enums.WorkoutEnum;
import com.example.cbr__fitness.logic.AccountUtil;
import com.example.cbr__fitness.logic.SharedPreferenceManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditProfile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditProfile extends Fragment {

    private User user;

    private List<ColorChangeToggleButton> equipmentButtons;

    private List<ColorChangeToggleButton> limitationButtons;

    private Spinner genderSpinner;

    private ArrayAdapter<CharSequence> genderAdapter;

    private Spinner workoutSpinner;

    private ArrayAdapter<CharSequence> workoutAdapter;

    private DecimalFormat format;

    private EditText weightText;

    private EditText heightText;

    private int oldAge;

    private int oldWeight;

    private int oldHeight;

    private ConstraintLayout constraintLayout;

    private Flow limitationsFlow;

    private Flow equipmentFlow;

    private TextView bmiText;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EditProfile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment edit_profile.
     */
    // TODO: Rename and change types and number of parameters
    public static EditProfile newInstance(String param1, String param2) {
        EditProfile fragment = new EditProfile();
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
        return inflater.inflate(R.layout.fragment_edit_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        FitnessDBSqliteHelper helper = new FitnessDBSqliteHelper(requireContext());
        format = new DecimalFormat("#.##");

        user = helper.getUserById(SharedPreferenceManager.getLoggedUserID(requireContext()));
        constraintLayout = view.findViewById(R.id.constraint_content_edit_profile);
        limitationsFlow = view.findViewById(R.id.flow_physical_restrictions_edit_profile);
        equipmentFlow = view.findViewById(R.id.flow_equipment_edit_profile);

        TextView userText = view.findViewById(R.id.text_label_name_edit_profile);
        userText.setText(user.getUserName());

        weightText = view.findViewById(R.id.text_weight_edit_profile);
        oldWeight = user.getWeightN();
        weightText.setText(Integer.toString(oldWeight));
        weightText.setOnFocusChangeListener(new TextChangeListener());

        heightText = view.findViewById(R.id.text_height_edit_profile);
        oldHeight = user.getHeight();
        heightText.setText(Integer.toString(oldHeight));
        heightText.setOnFocusChangeListener(new TextChangeListener());

        bmiText = view.findViewById(R.id.text_BMI_edit_profile);
        bmiText.setText(format.format(user.getBMI()));

        EditText ageText = view.findViewById(R.id.text_age_edit_profile);
        oldAge = user.getAgeN();
        ageText.setText(Integer.toString(oldAge));

        setUpSpinner(view);
        genderSpinner.setSelection(genderAdapter.getPosition(user.getGenderN().getLabel()));
        workoutSpinner.setSelection(workoutAdapter.getPosition(user.getWorkoutTypeN().getLabel()));

        FloatingActionButton button = view.findViewById(R.id.floating_edit_profile);
        button.setOnClickListener(v -> {
            List<Integer> removedLimitations = new ArrayList<>();
            List<Integer> addedLimitations = new ArrayList<>();
            List<Integer> removedEquipment = new ArrayList<>();
            List<Integer> addedEquipment = new ArrayList<>();
            determineEquipmentLimitationsChange(addedLimitations, removedLimitations, addedEquipment
                , removedEquipment);

            int age = !ageText.getText().toString().isEmpty() ? Integer.parseInt(ageText.getText().toString()) : oldAge;
            int weight = !weightText.getText().toString().isEmpty() ? Integer.parseInt(weightText.getText().toString()) : oldWeight;
            int height = !heightText.getText().toString().isEmpty() ? Integer.parseInt(heightText.getText().toString()) : oldHeight;
            int gender = GenderEnum.getEnumByString(genderSpinner.getSelectedItem().toString()).getID();
            int workoutType = WorkoutEnum.getEnumByString(workoutSpinner.getSelectedItem().toString()).getID();

            helper.updateUserProfile(user.getUid(), addedEquipment, removedEquipment, addedLimitations
                    , removedLimitations, age, weight, height,gender, workoutType);

            Navigation.findNavController(view).navigate(R.id.action_edit_profile_fragment_to_profile_landing_fragment);
        });

        equipmentButtons = new ArrayList<>();
        AccountUtil.setUpToggleButtons(user, EquipmentEnum.values(), constraintLayout, equipmentFlow
                , equipmentButtons, requireActivity(), true);
        limitationButtons = new ArrayList<>();
        AccountUtil.setUpToggleButtons(user, LimitationEnum.values(), constraintLayout
                , limitationsFlow, limitationButtons, requireActivity(), true);

        super.onViewCreated(view, savedInstanceState);
    }

    private class TextChangeListener implements View.OnFocusChangeListener {

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!(weightText.getText().toString().isEmpty() || heightText.getText().toString().isEmpty())) {
                bmiText.setText(format.format(AccountUtil.getBMI(
                        Integer.parseInt(weightText.getText().toString())
                        , Integer.parseInt(heightText.getText().toString()))));
            }
        }
    }

    private void setUpSpinner(View view) {
        genderSpinner = view.findViewById(R.id.spinner_gender_edit_profile);

        genderAdapter = ArrayAdapter.createFromResource(requireActivity(),
                R.array.gender_array, R.layout.spinner_item);
        // Specify the layout to use when the list of choices appears
        genderAdapter.setDropDownViewResource(R.layout.spinner_item);
        // Apply the adapter to the spinner
        genderSpinner.setAdapter(genderAdapter);

        workoutSpinner = view.findViewById(R.id.spinner_workout_type_edit_profile);

        workoutAdapter = ArrayAdapter.createFromResource(requireActivity(),
                R.array.workout_array, R.layout.spinner_item);
        // Specify the layout to use when the list of choices appears
        workoutAdapter.setDropDownViewResource(R.layout.spinner_item);
        // Apply the adapter to the spinner
        workoutSpinner.setAdapter(workoutAdapter);
    }

    private void determineEquipmentLimitationsChange (List<Integer> addedLimitations
            , List<Integer> removedLimitations, List<Integer> addedEquipment
            , List<Integer> removedEquipment ) {
        for (ColorChangeToggleButton c : equipmentButtons) {
            if (c.isChecked() && !user.getEquipments().contains(EquipmentEnum.getEnumById(c.getItemID()))) {
                addedEquipment.add(c.getItemID()); //adding
            } else if (!c.isChecked() && user.getEquipments().contains(EquipmentEnum.getEnumById(c.getItemID()))) {
                removedEquipment.add(c.getItemID());//removing
            }
        }
        for (ColorChangeToggleButton c : limitationButtons) {
            if (c.isChecked() && !user.getLimitations().contains(LimitationEnum.getEnumByID(c.getItemID()))) {
                addedLimitations.add(c.getItemID());//adding
            } else if (!c.isChecked() && user.getLimitations().contains(LimitationEnum.getEnumByID(c.getItemID()))) {
                removedLimitations.add(c.getItemID());//removing
            }
        }
    }
}