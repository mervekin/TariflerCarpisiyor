package com.mervebozkurt.tariflercarpisiyor;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.Map;

public class EachMealRecipeActivity extends AppCompatActivity {

    private static final String TAG = "EachMealRecipeActivity";
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    Bundle bundle;
    String detailID;
    ImageView imageViewMeal;
    TextView MealName,UserName,CookingStep,Ingredients,Rating,CookingTime,MealScore;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate:called");
        setContentView(R.layout.activity_each_meal_recipe);

        imageViewMeal=findViewById(R.id.detail_image);
        MealName=findViewById(R.id.detail_name);
        UserName=findViewById(R.id.detail_user);
        CookingStep=findViewById(R.id.detail_cookstep);
        Ingredients=findViewById(R.id.detail_ingred);
        CookingTime=findViewById(R.id.detail_time);
        MealScore=findViewById(R.id.detail_score);
        Rating=findViewById(R.id.detail_rating);

        bundle=getIntent().getExtras();
        if(bundle!=null){
            detailID=bundle.getString("position");
        }

        //



        //her bir yemek için gerkli bilgileri firestoredan çekeceğiz.
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        getMealDetailFromFireStore();
    }
    public void getMealDetailFromFireStore(){

        CollectionReference collectionReference=firebaseFirestore.collection("Recipes");
        collectionReference.whereEqualTo("mealname",detailID).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if(error!=null){
                    Toast.makeText(EachMealRecipeActivity.this, error.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
                }
                if(value!=null){
                    for(DocumentSnapshot snapshot:value.getDocuments()){
                        Map<String,Object> data=snapshot.getData();

                        //Casting
                        String useremail =(String)data.get("useremail");
                        String username=(String)data.get("username");
                        String category=(String)data.get("category");
                        String mealname=(String)data.get("mealname");
                        String ingredientslist=(String)data.get("ingredientslist");
                        String cookingstep=(String)data.get("cookingstep");
                        String downloadUrl=(String)data.get("downloadUrl");
                        System.out.println(mealname);

                        Picasso.get().load(downloadUrl).into(imageViewMeal);
                        MealName.setText(mealname);
                        UserName.setText(username);
                        CookingStep.setText(cookingstep);
                        Ingredients.setText(ingredientslist);
                         //Rating
                        //CookingTime
                         // MealScore;



                    }
                }
            }
        });
    }




}