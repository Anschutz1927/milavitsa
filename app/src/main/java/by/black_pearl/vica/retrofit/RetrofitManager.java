package by.black_pearl.vica.retrofit;

import retrofit2.Retrofit;

/**
 * Created by BLACK_Pearl.
 */

public class RetrofitManager {

    public static Retrofit getRetrofit(String url){
        return new Retrofit.Builder().baseUrl(url).build();
    }
}
