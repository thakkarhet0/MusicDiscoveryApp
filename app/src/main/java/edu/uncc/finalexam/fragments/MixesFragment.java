package edu.uncc.finalexam.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import edu.uncc.finalexam.R;
import edu.uncc.finalexam.adapters.MixListAdapter;
import edu.uncc.finalexam.models.Mix;
import edu.uncc.finalexam.models.Services;

public class MixesFragment extends Fragment {

    IMixes am;

    RecyclerView all_mixes;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof IMixes) {
            am = (IMixes) context;
        } else {
            throw new RuntimeException(context.toString());
        }
    }

    @Override
    public void onResume() {
        am.setTitle("Mixes");
        super.onResume();
    }

    public interface IMixes{

        void setTitle(CharSequence title);

        void toggleDialog(boolean show);

        String getUserId();

        void startCreateNewMixActivity();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mixes, container, false);

        all_mixes = view.findViewById(R.id.all_mixes);
        all_mixes.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        all_mixes.setLayoutManager(llm);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(all_mixes.getContext(),
                llm.getOrientation());
        all_mixes.addItemDecoration(dividerItemDecoration);

        view.findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                am.startCreateNewMixActivity();
            }
        });
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        am.toggleDialog(true);
        db.collection(Services.DB_USERS).document(am.getUserId()).collection(Services.DB_MIXES).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable QuerySnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (value == null) {
                    return;
                }
                am.toggleDialog(false);
                ArrayList<Mix> mixes = new ArrayList<>();
                for (QueryDocumentSnapshot doc : value) {
                    Mix mix = doc.toObject(Mix.class);
                    mix.setId(doc.getId());
                    mixes.add(mix);
                }
                all_mixes.setAdapter(new MixListAdapter(mixes));
            }
        });

        return view;
    }
}