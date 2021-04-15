package com.mervebozkurt.tariflercarpisiyor.Profile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mervebozkurt.tariflercarpisiyor.Fragments.HomeFragment;
import com.mervebozkurt.tariflercarpisiyor.Fragments.MyProfileFragment;
import com.mervebozkurt.tariflercarpisiyor.Login.SignInActivity;
import com.mervebozkurt.tariflercarpisiyor.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity  {

    private EditText Name,Surname,Phone;
    private TextView Email;
    ProgressBar progressBar;
    Button btnsave;
    private ImageView profilePicIV;

    Uri imageUri;
    private static final int PICk_IMAGE=1;
    Bitmap selectedpic;

    UploadTask uploadTask;

    private FirebaseAuth firebaseAuth;
    public   FirebaseFirestore firebaseFirestore;
    public FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private DocumentReference documentReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);


        profilePicIV=findViewById(R.id.update_imageView);
        Name=findViewById(R.id.EditTextName);
        Surname=findViewById(R.id.EditTextSurname);
        Phone=findViewById(R.id.EditTextPhone);
        btnsave=findViewById(R.id.btnSaveButton);
        progressBar=findViewById(R.id.progressbar_cp);

        firebaseAuth=FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser()==null){
            finish();
            startActivity(new Intent(getApplicationContext(), SignInActivity.class));
        }
        firebaseStorage=FirebaseStorage.getInstance();//resim
        firebaseFirestore=FirebaseFirestore.getInstance();//bilgi

        documentReference=firebaseFirestore.collection("userinfo").document(firebaseAuth.getUid());
        storageReference=firebaseStorage.getInstance().getReference("profile image");

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadData();
            }
        });

        //userInformation();


    }

    public void SelectProfileImage(View view){
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICk_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==PICk_IMAGE||requestCode==RESULT_OK||
        data!=null|| data.getData()!=null){
            imageUri=data.getData();
            Picasso.get().load(imageUri).into(profilePicIV);
        }

    }

    private String getFileExt(Uri uri){
        ContentResolver contentResolver=getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

    }
    private void UploadData(){
        final String name = Name.getText().toString().trim();
        final String surname = Surname.getText().toString().trim();
        final String phoneno = Phone.getText().toString().trim();

        if(!TextUtils.isEmpty(name)|| !TextUtils.isEmpty(surname)||!TextUtils.isEmpty(phoneno)){

            progressBar.setVisibility(View.VISIBLE);
            final  StorageReference reference=storageReference.child(System.currentTimeMillis()+"."+getFileExt(imageUri));

            uploadTask=reference.putFile(imageUri);
            Task<Uri> urlTask=uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }
                    return reference.getDownloadUrl();
                }
            })
                    .addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if(task.isSuccessful()){
                                Uri downloadUri=task.getResult();
                                HashMap<String,Object> profile=new HashMap<>();
                                profile.put("name",name);
                                profile.put("surname",surname);
                                profile.put("phone",phoneno);
                                profile.put("url",downloadUri.toString());

                                documentReference.set(profile)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {

                                                progressBar.setVisibility(View.INVISIBLE);
                                                Toast.makeText(EditProfileActivity.this,"Profil tamamlandı.",Toast.LENGTH_SHORT).show();
                                                Intent intent=new Intent(EditProfileActivity.this,ShowProfile.class);
                                                startActivity(intent);
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(EditProfileActivity.this,"hata",Toast.LENGTH_SHORT).show();

                                            }
                                        });

                            }

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
        }else{

            Toast.makeText(this,"Tüm alanlar tamamlanmalı",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        documentReference.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.getResult().exists()){
                            String name=task.getResult().getString("name");
                            String surname=task.getResult().getString("surname");
                            String phone=task.getResult().getString("phone");
                            String Url=task.getResult().getString("url");

                            Picasso.get().load(Url).into(profilePicIV);
                            Name.setText(name);
                            Surname.setText(surname);
                            Phone.setText(phone);


                        }
                        else{
                            Toast.makeText(EditProfileActivity.this,"Profil Yok",Toast.LENGTH_SHORT).show();
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    public void buttonClickedEditName(View view) {
    }

}