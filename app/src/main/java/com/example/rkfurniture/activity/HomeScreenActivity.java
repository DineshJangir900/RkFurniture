package com.example.rkfurniture.activity;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.rkfurniture.R;
import com.example.rkfurniture.activity.ui.dashboard.DashboardFragment;
import com.example.rkfurniture.activity.ui.home.HomeFragment;
import com.example.rkfurniture.databinding.ActivityHomeScreenBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class HomeScreenActivity extends AppCompatActivity {
    private ActivityHomeScreenBinding mBindings;
    private FragmentTransaction fragmentTransaction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBindings = ActivityHomeScreenBinding.inflate(getLayoutInflater());
        setTheme(R.style.fullScreenDesign);
        setContentView(mBindings.getRoot());


        mBindings.navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        loadFragment(new HomeFragment());

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            switch (item.getItemId()){
                case R.id.navigation_home:
                    fragment = new HomeFragment();
                    loadFragment(fragment);
                    break;
                case R.id.navigation_dashboard:
                    fragment = new DashboardFragment();
                    loadFragment(fragment);
                    break;

            }
            return true;
        }
    };



    private void loadFragment(Fragment fragment) {
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment, fragment);
        fragmentTransaction.commit();
    }

}