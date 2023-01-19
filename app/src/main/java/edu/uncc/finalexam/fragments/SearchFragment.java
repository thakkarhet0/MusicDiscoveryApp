package edu.uncc.finalexam.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import edu.uncc.finalexam.R;
import edu.uncc.finalexam.activities.CommonActivity;
import edu.uncc.finalexam.models.Album;
import edu.uncc.finalexam.models.Services;

public class SearchFragment extends Fragment {

    ISearch am;

    RecyclerView search_view;

    EditText search_box;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof ISearch) {
            am = (ISearch) context;
        } else {
            throw new RuntimeException(context.toString());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        am.setTitle("Search");
    }

    public interface ISearch{

        void alert(String msg);

        void setTitle(CharSequence title);

        void sendSearchRequest(CommonActivity.APIResponse response, String name);

        void startAlbumActivity(Album album);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        search_box = view.findViewById(R.id.editTextTextPersonName);

        search_box.setText("Eminem");
        search_view = view.findViewById(R.id.searchView);
        search_view.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        search_view.setLayoutManager(llm);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(search_view.getContext(),
                llm.getOrientation());
        search_view.addItemDecoration(dividerItemDecoration);

        view.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String search = search_box.getText().toString();
                if(search.isEmpty()){
                    am.alert("Please enter search parameter");
                    return;
                }
                am.sendSearchRequest(new CommonActivity.APIResponse() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Services.Albums albums = new Services.Albums(jsonObject);

                        search_view.setAdapter(new RecyclerView.Adapter() {
                            @NonNull
                            @Override
                            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
                                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item, parent, false);
                                return new RecyclerView.ViewHolder(view){};
                            }

                            @Override
                            public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {
                                Album album = albums.getAlbums().get(position);
                                TextView title = holder.itemView.findViewById(R.id.textView);
                                TextView artist_name = holder.itemView.findViewById(R.id.textView2);
                                TextView nb_tracks = holder.itemView.findViewById(R.id.textView3);
                                ImageView cover = holder.itemView.findViewById(R.id.imageView);

                                title.setText(album.title);
                                artist_name.setText(album.artist.name);
                                nb_tracks.setText(album.nb_tracks + "");

                                if(album.cover_medium != null)
                                    Picasso.get().load(album.cover_medium).into(cover);

                                holder.itemView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        am.startAlbumActivity(album);
                                    }
                                });
                            }

                            @Override
                            public int getItemCount() {
                                return albums.getAlbums().size();
                            }
                        });
                    }

                    @Override
                    public void onError(JSONObject jsonObject) {
                    }
                }, search);
            }
        });


        return view;
    }

}