package edu.uncc.finalexam.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import edu.uncc.finalexam.R;
import edu.uncc.finalexam.adapters.MixListAdapter;
import edu.uncc.finalexam.fragments.LoginFragment;
import edu.uncc.finalexam.fragments.MixesFragment;
import edu.uncc.finalexam.fragments.SearchFragment;
import edu.uncc.finalexam.models.Album;
import edu.uncc.finalexam.models.Mix;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MainActivity extends CommonActivity implements SearchFragment.ISearch, MixesFragment.IMixes, MixListAdapter.IMixList {

    FirebaseAuth mAuth;
    ViewPager2 pager;
    ViewPagerAdapter v_adapter;
    TabLayout tab_layout;

    public static final String ALBUM_KEY = "album";
    public static final String MIX_KEY = "mix";

    public void sendSearchRequest(APIResponse response, String name){
        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL + "search/album").newBuilder();
        urlBuilder.addQueryParameter("q", name);

        Request request = new Request.Builder()
                .url(urlBuilder.build())
                .build();

        sendRequest(request, response);
    }

    public void startMixActivity(Mix mix){
        Intent intent = new Intent(MainActivity.this, MixActivity.class);
        intent.putExtra(MIX_KEY, mix);
        startActivity(intent);
    }

    public void startCreateNewMixActivity(){
        startActivity(new Intent(MainActivity.this, CreateNewMixActivity.class));
    }

    public void startAlbumActivity(Album album){
        Intent intent = new Intent(MainActivity.this, AlbumActivity.class);
        intent.putExtra(ALBUM_KEY, album);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pager = findViewById(R.id.vpager);
        v_adapter = new ViewPagerAdapter(this);
        pager.setAdapter(v_adapter);
        tab_layout = findViewById(R.id.tabLayout);
        tab_layout.setTabMode(TabLayout.MODE_SCROLLABLE);

        new TabLayoutMediator(tab_layout, pager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                if(position == 1){
                    tab.setText("Mixes");
                }else{
                    tab.setText("Search");
                }
            }
        }).attach();
    }

    public static class ViewPagerAdapter extends FragmentStateAdapter{

        public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            if(position == 1){
                return new MixesFragment();
            }else {
                return new SearchFragment();
            }
        }

        @Override
        public int getItemCount() {
            return 2;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.logoutbut) {
            mAuth = FirebaseAuth.getInstance();
            mAuth.signOut();
            startActivity(new Intent(MainActivity.this, AuthActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



}