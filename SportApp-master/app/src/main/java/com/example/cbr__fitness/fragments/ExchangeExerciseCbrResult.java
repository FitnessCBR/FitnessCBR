package com.example.cbr__fitness.fragments;

import android.database.sqlite.SQLiteDatabase;
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
import android.widget.Button;

import com.example.cbr__fitness.R;
import com.example.cbr__fitness.adapters.CBRExerciseExchangeResultAdapter;
import com.example.cbr__fitness.adapters.ExerciseListAdapter;
import com.example.cbr__fitness.customListenerMethods.ItemClickSupport;
import com.example.cbr__fitness.data.Exercise;
import com.example.cbr__fitness.data.ExerciseList;
import com.example.cbr__fitness.databasehelper.FitnessDBSqliteHelper;
import com.example.cbr__fitness.logic.SharedPreferenceManager;
import com.example.cbr__fitness.viewModels.ExerciseListViewModel;
import com.example.cbr__fitness.viewModels.ExerciseViewModel;
import com.example.cbr__fitness.viewModels.PlanViewModel;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExchangeExerciseCbrResult#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExchangeExerciseCbrResult extends Fragment {

    private ExerciseListViewModel exerciseListViewModel;

    private FitnessDBSqliteHelper helper;

    private  PlanViewModel planViewModel;

    private ExerciseViewModel exerciseViewModel;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ExchangeExerciseCbrResult() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ExchangeExerciseCbrResult.
     */
    // TODO: Rename and change types and number of parameters
    public static ExchangeExerciseCbrResult newInstance(String param1, String param2) {
        ExchangeExerciseCbrResult fragment = new ExchangeExerciseCbrResult();
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
        return inflater.inflate(R.layout.fragment_exchange_exercise_cbr_result, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        helper = new FitnessDBSqliteHelper(requireActivity());

        Button confirmExchangeButton = view.findViewById(R.id.button_confirm_exchange_result);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_cbr_result_exchange_exercise);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));

        ExerciseViewModel exerciseModel = new ViewModelProvider(requireActivity()).get(ExerciseViewModel.class);
        Exercise oldExercise = exerciseModel.getSelected().getValue();

        planViewModel = new ViewModelProvider(requireActivity()).get(PlanViewModel.class);
        ExerciseList currentPlan = planViewModel.getSelected().getValue();


        exerciseListViewModel = new ViewModelProvider(requireActivity()).get(ExerciseListViewModel.class);
        exerciseListViewModel.getSelected().observe(getViewLifecycleOwner(), list ->{
            CBRExerciseExchangeResultAdapter adapter = new CBRExerciseExchangeResultAdapter(list);
            recyclerView.setAdapter(adapter);

            confirmExchangeButton.setOnClickListener(v -> {
                Exercise newExercise = adapter.getChosenExercise();
               //updateDBPlanWithNewExercise(currentPlan.getPlan_id(), newExercise.getExerciseID(), );
                updateDBPlanWithNewExercise(planViewModel.getSelected().getValue().getPlan_id()
                        ,newExercise, oldExercise);
                Bundle bundle = new Bundle();
                bundle.putString("title" ,currentPlan.getPlan_name());
                Navigation.findNavController(v).navigate(R.id.action_fragment_exchange_exercise_cbr_result_to_fragment_edit_exercise_list, bundle);
            });

            ItemClickSupport.addTo(recyclerView).setOnItemClickListener((recyclerView1, position, v) -> {
                exerciseModel.addExercise(list.get(position).getFirst());
                Bundle bundleList = new Bundle();
                bundleList.putString("title",list.get(position).getFirst().getName());
                bundleList.putBoolean("result", true);
                Navigation.findNavController(view).navigate(R.id.action_fragment_exchange_exercise_cbr_result_to_fragment_show_exercise, bundleList);
            });
        });
    }

    /**
     * Updates the DataBase Relation with a new plan.
     * @param newExercise
     */
    private void updateDBPlanWithNewExercise(int pid, Exercise newExercise, Exercise oldExercise) {
        helper.updatePlanExerciseRelation(pid, newExercise, oldExercise);
        int userId = SharedPreferenceManager.getLoggedUserID(requireActivity());
        ExerciseList exerciseListList = helper.getExerciseListByID(pid, userId,oldExercise.getExerciseID());
        planViewModel.addPlanList(exerciseListList);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        helper.close();
    }
}