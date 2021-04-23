package com.mervebozkurt.tariflercarpisiyor.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.mervebozkurt.tariflercarpisiyor.Adapters.CategoryNamesAdapter;
import com.mervebozkurt.tariflercarpisiyor.Adapters.CategoryRecyclerAdapter;
import com.mervebozkurt.tariflercarpisiyor.R;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;


public class CategoriesFragment extends Fragment {

    ListView listViewCategory;
   // int imageArray[];
    //String[] categoryNameArray;

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


       // categoryNameArray=getResources().getStringArray(R.array.RecipesCategories);

        categoryNamesAdapter=new CategoryNamesAdapter(context,MealImageFromFB,MealNameFromFB);
        recyclerView.setAdapter(categoryNamesAdapter);



        //imageArray=new int[]{R.drawable.tavuk,R.drawable.corba,R.drawable.et,R.drawable.makarna,R.drawable.pilav,R.drawable.hamur,R.drawable.tatli,R.drawable.kurabiye,R.drawable.pasta,R.drawable.salata,R.drawable.anne};

       // System.out.println("resim"+imageArray[3]);


        //listViewCategory=view.findViewById(R.id.categoryListView);

       // CategoryNamesAdapter adapter= new CategoryNamesAdapter(view.getContext(),categoryNameArray,MealImageFromFB);
        //listViewCategory.setAdapter(adapter);

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