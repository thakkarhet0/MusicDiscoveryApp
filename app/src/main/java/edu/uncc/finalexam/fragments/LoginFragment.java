package edu.uncc.finalexam.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import edu.uncc.finalexam.R;
import edu.uncc.finalexam.models.User;


public class LoginFragment extends Fragment {

    LoginInterface am;
    EditText txt_email, txt_pass;
    Button log_btn;
    TextView new_view;
    private FirebaseAuth mAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof LoginInterface) {
            am = (LoginInterface) context;
        } else {
            throw new RuntimeException(context.toString());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        am.setTitle(R.string.login);
        txt_email = view.findViewById(R.id.enter_email);
        txt_pass = view.findViewById(R.id.enter_password);
        txt_email.setText("a@a.com");
        txt_pass.setText("test123");
        log_btn = view.findViewById(R.id.button_login);
        new_view = view.findViewById(R.id.create_new);
        log_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txt_email.getText().toString().isEmpty() || txt_pass.getText().toString().isEmpty()){
                    am.alert(getString(R.string.empty_fields));
                    return;
                }
                am.toggleDialog(true);
                mAuth = FirebaseAuth.getInstance();
                mAuth.signInWithEmailAndPassword(txt_email.getText().toString(), txt_pass.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                am.toggleDialog(false);
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    am.startMainActivity();
                                } else {
                                    am.alert(task.getException().getMessage());
                                }
                            }
                        });
            }
        });
        new_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                am.sendRegisterFragment();
            }
        });

        return view;
    }

    public interface LoginInterface{

        void alert(String msg);

        void toggleDialog(boolean show);

        void setTitle(int title_id);

        void sendRegisterFragment();

        void startMainActivity();

    }

}
