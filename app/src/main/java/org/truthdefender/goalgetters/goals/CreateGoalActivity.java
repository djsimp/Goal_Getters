package org.truthdefender.goalgetters.goals;

import android.app.Dialog;
import android.content.Intent;
import android.icu.text.DateFormat;
import android.os.Bundle;
import android.app.FragmentManager;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
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
import org.truthdefender.goalgetters.model.Group;
import org.truthdefender.goalgetters.model.Singleton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by dj on 10/18/17.
 */

public class CreateGoalActivity extends AppCompatActivity {
    TextInputEditText actionText;
    TextView amountText;
    TextInputEditText unitText;

    ImageButton startDateButton;
    TextView startDateText;
    ImageButton deadlineButton;
    TextView deadlineText;

    CardView chooseGroupButton;
    TextView groupName;
    TextView groupMemberList;

    Button createGoalButton;

    String goalGroupKey;
    Group goalGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_goal);

        actionText = (TextInputEditText)findViewById(R.id.edittext_action);
        unitText = (TextInputEditText)findViewById(R.id.edittext_units);
        amountText = (TextView) findViewById(R.id.amount);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);

        String currentDate = dateFormat.format(new Date());
        startDateButton = (ImageButton) findViewById(R.id.start_date_picker);
        startDateText = (TextView)findViewById(R.id.start_date);
        startDateText.setText(currentDate);

        deadlineButton = (ImageButton)findViewById(R.id.deadline_date_picker);
        deadlineText = (TextView)findViewById(R.id.deadline_date);
        deadlineText.setText("Deadline");

        Button amountButton = (Button) findViewById(R.id.number_picker_button);
        amountButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                show();
            }
        });

        chooseGroupButton = (CardView)findViewById(R.id.group_card);
        chooseGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(CreateGoalActivity.this, SelectGroupFragment.class);
            }
        });

        groupName = (TextView)findViewById(R.id.group_name);
        groupMemberList = (TextView)findViewById(R.id.group_member_list);

        groupName.setText("");
        groupMemberList.setText("");

        initializeGroupCard();

        createGoalButton = (Button)findViewById(R.id.create_goal_button);
        createGoalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuilder title = new StringBuilder();
                title.append(actionText.getText()).append(" ")
                        .append(amountText.getText()).append(" ")
                        .append(unitText.getText());

                String startText = startDateText.getText().toString();

                GregorianCalendar startDate = (GregorianCalendar)Calendar.getInstance();
                startDate.set(Calendar.MONTH, Integer.parseInt(startText.substring(0,2)));
                startDate.set(Calendar.DAY_OF_MONTH, Integer.parseInt(startText.substring(3,5)));
                startDate.set(Calendar.YEAR, Integer.parseInt(startText.substring(6)));

                String endText = deadlineText.getText().toString();

                GregorianCalendar deadlineDate = (GregorianCalendar)Calendar.getInstance();
                deadlineDate.set(Calendar.MONTH, Integer.parseInt(endText.substring(0,2)));
                deadlineDate.set(Calendar.DAY_OF_MONTH, Integer.parseInt(endText.substring(3,5)));
                deadlineDate.set(Calendar.YEAR, Integer.parseInt(endText.substring(6)));

                Group currentGroup = Singleton.get().getCurrentGroup();
                Goal goal;
                if(currentGroup != null) {
                    goal = new Goal(title.toString(), unitText.getText().toString(),
                            Integer.parseInt(amountText.getText().toString()), 0, deadlineDate.getTime(), startDate.getTime(),
                            Singleton.get().getCurrentGroup().getName());
                } else {
                    goal = new Goal(title.toString(), unitText.getText().toString(),
                            Integer.parseInt(amountText.getText().toString()), 0, deadlineDate.getTime(), startDate.getTime(),
                            "Self");
                }

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

                String key = myRef.push().getKey();
                myRef.child(key).setValue(goal);
                myRef.getParent().child("users").child(user.getUid()).child("goals").child(key).setValue(true);
                myRef.getParent().child("groups").child(goalGroupKey).child("goals").child(key).setValue(true);
                finish();
            }
        });

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

    public void initializeGroupCard() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users/" + user.getUid() + "/groups");
        ref.limitToFirst(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                goalGroupKey = dataSnapshot.getChildren().iterator().next().getKey();
                retrieveGroup();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //handle databaseError
            }
        });
    }

    public void retrieveGroup() {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("groups");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dataGroup : dataSnapshot.getChildren()) {
                    if(goalGroupKey.equals(dataGroup.getKey())) {
                        goalGroup = dataGroup.getValue(Group.class);
                        groupName.setText(goalGroup.getName());
                        groupMemberList.setText(goalGroup.getMemberList());
                        Singleton.get().setCurrentGroup(goalGroup);
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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

    public void show() {
        final Dialog npDialog = new Dialog(this);
        npDialog.setTitle("Set Amount");
        npDialog.setContentView(R.layout.numberpicker_layout);
        Button setBtn = (Button) npDialog.findViewById(R.id.setBtn);
        Button cnlBtn = (Button) npDialog.findViewById(R.id.CancelButton_NumberPicker);

        final NumberPicker numberPicker = (NumberPicker) npDialog.findViewById(R.id.numberPicker);
        numberPicker.setMaxValue(1000000);
        numberPicker.setMinValue(0);
        numberPicker.setWrapSelectorWheel(false);
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                // TODO Auto-generated method stub
            }
        });
        setBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView amount = (TextView) findViewById(R.id.amount);
                String number = String.valueOf(numberPicker.getValue());
                amount.setText(number);

                npDialog.dismiss();
            }
        });

        cnlBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                npDialog.dismiss();
            }
        });

        npDialog.show();
    }
}