package tech.rauballeza.instadog;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
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

    private void checkUserStatus() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {

        } else {
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
