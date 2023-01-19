package edu.uncc.finalexam.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import edu.uncc.finalexam.R;
import edu.uncc.finalexam.activities.CommonActivity;
import edu.uncc.finalexam.models.Album;
import edu.uncc.finalexam.models.Services;
import edu.uncc.finalexam.models.Track;

public class AlbumFragment extends Fragment {

    private static final String ALBUM = "album";

    private Album album;

    RecyclerView tracksview;

    IAlbum am;

    public static AlbumFragment newInstance(Album album) {
        AlbumFragment fragment = new AlbumFragment();
        Bundle args = new Bundle();
        args.putSerializable(ALBUM, album);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            album = (Album) getArguments().getSerializable(ALBUM);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof IAlbum){
            am = (IAlbum)context;
        }else{
            throw new RuntimeException(context.toString());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_album, container, false);
        am.setTitle("Album");

        Picasso.get().load(album.cover_big).into((ImageView) view.findViewById(R.id.imageView2)); // using cover big because cover small looks blurry
        Picasso.get().load(album.artist.picture_small).into((ImageView) view.findViewById(R.id.imageView3));
        TextView title = view.findViewById(R.id.textView4);
        title.setText(album.title);
        TextView artist = view.findViewById(R.id.textView5);
        artist.setText("Artist - " + album.artist.name);
        TextView tracks = view.findViewById(R.id.textView6);
        tracks.setText("Track List (" + album.nb_tracks + ") -");

        tracksview = view.findViewById(R.id.tracksview);
        tracksview.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        tracksview.setLayoutManager(llm);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(tracksview.getContext(),
                llm.getOrientation());
        tracksview.addItemDecoration(dividerItemDecoration);

        am.sendTrackRequest(new CommonActivity.APIResponse() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Services.Tracks tracks_op = new Services.Tracks(jsonObject, album.cover_big);

                tracksview.setAdapter(new RecyclerView.Adapter() {
                    @NonNull
                    @Override
                    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.track_item, parent, false);
                        return new RecyclerView.ViewHolder(view){};
                    }

                    @Override
                    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                        Track track = tracks_op.getTracks().get(position);

                        TextView title = holder.itemView.findViewById(R.id.textView7);
                        TextView duration = holder.itemView.findViewById(R.id.textView8);

                        holder.itemView.findViewById(R.id.imageView5).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                am.sendAddTrackFragment(track);
                            }
                        });

                        title.setText(track.title);

                        duration.setText(track.getReadableDuration());

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                am.sendAudioPlayerFragment(track);
                            }
                        });
                    }

                    @Override
                    public int getItemCount() {
                        return tracks_op.getTracks().size();
                    }
                });
            }

            @Override
            public void onError(JSONObject jsonObject) {
            }
        }, album.id);

        return view;
    }

    public interface IAlbum{

        void setTitle(CharSequence title);

        void sendTrackRequest(CommonActivity.APIResponse response, int album_id);

        void sendAddTrackFragment(Track track);

        void sendAudioPlayerFragment(Track track);

    }

}