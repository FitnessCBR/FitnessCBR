package com.example.cbr__fitness.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.cbr__fitness.data.Exercise;

public class ExerciseViewModel extends ViewModel {


    private final MutableLiveData<Exercise> exercise = new MutableLiveData<>();

    public void addExercise(Exercise e) {
        exercise.setValue(e);
    }

    public LiveData<Exercise> getSelected(){
        return exercise;
    }
}
