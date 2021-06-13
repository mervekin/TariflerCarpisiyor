package com.mervebozkurt.tariflercarpisiyor.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mervebozkurt.tariflercarpisiyor.R;
import com.mervebozkurt.tariflercarpisiyor.UI.RecipeDetailActivity;
import com.mervebozkurt.tariflercarpisiyor.Models.RecipeViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

//It is the adapter class created for the recycler view created to list the recipes added by the user.
//Here, the data in the Arraylists are listed in the recycler view.
public class AddedRecipesAdapter extends RecyclerView.Adapter<RecipeViewHolder> {

     ArrayList<String> mealIDlist;
     ArrayList<String> userNameList,userIdList;
     ArrayList<String> mealNameList;
     ArrayList<String> mealImageList;
     ArrayList<String> mealScoreList;
     ArrayList<String> mealTimeList;
     ArrayList<String> mealPortionList;

    Context context ;
    FirebaseStorage firebaseStorage;
    Activity activity;

    public AddedRecipesAdapter(Activity activity, ArrayList<String> mealIDList, ArrayList<String> userNameList, ArrayList<String> userIdList ,ArrayList<String> mealNameList, ArrayList<String> mealImageList, ArrayList<String> mealPortionList, ArrayList<String> mealScoreList, ArrayList<String> mealTimeList) {
      this.activity=activity;
        this.mealIDlist = mealIDList;
        this.userNameList = userNameList;
        this.userIdList=userIdList;
        this.mealNameList = mealNameList;
        this.mealImageList = mealImageList;
        this.mealScoreList = mealScoreList;
        this.mealTimeList = mealTimeList;
        this.mealPortionList = mealPortionList;


    }


    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_recycler_row, parent, false);
        context = view.getContext();
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecipeViewHolder holder, final int position) {


        if (mealTimeList != null) {
            holder.MealName.setText(mealNameList.get(position));
            holder.UserName.setText(userNameList.get(position));
            holder.MealPortion.setText(mealPortionList.get(position));
            holder.MealTime.setText(mealTimeList.get(position));
            holder.DeleteRecipe.setVisibility(View.VISIBLE);
            //holder.UpdateRecipe.setVisibility(View.VISIBLE);


            Picasso.get().load(mealImageList.get(position)).into(holder.MealImage);


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(view.getContext(), RecipeDetailActivity.class);
                    intent.putExtra("position", mealNameList.get(position));
                     intent.putExtra("userID",userIdList.get(position));
                    intent.putExtra("documentID", mealIDlist.get(position));
                   view.getContext().startActivity(intent);

                }
            });

            holder.UpdateRecipe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            holder.DeleteRecipe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                 AlertDialog.Builder alertDialogBuilderLabelDelete = new AlertDialog.Builder(v.getRootView().getContext());
                    alertDialogBuilderLabelDelete.setCancelable(false);
                    alertDialogBuilderLabelDelete.setTitle("Tarifi Sil");
                    alertDialogBuilderLabelDelete.setMessage("Silmek istediğinizden emin misiniz?");
                    alertDialogBuilderLabelDelete.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            deleteRecipe(position);
                            context.startActivity(new Intent(activity,activity.getClass()));

                        }
                    });
                    alertDialogBuilderLabelDelete.setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(context, "Okay", Toast.LENGTH_LONG).show();
                        }
                    });
                    AlertDialog alertDialog = alertDialogBuilderLabelDelete.create();
                    alertDialog.show();



                }
            });
        } else {
            System.out.println("hata var");
        }
    }


    @Override
    public int getItemCount() {
        return mealNameList.size();
    }
    // The user can delete the added recipes in this section.
     public void deleteRecipe( int position) {
        firebaseStorage = FirebaseStorage.getInstance();
        final StorageReference photoul = firebaseStorage.getReferenceFromUrl(mealImageList.get(position));

        FirebaseFirestore.getInstance().collection("Recipes")
                .document(mealIDlist.get(position))
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Good", "tarif Silindi...");
                        photoul.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("fd", "onSuccess:silindi");
                                Toast.makeText(activity,"başrılı",Toast.LENGTH_LONG).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("fd", "onFailure: did not delete file");
                                Toast.makeText(activity,"Başarısız",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Hata", "OnFailure", e);
            }
        });



    }
}