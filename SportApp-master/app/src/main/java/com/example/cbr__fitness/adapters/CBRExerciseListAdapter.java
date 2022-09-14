package com.example.cbr__fitness.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatToggleButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cbr__fitness.R;
import com.example.cbr__fitness.customViewElements.ColorChangeToggleButton;
import com.example.cbr__fitness.data.ExerciseList;
import com.example.cbr__fitness.logic.AccountUtil;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import de.dfki.mycbr.util.Pair;

public class CBRExerciseListAdapter extends RecyclerView.Adapter<CBRExerciseListAdapter.CBRExerciseListViewHolder>{

    private List<Pair<ExerciseList, Double>> results;

    private int activeButtonPosition = 0;

    DecimalFormat format;

    public CBRExerciseListAdapter (List<Pair<ExerciseList, Double>> results) {
        this.results = results;
        this.format = new DecimalFormat("#.###");
    }

    @NonNull
    @Override
    public CBRExerciseListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View exerciseListView= inflater.inflate(R.layout.fragment_c_b_r_result_exercise_list_recycler_view, parent, false);

        CBRExerciseListAdapter.CBRExerciseListViewHolder viewHolder = new CBRExerciseListAdapter.CBRExerciseListViewHolder(exerciseListView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CBRExerciseListViewHolder holder, int position) {
        Pair<ExerciseList, Double> list = results.get(position);
        TextView planHeader = holder.planHeader;
        planHeader.setText(list.getFirst().getPlan_name());
        TextView muscleGroup = holder.muscleGroup;
        muscleGroup.setText(list.getFirst().getMuscle_group().getLabel());
        TextView goal = holder.goal;
        goal.setText(list.getFirst().getGoal().getLabel());
        TextView duration = holder.duration;
        duration.setText(AccountUtil.getDurationAsTime(list.getFirst().getDuration()));
        ColorChangeToggleButton button = holder.button;
        button.setChecked(position == activeButtonPosition);
        TextView similarity = holder.similarity;
        similarity.setText(format.format(list.getSecond()));
    }

    public Pair<ExerciseList,Double> getActivePair(){
        return results.get(activeButtonPosition);
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public class CBRExerciseListViewHolder extends RecyclerView.ViewHolder {
        private TextView planHeader;

        private TextView muscleGroup;

        private TextView goal;

        private TextView similarity;

        private TextView duration;

        private ColorChangeToggleButton button;

        public CBRExerciseListViewHolder(@NonNull View itemView) {
            super(itemView);
            planHeader = itemView.findViewById(R.id.text_title_cbr_result_exercise_list_recycler_item);
            muscleGroup = itemView.findViewById(R.id.text_prime_muscle_cbr_result_exercise_list_recycler_item);
            goal = itemView.findViewById(R.id.text_goal_cbr_result_exercise_list_recycler_item);
            duration = itemView.findViewById(R.id.text_duration_cbr_result_exercise_list_recycler_item);
            button = itemView.findViewById(R.id.color_change_toggle_chosen_recycler_item);
            button.setOnClickListener((buttonView) -> {
                int lastButton = activeButtonPosition;
                activeButtonPosition = getAdapterPosition();
                notifyItemChanged(activeButtonPosition);
                notifyItemChanged(lastButton);
            });
            similarity = itemView.findViewById(R.id.text_similarity_cbr_result_exercise_list_recycler_item);

        }
    }
}
