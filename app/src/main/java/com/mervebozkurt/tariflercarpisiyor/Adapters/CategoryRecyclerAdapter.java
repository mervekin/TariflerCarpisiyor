package com.mervebozkurt.tariflercarpisiyor.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mervebozkurt.tariflercarpisiyor.EachMealRecipeActivity;
import com.mervebozkurt.tariflercarpisiyor.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CategoryRecyclerAdapter extends RecyclerView.Adapter<CategoryRecyclerAdapter.PostHolder> {

    private static final String TAG = "HomeRecyclerAdapter";
    private LayoutInflater layoutInflater;
    private ArrayList<String> userNameList;
    private ArrayList<String>  mealNameList;
    private ArrayList<String>  mealImageList;
    private ArrayList<String>  mealScoreList;
    private ArrayList<String> mealTimeList;
    private ArrayList<String>  mealPortionList;
    ProgressBar progressBar;

    private View.OnClickListener mOnItemClickListener;
    //burdan bir obje oluşturlması gerekirse bunları vermesi gerekn bir bir constracture oluşturmak gerek

    public CategoryRecyclerAdapter(ArrayList<String> userNameList, ArrayList<String> mealNameList, ArrayList<String> mealImageList, ArrayList<String> mealPortionList,ArrayList<String> mealScoreList, ArrayList<String> mealTimeList) {
        this.userNameList = userNameList;
        this.mealNameList = mealNameList;
        this.mealImageList = mealImageList;
        this.mealScoreList = mealScoreList;
        this.mealTimeList = mealTimeList;
        this.mealPortionList = mealPortionList;
    }
    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.category_recycler_row,parent,false);
        //View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.category_recycler_row,parent,false);

        return new PostHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostHolder holder, final int position) {
        if(mealTimeList!=null) {
            holder.MealName.setText(mealNameList.get(position));
            holder.UserName.setText(userNameList.get(position));
            holder.MealPortion.setText(mealPortionList.get(position));
            holder.MealTime.setText(mealTimeList.get(position));

            Picasso.get().load(mealImageList.get(position)).into(holder.MealImage);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent=new Intent(view.getContext(), EachMealRecipeActivity.class);
                    intent.putExtra("position",mealNameList.get(position));

                    System.out.println(mealNameList.get(position));
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


    public class PostHolder extends RecyclerView.ViewHolder {

        ImageView MealImage;
        TextView MealName,UserName,MealPortion,MealScore,MealTime;

        public PostHolder(@NonNull View itemView) {
            super(itemView);

            UserName=itemView.findViewById(R.id.recyclerview_row_user_name_cv);
            MealImage=itemView.findViewById(R.id.recyclerview_row_meal_iv_ct);
            MealName=itemView.findViewById(R.id.recyclerview_row_meal_name_cv);
            MealPortion=itemView.findViewById(R.id.recyclerview_row_meal_portion_cv);
            MealScore=itemView.findViewById(R.id.recyclerview_row_meal_score_cv);
            MealTime=itemView.findViewById(R.id.recyclerview_row_meal_time_cv);

            progressBar=itemView.findViewById(R.id.progressBar_cv);


        }


    }

}
