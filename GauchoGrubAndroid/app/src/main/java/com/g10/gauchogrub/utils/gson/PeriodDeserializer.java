package com.g10.gauchogrub.utils.gson;

import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class PeriodDeserializer implements JsonDeserializer<Period> {
    @Override
    public Period deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        PeriodFormatter hh_mm_ss = new PeriodFormatterBuilder()
                .appendHours()
                .appendSuffix(":")
                .appendMinutes()
                .appendSuffix(":")
                .appendSeconds()
                .toFormatter();
        return hh_mm_ss.parsePeriod(json.getAsString());
    }
}