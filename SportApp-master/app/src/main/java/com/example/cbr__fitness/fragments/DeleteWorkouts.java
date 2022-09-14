package com.example.cbr__fitness.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavInflater;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cbr__fitness.R;
import com.example.cbr__fitness.adapters.DeleteExerciseListAdapter;
import com.example.cbr__fitness.customListenerMethods.ItemClickSupport;
import com.example.cbr__fitness.databasehelper.FitnessDBSqliteHelper;
import com.example.cbr__fitness.viewModels.ExerciseListListViewModel;
import com.example.cbr__fitness.viewModels.ExerciseListViewModel;
import com.example.cbr__fitness.viewModels.PlanViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DeleteWorkouts#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DeleteWorkouts extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DeleteWorkouts() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment delete_workouts.
     */
    // TODO: Rename and change types and number of parameters
    public static DeleteWorkouts newInstance(String param1, String param2) {
        DeleteWorkouts fragment = new DeleteWorkouts();
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
        return inflater.inflate(R.layout.fragment_delete_workouts, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FitnessDBSqliteHelper helper = new FitnessDBSqliteHelper(requireContext());
        ExerciseListListViewModel model = new ViewModelProvider(requireActivity()).get(ExerciseListListViewModel.class);
        PlanViewModel exerciseListViewModel = new ViewModelProvider(requireActivity()).get(PlanViewModel.class);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_delete_workouts);

        FloatingActionButton button = view.findViewById(R.id.floating_edit_button_delete_workouts);

        model.getSelected().observe(getViewLifecycleOwner(), list -> {
            DeleteExerciseListAdapter adapter = new DeleteExerciseListAdapter(list, requireContext());
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
            button.setOnClickListener(v -> {
                System.out.println("NUMBER: " + adapter.getChosenPlans().size());
                helper.deletePlans(adapter.getChosenPlans());
                Navigation.findNavController(v).navigate(R.id.action_fragment_delete_workouts_to_fragment_workout_landing);
            });

            ItemClickSupport.addTo(recyclerView).setOnItemClickListener((recycler, position, v) -> {
                exerciseListViewModel.addPlanList(list.get(position));
                Bundle bundle = new Bundle();
                bundle.putString("title",list.get(position).getPlan_name());
                bundle.putBoolean("result", false);
                Navigation.findNavController(v).navigate(R.id.action_fragment_delete_workouts_to_fragment_show_exercise_list, bundle);
            });
        });
    }
}