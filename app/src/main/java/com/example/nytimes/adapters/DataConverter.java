package com.example.nytimes.adapters;

import androidx.room.TypeConverter;

import com.example.nytimes.data.Multimedia;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class DataConverter {

    @TypeConverter // note this annotation
    public String fromOptionValuesList(List<Multimedia> optionValues) {
        if (optionValues == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Multimedia>>() {
        }.getType();
        String json = gson.toJson(optionValues, type);
        return json;
    }

    @TypeConverter // note this annotation
    public List<Multimedia> toOptionValuesList(String optionValuesString) {
        if (optionValuesString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Multimedia>>() {
        }.getType();
        List<Multimedia> multimediaList = gson.fromJson(optionValuesString, type);
        return multimediaList;
    }
}
