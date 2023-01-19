package edu.uncc.finalexam.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import edu.uncc.finalexam.R;
import edu.uncc.finalexam.models.Mix;
import edu.uncc.finalexam.models.Services;

public class MixListAdapter extends RecyclerView.Adapter<MixListAdapter.UViewHolder> {

    ArrayList<Mix> mixes;

    IMixList am;

    FirebaseFirestore db;

    public MixListAdapter(ArrayList<Mix> mixes) {
        this.mixes = mixes;
    }

    @NonNull
    @Override
    public UViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mix_item, parent, false);
        am = (IMixList) parent.getContext();
        return new UViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UViewHolder holder, int position) {
        Mix mix = mixes.get(position);
        holder.position = position;

        holder.name.setText(mix.name + " Mix");
        holder.nb_tracks.setText(mix.tracks.size() + " Tracks");
        if(mix.self_owner){
            holder.creation.setText("Created By: Me");
        }else{
            holder.creation.setText("Shared By: " + mix.shared_by);
        }

        holder.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                am.toggleDialog(true);
                db = FirebaseFirestore.getInstance();
                CollectionReference ddb = db.collection(Services.DB_USERS).document(am.getUserId()).collection(Services.DB_MIXES);
                ddb.document(mix.getId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        am.toggleDialog(false);
                        if (task.isSuccessful()) {
                            am.alert("Mix deleted!");
                        } else {
                            task.getException().printStackTrace();
                        }
                    }
                });
            }
        });


        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                am.startMixActivity(mix);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.mixes.size();
    }

    public interface IMixList {

        void toggleDialog(boolean show);

        void alert(String msg);

        String getUserId();

        void startMixActivity(Mix mix);

    }

    public static class UViewHolder extends RecyclerView.ViewHolder {

        TextView name, nb_tracks, creation;
        ImageView del;
        View rootView;
        int position;

        public UViewHolder(@NonNull View itemView) {
            super(itemView);
            rootView = itemView;
            name = itemView.findViewById(R.id.textView12);
            nb_tracks = itemView.findViewById(R.id.textView13);
            creation = itemView.findViewById(R.id.textView14);
            del = itemView.findViewById(R.id.imageView7);
        }

    }

}

