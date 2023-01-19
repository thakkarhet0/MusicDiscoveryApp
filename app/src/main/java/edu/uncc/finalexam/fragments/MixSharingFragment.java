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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

import edu.uncc.finalexam.R;
import edu.uncc.finalexam.models.Mix;
import edu.uncc.finalexam.models.Services;
import edu.uncc.finalexam.models.User;


public class MixSharingFragment extends Fragment {

    private static final String MIX_KEY = "MIX";

    private Mix mix;

    ListView user_view;

    IMixShare am;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof IMixShare) {
            am = (IMixShare) context;
        } else {
            throw new RuntimeException(context.toString());
        }
    }

    public static MixSharingFragment newInstance(Mix mix) {
        MixSharingFragment fragment = new MixSharingFragment();
        Bundle args = new Bundle();
        args.putSerializable(MIX_KEY, mix);
        fragment.setArguments(args);
        return fragment;
    }

    public interface IMixShare{

        void toggleDialog(boolean show);

        void alert(String msg);

        String getUserId();

        String getUserName();

        void goBack();

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
        View view = inflater.inflate(R.layout.fragment_mix_sharing, container, false);
        getActivity().setTitle("Mix Sharing");

        TextView mix_name = view.findViewById(R.id.textView22);
        mix_name.setText(mix.name + " Mix");

        user_view = view.findViewById(R.id.user_view);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        am.toggleDialog(true);
        db.collection(Services.DB_USERS).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                am.toggleDialog(false);
                if(task.isSuccessful()){
                    ArrayList<User> users = new ArrayList<>();
                    for (QueryDocumentSnapshot snapshot : task.getResult()){
                        if(snapshot.getId().equals(am.getUserId())) continue;

                        users.add(snapshot.toObject(User.class));
                    }
                    ArrayAdapter<User> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, android.R.id.text1, users);
                    user_view.setAdapter(adapter);
                    user_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            User user = users.get(i);
                            am.toggleDialog(true);
                            mix.self_owner = false;
                            mix.shared_by = am.getUserName();
                            db.collection(Services.DB_USERS).document(user.getId()).collection(Services.DB_MIXES).add(mix).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<DocumentReference> task) {
                                    am.toggleDialog(false);
                                    if(task.isSuccessful()){
                                        am.alert(mix.name + " Mix shared with " + user.display_name);
                                        am.goBack();
                                    }else{
                                        task.getException().printStackTrace();
                                    }
                                }
                            });
                        }
                    });
                }else{
                    task.getException().printStackTrace();
                }
            }
        });
        return view;
    }
}