package com.guideme.guideme.helpers;

import android.content.Context;
import android.content.SharedPreferences;

import java.lang.reflect.Type;
import java.util.List;

public class CustomSharedPreference {

    private SharedPreferences sharedPref;

//    private Gson gson;
//
//    public CustomSharedPreference(Context context) {
//        sharedPref = context.getSharedPreferences(Helper.PREFS_TAG, Context.MODE_PRIVATE);
//        gson = new Gson();
//    }
//
//    public void setDataFromSharedPreferences(String key, List<ListJsonObject> listObjects) {
//        String json = gson.toJson(listObjects);
//        sharedPref.edit().putString(key, json).apply();
//    }
//
//    public List<ListJsonObject> getAllDataObject(String key){
//        String stringObjects = sharedPref.getString(key, "");
//        Type type = new TypeToken<List<ListJsonObject>>(){}.getType();
//        return gson.fromJson(stringObjects, type);
//    }

    public void setDataSourceIfPresent(boolean isData){
        sharedPref.edit().putBoolean(Helper.IS_DATA_PRESENT, isData).apply();
    }

    public boolean getDataSourceIfPresent(){
        return sharedPref.getBoolean(Helper.IS_DATA_PRESENT, false);
    }

    public void setLocationInPreference(String cityName){
        sharedPref.edit().putString(Helper.LOCATION_PREFS, cityName).apply();
    }

    public String getLocationInPreference(){
        return sharedPref.getString(Helper.LOCATION_PREFS, "");
    }
}
