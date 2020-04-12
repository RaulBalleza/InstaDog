package tech.rauballeza.instadog;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class FeedActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FeedRecyclerAdapter feedRecyclerAdapter;
    RecyclerView.LayoutManager layoutManager;

    //Firebase auth
    FirebaseAuth firebaseAuth;

    private ArrayList<FeedPost> FeedPosts = new ArrayList<FeedPost>();

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed);
        firebaseAuth = FirebaseAuth.getInstance();
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.home:
                        return true;
                    case R.id.search_button:
                        return true;
                    case R.id.upload:
                        startActivity(new Intent(getApplicationContext(), AddPostActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.likes:
                        return true;
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
        FeedPost fp1 = new FeedPost("RaulBalleza", "Chiaquiles", 1234, R.drawable.photo, R.drawable.steve);
        FeedPost fp2 = new FeedPost("FernandaBalleza", "Machimito", 10000000, R.drawable.photo, R.drawable.steve);
        FeedPosts.add(fp1);
        FeedPosts.add(fp2);
        recyclerView = findViewById(R.id.posts);
        layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        feedRecyclerAdapter = new FeedRecyclerAdapter(FeedPosts);
        recyclerView.setAdapter(feedRecyclerAdapter);
    }

    private void checkUserStatus(){
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null){

        }else{
            startActivity(new Intent(FeedActivity.this, MainActivity.class));
            finish();
        }
    }

    @Override
    protected void onStart() {
        checkUserStatus();
        super.onStart();
    }
}
