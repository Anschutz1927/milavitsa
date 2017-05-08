package by.black_pearl.vica.retrofit;

import java.util.ArrayList;

import by.black_pearl.vica.realm_db.ShopsCoordinatesDb;
import io.realm.RealmList;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by BLACK_Pearl.
 */

public class RetrofitManager {

    public static Retrofit getRetrofit(String url) {
        return new Retrofit.Builder().baseUrl(url).build();
    }

    public static Retrofit getGsonRetrofit(String url) {
        return new Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build();
    }

    public interface RetrofitApi {

        @GET("/{repos}")
        Call<ResponseBody> getShopsCoordinates(@Path(value = "repos", encoded = true) String repos,
                                               @Query("pageSize") int pageSize, @Query("offset") int offset);

        @GET("/{repos}")
        Call<ArrayList<ShopsCoordinatesDb>> getArrayGsonShopsCoordinates(@Path(value = "repos", encoded = true) String repos,
                                                                    @Query("pageSize") int pageSize, @Query("offset") int offset);

        @GET("/{repos}")
        Call<RealmList<ShopsCoordinatesDb>> getRealmListGsonShopsCoordinates(@Path(value = "repos", encoded = true) String repos,
                                                                              @Query("pageSize") int pageSize, @Query("offset") int offset);
    }
}
