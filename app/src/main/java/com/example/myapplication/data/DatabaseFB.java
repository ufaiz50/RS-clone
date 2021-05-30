package com.example.myapplication.data;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class DatabaseFB {

    public void getUser(String getUsername){
        Query myRef = FirebaseDatabase.getInstance().getReference("users").orderByChild("username").equalTo(getUsername);

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child(getUsername).child("name").getValue(String.class);
                String username = snapshot.child(getUsername).child("username").getValue(String.class);
                String email = snapshot.child(getUsername).child("email").getValue(String.class);
                String phone = snapshot.child(getUsername).child("phone").getValue(String.class);
                GetDataUser dataUser = new GetDataUser(name, username, email, phone);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
