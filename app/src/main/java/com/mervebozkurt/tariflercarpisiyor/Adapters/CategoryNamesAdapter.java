package com.mervebozkurt.tariflercarpisiyor.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mervebozkurt.tariflercarpisiyor.R;
import com.mervebozkurt.tariflercarpisiyor.ShowRecipeByCategoryActivity;

public class CategoryNamesAdapter extends BaseAdapter {
    Context context;
    String[] CategoryName;
    int[] CategoryImages;

    public CategoryNamesAdapter() {
    }

    public CategoryNamesAdapter(Context context,String[] categoryNameArray,int[] categoryImages){
        this.context=context;
        CategoryName=categoryNameArray;
        CategoryImages=categoryImages;

    }

    @Override
    public int getCount() {
        return CategoryName.length;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @SuppressLint("ViewHolder")
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
    }
}
