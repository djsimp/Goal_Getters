package org.truthdefender.goalgetters.main;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.annotation.AnimatorRes;
import android.support.annotation.IdRes;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdException;
import org.truthdefender.goalgetters.R;
import org.truthdefender.goalgetters.goals.MyGoalsFragment;
import org.truthdefender.goalgetters.groupchannel.GroupChannelActivity;
import org.truthdefender.goalgetters.openchannel.OpenChannelActivity;
import org.truthdefender.goalgetters.utils.PreferenceUtils;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView menu;
    private MyGoalsFragment myGoalsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentManager fragmentManager = getFragmentManager();

        if(myGoalsFragment == null) {
            myGoalsFragment = new MyGoalsFragment();
        }
        fragmentManager.beginTransaction()
                .replace(R.id.main, myGoalsFragment)
                .addToBackStack(null)
                .commit();

        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentManager menuFragmentManager = getFragmentManager();
                switch(item.getItemId()) {
                    case R.id.navigation_goals:
                        menuFragmentManager.beginTransaction()
                                .replace(R.id.main, myGoalsFragment)
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

        // Displays the SDK version in a TextView
//        String sdkVersion = String.format(getResources().getString(R.string.all_app_version),
//                BaseApplication.VERSION, SendBird.getSDKVersion());
//        ((TextView) findViewById(R.id.text_main_versions)).setText(sdkVersion);
    }

    /**
     * Unregisters all push tokens for the current user so that they do not receive any notifications,
     * then disconnects from SendBird.
     */
    private void disconnect() {
        SendBird.unregisterPushTokenAllForCurrentUser(new SendBird.UnregisterPushTokenHandler() {
            @Override
            public void onUnregistered(SendBirdException e) {
                if (e != null) {
                    // Error!
                    e.printStackTrace();

                    // Don't return because we still need to disconnect.
                } else {
                    Toast.makeText(MainActivity.this, "All push tokens unregistered.", Toast.LENGTH_SHORT)
                            .show();
                }

                SendBird.disconnect(new SendBird.DisconnectHandler() {
                    @Override
                    public void onDisconnected() {
                        PreferenceUtils.setConnected(MainActivity.this, false);
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        });
    }
}
