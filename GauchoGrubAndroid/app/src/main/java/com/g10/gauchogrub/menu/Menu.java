package com.g10.gauchogrub.menu;
import java.util.ArrayList;


public class Menu {
    private String menuName;
    private ArrayList<MenuItem> menuItems;

    public Menu(String menuName) {
        this.menuName = menuName;
        menuItems = new ArrayList<MenuItem>();
    }

    public MenuItem getMenuItem() {
        return menuItems.get(0);
    }

    public String getMenuName() {
        return this.menuName;
    }

    public ArrayList<MenuItem> getMenuItems() { return menuItems; }

    public void addMenuItem(MenuItem item) {
        menuItems.add(item);
    }

    public String toString() {
        String menuString;
        menuString = menuName + ":\n";

        for(MenuItem item : menuItems) {
            menuString = menuString + item.toString();
            menuString = menuString + "\n";
        }

        return menuString;
    }
}