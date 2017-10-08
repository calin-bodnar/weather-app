package calin.bodnar.weatherapp.weather;

import com.google.common.base.Joiner;
import com.google.common.collect.Collections2;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collection;
import java.util.List;

import calin.bodnar.weatherapp.data.model.WeatherData;
import calin.bodnar.weatherapp.data.model.WeatherEntity;
import calin.bodnar.weatherapp.data.source.WeatherDataRepository;
import calin.bodnar.weatherapp.util.schedulers.BaseSchedulerProvider;
import calin.bodnar.weatherapp.util.schedulers.ImmediateSchedulerProvider;
import io.reactivex.Flowable;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class WeatherPresenterTest {

    private static WeatherData WEATHER_DATA;

    @Mock
    private WeatherDataRepository weatherDataRepository;

    @Mock
    private WeatherContract.View weatherView;

    private BaseSchedulerProvider schedulerProvider;

    private WeatherPresenter weatherPresenter;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        schedulerProvider = new ImmediateSchedulerProvider();
        weatherPresenter = new WeatherPresenter(weatherDataRepository, weatherView, schedulerProvider);

        List<WeatherEntity> weatherEntities = weatherPresenter.getDefaultWeatherCities();
        WEATHER_DATA = new WeatherData();
        WEATHER_DATA.setCnt(weatherEntities.size());
        WEATHER_DATA.setWeatherEntities(weatherEntities);
    }

    @After
    public void tearDown() throws Exception {
        WEATHER_DATA = null;
    }

    @Test
    public void createPresenter_setsThePresenterToView() {
        weatherPresenter = new WeatherPresenter(weatherDataRepository, weatherView, schedulerProvider);
        verify(weatherView).setPresenter(weatherPresenter);
    }

    @Test
    public void loadWeatherDataAndPopulateView() throws Exception {
        when(weatherDataRepository.getWeatherData(getCityIds(), "metric")).thenReturn(Flowable.just(WEATHER_DATA));
        weatherPresenter.loadWeatherData();
        verify(weatherView).setLoadingIndicator(true);
        verify(weatherView).showWeatherInfo(WEATHER_DATA.getWeatherEntities());
        verify(weatherView).setLoadingIndicator(false);
    }

    @Test
    public void errorLoadingWeatherData_ShowsError() {
        when(weatherDataRepository.getWeatherData(getCityIds(), "metric")).thenReturn(Flowable.error(new Exception()));
        weatherPresenter.loadWeatherData();
        verify(weatherView).showNoWeatherInfo();
    }

    @Test
    public void clickOnCity_ShowsDetailsUi() {
        WeatherEntity weatherEntity = new WeatherEntity(2759794, "Amsterdam");
        weatherPresenter.openWeatherDetails(weatherEntity);
        verify(weatherView).showWeatherDetailsUi(any(WeatherEntity.class));
    }

    @Test
    public void multipleSubscribesIfDataLoaded_DoesNothing() {
        when(weatherDataRepository.getWeatherData(getCityIds(), "metric")).thenReturn(Flowable.just(WEATHER_DATA));
        weatherPresenter.subscribe();
        weatherPresenter.subscribe();
        verify(weatherView, times(1)).showWeatherInfo(WEATHER_DATA.getWeatherEntities());
    }

    @Test
    public void multipleSubscribesIfDataNotLoaded_ReloadsData() {
        when(weatherDataRepository.getWeatherData(getCityIds(), "metric")).thenReturn(Flowable.error(new Exception())).thenReturn(Flowable.just(WEATHER_DATA));
        weatherPresenter.subscribe();
        weatherPresenter.subscribe();
        verify(weatherView, times(1)).showWeatherInfo(WEATHER_DATA.getWeatherEntities());
    }

    private String getCityIds() {
        Collection cityWeatherCollection = Collections2.transform(WEATHER_DATA.getWeatherEntities(), WeatherEntity::getId);
        return Joiner.on(",").join(cityWeatherCollection);
    }

}