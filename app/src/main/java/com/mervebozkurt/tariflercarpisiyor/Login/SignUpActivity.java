package com.mervebozkurt.tariflercarpisiyor.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mervebozkurt.tariflercarpisiyor.MainActivity;
import com.mervebozkurt.tariflercarpisiyor.Profile.EditProfileActivity;
import com.mervebozkurt.tariflercarpisiyor.R;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {
    public static final String TAG = "TAG";
    private FirebaseAuth Auth;
    EditText mUserName,mFirstName,mSurname,mEmail,mPassword;
    Button mSignUpBtn;
    TextView mSignInbtn;
    ProgressBar progressBar;
    FirebaseFirestore fStore;
    String UserID;


    AlertDialog.Builder builder ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        mUserName=findViewById(R.id.TextUserName);
        mFirstName=findViewById(R.id.et_FirstName);
        mSurname=findViewById(R.id.et_SurName);
        mEmail=findViewById(R.id.TextEmail);
        mPassword=findViewById(R.id.TextPassword);
        mSignUpBtn=findViewById(R.id.btnSignUp);
        mSignInbtn=findViewById(R.id.TextSignIn);

        Auth = FirebaseAuth.getInstance();
        fStore=FirebaseFirestore.getInstance();
        progressBar=findViewById(R.id.progressBar);

        if(Auth.getCurrentUser()!=null){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
}

    public void SignUpClicked(View view){
        mSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email=mEmail.getText().toString().trim();
                String password=mPassword.getText().toString().trim();
                final  String UserName=mUserName.getText().toString();
                final String firstName=mFirstName.getText().toString();
                final String surname=mSurname.getText().toString();


                if (TextUtils.isEmpty(email)){
                    mEmail.setError("Email alanı boş bırakılamaz.");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    mPassword.setError("Parola alanı boş bırakılamaz.");
                    return;
                }
                if(password.length()<6){
                    mPassword.setError("Parola 6 karakterden fazla olmalıdır.");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                //firebase de kullanıcı oluşturma

                Auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //doğrulma linki gönder
                            FirebaseUser fuser=Auth.getCurrentUser();
                            assert fuser != null;
                            fuser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(SignUpActivity.this,"Doğrulama maili gönderilmiştir.",Toast.LENGTH_SHORT).show();

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG,"Hata: Mail gönderilemedi."+e.getMessage());
                                }
                            });
                            Toast.makeText(SignUpActivity.this,"Kullanıcı Oluşturuldu.",Toast.LENGTH_SHORT).show();

                            UserID=Auth.getCurrentUser().getUid();
                            String reference= System.currentTimeMillis()+"."+R.drawable.ic_action_account_box_black;
                            DocumentReference documentReference=fStore.collection("users").document(UserID);
                            Map<String,Object> user=new HashMap<>();
                            user.put("username",UserName);
                            user.put("name",firstName);
                            user.put("surname",surname);
                            user.put("email",email);
                            
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG,"Başarlı:kullanıcı profili oluşturuldu "+UserID);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG,"Hata"+e.toString());
                                }
                            });
                            Intent intent=new Intent(getApplicationContext(), EditProfileActivity.class);
                            intent.putExtra("userName",UserName);
                            startActivity(intent);
                            finish();
                        }else{
                            Toast.makeText(SignUpActivity.this,"Hata!"+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        });



    }
    public void SignInClicked(View view) {
        mSignInbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                finish();
            }
        });
    }

}