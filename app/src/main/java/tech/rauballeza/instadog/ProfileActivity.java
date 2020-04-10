package tech.rauballeza.instadog;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfileActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ProfileRecyclerAdapter profileRecyclerAdapter;
    RecyclerView.LayoutManager layoutManager;

    private int[] posts = {
            R.drawable.steve, R.drawable.steve, R.drawable.steve,
            R.drawable.steve, R.drawable.steve, R.drawable.steve,
            R.drawable.steve, R.drawable.steve, R.drawable.steve,
            R.drawable.steve, R.drawable.steve, R.drawable.steve,
            R.drawable.steve, R.drawable.steve, R.drawable.steve,
            R.drawable.steve, R.drawable.steve, R.drawable.steve};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        recyclerView = findViewById(R.id.posts);
        layoutManager = new GridLayoutManager(this, 3);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        profileRecyclerAdapter = new ProfileRecyclerAdapter(posts);
        recyclerView.setAdapter(profileRecyclerAdapter);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.profile);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), FeedActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.search_button:
                        return true;
                    case R.id.upload:
                        return true;
                    case R.id.likes:
                        return true;
                    case R.id.profile:
                        return true;

                }
                return false;
            }
        });
    }
}
