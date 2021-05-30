package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.util.UserHelperClass;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class SignupActivity extends AppCompatActivity {

    // Write a message to the database
    FirebaseDatabase database;
    DatabaseReference myRef;

    Button login, register;
    ImageView image;
    TextView text;
    TextInputLayout regName, regUsername, regEmail, regPhone, regPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        register = findViewById(R.id.register);
        login = findViewById(R.id.login);
        image = findViewById(R.id.imageView);
        text = findViewById(R.id.textView);
        regName = findViewById(R.id.name);
        regUsername = findViewById(R.id.username);
        regEmail = findViewById(R.id.email);
        regPhone = findViewById(R.id.phone);
        regPassword = findViewById(R.id.password);

        register.setOnClickListener(v -> {
            database = FirebaseDatabase.getInstance();
            myRef = database.getReference("users");

            registerUser();

        });

        login.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        });
    }

    private Boolean validateName(){
        String val = Objects.requireNonNull(regName.getEditText()).getText().toString();

        if (val.isEmpty()){
            regName.setError("Kolom tidak boleh kosong");
            return false;
        } else {
            regName.setError(null);
            regName.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateUsername(){
        String val = Objects.requireNonNull(regUsername.getEditText()).getText().toString();
        String whiteSpace = "\\A\\w{4,20}\\z";

        if (val.isEmpty()){
            regUsername.setError("Kolom tidak boleh kosong");
            return false;
        } else if (val.length() >= 15){
            regUsername.setError("Username terlalu panjang");
            return false;
        } else if (!val.matches(whiteSpace)){
            regUsername.setError("Username tidak boleh diberi spasi");
            return false;
        } else{
            regUsername.setError(null);
            regUsername.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateEmail(){
        String val = Objects.requireNonNull(regEmail.getEditText()).getText().toString();
        String emailPatt = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (val.isEmpty()){
            regEmail.setError("Kolom tidak boleh kosong");
            return false;
        } else if (!val.matches(emailPatt)){
            regEmail.setError("Email tidak benar");
            return false;
        } else {
            regEmail.setError(null);
            regEmail.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePhone(){
        String val = Objects.requireNonNull(regPhone.getEditText()).getText().toString();

        if (val.isEmpty()){
            regPhone.setError("Kolom tidak boleh kosong");
            return false;
        } else {
            regPhone.setError(null);
            regPhone.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePassword(){
        String val = regPassword.getEditText().getText().toString();
        String passwordpatt = "^" +
                "(?=.*[a-zA-z])" + //ada huruf
                "(?=.*[@#$%^&+=])" + //ada karakter special
                "(?=\\s+$)" + //tidak bole ada spasi
                ".{4,}" + //minimal 4 huruf
                "$";

        if (val.isEmpty()){
            regPassword.setError("Kolom tidak boleh kosong");
            return false;
//        } else if (!val.matches(passwordpatt)){
//            regPassword.setError("Passwod harus ada special karakter $ min 4 huruf");
//            return false;
        } else {
            regPassword.setError(null);
            regPassword.setErrorEnabled(false);
            return true;
        }
    }

    public void registerUser(){

        if (!validateName() | !validateUsername() | !validateEmail() | !validatePhone() | !validatePassword()){
            return;
        }

        //Get Data
        String name = Objects.requireNonNull(regName.getEditText()).getText().toString();
        String username = Objects.requireNonNull(regUsername.getEditText()).getText().toString();
        String email = Objects.requireNonNull(regEmail.getEditText()).getText().toString();
        String phone = Objects.requireNonNull(regPhone.getEditText()).getText().toString();
        String password = Objects.requireNonNull(regPassword.getEditText()).getText().toString();

        UserHelperClass helperClass = new UserHelperClass(name, username, email, phone, password);

        myRef.child(username).setValue(helperClass);
    }
}