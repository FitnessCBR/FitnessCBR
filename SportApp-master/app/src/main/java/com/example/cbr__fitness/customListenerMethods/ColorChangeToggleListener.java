package com.example.cbr__fitness.customListenerMethods;

import android.graphics.Color;
import android.widget.CompoundButton;

import androidx.appcompat.widget.AppCompatToggleButton;
import androidx.core.content.ContextCompat;

import com.example.cbr__fitness.R;
import com.example.cbr__fitness.customViewElements.ColorChangeToggleButton;

import java.util.List;

public class ColorChangeToggleListener {

    public static void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked){
            buttonView.setBackgroundColor(ContextCompat.getColor(buttonView.getContext(), R.color.checkedColor));
        } else {
            buttonView.setBackgroundColor(ContextCompat.getColor(buttonView.getContext(), R.color.uncheckedColor));
        }
    }

    public static void onCheckedChangedSingleButtonChecked(CompoundButton buttonView, boolean isChecked, List<ColorChangeToggleButton> toggleButtons) {
        if (isChecked) {
            for (ColorChangeToggleButton b : toggleButtons) {
                if (!b.equals(buttonView) && b.isChecked()) {
                    b.setChecked(false); //This should trigger the color change through the listener (it will not be checked then)
                }
            }
        }
    }
}
