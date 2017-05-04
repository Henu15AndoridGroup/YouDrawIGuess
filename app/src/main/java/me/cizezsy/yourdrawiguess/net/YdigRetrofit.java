package me.cizezsy.yourdrawiguess.net;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class YdigRetrofit {
    private final YdigRetrofitApi mService;

    public static final String BASE_URL = "http://115.159.49.186:8080/ydig2/";
    public static final List<Cookie> cookieStore = new ArrayList<>();

    public YdigRetrofit() {
        OkHttpClient mHttpClient = new OkHttpClient.Builder().
                connectTimeout(8, TimeUnit.SECONDS)
                .cookieJar(new CookieJar() {

                    @Override
                    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                        cookieStore.addAll(cookies);
                    }

                    @Override
                    public List<Cookie> loadForRequest(HttpUrl url) {
                        return cookieStore;
                    }
                }).build();

        Retrofit retrofit = new Retrofit.Builder()
                .client(mHttpClient)
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        mService = retrofit.create(YdigRetrofitApi.class);
    }

    public YdigRetrofitApi getService() {
        return mService;
    }

}
