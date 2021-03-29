package com.mervebozkurt.tariflercarpisiyor.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.mervebozkurt.tariflercarpisiyor.MainActivity;
import com.mervebozkurt.tariflercarpisiyor.R;

public class SignInActivity extends AppCompatActivity {
    EditText mEmail,mPassword;
    Button mSigninBtn;
    TextView mSignup,mforgetPass;
    private FirebaseAuth Auth;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);


        mEmail=findViewById(R.id.emailText);
        mPassword=findViewById(R.id.passwordText);
        Auth=FirebaseAuth.getInstance();
        mSigninBtn=findViewById(R.id.btnsingin);
        mSignup=findViewById(R.id.Textsingup);
        mforgetPass=findViewById(R.id.forgetPassword);
        progressBar=findViewById(R.id.progressBar);

        FirebaseUser firebaseUser=Auth.getCurrentUser();
        if(firebaseUser!=null){
            startActivity(new Intent(SignInActivity.this, MainActivity.class));
            finish();
        }


    }
    public void signInClicked(View view) {
        mSigninBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=mEmail.getText().toString().trim();
                String password=mPassword.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Email girilmesi gerekiyor!");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    mPassword.setError("Parola girilmesi gerekiyor!");
                    return;
                }
                if (password.length()<6){
                    mPassword.setError("parola 6 karakterden fazla olmalı");
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);

                //authenticate the user
                Auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(SignInActivity.this,"Giriş Başarılı",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                            finish();
                        }else{
                            Toast.makeText(SignInActivity.this,"Giriş Başarısız,tekrar deneyiniz!"+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        });



    }

    public void SignUpfromClicked(View view){
        mSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
            }
        });


    }
    public void ForgetPasswordClicked(View view){
        mforgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText resetMail= new EditText(view.getContext());
                final AlertDialog.Builder passwordResetDialog=new AlertDialog.Builder(view.getContext());
                passwordResetDialog.setTitle("Parolanızı resetleyecek misiniz?");
                passwordResetDialog.setMessage("Parola Sınırlama bağlantınızı almak için Email adresinizi giriniz");
                passwordResetDialog.setView(resetMail);
                passwordResetDialog.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String email =resetMail.getText().toString();
                        Auth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(SignInActivity.this,"Sıfırlama bağlantınız mailinize göderildi.",Toast.LENGTH_SHORT).show();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(SignInActivity.this,"Hata,Mail gönderilemedi.",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                passwordResetDialog.setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //dialog kapatılır.
                    }
                });
                passwordResetDialog.create().show();
            }
        });
    }




}