package com.guideme.guideme;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import com.kwabenaberko.openweathermaplib.constants.Units;
import com.kwabenaberko.openweathermaplib.implementation.OpenWeatherMapHelper;
import com.kwabenaberko.openweathermaplib.implementation.callbacks.CurrentWeatherCallback;
import com.kwabenaberko.openweathermaplib.models.currentweather.CurrentWeather;

public class WeatherCard extends CardView implements LocationListener {
    //weather stuff
    private TextView greetingTv, usernameTv, weatherTodayTv, descriptionTv, maxTv, windTv, pressureTv, humaTv;
    private ImageView weatherIconIv;
    private OpenWeatherMapHelper helper;

    private Context context;

    public WeatherCard(Context context) {
        this(context, null);
    }

    public WeatherCard(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WeatherCard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView();
    }

    public void initView() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.weather_card, this, true);
        setRadius(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15.0f, getResources().getDisplayMetrics()));

        //weather
        greetingTv = findViewById(R.id.greeting);
        usernameTv = findViewById(R.id.username);
        weatherTodayTv = findViewById(R.id.weatherToday);
        descriptionTv = findViewById(R.id.description);
        maxTv = findViewById(R.id.max_weather);
        windTv = findViewById(R.id.wind_speed);
        pressureTv = findViewById(R.id.pressure);
        humaTv = findViewById(R.id.huma);
//        weatherTodayTv = findViewById(R.id.weatherIcon);

        helper = new OpenWeatherMapHelper("0e5e434a8f9778e596ff155231d12d9a");
        helper.setUnits(Units.IMPERIAL);


    }

    private void getWeather(double longitude, double latitude) {
        helper.getCurrentWeatherByGeoCoordinates(longitude, latitude, new CurrentWeatherCallback() {
            @Override
            public void onSuccess(CurrentWeather currentWeather) {
                Log.v("WEATHER_WIN", "Coordinates: " + currentWeather.getCoord().getLat() + ", " + currentWeather.getCoord().getLon() + "\n"
                        + "Weather Description: " + currentWeather.getWeather().get(0).getDescription() + "\n"
                        + "Temperature: " + currentWeather.getMain().getTempMax() + "\n"
                        + "Wind Speed: " + currentWeather.getWind().getSpeed() + "\n"
                        + "City, Country: " + currentWeather.getName() + ", " + currentWeather.getSys().getCountry()
                );
                weatherTodayTv.setText(String.format("%s", currentWeather.getMain().getTemp()));
                descriptionTv.setText(currentWeather.getWeather().get(0).getDescription());
                maxTv.setText(String.format("%s", currentWeather.getMain().getTempMax()));
                windTv.setText(String.format("%s", currentWeather.getWind().getSpeed()));
                pressureTv.setText(String.format("%s", currentWeather.getMain().getPressure()));
                humaTv.setText(String.format("%s", currentWeather.getMain().getHumidity()));
            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.v("WEATHER_LOSE", throwable.getMessage());
            }
        });
    }

    @Override
    public void onLocationChanged(Location loc) {
        Toast.makeText(getContext(), "OnLocationChanged", Toast.LENGTH_SHORT).show();   // Toast not showing
        String longitude = "Longitude: " + loc.getLongitude();
        String latitude = "Latitude: " + loc.getLatitude();
        String s = longitude + "\n" + latitude;
        getWeather(Double.parseDouble(longitude), Double.parseDouble(latitude));
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(getContext(), "onProviderDisabled", Toast.LENGTH_SHORT).show();  // Toast not showing
    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(getContext(), "on ProviderEnabled", Toast.LENGTH_SHORT).show();  // Toast not showing
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Toast.makeText(getContext(), "onStatusChanged", Toast.LENGTH_SHORT).show();  // Toast not showing
    }
}

