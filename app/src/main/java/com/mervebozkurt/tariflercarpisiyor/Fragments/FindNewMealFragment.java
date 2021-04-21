package com.mervebozkurt.tariflercarpisiyor.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mervebozkurt.tariflercarpisiyor.R;
import com.mervebozkurt.tariflercarpisiyor.RecipeListActivity;

public class FindNewMealFragment extends Fragment implements View.OnClickListener{


    TextView mInstructionView;
    EditText mIngredient;
    Button mSearchRecipesButton;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mInstructionView=view.findViewById(R.id.instructionView);
        mIngredient=view.findViewById(R.id.ingredient);
        mSearchRecipesButton=view.findViewById(R.id.searchButton);
        mSearchRecipesButton.setOnClickListener(this);


    }
    @Override
    public void onClick(View v) {
        if (v == mSearchRecipesButton) {
            String ingredient = mIngredient.getText().toString();
           /* if(!(ingredient).equals("")) {
                addToSharedPreferences(ingredient);
            }*/
            Intent recipeintent = new Intent(getContext(), RecipeListActivity.class);
            recipeintent.putExtra("ingredient", ingredient);
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