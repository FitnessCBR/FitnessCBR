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
import android.widget.Button;
import android.widget.TextView;

import com.example.cbr__fitness.R;
import com.example.cbr__fitness.adapters.CBRExerciseListAdapter;
import com.example.cbr__fitness.cbr.CBRConstants;
import com.example.cbr__fitness.cbr.RetrievalUtil;
import com.example.cbr__fitness.customListenerMethods.ItemClickSupport;
import com.example.cbr__fitness.data.Exercise;
import com.example.cbr__fitness.data.ExerciseList;
import com.example.cbr__fitness.data.User;
import com.example.cbr__fitness.databasehelper.FitnessDBSqliteHelper;
import com.example.cbr__fitness.enums.EquipmentEnum;
import com.example.cbr__fitness.enums.GoalEnum;
import com.example.cbr__fitness.enums.MuscleGroupEnum;
import com.example.cbr__fitness.logic.LogUtil;
import com.example.cbr__fitness.logic.SharedPreferenceManager;
import com.example.cbr__fitness.viewModels.PlanViewModel;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

import de.dfki.mycbr.util.Pair;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CbrResult#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CbrResult extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    /**
     * Used to prevent the fragment to re-query on each creation, as the data is still valid on
     * back navigation. Will be reset to true on the destruction of this fragment.
     */
    boolean doQuery = true;

    private String mParam1;
    private String mParam2;

    private FitnessDBSqliteHelper helper;

    private PlanViewModel viewModel;

    private User user;

    private List<Pair<ExerciseList, Double>> allPlans;

    private RetrievalUtil retrievalUtil;

    public CbrResult() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CbrResult.
     */
    // TODO: Rename and change types and number of parameters
    public static CbrResult newInstance(String param1, String param2) {
        CbrResult fragment = new CbrResult();
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
        return inflater.inflate(R.layout.fragment_cbr_result, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (doQuery) {
            helper = new FitnessDBSqliteHelper(getActivity().getApplicationContext());

            int id = SharedPreferenceManager.getLoggedUserID(getActivity().getApplicationContext());
            user = helper.getUserById(id);
        }
        //Keys: goal, group, duration
        Bundle bundle = getArguments();

        viewModel = new ViewModelProvider(requireActivity()).get(PlanViewModel.class);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_show_cbr_exercise_list_results);

        if (doQuery) {
            //Start retrieval by querying for similar users first.
            retrievalUtil = new RetrievalUtil(this.getActivity().getFilesDir().getAbsolutePath() + "/");
            List<Pair<User, Double>> similarUser = CBRConstants.getUserFromCBR(user , retrievalUtil, helper);
            LogUtil.logUserSimilarity(requireContext(),user, similarUser);
            //Get all plans for each user still in the process from the database.
            allPlans = getExerciseListsByUsers(similarUser, bundle, user
                    , CBRConstants.EXERCISE_LIST_SIMILARITY_THRESHOLD);
            adaptPlans(allPlans, bundle);
            LogUtil.LogPlanSimilarity(requireContext(), "--------------------BREAK----------------");
        }

        CBRExerciseListAdapter adapter = new CBRExerciseListAdapter(allPlans);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        ItemClickSupport.addTo(recyclerView).setOnItemClickListener((recyclerView1, position, v) -> {
            viewModel.addPlanList(allPlans.get(position).getFirst());
            Bundle bundleList = new Bundle();
            bundleList.putString("title",allPlans.get(position).getFirst().getPlan_name());
            bundleList.putBoolean("result", true);
            Navigation.findNavController(view).navigate(R.id.action_fragment_show_cbr_result_to_fragment_show_exercise_list, bundleList);
        });

        Button button = view.findViewById(R.id.button_confirm_pick_cbr_result);
        button.setOnClickListener(v -> {
            System.out.println(adapter.getActivePair().getFirst().getPlan_name());
            helper.addPlanToUser(adapter.getActivePair().getFirst(), user.getUid());
            Navigation.findNavController(view).navigate(R.id.action_fragment_show_cbr_result_to_fragment_workout_landing);
        });
        doQuery = false;
    }

    /**
     * Returns the exercise lists with their similarity values. Those below the needed threshold will
     * be removed from the list.
     * @param userList
     * @param bundle
     * @param user
     * @return
     */
    private List<Pair<ExerciseList, Double>> getExerciseListsByUsers(List<Pair<User, Double>> userList
            , Bundle bundle, User user, double exerciseListThreshold) {
        List<Pair<ExerciseList, Double>> exerciseLists = helper.getPlansForCBRUsers(userList);

        Iterator<Pair<ExerciseList, Double>> it = exerciseLists.iterator();

        while (it.hasNext()){
            Pair<ExerciseList, Double> p = it.next();
            LogUtil.LogPlanSimilarity(requireContext(), "\t>SIMILARITY FOR PLAN: "
                    + p.getFirst().getPlan_name() + " FROM " + p.getFirst().getuID()
                    + " FOR: " + p.getFirst().getMuscle_group().getLabel());
            double planSimilarity = 0.0;
            LogUtil.LogPlanSimilarity(requireContext(), "\t\t>USER SIMILARITY: " + p.getSecond());
            //Multiplication as the input is in minutes but the data is held in seconds.
            planSimilarity += CBRConstants.getSimilarityForDuration(p.getFirst().getDuration()
                    ,(bundle.getInt("duration")* 60)) /CBRConstants.getCompleteWeights();
            LogUtil.LogPlanSimilarity(requireContext(), "\t\t>DURATION SIMILARITY: " + planSimilarity);
            planSimilarity += CBRConstants.getEquipmentSimilarity(user.getEquipments(), p.getFirst()
                    .getNeededEquipment()) / CBRConstants.getCompleteWeights();
            LogUtil.LogPlanSimilarity(requireContext(), "\t\t>EQUIPMENT SIMILARITY: " + planSimilarity);

            planSimilarity += CBRConstants.getGoalSimilarity(p.getFirst().getGoal(),
                    GoalEnum.getEnumByID(bundle.getInt("goal")))/ CBRConstants.getCompleteWeights();
            LogUtil.LogPlanSimilarity(requireContext(), "\t\t>GOAL SIMILARITY: " + planSimilarity);

            p.setSecond(
                    ((p.getSecond() * CBRConstants.getCompleteWeightSumPerson())
                            /CBRConstants.getCompleteWeights()) + planSimilarity);

          //  p.setSecond(p.getSecond() * CBRConstants.WEIGHT_PERSON
          //         + planSimilarity * (1-CBRConstants.WEIGHT_PERSON));

            p.setSecond(p.getSecond()* CBRConstants.getMuscleGroupSimilarityMultiplier
                    (MuscleGroupEnum.getEnumByID(bundle.getInt("group")), p.getFirst().getMuscle_group()));
            LogUtil.LogPlanSimilarity(requireContext(), "\t\tEND SIMILARITY: " + p.getSecond());
            if (p.getSecond() < exerciseListThreshold) {
                it.remove();
            }
        }
        return exerciseLists.stream()
                .sorted(Comparator.comparingDouble(Pair<ExerciseList,Double>::getSecond).reversed())
                .collect(Collectors.toList());
    }

    private void adaptPlans(List<Pair<ExerciseList, Double>> plans, Bundle bundle) {
        for (Pair<ExerciseList, Double> p : plans) {
            LogUtil.LogPlanSimilarity(requireContext(), "\t>Adapting Plan: " + p.getFirst().getPlan_name());
            if (p.getFirst().getGoal() != GoalEnum.getEnumByID(bundle.getInt("goal"))
                    && GoalEnum.getEnumByID(bundle.getInt("goal"))!= GoalEnum.POWER) {
                LogUtil.LogPlanSimilarity(requireContext(), "\t\t\t>Adapting GOAL: " + p.getFirst().getPlan_name());
                adaptForGoal(p.getFirst(),bundle);
            }
            ListIterator<Exercise> ite = p.getFirst().getExercises().listIterator();

            while (ite.hasNext()) {
                Exercise e = ite.next();
                if (!user.getEquipments().contains(e.getEquipment())
                        && !(user.getEquipments().contains(EquipmentEnum.FitnessStudio)
                        && CBRConstants.equipmentsOfSportStudios.contains(e.getEquipment()))) {
                    LogUtil.LogPlanSimilarity(requireContext(), "\t\t>Adapting Exercise: " + e.getName());
                    List<Pair<Exercise, Double>> exchangeExercises = retrievalUtil
                            .retrieveExercise(helper, e, user.getEquipments(), p.getFirst().getAllExerciseIDs());
                    if (exchangeExercises.size() > 0 && exchangeExercises.get(0) != null) {
                        Exercise exchange = exchangeExercises.get(0).getFirst();
                        LogUtil.LogPlanSimilarity(requireContext(), "\t\t\t>Similar Exercises Found!"
                                + exchange.getName() + " with SIM:" + exchangeExercises.get(0).getSecond());
                        List<Exercise> similarExercises = getSimilarExercisesForAdaption(exchange, bundle);
                        if (e.isBodyweight() == exchange.isBodyweight() || similarExercises.size() <= 0) { //Assuming that we can copy values
                            LogUtil.LogPlanSimilarity(requireContext(), "\t\t\t>Exercise Type match, copy values!");
                            exchange.setWeight(exchange.isBodyweight() ? 0 : e.getWeight()); //to at least remove weight
                            exchange.setSetNumber(e.getSetNumber());
                            exchange.setRepNumber(e.getRepNumber());
                            exchange.setBreakTime(e.getBreakTime());
                        } else {
                            LogUtil.LogPlanSimilarity(requireContext(), "\t\t\t>No Match, try estimation! ");
                            CBRConstants.adaptExerciseAfter(similarExercises, exchange, requireContext()
                                    , CBRConstants.initExerciseValues(GoalEnum.getEnumByID(bundle.getInt("goal"))
                                            , user.getWorkoutTypeN()));
                        }
                        ite.remove();
                        ite.add(exchange);
                    }
                }
            }
        }
    }

    private void adaptForGoal (ExerciseList exerciseList, Bundle bundle) {
        for (Exercise e : exerciseList.getExercises()) {
            List<Exercise> exercises = getSimilarExercisesForAdaption(e, bundle);
            CBRConstants.adaptExerciseAfter(exercises, e, requireContext()
                    , CBRConstants.initExerciseValues(GoalEnum.getEnumByID(bundle.getInt("goal")), user.getWorkoutTypeN()));
        }
        exerciseList.setGoal(bundle.getInt("goal"));
    }

    private List<Exercise> getSimilarExercisesForAdaption(Exercise exchange, Bundle bundle ) {
        List<Exercise> similarExercises = new ArrayList<>();

        for (Pair<ExerciseList, Double> p : allPlans) {
            if (p.getFirst().getGoal() ==  GoalEnum.getEnumByID(bundle.getInt("goal"))
                    && p.getFirst().getPlan_id() != exchange.getPlanID()) {
                for (Exercise e : p.getFirst().getExercises()) {
                    if (e.getMuscle() == exchange.getMuscle()
                            && e.isBodyweight() == exchange.isBodyweight()) { //Exercise targeting same muscle with same goal
                        LogUtil.LogPlanSimilarity(requireContext(), "\t\t\t>Looking for similar to: "
                                + exchange.getName() + " Bodyweight: "+ exchange.isBodyweight()
                                +" Muscle: " + exchange.getMuscle().getLabel() + " FOUND: "
                                + e.getName() + " Bodyweigt : " + e.isBodyweight() + " MUSCLE: "
                                + e.getMuscle().getLabel() + " FROM Plan: " + p.getFirst().getPlan_id());
                        similarExercises.add(e);
                    }
                }
            }
        }
        return similarExercises;
    }

    /**
     * Needed to prevent the queries to run while the data is still relevant and accessible.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        doQuery = true;
    }
}