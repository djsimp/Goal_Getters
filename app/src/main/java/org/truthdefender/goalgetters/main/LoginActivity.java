package org.truthdefender.goalgetters.main;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.truthdefender.goalgetters.R;
import org.truthdefender.goalgetters.model.Singleton;
import org.truthdefender.goalgetters.model.User;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    private String email;
    private String password;
    private boolean loginSuccess;

    @InjectView(R.id.section_title_text) TextView _sectionTitle;
    @InjectView(R.id.input_email) EditText _emailText;
    @InjectView(R.id.input_password) EditText _passwordText;
    @InjectView(R.id.button_login) Button _loginButton;
    @InjectView(R.id.link_signup) TextView _signupLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);

        _sectionTitle.setText("GOAL GETTERS");
        _sectionTitle.setTextSize(70);
        _sectionTitle.setHeight(200);
        _sectionTitle.setGravity(Gravity.CENTER);

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

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Singleton.get().setUser(new User(null, _emailText.getText().toString(), Singleton.get().getThumbIds()[0]));
                Intent intent = new Intent(LoginActivity.this, CreateAccountActivity.class);
                startActivity(intent);
                finish();
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

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        loginSuccess = false;

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_PopupOverlay);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        email = _emailText.getText().toString();
        password = _passwordText.getText().toString();

        Singleton.get().getmAuth().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                            Toast.makeText(LoginActivity.this, R.string.auth_failed,
                                    Toast.LENGTH_SHORT).show();
                            loginSuccess = false;
                        } else {
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            if (user != null) {
                                Singleton.get().setUserId(user.getUid());
                                loginSuccess = true;
                            }
                        }

                        // ...
                    }
                });

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        if(loginSuccess) {
                            onLoginSuccess();
                        } else {
                            onLoginFailed();
                        }
                        progressDialog.dismiss();
                    }
                }, 3000);
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
}