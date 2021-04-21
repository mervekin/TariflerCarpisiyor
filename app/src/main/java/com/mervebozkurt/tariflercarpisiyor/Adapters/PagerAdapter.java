package com.mervebozkurt.tariflercarpisiyor.Adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.mervebozkurt.tariflercarpisiyor.Fragments.AddedRecipes;
import com.mervebozkurt.tariflercarpisiyor.Fragments.Favorite;

public class PagerAdapter extends FragmentStatePagerAdapter {
    final int pageCount=2;
    private String tabTitles[]=new String[]{ "Tariflerim","Favorilerim"};

    public PagerAdapter(@NonNull FragmentManager fm) {
        super(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new AddedRecipes();
            case 1:
                return  new Favorite();
            default:
                return null;
        }


    }

    @Override
    public int getCount() {
        return pageCount;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position] ;
    }
}
