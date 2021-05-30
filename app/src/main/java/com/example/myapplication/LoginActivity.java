package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    Button register, login;
    ImageView image;
    TextView text;
    TextInputLayout username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        image = findViewById(R.id.imageView);
        text = findViewById(R.id.textView);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        register = findViewById(R.id.register);
        login = findViewById(R.id.login);

        register.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            Pair[] pairs = new Pair[5];
            pairs[0] = new Pair<View, String>(image, "logo_image");
            pairs[1] = new Pair<View, String>(text, "logo_text");
            pairs[2] = new Pair<View, String>(username, "username_trans");
            pairs[3] = new Pair<View, String>(password, "password_trans");
            pairs[4] = new Pair<View, String>(register, "register_trans");
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this, pairs);
                startActivity(intent, activityOptions.toBundle());

            }
        });

        login.setOnClickListener(v -> {
            loginUser();
        });
    }

    private Boolean validateUsername(){
        String val = username.getEditText().getText().toString();
//        String noWhiteSpace = "\\A\\w{4,20}\\z";

        if (val.isEmpty()){
            username.setError("Username Tidak Boleh Kosong");
            return false;
//        } else if (val.length() >= 15){
//            username.setError("Username Terlalu Panjang");
//            return false;
//        } else if (!val.matches(noWhiteSpace)){
//            username.setError("Tidak Boleh Ada Spasi");
//            return false;
        } else {
            username.setError(null);
            username.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePassword(){
        String val = password.getEditText().getText().toString();

        if (val.isEmpty()){
            password.setError("Katasandi Tidak Boleh Kosong");
            return false;
        } else {
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }
    }

    public void loginUser(){
        if (!validateUsername() | !validatePassword()){
            return;
        } else {
            isUser();
        }
    }

    private void isUser(){
        final String enterUsername = username.getEditText().getText().toString().trim();
        final String enterPassword = password.getEditText().getText().toString().trim();

        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("users");

        Query checkUser = myRef.orderByChild("username").equalTo(enterUsername);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    username.setError(null);
                    username.setErrorEnabled(false);

                    String passwordDB = dataSnapshot.child(enterUsername).child("password").getValue(String.class);

                    if (passwordDB.equals(enterPassword)){
                        password.setError(null);
                        password.setErrorEnabled(false);

                        Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);

                        intent.putExtra("username", enterUsername);

                        startActivity(intent);
                    } else {
                        password.setError("Kata sandi salah");
                        password.requestFocus();
                    }
                } else {
                    username.setError("Usernama tidak terdaftar");
                    username.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
//                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    public void CallSignupScreen(View view){
    }
}