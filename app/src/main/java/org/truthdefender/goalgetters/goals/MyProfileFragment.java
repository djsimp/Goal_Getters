package org.truthdefender.goalgetters.goals;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

import org.truthdefender.goalgetters.R;
import org.truthdefender.goalgetters.main.LoginActivity;

public class MyProfileFragment extends Fragment {

//    private EditText mNameText;
//    private EditText mEmailText;
//    private EditText mPasswordText;
//    private Button mSaveButton;
    private Button mLogoutButton;
//    private ImageButton mImageButton;
//    private String oldPassword;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_my_profile, container, false);

        mLogoutButton = (Button)v.findViewById(R.id.button_logout);

        mLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity().getBaseContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                getActivity().finish();
            }
        });
        return v;
    }
}