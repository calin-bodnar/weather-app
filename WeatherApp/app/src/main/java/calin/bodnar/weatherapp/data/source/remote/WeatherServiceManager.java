package calin.bodnar.weatherapp.data.source.remote;

import calin.bodnar.weatherapp.data.model.WeatherData;
import io.reactivex.Flowable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;


public class WeatherServiceManager {
    private static final String BASE_URL = "http://api.openweathermap.org";
    private static final String API_ID = "72f0daa805673109faeb5dedc077ec94";

    private static WeatherServiceManager INSTANCE;
    private WeatherService weatherService;

    static WeatherServiceManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new WeatherServiceManager();
        }
        return INSTANCE;
    }

    private WeatherServiceManager() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        weatherService = retrofit.create(WeatherService.class);
    }

    WeatherService getService() {
        return weatherService;
    }

    interface WeatherService {
        @GET("/data/2.5/group?appid=" + API_ID)
        Flowable<WeatherData> getWeatherData(@Query("id") String ids, @Query("units") String unit);
    }
}
