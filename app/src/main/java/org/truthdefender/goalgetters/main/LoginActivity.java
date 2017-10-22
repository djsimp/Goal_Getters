package org.truthdefender.goalgetters.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdException;
import com.sendbird.android.User;
import org.truthdefender.goalgetters.R;
import org.truthdefender.goalgetters.model.Singleton;
import org.truthdefender.goalgetters.utils.PreferenceUtils;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private TextView mSectionTitle;
    private CoordinatorLayout mLoginLayout;
    private TextInputEditText mEmail, mPassword;
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

        mEmail = (TextInputEditText) findViewById(R.id.edittext_login_username);
        mPassword = (TextInputEditText) findViewById(R.id.edittext_login_password);

        mEmail.setText(PreferenceUtils.getUsername(this));
        mPassword.setText(PreferenceUtils.getPassword(this));

        mSignInButton = (Button) findViewById(R.id.button_sign_in);
        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = mEmail.getText().toString();
                // Remove all spaces from userID
                username = username.replaceAll("\\s", "");

                String password = mPassword.getText().toString();

                PreferenceUtils.setUsername(LoginActivity.this, username);
                PreferenceUtils.setPassword(LoginActivity.this, password);

                signIn(mEmail.getText().toString(), mPassword.getText().toString());
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

        Singleton.get().setmAuth(FirebaseAuth.getInstance());

        Singleton.get().setmAuthListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        Singleton.get().getmAuth().addAuthStateListener(Singleton.get().getmAuthListener());
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Singleton.get().getmAuthListener() != null) {
            Singleton.get().getmAuth().removeAuthStateListener(Singleton.get().getmAuthListener());
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

    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);

        Singleton.get().getmAuth().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = Singleton.get().getmAuth().getCurrentUser();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }
                    }
                });
        // [END sign_in_with_email]
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
