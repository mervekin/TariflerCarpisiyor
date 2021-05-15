package com.mervebozkurt.tariflercarpisiyor.Profile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.mervebozkurt.tariflercarpisiyor.MainActivity;
import com.mervebozkurt.tariflercarpisiyor.R;
import com.mervebozkurt.tariflercarpisiyor.UploadRecipeActivity;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class ShowProfile extends AppCompatActivity {
    UploadTask uploadTask;

    private FirebaseAuth firebaseAuth;
    public FirebaseFirestore firebaseFirestore;
    public FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private DocumentReference documentReference;

    ImageView imageProfile;
    TextView tvName,tvSurname,tvPhone,tvMail,tvInfo;
    FloatingActionButton floatingActionButton;
    ProgressBar progressBar;

    Uri imageUri;
    private static final int PICK_IMAGE=1;

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.recipe_option_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem refresh = menu.findItem(R.id.action_refresh);
        refresh.setVisible(firebaseAuth.getCurrentUser()!=null);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //menu seçeneklerine tıklayınca ne yapması gerektiğini göstereceğiz.

        if(item.getItemId()==R.id.UploadNewRecipe){
            startActivity(new Intent(ShowProfile.this, UploadRecipeActivity.class));
            finish();

        }
        else if(item.getItemId()==R.id.action_refresh){
            Intent reload=new Intent(getApplicationContext(),ShowProfile.class);
            startActivity(reload);
            finish();

        }
        else if(item.getItemId()==R.id.signout){
            firebaseAuth.signOut();
            startActivity(new Intent(ShowProfile.this, SignInActivity.class));
            finish();

        }
        else if (item.getItemId()==R.id.home){
            startActivity(new Intent(ShowProfile.this,MainActivity.class));

        }
        else if(item.getItemId()==R.id.editProfile){
            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.getResult().exists()){
                        startActivity(new Intent(ShowProfile.this, UpdateUserProfile.class));
                    }
                    else{
                        startActivity(new Intent(ShowProfile.this, EditProfileActivity.class));
                    }
                }
            });

        }


        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_profile);
        floatingActionButton=findViewById(R.id.floatingbtn_sp);

        imageProfile=findViewById(R.id.show_imageView);
        tvName=findViewById(R.id.name_tv_sp);
        tvSurname=findViewById(R.id.surname_tv_sp);
        tvPhone=findViewById(R.id.phone_tv_sp);
        tvMail=findViewById(R.id.mail_tv_sp);
        tvInfo=findViewById(R.id.info_tv_sp);
        progressBar=findViewById(R.id.progressBar1);

      //  imageProfile.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_account_box_black));
       Bundle bundle = getIntent().getExtras();
       if(bundle!=null){
           String flag=bundle.getString("Flag");
           System.out.println(flag);
           if (flag=="123") {
               progressBar.setVisibility(View.VISIBLE);
               onStart();
           }

        }
       progressBar.setVisibility(View.INVISIBLE);



        firebaseAuth=FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser()==null){
            finish();
            startActivity(new Intent(getApplicationContext(), SignInActivity.class));
        }
        firebaseStorage=FirebaseStorage.getInstance();//resim
        firebaseFirestore=FirebaseFirestore.getInstance();//bilgi

        documentReference=firebaseFirestore.collection("userinfo").document(firebaseAuth.getUid());
        storageReference=firebaseStorage.getInstance().getReference("profile image");
        System.out.println(firebaseAuth.getUid());


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
                            String email=task.getResult().getString("email");
                            String phone=task.getResult().getString("phone");
                            String info=task.getResult().getString("info");
                            String Url=task.getResult().getString("url");
                            Picasso.get().load(Url).into(imageProfile);



                            tvName.setText(name);
                            tvSurname.setText(surname);
                            tvPhone.setText(phone);
                            tvMail.setText(email);
                            tvInfo.setText(info);


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
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(requestCode==PICK_IMAGE||requestCode==RESULT_OK||
                data!=null|| data.getData()!=null){
            imageUri=data.getData();
            Picasso.get().load(imageUri).into(imageProfile);
            uploadImage();
        }

    }

    private String getFileExt(Uri uri){
        ContentResolver contentResolver=getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

    }
    private void uploadImage(){

            final  StorageReference reference=storageReference.child(System.currentTimeMillis()+"."+getFileExt(imageUri));
            uploadTask=reference.putFile(imageUri);
            Task<Uri> uriTask=uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(task.isSuccessful()){
                        throw task.getException();
                    }
                    return reference.getDownloadUrl();

                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if(task.isSuccessful()){
                                final Uri downloaduri=task.getResult();
                                HashMap<String,Object> profile=new HashMap<>();
                                profile.put("url",downloaduri.toString());
                                documentReference.set(profile)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(ShowProfile.this,"Profil tamamlandı.",Toast.LENGTH_SHORT).show();
                                        Picasso.get().load(downloaduri).into(imageProfile);

                                    }
                                })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(ShowProfile.this,"hata",Toast.LENGTH_SHORT).show();
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

        }
    }
