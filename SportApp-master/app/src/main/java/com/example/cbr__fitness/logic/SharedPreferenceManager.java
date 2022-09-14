package com.example.cbr__fitness.logic;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPreferenceManager {

    public static final String USER_ID_KEY = "uID";

    public static final String USER_ADMIN_KEY = "admin";

    public static void saveUserID (int id, Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefEditor = pref.edit();
        prefEditor.putInt(USER_ID_KEY, id);
        prefEditor.apply();
    }

    public static int getLoggedUserID (Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getInt(USER_ID_KEY, -1);
    }

    public static void saveUserAdminRoll(int id, Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefEditor = pref.edit();
        prefEditor.putInt(USER_ADMIN_KEY, id);
        prefEditor.apply();
    }

    public static boolean getIsUserAdmin(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getInt(USER_ADMIN_KEY, 1) == 3;
    }

    public static void resetRolls(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefEditor = pref.edit();
        prefEditor.remove(USER_ADMIN_KEY);
        prefEditor.apply();
    }
}
