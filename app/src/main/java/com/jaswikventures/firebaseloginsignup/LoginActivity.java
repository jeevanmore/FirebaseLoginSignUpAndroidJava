package com.jaswikventures.firebaseloginsignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jaswikventures.firebaseloginsignup.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {
    //view binding
    private ActivityLoginBinding binding;

    //firebase auth
    private FirebaseAuth firebaseAuth;

    //progress dialog
    private ProgressDialog progressDialog;

    //actionBar
    private ActionBar actionBar;

    private String email="", password="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Configure action bar, title
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("LogIn");
        }

        //Configure progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Logging In");
        progressDialog.setCanceledOnTouchOutside(false);

        //Initialise firebase auth
        firebaseAuth = FirebaseAuth.getInstance();
        checkUser();


        //if have account, SignUp
        binding.haveAccountTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this ,SignUpActivity.class));
                finish();
            }
        });

        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();
            }
        });
    }

    private void checkUser() {
        //check if user is already logged in
        //get current user
        //if already logged in, then open profile activity
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser != null){
            //user is already logged in
            startActivity(new Intent(this, ProfileActivity.class));
            finish();
        }

    }

    //Method 1 : Validate User Data
    private void validateData() {
        email = binding.emailEt.getText().toString().trim();
        password = binding.passwordEt.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            binding.emailEt.setError("This field cannot be empty");
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.emailEt.setError("Invalid Email format");
        }
        else if(TextUtils.isEmpty(password)){
            binding.passwordEt.setError("This field cannot be empty");
        }
        else if(password.length() < 6){
            binding.passwordEt.setError("Password must be atleast 6 characters long");
        }
        else {
            //data is valid. Now continue to firebase LogIn.
            firebaseLogIn();
        }
    }

    //Method 2 : Firebase Log in
    private void firebaseLogIn(){
        //show progress
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                //login success
                progressDialog.dismiss();
                //get user info
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                email = firebaseUser.getEmail();
                //Toast a Logged In message
                Toast.makeText(LoginActivity.this, "LoggedIn\n"+email, Toast.LENGTH_SHORT).show();
                //open profile activity
                startActivity(new Intent(LoginActivity.this, ProfileActivity.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //login failed. get and show error message
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}