package me.cizezsy.yourdrawiguess.util;

import com.google.gson.Gson;

public class JsonUtils {

    private static Gson sGson = new Gson();

    public static String toJson(Object o) {
        return sGson.toJson(o);
    }

    public static <T> T fromJson(String data, Class<T> c) {
        return sGson.fromJson(data, c);
    }
}
