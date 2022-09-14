package com.example.cbr__fitness.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.cbr__fitness.data.ExerciseList;

public class PlanViewModel extends ViewModel {

    private final MutableLiveData<ExerciseList> planList = new MutableLiveData<ExerciseList>();

    public void addPlanList (ExerciseList list) {
        planList.setValue(list);
    }

    public LiveData<ExerciseList> getSelected(){
        return planList;
    }
}
