package com.example.cbr__fitness.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.cbr__fitness.R;
import com.example.cbr__fitness.customListenerMethods.ToCsvButtonListener;
import com.example.cbr__fitness.data.Exercise;
import com.example.cbr__fitness.data.User;
import com.example.cbr__fitness.databasehelper.CsvDataHelper;
import com.example.cbr__fitness.databasehelper.FitnessDBSqliteHelper;
import com.example.cbr__fitness.interfaces.ToCaseCsvInterface;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdminLanding#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminLanding extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private List<ToCaseCsvInterface> allUser;

    private List<ToCaseCsvInterface> allExercises;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AdminLanding() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment admin_landing.
     */
    // TODO: Rename and change types and number of parameters
    public static AdminLanding newInstance(String param1, String param2) {
        AdminLanding fragment = new AdminLanding();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_landing, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        allExercises = new ArrayList<>();
        allUser = new ArrayList<>();

        Button userCsvButton = view.findViewById(R.id.button_user_to_csv_admin);
        Button exerciseCsvButton = view.findViewById(R.id.button_exercise_to_csv_admin);
        FitnessDBSqliteHelper helper = new FitnessDBSqliteHelper(requireContext());



        userCsvButton.setOnClickListener(v -> {
            allUser.addAll(helper.getAllUser());
            File file = new File(getActivity().getFilesDir() + "/" + CsvDataHelper.userCsvName);
            ToCsvButtonListener.writeClassToCaseCsv(allUser, file);
        });

        exerciseCsvButton.setOnClickListener(v -> {
            allExercises.addAll(helper.getAllExercises(0));
            File file = new File(getActivity().getFilesDir() + "/" + CsvDataHelper.exerciseCsvName);
            ToCsvButtonListener.writeClassToCaseCsv(allExercises, file);
        });
    }
}