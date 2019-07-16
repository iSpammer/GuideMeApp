package com.guideme.guideme.citys;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class City {

    public ArrayList<String> placeName = new ArrayList<>();

    // ...

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("placeName", placeName);

        return result;
    }
}