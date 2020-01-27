package com.example.uniapp_20.model;

public class MealComponents{
    String componentName;
    Notice[] notices;

    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public Notice[] getNotices() {
        return this.notices;
    }

    public void setNotices(Notice[] notices) {
        this.notices = notices;
    }


}

