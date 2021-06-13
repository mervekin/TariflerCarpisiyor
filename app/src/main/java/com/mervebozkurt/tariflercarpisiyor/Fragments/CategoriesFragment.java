package com.mervebozkurt.tariflercarpisiyor.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.mervebozkurt.tariflercarpisiyor.Adapters.CategoryNamesAdapter;
import com.mervebozkurt.tariflercarpisiyor.R;

import java.util.ArrayList;
import java.util.Map;

// Fragment created for categories
public class CategoriesFragment extends Fragment {

    Context context;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    ArrayList<String> MealImageFromFB;
    ArrayList<String> MealNameFromFB;

    CategoryNamesAdapter categoryNamesAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        MealImageFromFB=new ArrayList<>();
        MealNameFromFB=new ArrayList<>();
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();

        getImage();

        return inflater.inflate(R.layout.fragment_categories, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView=view.findViewById(R.id.categoryRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //Adapter class object
        categoryNamesAdapter=new CategoryNamesAdapter(context,MealImageFromFB,MealNameFromFB);
        recyclerView.setAdapter(categoryNamesAdapter);


    }

    public void getImage(){
        CollectionReference collectionReference=firebaseFirestore.collection("CategoryNames");
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error!=null){
                    Toast.makeText(getContext(),error.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                }
                if(value!=null){
                    for (DocumentSnapshot snapshot:value.getDocuments()) {
                        Map<String, Object> data=snapshot.getData();

                        String mealname= (String) data.get("name");
                        String downloadurl= (String) data.get("url");
                        System.out.println(mealname);


                        MealNameFromFB.add(mealname);
                        MealImageFromFB.add(downloadurl);
                        categoryNamesAdapter.notifyDataSetChanged();

                    }

                }
            }
        });

    }


}