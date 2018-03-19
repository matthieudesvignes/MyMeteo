package iut.desvignes.mymeteo;

import java.io.Serializable;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import com.google.gson.annotations.SerializedName;

/**
 * Created by androidS4 on 13/03/18.
 */
@Entity(tableName = "town_table")
public class MeteoModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @PrimaryKey()
    private int id;

    @SerializedName("name") private String townName;
    @SerializedName("main.temp") private double temperature;
    @SerializedName("weather.icon") private String iconID; // ensoleillé, pluvieux etc à voir en fonction de l'API


    public String getTownName() {
        return townName;
    }
    public void setTownName(String townName) {
        this.townName = townName;
    }

    public double getTemperature() {
        return temperature;
    }
    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public String getIconID() {
        return iconID;
    }
    public void setIconID(String weather) {
        this.iconID = weather;
    }

    public int getId(){ return this.id; }
    public void setId(int id) { this.id = id; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MeteoModel that = (MeteoModel) o;

        if (temperature != that.temperature) return false;
        if (!townName.equals(that.townName)) return false;
        return iconID.equals(that.iconID);
    }


}
