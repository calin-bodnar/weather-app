
package calin.bodnar.weatherapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WeatherEntity implements Parcelable {

    @Expose
    private List<Weather> weather = null;
    @SerializedName("main")
    @Expose
    private WeatherMain weatherMain;
    @SerializedName("visibility")
    @Expose
    private double visibility;
    @SerializedName("wind")
    @Expose
    private Wind wind;
    @Expose
    private int dt;
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("name")
    @Expose
    private String name;

    public WeatherEntity(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public boolean hasWeather() {
        return weather != null && !weather.isEmpty();
    }

    public Weather getWeather() {
        if (hasWeather()) {
            return weather.get(0);
        }
        return null;
    }

    public void setWeather(List<Weather> weather) {
        this.weather = weather;
    }

    public WeatherMain getWeatherMain() {
        return weatherMain;
    }

    public void setWeatherMain(WeatherMain weatherMain) {
        this.weatherMain = weatherMain;
    }

    public double getVisibility() {
        return visibility;
    }

    public void setVisibility(double visibility) {
        this.visibility = visibility;
    }

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public int getDt() {
        return dt;
    }

    public void setDt(int dt) {
        this.dt = dt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.weather);
        dest.writeParcelable(this.weatherMain, flags);
        dest.writeDouble(this.visibility);
        dest.writeParcelable(this.wind, flags);
        dest.writeInt(this.dt);
        dest.writeInt(this.id);
        dest.writeString(this.name);
    }

    protected WeatherEntity(Parcel in) {
        this.weather = in.createTypedArrayList(Weather.CREATOR);
        this.weatherMain = in.readParcelable(WeatherMain.class.getClassLoader());
        this.visibility = in.readDouble();
        this.wind = in.readParcelable(Wind.class.getClassLoader());
        this.dt = in.readInt();
        this.id = in.readInt();
        this.name = in.readString();
    }

    public static final Parcelable.Creator<WeatherEntity> CREATOR = new Parcelable.Creator<WeatherEntity>() {
        @Override
        public WeatherEntity createFromParcel(Parcel source) {
            return new WeatherEntity(source);
        }

        @Override
        public WeatherEntity[] newArray(int size) {
            return new WeatherEntity[size];
        }
    };
}
