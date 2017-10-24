package org.truthdefender.goalgetters.goals;

import android.content.Intent;
import android.icu.text.DateFormat;
import android.os.Bundle;
import android.app.FragmentManager;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.truthdefender.goalgetters.R;
import org.truthdefender.goalgetters.model.Goal;
import org.truthdefender.goalgetters.model.Singleton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by dj on 10/18/17.
 */

public class CreateGoalActivity extends AppCompatActivity {
    TextInputEditText actionText;
    NumberPicker amountText;
    TextInputEditText unitText;

    ImageButton startDateButton;
    TextView startDateText;
    ImageButton deadlineButton;
    TextView deadlineText;

    Button createGoalButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_goal);

        actionText = (TextInputEditText)findViewById(R.id.edittext_action);
        amountText = (NumberPicker)findViewById(R.id.number_picker);
        amountText.setMinValue(0);
        amountText.setMaxValue(1000000);
        amountText.setWrapSelectorWheel(false);
        unitText = (TextInputEditText)findViewById(R.id.edittext_units);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

        String currentDate = dateFormat.format(new Date());
        startDateButton = (ImageButton) findViewById(R.id.start_date_picker);
        startDateText = (TextView)findViewById(R.id.start_date);
        startDateText.setText(currentDate);

        deadlineButton = (ImageButton)findViewById(R.id.deadline_date_picker);
        deadlineText = (TextView)findViewById(R.id.deadline_date);

        createGoalButton = (Button)findViewById(R.id.button_create_goal);
        createGoalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuilder title = new StringBuilder();
                title.append(actionText.getText()).append(" ")
                        .append(amountText.getValue()).append(" ")
                        .append(unitText.getText());

                String startText = startDateText.getText().toString();

                Calendar startDate = Calendar.getInstance();
                startDate.set(Calendar.MONTH, Integer.parseInt(startText.substring(0,2)));
                startDate.set(Calendar.DAY_OF_MONTH, Integer.parseInt(startText.substring(3,5)));
                startDate.set(Calendar.YEAR, Integer.parseInt(startText.substring(6)));

                String endText = deadlineText.getText().toString();

                Calendar deadlineDate = Calendar.getInstance();
                startDate.set(Calendar.MONTH, Integer.parseInt(endText.substring(0,2)));
                startDate.set(Calendar.DAY_OF_MONTH, Integer.parseInt(endText.substring(3,5)));
                startDate.set(Calendar.YEAR, Integer.parseInt(endText.substring(6)));

                Goal goal = new Goal(title.toString(), unitText.getText().toString(),
                        amountText.getValue(), 0, deadlineDate, startDate,
                        Singleton.get().getCurrentGroup());

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference().child("goals");
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Singleton.get().addGoal(dataSnapshot.getValue(Goal.class));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("Create Goal", "Failed to read value.", databaseError.toException());
                    }
                });

                myRef.push().setValue(goal);
            }
        });

        //startDateButton.setText("HI");

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_create_goal);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }




//        startDateButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                DialogFragment newFragment = new DatePickerFragment();
//                newFragment.show(getSupportFragmentManager(), "startDatePicker");
//            }
//        });

    }

    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        deadlineText.setText("lol");
    }





    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void showStartDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "startDatePicker");
    }

    public void showDeadlinePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "deadlinePicker");
    }
}