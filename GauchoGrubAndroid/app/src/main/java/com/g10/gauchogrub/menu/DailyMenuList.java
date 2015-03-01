package com.g10.gauchogrub.menu;
import java.util.ArrayList;
import com.google.gson.annotations.SerializedName;


public class DailyMenuList {
    //@SerializedName("Date")
    private String date;
    //@SerializedName("DiningCommon")
    private String diningCommon;
    //@SerializedName("DayOfWeek")
    private int day;

    public ArrayList<Menu> Menus;

    public DailyMenuList(String date, String diningCommon, int day) {
        Menus = new ArrayList<Menu>();
        setDate(date);
        setDiningcommon(diningCommon);
        setDay(day);
    }

    public DailyMenuList(){
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

    public ArrayList<Menu> getMenus() { return Menus; }

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

    public void addMenu(Menu Menu) {
        Menus.add(Menu);
    }

    public String toString(){
        String DailyMenuListString;

        ArrayList<String> days = new ArrayList<String>();
        days.add("Sunday");
        days.add("Monday");
        days.add("Tuesday");
        days.add("Wednesday");
        days.add("Thursday");
        days.add("Friday");
        days.add("Saturday");


        DailyMenuListString = diningCommon + " Menu: " + days.get(day) + " " + date;
        DailyMenuListString = DailyMenuListString + "\n";
        for(Menu menu : Menus){
            DailyMenuListString = DailyMenuListString + menu.toString();
        }

        return DailyMenuListString;
    }
}