package com.g10.gauchogrub.menu;

import com.google.gson.annotations.SerializedName;

import org.joda.time.Period;

public class RepeatedEvent {
    @SerializedName("DayOfWeek")
    public String dayOfWeek;
    @SerializedName("DiningCommon")
    public DiningCommon diningCommon;
    @SerializedName("Meal")
    public Meal meal;
    @SerializedName("From")
    public Period from;
    @SerializedName("To")
    public Period to;

}
