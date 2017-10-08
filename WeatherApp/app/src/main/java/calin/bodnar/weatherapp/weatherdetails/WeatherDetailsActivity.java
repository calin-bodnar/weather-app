package calin.bodnar.weatherapp.weatherdetails;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import calin.bodnar.weatherapp.R;
import calin.bodnar.weatherapp.data.model.WeatherEntity;
import calin.bodnar.weatherapp.data.model.WeatherMain;
import calin.bodnar.weatherapp.data.model.Wind;
import calin.bodnar.weatherapp.util.Constants;

public class WeatherDetailsActivity extends AppCompatActivity {
    public static final String EXTRA_WEATHER_ENTITY = "WeatherDetailsActivity.WeatherEntity.Extra";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_details);

        WeatherEntity weatherEntity = getIntent().getParcelableExtra(EXTRA_WEATHER_ENTITY);
        setWeatherDetails(weatherEntity);
    }

    @SuppressLint("DefaultLocale")
    private void setWeatherDetails(WeatherEntity weatherEntity) {
        TextView cityTV = findViewById(R.id.name);
        cityTV.setText(weatherEntity.getName());

        TextView temperatureTV = findViewById(R.id.temperature);
        TextView humidityTV = findViewById(R.id.humidity);
        TextView pressureTV = findViewById(R.id.pressure);

        WeatherMain weatherMain = weatherEntity.getWeatherMain();
        if (weatherMain != null) {
            temperatureTV.setText(String.format("%d°C", Math.round(weatherMain.getTemp())));
            humidityTV.setText(String.format("%d %%", Math.round(weatherMain.getHumidity())));
            pressureTV.setText(String.format("%d hPa", Math.round(weatherMain.getPressure())));
        }

        ImageView weatherIV = findViewById(R.id.weather_img);
        if (weatherEntity.hasWeather()) {
            String url = String.format(Constants.WEATHER_ICON_URL, weatherEntity.getWeather().getIcon());
            Picasso.with(this)
                    .load(url)
                    .into(weatherIV);
        }

        TextView windSpeedTV = findViewById(R.id.wind_speed);
        TextView windDirectionTV = findViewById(R.id.wind_direction);
        Wind wind = weatherEntity.getWind();
        if (wind != null) {
            windSpeedTV.setText(String.format("%d m/s", Math.round(wind.getSpeed())));
            windDirectionTV.setText(String.format("%d degrees", Math.round(wind.getDeg())));
        }
    }
}
