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
import com.example.cbr__fitness.data.ExerciseList;
import com.example.cbr__fitness.enums.EquipmentEnum;
import com.example.cbr__fitness.logic.AccountUtil;

import org.w3c.dom.Text;

import java.util.List;

public class ExerciseListAdapter extends RecyclerView.Adapter<ExerciseListAdapter.ExerciseListViewHolder>{

    private List<ExerciseList> exerciseLists;

    private Context context;

    public ExerciseListAdapter (List<ExerciseList> list, Context context) {
        this.context = context;
        exerciseLists = list;
    }

    @NonNull
    @Override
    public ExerciseListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View exerciseListView= inflater.inflate(R.layout.fragment_exercise_list_recycler_item_layout, parent, false);

        ExerciseListViewHolder viewHolder = new ExerciseListViewHolder(exerciseListView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ExerciseListViewHolder holder, int position) {
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
            //System.out.println("NEEDING: " + e.getLabel());
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

    public class ExerciseListViewHolder extends RecyclerView.ViewHolder {

        public TextView workout_plan_header;

        public TextView workout_plan_duration;

        public TextView workout_plan_prime_muscle;

        public TextView textWorkoutGoal;

        public Flow flowEquipmentList;

        public ConstraintLayout constraintMainBody;

        public ExerciseListViewHolder(View itemView) {
            super(itemView);

            workout_plan_header = itemView.findViewById(R.id.text_plan_name_exercise_list_recycler_item);
            workout_plan_duration =  itemView.findViewById(R.id.text_duration_exercise_list_recycler_item);
            workout_plan_prime_muscle = itemView.findViewById(R.id.text_main_muscle_exercise_list_recycler_item);
            textWorkoutGoal = itemView.findViewById(R.id.text_goal_exercise_list_recycler_item);
            constraintMainBody = itemView.findViewById(R.id.constraint_plan_content_exercise_list_recycler_item);
            flowEquipmentList = itemView.findViewById(R.id.flow_equipment_exercise_list_landing_recycler_item);


        }
    }
}
