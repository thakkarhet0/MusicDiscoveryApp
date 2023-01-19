package edu.uncc.finalexam.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

import edu.uncc.finalexam.R;
import edu.uncc.finalexam.models.Mix;
import edu.uncc.finalexam.models.Services;
import edu.uncc.finalexam.models.Track;


public class AddTrackFragment extends Fragment {

    private static final String TRACK = "track";

    private Track track;

    ListView mix_view;

    IAddTrack am;

    public static AddTrackFragment newInstance(Track track) {
        AddTrackFragment fragment = new AddTrackFragment();
        Bundle args = new Bundle();
        args.putSerializable(TRACK, track);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof IAddTrack){
            am = (IAddTrack) context;
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
        View view = inflater.inflate(R.layout.fragment_add_track, container, false);
        am.setTitle("Add Track");
        TextView title = view.findViewById(R.id.textView10);
        title.setText(track.title);

        mix_view = view.findViewById(R.id.mix_view);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        am.toggleDialog(true);
        CollectionReference ddb = db.collection(Services.DB_USERS).document(am.getUserId()).collection(Services.DB_MIXES);
        ddb.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                am.toggleDialog(false);
                if (task.isSuccessful()) {
                    ArrayList<Mix> mixes = new ArrayList<>();
                    for (QueryDocumentSnapshot snapshot : task.getResult()){
                        Mix mix = snapshot.toObject(Mix.class);
                        mix.setId(snapshot.getId());
                        mixes.add(mix);
                    }list
                    ArrayAdapter<Mix> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, android.R.id.text1, mixes);
                    mix_view.setAdapter(adapter);
                    mix_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Mix mix = mixes.get(i);
                            if(mix.getTracks().contains(track)){
                                am.alert("Track already exists in that mix!");
                                return;
                            }
                            am.toggleDialog(true);
                            mix.addTrack(track);
                            HashMap<String, Object> upd = new HashMap<>();
                            upd.put("tracks", mix.getTracks());
                            ddb.document(mix.getId()).update(upd).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<Void> task) {
                                    am.toggleDialog(false);
                                    Toast.makeText(getContext(), "Track added successfully!", Toast.LENGTH_LONG).show();
                                    am.goBack();
                                }
                            });
                        }
                    });
                } else {
                    task.getException().printStackTrace();
                }
            }
        });
        return view;
    }

    public interface IAddTrack{

        void setTitle(CharSequence title);

        void toggleDialog(boolean show);

        void goBack();

        String getUserId();

        void alert(String msg);

    }

}