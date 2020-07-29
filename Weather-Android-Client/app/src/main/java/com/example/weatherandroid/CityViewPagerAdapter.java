package com.example.weatherandroid;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class CityViewPagerAdapter extends FragmentStatePagerAdapter {


    private List<List<String>> mCityList;
    private FragmentManager mFragmentManager;

    public CityViewPagerAdapter(@NonNull FragmentManager fm, List<List<String>> cityList) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.mFragmentManager = fm;
        this.mCityList = cityList;

    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return CurrentLocationTab.getInstance(mCityList.get(position));
        } else {
            return CityTab.getInstance(mCityList.get(position));
        }

    }

    @Override
    public int getCount() {
        return mCityList.size();
    }

    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

}
