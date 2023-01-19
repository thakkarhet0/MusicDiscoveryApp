package edu.uncc.finalexam.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import edu.uncc.finalexam.R;
import edu.uncc.finalexam.fragments.AddTrackFragment;
import edu.uncc.finalexam.fragments.AlbumFragment;
import edu.uncc.finalexam.fragments.AudioPlayerFragment;
import edu.uncc.finalexam.fragments.LoginFragment;
import edu.uncc.finalexam.models.Album;
import edu.uncc.finalexam.models.Track;
import okhttp3.HttpUrl;
import okhttp3.Request;

public class AlbumActivity extends CommonActivity implements AlbumFragment.IAlbum, AudioPlayerFragment.IAudioPlayer, AddTrackFragment.IAddTrack {

    Album album = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        if(getIntent() != null && getIntent().getExtras() != null && getIntent().hasExtra(MainActivity.ALBUM_KEY)){
            album = (Album) getIntent().getSerializableExtra(MainActivity.ALBUM_KEY);
        }

        sendAlbumFragment();
    }

    public void sendAlbumFragment(){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.albumView, AlbumFragment.newInstance(album))
                .commit();
    }

    public void sendAddTrackFragment(Track track){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.albumView, AddTrackFragment.newInstance(track))
                .addToBackStack(null)
                .commit();
    }

    public void sendAudioPlayerFragment(Track track){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.albumView, AudioPlayerFragment.newInstance(track))
                .addToBackStack(null)
                .commit();
    }

    public void sendTrackRequest(CommonActivity.APIResponse response, int album_id){
        Request request = new Request.Builder()
                .url(BASE_URL + "album/" + album_id + "/tracks")
                .build();

        sendRequest(request, response);
    }

}