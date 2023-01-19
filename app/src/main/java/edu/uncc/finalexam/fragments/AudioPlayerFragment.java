package edu.uncc.finalexam.fragments;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;

import edu.uncc.finalexam.R;
import edu.uncc.finalexam.models.Album;
import edu.uncc.finalexam.models.Track;

public class AudioPlayerFragment extends Fragment {

    private static final String TRACK = "track";

    private Track track;

    MediaPlayer mediaPlayer;

    ImageView ctlr;

    boolean playing = false;

    IAudioPlayer am;

    public static AudioPlayerFragment newInstance(Track track) {
        AudioPlayerFragment fragment = new AudioPlayerFragment();
        Bundle args = new Bundle();
        args.putSerializable(TRACK, track);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof IAudioPlayer){
            am = (IAudioPlayer) context;
        }else{
            throw new RuntimeException(context.toString());
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            track = (Track) getArguments().getSerializable(TRACK);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_audio_player, container, false);
        am.setTitle("Audio Player");

        TextView title = view.findViewById(R.id.textView11);
        title.setText(track.title);

        String url = track.preview;
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioAttributes(
                new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
        );

        class Prepare extends AsyncTask<Void, Void, Void>{

            @Override
            protected void onPreExecute() {
                am.toggleDialog(true);
            }

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    mediaPlayer.setDataSource(url);
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void unused) {
                am.toggleDialog(false);
            }
        }

        new Prepare().execute();

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                playing = false;
                ctlr.setImageResource(android.R.drawable.ic_media_play);
            }
        });

        Picasso.get().load(track.getAlbum_cover()).into((ImageView) view.findViewById(R.id.imageView4));
        ctlr = view.findViewById(R.id.imageView6);
        ctlr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(playing){
                    playing = false;
                    mediaPlayer.pause();
                    ctlr.setImageResource(android.R.drawable.ic_media_play);
                }else{
                    playing = true;
                    mediaPlayer.start();
                    ctlr.setImageResource(android.R.drawable.ic_media_pause);
                }
            }
        });

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mediaPlayer.stop();
    }

    public interface IAudioPlayer{

        void toggleDialog(boolean show);

        void setTitle(CharSequence title);

    }
}