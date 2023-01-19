package edu.uncc.finalexam.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import edu.uncc.finalexam.R;
import edu.uncc.finalexam.models.User;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class CommonActivity extends AppCompatActivity {

    public final String BASE_URL = "https://api.deezer.com/";

    ProgressDialog dialog;

    FirebaseAuth mAuth;

    private final OkHttpClient client = new OkHttpClient();

    public void toggleDialog(boolean show){
        toggleDialog(show, null);
    }

    public String getUserId(){
        mAuth = FirebaseAuth.getInstance();
        return mAuth.getCurrentUser().getUid();
    }

    public String getUserName(){
        mAuth = FirebaseAuth.getInstance();
        return mAuth.getCurrentUser().getDisplayName();
    }

    public void toggleDialog(boolean show, String msg){
        if(show) {
            dialog = new ProgressDialog(this);
            if(msg == null)
                dialog.setMessage(getString(R.string.loading));
            else
                dialog.setMessage(msg);
            dialog.setCancelable(false);
            dialog.show();
        }else{
            dialog.dismiss();
        }
    }

    public void goBack(){
        getSupportFragmentManager().popBackStack();
    }

    public void alert(String alert){
        runOnUiThread(() -> new AlertDialog.Builder(this)
                .setTitle(R.string.info)
                .setMessage(alert)
                .setPositiveButton(R.string.okay, null)
                .show());
    }

    protected void sendRequest(Request request, APIResponse callback) {
        toggleDialog(true, getString(R.string.loading));
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    toggleDialog(false, null);
                    alert(e.toString());
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                runOnUiThread(() -> toggleDialog(false, null));

                ResponseBody responseBody = response.body();
                JSONObject jsonObject;
                if (responseBody != null) {
                    try {
                        jsonObject = new JSONObject(responseBody.string());
                        if(!jsonObject.has("error")){
                            runOnUiThread(() -> callback.onResponse(jsonObject));
                        }else{
                            if(jsonObject.has("message")) {
                                alert(jsonObject.getString("message"));
                            }
                            runOnUiThread(() -> callback.onError(jsonObject));
                        }
                    }catch (JSONException exception){
                        exception.printStackTrace();
                    }
                }
            }
        });
    }

    public interface APIResponse{

        void onResponse(JSONObject jsonObject);

        void onError(JSONObject jsonObject);

    }

}
