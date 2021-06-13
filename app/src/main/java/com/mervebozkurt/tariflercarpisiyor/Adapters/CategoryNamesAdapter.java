package com.mervebozkurt.tariflercarpisiyor.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mervebozkurt.tariflercarpisiyor.R;
import com.mervebozkurt.tariflercarpisiyor.UI.ShowRecipeByCategoryActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

//Adapter class created for category names and images
public class CategoryNamesAdapter extends RecyclerView.Adapter<CategoryNamesAdapter.ViewHolder> {
    Context context;

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

}
