package com.g10.gauchogrub.menu;

import com.google.gson.annotations.SerializedName;

public class MenuItem {
    @SerializedName("Title")
    public String title;
    @SerializedName("MenuCategory")
    public MenuCategory menuCategory;
    @SerializedName("MenuItemType")
    public MenuItemType menuItemType;
}