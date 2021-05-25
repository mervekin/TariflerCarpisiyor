package com.mervebozkurt.tariflercarpisiyor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mervebozkurt.tariflercarpisiyor.Fragments.AddedRecipes;
import com.mervebozkurt.tariflercarpisiyor.Fragments.MyProfileFragment;
import com.mervebozkurt.tariflercarpisiyor.Models.Recipe;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecipeDetailActivity extends AppCompatActivity {

    private static final String TAG = "RecipeDetailActivity";
    private FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    CollectionReference collectionReference;
    Bundle bundle;
    String detailID,userID,mealID;
    ImageView imageViewMeal;
    TextView MealName, UserName, CookingStep, Ingredients, MealScore, CookingTime, MealPortion;

    RatingBar ratingBar;
   // List<String> ingredientslist;
    public float RatingNum;


    private ArrayAdapter<String> mIngredientsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate:called");
        setContentView(R.layout.activity_recipe_detail);
        imageViewMeal = findViewById(R.id.detail_image);
        MealName = findViewById(R.id.detail_name);
        UserName = findViewById(R.id.detail_user);
        CookingStep = findViewById(R.id.detail_cookstep);
        Ingredients = findViewById(R.id.detail_ingred);
        CookingTime = findViewById(R.id.detail_time);
        MealPortion = findViewById(R.id.detail_portion);
        MealScore = findViewById(R.id.detail_score);

        //Puanlama kısmı
        ratingBar = findViewById(R.id.rating_bar);


        bundle = getIntent().getExtras();
        if (bundle != null) {
            detailID = bundle.getString("position");
            userID = bundle.getString("userID");
            mealID=bundle.getString("documentID");
        }

        System.out.println("detailId"+detailID);
        System.out.println("mealId"+mealID);



        //her bir yemek için gerkli bilgileri firestoredan çekeceğiz.
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        user=firebaseAuth.getCurrentUser();
        getMealDetailFromFireStore();

        getRating();



    }

    public void getRating() {
        ratingBar.setVisibility(View.VISIBLE);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(final RatingBar ratingBar, float rating, boolean fromUser) {
                 UserName.setText("puanınız: " + (int) rating);
                RatingNum = rating;
                String totalStars = "Total Stars:: " + ratingBar.getNumStars();
                float count=Float.parseFloat(String.valueOf(rating));
                count+=rating;
                Toast.makeText(getApplicationContext(), totalStars + "\n" + rating, Toast.LENGTH_LONG).show();
                Map<String,Object> data=new HashMap<>();
                data.put("userID",userID);
                data.put("mealID",mealID);
                data.put("mealrating",RatingNum);
                firebaseFirestore.collection("Rating").document(mealID)
                        .set(data)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(RecipeDetailActivity.this,"Puan verildi,Teşekkürler",Toast.LENGTH_SHORT).show();
                                ratingBar.setVisibility(View.INVISIBLE);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(RecipeDetailActivity.this,"Hata!",Toast.LENGTH_SHORT).show();
                                Log.d("RecipeDetailActivity",e.toString());
                            }
                        });

            }
        });

    }


    public void getMealDetailFromFireStore() {

         collectionReference = firebaseFirestore.collection("Recipes");
         collectionReference.whereEqualTo("mealname",detailID).get()
                 .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                     @Override
                     public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                         for(QueryDocumentSnapshot doc :queryDocumentSnapshots){


                                List<String> ingred = (List<String>) doc.get("ingredientlist");
                                System.out.println(ingred);
                                Ingredients.setText("");
                                for (int j = 0; j < ingred.size(); j++) {
                                    Ingredients.append("    +  " + ingred.get(j) + "\n");
                                }

                                String downloadUrl = doc.getString("downloadUrl");
                                String useremail = doc.getString("useremail");
                                String username = doc.getString("username");
                                String userID=doc.getString("Userid");
                                String category = doc.getString("category");
                                String mealname = doc.getString("mealname");
                                String cookingstep = doc.getString("cookingstep");
                                String cookingtime = doc.getString("cookingtime");
                                String mealportion = doc.getString("mealportion");

                                MealName.setText(mealname);
                                Picasso.get().load(downloadUrl).into(imageViewMeal);
                                UserName.setText(username);
                                CookingStep.setText(cookingstep);
                                CookingTime.setText(cookingtime);
                                MealPortion.setText(mealportion);

                         }
                     }
                 });

             if(!mealID.isEmpty()){
                 System.out.println(" receipedetail MealId "+mealID);
                 //MealScore.setText("5");

             firebaseFirestore.collection("Rating").document(mealID)
                 .get()
                 .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                     @Override
                     public void onSuccess(DocumentSnapshot documentSnapshot) {
                         Number mealrating= (Number) documentSnapshot.get("mealrating");
                         System.out.println(mealrating+ " yeni veri ");
                         MealScore.setText(mealrating+"");
                     }

                 });




    }




             //bu kısım şüpheli tekrar bak
       /* firebaseFirestore.collection("users").document(userID)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String usernames= (String) documentSnapshot.get("username");
                        System.out.println(usernames+ "denemrfgrfg");
                        UserName.setText(usernames);
                        UserName.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Fragment fragment=new MyProfileFragment();
                               /* Intent intent= new Intent(getApplicationContext(), MyProfileFragment.class);
                                startActivity(intent);/

                            }
                        });

                    }
                });*/
    }
   }



