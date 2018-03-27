package iut.desvignes.mymeteo;

import java.io.Serializable;
import java.util.List;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import com.google.gson.annotations.SerializedName;

/**
 * Created by androidS4 on 13/03/18.
 */

public class MeteoModel implements Serializable {
    private static final long serialVersionUID = 1L;




    public String getName() { return nameCity; }

    public int getId(){ return this.idCity; }

    public Double getTemp(){ return this.main.temp;}

    public Double getLat(){ return this.coord.lat;}

    public Double getLon(){ return this.coord.lon;}

    public String getIcon(){ return this.weather.get(0).icon;}

    @SerializedName("id") Integer idCity;
    @SerializedName("name") String nameCity;

    @SerializedName("coord") Coord coord;
    static class Coord {
        @SerializedName("lat") Double lat;
        @SerializedName("lon") Double lon;
    }

    @SerializedName("main") Main main;
    static class Main {
        @SerializedName("temp") Double temp;
    }

    @SerializedName("weather")
    List<Weather> weather;
    static class Weather {
        @SerializedName("icon") String icon;
    }

    public static MeteoRoom createMeteoRoom(MeteoModel townRetrofit){
        MeteoRoom town = new MeteoRoom();
        town.setIconID(townRetrofit.getIcon());
        town.setId(townRetrofit.getId());
        town.setLat(townRetrofit.getLat());
        town.setLng(townRetrofit.getLon());
        town.setTemperature(townRetrofit.getTemp());
        town.setTownName(townRetrofit.getName());
        return town;
    }
}
