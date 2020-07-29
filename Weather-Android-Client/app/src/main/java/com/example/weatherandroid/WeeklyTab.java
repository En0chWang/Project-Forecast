package com.example.weatherandroid;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class WeeklyTab extends Fragment {
    int position;
    String json_string;
    private TextView textView;
    private ImageView imageView;

    public static Fragment getInstance(int position, String json_string) {
        Bundle bundle = new Bundle();
        bundle.putInt("pos", position);
        bundle.putString("data", json_string);
        WeeklyTab weeklyTab = new WeeklyTab();
        weeklyTab.setArguments(bundle);
        return weeklyTab;
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
        View view = inflater.inflate(R.layout.weekly_tab, container, false);
        LineChart chart = (LineChart) view.findViewById(R.id.line_chart);
        Gson gson = new Gson();
        JsonObject convertedObject = gson.fromJson(json_string, JsonObject.class);


        String summary = convertedObject.get("weather").getAsJsonObject().get("daily").getAsJsonObject().get("summary").getAsString();
        int imgResource = convertToIcon(convertedObject.get("weather").getAsJsonObject().get("daily").getAsJsonObject().get("icon").getAsString());
        imageView = view.findViewById(R.id.weekly_summary_icon);
        imageView.setImageResource(imgResource);

        textView = view.findViewById(R.id.weekly_summary_text);
        textView.setText(summary);

        JsonArray futureWeather = convertedObject.get("weather").getAsJsonObject().get("daily").getAsJsonObject().get("data").getAsJsonArray();
        List<Entry> temperatureLows = new ArrayList<>();
        List<Entry> temperatureHighs = new ArrayList<>();

        for (int i = 0; i < futureWeather.size(); i++) {
            float temperatureLow = futureWeather.get(i).getAsJsonObject().get("temperatureLow").getAsFloat() * 100 / 100;
            // turn your data into Entry objects
            temperatureLows.add(new Entry(i, temperatureLow));
        }

        for (int i = 0; i < futureWeather.size(); i++) {
            float temperatureHigh = futureWeather.get(i).getAsJsonObject().get("temperatureHigh").getAsFloat() * 100 / 100;
            // turn your data into Entry objects
            temperatureHighs.add(new Entry(i, temperatureHigh));
        }

        // change legend color
        Legend l = chart.getLegend();
        l.setForm(Legend.LegendForm.SQUARE);
        l.setTextColor(Color.parseColor("#ffffff"));
        l.setTextSize(15f);

        // change x,y axises
        XAxis xAxis = chart.getXAxis();
        xAxis.setTextColor(Color.parseColor("#ffffff"));

        YAxis leftAxis = chart.getAxisLeft();
        YAxis rightAxis = chart.getAxisRight();
        leftAxis.setTextColor(Color.parseColor("#ffffff"));
        rightAxis.setTextColor(Color.parseColor("#ffffff"));

        LineDataSet lowTemperatureDataSet = new LineDataSet(temperatureLows, "Minimum Temperature");

        lowTemperatureDataSet.setColor(Color.parseColor("#b28cf5"));
        lowTemperatureDataSet.setValueTextColor(Color.parseColor("#b28cf5"));
        lowTemperatureDataSet.setDrawValues(false);
        LineDataSet highTemperatureDataSet = new LineDataSet(temperatureHighs, "Maximum Temperature");
        highTemperatureDataSet.setColor(Color.parseColor("#f4a32f"));
        highTemperatureDataSet.setValueTextColor(Color.parseColor("#f4a32f"));
        highTemperatureDataSet.setDrawValues(false);
        List<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lowTemperatureDataSet);
        dataSets.add(highTemperatureDataSet);
        LineData data = new LineData(dataSets);
        chart.setData(data);
        chart.invalidate(); // refresh

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