package com.g10.gauchogrub.menu;

public class MenuItemType extends NamedEnum {
    public String getShortVersion(){
        if(this.name.equals("Vegetarian")){
            return "(v)";
        } else if(this.name.equals("Vegan")){
            return "(vgn)";
        } else {
            return "";
        }
    }
}
