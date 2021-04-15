package com.mervebozkurt.tariflercarpisiyor;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.mervebozkurt.tariflercarpisiyor.Adapters.CategoryRecyclerAdapter;

import java.util.ArrayList;
import java.util.Map;


public class ShowRecipeByCategoryActivity extends AppCompatActivity {
    private static final String TAG = "BYCATEGORY";
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    TextView categoryName;

    ArrayList<String> cMealImageFromFB;
    ArrayList<String> cUserNameFromFB;
    ArrayList<String> cMealNameFromFB;
    ArrayList<String> cCookingStepFromFB;
    ArrayList<String> cIngredientsListFromFB;
    ArrayList<String> cCategoriesFromFB;
    ArrayList<String> cMealPortionFromFB;
    ArrayList<String> cMealScoreFromFB;
    ArrayList<String> cMealTimeFromFB;

    CategoryRecyclerAdapter categoryRecyclerAdapter;
    Bundle bundle;
    String CategoryID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_recipeby_category);

        cMealImageFromFB=new ArrayList<>();
        cUserNameFromFB=new ArrayList<>();
        cMealNameFromFB=new ArrayList<>();
        cCookingStepFromFB=new ArrayList<>();
        cIngredientsListFromFB=new ArrayList<>();
        cCategoriesFromFB=new ArrayList<>();
        cMealPortionFromFB=new ArrayList<>();
        cMealScoreFromFB=new ArrayList<>();
        cMealTimeFromFB=new ArrayList<>();

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();

        bundle=getIntent().getExtras();
        if(bundle!=null){
            CategoryID=bundle.getString("category");
        }

        getRecipeByCategory();

        categoryName=findViewById(R.id.recycler_CategoryName);

        //REcyclerView

        RecyclerView recyclerView=findViewById(R.id.category_recylerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //verileri homerecyler view de kullanmak için oraya gönderiyoruz
        categoryRecyclerAdapter=new CategoryRecyclerAdapter(cUserNameFromFB,cMealNameFromFB,cMealImageFromFB,cMealPortionFromFB,cMealScoreFromFB,cMealTimeFromFB);
        recyclerView.setAdapter(categoryRecyclerAdapter);



    }

    public void getRecipeByCategory(){

        CollectionReference collectionReference=firebaseFirestore.collection("Recipes");

        collectionReference.whereEqualTo("category",CategoryID).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if(error!=null){
                    Toast.makeText(getApplicationContext(),error.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
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
                        System.out.println(category);
                        categoryName.setText(category);
                        if(categoryName!=null)
                            categoryName.setVisibility(View.VISIBLE);


                        //array atadık gelen verileri
                        cUserNameFromFB.add(username);
                        cMealNameFromFB.add(mealname);
                        cCookingStepFromFB.add(cookingstep);
                        cIngredientsListFromFB.add(ingredientslist);
                        cCategoriesFromFB.add(category);
                        cMealPortionFromFB.add(mealportion);
                        cMealTimeFromFB.add(cookingtime);
                        cMealImageFromFB.add(downloadUrl);
                        System.out.println(cMealPortionFromFB);
                        categoryRecyclerAdapter.notifyDataSetChanged();


                    }
                }
            }
        });


    }
}