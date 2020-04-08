package com.example.seniorcare.utils;

import java.util.HashMap;

public class HashMapUtils {
    public static HashMap<String, String> toString(HashMap<String, Object> hashMap) {
        HashMap<String, String> result = new HashMap<>();

        for(String key : hashMap.keySet()) {
            Object value = hashMap.get(key);
            if (value == null) {
                result.put(key, "null");
                continue;
            }
            result.put(key, hashMap.get(key).toString());
        }

        return result;
    }
}
