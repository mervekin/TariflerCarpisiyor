package com.mervebozkurt.tariflercarpisiyor.Profile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mervebozkurt.tariflercarpisiyor.Login.SignInActivity;
import com.mervebozkurt.tariflercarpisiyor.R;
import com.squareup.picasso.Picasso;



public class UpdateUserProfile extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    public FirebaseFirestore firebaseFirestore;
    public FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private DocumentReference documentReference;

    private EditText etName,etSurname,etPhone,etInfo;
    private TextView Email;
    ProgressBar progressBar;
    Button btnsave;
    private ImageView imageProfile;

    Uri imageUri;
    private static final int PICk_IMAGE=1;
    UploadTask uploadTask;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_info);


        imageProfile=findViewById(R.id.update_imageView);
        etName=findViewById(R.id.et_name_up);
        etSurname=findViewById(R.id.et_surname_up);
        etPhone=findViewById(R.id.et_phone_up);
        etInfo=findViewById(R.id.et_info_up);
        btnsave=findViewById(R.id.btnSaveButton_up);
        progressBar=findViewById(R.id.progressbar_up);

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
                UpdateUser();
                onStart();
                Intent intent=new Intent(UpdateUserProfile.this,ShowProfile.class);
                intent.putExtra("Flag", "123");
                startActivity(intent);


            }
        });

        //userInformation();
    }



    public void ChooseImage(View view) {
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
            Picasso.get().load(imageUri).into(imageProfile);
        }

    }

    private String getFileExt(Uri uri){
        ContentResolver contentResolver=getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

    }

    //The changes made are taken from the user with the Transaction method and changed in the firestore collection.
    private void UpdateUser(){

        final String name = etName.getText().toString();
        final String surname = etSurname.getText().toString();
        final String phoneno = etPhone.getText().toString();
        final String info=etInfo.getText().toString();
        final String userid=firebaseAuth.getUid();

        if(imageUri!=null){
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
                                final Uri downloadUri=task.getResult();

                                // [START transactions]
                                final DocumentReference sfDocRef = firebaseFirestore.collection("userinfo").document(userid);

                                firebaseFirestore.runTransaction(new Transaction.Function<Void>() {
                                    @Override
                                    public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                                        DocumentSnapshot snapshot = transaction.get(sfDocRef);

                                        transaction.update(sfDocRef,"name",name);
                                        transaction.update(sfDocRef,"surname",surname);
                                        transaction.update(sfDocRef,"phone",phoneno);
                                        transaction.update(sfDocRef,"info",info);
                                        transaction.update(sfDocRef,"url",downloadUri.toString());





                                      //  transaction.update(sfDocRef, "population", newPopulation);

                                        return null;
                                    }
                                }).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                    }
                                })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                            }
                                        });
                                // [END transactions]


                            }

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
        }
        else{
            final DocumentReference sfDocRef = firebaseFirestore.collection("userinfo").document(userid);
            firebaseFirestore.runTransaction(new Transaction.Function<Void>() {
                @Override
                public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                    DocumentSnapshot snapshot = transaction.get(sfDocRef);

                    transaction.update(sfDocRef,"name",name);
                    transaction.update(sfDocRef,"surname",surname);
                    transaction.update(sfDocRef,"phone",phoneno);
                    transaction.update(sfDocRef,"info",info);
                    return null;
                }
            }).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });



        }
    }
    // contains information that exists to show the user after the activity has started
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
                            String info=task.getResult().getString("info");
                            //String Url=task.getResult().getString("url");

                           // Picasso.get().load(Url).into(imageProfile);
                            etName.setText(name);
                            etSurname.setText(surname);
                            etPhone.setText(phone);
                            etInfo.setText(info);


                        }
                        else{
                            Toast.makeText(UpdateUserProfile.this,"Profil Yok",Toast.LENGTH_SHORT).show();
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }
}