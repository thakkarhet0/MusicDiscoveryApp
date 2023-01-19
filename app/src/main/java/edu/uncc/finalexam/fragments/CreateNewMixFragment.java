package edu.uncc.finalexam.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

import edu.uncc.finalexam.R;
import edu.uncc.finalexam.models.Mix;
import edu.uncc.finalexam.models.Services;
import edu.uncc.finalexam.models.User;

public class CreateNewMixFragment extends Fragment {

    INewMix am;

    public interface INewMix{

        void goBack();

        void alert(String msg);

        String getUserId();

        void toggleDialog(boolean show);

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof INewMix){
            am = (INewMix)context;
        }else{
            throw new RuntimeException(context.toString());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_new_mix, container, false);

        getActivity().setTitle("Create New Mix");

        EditText mix_name = view.findViewById(R.id.editTextTextPersonName2);

        view.findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mixname = mix_name.getText().toString();
                if(mixname.isEmpty()){
                    am.alert("Please enter mix name!");
                    return;
                }

                HashMap<String, Object> mix = new HashMap<>();
                mix.put("name", mixname);
                mix.put("self_owner", true);
                mix.put("shared_by", null);
                mix.put("tracks", new ArrayList<>());
                am.toggleDialog(true);
                FirebaseFirestore db = FirebaseFirestore.getInstance();

                db.collection(Services.DB_USERS).document(am.getUserId()).collection(Services.DB_MIXES).add(mix).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        am.toggleDialog(false);
                        if(task.isSuccessful()){
                            Toast.makeText(getContext(), "Mix Created", Toast.LENGTH_LONG).show();
                            am.goBack();
                        }else{
                            task.getException().printStackTrace();
                        }
                    }
                });
            }
        });

        view.findViewById(R.id.textView16).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                am.goBack();
            }
        });

        return view;
    }
}