package com.mervebozkurt.tariflercarpisiyor.Profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mervebozkurt.tariflercarpisiyor.Login.SignInActivity;
import com.mervebozkurt.tariflercarpisiyor.R;
import com.squareup.picasso.Picasso;

public class ShowProfile extends AppCompatActivity {
    UploadTask uploadTask;

    private FirebaseAuth firebaseAuth;
    public FirebaseFirestore firebaseFirestore;
    public FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private DocumentReference documentReference;

    ImageView imageProfile;
    TextView tvName,tvSurname,tvPhone,tvMail;
    FloatingActionButton floatingActionButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_profile);
        floatingActionButton=findViewById(R.id.floatingbtn_sp);

        imageProfile=findViewById(R.id.show_imageView);
        tvName=findViewById(R.id.name_tv_sp);
        tvSurname=findViewById(R.id.surname_tv_sp);
        tvPhone=findViewById(R.id.phone_tv_sp);



        firebaseAuth=FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser()==null){
            finish();
            startActivity(new Intent(getApplicationContext(), SignInActivity.class));
        }
        firebaseStorage=FirebaseStorage.getInstance();//resim
        firebaseFirestore=FirebaseFirestore.getInstance();//bilgi

        documentReference=firebaseFirestore.collection("userinfo").document(firebaseAuth.getUid());
        storageReference=firebaseStorage.getInstance().getReference("profile image");

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ShowProfile.this,UpdateUserProfile.class));

            }
        });

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

                            Picasso.get().load(Url).into(imageProfile);
                            tvName.setText(name);
                            tvSurname.setText(surname);
                            tvPhone.setText(phone);


                        }
                        else{
                            Toast.makeText(ShowProfile.this,"Profil Yok",Toast.LENGTH_SHORT).show();
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    public void SelectProfileImage(View view) {
    }
}