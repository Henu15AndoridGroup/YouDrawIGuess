package me.cizezsy.yourdrawiguess.net;

public class YdigRetrofitFactory {

    private static YdigRetrofitApi mService;

    private YdigRetrofitFactory() {}

    public static YdigRetrofitApi getService() {
        if(mService == null)
            mService = new YdigRetrofit().getService();
        return mService;
    }
}
