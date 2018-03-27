package iut.desvignes.mymeteo;

import android.util.Log;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by androidS4 on 19/03/18.
 */

public class Repository {

    private OpenWeatherService service;

    public Repository(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(OpenWeatherService.class);
}

    public MeteoModel getTownByName(String townName) {
        try {
            Call<MeteoModel> call = service.getTownByName(townName);
            Response<MeteoModel> response = call.execute();
            Log.i("Message", response.toString());
            return response.body();
        } catch (IOException e) {
            return null;
        }
    }

}
