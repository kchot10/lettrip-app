package com.cookandroid.travelerapplication.pokeInfo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class OneLineReviewAdapter extends FragmentPagerAdapter {
    private static final int NUM_PAGES = 2;
    private int selectedTab = 0; // 기본값을 매칭 성공 탭으로 설정


    OneLineReviewAdapter(FragmentManager fm){
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new OneLineReivewTab1();
            case 1:
                return new OneLineReivewTab2();
            default:
                return null;
        }
    }


    @Override
    public int getCount() {
        return NUM_PAGES;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "매칭 성공";
            case 1:
                return "매칭 실패";
            default:
                return null;
        }
    }

    public void setSelectedTab(int tab) {
        selectedTab = tab;
    }

    public int getSelectedTab() {
        return selectedTab;
    }
}
