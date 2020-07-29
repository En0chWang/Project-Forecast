package com.example.weatherandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.ActionBar;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import android.util.Log;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;

public class DetailedWeather extends AppCompatActivity {
    private int[] tabIcons = {R.drawable.calendar_today, R.drawable.trending_up, R.drawable.google_photos};

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Toolbar toolbar;
    // pass in the json object
    public String json_string;
    public String address;

    public ProgressBar spinner;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_page_menu, menu);
        MenuItem item = menu.findItem(R.id.twitter_icon);
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Gson gson = new Gson();
                JsonObject object = gson.fromJson(json_string, JsonObject.class);
                String temp = Integer.toString(Math.round(object.get("weather").getAsJsonObject().get("currently").getAsJsonObject().get("temperature").getAsFloat()));
                Uri uri = Uri.parse("https://twitter.com/intent/tweet?text=Check out ".concat(address).concat("'s Weather! It is ").concat(temp).concat("°F.").concat("&hashtags=CSCI571WeatherSearch"));
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailed_weather);


        // initially set spinner as gone
        spinner = findViewById(R.id.detail_progressBar);
        spinner.setVisibility(View.VISIBLE);


        // get the string information
        json_string = getIntent().getStringExtra("DETAIL");
        address = getIntent().getStringExtra("CITY");

        androidx.appcompat.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(address);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), json_string);


        viewPager.setAdapter(adapter);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#FFFFFF"));
        tabLayout.setBackgroundColor(Color.parseColor("#000000"));
        tabLayout.setTabTextColors(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);

        spinner.setVisibility(View.GONE);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 2) {
                    Log.d("HERE", "it is here");
                    spinner.setVisibility(View.VISIBLE);
                    makeVolleyCall(address);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }

            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });
    }


    @Override
    public boolean onSupportNavigateUp(){
        this.finish();
        return true;
    }

    private void makeVolleyCall(String address) {
        String url = "https://www.googleapis.com/customsearch/v1";
        String encodedURL = Uri.parse(url).buildUpon()
                .appendQueryParameter("q", address)
                .appendQueryParameter("cx", "009900325867064746831:hfchxhdjxrb")
                .appendQueryParameter("imgSize", "huge")
                .appendQueryParameter("imgType", "news")
                .appendQueryParameter("num", "8")
                .appendQueryParameter("searchType", "image")
                .appendQueryParameter("key", "AIzaSyDAPCMh2SabpU84KuUzvUh1NyBewWzrGHU").build().toString();
        StringRequest stringReq = new StringRequest(
                Request.Method.GET, encodedURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        List<ImageItem> imageList = getImages(response);

                        RecyclerView recyclerView = findViewById(R.id.image_recycler_view);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(DetailedWeather.this);
                        recyclerView.setLayoutManager(layoutManager);
                        ImageAdapter adapter = new ImageAdapter(imageList);
                        recyclerView.setAdapter(adapter);


                        // disable the spinner
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

    private List<ImageItem> getImages(String response) {
        List<ImageItem> res = new ArrayList<>();
        Gson gson = new Gson();
        JsonObject object = gson.fromJson(response, JsonObject.class);
        JsonArray arr = object.get("items").getAsJsonArray();
        for (JsonElement each : arr) {
            res.add(new ImageItem(each.getAsJsonObject().get("link").getAsString()));
        }
        return res;
    }
}
