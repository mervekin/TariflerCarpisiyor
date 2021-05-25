package com.mervebozkurt.tariflercarpisiyor.Fragments;

import android.app.Activity;
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
    ArrayList<String> MealIDFromFB;
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

        MealIDFromFB=new ArrayList<>();
        CookingStepFromFB = new ArrayList<>();
        IngredientsListFromFB = new ArrayList<>();
        CategoriesFromFB = new ArrayList<>();
        MealScoreFromFB = new ArrayList<>();
        MealPortionFromFB = new ArrayList<>();
        MealTimeFromFB = new ArrayList<>();

        // Inflate the layout for this fragment
        firebaseFirestore = FirebaseFirestore.getInstance();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        getDataFromFireStore();


        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.recyclerviewforhome);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        homeRecyclerAdapter = new HomeRecyclerAdapter(getActivity(),UserNameFromFB, MealNameFromFB, MealImageFromFB,
                MealPortionFromFB, MealScoreFromFB, MealTimeFromFB,MealIDFromFB);
        recyclerView.setAdapter(homeRecyclerAdapter);
    }        //verileri homerecyler view de kullanmak için oraya gönderiyoruz

    //tüm veriler burada querysnapshotta veriler güncellendikçe buraya değişikliiikler yansıyor
    //verileri filtreleme yaparak çekmek istersek addSnapshotListenerdan önce belirteceğiz.
     public void getDataFromFireStore() {
        CollectionReference collectionReference = firebaseFirestore.collection("Recipes");
        collectionReference.orderBy("date", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
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
                        String mealId= (String) data.get("documentId");
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
                        MealIDFromFB.add(mealId);
                        System.out.println(MealPortionFromFB);
                        homeRecyclerAdapter.notifyDataSetChanged();


                    }

                }
            }
        });



    }



}