package com.example.cbr__fitness.fragments;

import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cbr__fitness.R;
import com.example.cbr__fitness.viewModels.ExerciseViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShowExercise#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShowExercise extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ShowExercise() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ShowWorkout.
     */
    // TODO: Rename and change types and number of parameters
    public static ShowExercise newInstance(String param1, String param2) {
        ShowExercise fragment = new ShowExercise();
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

        return inflater.inflate(R.layout.fragment_show_exercise, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView setTextView = view.findViewById(R.id.text_set_show_exercise);
        TextView repTextView = view.findViewById(R.id.text_rep_show_exercise);
        TextView breakTextView = view.findViewById(R.id.text_break_show_exercise);
        TextView textWeight = view.findViewById(R.id.text_weight_show_exercise);
        TextView textDescription = view.findViewById(R.id.text_description_box_show_exercise);
        TextView textPrimeMuscle = view.findViewById(R.id.text_prime_muscle_show_exercise);
        TextView textSecondaryMuscle = view.findViewById(R.id.text_secondary_muscle_show_exercise);
        TextView textEquipment = view.findViewById(R.id.text_equipment_show_exercise);
        TextView textExerciseType = view.findViewById(R.id.text_exercise_type_show_exercise);
        TextView textMovementType = view.findViewById(R.id.text_movement_type_show_exercise);

        ImageView image = view.findViewById(R.id.image_show_exercise);

        FloatingActionButton actionButton = view.findViewById(R.id.floating_edit_show_exercise);

        Bundle bundle = getArguments();

        boolean fromResult = bundle.getBoolean("result");


        ExerciseViewModel model = new ViewModelProvider(requireActivity()).get(ExerciseViewModel.class);
        model.getSelected().observe(getViewLifecycleOwner(), item -> {
            setTextView.setText(Integer.toString(item.getSetNumber()));
            repTextView.setText(Integer.toString(item.getRepNumber()));
            breakTextView.setText(Integer.toString(item.getBreakTime()));
            textWeight.setText(Integer.toString(item.getWeight()));
            textPrimeMuscle.setText(item.getMuscle().getLabel());
            textSecondaryMuscle.setText(item.getSecondaryMuscle().getLabel());
            textEquipment.setText(item.getEquipment().getLabel());
            textExerciseType.setText(item.getType().getLabel());
            textDescription.setText(item.getDescription());
            textMovementType.setText(item.getMovementType().getLabel());
            textDescription.setMovementMethod(new ScrollingMovementMethod());

            int id = getResources()
                    .getIdentifier(item.getImagePath(), "drawable", requireContext().getPackageName());
            image.setImageBitmap(BitmapFactory.decodeResource(getResources(), id));

            actionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("title", item.getName());
                    Navigation.findNavController(v).navigate(R.id.action_fragment_show_exercise_to_fragment_edit_exercise, bundle);
                }
            });
        });
        if (fromResult) {
            actionButton.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.edit_menue, menu);
    }
}