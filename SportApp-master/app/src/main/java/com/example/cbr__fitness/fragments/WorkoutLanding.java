package com.example.cbr__fitness.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.cbr__fitness.R;
import com.example.cbr__fitness.adapters.ExerciseListAdapter;
import com.example.cbr__fitness.customListenerMethods.ItemClickSupport;
import com.example.cbr__fitness.data.ExerciseList;
import com.example.cbr__fitness.databasehelper.FitnessDBSqliteHelper;
import com.example.cbr__fitness.logic.SharedPreferenceManager;
import com.example.cbr__fitness.viewModels.ExerciseListListViewModel;
import com.example.cbr__fitness.viewModels.PlanViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WorkoutLanding#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WorkoutLanding extends Fragment {

    private boolean first = true;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private PlanViewModel model;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    List<ExerciseList> exerciseLists;



    public WorkoutLanding() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment workout_landing.
     */
    // TODO: Rename and change types and number of parameters
    public static WorkoutLanding newInstance(String param1, String param2) {
        WorkoutLanding fragment = new WorkoutLanding();
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
        View view = inflater.inflate(R.layout.fragment_workout_landing, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FitnessDBSqliteHelper helper = new FitnessDBSqliteHelper(getActivity().getApplicationContext());
        model = new ViewModelProvider(requireActivity()).get(PlanViewModel.class);
        Button generateButton = view.findViewById(R.id.button_generate_workout_landing);
        Button createExercise = view.findViewById(R.id.button_create_workout_landing);
        FloatingActionButton floating = view.findViewById(R.id.floating_edit_button_workout);

        if (first) {
            exerciseLists =  helper.getExerciseListsByUser(SharedPreferenceManager.getLoggedUserID(getActivity().getApplicationContext()));
            first = false;
        }

        generateButton.setOnClickListener(v ->
                Navigation.findNavController(v)
                        .navigate(R.id.action_fragment_workout_landing_to_fragment_generate_exercise_list));

        RecyclerView exerciseListView = view.findViewById(R.id.recycler_workout_landing);

        ExerciseListAdapter adapter = new ExerciseListAdapter(exerciseLists, requireContext());
        exerciseListView.setAdapter(adapter);
        exerciseListView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        ItemClickSupport.addTo(exerciseListView).setOnItemClickListener((recyclerView, position, v) -> {
            model.addPlanList(exerciseLists.get(position));
            Bundle bundle = new Bundle();
            bundle.putString("title",exerciseLists.get(position).getPlan_name());
            bundle.putBoolean("result", false);
            Navigation.findNavController(v).navigate(R.id.action_fragment_workout_landing_to_fragment_show_exercise_list_fragment, bundle);
        });
        floating.setOnClickListener(v -> {
            ExerciseListListViewModel allExerciseLists = new ViewModelProvider(requireActivity()).get(ExerciseListListViewModel.class);
            allExerciseLists.addExercise(exerciseLists);
            Navigation.findNavController(v).navigate(R.id.action_fragment_workout_landing_to_fragment_delete_workouts);
        });
        createExercise.setOnClickListener(
                v -> Navigation.findNavController(v).navigate(R.id.action_fragment_workout_landing_to_fragment_create_exercises));
    }
}