package com.example.firebaselogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

import java.security.PrivateKey;

public class LoginActivity extends AppCompatActivity {
    private TextView register_txt, forgot_password_txt;
    private EditText email_et, password_et;
    private ProgressBar progressBar;
    private Button login_btn;
    private ImageView show_hide_img;


    private FirebaseAuth mAuth;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        forgot_password_txt = findViewById(R.id.forget_pass_txt);
        register_txt = findViewById(R.id.register_txt);
        email_et = findViewById(R.id.email_et);
        password_et = findViewById(R.id.password_et);
        progressBar = findViewById(R.id.progressBar);
        login_btn = findViewById(R.id.login_btn);
        mAuth = FirebaseAuth.getInstance();
        show_hide_img = findViewById(R.id.show_hide_img);





        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });


        forgot_password_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });


        register_txt.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void login(){
        String email = email_et.getText().toString().trim();
        String password = password_et.getText().toString().trim();

        if(email.isEmpty()){
            email_et.setError("Email is required");
            email_et.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            email_et.setError("Email is not valid");
        }

        if(password.isEmpty()){
            password_et.setError("Password is required");
            password_et.requestFocus();
            return;
        }

        if(password.length()< 6){
            password_et.setError("Min password length is 6 characters");
            password_et.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if(user.isEmailVerified()) {
                        //Toast.makeText(LoginActivity.this, "Succesfully loged in!", Toast.LENGTH_LONG).show();
                        Intent intent  = new Intent(LoginActivity.this, UserProfileActivity.class);
                        startActivity(intent);
                    }else{
                        user.sendEmailVerification();
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(LoginActivity.this, "Check your email to verify your account!", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(LoginActivity.this, "Failed to login! Please check your credentials", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void ShowHidePass(View view){

        if(view.getId()==R.id.show_hide_img){

            if(password_et.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                show_hide_img.setImageResource(R.drawable.ic_hide_pass);

                //Show Password
                password_et.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
            else{
                show_hide_img.setImageResource(R.drawable.ic_show_pass);

                //Hide Password
                password_et.setTransformationMethod(PasswordTransformationMethod.getInstance());

            }
        }
    }

}