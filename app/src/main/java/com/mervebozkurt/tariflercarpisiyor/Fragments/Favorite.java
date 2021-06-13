package com.mervebozkurt.tariflercarpisiyor.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mervebozkurt.tariflercarpisiyor.R;

//Dahil olmaya bir kısım ileride geliştirilecek
public class Favorite extends Fragment {

    public Favorite() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_favorite, container, false);
    }

    public void addFavoriteMeal(){

    }
}