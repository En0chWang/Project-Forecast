package com.example.weatherandroid;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder> {

    private List<FutureWeather> mFutureWeatherList;


    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView weatherImage;
        TextView date;
        TextView temperatureLow;
        TextView temperatureHigh;
        public ViewHolder(View view) {
            super(view);
            weatherImage = (ImageView) view.findViewById(R.id.image_id);
            date = (TextView) view.findViewById(R.id.date_id);
            temperatureLow = (TextView) view.findViewById(R.id.temperature_low_id);
            temperatureHigh = (TextView) view.findViewById(R.id.temperature_high_id);
        }
    }
    public WeatherAdapter(List<FutureWeather> futureWeatherList) {
        this.mFutureWeatherList = futureWeatherList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.future_weather_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherAdapter.ViewHolder holder, int position) {
        FutureWeather futureWeather = mFutureWeatherList.get(position);
        holder.weatherImage.setImageResource(futureWeather.getImageId());
        holder.date.setText(futureWeather.getDate());
        holder.temperatureLow.setText(futureWeather.getTemperatureLow());
        holder.temperatureHigh.setText(futureWeather.getTemperatureHigh());

    }

    @Override
    public int getItemCount() {
        return mFutureWeatherList.size();
    }
}
