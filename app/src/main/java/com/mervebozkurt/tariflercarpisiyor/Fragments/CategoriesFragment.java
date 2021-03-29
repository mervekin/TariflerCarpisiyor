package com.mervebozkurt.tariflercarpisiyor.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ListView;

import com.mervebozkurt.tariflercarpisiyor.Adapters.CategoryNamesAdapter;
import com.mervebozkurt.tariflercarpisiyor.R;


public class CategoriesFragment extends Fragment {

    ListView listViewCategory;
    int imageArray[];
    String[] categoryNameArray;



    public CategoriesFragment() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imageArray=new int[]{R.drawable.tavuk,R.drawable.corba,R.drawable.et,R.drawable.makarna,R.drawable.pilav,R.drawable.hamur,R.drawable.tatli,R.drawable.kurabiye,R.drawable.pasta,R.drawable.salata};
        categoryNameArray=getResources().getStringArray(R.array.RecipesCategories);
        System.out.println("resim"+imageArray[3]);

        listViewCategory=view.findViewById(R.id.categoryListView);

        CategoryNamesAdapter adapter= new CategoryNamesAdapter(view.getContext(),categoryNameArray,imageArray);
        listViewCategory.setAdapter(adapter);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_categories, container, false);
    }
}