package com.example.cbr__fitness.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.helper.widget.Flow;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;

import com.example.cbr__fitness.R;
import com.example.cbr__fitness.cbr.CBRConstants;
import com.example.cbr__fitness.cbr.RetrievalUtil;
import com.example.cbr__fitness.customListenerMethods.ColorChangeToggleListener;
import com.example.cbr__fitness.customViewElements.ColorChangeToggleButton;
import com.example.cbr__fitness.data.Exercise;
import com.example.cbr__fitness.data.ExerciseList;
import com.example.cbr__fitness.data.Plan;
import com.example.cbr__fitness.data.User;
import com.example.cbr__fitness.databasehelper.FitnessDBSqliteHelper;
import com.example.cbr__fitness.enums.EquipmentEnum;
import com.example.cbr__fitness.enums.GoalEnum;
import com.example.cbr__fitness.logic.LogUtil;
import com.example.cbr__fitness.logic.SharedPreferenceManager;
import com.example.cbr__fitness.viewModels.ExerciseListViewModel;
import com.example.cbr__fitness.viewModels.ExerciseViewModel;
import com.example.cbr__fitness.viewModels.PlanViewModel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.dfki.mycbr.util.Pair;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExchangeExercise#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExchangeExercise extends Fragment {

    private List<ColorChangeToggleButton> equipmentButtons;

    private FitnessDBSqliteHelper helper;

    private ExerciseViewModel model;

    private RetrievalUtil util;

    private User user;

    private ExerciseList exerciseList;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ExchangeExercise() {
        equipmentButtons = new ArrayList<>();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ExchangeExercise.
     */
    // TODO: Rename and change types and number of parameters
    public static ExchangeExercise newInstance(String param1, String param2) {
        ExchangeExercise fragment = new ExchangeExercise();
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
        return inflater.inflate(R.layout.fragment_exchange_exercise, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>GOT GESTEROASDF");
        util = new RetrievalUtil(this.getActivity().getFilesDir().getAbsolutePath() + "/");
        helper = new FitnessDBSqliteHelper(requireContext());

        int id = SharedPreferenceManager.getLoggedUserID(requireContext());
        user = helper.getUserById(id);

        List<EquipmentEnum> equipments = new ArrayList<>();

        exerciseList = new ViewModelProvider(requireActivity()).get(PlanViewModel.class).getSelected().getValue();


        model = new ViewModelProvider(requireActivity()).get(ExerciseViewModel.class);

        Flow flowLayout = view.findViewById(R.id.flow_choose_equipment_exchange_exercise);
        ConstraintLayout constraintLayout = view.findViewById(R.id.fragment_exchange_exercise);

        for (EquipmentEnum e : user.getEquipments()) {
            ColorChangeToggleButton button = new ColorChangeToggleButton(requireContext(), e);
            button.setOnCheckedChangeListener((buttonView, isChecked) ->
                    ColorChangeToggleListener.onCheckedChanged(buttonView, isChecked));
            button.setChecked(true);
            equipmentButtons.add(button);
            constraintLayout.addView(button);
            flowLayout.addView(button);
        }

        Button confirmButton = view.findViewById(R.id.button_confirm_exchange);
        confirmButton.setOnClickListener(v -> {
            for (ColorChangeToggleButton c : equipmentButtons) {
                if (c.isChecked()) {
                    equipments.add((EquipmentEnum) c.getEnumE());
                }
            }
            List<Pair<Exercise, Double>> retrievedExercises = util.retrieveExercise(helper
                    , model.getSelected().getValue() , equipments, exerciseList.getAllExerciseIDs());

            retrievedExercises.removeIf(p -> !(p.getFirst().getMuscle() == model.getSelected().getValue().getMuscle()));

            List<Pair<User, Double>> users = CBRConstants.getUserFromCBR(user,util,helper);
            List<Pair<ExerciseList, Double>> exerciseListsUsers = helper.getPlansForCBRUsers(users);
            //gets all plans with the same goal as the one to be adapted
            exerciseListsUsers.removeIf(p -> p.getFirst().getGoal() != exerciseList.getGoal());

            for (Pair<Exercise, Double> p : retrievedExercises) {
                adaptForSimilarExercise(p.getFirst(), exerciseListsUsers);
            }

            ExerciseListViewModel viewModel = new ViewModelProvider(requireActivity()).get(ExerciseListViewModel.class);
            viewModel.addExercise(retrievedExercises);
            helper.close();
            Navigation.findNavController(view).navigate(R.id.action_fragment_exchange_exercise_to_fragment_exchange_exercise_cbr_result);
        });
    }

    private void adaptForSimilarExercise(Exercise exercise, List<Pair<ExerciseList, Double>> lists) {
        List<Exercise> similarExercises = new ArrayList<>();
        for (Pair<ExerciseList, Double> p : lists) {
            for (Exercise e : p.getFirst().getExercises()) {
                if (e.getMuscle() == exercise.getMuscle()
                        && e.isBodyweight() == exercise.isBodyweight()) { //Exercise targeting same muscle with same goal
                    LogUtil.LogPlanSimilarity(requireContext(), "\t\t\t>Looking for similar to Create: "
                            + exercise.getName() + " Bodyweight: "+ exercise.isBodyweight()
                            +" Muscle: " + exercise.getMuscle().getLabel() + " FOUND: "
                            + e.getName() + " Bodyweigt : " + e.isBodyweight() + " MUSCLE: "
                            + e.getMuscle().getLabel() + " FROM Plan: " + p.getFirst().getPlan_id());
                    similarExercises.add(e);
                }
            }
        }
        CBRConstants.adaptExerciseAfter(similarExercises,exercise,requireContext()
                , CBRConstants.initExerciseValues(exerciseList.getGoal(), user.getWorkoutTypeN()));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        helper.close();
    }
}