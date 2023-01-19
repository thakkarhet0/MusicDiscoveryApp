package edu.uncc.finalexam.activities;

import android.content.Intent;
import android.os.Bundle;

import edu.uncc.finalexam.R;
import edu.uncc.finalexam.fragments.CreateNewMixFragment;

public class CreateNewMixActivity extends CommonActivity implements CreateNewMixFragment.INewMix {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_mix);

        sendNewMixFragment();
    }

    public void sendNewMixFragment(){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.newMixView, new CreateNewMixFragment())
                .commit();
    }

    public void goBack(){
        startActivity(new Intent(CreateNewMixActivity.this, MainActivity.class));
    }

}