package edu.uncc.finalexam.activities;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import edu.uncc.finalexam.R;
import edu.uncc.finalexam.fragments.LoginFragment;
import edu.uncc.finalexam.fragments.SignUpFragment;

public class AuthActivity extends CommonActivity implements LoginFragment.LoginInterface, SignUpFragment.RegInterface {

    /**
     * Assignment #Final Exam
     * AuthActivity.java
     * Ivy Pham
     */

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user == null) {
            sendLoginFragment();
        }else{
            startMainActivity();
        }
    }

    public void sendLoginFragment(){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new LoginFragment())
                .commit();
    }

    public void sendRegisterFragment(){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new SignUpFragment())
                .addToBackStack(null)
                .commit();
    }

    public void startMainActivity(){
        startActivity(new Intent(AuthActivity.this, MainActivity.class));
    }


}