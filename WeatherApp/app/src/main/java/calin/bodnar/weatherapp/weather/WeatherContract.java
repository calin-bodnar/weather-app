package calin.bodnar.weatherapp.weather;

import android.widget.ImageView;

import java.util.List;

import calin.bodnar.weatherapp.BasePresenter;
import calin.bodnar.weatherapp.BaseView;
import calin.bodnar.weatherapp.data.model.WeatherEntity;

/**
 * This specifies the contract between the view and the presenter.
 */
public class WeatherContract {
    interface View extends BaseView<Presenter> {

        void setLoadingIndicator(boolean active);

        void showWeatherInfo(List<WeatherEntity> weatherEntities);

        void showNoWeatherInfo();
    }

    interface Presenter extends BasePresenter {

        List<WeatherEntity> getDefaultWeatherCities();

        void loadWeatherData();

    }
}
