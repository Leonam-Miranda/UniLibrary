package com.example.unilibrary.service;

import android.content.Context;

public class Session {
    private static final String PREFS = "library_section";
    private static final String KEY_ID = "user_id";

    public static void save(Context ctx, int userId){
        ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
                .edit().putInt(KEY_ID, userId).apply();
    }

    public static int getUserId(Context ctx){
        return ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
                .getInt(KEY_ID, -1);
    }

    public static boolean isLogged(Context ctx){
        return getUserId(ctx) != -1;
    }

    public static void end(Context ctx){
        ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
                .edit().remove(KEY_ID).apply();
    }

}
