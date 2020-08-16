package com.kpgn.tictactoe.helper;

import android.content.Context;
import android.content.SharedPreferences;

import com.kpgn.tictactoe.application.TicTacToeApplication;

public final class SharedPreferenceHelper {

    private static final String SP_NAME = "TicTacToe.SP";
    private static final String X_SCORE = "XScore";
    private static final String O_SCORE = "OScore";

    private SharedPreferenceHelper() {

    }

    private static SharedPreferences getSharedPreferences() {
        return TicTacToeApplication.getContext().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }

    public static void clearSharedPreference() {
        getSharedPreferences().edit().clear().apply();
    }

    public static void setXScore(int xScore) {
        getSharedPreferences().edit().putInt(X_SCORE, xScore).apply();
    }

    public static int getXScore() {
        return getSharedPreferences().getInt(X_SCORE, 0);
    }

    public static void setOScore(int oScore) {
        getSharedPreferences().edit().putInt(O_SCORE, oScore).apply();
    }

    public static int getOScore() {
        return getSharedPreferences().getInt(O_SCORE, 0);
    }
}