package tech.rauballeza.instadog;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ProfileRecyclerAdapter profileRecyclerAdapter;
    RecyclerView.LayoutManager layoutManager;
    Button btn_more;
    FirebaseAuth firebaseAuth;
    TextView username;

    private int[] posts = {
            R.drawable.steve, R.drawable.steve, R.drawable.steve,
            R.drawable.steve, R.drawable.steve, R.drawable.steve,
            R.drawable.steve, R.drawable.steve, R.drawable.steve,
            R.drawable.steve, R.drawable.steve, R.drawable.steve,
            R.drawable.steve, R.drawable.steve, R.drawable.steve,
            R.drawable.steve, R.drawable.steve, R.drawable.steve};

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.profile_more, menu);
        return true;
    }*/

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
        firebaseAuth = FirebaseAuth.getInstance();

        username = findViewById(R.id.profile_user);
        username.setText(firebaseAuth.getCurrentUser().getEmail());

        btn_more = findViewById(R.id.btn_more);
        btn_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                checkUserStatus();
            }
        });

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
                        startActivity(new Intent(getApplicationContext(), AddPostActivity.class));
                        overridePendingTransition(0,0);
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

    private void checkUserStatus(){
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null){

        }else{
            startActivity(new Intent(ProfileActivity.this, MainActivity.class));
            finish();
        }
    }

    @Override
    protected void onStart() {
        checkUserStatus();
        super.onStart();
    }
}
