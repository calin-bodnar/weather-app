package calin.bodnar.weatherapp.weather;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import calin.bodnar.weatherapp.R;
import calin.bodnar.weatherapp.data.model.WeatherEntity;
import calin.bodnar.weatherapp.data.model.WeatherMain;
import calin.bodnar.weatherapp.data.source.WeatherDataRepository;
import calin.bodnar.weatherapp.data.source.remote.WeatherRemoteDataSource;
import calin.bodnar.weatherapp.util.schedulers.SchedulerProvider;

import static com.google.common.base.Preconditions.checkNotNull;

public class WeatherActivity extends AppCompatActivity implements WeatherContract.View {
    private static final String WEATHER_ICON_URL = "http://openweathermap.org/img/w/%s.png";

    private WeatherPresenter weatherPresenter;
    private WeatherListAdapter listAdapter;

    WeatherItemListener itemListener = weatherEntity -> {
        // TODO: 08/10/2017 Open details activity
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        weatherPresenter = new WeatherPresenter(
                new WeatherDataRepository(new WeatherRemoteDataSource()),
                this,
                SchedulerProvider.getInstance());

        final SwipeRefreshLayout swipeRefresh = findViewById(R.id.swipe_refresh);
        swipeRefresh.setOnRefreshListener(() -> weatherPresenter.loadWeatherData());

        listAdapter = new WeatherListAdapter(weatherPresenter.getDefaultWeatherCities(), itemListener);
        final ListView lv = findViewById(android.R.id.list);
        lv.setAdapter(listAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        weatherPresenter.subscribe();
    }

    @Override
    public void onPause() {
        super.onPause();
        weatherPresenter.unsubscribe();
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        final SwipeRefreshLayout swipeRefresh = findViewById(R.id.swipe_refresh);
        swipeRefresh.post(() -> swipeRefresh.setRefreshing(active));
    }

    @Override
    public void showWeatherInfo(List<WeatherEntity> weatherEntities) {
        listAdapter.replaceData(weatherEntities);
    }

    @Override
    public void showNoWeatherInfo() {
        // No special handling needed
    }

    @Override
    public void setPresenter(WeatherContract.Presenter presenter) {
        // Already set in onCreate()
    }

    private static class WeatherListAdapter extends BaseAdapter {
        private List<WeatherEntity> weatherEntities;
        private WeatherItemListener itemListener;

        public WeatherListAdapter(List<WeatherEntity> weatherEntities, WeatherItemListener itemListener) {
            setList(weatherEntities);
            this.itemListener = itemListener;
        }

        public void replaceData(List<WeatherEntity> weatherEntities) {
            setList(weatherEntities);
            notifyDataSetChanged();
        }

        private void setList(List<WeatherEntity> weatherEntities) {
            this.weatherEntities = checkNotNull(weatherEntities);
        }

        @Override
        public int getCount() {
            return weatherEntities.size();
        }

        @Override
        public WeatherEntity getItem(int i) {
            return weatherEntities.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            View rowView = view;
            if (rowView == null) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                rowView = inflater.inflate(R.layout.weather_list_item, viewGroup, false);
            }

            final WeatherEntity weatherEntity = getItem(position);

            TextView cityTV = rowView.findViewById(R.id.city);
            cityTV.setText(weatherEntity.getName());

            TextView temperatureTV = rowView.findViewById(R.id.temperature);
            WeatherMain weatherMain = weatherEntity.getWeatherMain();
            if (weatherMain != null) {
                temperatureTV.setText(String.format("%d°C", Math.round(weatherMain.getTemp())));
            }

            ImageView weatherIV = rowView.findViewById(R.id.weather_img);
            if (weatherEntity.hasWeather()) {
                String url = String.format(WEATHER_ICON_URL, weatherEntity.getWeather().getIcon());
                Picasso.with(viewGroup.getContext())
                        .load(url)
                        .into(weatherIV);
            }

            rowView.setOnClickListener(__ -> itemListener.onWeatherCityClick(weatherEntity));

            return rowView;
        }
    }

    interface WeatherItemListener {

        void onWeatherCityClick(WeatherEntity weatherEntity);

    }
}
