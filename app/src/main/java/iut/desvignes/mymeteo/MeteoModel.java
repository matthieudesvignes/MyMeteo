package iut.desvignes.mymeteo;

import java.io.Serializable;

/**
 * Created by androidS4 on 13/03/18.
 */

public class MeteoModel implements Serializable {
    private static final long serialVersionUID = 1L;

    private String townName;
    private int temperature;
    private String weather; // ensoleillé, pluvieux etc à voir en fonction de l'API

    public MeteoModel(String townName){
        this.townName = townName;
        this.temperature = 42;
        this.weather = "Soleil";
    }

    public String getTownName() {
        return townName;
    }

    public void setTownName(String townName) {
        this.townName = townName;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }
}
