package com.example.groomzoom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.gauravk.bubblenavigation.BubbleNavigationLinearView;
import com.gauravk.bubblenavigation.listener.BubbleNavigationChangeListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.ParseUser;

import es.dmoral.toasty.Toasty;
import fragments.AppointmentFragment;
import fragments.BrowseFragment;
import fragments.ProfileFragment;

public class MainActivity extends AppCompatActivity {

    final FragmentManager fragmentManager = getSupportFragmentManager();
//    private BottomNavigationView bottomNavigationView;
    public BubbleNavigationLinearView bubbleNavigation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bubbleNavigation = findViewById(R.id.bubbleNavigation);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new BrowseFragment()).commit();
        
        bubbleNavigation.setNavigationChangeListener(new BubbleNavigationChangeListener() {
            @Override
            public void onNavigationChanged(View view, int position) {
                Fragment fragment;
                switch (position){
                    case 0:
                        fragment = new BrowseFragment();
                        break;
                    case 1:
                        fragment = new AppointmentFragment();
                        break;
                    case 2:
                        fragment = new ProfileFragment();
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + position);
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
            }
        });


    }

}