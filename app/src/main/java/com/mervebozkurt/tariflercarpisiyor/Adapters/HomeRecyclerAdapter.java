package com.mervebozkurt.tariflercarpisiyor.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.mervebozkurt.tariflercarpisiyor.Fragments.MyProfileFragment;
import com.mervebozkurt.tariflercarpisiyor.R;
import com.mervebozkurt.tariflercarpisiyor.RecipeDetailActivity;
import com.mervebozkurt.tariflercarpisiyor.Models.RecipeViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HomeRecyclerAdapter extends RecyclerView.Adapter<RecipeViewHolder> {
    private ArrayList<String> userNameList;
    private ArrayList<String>  mealNameList;
    private ArrayList<String> mealIDList;
    private ArrayList<String>  mealImageList;
    private ArrayList<String>  mealScoreList;
    private ArrayList<String> mealTimeList;
    private ArrayList<String>  mealPortionList;
    ProgressBar progressBar;
    public FragmentActivity context;
    Activity activity;


    private View.OnClickListener mOnItemClickListener;
    //burdan bir obje oluşturlması gerekirse bunları vermesi gerekn bir bir constracture oluşturmak gerek

    public HomeRecyclerAdapter(Activity activity ,ArrayList<String> userNameList, ArrayList<String> mealNameList,
                               ArrayList<String> mealImageList, ArrayList<String> mealPortionList,
                               ArrayList<String> mealScoreList, ArrayList<String> mealTimeList, ArrayList<String> mealIDList) {
       this.activity=activity;
        this.userNameList = userNameList;
        this.mealNameList = mealNameList;
        this.mealImageList = mealImageList;
        this.mealScoreList = mealScoreList;
        this.mealTimeList = mealTimeList;
        this.mealPortionList = mealPortionList;
        this.mealIDList=mealIDList;


    }

    @NonNull
    @Override //view holder oluşturunca ne yapacagım.bir Post holder göderecek xml ve reccler birbirine bağlayacagız layout inflater ile
    public RecipeViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {

        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.home_recycler_row,parent,false);



        return new RecipeViewHolder(view);
    }

    @Override // view holdera bağlanınca ne yapacagım
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, final int position) {
        if(mealTimeList!=null) {
            holder.MealName.setText(mealNameList.get(position));
            holder.UserName.setText(userNameList.get(position));
            holder.MealPortion.setText(mealPortionList.get(position));
            holder.MealTime.setText(mealTimeList.get(position));
            Picasso.get().load(mealImageList.get(position)).into(holder.MealImage);

            //Tarif detay sayfasına gidiyor.
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent=new Intent(view.getContext(), RecipeDetailActivity.class);
                    intent.putExtra("position",mealNameList.get(position));
                    intent.putExtra("userID",userNameList.get(position));
                    intent.putExtra("documentID",mealIDList.get(position) );
                    System.out.println(mealNameList.get(position));
                    view.getContext().startActivity(intent);

                }
            });

            holder.UserName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    /*AppCompatActivity appCompatActivity= (AppCompatActivity) v.getContext();
                    Fragment myProfileFragment=new MyProfileFragment();
                    appCompatActivity.getSupportFragmentManager().beginTransaction() .replace(R.id.homefragment,myProfileFragment)
                            .addToBackStack(null).commit();*/
                    // pushFragment(myProfileFragment,context);

              /*MyProfileFragment fragment = new MyProfileFragment();
              FragmentManager fragmentManager = (context).getFragmentManager();
              fragmentManager.beginTransaction()
                      .replace(R.id.homefragment, fragment,fragment.getTag())
                      .commit();*/

                    //Fragment fragment= new FindNewMealFragment();
             /* Intent intent= new Intent(v.getContext(),new MyProfileFragment().getClass());
              intent.putExtra("userId",userNameList.get(position));
              System.out.println(userNameList.get(position));
              v.getContext().startActivity(intent);*/

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




}
