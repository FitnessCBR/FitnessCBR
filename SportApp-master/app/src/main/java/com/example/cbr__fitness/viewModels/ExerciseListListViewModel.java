package com.example.cbr__fitness.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.cbr__fitness.data.ExerciseList;

import java.util.List;


public class ExerciseListListViewModel extends ViewModel {

    private final MutableLiveData<List<ExerciseList>> exercise = new MutableLiveData<>();

    public void addExercise(List<ExerciseList> e) {
        exercise.setValue(e);
    }

    public LiveData<List<ExerciseList>> getSelected(){
        return exercise;
    }

}
