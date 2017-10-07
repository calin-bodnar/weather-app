package calin.bodnar.weatherapp.data.source.remote;

import calin.bodnar.weatherapp.data.model.WeatherData;
import calin.bodnar.weatherapp.data.source.WeatherDataSource;
import io.reactivex.Flowable;


public class WeatherRemoteDataSource implements WeatherDataSource {

    @Override
    public Flowable<WeatherData> getWeatherData(String cityIds, String unit) {
        return WeatherServiceManager.getInstance()
                .getService()
                .getWeatherData(cityIds, unit);
    }

}
