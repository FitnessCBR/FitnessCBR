package com.example.cbr__fitness.fragments;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.helper.widget.Flow;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.cbr__fitness.R;
import com.example.cbr__fitness.adapters.CreateExerciseAdapter;
import com.example.cbr__fitness.cbr.CBRConstants;
import com.example.cbr__fitness.cbr.RetrievalUtil;
import com.example.cbr__fitness.customListenerMethods.ColorChangeToggleListener;
import com.example.cbr__fitness.customListenerMethods.ItemClickSupport;
import com.example.cbr__fitness.customViewElements.ColorChangeToggleButton;
import com.example.cbr__fitness.data.Exercise;
import com.example.cbr__fitness.data.ExerciseList;
import com.example.cbr__fitness.data.User;
import com.example.cbr__fitness.databasehelper.FitnessDBContract;
import com.example.cbr__fitness.databasehelper.FitnessDBSqliteHelper;
import com.example.cbr__fitness.enums.GoalEnum;
import com.example.cbr__fitness.enums.LimitationEnum;
import com.example.cbr__fitness.enums.MuscleEnum;
import com.example.cbr__fitness.enums.MuscleGroupEnum;
import com.example.cbr__fitness.interfaces.EnumInterface;
import com.example.cbr__fitness.logic.AccountUtil;
import com.example.cbr__fitness.logic.LogUtil;
import com.example.cbr__fitness.logic.SharedPreferenceManager;
import com.example.cbr__fitness.viewModels.ExerciseViewModel;

import java.util.ArrayList;
import java.util.List;

import de.dfki.mycbr.util.Pair;

/**
 * create an instance of this fragment.
 */
public class CreateExercise extends Fragment {

    private List<ColorChangeToggleButton> muscleGroupList;

    private List<ColorChangeToggleButton> goalGroupList;

    private ConstraintLayout constraintLayout;

    private FitnessDBSqliteHelper helper;

    private Flow flow;

    private Flow goalFlow;

    private List<Exercise> allExercises;

    private List<Exercise> selectedExercises;

    private CreateExerciseAdapter adapter;

    private CreateExerciseAdapter selectedAdapter;

    private EditText text;

    private User user;

    private Button createButton;

    public CreateExercise() {
        // Required empty public constructor
        muscleGroupList = new ArrayList<>();
        allExercises = new ArrayList<>();
        selectedExercises = new ArrayList<>();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_exercise, container, false);
   }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        helper = new FitnessDBSqliteHelper(requireContext());
        user = helper.getUserById(SharedPreferenceManager.getLoggedUserID(getActivity().getApplicationContext()));
        allExercises = helper.getAllPossibleExercisesForUser(1, user.getEquipments());

        ExerciseViewModel modelExercise = new ViewModelProvider(requireActivity()).get(ExerciseViewModel.class);

        constraintLayout = view.findViewById(R.id.constraint_create_exercise_constraint);
        flow = view.findViewById(R.id.flow_create_exercise_muscle_group);
        goalFlow = view.findViewById(R.id.flow_create_exercise_goal);

        text = view.findViewById(R.id.edit_text_title_create_exercise_list);
        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                createEnabled();
            }
        });
        RecyclerView recyclerViewPossible = view.findViewById(R.id.recycler_create_offered_exercises);
        RecyclerView recyclerViewSelected = view.findViewById(R.id.recycler_create_chosen_exercises);

        adapter = new CreateExerciseAdapter(allExercises, this, false);
        selectedAdapter = new CreateExerciseAdapter(selectedExercises, this, true);

        recyclerViewPossible.setAdapter(adapter);
        recyclerViewPossible.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        recyclerViewSelected.setAdapter(selectedAdapter);
        recyclerViewSelected.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        ItemClickSupport.addTo(recyclerViewPossible).setOnItemClickListener((recycler, position, v) -> {
            modelExercise.addExercise(allExercises.get(position));
            Bundle bundle = new Bundle();
            bundle.putString("title",allExercises.get(position).getName());
            bundle.putBoolean("result", true);
            Navigation.findNavController(v).navigate(R.id.action_fragment_create_exercises_to_fragment_show_exercise, bundle);

        });

        ItemClickSupport.addTo(recyclerViewSelected).setOnItemClickListener((recycler, position, v) -> {
            modelExercise.addExercise(selectedExercises.get(position));
            Bundle bundle = new Bundle();
            bundle.putString("title",selectedExercises.get(position).getName());
            bundle.putBoolean("result", true);
            Navigation.findNavController(v).navigate(R.id.action_fragment_create_exercises_to_fragment_show_exercise, bundle);
        });

        muscleGroupList = new ArrayList<>();
        createSwitchButtonsFromEnums(MuscleGroupEnum.values(), muscleGroupList, flow);
        goalGroupList = new ArrayList<>();
        createSwitchButtonsFromEnums(GoalEnum.values(), goalGroupList, goalFlow);

        createButton = view.findViewById(R.id.button_create_create_exercise_list);
        createButton.setOnClickListener(v -> {
            ExerciseList list = createExerciseList();
            adaptValues(list);
            helper.addPlanToUser(list, SharedPreferenceManager.getLoggedUserID(requireContext()));
            Navigation.findNavController(v).navigate(R.id.action_fragment_create_exercises_to_fragment_workout_landing);
        });
        createEnabled();
    }

    private void createSwitchButtonsFromEnums(EnumInterface[] enums, List<ColorChangeToggleButton> buttons
            , Flow flowlayout) {
        for (EnumInterface e : enums) {
            ColorChangeToggleButton t = new ColorChangeToggleButton(getActivity(), e);
            t.setId(View.generateViewId());
            t.setOnCheckedChangeListener((buttonView, isChecked) -> {

                ColorChangeToggleListener.onCheckedChanged(buttonView,isChecked);
                ColorChangeToggleListener.onCheckedChangedSingleButtonChecked(buttonView,isChecked, buttons);
            });
            t.setOnClickListener(v -> createEnabled());
            buttons.add(t);
            constraintLayout.addView(t);
            flowlayout.addView(t);
        }
    }

    public void gotNotified (Exercise e, int position) {
        if (selectedExercises.contains(e)) {
            System.out.println("SELECTED");
            if (position < selectedExercises.size() && position >= 0) {
                selectedExercises.remove(e);
                allExercises.add(e);
                selectedAdapter.notifyItemRemoved(position);
                adapter.notifyItemInserted(adapter.getItemCount() == 0 ? 0 : adapter.getItemCount());
            }
        } else {
            if (position < allExercises.size() && position >= 0) {
                allExercises.remove(e);
                selectedExercises.add(e);
                adapter.notifyItemRemoved(position);
                selectedAdapter.notifyItemInserted(selectedAdapter.getItemCount() == 0 ? 0 : selectedAdapter.getItemCount());
            }
        }
        createEnabled();
        System.out.println("got notified" + e.getName());
    }

    private ExerciseList createExerciseList() {
        int goalID = 0;
        int muscleGroupID = 0;
        for (ColorChangeToggleButton c : goalGroupList) {
            if (c.isChecked()) {
                goalID = c.getItemID();
            }
        }
        for (ColorChangeToggleButton c : muscleGroupList) {
            if (c.isChecked()) {
                muscleGroupID = c.getItemID();
            }
        }
        ExerciseList list = new ExerciseList(0, goalID, muscleGroupID, text.getText().toString());
        for (Exercise e : selectedExercises) {
            list.addExercise(e);
        }

        return list;
    }

    private void adaptValues (ExerciseList list) {
        User user = helper.getUserById(SharedPreferenceManager.getLoggedUserID(requireContext()));
        RetrievalUtil util = new RetrievalUtil(this.getActivity().getFilesDir().getAbsolutePath() + "/");
        List<Pair<User, Double>> users = CBRConstants.getUserFromCBR(user,util,helper);
        List<Pair<ExerciseList, Double>> exerciseListsUsers = helper.getPlansForCBRUsers(users);
        //gets all plans with the same goal as the one to be adapted
        List<ExerciseList> exerciseLists = getSimilarExerciseLists(list.getGoal(), exerciseListsUsers);
        for (Exercise e : list.getExercises()) {
            adaptForSimilarExercise(e, exerciseLists, list.getGoal());
        }
    }

    private List<ExerciseList> getSimilarExerciseLists (GoalEnum goal, List<Pair<ExerciseList, Double>> exerciseLists) {
        List<ExerciseList> similarExercises = new ArrayList<>();

        for (Pair<ExerciseList, Double> e : exerciseLists) {
            if (e.getFirst().getGoal() == goal) {
                similarExercises.add(e.getFirst());
            }
        }

        return similarExercises;
    }

    private void adaptForSimilarExercise(Exercise exercise, List<ExerciseList> lists, GoalEnum goal) {
        List<Exercise> similarExercises = new ArrayList<>();
        for (ExerciseList p : lists) {
            for (Exercise e : p.getExercises()) {
                if (e.getMuscle() == exercise.getMuscle()
                        && e.isBodyweight() == exercise.isBodyweight()) { //Exercise targeting same muscle with same goal
                    LogUtil.LogPlanSimilarity(requireContext(), "\t\t\t>Looking for similar to Create: "
                            + exercise.getName() + " Bodyweight: "+ exercise.isBodyweight()
                            +" Muscle: " + exercise.getMuscle().getLabel() + " FOUND: "
                            + e.getName() + " Bodyweigt : " + e.isBodyweight() + " MUSCLE: "
                            + e.getMuscle().getLabel() + " FROM Plan: " + p.getPlan_id());
                    similarExercises.add(e);
                }
            }
        }
        CBRConstants.adaptExerciseAfter(similarExercises,exercise,requireContext()
                , CBRConstants.initExerciseValues(goal, user.getWorkoutTypeN()));
    }

    private void createEnabled() {
        boolean group = false;
        boolean muscle = false;
        boolean selected = false;
        boolean name = false;
        if (!selectedExercises.isEmpty()) {
            selected = true;
        }

        if (!text.getText().toString().isEmpty()) {
            name = true;
        }
        for (ColorChangeToggleButton c : muscleGroupList) {
            if (c.isChecked()) {
                muscle = c.isChecked();
            }
        }

        for (ColorChangeToggleButton c : goalGroupList) {
            if (c.isChecked()) {
                group = c.isChecked();
            }
        }

        createButton.setEnabled(selected && muscle && group && name);
    }
}