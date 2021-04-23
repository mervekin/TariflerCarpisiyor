package com.mervebozkurt.tariflercarpisiyor;

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
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.SortedSet;
import java.util.UUID;

public class UploadRecipeActivity extends AppCompatActivity {


    Bitmap selectedImage;
    Uri imageData;
    ImageView selectImage;
    EditText MealName,IngredientsList,CookingStep,CookingTime,MealPortion;
    Spinner spinnerCategory;
    String ChoisenCate;



    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private FirebaseFirestore firebaseFirestore;// verileri firestore eklerken
    private FirebaseAuth firebaseAuth; //güncel kullanıcı için kullanılacak



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

        firebaseStorage=FirebaseStorage.getInstance();
        storageReference=firebaseStorage.getReference();
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();

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

            }
        });
    }



    public void UploadRecipe(View view){
        if(imageData!=null){
            //univarsal uniqui id
            UUID uuid=UUID.randomUUID();
            final String imageName="images/" + uuid + ".jpg";
            storageReference.child(imageName).putFile(imageData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //Download URL
                    StorageReference referenceforstorage=FirebaseStorage.getInstance().getReference(imageName);
                    referenceforstorage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            String downloadUrl=uri.toString();

                            FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
                                String userEmail=firebaseUser.getEmail();
                                String userName=firebaseUser.getDisplayName();
                                String uid=firebaseUser.getUid();






                            String mealName=MealName.getText().toString();
                            String cookingStep=CookingStep.getText().toString();
                            String ingredientsList=IngredientsList.getText().toString();
                            String cookingTime=CookingTime.getText().toString();
                            String mealPortion=MealPortion.getText().toString();

                            //hashmsaap oluşturacagız
                            HashMap<String,Object> recipeData=new HashMap<>();
                            recipeData.put("username",userName);
                            recipeData.put("useremail",userEmail);
                            recipeData.put("useruid",uid);
                            recipeData.put("downloadUrl",downloadUrl);
                            recipeData.put("category",ChoisenCate);
                            recipeData.put("mealname",mealName);
                            recipeData.put("cookingstep",cookingStep);
                            recipeData.put("ingredientslist",ingredientsList);
                            recipeData.put("mealportion",mealPortion);
                            recipeData.put("cookingtime",cookingTime);
                            recipeData.put("date", FieldValue.serverTimestamp());

                            Toast.makeText(UploadRecipeActivity.this,"Başarılı",Toast.LENGTH_SHORT).show();
                            //save to infromation database

                            firebaseFirestore.collection("Recipes").add(recipeData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    //kullanıcıyı main activitiye geri götürecegiz yani receipe yüklenmiş olacak bu ksımı sonra profil sayfasına götürebilirsin

                                    Intent intent=new Intent(UploadRecipeActivity.this,MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//ddaha önceki tüm activitiyleri kapat anlamına geliyor
                                    startActivity(intent);



                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(UploadRecipeActivity.this,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
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

//izin istedik ve ne yapcağız
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
        //galeriye gitti resmi seçti ne yapacagız request kod ile kontrol ediyoruz
        //data resim varsa,equestVode galeriye gittiyse result ok gereçekten resmi okayladıyse
        //datayı Url çevirmemiz gerekiyor.
        if(requestCode==2 && resultCode==RESULT_OK && data!=null){
            imageData=data.getData();   //bunu alıp bitmap olarak çevirmemiz gerekitor.
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