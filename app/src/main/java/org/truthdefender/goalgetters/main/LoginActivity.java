package org.truthdefender.goalgetters.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdException;
import com.sendbird.android.User;
import org.truthdefender.goalgetters.R;
import org.truthdefender.goalgetters.utils.PreferenceUtils;

public class LoginActivity extends AppCompatActivity {

    private TextView mSectionTitle;
    private CoordinatorLayout mLoginLayout;
    private TextInputEditText mUsername, mPassword;
    private Button mSignInButton, mCreateAccountButton;
    private ContentLoadingProgressBar mProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        mLoginLayout = (CoordinatorLayout) findViewById(R.id.layout_login);

        mSectionTitle = (TextView)findViewById(R.id.section_title_text);
        mSectionTitle.setText(getResources().getText(R.string.app_name).toString().toUpperCase());
        mSectionTitle.setTextColor(getResources().getColor(R.color.colorPrimary));
        mSectionTitle.setTextSize(80);
        mSectionTitle.setHeight(200);

        mUsername = (TextInputEditText) findViewById(R.id.edittext_login_username);
        mPassword = (TextInputEditText) findViewById(R.id.edittext_login_password);

        mUsername.setText(PreferenceUtils.getUsername(this));
        mPassword.setText(PreferenceUtils.getPassword(this));

        mSignInButton = (Button) findViewById(R.id.button_sign_in);
        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = mUsername.getText().toString();
                // Remove all spaces from userID
                username = username.replaceAll("\\s", "");

                String password = mPassword.getText().toString();

                PreferenceUtils.setUsername(LoginActivity.this, username);
                PreferenceUtils.setPassword(LoginActivity.this, password);

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
               // connectToGoalGetters(username, password);

            }
        });

        mCreateAccountButton = (Button) findViewById(R.id.button_create_account);
        mCreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, CreateAccountActivity.class);
                startActivity(intent);

            }
        });

        // A loading indicator
        mProgressBar = (ContentLoadingProgressBar) findViewById(R.id.progress_bar_login);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(PreferenceUtils.getConnected(this)) {
            connectToGoalGetters(PreferenceUtils.getUsername(this), PreferenceUtils.getPassword(this));
        }
    }

    /**
     * Attempts to connect a user to GoalGetters.
     * @param username    The unique username of the user, which will be displayed throughout app.
     * @param password  The user's password, which will be displayed in chats.
     */
    private void connectToGoalGetters(final String username, final String password) {
        // Show the loading indicator
        showProgressBar(true);
        mSignInButton.setEnabled(false);

        SendBird.connect(username, new SendBird.ConnectHandler() {
            @Override
            public void onConnected(User user, SendBirdException e) {
                // Callback received; hide the progress bar.
                showProgressBar(false);

                if (e != null) {
                    // Error!
                    Toast.makeText(
                            LoginActivity.this, "" + e.getCode() + ": " + e.getMessage(),
                            Toast.LENGTH_SHORT)
                            .show();

                    // Show login failure snackbar
                    showSnackbar("Login to GoalGetters failed");
                    mSignInButton.setEnabled(true);
                    PreferenceUtils.setConnected(LoginActivity.this, false);
                    return;
                }

                PreferenceUtils.setConnected(LoginActivity.this, true);

                // Update the user's nickname
//                updateCurrentUserInfo(userNickname);
//                updateCurrentUserPushToken();

                // Proceed to MainActivity
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    /**
     * Update the user's push token.
     */
//    private void updateCurrentUserPushToken() {
//        // Register Firebase Token
//        SendBird.registerPushTokenForCurrentUser(FirebaseInstanceId.getInstance().getToken(),
//                new SendBird.RegisterPushTokenWithStatusHandler() {
//                    @Override
//                    public void onRegistered(SendBird.PushTokenRegistrationStatus pushTokenRegistrationStatus, SendBirdException e) {
//                        if (e != null) {
//                            // Error!
//                            Toast.makeText(LoginActivity.this, "" + e.getCode() + ":" + e.getMessage(), Toast.LENGTH_SHORT).show();
//                            return;
//                        }
//
//                        Toast.makeText(LoginActivity.this, "Push token registered.", Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }

    /**
     * Updates the user's nickname.
     */
//    private void updateCurrentUserInfo(String userNickname) {
//        SendBird.updateCurrentUserInfo(userNickname, null, new SendBird.UserInfoUpdateHandler() {
//            @Override
//            public void onUpdated(SendBirdException e) {
//                if (e != null) {
//                    // Error!
//                    Toast.makeText(
//                            LoginActivity.this, "" + e.getCode() + ":" + e.getMessage(),
//                            Toast.LENGTH_SHORT)
//                            .show();
//
//                    // Show update failed snackbar
//                    showSnackbar("Update user nickname failed");
//
//                    return;
//                }
//
//            }
//        });
//    }

    // Displays a Snackbar from the bottom of the screen
    private void showSnackbar(String text) {
        Snackbar snackbar = Snackbar.make(mLoginLayout, text, Snackbar.LENGTH_SHORT);

        snackbar.show();
    }

    // Shows or hides the ProgressBar
    private void showProgressBar(boolean show) {
        if (show) {
            mProgressBar.show();
        } else {
            mProgressBar.hide();
        }
    }
}
