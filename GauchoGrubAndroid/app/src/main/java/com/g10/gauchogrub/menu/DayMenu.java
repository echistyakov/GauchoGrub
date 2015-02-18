package com.g10.gauchogrub.menu;
import java.util.Hashtable;
import java.util.ArrayList;
import java.util.HashMap;


public class DayMenu {
    private String date;
    private String diningCommon;
    private int day;
    ArrayList<Meal> meals;

    public DayMenu(String date, String diningCommon, int day) {
        meals = new ArrayList<Meal>();
        setDate(date);
        setDiningcommon(diningCommon);
        setDay(day);
    }

    public DayMenu(){
        date = "";
        diningCommon = "";
        day = 0;
    }

    //getters

    public String getDate(){
        return date;
    }

    public String getDiningCommon() {
        return this.diningCommon;
    }

    public ArrayList<Meal> getMeals() { return meals; }

    //setters

    public void setDate(String date){
        this.date = date;
    }

    public void setDiningcommon(String diningCommon){
        this.diningCommon = diningCommon;
    }

    public void setDay(int day){
        this.day = day;
    }

    public void setData(String date, String diningCommon, int day){
        this.date = date;
        this.diningCommon = diningCommon;
        this.day = day;
    }


    //Methods

    public void addMeal(Meal meal) {
        meals.add(meal);
    }

    public String toString(){
        String dayMenuString;

        ArrayList<String> days = new ArrayList<String>();
        days.add("Sunday");
        days.add("Monday");
        days.add("Tuesday");
        days.add("Wednesday");
        days.add("Thursday");
        days.add("Friday");
        days.add("Saturday");


        dayMenuString = diningCommon + " Menu: " + days.get(day) + " " + date;
        dayMenuString = dayMenuString + "\n";
        for(Meal meal : meals){
            dayMenuString = dayMenuString + meal.toString();
        }

        return dayMenuString;
    }
}