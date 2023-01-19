package edu.uncc.finalexam.activities;

import android.os.Bundle;

import edu.uncc.finalexam.R;
import edu.uncc.finalexam.fragments.AlbumFragment;
import edu.uncc.finalexam.fragments.AudioPlayerFragment;
import edu.uncc.finalexam.fragments.MixDetailsFragment;
import edu.uncc.finalexam.fragments.MixSharingFragment;
import edu.uncc.finalexam.fragments.MixesFragment;
import edu.uncc.finalexam.models.Album;
import edu.uncc.finalexam.models.Mix;
import edu.uncc.finalexam.models.Track;

public class MixActivity extends CommonActivity implements MixDetailsFragment.IMixDetails, AudioPlayerFragment.IAudioPlayer, MixSharingFragment.IMixShare {

    Mix mix = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mix);

        if(getIntent() != null && getIntent().getExtras() != null && getIntent().hasExtra(MainActivity.MIX_KEY)){
            mix = (Mix) getIntent().getSerializableExtra(MainActivity.MIX_KEY);
        }

        sendMixFragment();
    }

    public void sendMixFragment(){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.remixView, MixDetailsFragment.newInstance(mix))
                .commit();
    }

    public void sendAudioPlayerFragment(Track track){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.remixView, AudioPlayerFragment.newInstance(track))
                .addToBackStack(null)
                .commit();
    }

    public void sendMixShareFragment(Mix mix){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.remixView, MixSharingFragment.newInstance(mix))
                .addToBackStack(null)
                .commit();
    }

}