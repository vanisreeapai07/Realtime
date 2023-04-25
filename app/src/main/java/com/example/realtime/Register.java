package com.example.realtime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    EditText name,email,pass;
    Button reg,log;
    FirebaseAuth fauth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.pass);
        reg = findViewById(R.id.reg);
        log = findViewById(R.id.log);
        fauth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sname = name.getText().toString();
                String semail = email.getText().toString();
                String spass = pass.getText().toString();

                if(TextUtils.isEmpty(sname)){
                    name.setError("Enter your name");
                    return;
                }
                if(TextUtils.isEmpty(semail)){
                    email.setError("Enter your email");
                    return;
                }
                if(TextUtils.isEmpty(spass)){
                    pass.setError("Enter your Password");
                    return;
                }

                fauth.createUserWithEmailAndPassword(semail,spass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){

                            FirebaseUser firebaseUser = fauth.getCurrentUser();
                            firebaseUser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(Register.this, "Verfictaion send", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Register.this, "Verfication cannot be send", Toast.LENGTH_SHORT).show();
                                }
                            });

                            Map<String,Object> user = new HashMap<>();
                            user.put("name",sname);
                            user.put("email",semail);
                            user.put("password",spass);


                            userId = fauth.getCurrentUser().getUid();
                            databaseReference.child("users").child(userId).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(Register.this, "data added Sucessfully", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Register.this, "Failed to add data"+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    }
                });


            }
        });

    }
}