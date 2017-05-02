package me.cizezsy.yourdrawiguess.net;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class YdigRetrofit {
    private final YdigRetrofitApi mService;

    private static final String BASE_URL = "http://115.159.49.186:8080/ydig2/";

    public YdigRetrofit() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(8, TimeUnit.SECONDS);

        Retrofit retrofit = new Retrofit.Builder()
                .client(builder.build())
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
