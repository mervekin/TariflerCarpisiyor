package com.mervebozkurt.tariflercarpisiyor.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mervebozkurt.tariflercarpisiyor.R;
import com.mervebozkurt.tariflercarpisiyor.UI.RecipeDetailActivity;
import com.mervebozkurt.tariflercarpisiyor.Models.RecipeViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
//It is the recyclerview adapter class created to list the recipes found by material.

public class FindRecipeAdapter extends RecyclerView.Adapter<RecipeViewHolder> {

    private ArrayList<String> mealNameList,mealIDList;
    private ArrayList<String> mealImageList=new ArrayList<>();
    private ArrayList<String> userNameList,userIdList;
    private ArrayList<String>  mealScoreList;
    private ArrayList<String> mealTimeList;
    private ArrayList<String>  mealPortionList;
    private Context mcontext;

    public FindRecipeAdapter(ArrayList<String> userIdList ,ArrayList<String> userNameList, ArrayList<String> mealNameList,ArrayList<String> mealIDList,ArrayList<String> mealImageList, ArrayList<String> mealPortionList,ArrayList<String> mealScoreList, ArrayList<String> mealTimeList) {
       this.userIdList=userIdList;
        this.userNameList = userNameList;
        this.mealNameList = mealNameList;
        this.mealIDList=mealIDList;
        this.mealImageList = mealImageList;
        this.mealScoreList = mealScoreList;
        this.mealTimeList = mealTimeList;
        this.mealPortionList = mealPortionList;


    }


    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.home_recycler_row,parent,false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, final int position) {

        if(mealTimeList!=null) {
            holder.MealName.setText(mealNameList.get(position));
            holder.UserName.setText(userNameList.get(position));
            holder.MealPortion.setText(mealPortionList.get(position));
            holder.MealTime.setText(mealTimeList.get(position));
            Picasso.get().load(mealImageList.get(position)).into(holder.MealImage);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent=new Intent(view.getContext(), RecipeDetailActivity.class);
                    intent.putExtra("position",mealNameList.get(position));
                    intent.putExtra("userID",userIdList.get(position));
                    intent.putExtra("documentID",mealIDList.get(position));

                    view.getContext().startActivity(intent);

                }
            });
        }
        else{
            System.out.println("hata var");
        }
    }

    @Override
    public int getItemCount() {
        return mealNameList.size();
    }
}
