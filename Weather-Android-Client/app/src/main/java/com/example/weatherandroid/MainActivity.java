package com.example.weatherandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;

import android.view.View;
import android.widget.AdapterView;
import androidx.appcompat.widget.SearchView;
import android.view.MenuItem;
import android.widget.ProgressBar;

import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import androidx.appcompat.widget.SearchView.SearchAutoComplete;
import androidx.viewpager.widget.ViewPager;


public class MainActivity extends AppCompatActivity {

    private static final int TRIGGER_AUTO_COMPLETE = 100;
    private static final long AUTO_COMPLETE_DELAY = 300;
    private Handler handler;


    private ViewPager viewPager;
    private CityViewPagerAdapter cityAdapter;
    private TabLayout tabLayout;
    private AutoSuggestAdapter autoSuggestAdapter;
    private ProgressBar spinner;

    private List<List<String>> mCityList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinner = findViewById(R.id.progressBar);
        spinner.setVisibility(View.VISIBLE);
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://ip-api.com/json/";
        this.mCityList = new ArrayList<>();
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("response", response);
                        Gson g = new Gson();
                        final JsonObject convertedObject = g.fromJson(response, JsonObject.class);


                        // get the current latitude and longitude
                        String latitude = convertedObject.get("lat").getAsString();
                        String longitude = convertedObject.get("lon").getAsString();
                        String city = convertedObject.get("city").getAsString();
                        String state = convertedObject.get("region").getAsString();
                        String country = convertedObject.get("country").getAsString();
                        // second volley call
                        secondServiceCall(latitude, longitude, city, state, country, "http://homeworkeightsucks.us-west-1.elasticbeanstalk.com/search-by-current-location");
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ERROR","That didn't work!");
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("RESUME", "it is hit");
        if (viewPager != null && viewPager.getAdapter() != null) {
            List<String> firstItem = mCityList.get(0);
            mCityList.clear();
            mCityList.add(firstItem);
            List<String> temp = new ArrayList<>();
            SharedPreferences mPrefs = getSharedPreferences("MyPref", 0);
            Map<String,?> keys = mPrefs.getAll();

            for(Map.Entry<String, ?> entry : keys.entrySet()){
                temp = new ArrayList<>();
                temp.add(entry.getKey());
                Log.d("NAME", entry.getKey());
                temp.add(entry.getValue().toString());
                mCityList.add(temp);
            }
            cityAdapter = new CityViewPagerAdapter(getSupportFragmentManager(), mCityList);
            viewPager.setAdapter(cityAdapter);
        }
    }

    public void dataChanged() {
        List<String> firstItem = mCityList.get(0);
        mCityList = new ArrayList<>();
        mCityList.add(firstItem);
        List<String> temp = new ArrayList<>();
        SharedPreferences mPrefs = getSharedPreferences("MyPref", 0);
        Map<String,?> keys = mPrefs.getAll();

        for(Map.Entry<String, ?> entry : keys.entrySet()){
            temp = new ArrayList<>();
            temp.add(entry.getKey());
            Log.d("NAME", entry.getKey());
            temp.add(entry.getValue().toString());
            mCityList.add(temp);
        }
        System.out.println(mCityList.size());
        cityAdapter = new CityViewPagerAdapter(getSupportFragmentManager(), mCityList);
        viewPager.setAdapter(cityAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem item = menu.findItem(R.id.search_item);
        SearchView mSearchView = (SearchView) item.getActionView();
        final SearchAutoComplete autoCompleteTextView = mSearchView.findViewById(R.id.search_src_text);

        //Setting up the adapter for AutoSuggest
        autoSuggestAdapter = new AutoSuggestAdapter(this, android.R.layout.simple_dropdown_item_1line);
        autoCompleteTextView.setThreshold(2);

        autoCompleteTextView.setDropDownBackgroundResource(R.color.colorWhite);
        autoCompleteTextView.setAdapter(autoSuggestAdapter);


        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                autoCompleteTextView.setText("" + autoSuggestAdapter.getObject(position));
            }
        });


        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(MainActivity.this, ResultPage.class);
                intent.putExtra("city", query);

                startActivity(intent);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                handler.removeMessages(TRIGGER_AUTO_COMPLETE);
                handler.sendEmptyMessageDelayed(TRIGGER_AUTO_COMPLETE, AUTO_COMPLETE_DELAY);
                return true;
            }
        });

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == TRIGGER_AUTO_COMPLETE) {
                    if (!TextUtils.isEmpty(autoCompleteTextView.getText())) {
                        makeApiCall(autoCompleteTextView.getText().toString());
                    }
                }
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void makeApiCall(String text) {

        ApiCall.make(this, text, (response) -> {

            List<String> stringList = new ArrayList<>();
            try {
                Gson gson = new Gson();
                JsonObject convertedToJson = gson.fromJson(response.toString(), JsonObject.class);
                JsonArray arr = convertedToJson.getAsJsonArray("predictions");

                for (JsonElement each : arr) {
                    JsonObject item = each.getAsJsonObject();
                    stringList.add(item.get("description").getAsString());
                }

                //IMPORTANT: set data here and notify
                autoSuggestAdapter.setData(stringList);
                autoSuggestAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, error-> {
            Log.e("err", "something goes wrong here");
        });
    }


    public void secondServiceCall(String latitude, String longitude, final String city, final String state, final String country, String url) {
        String encodedURL = Uri.parse(url)
                .buildUpon()
                .appendQueryParameter("latitude", latitude).appendQueryParameter("longitude", longitude).appendQueryParameter("region", state).appendQueryParameter("city", city)
                .build().toString();

        StringRequest stringReq = new StringRequest(
                Request.Method.GET, encodedURL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        // get response and bind the data
                        Log.d("great", response);
                        Gson gson = new Gson();

                        // set up an adapter
                        viewPager = findViewById(R.id.city_view_pager);
                        String address = city.concat(", ").concat(state).concat(", ").concat(country);
                        List<String> temp = new ArrayList<>();
                        temp.add(address);
                        temp.add(response);
                        mCityList.add(temp);
                        SharedPreferences mPrefs = getSharedPreferences("MyPref", 0);
                        Map<String,?> keys = mPrefs.getAll();

                        for(Map.Entry<String, ?> entry : keys.entrySet()){
                            temp = new ArrayList<>();
                            temp.add(entry.getKey());
                            temp.add(entry.getValue().toString());
                            mCityList.add(temp);
                        }
                        cityAdapter = new CityViewPagerAdapter(getSupportFragmentManager(), mCityList);


                        viewPager.setAdapter(cityAdapter);

                        tabLayout = findViewById(R.id.city_tabs);
                        tabLayout.setupWithViewPager(viewPager);
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


}
