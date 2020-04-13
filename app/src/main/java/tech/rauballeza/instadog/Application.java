package tech.rauballeza.instadog;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Application extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.application);

        //Bottom Navigation View
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.home);

        //Default Fragment
        HomeFragment HomeFragment = new HomeFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content, HomeFragment);
        fragmentTransaction.commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.home:
                        HomeFragment homeFragment = new HomeFragment();
                        FragmentTransaction fragmentTransactionHome = getSupportFragmentManager().beginTransaction();
                        fragmentTransactionHome.replace(R.id.content, homeFragment);
                        fragmentTransactionHome.commit();
                        return true;
                    case R.id.search_button:
                        return true;
                    case R.id.upload:
                        startActivity(new Intent(Application.this, AddPostActivity.class));
                        return true;
                    case R.id.likes:
                        return true;
                    case R.id.profile:
                        ProfileFragment profileFragment = new ProfileFragment();
                        FragmentTransaction fragmentTransactionProfile = getSupportFragmentManager().beginTransaction();
                        fragmentTransactionProfile.replace(R.id.content, profileFragment);
                        fragmentTransactionProfile.commit();
                        return true;
                }
                return false;
            }
        });

    }
}
