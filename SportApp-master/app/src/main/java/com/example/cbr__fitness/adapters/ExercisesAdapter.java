package com.example.cbr__fitness.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cbr__fitness.R;
import com.example.cbr__fitness.data.Exercise;

import java.util.List;

public class ExercisesAdapter extends RecyclerView.Adapter<ExercisesAdapter.ExerciseViewHolder>{

    private List<Exercise> exercises;

    public ExercisesAdapter(List<Exercise> exercises){
        this.exercises = exercises;
    }

    @NonNull
    @Override
    public ExercisesAdapter.ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View exerciseListView= inflater.inflate(R.layout.fragment_exercise_recycler_item_layout, parent, false);

        ExerciseViewHolder viewHolder = new ExerciseViewHolder(exerciseListView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ExercisesAdapter.ExerciseViewHolder holder, int position) {
        Exercise exercise = exercises.get(position);

        TextView exerciseTitle = holder.exerciseTitleView;
        exerciseTitle.setText(exercise.getName());
        TextView exercisePrimeMuscle = holder.exercisePrimeMuscle;
        exercisePrimeMuscle.setText(exercise.getMuscle().toString());
        TextView setTextView = holder.setTextView;
        setTextView.setText("Sets: " + Integer.toString(exercise.getSetNumber()));
        TextView repTextView = holder.repTextView;
        repTextView.setText("Repetitions: " + Integer.toString(exercise.getRepNumber()));
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    public class ExerciseViewHolder extends RecyclerView.ViewHolder {

        public Context context;

        public TextView exerciseTitleView;

        public TextView exercisePrimeMuscle;

        public TextView setTextView;

        public TextView repTextView;

        public ExerciseViewHolder(@NonNull View itemView) {
            super(itemView);

            exerciseTitleView = (TextView) itemView.findViewById(R.id.text_title_exercise_recycler_item);
            exercisePrimeMuscle = (TextView) itemView.findViewById(R.id.text_prime_muscle_exercise_recycler_item);
            setTextView = (TextView) itemView.findViewById(R.id.text_set_exercise_recycler_item);
            repTextView = (TextView) itemView.findViewById(R.id.text_rep_exercise_recycler_item);
        }
    }
}
