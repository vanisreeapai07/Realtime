package com.example.realtime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    EditText et1,et2;
    Button b1,b2;
    FirebaseAuth fauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et1 = findViewById(R.id.et1);
        et2 = findViewById(R.id.et2);
        b1 = findViewById(R.id.b1);
        b2 = findViewById(R.id.b2);
        fauth = FirebaseAuth.getInstance();

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Register.class));
            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String semail = et1.getText().toString();
                String spass = et2.getText().toString();
                if(TextUtils.isEmpty(semail)){
                    et1.setError("Enter your email");
                    return;
                }
                if(TextUtils.isEmpty(spass)){
                   et2.setError("Enter your Password");
                   return;
                }

                fauth.signInWithEmailAndPassword(semail,spass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(MainActivity.this, "Login sucess", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainActivity.this,Home.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Failed"+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }
}