package by.black_pearl.vica.retrofit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by BLACK_Pearl.
 */

public interface ServerApi {

    @GET("/{repos}")
    Call<GeoObjectGson> getGeoObject(
            @Path("repos") String repos,
            @Query("geocode") String address,
            @Query("format") String format,
            @Query("results") int resultsCount,
            @Query("skip") int skip,
            @Query("lang") String lang
    );

    @GET("/{repos}")
    Call<ResponseBody> getGeoInfo(
            @Path("repos") String repos,
            @Query("geocode") String address,
            @Query("format") String format,
            @Query("results") int resultsCount,
            @Query("skip") int skip,
            @Query("lang") String lang
    );
}
