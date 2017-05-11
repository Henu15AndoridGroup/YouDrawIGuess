package me.cizezsy.yourdrawiguess;

import android.app.Application;

import me.cizezsy.yourdrawiguess.model.User;

public class MyApplication extends Application {
    private static User sUser;

    public final static String IS_FIRST_OPEN = "me.cizezsy.youdrawiguess.isfirstopen";

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static void setUser(User user) {
        sUser = user;
    }

    public static User getUser() {
        return sUser;
    }
}
