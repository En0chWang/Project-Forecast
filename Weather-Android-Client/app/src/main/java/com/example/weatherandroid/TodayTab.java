package com.example.weatherandroid;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class TodayTab extends Fragment {
    int position;
    String json_string;
    private TextView textView;
    private JsonObject convertedObject;

    private TextView mWindSpeedCardText;
    private TextView mPressureCardText;
    private TextView mPrecipitationText;
    private TextView mTemperatureText;
    private TextView mSummaryText;
    private ImageView mSummaryIcon;
    private TextView mHumidityText;
    private TextView mVisibilityText;
    private TextView mCloudCoverText;
    private TextView mOzoneText;


    public static Fragment getInstance(int position, String json_string) {
        Bundle bundle = new Bundle();
        bundle.putInt("pos", position);
        bundle.putString("data", json_string);
        TodayTab todayTab = new TodayTab();
        todayTab.setArguments(bundle);
        return todayTab;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt("pos");
        json_string = getArguments().getString("data");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.today_tab, container, false);
        Gson gson = new Gson();
        convertedObject = gson.fromJson(json_string, JsonObject.class);

        String windSpeedText = String.format("%.2f", convertedObject.get("weather").getAsJsonObject().get("currently").getAsJsonObject().get("windSpeed").getAsFloat()).concat(" mph");

        String pressureText = String.format("%.2f", convertedObject.get("weather").getAsJsonObject().get("currently").getAsJsonObject().get("pressure").getAsFloat()).concat(" mb");

        String precipitationText = String.format("%.2f", convertedObject.get("weather").getAsJsonObject().get("currently").getAsJsonObject().get("precipIntensity").getAsFloat()).concat(" mmph");

        String temperatureText = Integer.toString(Math.round(convertedObject.get("weather").getAsJsonObject().get("currently").getAsJsonObject().get("temperature").getAsFloat())).concat("\u2109");

        String summaryText = convertedObject.get("weather").getAsJsonObject().get("currently").getAsJsonObject().get("summary").getAsString();
        String summaryIconText =  convertedObject.get("weather").getAsJsonObject().get("currently").getAsJsonObject().get("icon").getAsString();
        int summaryIcon = convertToIcon(summaryIconText);

        String humidityText =  Integer.toString(Math.round(convertedObject.get("weather").getAsJsonObject().get("currently").getAsJsonObject().get("humidity").getAsFloat() * 100)).concat("%");

        String visibilityText = String.format("%.2f", convertedObject.get("weather").getAsJsonObject().get("currently").getAsJsonObject().get("visibility").getAsFloat()).concat(" km");

        String cloudCoverText = Integer.toString(Math.round(convertedObject.get("weather").getAsJsonObject().get("currently").getAsJsonObject().get("cloudCover").getAsFloat() * 100)).concat("%");

        String ozoneText = String.format("%.2f", convertedObject.get("weather").getAsJsonObject().get("currently").getAsJsonObject().get("ozone").getAsFloat()).concat(" DU");

        // wind speed
        mWindSpeedCardText = view.findViewById(R.id.wind_speed_card_text);
        mWindSpeedCardText.setText(windSpeedText);

        // pressure
        mPressureCardText = view.findViewById(R.id.pressure_card_text);
        mPressureCardText.setText(pressureText);

        // precipitation
        mPrecipitationText = view.findViewById(R.id.precipitation_card_text);
        mPrecipitationText.setText(precipitationText);


        // temperature
        mTemperatureText = view.findViewById(R.id.temperature_card_text);
        mTemperatureText.setText(temperatureText);

        // summary
        mSummaryText = view.findViewById(R.id.summary_card_text);
        mSummaryIcon = view.findViewById(R.id.summary_card_icon);
        mSummaryText.setText(summaryText);
        if (summaryIcon == R.drawable.weather_sunny) {
            mSummaryIcon.setColorFilter(Color.parseColor("#f4a32f"));
        }
        mSummaryIcon.setImageResource(summaryIcon);

        // humidity
        mHumidityText = view.findViewById(R.id.humidity_card_text);
        mHumidityText.setText(humidityText);

        // visibility
        mVisibilityText = view.findViewById(R.id.visibility_card_text);
        mVisibilityText.setText(visibilityText);

        // cloud cover
        mCloudCoverText = view.findViewById(R.id.cloud_cover_card_text);
        mCloudCoverText.setText(cloudCoverText);

        // ozone
        mOzoneText = view.findViewById(R.id.ozone_card_text);
        mOzoneText.setText(ozoneText);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


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
