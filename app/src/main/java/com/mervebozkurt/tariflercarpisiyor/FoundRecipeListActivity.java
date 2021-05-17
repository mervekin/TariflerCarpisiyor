package com.mervebozkurt.tariflercarpisiyor;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mervebozkurt.tariflercarpisiyor.Adapters.CategoryRecyclerAdapter;
import com.mervebozkurt.tariflercarpisiyor.Adapters.FindRecipeAdapter;
import com.mervebozkurt.tariflercarpisiyor.Fragments.FindNewMealFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class FoundRecipeListActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    TextView categoryName,textview;
    ArrayList<String> MealImageFromFB;
    ArrayList<String> UserNameFromFB;
    ArrayList<String> MealNameFromFB;
    ArrayList<String> CookingStepFromFB;
    ArrayList<String> IngredientsListFromFB;
    ArrayList<String> CategoriesFromFB;
    ArrayList<String> MealPortionFromFB;
    ArrayList<String> MealScoreFromFB;
    ArrayList<String> MealTimeFromFB;
    ArrayList<String> cIngredientsListFromUser;
    FindRecipeAdapter findRecipeAdapter;
    String ing1,ing2,ing3,ing4;
    Bundle bundle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_founded_recipe_list);


        MealImageFromFB=new ArrayList<>();
        UserNameFromFB=new ArrayList<>();
        MealNameFromFB=new ArrayList<>();
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
        findRecipeAdapter=new FindRecipeAdapter(UserNameFromFB,MealNameFromFB,MealImageFromFB,MealPortionFromFB,MealScoreFromFB,MealTimeFromFB);
        recyclerView.setAdapter(findRecipeAdapter);

        getRecipes();
    }

    private void getRecipes(){
        CollectionReference collectionReference=firebaseFirestore.collection("Recipes");
        collectionReference .whereArrayContainsAny("search",cIngredientsListFromUser) .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.isEmpty()){
                            textview.setVisibility(View.VISIBLE);
                            textview.setText(ing1 + " " + ing2 + ing3 + ing4 + " ile ilgili tarif bulunamadı");
                            //startActivity(new Intent(FoundRecipeListActivity.this, FindNewMealFragment.class));
                           // finish();
                            //textview.setVisibility(View.INVISIBLE);

                        }

                        for (QueryDocumentSnapshot doc:queryDocumentSnapshots){

                            //Casting
                           // String useremail =(String)doc.getString("useremail");
                            String username=(String)doc.getString("fname");
                            String category=(String)doc.getString("category");
                            String mealname=(String)doc.getString("mealname");
                            //String ingredientslist=(String)data.get("ingredientslist");
                            String cookingstep=(String)doc.getString("cookingstep");
                            String cookingtime= (String)doc.getString("cookingtime");
                            String mealportion=(String)doc.getString("mealportion");
                            String downloadUrl=(String)doc.getString("downloadUrl");
                            System.out.println(mealname);
                            // categoryName.setText(category);
                            if(categoryName!=null)
                                categoryName.setVisibility(View.VISIBLE);
                            System.out.println("tarif"+ mealname);


                            //array atadık gelen verileri
                            UserNameFromFB.add(username);
                            MealNameFromFB.add(mealname);
                            CookingStepFromFB.add(cookingstep);
                            //IngredientsListFromFB.add(ingredientslist);
                            CategoriesFromFB.add(category);
                            MealPortionFromFB.add(mealportion);
                            MealTimeFromFB.add(cookingtime);
                            MealImageFromFB.add(downloadUrl);
                            System.out.println(MealPortionFromFB);
                            findRecipeAdapter.notifyDataSetChanged();

                        }
                    }
                });


               /* .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if(error!=null){
                    Toast.makeText(getApplicationContext(),"sorun ne"+error.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
                }
                if(value!=null){

                    for (DocumentSnapshot snapshot:value.getDocuments()){

                        Map<String,Object> data=snapshot.getData();

                        //Casting
                        String useremail =(String)data.get("useremail");
                        String username=(String)data.get("fname");
                        String category=(String)data.get("category");
                        String mealname=(String)data.get("mealname");
                        String ingredientslist=(String)data.get("ingredientslist");
                        String cookingstep=(String)data.get("cookingstep");
                        String cookingtime= (String)data.get("cookingtime");
                        String mealportion=(String)data.get("mealportion");
                        String downloadUrl=(String)data.get("downloadUrl");
                        System.out.println(mealname);
                       // categoryName.setText(category);
                        if(categoryName!=null)
                            categoryName.setVisibility(View.VISIBLE);
                        System.out.println("tarif"+ mealname);


                        //array atadık gelen verileri
                        UserNameFromFB.add(username);
                        MealNameFromFB.add(mealname);
                        CookingStepFromFB.add(cookingstep);
                        IngredientsListFromFB.add(ingredientslist);
                         CategoriesFromFB.add(category);
                        MealPortionFromFB.add(mealportion);
                        MealTimeFromFB.add(cookingtime);
                        MealImageFromFB.add(downloadUrl);
                        System.out.println(MealPortionFromFB);
                      findRecipeAdapter.notifyDataSetChanged();


                    }
                }
            }*
        });*/


    }

    }
