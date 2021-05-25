package com.mervebozkurt.tariflercarpisiyor.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mervebozkurt.tariflercarpisiyor.R;
import com.mervebozkurt.tariflercarpisiyor.FoundRecipeListActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FindNewMealFragment extends Fragment implements View.OnClickListener{


    TextView mInstructionView;
    EditText mIng1,mIng2,mIng3,mIng4;
    Button mSearchRecipesButton;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mInstructionView=view.findViewById(R.id.instructionView);
        mIng1=view.findViewById(R.id.ingredient1);
        mIng2=view.findViewById(R.id.ingredient2);
        mIng3=view.findViewById(R.id.ingredient3);
        mIng4=view.findViewById(R.id.ingredient4);
        mSearchRecipesButton=view.findViewById(R.id.searchButton);
        mSearchRecipesButton.setOnClickListener(this);


    }
    @Override
    public void onClick(View v) {
        if (v == mSearchRecipesButton) {
            String ingredient1 = mIng1.getText().toString();
            String ingredient2 = mIng2.getText().toString();
            String ingredient3 = mIng3.getText().toString();
            String ingredient4 = mIng4.getText().toString();

            List<String> liste= Arrays.asList(ingredient1,ingredient2,ingredient3,ingredient4);
            ArrayList<String> myList = new ArrayList<String>();
            myList.addAll(liste);
            System.out.println(myList);
            Intent recipeintent = new Intent(getContext(), FoundRecipeListActivity.class);
            //recipeintent.putExtra("ingredient", ingredient1);
            recipeintent.putExtra("key1", ingredient1);
            recipeintent.putExtra("key2",ingredient2);
            recipeintent.putExtra("key3",ingredient3);
            recipeintent.putExtra("key4",ingredient4);
            startActivity(recipeintent);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_find_new_meal, container, false);
    }
}