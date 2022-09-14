package com.example.cbr__fitness.fragments;

import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cbr__fitness.R;
import com.example.cbr__fitness.databasehelper.FitnessDBSqliteHelper;
import com.example.cbr__fitness.viewModels.ExerciseViewModel;
import com.example.cbr__fitness.viewModels.PlanViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditExercise#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditExercise extends Fragment {

    private int oldSet;

    private int oldRep;

    private int oldBreak;

    private int oldWeight;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EditExercise() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditExercise.
     */
    // TODO: Rename and change types and number of parameters
    public static EditExercise newInstance(String param1, String param2) {
        EditExercise fragment = new EditExercise();
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
        return inflater.inflate(R.layout.fragment_edit_exercise, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FitnessDBSqliteHelper helper = new FitnessDBSqliteHelper(getActivity().getApplicationContext());

        TextView textDescription = view.findViewById(R.id.text_description_box_edit_exercise);
        EditText setEditText = view.findViewById(R.id.text_set_edit_exercise);
        EditText repEditText = view.findViewById(R.id.text_rep_edit_exercise);
        EditText breakEditText = view.findViewById(R.id.text_break_edit_exercise);
        EditText weightEditText = view.findViewById(R.id.text_weight_edit_exercise);
        TextView textPrimeMuscle = view.findViewById(R.id.text_prime_muscle_edit_exercise);
        TextView textSecondaryMuscle = view.findViewById(R.id.text_secondary_muscle_edit_exercise);
        TextView textEquipment = view.findViewById(R.id.text_equipment_edit_exercise);
        TextView textExerciseType = view.findViewById(R.id.text_exercise_type_edit_exercise);
        TextView textMovementType = view.findViewById(R.id.text_movement_type_edit_exercise);
        ImageView image = view.findViewById(R.id.image_edit_exercise);

        textDescription.setMovementMethod(new ScrollingMovementMethod());



        FloatingActionButton actionButton = view.findViewById(R.id.floating_edit_edit_exercise);
        PlanViewModel planModel = new ViewModelProvider(requireActivity()).get(PlanViewModel.class);
        System.out.println("<<<<<<<<<<<<<< PLAN ID: " + planModel.getSelected().getValue().getPlan_id());
        ExerciseViewModel model = new ViewModelProvider(requireActivity()).get(ExerciseViewModel.class);
        model.getSelected().observe(getViewLifecycleOwner(), exercise -> {
            oldSet = exercise.getSetNumber();
            setEditText.setText(Integer.toString(oldSet));
            oldRep = exercise.getRepNumber();
            repEditText.setText(Integer.toString(oldRep));
            oldBreak = exercise.getBreakTime();
            breakEditText.setText(Integer.toString(oldBreak));
            oldWeight = exercise.getWeight();
            weightEditText.setText(Integer.toString(oldWeight));
            textPrimeMuscle.setText(exercise.getMuscle().getLabel());
            textSecondaryMuscle.setText(exercise.getSecondaryMuscle().getLabel());
            textEquipment.setText(exercise.getEquipment().getLabel());
            textExerciseType.setText(exercise.getType().getLabel());
            textMovementType.setText(exercise.getMovementType().getLabel());
            textDescription.setText(exercise.getDescription());

            int id = getResources()
                    .getIdentifier(exercise.getImagePath(), "drawable", requireContext().getPackageName());
            image.setImageBitmap(BitmapFactory.decodeResource(getResources(), id));

            actionButton.setOnClickListener(v -> {
                Bundle bundle = new Bundle();
                int newSet = !setEditText.getText().toString().isEmpty() ? Integer.parseInt(setEditText.getText().toString()) : oldSet;
                int newRep = !repEditText.getText().toString().isEmpty() ? Integer.parseInt(repEditText.getText().toString()) : oldRep;
                int newBreak = !breakEditText.getText().toString().isEmpty() ? Integer.parseInt(breakEditText.getText().toString()) : oldBreak;
                int newWeight = !weightEditText.getText().toString().isEmpty() ? Integer.parseInt(weightEditText.getText().toString()): oldWeight;

                if ((oldSet != newSet) || (oldRep != newRep) || (oldBreak != newBreak) || (newWeight != oldWeight)) {
                    helper.updateExerciseData(exercise.getExerciseID(), exercise.getPlanID(), newSet, newRep, newWeight, newBreak);
                    exercise.setBreakTime(newBreak);
                    exercise.setRepNumber(newRep);
                    exercise.setWeight(newWeight);
                    exercise.setSetNumber(newSet);
                }

                bundle.putString("title", exercise.getName());
                Navigation.findNavController(v).navigate(R.id.action_fragment_edit_exercise_to_fragment_show_exercise, bundle);
            });
        });
    }
}