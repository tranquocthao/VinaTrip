package com.dfa.vinatrip;

import android.content.Context;
import android.content.SharedPreferences;

public class CheckFirstTimeLaunch {
    private static final String PREF_NAME = "WelcomeScreen";
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context context;
    private int PRIVATE_MODE = 0;

    public CheckFirstTimeLaunch(Context context) {
        this.context = context;
        sharedPreferences = this.context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public void SetFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean IsFirstTimeLaunch() {
        return sharedPreferences.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }
}
