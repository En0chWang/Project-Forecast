package com.example.weatherandroid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CityTab extends Fragment {
    String weather_info;
    String address;
    private TextView textView;
    private CardView mFirstCard;
    public List<FutureWeather> weatherList = new ArrayList<>();

    public static Fragment getInstance(List<String> cityMap) {
        Bundle bundle = new Bundle();
        bundle.putString("address", cityMap.get(0));
        bundle.putString("info", cityMap.get(1));
        CityTab cityTab = new CityTab();
        cityTab.setArguments(bundle);
        return cityTab;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        weather_info = getArguments().getString("info");
        address = getArguments().getString("address");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // todo: parse weather info

        View view = inflater.inflate(R.layout.city_tab, container, false);
        FloatingActionButton fab = view.findViewById(R.id.fab_button);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0); // 0 - for private mode
                SharedPreferences.Editor editor = pref.edit();
                Toast.makeText(getContext(),address.concat(" was removed from favorites"),Toast.LENGTH_SHORT).show();
                editor.remove(address);
                editor.commit();
                ((MainActivity) getActivity()).dataChanged();

            }
        });

        // get temperature, summary, and location
        Gson gson = new Gson();
        JsonObject object = gson.fromJson(weather_info, JsonObject.class);
        String temperature = object.get("weather").getAsJsonObject().get("currently").getAsJsonObject().get("temperature").getAsString();
        temperature = temperature.substring(0, temperature.indexOf('.')).concat("\u2109");
        TextView temperature_label = (TextView) view.findViewById(R.id.temperature_text);
        temperature_label.setText(temperature);

        String summary = object.get("weather").getAsJsonObject().get("currently").getAsJsonObject().get("summary").getAsString();
        TextView summary_label = (TextView) view.findViewById(R.id.summary_text);
        ImageView summary_image = (ImageView) view.findViewById(R.id.summary_icon);

        // set summary
        summary_label.setText(summary);

        // set image
        if (summary.contains("cloudy") || summary.contains("Cloudy")) {
            summary_image.setImageResource(R.drawable.weather_cloudy);
        } else if (summary.contains("fog") || summary.contains("Fog")) {
            summary_image.setImageResource(R.drawable.weather_fog);
        } else if (summary.contains("wind") || summary.contains("Wind")) {
            summary_image.setImageResource(R.drawable.weather_windy_variant);
        } else if (summary.contains("snow") || summary.contains("Snow")) {
            summary_image.setImageResource(R.drawable.weather_snowy);
        } else if (summary.contains("sleet") || summary.contains("Sleet")) {
            summary_image.setImageResource(R.drawable.weather_snowy_rainy);
        } else if (summary.contains("rain") || summary.contains("Rain")) {
            summary_image.setImageResource(R.drawable.weather_rainy);
        } else if (summary.contains("Clear-night") || summary.contains("Clear night")) {
            summary_image.setImageResource(R.drawable.weather_night);
        } else if (summary.contains("partly-cloudy-night")) {
            summary_image.setImageResource(R.drawable.weather_night_partly_cloudy);
        } else if (summary.contains("partly-cloudy-day")) {
            summary_image.setImageResource(R.drawable.weather_partly_cloudy);
        } else {
            summary_image.setImageResource(R.drawable.weather_sunny);
        }


        mFirstCard = view.findViewById(R.id.summary_card_view);
        // pass json object to detailed weather
        mFirstCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDetailedWeather(object);
            }
        });

        // set location
        String location = address;
        TextView location_label = (TextView) view.findViewById(R.id.location_text);
        location_label.setText(location);



        String humidity = Integer.toString(Math.round(object.get("weather").getAsJsonObject().get("currently").getAsJsonObject().get("humidity").getAsFloat() * 100)).concat(" %");
        String windSpeed = String.format("%.2f", object.get("weather").getAsJsonObject().get("currently").getAsJsonObject().get("windSpeed").getAsFloat()).concat(" mph");
        String visibility = String.format("%.2f", object.get("weather").getAsJsonObject().get("currently").getAsJsonObject().get("visibility").getAsFloat()).concat(" km");
        String pressure = String.format("%.2f", object.get("weather").getAsJsonObject().get("currently").getAsJsonObject().get("pressure").getAsFloat()).concat(" mb");

        // set humidity data
        TextView humidity_label = (TextView) view.findViewById(R.id.humidity);
        humidity_label.setText(humidity);

        // set wind speed data
        TextView windSpeed_label = (TextView) view.findViewById(R.id.windSpeed);
        windSpeed_label.setText(windSpeed);

        // set visibility data
        TextView visibility_label = (TextView) view.findViewById(R.id.visibility);
        visibility_label.setText(visibility);

        // set pressure
        TextView pressure_label = (TextView) view.findViewById(R.id.pressure);
        pressure_label.setText(pressure);


        // set the future weather
        JsonArray futureWeather = object.get("weather").getAsJsonObject().get("daily").getAsJsonObject().get("data").getAsJsonArray();

        // initialize weather
        initFutureWeather(futureWeather);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        WeatherAdapter adapter = new WeatherAdapter(weatherList);
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private void initFutureWeather(JsonArray futureWeather) {

        for (int i = 0; i < futureWeather.size(); i++) {
            JsonElement temp = futureWeather.get(i);
            long unixTime = temp.getAsJsonObject().get("time").getAsLong();

            // convert seconds to milliseconds
            Date date = new java.util.Date(unixTime*1000L);
            // the format of your date
            SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM/dd/yyyy");
            // give a timezone reference for formatting
            String formattedDate = sdf.format(date);

//            Date time = new Date(date * 1000);
            String summary = temp.getAsJsonObject().get("icon").getAsString();
            int imageSource;
            String temperatureLow = temp.getAsJsonObject().get("temperatureLow").getAsString();
            if (temperatureLow.indexOf('.') != -1) {
                temperatureLow = temperatureLow.substring(0, temperatureLow.indexOf('.'));
            }

            String temperatureHigh = temp.getAsJsonObject().get("temperatureHigh").getAsString();
            if (temperatureHigh.indexOf('.') != -1) {
                temperatureHigh = temperatureHigh.substring(0, temperatureHigh.indexOf('.'));
            }
            // set image
            if (summary.contains("cloudy") || summary.contains("Cloudy")) {
                imageSource = R.drawable.weather_cloudy;
            } else if (summary.contains("fog") || summary.contains("Fog")) {
                imageSource = R.drawable.weather_fog;
            } else if (summary.contains("wind") || summary.contains("Wind")) {
                imageSource = R.drawable.weather_windy_variant;
            } else if (summary.contains("snow") || summary.contains("Snow")) {
                imageSource = R.drawable.weather_snowy;
            } else if (summary.contains("sleet") || summary.contains("Sleet")) {
                imageSource = R.drawable.weather_snowy_rainy;
            } else if (summary.contains("rain") || summary.contains("Rain")) {
                imageSource = R.drawable.weather_rainy;
            } else if (summary.contains("Clear-night") || summary.contains("Clear night")) {
                imageSource = R.drawable.weather_night;
            } else if (summary.contains("partly-cloudy-night")) {
                imageSource = R.drawable.weather_night_partly_cloudy;
            } else if (summary.contains("partly-cloudy-day")) {
                imageSource = R.drawable.weather_partly_cloudy;
            } else {
                imageSource = R.drawable.weather_sunny;
            }
            FutureWeather weather = new FutureWeather(formattedDate, imageSource, temperatureLow,temperatureHigh);
            weatherList.add(weather);
        }
    }

    private void openDetailedWeather(JsonObject json_object) {
        Intent intent = new Intent(getActivity(), DetailedWeather.class);
        intent.putExtra("DETAIL", json_object.toString());
        intent.putExtra("CITY", address);
        startActivity(intent);
    }

}
