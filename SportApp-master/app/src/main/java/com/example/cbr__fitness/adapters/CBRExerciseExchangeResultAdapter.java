package com.example.cbr__fitness.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cbr__fitness.R;
import com.example.cbr__fitness.customViewElements.ColorChangeToggleButton;
import com.example.cbr__fitness.data.Exercise;
import com.example.cbr__fitness.logic.AccountUtil;

import java.text.DecimalFormat;
import java.util.List;

import de.dfki.mycbr.util.Pair;

public class CBRExerciseExchangeResultAdapter extends RecyclerView.Adapter<CBRExerciseExchangeResultAdapter.CBRExerciseExchangeResultViewHolder> {

    private List<Pair<Exercise, Double>> results;

    private int activeButtonPosition;

    DecimalFormat format;

    public CBRExerciseExchangeResultAdapter(List<Pair<Exercise, Double>> list) {
        this.results = list;
        this.format = new DecimalFormat("#.###");
    }

    @NonNull
    @Override
    public CBRExerciseExchangeResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View exerciseListView= inflater.inflate(R.layout.fragment_cbr_exchange_exercise_result_recycler_item, parent, false);

        return new CBRExerciseExchangeResultAdapter.CBRExerciseExchangeResultViewHolder(exerciseListView);
    }

    @Override
    public void onBindViewHolder(@NonNull CBRExerciseExchangeResultViewHolder holder, int position) {
        Pair<Exercise, Double> list = results.get(position);
        TextView planHeader = holder.planHeader;
        planHeader.setText(list.getFirst().getName());
        TextView muscleGroup = holder.muscleGroup;
        muscleGroup.setText(list.getFirst().getMuscle().getLabel());
        TextView secondaryMuscle = holder.secondaryMuscle;
        secondaryMuscle.setText(list.getFirst().getSecondaryMuscle().getLabel());
        ColorChangeToggleButton button = holder.button;
        button.setChecked(position == activeButtonPosition);
        TextView similarity = holder.similarity;
        similarity.setText(format.format(list.getSecond()));
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public Exercise getChosenExercise() {
        return results.get(activeButtonPosition).getFirst();
    }

    public class CBRExerciseExchangeResultViewHolder extends RecyclerView.ViewHolder {

        private TextView planHeader;

        private TextView muscleGroup;

        private TextView secondaryMuscle;

        private TextView similarity;

        private ColorChangeToggleButton button;

        public CBRExerciseExchangeResultViewHolder(@NonNull View itemView) {
            super(itemView);
            planHeader = itemView.findViewById(R.id.text_title_cbr_result_exchange_exercise_recycler_item);
            muscleGroup = itemView.findViewById(R.id.text_prime_muscle_cbr_result_exchange_exercise_recycler_item);
            secondaryMuscle = itemView.findViewById(R.id.text_secondary_muscle_cbr_result_exchange_exercise_recycler_item);
            button = itemView.findViewById(R.id.color_change_toggle_chosen_exchange_exercise_recycler_item);
            button.setOnClickListener((buttonView) -> {
                int lastButton = activeButtonPosition;
                activeButtonPosition = getAdapterPosition();
                notifyItemChanged(activeButtonPosition);
                notifyItemChanged(lastButton);
            });
            similarity = itemView.findViewById(R.id.text_similarity_cbr_result_exchange_exercise_recycler_item);

        }
    }
}
