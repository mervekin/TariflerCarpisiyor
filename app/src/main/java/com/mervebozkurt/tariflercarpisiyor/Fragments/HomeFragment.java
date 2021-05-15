package com.mervebozkurt.tariflercarpisiyor.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.mervebozkurt.tariflercarpisiyor.Adapters.HomeRecyclerAdapter;
import com.mervebozkurt.tariflercarpisiyor.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    ArrayList<String> MealImageFromFB;
    ArrayList<String> UserNameFromFB;
    ArrayList<String> MealNameFromFB;
    ArrayList<String> CookingStepFromFB;
    List<Map<String, Object>> IngredientsListFromFB;
    ArrayList<String> CategoriesFromFB;
    ArrayList<String> MealPortionFromFB;
    ArrayList<String> MealScoreFromFB;
    ArrayList<String> MealTimeFromFB;
    ArrayList<String> UserIdFromFB;
    HomeRecyclerAdapter homeRecyclerAdapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        MealImageFromFB = new ArrayList<>();
        UserIdFromFB=new ArrayList<>();
        UserNameFromFB = new ArrayList<>();
        MealNameFromFB = new ArrayList<>();
        CookingStepFromFB = new ArrayList<>();
        IngredientsListFromFB = new ArrayList<>();
        CategoriesFromFB = new ArrayList<>();
        MealScoreFromFB = new ArrayList<>();
        MealPortionFromFB = new ArrayList<>();
        MealTimeFromFB = new ArrayList<>();

        // Inflate the layout for this fragment
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        getDataFromFireStore();


        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //RecyclerView

        RecyclerView recyclerView = view.findViewById(R.id.recyclerviewforhome);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //verileri homerecyler view de kullanmak için oraya gönderiyoruz
        homeRecyclerAdapter = new HomeRecyclerAdapter(UserNameFromFB, MealNameFromFB, MealImageFromFB, MealPortionFromFB, MealScoreFromFB, MealTimeFromFB);
        recyclerView.setAdapter(homeRecyclerAdapter);


    }


    public void getDataFromFireStore() {
        //tüm veriler burada querysnapshotta veriler güncellendikçe buraya değişikliiikler yansıyor
        //verileri filtreleme yaparak çekmek istersek addSnapshotListenerdan önce belirteceğiz.


        CollectionReference collectionReference = firebaseFirestore.collection("Recipes");
     /*   collectionReference.orderBy("date", Query.Direction.DESCENDING).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){

                            Recipe recipe=documentSnapshot.toObject(Recipe.class);
                            recipe.setDocumentId(documentSnapshot.getId());
                            String documentID=recipe.getDocumentId();
                            //UserNameFromFB.add(recipe.getUsername());
                            MealNameFromFB.add(recipe.getMealname());
                            CookingStepFromFB.add(recipe.getCookingstep());
                            CategoriesFromFB.add(recipe.getCategory());
                            MealPortionFromFB.add(recipe.getMealportion());
                            MealTimeFromFB.add(recipe.getCookingtime());
                            MealImageFromFB.add(recipe.getDownloadUrl());
                            System.out.println("ete"+MealPortionFromFB);
                            homeRecyclerAdapter.notifyDataSetChanged();

                        }
                    }
                });

*/
        /*collectionReference.orderBy("date", Query.Direction.DESCENDING).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>(){
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (DocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult())) {
                                 Recipe recipe = documentSnapshot.toObject(Recipe.class);
                                 recipe.setDocumentId(documentSnapshot.getId());

                                String documentId = recipe.getDocumentId();

                                IngredientsListFromFB.addAll(recipe.getIngredientlist());
                                //UserNameFromFB.add(recipe.getUsername());
                                MealNameFromFB.add(recipe.getMealname());
                                CookingStepFromFB.add(recipe.getCookingstep());
                                CategoriesFromFB.add(recipe.getCategory());
                                MealPortionFromFB.add(recipe.getMealportion());
                                MealTimeFromFB.add(recipe.getCookingtime());
                                MealImageFromFB.add(recipe.getDownloadUrl());
                                System.out.println(MealPortionFromFB);
                                homeRecyclerAdapter.notifyDataSetChanged();
                            }

                        }
                    }

                  /*  @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Recipe recipe = documentSnapshot.toObject(Recipe.class);
                            recipe.setDocumentId(documentSnapshot.getId());

                            String documentId = recipe.getDocumentId();

                            for (String i : recipe.getIngredientlist()) {
                                IngredientsListFromFB.add(i);
                            }
                            //UserNameFromFB.add(recipe.getUsername());
                            MealNameFromFB.add(recipe.getMealname());
                            CookingStepFromFB.add(recipe.getCookingstep());
                            CategoriesFromFB.add(recipe.getCategory());
                            MealPortionFromFB.add(recipe.getMealportion());
                            MealTimeFromFB.add(recipe.getCookingtime());
                            MealImageFromFB.add(recipe.getDownloadUrl());
                            System.out.println(MealPortionFromFB);
                            homeRecyclerAdapter.notifyDataSetChanged();
                        }


                    }/
                });
    }*/


          collectionReference.orderBy("date", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    //Toast.makeText(getContext(),error.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                }
                if (value != null) {
                    for (DocumentSnapshot snapshot : value.getDocuments()) {

                        Map<String, Object> data = snapshot.getData();

                        //Casting
                        String useremail = (String) data.get("useremail");
                        String username = (String) data.get("username");
                        String category = (String) data.get("category");
                        String mealname = (String) data.get("mealname");
                        String userid= (String) data.get("Userid");

                        //List<Map<String,Object>> ingredientslist = (List<Map<String, Object>>) snapshot.get("ingredientslist");
                       /* for(String i: recipe.getIngredientlist()){
                            ingredientslist.add(i);
                        }*/


                        String cookingstep = (String) data.get("cookingstep");
                        String cookingtime = (String) data.get("cookingtime");
                        String mealportion = (String) data.get("mealportion");
                        String downloadUrl = (String) data.get("downloadUrl");
                        System.out.println(cookingtime);

                        //array atadık gelen verileri
                        
                        UserNameFromFB.add(userid);
                        MealNameFromFB.add(mealname);
                        CookingStepFromFB.add(cookingstep);
                       // UserIdFromFB.add(userid);
                       // IngredientsListFromFB =ingredientslist;
                        CategoriesFromFB.add(category);
                        MealPortionFromFB.add(mealportion);
                        MealTimeFromFB.add(cookingtime);
                        MealImageFromFB.add(downloadUrl);
                        System.out.println(MealPortionFromFB);
                        homeRecyclerAdapter.notifyDataSetChanged();


                    }

                }
            }
        });



    }



}