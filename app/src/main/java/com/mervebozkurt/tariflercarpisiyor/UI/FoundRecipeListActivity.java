package com.mervebozkurt.tariflercarpisiyor.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mervebozkurt.tariflercarpisiyor.Adapters.FindRecipeAdapter;
import com.mervebozkurt.tariflercarpisiyor.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//Activity where recipes are found and listed by material
public class FoundRecipeListActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    TextView categoryName,textview,ingredients;
    ArrayList<String> MealImageFromFB;
    ArrayList<String> UserNameFromFB,UserIdFromFB;
    ArrayList<String> MealNameFromFB ,MealIDFromFB;
    ArrayList<String> CookingStepFromFB;
    ArrayList<String> IngredientsListFromFB;
    ArrayList<String> CategoriesFromFB;
    ArrayList<String> MealPortionFromFB;
    ArrayList<String> MealScoreFromFB;
    ArrayList<String> MealTimeFromFB;
    ArrayList<String> cIngredientsListFromUser;
    FindRecipeAdapter findRecipeAdapter;
    String ing1,ing2,ing3,ing4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_founded_recipe_list);


        MealImageFromFB=new ArrayList<>();
        UserNameFromFB=new ArrayList<>();
        UserIdFromFB=new ArrayList<>();
        MealNameFromFB=new ArrayList<>();
        MealIDFromFB=new ArrayList<>();
        CookingStepFromFB=new ArrayList<>();
        IngredientsListFromFB=new ArrayList<>();
        CategoriesFromFB=new ArrayList<>();
        MealScoreFromFB=new ArrayList<>();
        MealPortionFromFB=new ArrayList<>();
        MealTimeFromFB=new ArrayList<>();
        cIngredientsListFromUser=new ArrayList<>();
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();

        textview=findViewById(R.id.uyarıtext);
        ingredients=findViewById(R.id.inputingred);

        //The materials entered by the user are taken here and compared in firestore.
        Intent recipeintent=getIntent();
        ing1=recipeintent.getStringExtra("key1");
        ing2=recipeintent.getStringExtra("key2");
        ing3=recipeintent.getStringExtra("key3");
        ing4=recipeintent.getStringExtra("key4");
        List<String> liste= Arrays.asList(ing1,ing2,ing3,ing4);
        cIngredientsListFromUser.addAll(liste);
        System.out.println(cIngredientsListFromUser);

        RecyclerView recyclerView =findViewById(R.id.recycler_findrecipe);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        findRecipeAdapter=new FindRecipeAdapter(UserIdFromFB,UserNameFromFB,MealNameFromFB,MealIDFromFB,MealImageFromFB,MealPortionFromFB,MealScoreFromFB,MealTimeFromFB);
        recyclerView.setAdapter(findRecipeAdapter);

        getRecipes();
    }

    private void getRecipes(){
        CollectionReference collectionReference=firebaseFirestore.collection("Recipes");
        //search part for ingredients
        collectionReference .whereArrayContainsAny("search",cIngredientsListFromUser) .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.isEmpty()){
                            textview.setVisibility(View.VISIBLE);
                            textview.setText(ing1 + " " + ing2 + ing3 + ing4 + " ile ilgili tarif bulunamadı");

                        }

                        for (QueryDocumentSnapshot doc:queryDocumentSnapshots){

                            ingredients.setText(ing1 +" "+ing2+" "+ing3+" "+ing4 +"");
                            //Casting
                            String username=(String)doc.getString("fname");
                            String userId=doc.getString("Userid");
                            String category=(String)doc.getString("category");
                            String mealname=(String)doc.getString("mealname");
                            String mealId=doc.getString("documentId");
                            String cookingstep=(String)doc.getString("cookingstep");
                            String cookingtime= (String)doc.getString("cookingtime");
                            String mealportion=(String)doc.getString("mealportion");
                            String downloadUrl=(String)doc.getString("downloadUrl");

                            if(categoryName!=null)
                                categoryName.setVisibility(View.VISIBLE);



                            // we assign the incoming data to the array
                            UserNameFromFB.add(username);
                            UserIdFromFB.add(userId);
                            MealNameFromFB.add(mealname);
                            MealIDFromFB.add(mealId);
                            CookingStepFromFB.add(cookingstep);

                            CategoriesFromFB.add(category);
                            MealPortionFromFB.add(mealportion);
                            MealTimeFromFB.add(cookingtime);
                            MealImageFromFB.add(downloadUrl);

                            findRecipeAdapter.notifyDataSetChanged();

                        }
                    }
                });




    }

    }
