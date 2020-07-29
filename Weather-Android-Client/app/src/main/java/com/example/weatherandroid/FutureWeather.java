package com.example.weatherandroid;

public class FutureWeather {
    private String date;
    private int imageId;
    private String temperatureLow;
    private String temperatureHigh;

    public FutureWeather(String date, int imageId, String temperatureLow, String temperatureHigh) {
        this.date = date;
        this.imageId = imageId;
        this.temperatureLow = temperatureLow;
        this.temperatureHigh = temperatureHigh;
    }

    public String getDate() {
        return date;
    }

    public int getImageId() {
        return imageId;
    }

    public String getTemperatureLow() {
        return temperatureLow;
    }

    public String getTemperatureHigh() {
        return temperatureHigh;
    }
}
