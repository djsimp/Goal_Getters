package org.truthdefender.goalgetters.main;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
import org.truthdefender.goalgetters.model.Goal;
import org.truthdefender.goalgetters.model.SmartGoal;
import org.truthdefender.goalgetters.model.User;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class CreateAccountActivity extends AppCompatActivity {
    private static final String TAG = "CreateAccountActivity";
    private boolean joinSuccess;

    private EditText mNameText;
    private EditText mEmailText;
    private EditText mPasswordText;
    private Button mSignupButton;
    private TextView mLoginLink;
    private ImageButton mImageButton;
    private int profileImageTag;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        mNameText = (EditText)findViewById(R.id.input_name);
        mEmailText = (EditText)findViewById(R.id.input_email);
        mPasswordText = (EditText)findViewById(R.id.input_password);
        mSignupButton = (Button)findViewById(R.id.button_join);
        mLoginLink = (TextView)findViewById(R.id.link_login);
        mImageButton = (ImageButton)findViewById(R.id.profile_icon_button);

        String email = getIntent().getStringExtra("email");
        if(email != null) {
            mEmailText.setText(email);
        }

        joinSuccess = false;

        mSignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        mLoginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateAccountActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        profileImageTag = getIntent().getIntExtra("imageTag", R.drawable.african);
        mImageButton.setImageResource(profileImageTag);
        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateAccountActivity.this, SelectIconActivity.class);
                intent.putExtra("imageTag", profileImageTag);
                startActivity(intent);
                finish();
            }
        });

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_create_account);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        mSignupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(CreateAccountActivity.this,
                R.style.AppTheme_PopupOverlay);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        String name = mNameText.getText().toString();
        String email = mEmailText.getText().toString();
        String password = mPasswordText.getText().toString();


        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            onSignupFailed();
                        } else {
                            onSignupSuccess();
                        }
                        // ...
                    }
                });

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        progressDialog.dismiss();
                    }
                }, 3000);
    }


    public void onSignupSuccess() {
        mSignupButton.setEnabled(true);
        User newUser = new User(mNameText.getText().toString(), mEmailText.getText().toString(),
                profileImageTag);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");
        myRef.child(userId).setValue(newUser);

        String groupKey = myRef.getParent().child("groups").push().getKey();
        myRef.getParent().child("groups").child(groupKey).child("name").setValue("Just Me");
        myRef.getParent().child("groups").child(groupKey).child("members").child(userId).setValue(newUser.getName());
        myRef.child(userId).child("groups").child(groupKey).setValue("Just Me");

        GregorianCalendar today = (GregorianCalendar)Calendar.getInstance();
        int dayOfMonth = today.get(Calendar.DAY_OF_MONTH);
        GregorianCalendar tomorrow = (GregorianCalendar)Calendar.getInstance();
        tomorrow.set(Calendar.DAY_OF_MONTH, dayOfMonth + 1);
        Goal firstGoal = new SmartGoal("Create", "goals", 1, 0, tomorrow.getTime(), today.getTime());

        String goalKey = myRef.getParent().child("goals").push().getKey();
        myRef.getParent().child("goals").child(goalKey).setValue(firstGoal);
        myRef.getParent().child("goals").child(goalKey).child("group").setValue(groupKey);
        myRef.child(userId).child("goals").child(goalKey).setValue("Create 1 Goal");

        myRef.getParent().child("groups").child(groupKey).child("goals").child(goalKey).setValue("Create 1 goal");

        Intent intent = new Intent(CreateAccountActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        mSignupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = mNameText.getText().toString();
        String email = mEmailText.getText().toString();
        String password = mPasswordText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            mNameText.setError("at least 3 characters");
            valid = false;
        } else {
            mNameText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mEmailText.setError("enter a valid email address");
            valid = false;
        } else {
            mEmailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            mPasswordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            mPasswordText.setError(null);
        }

        return valid;
    }
}