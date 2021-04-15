package com.mervebozkurt.tariflercarpisiyor.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mervebozkurt.tariflercarpisiyor.EachMealRecipeActivity;
import com.mervebozkurt.tariflercarpisiyor.Fragments.HomeFragment;
import com.mervebozkurt.tariflercarpisiyor.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HomeRecyclerAdapter extends RecyclerView.Adapter<HomeRecyclerAdapter.PostHolder> {
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

    public HomeRecyclerAdapter(ArrayList<String> userNameList, ArrayList<String> mealNameList, ArrayList<String> mealImageList, ArrayList<String> mealPortionList,ArrayList<String> mealScoreList, ArrayList<String> mealTimeList) {
        this.userNameList = userNameList;
        this.mealNameList = mealNameList;
        this.mealImageList = mealImageList;
        this.mealScoreList = mealScoreList;
        this.mealTimeList = mealTimeList;
        this.mealPortionList = mealPortionList;


    }

    @NonNull
    @Override //view holder oluşturunca ne yapacagım.bir Post holder göderecek xml ve reccler birbirine bağlayacagız layout inflater ile
    public PostHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {

        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.home_recycler_row,parent,false);



        return new PostHolder(view);
    }

    @Override // view holdera bağlanınca ne yapacagım
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

    @Override // bizim recyler viewde kaç tane row olduğunu tutacagız.vereceği
    public int getItemCount() {
        return mealNameList.size();
    }

  /*  public void setOnItemClickListener(View.OnClickListener itemClickListener){
        mOnItemClickListener=itemClickListener;
    }*/

    //view holder:görünüm tutucu //oluşturduğumuz text viewleri burda tutacagız
    public class PostHolder extends RecyclerView.ViewHolder {

        ImageView MealImage;
        TextView MealName,UserName,MealPortion,MealScore,MealTime;




        public PostHolder(@NonNull View itemView) {
            super(itemView);

            UserName=itemView.findViewById(R.id.recyclerview_row_user_name);
            MealImage=itemView.findViewById(R.id.recyclerview_row_meal_imageView);
            MealName=itemView.findViewById(R.id.recyclerview_row_meal_name);
            MealPortion=itemView.findViewById(R.id.recyclerview_row_meal_portion);
            MealScore=itemView.findViewById(R.id.recyclerview_row_meal_score);
            MealTime=itemView.findViewById(R.id.recyclerview_row_meal_time);

            progressBar=itemView.findViewById(R.id.progressBar);


        }


    }



}
