package com.mervebozkurt.tariflercarpisiyor.UI;

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
import com.mervebozkurt.tariflercarpisiyor.R;

import java.util.ArrayList;
import java.util.Map;

//Activity created for recipes listed according to the selected category

public class ShowRecipeByCategoryActivity extends AppCompatActivity {
    private FirebaseFirestore firebaseFirestore;
    TextView categoryName,NoRecipeText;

    ArrayList<String> cMealImageFromFB;
    ArrayList<String> cUserNameFromFB ,cUserIdFromFB;
    ArrayList<String> cMealNameFromFB,cMealIdFromFB;
    ArrayList<String> cCookingStepFromFB;
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
        cUserIdFromFB=new ArrayList<>();
        cMealNameFromFB=new ArrayList<>();
        cMealIdFromFB=new ArrayList<>();
        cCookingStepFromFB=new ArrayList<>();
        cCategoriesFromFB=new ArrayList<>();
        cMealPortionFromFB=new ArrayList<>();
        cMealScoreFromFB=new ArrayList<>();
        cMealTimeFromFB=new ArrayList<>();

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();

        bundle=getIntent().getExtras();
        if(bundle!=null){
            CategoryID=bundle.getString("category");
        }

        getRecipeByCategory();

        categoryName=findViewById(R.id.recycler_CategoryName);
        NoRecipeText=findViewById(R.id.withoutrecipetext);

        //RecyclerView operation

        RecyclerView recyclerView=findViewById(R.id.category_recylerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //Constructer object is created to use the data in the categoryrecyler adapter class.
        categoryRecyclerAdapter=new CategoryRecyclerAdapter(cUserNameFromFB,cUserIdFromFB,cMealNameFromFB,cMealIdFromFB,cMealImageFromFB,cMealPortionFromFB,cMealScoreFromFB,cMealTimeFromFB);
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
                        //
                        String username=(String)data.get("fname");
                        String userId= (String) data.get("Userid");
                        String category=(String)data.get("category");
                        String mealname=(String)data.get("mealname");
                        String mealId= (String) data.get("documentId");
                        String cookingstep=(String)data.get("cookingstep");
                        String cookingtime= (String)data.get("cookingtime");
                        String mealportion=(String)data.get("mealportion");
                        String downloadUrl=(String)data.get("downloadUrl");
                        System.out.println(category);
                        categoryName.setText(category);
                        if(categoryName!=null)
                            categoryName.setVisibility(View.VISIBLE);


                        //We assigned the data from the firestore to the array
                        cUserNameFromFB.add(username);
                        cUserIdFromFB.add(userId);
                        cMealNameFromFB.add(mealname);
                        cMealIdFromFB.add(mealId);
                        cCookingStepFromFB.add(cookingstep);
                        cCategoriesFromFB.add(category);
                        cMealPortionFromFB.add(mealportion);
                        cMealTimeFromFB.add(cookingtime);
                        cMealImageFromFB.add(downloadUrl);

                        categoryRecyclerAdapter.notifyDataSetChanged();


                    }
                }
                if(value==null) {
                        NoRecipeText.setVisibility(View.VISIBLE);

                }
            }
        });


    }
}