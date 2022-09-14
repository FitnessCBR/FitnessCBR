package com.example.cbr__fitness.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cbr__fitness.R;
import com.example.cbr__fitness.adapters.ExchangeExercisesAdapter;
import com.example.cbr__fitness.customListenerMethods.ItemClickSupport;
import com.example.cbr__fitness.logic.AccountUtil;
import com.example.cbr__fitness.viewModels.ExerciseViewModel;
import com.example.cbr__fitness.viewModels.PlanViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditExerciseList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditExerciseList extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EditExerciseList() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditExerciseList.
     */
    // TODO: Rename and change types and number of parameters
    public static EditExerciseList newInstance(String param1, String param2) {
        EditExerciseList fragment = new EditExerciseList();
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
        return inflater.inflate(R.layout.fragment_edit_exercise_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView durationTextView = view.findViewById(R.id.text_duration_edit_exercise_list);
        TextView goalShowExercise = view.findViewById(R.id.text_goal_edit_exercise_list);
        TextView muscleShowExercise = view.findViewById(R.id.text_muscle_group_edit_exercises_list);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_edit_exercises_list);

        ExerciseViewModel modelExercise = new ViewModelProvider(getActivity()).get(ExerciseViewModel.class);

        PlanViewModel model = new ViewModelProvider(requireActivity()).get(PlanViewModel.class);
        model.getSelected().observe(getViewLifecycleOwner(), list -> {
            durationTextView.setText((AccountUtil.getDurationAsTime(list.getDuration())));
            goalShowExercise.setText(list.getGoal().getLabel());
            muscleShowExercise.setText(list.getMuscle_group().getLabel());
            ExchangeExercisesAdapter adapter = new ExchangeExercisesAdapter(list.getExercises(), requireActivity());
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

            ItemClickSupport.addTo(recyclerView).setOnItemClickListener((recyclerView1, position, v) -> {
                modelExercise.addExercise(list.getExercises().get(position));
                Bundle bundle = new Bundle();
                bundle.putString("title",list.getExercises().get(position).getName());
                bundle.putBoolean("result", true);
                Navigation.findNavController(v).navigate(R.id.action_fragment_edit_exercise_list_to_fragment_show_exercise, bundle);
            });
        });
    }
}