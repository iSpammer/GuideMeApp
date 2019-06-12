package com.guideme.guideme.ENTITY;

public class WeatherObject {
    private String dayOfWeek;
    private int weatherIcon;
    private String weatherResult;
    private String weatherResultSmall;

    public WeatherObject(String dayOfWeek, int weatherIcon, String weatherResult, String weatherResultSmall) {
        this.dayOfWeek = dayOfWeek;
        this.weatherIcon = weatherIcon;
        this.weatherResult = weatherResult;
        this.weatherResultSmall = weatherResultSmall;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public int getWeatherIcon() {
        return weatherIcon;
    }

    public String getWeatherResult() {
        return weatherResult;
    }

    public String getWeatherResultSmall() {
        return weatherResultSmall;
    }
}