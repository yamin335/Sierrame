package com.mmfinfotech.streameApp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import kotlin.Deprecated;

@Deprecated(message = "use AppPreferences")
public class PrefManager {
    public static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(AppConstants.PREF_NAME, Context.MODE_PRIVATE);
    }
}
