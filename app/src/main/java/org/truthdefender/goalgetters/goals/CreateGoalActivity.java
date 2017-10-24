package org.truthdefender.goalgetters.goals;

import android.content.Intent;
import android.icu.text.DateFormat;
import android.os.Bundle;
import android.app.FragmentManager;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.NumberPicker;

import org.truthdefender.goalgetters.R;

import java.util.Date;

/**
 * Created by dj on 10/18/17.
 */

public class CreateGoalActivity extends AppCompatActivity {
    Button startDateButton;
    Button deadlineButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_goal);
        NumberPicker np =(NumberPicker) findViewById(R.id.number_picker);

        np.setMinValue(0);
        np.setMaxValue(1000000);
        np.setWrapSelectorWheel(false);

        String currentDate = DateFormat.getDateInstance().format(new Date());
        startDateButton = (Button) findViewById(R.id.start_date_picker);
        startDateButton.setText(currentDate);
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
        deadlineButton = (Button) findViewById(R.id.deadline_date_picker);
        deadlineButton.setText("lol");
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