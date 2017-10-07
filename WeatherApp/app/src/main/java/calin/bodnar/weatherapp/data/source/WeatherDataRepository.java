package calin.bodnar.weatherapp.data.source;

import android.support.annotation.NonNull;

import calin.bodnar.weatherapp.data.model.WeatherData;
import calin.bodnar.weatherapp.data.source.remote.WeatherRemoteDataSource;
import io.reactivex.Flowable;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Concrete implementation to load weather data.
 */
public class WeatherDataRepository implements WeatherDataSource {

    @NonNull
    private final WeatherRemoteDataSource weatherRemoteDataSource;

    public WeatherDataRepository(@NonNull WeatherRemoteDataSource weatherRemoteDataSource) {
        this.weatherRemoteDataSource = checkNotNull(weatherRemoteDataSource);
    }

    @Override
    public Flowable<WeatherData> getWeatherData(String cityIds, String unit) {
        return weatherRemoteDataSource
                .getWeatherData(cityIds, unit);
    }
}
