package edu.uncc.finalexam.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

import edu.uncc.finalexam.R;
import edu.uncc.finalexam.models.Mix;
import edu.uncc.finalexam.models.Services;
import edu.uncc.finalexam.models.Track;


public class MixDetailsFragment extends Fragment {

    private static final String MIX_KEY = "mix";

    private Mix mix;

    IMixDetails am;

    RecyclerView track_list;

    TextView track_count;

    public static MixDetailsFragment newInstance(Mix mix) {
        MixDetailsFragment fragment = new MixDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable(MIX_KEY, mix);
        fragment.setArguments(args);
        return fragment;
    }

    public interface IMixDetails{

        String getUserId();

        void toggleDialog(boolean show);

        void sendAudioPlayerFragment(Track track);

        void sendMixShareFragment(Mix mix);

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof IMixDetails){
            am = (IMixDetails) context;
        }else{
            throw new RuntimeException(context.toString());
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mix = (Mix) getArguments().getSerializable(MIX_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mix_details, container, false);
        getActivity().setTitle("Mix Details");

        TextView mix_name = view.findViewById(R.id.textView17);
        mix_name.setText(mix.name);

        view.findViewById(R.id.imageView8).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                am.sendMixShareFragment(mix);
            }
        });

        track_count = view.findViewById(R.id.textView18);
        track_count.setText("Track List (" + mix.tracks.size() + " tracks)");

        track_list = view.findViewById(R.id.track_view);
        track_list.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        track_list.setLayoutManager(llm);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(track_list.getContext(),
                llm.getOrientation());
        track_list.addItemDecoration(dividerItemDecoration);

        track_list.setAdapter(new RecyclerView.Adapter() {
            @NotNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mix_detail_item, parent, false);
                return new RecyclerView.ViewHolder(view){};
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                Track track = mix.getTracks().get(position);
                TextView title = holder.itemView.findViewById(R.id.textView19);
                TextView duration = holder.itemView.findViewById(R.id.textView20);
                title.setText(track.title);
                duration.setText(track.getReadableDuration());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        am.sendAudioPlayerFragment(track);
                    }
                });
                holder.itemView.findViewById(R.id.imageView9).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        am.toggleDialog(true);
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        mix.tracks.remove(track);
                        HashMap<String, Object> upd = new HashMap<>();
                        upd.put("tracks", mix.getTracks());
                        db.collection(Services.DB_USERS).document(am.getUserId()).collection(Services.DB_MIXES).document(mix.getId()).update(upd).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                am.toggleDialog(false);
                                if(task.isSuccessful()) {
                                    notifyDataSetChanged();
                                    track_count.setText("Track List (" + mix.tracks.size() + " tracks)");
                                    Toast.makeText(getContext(), "Track removed", Toast.LENGTH_LONG).show();
                                }else{
                                    task.getException().printStackTrace();
                                }
                            }
                        });
                    }
                });
            }

            @Override
            public int getItemCount() {
                return mix.getTracks().size();
            }
        });

        return view;
    }
}