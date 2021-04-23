package com.mervebozkurt.tariflercarpisiyor.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mervebozkurt.tariflercarpisiyor.Fragments.HomeFragment;
import com.mervebozkurt.tariflercarpisiyor.R;
import com.mervebozkurt.tariflercarpisiyor.ShowRecipeByCategoryActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class CategoryNamesAdapter extends RecyclerView.Adapter<CategoryNamesAdapter.ViewHolder> {
    Context context;

    //String[] CategoryName;
    ArrayList<String> mealImageList;
    ArrayList<String> mealNameList;
    private View.OnClickListener mOnItemClickListener;
    private LayoutInflater layoutInflater;



    public CategoryNamesAdapter(Context context, ArrayList<String> mealImageList, ArrayList<String> mealNameList) {
        this.mealImageList=mealImageList;
        this.mealNameList=mealNameList;
        this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.category_listview_row,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        holder.textCat.setText(mealNameList.get(position));
        Picasso.get().load(mealImageList.get(position)).into(holder.imageCat);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(v.getContext(), ShowRecipeByCategoryActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("category", mealNameList.get(position));
                v.getContext().startActivity(intent);

            }
        });



    }


    @Override
    public int getItemCount() {
        return mealNameList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView imageCat;
        public TextView textCat;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageCat=itemView.findViewById(R.id.imageViewCategorylist);
            textCat=itemView.findViewById(R.id.textviewCategorylist);
        }
    }

    /*@SuppressLint("ViewHolder")
    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        final LayoutInflater inflater=LayoutInflater.from(context);
        view=inflater.inflate(R.layout.category_listview_row,null);

        ImageView imageCat=view.findViewById(R.id.imageViewCategorylist);
        TextView textCat=view.findViewById(R.id.textviewCategorylist);

        imageCat.setImageResource(CategoryImages[i]);
        textCat.setText(CategoryName[i]);

        imageCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, ShowRecipeByCategoryActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("category",CategoryName[i]);
                context.startActivity(intent);
            }
        });

        return view;
    }*/
}
