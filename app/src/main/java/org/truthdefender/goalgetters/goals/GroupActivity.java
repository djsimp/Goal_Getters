package org.truthdefender.goalgetters.goals;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.truthdefender.goalgetters.R;
import org.truthdefender.goalgetters.main.MainActivity;
import org.truthdefender.goalgetters.model.Person;
import org.truthdefender.goalgetters.model.Progress;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dj on 10/18/17.
 */

public class GroupActivity extends AppCompatActivity {

    GroupMembersFragment groupMembersFragment;
    GroupGoalsFragment groupGoalsFragment;
    GroupChatFragment groupChatFragment;
    TextView groupName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        if(groupMembersFragment == null) {
            groupMembersFragment = new GroupMembersFragment();
        }
        if(groupGoalsFragment == null) {
            groupGoalsFragment = new GroupGoalsFragment();
        }
        if(groupChatFragment == null) {
            groupChatFragment = new GroupChatFragment();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.group_frame, groupMembersFragment)
                .addToBackStack(null)
                .commit();

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_group);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        groupName = (TextView) findViewById(R.id.group_name);
        groupName.setText("My New Group");

        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.nav_group);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentManager menuFragmentManager = getSupportFragmentManager();
                switch(item.getItemId()) {
                    case R.id.nav_edit_members:
                        menuFragmentManager.beginTransaction()
                                .replace(R.id.group_frame, groupMembersFragment)
                                .addToBackStack(null)
                                .commit();
                        break;
                    case R.id.nav_group_goals:
                        menuFragmentManager.beginTransaction()
                                .replace(R.id.group_frame, groupGoalsFragment)
                                .addToBackStack(null)
                                .commit();
                        break;
                    case R.id.nav_chat:
                        menuFragmentManager.beginTransaction()
                                .replace(R.id.progress_frame, groupChatFragment)
                                .addToBackStack(null)
                                .commit();
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent(GroupActivity.this.getApplicationContext(), MainActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);

        //onBackPressed();
        return true;
    }
}