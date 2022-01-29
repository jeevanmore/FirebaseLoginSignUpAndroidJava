package com.jaswikventures.firebaseloginsignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jaswikventures.firebaseloginsignup.databinding.ActivitySignUpBinding;

public class SignUpActivity extends AppCompatActivity {
    //activityBinding
    private ActivitySignUpBinding binding;

    private FirebaseAuth firebaseAuth;

    //progress dialog
    private ProgressDialog progressDialog;

    //actionBar
    private ActionBar actionBar;

    private String email="", password="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //config actionBar title, back button
        actionBar = getSupportActionBar();
        actionBar.setTitle("SignUp");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        //initialise firebase auth
        firebaseAuth = FirebaseAuth.getInstance();

        //configure progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Creating your account...");
        progressDialog.setCanceledOnTouchOutside(false);

        binding.signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed(); // go to previous activity when back button on actionBar is pressed.
        return super.onSupportNavigateUp();
    }

    public void validateData(){
        email = binding.emailEt.getText().toString().trim();
        password = binding.passwordEt.getText().toString().trim();

        if(email.isEmpty()){
            binding.emailEt.setError("This field cannot be empty");
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.emailEt.setError("Invalid Email format");
        }
        else if(password.isEmpty()) {
            binding.passwordEt.setError("This field can't be empty");
        } else if(password.length()<6 ){
            binding.passwordEt.setError("Password must be atleast 6 characters long");
        } else {
            //data is valid. Now continue to firebase signup.
            firebaseSignUp();
        }
    }

    private void firebaseSignUp() {
        //Show progress dialog
        progressDialog.show();
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                //Signup success
                progressDialog.dismiss();
                //get user info
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                firebaseUser.getEmail();

                Toast.makeText(SignUpActivity.this, "Account created \n"+ email, Toast.LENGTH_SHORT).show();
                        
                //open profile activity
                startActivity(new Intent(SignUpActivity.this,ProfileActivity.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //Signup failed
                progressDialog.dismiss();
                Toast.makeText(SignUpActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }
}