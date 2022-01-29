package com.jaswikventures.firebaseloginsignup;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jaswikventures.firebaseloginsignup.databinding.ActivityProfileBinding;

public class ProfileActivity extends AppCompatActivity {

    //view binding
    private ActivityProfileBinding binding;

    //actionBar
    private ActionBar actionBar;

    //firebase auth
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //configure action bar, title
        actionBar = getSupportActionBar();
        actionBar.setTitle("LogIn");

        //firebase auth
        firebaseAuth = FirebaseAuth.getInstance();
        checkUser();

        //logout user by clicking logout button
        binding.logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                checkUser();
            }
        });

    }

    private void checkUser() {
        //check if use is not logged in, then move to login activity

        //get current user
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser == null){
            //user not logged in, move to login screen
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
        else {
            //user logged in, get info
            String email = firebaseUser.getEmail();
            //set to emailtv
            binding.emailTv.setText(email);
        }
    }
}