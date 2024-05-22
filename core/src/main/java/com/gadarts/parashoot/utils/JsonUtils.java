package com.gadarts.parashoot.utils;

import com.badlogic.gdx.utils.JsonValue;

/**
 * Created by Gad on 08/08/2016.
 */
public class JsonUtils {
    public static Object getBooleanOrStringArray(JsonValue json, String key) {
        Object value;
        try {
            value = json.getBoolean(key);
        } catch (Exception e) {
            try {
                value = json.get(key).asStringArray();
            } catch (Exception e2) {
                value = false;
            }
        }
        return value;
    }
}
