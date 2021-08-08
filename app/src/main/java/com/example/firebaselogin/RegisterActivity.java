package com.example.firebaselogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
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
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText reg_fullname_et, reg_age_et, reg_email_et, reg_password_et;
    private ProgressBar reg_progressBar;
    private ImageView reg_show_hide_img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        reg_fullname_et = findViewById(R.id.reg_fullname_et);
        reg_age_et = findViewById(R.id.reg_age_et);
        reg_email_et = findViewById(R.id.reg_email_et);
        reg_password_et = findViewById(R.id.reg_password_et);
        reg_progressBar = findViewById(R.id.reg_progressBar);
        reg_show_hide_img = findViewById(R.id.reg_show_hide_img);

        Button register_btn = findViewById(R.id.register_btn);
        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });
    }

    private void register(){
        String email = reg_email_et.getText().toString().trim();
        String password = reg_password_et.getText().toString().trim();
        String age = reg_age_et.getText().toString().trim();
        String fullname = reg_fullname_et.getText().toString().trim();

        if(fullname.isEmpty()){
            reg_fullname_et.setError("Full name is required");
            reg_fullname_et.requestFocus();
            return;
        }

        if(age.isEmpty()){
            reg_age_et.setError("Age is required");
            reg_age_et.requestFocus();
            return;
        }

        if(email.isEmpty()){
            reg_email_et.setError("Email is required");
            reg_email_et.requestFocus();
            return;
        }

        if(password.isEmpty()){
            reg_password_et.setError("Password is required");
            reg_password_et.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            reg_email_et.setError("Please provide valid email address");
            reg_email_et.requestFocus();
        }

        if(password.length() < 6){
            reg_password_et.setError("Min password length should be at least 6 characters");
            reg_password_et.requestFocus();
        }

        reg_progressBar.setVisibility(View.VISIBLE);


        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            User user = new User(fullname, age, email);

                            FirebaseDatabase.getInstance("https://fir-login-b48ca-default-rtdb.europe-west1.firebasedatabase.app").getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(RegisterActivity.this, "User has been registered successfully", Toast.LENGTH_LONG).show();
                                        reg_progressBar.setVisibility(View.VISIBLE);
                                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                    }else{
                                        Toast.makeText(RegisterActivity.this,"Failed to register! Try again!", Toast.LENGTH_LONG).show();
                                        reg_progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });
                        }else{
                            Toast.makeText(RegisterActivity.this,"Failed to register! Try again!", Toast.LENGTH_LONG).show();
                            reg_progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }

    public void ShowHidePass(View view){

        if(view.getId()==R.id.reg_show_hide_img){

            if(reg_password_et.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                reg_show_hide_img.setImageResource(R.drawable.ic_hide_pass);

                //Show Password
                reg_password_et.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
            else{
                reg_show_hide_img.setImageResource(R.drawable.ic_show_pass);

                //Hide Password
                reg_password_et.setTransformationMethod(PasswordTransformationMethod.getInstance());

            }
        }
    }
}