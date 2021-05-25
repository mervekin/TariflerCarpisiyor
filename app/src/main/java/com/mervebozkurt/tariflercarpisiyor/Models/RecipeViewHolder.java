package com.mervebozkurt.tariflercarpisiyor.Models;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mervebozkurt.tariflercarpisiyor.R;

public class RecipeViewHolder extends RecyclerView.ViewHolder {

    public ImageView MealImage;
    public TextView MealName,UserName,MealPortion,MealScore,MealTime;
    public ImageView DeleteRecipe,UpdateRecipe;
    public ProgressBar progressBar;



    public RecipeViewHolder(@NonNull View itemView) {
        super(itemView);
        UserName=itemView.findViewById(R.id.recyclerview_row_user_name);
        MealImage=itemView.findViewById(R.id.recyclerview_row_meal_imageView);
        MealName=itemView.findViewById(R.id.recyclerview_row_meal_name);
        MealPortion=itemView.findViewById(R.id.recyclerview_row_meal_portion);
        MealScore=itemView.findViewById(R.id.recyclerview_row_meal_score);
        MealTime=itemView.findViewById(R.id.recyclerview_row_meal_time);
        DeleteRecipe=itemView.findViewById(R.id.btn_delete);
        UpdateRecipe=itemView.findViewById(R.id.btn_update);


        progressBar=itemView.findViewById(R.id.progressBar);

    }
}
