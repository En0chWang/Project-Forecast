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
public class CurrentLocationTab extends Fragment {

    String weather_info;
    String address;
    private TextView textView;
    private CardView mFirstCard;
    public List<FutureWeather> weatherList = new ArrayList<>();

    public static Fragment getInstance(List<String> cityMap) {
        Bundle bundle = new Bundle();
        bundle.putString("address", cityMap.get(0));
        bundle.putString("info", cityMap.get(1));
        CurrentLocationTab currentLocationTab = new CurrentLocationTab();
        currentLocationTab.setArguments(bundle);
        return currentLocationTab;
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

        View view = inflater.inflate(R.layout.current_location_tab, container, false);


        // get temperature, summary, and location
        Gson gson = new Gson();
        JsonObject object = gson.fromJson(weather_info, JsonObject.class);
        String temperature = object.get("weather").getAsJsonObject().get("currently").getAsJsonObject().get("temperature").getAsString();
        temperature = temperature.substring(0, temperature.indexOf('.')).concat("\u2109");
        TextView temperature_label = (TextView) view.findViewById(R.id.temperature_text);
        temperature_label.setText(temperature);

        String summary = object.get("weather").getAsJsonObject().get("currently").getAsJsonObject().get("summary").getAsString();
        String summary_icon = object.get("weather").getAsJsonObject().get("currently").getAsJsonObject().get("icon").getAsString();
        TextView summary_label = (TextView) view.findViewById(R.id.summary_text);
        ImageView summary_image = (ImageView) view.findViewById(R.id.summary_icon);

        // set summary
        summary_label.setText(summary);
        int resource = convertToIcon(summary_icon);
        summary_image.setImageResource(resource);


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



        String humidity = object.get("weather").getAsJsonObject().get("currently").getAsJsonObject().get("humidity").getAsString().substring(2).concat(" %");
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

            imageSource = convertToIcon(summary);

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

    private int convertToIcon(String text) {
        if (text.equals("partly-cloudy-night")) {
            return R.drawable.weather_night_partly_cloudy;
        } else if (text.equals("partly-cloudy-day")) {
            return R.drawable.weather_partly_cloudy;
        } else if (text.equals("clear-night")) {
            return R.drawable.weather_night;
        } else if (text.equals("rain")) {
            return R.drawable.weather_rainy;
        } else if (text.equals("sleet")) {
            return R.drawable.weather_snowy_rainy;
        } else if (text.equals("snow")) {
            return R.drawable.weather_snowy;
        } else if (text.equals("wind")) {
            return R.drawable.weather_windy_variant;
        } else if (text.equals("fog")) {
            return R.drawable.weather_fog;
        } else if (text.equals("cloudy")) {
            return R.drawable.weather_cloudy;
        } else {

            return R.drawable.weather_sunny;
        }
    }


}
