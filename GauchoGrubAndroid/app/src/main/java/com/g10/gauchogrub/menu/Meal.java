package com.g10.gauchogrub.menu;
import java.util.Hashtable;
import java.util.ArrayList;
import java.util.HashMap;


public class Meal {
    private String mealName;
    private ArrayList<MenuItem> menuItems;

    public Meal(String mealName) {
        this.mealName = mealName;
        menuItems = new ArrayList<MenuItem>();
    }

    public MenuItem getMenuItem() {
        return menuItems.get(0);
    }

    public String getMealName() {
        return this.mealName;
    }

    public ArrayList<MenuItem> getMenuItems() { return menuItems; }

    public void addMenuItem(MenuItem item) {
        menuItems.add(item);
    }

    public String toString() {
        String mealString;
        mealString = mealName + ":\n";

        for(MenuItem item : menuItems) {
            mealString = mealString + item.toString();
            mealString = mealString + "\n";
        }

        return mealString;
    }
}