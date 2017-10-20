package org.truthdefender.goalgetters.main;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import org.truthdefender.goalgetters.R;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class CreateAccountActivity extends AppCompatActivity {
//    private TextView mSectionTitle;
//    private CoordinatorLayout mLoginLayout;
//    private TextInputEditText mUsername, mPassword;
//    private Button mSignInButton, mCreateAccountButton;
//    private ContentLoadingProgressBar mProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_account);

//        mLoginLayout = (CoordinatorLayout) findViewById(R.id.layout_login);
//
//        mSectionTitle = (TextView)findViewById(R.id.section_title_text);
//        mSectionTitle.setText(getResources().getText(R.string.app_name).toString().toUpperCase());
//        mSectionTitle.setTextColor(getResources().getColor(R.color.colorPrimary));
//        mSectionTitle.setTextSize(80);
//        mSectionTitle.setHeight(200);
//
//        mUsername = (TextInputEditText) findViewById(R.id.edittext_login_username);
//        mPassword = (TextInputEditText) findViewById(R.id.edittext_login_password);
//
//        mUsername.setText(PreferenceUtils.getUsername(this));
//        mPassword.setText(PreferenceUtils.getPassword(this));
//
//        mSignInButton = (Button) findViewById(R.id.button_sign_in);
//        mSignInButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String username = mUsername.getText().toString();
//                // Remove all spaces from userID
//                username = username.replaceAll("\\s", "");
//
//                String password = mPassword.getText().toString();
//
//                PreferenceUtils.setUsername(LoginActivity.this, username);
//                PreferenceUtils.setPassword(LoginActivity.this, password);
//
//                connectToGoalGetters(username, password);
//
//            }
//        });
//
//        mCreateAccountButton = (Button) findViewById(R.id.button_create_account);
//        mCreateAccountButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(LoginActivity.this, CreateAccountActivity.class);
//                startActivity(intent);
//
//            }
//        });
//
//        // A loading indicator
//        mProgressBar = (ContentLoadingProgressBar) findViewById(R.id.progress_bar_login);
    }
}

