package me.cizezsy.yourdrawiguess.util;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.lang.reflect.Type;

public class JsonUtils {

    private static Gson sGson = new Gson();

    public static String toJson(Object o) {
        return sGson.toJson(o);
    }

    public static <T> T fromJson(String data, Class<T> c) {
        return sGson.fromJson(data, c);
    }

    public static <T> T fromJson(String data, Type type) {
        return sGson.fromJson(data, type);
    }

    public static <T> T fromJson(JsonElement json, Class<T> c){
        return sGson.fromJson(json, c);
    }

    public static <T> T fromJson(JsonElement json, Type type) {
        return sGson.fromJson(json, type);
    }
}
