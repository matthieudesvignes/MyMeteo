package iut.desvignes.mymeteo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by androidS4 on 19/03/18.
 */

public interface OpenWeatherService {
    @GET("forecast")
        Call<List<MeteoModel>> getProjects(@Query("id") int cityId, @Query("APPID") String apiKey);
}
