package net.bridgeint.app.network;

import net.bridgeint.app.responces.packages.ApplyResponse;
import net.bridgeint.app.responces.packages.FreepackagesResponse;
import net.bridgeint.app.utils.Config;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WebServiceHelper {

    private static final long READ_TIMEOUT = 90;
    private static final long CONNECT_TIMEOUT = READ_TIMEOUT;
    private static String BASE_URL = Config.retrofit_base_url;
    private static Retrofit mRetrofit = null;

    private static OkHttpClient getClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        return new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .build();
    }

    private static Retrofit getRetrofit() {
        if (null == mRetrofit) {
            mRetrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(getClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return mRetrofit;
    }

    public static Call<FreepackagesResponse> getFreepackageCall() {
        return getRetrofit().create(UserInterfaces.class)
                .getFreepackage();
    }

    public static Call<ApplyResponse> applyfreepackageCall(String userId,String accessKey,String apiKey,
        String countryId,String countryName,String universityId,String universityName,
                                                           String doument,String doumentType,String comment){
        return getRetrofit().create(UserInterfaces.class)
                .applyfreepackage(userId,accessKey,apiKey,countryId,countryName,universityId,universityName,doument,doumentType,comment);
    }





}
