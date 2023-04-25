package com.example.realtime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Home extends AppCompatActivity {

    TextView tvname,tvemail;
    Button button;
    FirebaseAuth fauth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        tvname = findViewById(R.id.tvname);
        tvemail = findViewById(R.id.tvemail);
        button = findViewById(R.id.button);
        fauth = FirebaseAuth.getInstance();
        userId = fauth.getCurrentUser().getUid();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        databaseReference.child(userId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){

                    if(task.getResult().exists()) {

                        Toast.makeText(Home.this, "read succesfull", Toast.LENGTH_SHORT).show();
                        DataSnapshot dataSnapshot = task.getResult();
                        String name  =  String.valueOf(dataSnapshot.child("name").getValue());
                        String email = String.valueOf(dataSnapshot.child("email").getValue());
                        tvname.setText(name);
                        tvemail.setText(email);

                    }
                    else{
                        Toast.makeText(Home.this, "user dont exist", Toast.LENGTH_SHORT).show();
                    }
                }else
                {
                    Toast.makeText(Home.this, "Failed to read", Toast.LENGTH_SHORT).show();
                }
            }
        });






    }
}