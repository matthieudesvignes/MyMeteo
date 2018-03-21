package iut.desvignes.mymeteo;

import java.io.Serializable;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import com.google.gson.annotations.SerializedName;

/**
 * Created by androidS4 on 13/03/18.
 */

public class MeteoModel implements Serializable {
    private static final long serialVersionUID = 1L;


     String name;
     int id;
     Main main;
     Weather weather;
     Coord coord;


    static class Main{
        Double temp;
    }

    static class Weather{
        String icon;
    }

    static class Coord{
        Double lon;
        Double lat;
    }

    public String getName() { return name; }

    public int getId(){ return this.id; }

    public Double getTemp(){ return this.main.temp;}

    public Double getLat(){ return this.coord.lat;}

    public Double getLon(){ return this.coord.lon;}

    public String getIcon(){ return this.weather.icon;}
}
