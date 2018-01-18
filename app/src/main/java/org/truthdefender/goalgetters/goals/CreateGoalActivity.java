package org.truthdefender.goalgetters.goals;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
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
import org.truthdefender.goalgetters.model.GoalWrapper;
import org.truthdefender.goalgetters.model.Group;
import org.truthdefender.goalgetters.model.HabitGoal;
import org.truthdefender.goalgetters.model.HabitGoalWrapper;
import org.truthdefender.goalgetters.model.SmartGoal;
import org.truthdefender.goalgetters.model.SmartGoalWrapper;
import org.truthdefender.goalgetters.model.TaskGoal;
import org.truthdefender.goalgetters.model.TaskGoalWrapper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CreateGoalActivity extends AppCompatActivity {
    CreateSmartFragment createSmartFragment;
    CreateTaskFragment createTaskFragment;
    CreateHabitFragment createHabitFragment;
    Spinner goalTypeSpinner;

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
    List<String> groupNames;
    List<Group> groupList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_goal);

        if(createSmartFragment == null) {
            createSmartFragment = new CreateSmartFragment();
        }
        if(createTaskFragment == null) {
            createTaskFragment = new CreateTaskFragment();
        }
        if(createHabitFragment == null) {
            createHabitFragment = new CreateHabitFragment();
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.goal_layout, createSmartFragment)
                .addToBackStack(null)
                .commit();

        goalTypeSpinner = findViewById(R.id.goal_type_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.goal_type_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        goalTypeSpinner.setAdapter(adapter);
        goalTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                switch(adapterView.getItemAtPosition(i).toString()) {
                    case "Project":
                        fragmentManager.beginTransaction()
                                .replace(R.id.goal_layout, createTaskFragment)
                                .addToBackStack(null)
                                .commit();
                        break;
                    case "Habit":
                        fragmentManager.beginTransaction()
                                .replace(R.id.goal_layout, createHabitFragment)
                                .addToBackStack(null)
                                .commit();
                        break;
                    default:
                        fragmentManager.beginTransaction()
                                .replace(R.id.goal_layout, createSmartFragment)
                                .addToBackStack(null)
                                .commit();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);

        String currentDate = dateFormat.format(new Date());
        startDateButton = findViewById(R.id.start_date_picker);
        startDateText = findViewById(R.id.start_date);
        startDateText.setText(currentDate);

        deadlineButton = findViewById(R.id.deadline_date_picker);
        deadlineText = findViewById(R.id.deadline_date);
        deadlineText.setText(R.string.deadline);

        chooseGroupButton = findViewById(R.id.group_card);
        chooseGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        groupName = findViewById(R.id.group_name);
        groupMemberList = findViewById(R.id.group_member_list);

        groupName.setText("");
        groupMemberList.setText("");

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null) {
            return;
        }

        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        final DatabaseReference myRef = database.getReference().child("goals");

        createGoalButton = findViewById(R.id.create_goal_button);
        createGoalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String startText = startDateText.getText().toString();
                String[] startParts = startText.split("/");
                int startMonth = Integer.parseInt(startParts[0])-1;
                int startDay = Integer.parseInt(startParts[1]);
                int startYear = Integer.parseInt(startParts[2]);

                GregorianCalendar startDate = (GregorianCalendar)Calendar.getInstance();
                startDate.set(Calendar.MONTH, startMonth);
                startDate.set(Calendar.DAY_OF_MONTH, startDay);
                startDate.set(Calendar.YEAR, startYear);

                String endText = deadlineText.getText().toString();

                GregorianCalendar deadlineDate = (GregorianCalendar)Calendar.getInstance();
                String[] dateParts = endText.split("/");
                int deadMonth = Integer.parseInt(dateParts[0])-1;
                int deadDay = Integer.parseInt(dateParts[1]);
                int deadYear = Integer.parseInt(dateParts[2]);
                deadlineDate.set(Calendar.MONTH, deadMonth);
                deadlineDate.set(Calendar.DAY_OF_MONTH, deadDay);
                deadlineDate.set(Calendar.YEAR, deadYear);

                Goal goal;
                GoalWrapper goalWrapper;
                switch(goalTypeSpinner.getSelectedItem().toString()) {
                    case "Habit":
                        goal = new HabitGoal(createHabitFragment.getDescription(),
                                createHabitFragment.getFrequencyType(),
                                createHabitFragment.getFrequencyMap(), createHabitFragment.getSeekBarValue(),
                                deadlineDate.getTime(), startDate.getTime(), groupName.getText().toString());
                        goalWrapper = new HabitGoalWrapper((HabitGoal)goal);
                        break;
                    case "Project":
                        goal = new TaskGoal(createTaskFragment.getTaskTree(), deadlineDate.getTime(),
                                startDate.getTime(), groupName.getText().toString());
                        goalWrapper = new TaskGoalWrapper((TaskGoal)goal);
                        break;
                    default:
                        goal = new SmartGoal(createSmartFragment.getAction(), createSmartFragment.getUnit(),
                                Integer.parseInt(createSmartFragment.getAmount()),0,
                                deadlineDate.getTime(), startDate.getTime(), groupName.getText().toString());
                        goalWrapper = new SmartGoalWrapper((SmartGoal)goal);
                }

                String key = myRef.push().getKey();
                myRef.child(key).setValue(goal);
                myRef.getParent().child("users").child(user.getUid()).child("goals").child(key).setValue(goalWrapper.getTitle());
                for(Map.Entry<String, String> member : goalGroup.getMembers().entrySet()) {
                    myRef.getParent().child("users").child(member.getKey()).child("goals").child(key).setValue(goalWrapper.getTitle());
                }
                myRef.getParent().child("groups").child(goalGroupKey).child("goals").child(key).setValue(goalWrapper.getTitle());
                finish();
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar_create_goal);

        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initializeGroupCard();
    }

    public void initializeGroupCard() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null) {
            return;
        }
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users/" + user.getUid() + "/groups");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(groupName.getText().toString().equals("")) {
                    goalGroupKey = dataSnapshot.getChildren().iterator().next().getKey();
                } else {
                    for(DataSnapshot dataGroup : dataSnapshot.getChildren()) {
                        if(dataGroup.getValue() == null) {
                            return;
                        }
                        if(dataGroup.getValue().equals(groupName.getText().toString())) {
                            goalGroupKey = dataGroup.getKey();
                        }
                    }
                }
                retrieveGroup();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //handle databaseError
            }
        });
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(groupName.getText().toString().equals("")) {
                    goalGroupKey = dataSnapshot.getChildren().iterator().next().getKey();
                } else {
                    for(DataSnapshot dataGroup : dataSnapshot.getChildren()) {
                        if(dataGroup.getValue() == null) {
                            return;
                        }
                        if(dataGroup.getValue().equals(groupName.getText().toString())) {
                            goalGroupKey = dataGroup.getKey();
                        }
                    }
                }
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
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user == null) {
                    return;
                }
                groupList = new ArrayList<>();
                groupNames = new ArrayList<>();
                for(DataSnapshot dataGroup : dataSnapshot.getChildren()) {
                    goalGroup = dataGroup.getValue(Group.class);
                    if(goalGroup == null) {
                        return;
                    }
                    if(goalGroup.getMembers().containsKey(user.getUid())) {
                        groupNames.add(dataGroup.child("name").getValue(String.class));
                        groupList.add(goalGroup);
                    }
                    if(goalGroupKey.equals(dataGroup.getKey())) {
                        groupName.setText(goalGroup.getName());
                        groupMemberList.setText(goalGroup.getMemberList());
                    }
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user == null) {
                    return;
                }
                groupList = new ArrayList<>();
                groupNames = new ArrayList<>();
                for(DataSnapshot dataGroup : dataSnapshot.getChildren()) {
                    goalGroup = dataGroup.getValue(Group.class);
                    if(goalGroup == null) {
                        return;
                    }
                    if(goalGroup.getMembers().containsKey(user.getUid())) {
                        groupNames.add(dataGroup.child("name").getValue(String.class));
                        groupList.add(goalGroup);
                    }
                    if(goalGroupKey.equals(dataGroup.getKey())) {
                        groupName.setText(goalGroup.getName());
                        groupMemberList.setText(goalGroup.getMemberList());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    int selected;
    String[] items;
    public void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(CreateGoalActivity.this);
        builder.setTitle(R.string.select_group_dialog_title);


        //list of items
        items = new String[groupNames.size()];
        for(int i = 0; i < groupNames.size(); i++) {
            items[i] = groupNames.get(i);
        }
        builder.setSingleChoiceItems(items, 0,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // item selected logic
                        selected = which;
                    }
                });

        String positiveText = getString(android.R.string.ok);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // positive button logic
                        groupName.setText(items[selected]);
                        initializeGroupCard();
                    }
                });

        String negativeText = getString(android.R.string.cancel);
        builder.setNegativeButton(negativeText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // negative button logic
                    }
                });

        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();
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