package calin.bodnar.weatherapp.data.source;

import calin.bodnar.weatherapp.data.model.WeatherData;
import io.reactivex.Flowable;


public interface WeatherDataSource {

    Flowable<WeatherData> getWeatherData(String cityIds, String unit);

}
