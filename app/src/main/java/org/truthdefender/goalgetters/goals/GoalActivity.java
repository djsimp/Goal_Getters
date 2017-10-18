package org.truthdefender.goalgetters.goals;

import android.os.Bundle;
import android.app.FragmentManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import org.truthdefender.goalgetters.R;

/**
 * Created by dj on 10/18/17.
 */

public class GoalActivity extends AppCompatActivity {

    MyProgressFragment myProgressFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal);

        if(myProgressFragment == null) {
            myProgressFragment = new MyProgressFragment();
        }
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.progress_frame, myProgressFragment)
                .addToBackStack(null)
                .commit();

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_goal);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.nav_goal);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentManager menuFragmentManager = getFragmentManager();
                switch(item.getItemId()) {
                    case R.id.navigation_goals:
                        menuFragmentManager.beginTransaction()
                                .replace(R.id.progress_frame, myProgressFragment)
                                .addToBackStack(null)
                                .commit();
                        break;
//                    case R.id.navigation_groups:
//                        menuFragmentManager.beginTransaction()
//                                .replace(R.id.main, myGroupsFragment)
//                                .addToBackStack(null)
//                                .commit();
//                        break;
//                    case R.id.navigation_past_goals:
//                        menuFragmentManager.beginTransaction()
//                                .replace(R.id.main, myPastGoalsFragment)
//                                .addToBackStack(null)
//                                .commit();
//                        break;
                }
                return true;
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}