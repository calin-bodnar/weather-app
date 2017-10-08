package calin.bodnar.weatherapp.weather;

import android.support.annotation.NonNull;

import com.google.common.base.Joiner;
import com.google.common.collect.Collections2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import calin.bodnar.weatherapp.data.model.WeatherData;
import calin.bodnar.weatherapp.data.model.WeatherEntity;
import calin.bodnar.weatherapp.data.source.WeatherDataRepository;
import calin.bodnar.weatherapp.util.EspressoIdlingResource;
import calin.bodnar.weatherapp.util.schedulers.BaseSchedulerProvider;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * Listens to user actions from the UI, retrieves the data and updates the UI as required.
 */
public class WeatherPresenter implements WeatherContract.Presenter {
    private static final List<WeatherEntity> WEATHER_ENTITIES = new ArrayList<WeatherEntity>() {{
        add(new WeatherEntity(2759794, "Amsterdam"));
        add(new WeatherEntity(2950159, "Berlin"));
        add(new WeatherEntity(2643743, "London"));
        add(new WeatherEntity(3117735, "Madrid"));
        add(new WeatherEntity(2990440, "Nice"));
        add(new WeatherEntity(6453366, "Oslo"));
        add(new WeatherEntity(6455259, "Paris"));
        add(new WeatherEntity(3067696, "Prague"));
        add(new WeatherEntity(2673730, "Stockholm"));
        add(new WeatherEntity(3186886, "Zagreb"));
    }};
    private static final String UNITS = "metric";

    @NonNull
    private final WeatherDataRepository weatherDataRepository;

    @NonNull
    private final WeatherContract.View weatherView;

    @NonNull
    private final BaseSchedulerProvider schedulerProvider;

    @NonNull
    private CompositeDisposable compositeDisposable;

    private boolean hasData = false;

    public WeatherPresenter(@NonNull WeatherDataRepository weatherDataRepository,
                            @NonNull WeatherContract.View weatherView,
                            @NonNull BaseSchedulerProvider schedulerProvider) {
        this.weatherDataRepository = checkNotNull(weatherDataRepository, "weatherDataRepository cannot be null");
        this.weatherView = checkNotNull(weatherView, "weatherView cannot be null!");
        this.schedulerProvider = checkNotNull(schedulerProvider, "schedulerProvider cannot be null");

        compositeDisposable = new CompositeDisposable();
        weatherView.setPresenter(this);
    }

    @Override
    public List<WeatherEntity> getDefaultWeatherCities() {
        return WEATHER_ENTITIES;
    }

    @Override
    public void loadWeatherData() {
        weatherView.setLoadingIndicator(true);

        // The network request might be handled in a different thread so make sure Espresso knows
        // that the app is busy until the response is handled.
        EspressoIdlingResource.increment(); // App is busy until further notice

        Collection cityWeatherCollection = Collections2.transform(WEATHER_ENTITIES, WeatherEntity::getId);
        String cityIds = Joiner.on(",").join(cityWeatherCollection);

        compositeDisposable.clear();
        Disposable disposable = weatherDataRepository
                .getWeatherData(cityIds, UNITS)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .doFinally(() -> {
                    if (!EspressoIdlingResource.getIdlingResource().isIdleNow()) {
                        EspressoIdlingResource.decrement(); // Set app as idle.
                    }
                })
                .subscribe(
                        // onNext
                        weatherData -> {
                            processWeatherData(weatherData);
                            weatherView.setLoadingIndicator(false);
                        },
                        // onError
                        throwable -> {
                            weatherView.showNoWeatherInfo();
                            weatherView.setLoadingIndicator(false);
                        });

        compositeDisposable.add(disposable);
    }

    @Override
    public void openWeatherDetails(@NonNull WeatherEntity weatherEntity) {
        checkNotNull(weatherEntity, "weatherEntity cannot be null!");
        weatherView.showWeatherDetailsUi(weatherEntity);
    }

    @Override
    public void subscribe() {
        if (!hasData) {
            loadWeatherData();
        }
    }

    @Override
    public void unsubscribe() {
        compositeDisposable.clear();
    }

    private void processWeatherData(@NonNull WeatherData weatherData) {
        if (weatherData.isEmpty()) {
            weatherView.showNoWeatherInfo();
        } else {
            hasData = true;
            weatherView.showWeatherInfo(weatherData.getWeatherEntities());
        }
    }
}
