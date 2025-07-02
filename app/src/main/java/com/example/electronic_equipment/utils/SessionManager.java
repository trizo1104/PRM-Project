package com.example.electronic_equipment.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private final String PREF_NAME = "user_session";
    private final String KEY_ROLE = "role";
    private final String KEY_TOKEN = "token";

    public SessionManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveSession(String token, String role) {
        editor.putString(KEY_TOKEN, token);
        editor.putString(KEY_ROLE, role);
        editor.apply();
    }

    public String getToken() {
        return sharedPreferences.getString(KEY_TOKEN, null);
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getString(KEY_TOKEN, null) != null;
    }

    public void logout() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
