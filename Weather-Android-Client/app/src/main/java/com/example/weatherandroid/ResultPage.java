package com.example.weatherandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ResultPage extends AppCompatActivity {

    private String city;
    private List<FutureWeather> weatherList = new ArrayList<>();
    private JsonObject object;
    private View mFirstCard;
    private boolean flag = true;

    private ProgressBar spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_page);
        city = getIntent().getStringExtra("city");

        getSupportActionBar().setTitle(city);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        spinner = findViewById(R.id.progressBar);
        spinner.setVisibility(View.VISIBLE);
        mFirstCard = findViewById(R.id.summary_card_view);
        String url = "http://homeworkeightsucks.us-west-1.elasticbeanstalk.com/search-by-address";
        volleyServiceCall(url, city);
        FloatingActionButton fab = findViewById(R.id.fab_button);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                SharedPreferences.Editor editor;
                editor = pref.edit();
                if (flag) {
                    fab.setImageResource(R.drawable.map_marker_minus);
                    Toast.makeText(getApplicationContext(),city.concat(" was added to favorites"),Toast.LENGTH_SHORT).show();
                    editor.putString(city, object.toString());

                } else {
                    fab.setImageResource(R.drawable.map_marker_plus);
                    Toast.makeText(getApplicationContext(),city.concat(" was removed from favorites"),Toast.LENGTH_SHORT).show();
                    editor.remove(city);

                }
                editor.commit();
                flag = !flag;

            }
        });

    }
    @Override
    public boolean onSupportNavigateUp(){

        this.finish();
        // or call onBackPressed()
        return true;
    }


    private void volleyServiceCall(String url, String city) {
        String encodedURL = Uri.parse(url).buildUpon().appendQueryParameter("street", "").appendQueryParameter("city", city).appendQueryParameter("state", "").build().toString();
        StringRequest stringReq = new StringRequest(
                Request.Method.GET, encodedURL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        // get response and bind the data
                        Log.d("great", response);
                        Gson gson = new Gson();
                        object = gson.fromJson(response, JsonObject.class);

                        // pass json object to detailed weather
                        mFirstCard.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                openDetailedWeather(object);
                            }
                        });

                        // get temperature, summary, and location
                        String temperature = object.get("weather").getAsJsonObject().get("currently").getAsJsonObject().get("temperature").getAsString();
                        temperature = temperature.substring(0, temperature.indexOf('.')).concat("\u2109");
                        TextView temperature_label = (TextView) findViewById(R.id.temperature_text);
                        temperature_label.setText(temperature);

                        String summary = object.get("weather").getAsJsonObject().get("currently").getAsJsonObject().get("summary").getAsString();
                        TextView summary_label = (TextView) findViewById(R.id.summary_text);
                        ImageView summary_image = (ImageView) findViewById(R.id.summary_icon);

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

                        // set location
                        String location = city;
                        TextView location_label = (TextView) findViewById(R.id.location_text);
                        location_label.setText(location);



                        String humidity = Integer.toString(Math.round(object.get("weather").getAsJsonObject().get("currently").getAsJsonObject().get("humidity").getAsFloat() * 100)).concat(" %");
                        String windSpeed = String.format("%.2f", object.get("weather").getAsJsonObject().get("currently").getAsJsonObject().get("windSpeed").getAsFloat()).concat(" mph");
                        String visibility = String.format("%.2f", object.get("weather").getAsJsonObject().get("currently").getAsJsonObject().get("visibility").getAsFloat()).concat(" km");
                        String pressure = String.format("%.2f", object.get("weather").getAsJsonObject().get("currently").getAsJsonObject().get("pressure").getAsFloat()).concat(" mb");

                        // set humidity data
                        TextView humidity_label = (TextView) findViewById(R.id.humidity);
                        humidity_label.setText(humidity);

                        // set wind speed data
                        TextView windSpeed_label = (TextView) findViewById(R.id.windSpeed);
                        windSpeed_label.setText(windSpeed);

                        // set visibility data
                        TextView visibility_label = (TextView) findViewById(R.id.visibility);
                        visibility_label.setText(visibility);

                        // set pressure
                        TextView pressure_label = (TextView) findViewById(R.id.pressure);
                        pressure_label.setText(pressure);


                        // set the future weather
                        JsonArray futureWeather = object.get("weather").getAsJsonObject().get("daily").getAsJsonObject().get("data").getAsJsonArray();

                        // initialize weather
                        initFutureWeather(futureWeather);
                        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(ResultPage.this);
                        recyclerView.setLayoutManager(layoutManager);
                        WeatherAdapter adapter = new WeatherAdapter(weatherList);
                        recyclerView.setAdapter(adapter);


                        // spinner gone
                        spinner.setVisibility(View.GONE);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        Volley.newRequestQueue(getApplicationContext()).add(stringReq);

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

            // Date time = new Date(date * 1000);
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
            imageSource = convertToIcon(summary);
            FutureWeather weather = new FutureWeather(formattedDate, imageSource, temperatureLow,temperatureHigh);
            weatherList.add(weather);
        }
    }

    private void openDetailedWeather(JsonObject json_object) {
        Intent intent = new Intent(this, DetailedWeather.class);
        intent.putExtra("DETAIL", json_object.toString());
        intent.putExtra("CITY", city);
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
