package com.example.firebaselogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import static android.content.ContentValues.TAG;

public class UserProfileActivity extends AppCompatActivity {

    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;

    private Button logout_btn;
    private TextView user_name_txt, user_age_txt, user_email_txt, welcome_message_txt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        logout_btn = findViewById(R.id.logout_btn);
        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(UserProfileActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance("https://fir-login-b48ca-default-rtdb.europe-west1.firebasedatabase.app").getReference("Users");
        userID = user.getUid();

        user_name_txt = findViewById(R.id.user_name_txt);
        user_age_txt = findViewById(R.id.user_age_txt);
        user_email_txt = findViewById(R.id.user_email_txt);
        welcome_message_txt = findViewById(R.id.welcome_message_txt);


        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if(userProfile != null){
                    String fullname = userProfile.fullname;
                    String email = userProfile.email;
                    String age = userProfile.age;

                    Log.e(TAG, "onDataChange: " +  fullname);
                    user_name_txt.setText(fullname);
                    user_age_txt.setText(age);
                    user_email_txt.setText(email);
                    welcome_message_txt.setText("Welcome " + fullname);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(UserProfileActivity.this, "Something Wrong happend", Toast.LENGTH_LONG).show();
            }
        });
    }
}