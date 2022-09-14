package com.example.cbr__fitness.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.helper.widget.Flow;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cbr__fitness.R;
import com.example.cbr__fitness.customViewElements.ColorChangeToggleButton;
import com.example.cbr__fitness.data.ExerciseList;
import com.example.cbr__fitness.enums.EquipmentEnum;
import com.example.cbr__fitness.logic.AccountUtil;

import java.util.ArrayList;
import java.util.List;

public class DeleteExerciseListAdapter extends RecyclerView.Adapter<DeleteExerciseListAdapter.DeleteExerciseListViewHolder>{

    private List<ExerciseList> exerciseLists;

    private Context context;

    private List<Integer> chosenPlans;

    public DeleteExerciseListAdapter (List<ExerciseList> list, Context context) {
        this.context = context;
        exerciseLists = list;
        chosenPlans = new ArrayList<>();
    }

    @NonNull
    @Override
    public DeleteExerciseListAdapter.DeleteExerciseListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View exerciseListView= inflater.inflate(R.layout.fragment_delete_exercise_list_recycler_item_layout, parent, false);

        DeleteExerciseListAdapter.DeleteExerciseListViewHolder viewHolder = new DeleteExerciseListAdapter
                .DeleteExerciseListViewHolder(exerciseListView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(DeleteExerciseListAdapter.DeleteExerciseListViewHolder holder, int position) {
        ExerciseList list = exerciseLists.get(position);

        TextView workout_plan_header = holder.workout_plan_header;
        workout_plan_header.setText(list.getPlan_name());
        TextView workout_plan_duration = holder.workout_plan_duration;
        workout_plan_duration.setText(AccountUtil.getDurationAsTime(list.getDuration()));
        TextView workout_plan_prime_muscle = holder.workout_plan_prime_muscle;
        workout_plan_prime_muscle.setText(list.getMuscle_group().getLabel());
        TextView textWorkoutGoal = holder.textWorkoutGoal;
        textWorkoutGoal.setText(list.getGoal().getLabel());

        for (EquipmentEnum e : list.getNeededEquipment()) {
            System.out.println("NEEDING: " + e.getLabel());
            TextView t = new TextView(context);
            t.setText(e.getLabel());
            t.setText(e.getLabel());
            t.setTextSize(R.dimen.target_text_size_small);
            t.setId(View.generateViewId());
            holder.constraintMainBody.addView(t);
            holder.flowEquipmentList.addView(t);
        }
        holder.itemView.setOnClickListener(v -> System.out.println("HIT: " + v.getId()));
    }

    @Override
    public int getItemCount() {
        return exerciseLists.size();
    }

    public List<Integer> getChosenPlans() {
        List<Integer> toRemove = new ArrayList<>();
        for (Integer i : chosenPlans) {
            toRemove.add(exerciseLists.get(i).getPlan_id());
        }
        return toRemove;
    }

    public class DeleteExerciseListViewHolder extends RecyclerView.ViewHolder {

        public TextView workout_plan_header;

        public TextView workout_plan_duration;

        public TextView workout_plan_prime_muscle;

        public TextView textWorkoutGoal;

        public Flow flowEquipmentList;

        public ColorChangeToggleButton button;

        public ConstraintLayout constraintMainBody;

        public DeleteExerciseListViewHolder(View itemView) {
            super(itemView);

            workout_plan_header = itemView.findViewById(R.id.text_plan_name_delete_exercise_list_recycler_item);
            workout_plan_duration =  itemView.findViewById(R.id.text_duration_delete_exercise_list_recycler_item);
            workout_plan_prime_muscle = itemView.findViewById(R.id.text_main_muscle_delete_exercise_list_recycler_item);
            textWorkoutGoal = itemView.findViewById(R.id.text_goal_delete_exercise_list_recycler_item);
            constraintMainBody = itemView.findViewById(R.id.constraint_plan_content_delete_exercise_list_recycler_item);
            flowEquipmentList = itemView.findViewById(R.id.flow_equipment_delete_exercise_list_recycler_item);
            button = itemView.findViewById(R.id.color_change_toggle_delete_exercise_list_recycler_item);
            button.setOnClickListener(v -> {
                if (chosenPlans.contains(getAdapterPosition())) {
                    chosenPlans.remove(Integer.valueOf(getAdapterPosition()));
                } else {
                    chosenPlans.add(getAdapterPosition());
                }
            });
        }
    }
}
