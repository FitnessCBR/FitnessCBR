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
import com.example.cbr__fitness.adapters.ExercisesAdapter;
import com.example.cbr__fitness.customListenerMethods.ItemClickSupport;
import com.example.cbr__fitness.logic.AccountUtil;
import com.example.cbr__fitness.viewModels.ExerciseViewModel;
import com.example.cbr__fitness.viewModels.PlanViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShowExerciseList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShowExerciseList extends Fragment {

    ExerciseViewModel modelExercise;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ShowExerciseList() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ShowExerciseList.
     */
    // TODO: Rename and change types and number of parameters
    public static ShowExerciseList newInstance(String param1, String param2) {
        ShowExerciseList fragment = new ShowExerciseList();
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
        return inflater.inflate(R.layout.fragment_show_exercise_list, container, false);
    }

    /**
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        modelExercise = new ViewModelProvider(requireActivity()).get(ExerciseViewModel.class);

        Bundle originBundle = getArguments();
        boolean fromResult = originBundle.getBoolean("result");

        TextView durationTextView = view.findViewById(R.id.text_duration_show_exercise_list);
        TextView primeMuscle = view.findViewById(R.id.text_prime_muscle_show_exercise_list);
        TextView goalShowExercise = view.findViewById(R.id.text_goal_show_exercises_list);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_show_exercises_list);
        FloatingActionButton actionButton = view.findViewById(R.id.floating_edit_button_show_exercise_list);


        PlanViewModel model = new ViewModelProvider(requireActivity()).get(PlanViewModel.class);
        model.getSelected().observe(getViewLifecycleOwner(), list -> {
            durationTextView.setText(AccountUtil.getDurationAsTime(list.getDuration()));
            goalShowExercise.setText((list.getGoal()).getLabel());
            primeMuscle.setText(list.getMuscle_group().getLabel());
            ExercisesAdapter adapter = new ExercisesAdapter(list.getExercises());
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

            ItemClickSupport.addTo(recyclerView).setOnItemClickListener((recyclerView1, position, v) -> {
                modelExercise.addExercise(list.getExercises().get(position));
                Bundle bundle = new Bundle();
                bundle.putString("title",list.getExercises().get(position).getName());
                bundle.putBoolean("result", fromResult);
                Navigation.findNavController(v).navigate(R.id.action_fragment_show_exercise_list_to_fragment_show_exercise, bundle);
            });

            actionButton.setOnClickListener(v -> {
                Bundle bundle = new Bundle();
                bundle.putString("title", list.getPlan_name());
                Navigation.findNavController(v).navigate(R.id.action_show_exercises_fragments_to_editExerciseList, bundle);
            });
        });
        if (fromResult) {
            actionButton.setVisibility(View.INVISIBLE);
        }
    }
}