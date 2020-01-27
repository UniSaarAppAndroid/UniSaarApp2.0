package com.example.uniapp_20.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    public static SessionManager mInstance;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    Context context;
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "uni_app";
    private static final String IS_SB = "isSB";
    private static final String IS_HB = "isHB";
    private static final String SCREEN = "SCREEN";

    private static final String SWITCH_DE = "switchDE";
    private static final String SWITCH_FR = "switchFR";
    private static final String SWITCH_EN = "switchEN";
    private static final String SWITCH_ONE = "switchOne";
    private static final String SWITCH_TWO = "switchTwo";
    private static final String SWITCH_THREE = "switchThree";
    private static final String SWITCH_FOUR = "switchFour";
    private static final String SWITCH_FIVE = "switchFive";
    private static final String SWITCH_SIX = "switchSix";

    private SessionManager(Context context){
        this.context = context;
        preferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = preferences.edit();
    }
    public static SessionManager getInstance(Context context){
        if(mInstance == null){
            mInstance = new SessionManager(context);
        }
        return mInstance;
    }


    public void setScreen(String val) {
        editor.putString(SCREEN, val);
        editor.commit();
    }

    public void setSwitchDE(Boolean val) {
        editor.putBoolean(SWITCH_DE, val);
        editor.commit();
    }

    public void setSwitchFR(Boolean val) {
        editor.putBoolean(SWITCH_FR, val);
        editor.commit();
    }

    public void setSwitchEN(Boolean val) {
        editor.putBoolean(SWITCH_EN, val);
        editor.commit();
    }

    public void setSwitchOne(Boolean val){
        editor.putBoolean(SWITCH_ONE, val);
        editor.commit();
    }

    public void setSwitchTwo(Boolean val){
        editor.putBoolean(SWITCH_TWO, val);
        editor.commit();
    }
    public void setSwitchThree(Boolean val){
        editor.putBoolean(SWITCH_THREE, val);
        editor.commit();
    }
    public void setSwitchFour(Boolean val){
        editor.putBoolean(SWITCH_FOUR, val);
        editor.commit();
    }
    public void setSwitchFive(Boolean val){
        editor.putBoolean(SWITCH_FIVE, val);
        editor.commit();
    }
    public void setSwitchSix(Boolean val){
        editor.putBoolean(SWITCH_SIX, val);
        editor.commit();
    }

    public void setSB(boolean val){
        editor.putBoolean(IS_SB, val);
        editor.commit();
    }

    public void setHB(boolean val){
        editor.putBoolean(IS_HB, val);
        editor.commit();
    }


    public boolean isSB(){
        return preferences.getBoolean(IS_SB, false);
    }
    public boolean isHB(){
        return preferences.getBoolean(IS_HB, false);
    }
    public boolean switchOne(){
        return preferences.getBoolean(SWITCH_ONE, true);
    }
    public boolean switchTwo(){
        return preferences.getBoolean(SWITCH_TWO, true);
    }
    public boolean switchThree(){
        return preferences.getBoolean(SWITCH_THREE, true);
    }
    public boolean switchFour(){
        return preferences.getBoolean(SWITCH_FOUR, true);
    }
    public boolean switchFive(){
        return preferences.getBoolean(SWITCH_FIVE, true);
    }
    public boolean switchSix(){
        return preferences.getBoolean(SWITCH_SIX, true);
    }
    public boolean switchDE(){
        return preferences.getBoolean(SWITCH_DE, true);
    }
    public boolean switchFR(){
        return preferences.getBoolean(SWITCH_FR, false);
    }
    public boolean switchEN(){
        return preferences.getBoolean(SWITCH_EN, false);
    }
    public String getScreen(){
        return preferences.getString(SCREEN, "MainActivity");
    }


}