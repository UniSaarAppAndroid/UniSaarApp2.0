package com.example.uniapp_20.model;

import java.io.Serializable;

public class MealData implements Serializable {
    String mealName;
    MealComponents[] components;

    public String getMealName() {
        return mealName;
    }

    public void setMealName(String mealName) {
        this.mealName = mealName;
    }

    public MealComponents[] getComponents() {
        return this.components;
    }

    public void setComponents(MealComponents[] components) {
        this.components = components;
    }

}



