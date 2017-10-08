
package calin.bodnar.weatherapp.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WeatherData {

    @SerializedName("cnt")
    @Expose
    private int cnt;
    @SerializedName("list")
    @Expose
    private List<WeatherEntity> weatherEntities = null;

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    public List<WeatherEntity> getWeatherEntities() {
        return weatherEntities;
    }

    public void setWeatherEntities(List<WeatherEntity> weatherEntities) {
        this.weatherEntities = weatherEntities;
    }

    public boolean isEmpty() {
        return weatherEntities == null || weatherEntities.isEmpty();
    }

}
