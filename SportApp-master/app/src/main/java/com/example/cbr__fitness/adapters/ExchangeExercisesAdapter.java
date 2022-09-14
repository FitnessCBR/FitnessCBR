package com.example.cbr__fitness.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cbr__fitness.R;
import com.example.cbr__fitness.data.Exercise;
import com.example.cbr__fitness.viewModels.ExerciseViewModel;

import java.text.DecimalFormat;
import java.util.List;

public class ExchangeExercisesAdapter extends RecyclerView.Adapter<ExchangeExercisesAdapter.ExchangeExercisesViewHolder> {

    private List<Exercise> exercises;

    ExerciseViewModel exerciseViewModel;

    public ExchangeExercisesAdapter(List<Exercise> exercises, FragmentActivity context) {
        this.exercises = exercises;
        exerciseViewModel = new ViewModelProvider(context).get(ExerciseViewModel.class);
    }

    @NonNull
    @Override
    public ExchangeExercisesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View exerciseListView= inflater.inflate(R.layout.fragment_edit_exercise_list_recycler_item, parent, false);

        return new ExchangeExercisesViewHolder(exerciseListView);
    }

    @Override
    public void onBindViewHolder(@NonNull ExchangeExercisesViewHolder holder, int position) {
        Exercise exercise = exercises.get(position);
        holder.textSecondaryMuscle.setText(exercise.getSecondaryMuscle().getLabel());
        holder.textPrimeMuscle.setText(exercise.getMuscle().getLabel());
        holder.textEquipment.setText(exercise.getEquipment().getLabel());
        holder.textTitle.setText(exercise.getName());
        holder.textIsMultiJoint.setText(exercise.getType().name());

        holder.buttonExchange.setOnClickListener(v -> {

            exerciseViewModel.addExercise(exercises.get(position));
            Bundle bundle = new Bundle();
            bundle.putInt("eId", exercise.getExerciseID());
            bundle.putInt("pId", exercise.getPlanID());
            Navigation.findNavController(v).navigate(R.id.action_fragment_edit_exercise_list_to_fragment_exchange_exercise, bundle);
        });
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    public class ExchangeExercisesViewHolder extends RecyclerView.ViewHolder {

        public TextView textTitle;

        public TextView textIsMultiJoint;

        public TextView textPrimeMuscle;

        public TextView textSecondaryMuscle;

        public TextView textEquipment;

        public Button buttonExchange;

        public ExchangeExercisesViewHolder(@NonNull View itemView) {
            super(itemView);
            textPrimeMuscle = itemView.findViewById(R.id.text_prime_muscle_edit_exercise_list_recycler_item);
            textIsMultiJoint = itemView.findViewById(R.id.text_is_multi_joint_edit_exercise_list_recycler_item);
            textTitle = itemView.findViewById(R.id.text_title_edit_exercise_list_recycler_item);
            textEquipment = itemView.findViewById(R.id.text_equipment_edit_exercise_list_recycler_item);
            textSecondaryMuscle = itemView.findViewById(R.id.text_secondary_muscle_edit_exercise_list_recycler_item);
            buttonExchange = itemView.findViewById(R.id.button_exchange_exercise);
        }
    }
}
