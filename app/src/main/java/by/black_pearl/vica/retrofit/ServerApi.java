package by.black_pearl.vica.retrofit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by BLACK_Pearl.
 */

public interface ServerApi {

    @GET
    Call<ResponseBody> getOstRarArchive(@Url String ostUrl);
}
