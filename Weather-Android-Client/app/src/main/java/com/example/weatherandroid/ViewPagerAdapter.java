package com.example.weatherandroid;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private String title[] = {"Today", "Weekly", "Photos"};

    private String json_string;


    public ViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    public ViewPagerAdapter(@NonNull FragmentManager fm, String json_string) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.json_string = json_string;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch(position)
        {

            case 0:
                return TodayTab.getInstance(0, this.json_string);
            case 1:
                return WeeklyTab.getInstance(1, this.json_string);
            case 2:
                return PhotoTab.getInstance(2);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return title.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return title[position];
    }
}
