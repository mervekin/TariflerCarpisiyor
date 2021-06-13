package com.mervebozkurt.tariflercarpisiyor.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mervebozkurt.tariflercarpisiyor.Adapters.AddedRecipesAdapter;
import com.mervebozkurt.tariflercarpisiyor.Adapters.HomeRecyclerAdapter;
import com.mervebozkurt.tariflercarpisiyor.Models.Recipe;
import com.mervebozkurt.tariflercarpisiyor.R;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

import static com.google.firebase.firestore.FieldPath.documentId;

public class AddedRecipes extends Fragment {
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    ArrayList<String> MealImageFromFB;
    ArrayList<String> UserNameFromFB,UserIdFromFB;
    ArrayList<String> MealNameFromFB;
    ArrayList<String> CookingStepFromFB;
    ArrayList<String> CategoriesFromFB;
    ArrayList<String> MealPortionFromFB;
    ArrayList<String> MealScoreFromFB;
    ArrayList<String> MealTimeFromFB;
    ArrayList<String>MealIDFromFB;
    AddedRecipesAdapter addedRecipesAdapter;
    Context context;

    // We list the food recipes added by the user through this fragment.
    public AddedRecipes(){ }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context=getActivity();


        RecyclerView recyclerView = view.findViewById(R.id.recyclerview_for_addedrecipe);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        addedRecipesAdapter = new AddedRecipesAdapter(getActivity(),MealIDFromFB,UserNameFromFB,UserIdFromFB, MealNameFromFB, MealImageFromFB, MealPortionFromFB, MealScoreFromFB, MealTimeFromFB);
        recyclerView.setAdapter(addedRecipesAdapter);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//ArrayLists for added  recipes
        MealImageFromFB = new ArrayList<>();
        UserNameFromFB = new ArrayList<>();
        UserIdFromFB=new ArrayList<>();
        MealIDFromFB=new ArrayList<>();
        MealNameFromFB = new ArrayList<>();
        CookingStepFromFB = new ArrayList<>();
        CategoriesFromFB = new ArrayList<>();
        MealScoreFromFB = new ArrayList<>();
        MealPortionFromFB = new ArrayList<>();
        MealTimeFromFB = new ArrayList<>();

        // Inflate the layout for this fragment
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        getDataFromFireStore();

        return inflater.inflate(R.layout.fragment_added_recipes, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void getDataFromFireStore() {
        FirebaseUser user=firebaseAuth.getCurrentUser();
        final String uid=user.getUid();
        CollectionReference collectionReference = firebaseFirestore.collection("Recipes");
        collectionReference
                .whereEqualTo("Userid", uid)
                .orderBy("date", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                  // Toast.makeText(getContext(),error.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                }
                if (value != null) {
                    for (DocumentSnapshot snapshot : value.getDocuments()) {

                        Map<String, Object> data = snapshot.getData();

                        //Casting
                        String mealId= (String) data.get("documentId");
                        String username = (String) data.get("fname");
                        String category = (String) data.get("category");
                        String mealname = (String) data.get("mealname");
                        String userId= (String) data.get("Userid");
                        System.out.println(userId+"=="+uid);

                        String cookingstep = (String) data.get("cookingstep");
                        String cookingtime = (String) data.get("cookingtime");
                        String mealportion = (String) data.get("mealportion");
                        String downloadUrl = (String) data.get("downloadUrl");
                        System.out.println( mealname);
                        System.out.println(cookingtime);
                        UserNameFromFB.add(username);
                        UserIdFromFB.add(userId);
                        MealNameFromFB.add(mealname);
                        CookingStepFromFB.add(cookingstep);
                        CategoriesFromFB.add(category);
                        MealPortionFromFB.add(mealportion);
                        MealTimeFromFB.add(cookingtime);
                        MealImageFromFB.add(downloadUrl);
                        MealIDFromFB.add(mealId);
                        addedRecipesAdapter.notifyDataSetChanged();


                    }

                }
            }
        });


    }





}