package com.example.offlinepostsapp.data;

import android.content.Context;
import android.content.SharedPreferences;

public class AppPrefs {
    private static final String PREF = "app_prefs";
    private static final String KEY_THEME = "theme";
    private static final String KEY_LOGGED_IN = "logged_in";

    public static final String THEME_LIGHT = "light";
    public static final String THEME_DARK = "dark";
    public static final String THEME_BLUE = "blue";

    private static SharedPreferences sp(Context c) {
        return c.getSharedPreferences(PREF, Context.MODE_PRIVATE);
    }

    public static void setTheme(Context c, String theme) {
        sp(c).edit().putString(KEY_THEME, theme).apply();
    }

    public static String getTheme(Context c) {
        return sp(c).getString(KEY_THEME, THEME_LIGHT);
    }

    public static void setLoggedIn(Context c, boolean v) {
        sp(c).edit().putBoolean(KEY_LOGGED_IN, v).apply();
    }

    public static boolean isLoggedIn(Context c) {
        return sp(c).getBoolean(KEY_LOGGED_IN, false);
    }
}
