package com.example.cbr__fitness.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cbr__fitness.R;
import com.example.cbr__fitness.data.Exercise;
import com.example.cbr__fitness.fragments.CreateExercise;

import java.util.List;

public class CreateExerciseAdapter extends RecyclerView.Adapter<CreateExerciseAdapter.CreateExerciseViewHolder> {

    List<Exercise> exercises;

    CreateExercise fragment;

    boolean isSelected;

    public CreateExerciseAdapter(List<Exercise> exercises, CreateExercise parent, boolean isSelected) {
        this.exercises = exercises;
        fragment = parent;
        this.isSelected = isSelected;
    }

    @NonNull
    @Override
    public CreateExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View exerciseListView= inflater.inflate(R.layout.fragment_create_exercise_recycler_item, parent, false);

        CreateExerciseAdapter.CreateExerciseViewHolder viewHolder = new CreateExerciseAdapter.CreateExerciseViewHolder(exerciseListView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CreateExerciseViewHolder holder, int position) {
        Exercise exercise = exercises.get(position);

        holder.headerText.setText(exercise.getName());
        holder.primaryMuscle.setText(exercise.getMuscle().getLabel());
        holder.secondaryMuscle.setText(exercise.getSecondaryMuscle().getLabel());
        holder.buttonSelectOrDeselect.setText(isSelected ? "-" : "+");
        holder.buttonSelectOrDeselect.setOnClickListener(v -> fragment.
                gotNotified(exercise, holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    public class CreateExerciseViewHolder extends RecyclerView.ViewHolder {

        TextView headerText;

        TextView primaryMuscle;

        TextView secondaryMuscle;

        Button buttonSelectOrDeselect;

        public CreateExerciseViewHolder(@NonNull View itemView) {
            super(itemView);
            headerText = itemView.findViewById(R.id.text_title_create_recycler_item);
            primaryMuscle = itemView.findViewById(R.id.text_prime_muscle_create_recycler_item);
            secondaryMuscle = itemView.findViewById(R.id.text_secondary_muscle_create_recycler_item);
            buttonSelectOrDeselect = itemView.findViewById(R.id.button_add_create_exercise);

        }
    }
}
