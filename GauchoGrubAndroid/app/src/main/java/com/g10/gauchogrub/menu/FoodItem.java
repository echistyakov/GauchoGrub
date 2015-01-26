package com.g10.gauchogrub.menu;

public class FoodItem {
    private String name = "";
    private FoodType f;

    public FoodItem(String name, FoodType f){
        this.name = name;
        this.f = f;
    }

    public String getName(){
        return name;
    }

    public FoodType getFoodType(){
        return f;
    }

}