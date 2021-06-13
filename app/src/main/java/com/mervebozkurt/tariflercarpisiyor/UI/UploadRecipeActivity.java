package com.mervebozkurt.tariflercarpisiyor.UI;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mervebozkurt.tariflercarpisiyor.Models.Recipe;
import com.mervebozkurt.tariflercarpisiyor.Profile.ShowProfile;
import com.mervebozkurt.tariflercarpisiyor.R;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

  //Adding New Recipe Page
public class UploadRecipeActivity extends AppCompatActivity {


    Bitmap selectedImage;
    Uri imageData;
    ImageView selectImage;
    EditText MealName,IngredientsList,CookingStep,CookingTime,MealPortion , textView;
    Spinner spinnerCategory;
    String ChoisenCate;
    String mealID, userID;
    ProgressBar progressBar;
    String mealName,cookingStep,cookingTime,mealPortion,downloadUrl;
    List<String> mylist;
    FieldValue date;
    List<String> search;

    private FirebaseStorage firebaseStorage;//image eklemek için
    private StorageReference storageReference; //referans
    private FirebaseFirestore firebaseFirestore;// verileri firestore eklerken
    private FirebaseAuth firebaseAuth; //güncel kullanıcı için kullanılacak
    CollectionReference collectionReference;
    FirebaseUser firebaseUser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_recipe);
        selectImage=findViewById(R.id.imageMeal);
        MealName=findViewById(R.id.editMealName);
        IngredientsList=findViewById(R.id.editIngredientsList);
        CookingStep=findViewById(R.id.editCookingStep);
        spinnerCategory=findViewById(R.id.spinnerCategory);
        CookingTime=findViewById(R.id.EditCookingTime);
        MealPortion=findViewById(R.id.EditMealPortion);
        progressBar=findViewById(R.id.progressBar);
        textView=findViewById(R.id.usernames);

        firebaseStorage=FirebaseStorage.getInstance();
        storageReference=firebaseStorage.getReference();
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
       collectionReference=firebaseFirestore.collection("Recipes");
       firebaseUser=firebaseAuth.getCurrentUser();

       //Spinner transactions created to select a category
        ArrayAdapter adapter=ArrayAdapter.createFromResource(this,R.array.RecipesCategories,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String[] categoryArray=getResources().getStringArray(R.array.RecipesCategories);
                ChoisenCate = (String)categoryArray[i];
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                ChoisenCate="Anne Tarifleri";

            }
        });


    }


    public void UploadRecipe(View view){

        progressBar.setVisibility(View.VISIBLE);
     if(imageData!=null && ChoisenCate!=null){
            // UUID = universal unique id
            UUID uuid=UUID.randomUUID();
            final String imageName="images/" + uuid + ".jpg";
            storageReference.child(imageName).putFile(imageData)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //Download URL
                    final StorageReference referenceforstorage=FirebaseStorage.getInstance().getReference(imageName);

                    referenceforstorage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            downloadUrl=uri.toString();
                            userID=firebaseUser.getUid();
                            String userEmail=firebaseUser.getEmail();
                            String username=firebaseUser.getDisplayName();

                            mealName = MealName.getText().toString();
                            cookingStep = CookingStep.getText().toString();
                            cookingTime = CookingTime.getText().toString();
                            mealPortion = MealPortion.getText().toString();
                            String ingredientsList = IngredientsList.getText().toString();
                            String[] ingrArray = ingredientsList.split("\\r?\\n");
                            mylist = Arrays.asList(ingrArray);
                            date = FieldValue.serverTimestamp();
                            String ingr = IngredientsList.getText().toString().toLowerCase();
                            String[] searchitem = ingr.split("\\s+");
                            search = Arrays.asList(searchitem);
                            mealID=collectionReference.document().getId();

                            //Object created from Recipe model class
                           final Recipe recipe=new Recipe(mealID,userID,userEmail,username,ChoisenCate,mealName,cookingStep,cookingTime,mealPortion,downloadUrl,mylist,date,search);

                            Toast.makeText(UploadRecipeActivity.this,"Başarılı",Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);

                            //Firestore data saving step
                            firebaseFirestore.collection("Recipes").document(mealID).set(recipe)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Intent intent=new Intent(UploadRecipeActivity.this, MainActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// means turn off all previous activities
                                            startActivity(intent);

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(UploadRecipeActivity.this,"hata",Toast.LENGTH_LONG).show();
                                    Intent intent=new Intent(UploadRecipeActivity.this, ShowProfile.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// means turn off all previous activities
                                    startActivity(intent);
                                }
                            });


                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UploadRecipeActivity.this,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                }
            });
        }




    }
    public void SelectImage(View view){

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }else{
            Intent intentToGallery=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intentToGallery,2);
        }

    }

    //

      // we asked for permission to access the gallery and what will we do
 @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==1){
            if(grantResults.length>0&& grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Intent intentToGallery=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intentToGallery,2);
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // went to the gallery, chose the picture, what to do, we check with the request code
        //data if the image exists, if the requestcode went to the gallery, if the result ok really approved the image
        // we need to convert the data to Url.
        if(requestCode==2 && resultCode==RESULT_OK && data!=null){
            imageData=data.getData(); // we need to take this and convert it as a bitmap.
            try {

                if(Build.VERSION.SDK_INT>=28){
                    ImageDecoder.Source source=ImageDecoder.createSource(this.getContentResolver(),imageData);
                    selectedImage=ImageDecoder.decodeBitmap(source);
                    selectImage.setImageBitmap(selectedImage);
                }else {
                    selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageData);
                    selectImage.setImageBitmap(selectedImage);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


        super.onActivityResult(requestCode, resultCode, data);
    }

}