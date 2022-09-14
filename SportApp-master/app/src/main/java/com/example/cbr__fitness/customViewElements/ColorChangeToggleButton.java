package com.example.cbr__fitness.customViewElements;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CompoundButton;

import androidx.appcompat.widget.AppCompatToggleButton;
import androidx.core.content.ContextCompat;

import com.example.cbr__fitness.R;
import com.example.cbr__fitness.customListenerMethods.ColorChangeToggleListener;
import com.example.cbr__fitness.interfaces.EnumInterface;


public class ColorChangeToggleButton extends AppCompatToggleButton {

    private String name;
    /**
     * stores the DB id for the represented item to simplify using it after selection.
     */
    private int id;

    private EnumInterface enumE;

    public ColorChangeToggleButton (Context context, int id, String label) {
        super(context);
        this.setTextOff(label);
        this.setTextOn(label);
        this.setChecked(false);
        this.setId(View.generateViewId());
        this.name = label;
        this.id = id;
        this.setPadding(5,5,5,5);
        this.setBackgroundColor(ContextCompat.getColor(context, R.color.uncheckedColor));
    }

    public ColorChangeToggleButton (Context context, EnumInterface enumE) {
        this(context, enumE.getID(), enumE.getLabel());
        this.enumE = enumE;
    }

    public ColorChangeToggleButton(Context context, AttributeSet set) {
        super(context, set);

    }

    public int getItemID(){
        return id;
    }

    public EnumInterface getEnumE() {
        return enumE;
    }
}
