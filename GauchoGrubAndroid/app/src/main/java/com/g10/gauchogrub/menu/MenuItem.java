package com.g10.gauchogrub.menu;
import java.util.Hashtable;
import java.util.ArrayList;
import java.util.HashMap;


public class MenuItem {
    private String title;
    private String menuCategory;
    private String menuItemType;

    public MenuItem(String title,String menuCategory, String menuItemType){
        this.title = title;
        this.menuCategory = menuCategory;
        this.menuItemType = menuItemType;
    }

    public String getTitle(){ return this.title; }

    public String getMenuCategory(){ return this.menuCategory; }

    public String getMenuItemType(){ return this.menuItemType; }

    public String toString(){
        String itemString = menuCategory + ": " + title + " (" + menuItemType + ")";

        return itemString;
    }
}