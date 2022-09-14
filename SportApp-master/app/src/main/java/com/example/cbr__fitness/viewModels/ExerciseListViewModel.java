package com.example.cbr__fitness.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.cbr__fitness.data.Exercise;

import java.util.List;

import de.dfki.mycbr.util.Pair;

public class ExerciseListViewModel extends ViewModel {

    private final MutableLiveData<List<Pair<Exercise, Double>>> exercise = new MutableLiveData<>();

    public void addExercise(List<de.dfki.mycbr.util.Pair<Exercise, Double>> e) {
        exercise.setValue(e);
    }

    public LiveData<List<Pair<Exercise, Double>>> getSelected(){
        return exercise;
    }
}
