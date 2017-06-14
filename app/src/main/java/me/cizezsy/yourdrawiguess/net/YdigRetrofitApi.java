package me.cizezsy.yourdrawiguess.net;


import me.cizezsy.yourdrawiguess.model.message.UsedForHttpMessage;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

public interface YdigRetrofitApi {
    @FormUrlEncoded
    @POST("signup")
    Observable<UsedForHttpMessage> getVerificationCode(@Field("phone") String phone);

    @FormUrlEncoded
    @POST("signup")
    Observable<UsedForHttpMessage> signUp(@Field("username") String username,
                                          @Field("password") String password,
                                          @Field("phone") String phone,
                                          @Field("verificationCode") String verificationCode);

    @FormUrlEncoded
    @POST("login")
    Observable<UsedForHttpMessage> login(@Field("phone") String phone, @Field("password") String password);

    @GET("room/list")
    Observable<UsedForHttpMessage> roomList();

    @GET("room/enter")
    Observable<UsedForHttpMessage> enterRoom(@Query("roomId") int roomId);
}
