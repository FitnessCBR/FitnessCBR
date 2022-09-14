package com.example.cbr__fitness.logic;

import android.app.Activity;
import android.view.View;

import androidx.constraintlayout.helper.widget.Flow;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.cbr__fitness.customListenerMethods.ColorChangeToggleListener;
import com.example.cbr__fitness.customViewElements.ColorChangeToggleButton;
import com.example.cbr__fitness.data.User;
import com.example.cbr__fitness.enums.EquipmentEnum;
import com.example.cbr__fitness.interfaces.EnumInterface;

import java.util.List;

public class AccountUtil {

    public static void setUpToggleButtons(User user, EnumInterface[] enums
            , ConstraintLayout constraint, Flow flow, List<ColorChangeToggleButton> buttons
            , Activity activity, boolean buttonsActive){
        for (EnumInterface eqn : enums) {
            if (!(eqn.getID() == 1 && eqn instanceof EquipmentEnum)) { //"Keins" is equipment everyone should have thus its excluded here.
                ColorChangeToggleButton t = new ColorChangeToggleButton(activity, eqn);
                t.setOnCheckedChangeListener(ColorChangeToggleListener::onCheckedChanged);

                buttons.add(t);
                constraint.addView(t);
                flow.addView(t);
                if (user.getEquipments().contains(eqn) || user.getLimitations().contains(eqn)) {
                    t.setChecked(true);
                }
                t.setClickable(buttonsActive);
            }
        }
    }

    public static float getBMI (int weight, int height) {
        return (weight / (height* 1.0f) / (height * 1.0f)) * 10000;
    }

    public static String getDurationAsTime(int duration) {
        return (duration / 60) + "Min.";
    }
}
