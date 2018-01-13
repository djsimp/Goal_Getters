package org.truthdefender.goalgetters.main;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import org.truthdefender.goalgetters.R;
import org.truthdefender.goalgetters.goals.MyGoalsFragment;
import org.truthdefender.goalgetters.goals.MyGroupsFragment;
import org.truthdefender.goalgetters.goals.MyPastGoalsFragment;
import org.truthdefender.goalgetters.goals.MyProfileFragment;

public class MainActivity extends AppCompatActivity {

    private MyGoalsFragment myGoalsFragment;
    private MyGroupsFragment myGroupsFragment;
    private MyPastGoalsFragment myPastGoalsFragment;
    private MyProfileFragment myProfileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentManager fragmentManager = getSupportFragmentManager();

        if(myGoalsFragment == null) {
            myGoalsFragment = new MyGoalsFragment();
        }
        if(myGroupsFragment == null) {
            myGroupsFragment = new MyGroupsFragment();
        }
        if(myPastGoalsFragment == null) {
            myPastGoalsFragment = new MyPastGoalsFragment();
        }
        if(myProfileFragment == null) {
            myProfileFragment = new MyProfileFragment();
        }
        fragmentManager.beginTransaction()
                .replace(R.id.main, myGoalsFragment)
                .addToBackStack(null)
                .commit();

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentManager menuFragmentManager = getSupportFragmentManager();
                switch(item.getItemId()) {
                    case R.id.navigation_goals:
                        menuFragmentManager.beginTransaction()
                                .replace(R.id.main, myGoalsFragment)
                                .addToBackStack(null)
                                .commit();
                        break;
                    case R.id.navigation_groups:
                        menuFragmentManager.beginTransaction()
                                .replace(R.id.main, myGroupsFragment)
                                .addToBackStack(null)
                                .commit();
                        break;
                    case R.id.navigation_past_goals:
                        menuFragmentManager.beginTransaction()
                                .replace(R.id.main, myPastGoalsFragment)
                                .addToBackStack(null)
                                .commit();
                        break;
                    case R.id.navigation_profile:
                        menuFragmentManager.beginTransaction()
                                .replace(R.id.main, myProfileFragment)
                                .addToBackStack(null)
                                .commit();
                        break;
                }
                return true;
            }
        });
    }
}
