
package calin.bodnar.weatherapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WeatherMain implements Parcelable {

    @SerializedName("temp")
    @Expose
    private double temp;
    @SerializedName("pressure")
    @Expose
    private double pressure;
    @SerializedName("humidity")
    @Expose
    private double humidity;
    @SerializedName("temp_min")
    @Expose
    private double tempMin;
    @SerializedName("temp_max")
    @Expose
    private double tempMax;

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public double getTempMin() {
        return tempMin;
    }

    public void setTempMin(double tempMin) {
        this.tempMin = tempMin;
    }

    public double getTempMax() {
        return tempMax;
    }

    public void setTempMax(double tempMax) {
        this.tempMax = tempMax;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.temp);
        dest.writeDouble(this.pressure);
        dest.writeDouble(this.humidity);
        dest.writeDouble(this.tempMin);
        dest.writeDouble(this.tempMax);
    }

    public WeatherMain() {
    }

    protected WeatherMain(Parcel in) {
        this.temp = in.readDouble();
        this.pressure = in.readDouble();
        this.humidity = in.readDouble();
        this.tempMin = in.readDouble();
        this.tempMax = in.readDouble();
    }

    public static final Parcelable.Creator<WeatherMain> CREATOR = new Parcelable.Creator<WeatherMain>() {
        @Override
        public WeatherMain createFromParcel(Parcel source) {
            return new WeatherMain(source);
        }

        @Override
        public WeatherMain[] newArray(int size) {
            return new WeatherMain[size];
        }
    };
}
