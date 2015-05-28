package com.qualifes.app.util;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class JsonUtil {
    private static JsonUtil inst = new JsonUtil();

    public static JsonUtil getInstance() {
        return inst;
    }

    public JSONArray removeJsonArray(JSONArray data, int position) {
        try {
            JSONArray list = new JSONArray();
            JSONArray jsonArray = data;
            int len = jsonArray.length();
            if (jsonArray != null) {
                for (int i = 0; i < len; i++) {
                    //Excluding the item at position
                    if (i != position) {
                        list.put(jsonArray.get(i));
                    }
                }
            }
            return list;
        } catch (JSONException e) {
            return data;
        }
    }
}
