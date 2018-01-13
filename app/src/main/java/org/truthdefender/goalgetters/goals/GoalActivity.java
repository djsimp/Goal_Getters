package org.truthdefender.goalgetters.goals;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
//import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.truthdefender.goalgetters.R;
import org.truthdefender.goalgetters.main.MainActivity;
import org.truthdefender.goalgetters.model.Goal;
import org.truthdefender.goalgetters.model.GoalWrapper;
import org.truthdefender.goalgetters.model.HabitGoal;
import org.truthdefender.goalgetters.model.HabitGoalWrapper;
//import org.truthdefender.goalgetters.model.Progress;
import org.truthdefender.goalgetters.model.SmartGoal;
import org.truthdefender.goalgetters.model.SmartGoalWrapper;
import org.truthdefender.goalgetters.model.TaskGoal;
import org.truthdefender.goalgetters.model.TaskGoalWrapper;

import java.io.Serializable;

//import java.util.ArrayList;
//import java.util.List;

//
// Created by dj on 10/18/17.
//

public class GoalActivity extends AppCompatActivity {

    private MySmartProgressFragment mySmartProgressFragment;
    private ProgressFragment progressFragment;
    private GroupChatFragment groupChatFragment;
    private LinearLayout groupGoalCard;
//    RecyclerView mProgressLogRecyclerView;

    private View mProgressBar;
    private View mInvProgressBar;
    private View mToDateBar;
    private View mInvToDateBar;
    private TextView goalTitle;
    private TextView timeLeft;
    private TextView goalStatus;
    private GoalWrapper goalWrapper;
    private Goal goal;
    private String goalId;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal);

        if(mySmartProgressFragment == null) {
            mySmartProgressFragment = new MySmartProgressFragment();
        }
        if(progressFragment == null) {
            progressFragment = new ProgressFragment();
        }
        if(groupChatFragment == null) {
            groupChatFragment = new GroupChatFragment();
        }

        goalId = getIntent().getStringExtra("goalId");

        bundle = new Bundle();
        bundle.putString("goalId", goalId);
        mySmartProgressFragment.setArguments(bundle);
        progressFragment.setArguments(bundle);
        groupChatFragment.setArguments(bundle);

        goalTitle = findViewById(R.id.goal_title);

        goalStatus = findViewById(R.id.goal_status);

        timeLeft = findViewById(R.id.days_left);

        mProgressBar = findViewById(R.id.progress_bar);
        mInvProgressBar = findViewById(R.id.inv_progress_bar);
        mToDateBar = findViewById(R.id.to_date_bar);
        mInvToDateBar = findViewById(R.id.inv_to_date_bar);

        setUpGoal();

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.progress_frame, mySmartProgressFragment)
                .addToBackStack(null)
                .commit();

        Toolbar toolbar = findViewById(R.id.toolbar_goal);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

//        mProgressLogRecyclerView = (RecyclerView)findViewById(R.id.group_progress_recycler_view);
//        mProgressLogRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        groupGoalCard = findViewById(R.id.list_item_goal);
//        groupGoalCard.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(mProgressLogRecyclerView.getVisibility() == View.VISIBLE) {
//                    mProgressLogRecyclerView.setVisibility(View.GONE);
//                } else {
//                    mProgressLogRecyclerView.setVisibility(View.VISIBLE);
//                }
//            }
//        });

//        initializeProgressList();

        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_goal);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentManager menuFragmentManager = getSupportFragmentManager();
                switch(item.getItemId()) {
                    case R.id.nav_my_progress:
                        bundle.putSerializable("myGoal", (Serializable)goal);
                        bundle.putSerializable("myGoalWrapper", (Serializable)goalWrapper);
                        mySmartProgressFragment.setArguments(bundle);
                        groupGoalCard.setClickable(false);
                        menuFragmentManager.beginTransaction()
                                .replace(R.id.progress_frame, mySmartProgressFragment)
                                .addToBackStack(null)
                                .commit();
                        break;
                    case R.id.nav_members:
                        groupGoalCard.setClickable(true);
                        menuFragmentManager.beginTransaction()
                                .replace(R.id.progress_frame, progressFragment)
                                .addToBackStack(null)
                                .commit();
                        break;
                    case R.id.nav_chat:
                        groupGoalCard.setClickable(false);
                        menuFragmentManager.beginTransaction()
                                .replace(R.id.progress_frame, groupChatFragment)
                                .addToBackStack(null)
                                .commit();
                        break;
                }
                return true;
            }
        });
    }

    public void updateBar() {
        LinearLayout.LayoutParams progParams = new LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.MATCH_PARENT,
                goalWrapper.getPercentComplete()
        );
        mProgressBar.setLayoutParams(progParams);
        LinearLayout.LayoutParams invProgParams = new LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.MATCH_PARENT,
                goalWrapper.getPercentLeft()
        );
        mInvProgressBar.setLayoutParams(invProgParams);
        LinearLayout.LayoutParams timeParams = new LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.MATCH_PARENT,
                goalWrapper.getPercentTimeTaken()
        );
        mToDateBar.setLayoutParams(timeParams);
        LinearLayout.LayoutParams invTimeParams = new LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.MATCH_PARENT,
                goalWrapper.getPercentTimeLeft()
        );
        mInvToDateBar.setLayoutParams(invTimeParams);
        goalStatus.setText(goalWrapper.getStatus());
    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent(GoalActivity.this.getApplicationContext(), MainActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);

        //onBackPressed();
        return true;
    }

    public void updateGoal(DataSnapshot dataSnapshot) {
        if(dataSnapshot != null) {
            String goalType = dataSnapshot.child("type").getValue(String.class);
            if(goalType != null) {
                switch (goalType) {
                    case "habit":
                        goal = dataSnapshot.getValue(HabitGoal.class);
                        break;
                    case "task":
                        goal = dataSnapshot.getValue(TaskGoal.class);
                        break;
                    default:
                        goal = dataSnapshot.getValue(SmartGoal.class);
                        break;
                }
                goalWrapper = getGoalWrapper();
                goalTitle.setText(goalWrapper.getTitle());
                goalStatus.setText(goalWrapper.getStatus());
                timeLeft.setText(goalWrapper.getDaysLeft());
                updateBar();
            }
        }
    }

    public void setUpGoal() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("goals/" + goalId);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                updateGoal(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                updateGoal(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public GoalWrapper getGoalWrapper() {
        if(goal instanceof SmartGoal) {
            return new SmartGoalWrapper((SmartGoal)goal);
        } else if(goal instanceof HabitGoal) {
            return new HabitGoalWrapper((HabitGoal)goal);
        } else { //if(goal instanceof TaskGoal) {
            return new TaskGoalWrapper((TaskGoal)goal);
        }
    }

//    public void initializeProgressList() {
//        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("logs/" + goalId);
//        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                List<Progress> progressList = new ArrayList<Progress>();
//                for (DataSnapshot dataProg : dataSnapshot.getChildren()) {
//                    progressList.add(dataProg.getValue(Progress.class));
//                }
//                goalWrapper.setProgressLog(progressList);
//                updateUI();
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//
//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                List<Progress> progressList = new ArrayList<Progress>();
//                for (DataSnapshot dataProg : dataSnapshot.getChildren()) {
//                    progressList.add(dataProg.getValue(Progress.class));
//                }
//                goalWrapper.setProgressLog(progressList);
//                updateUI();
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//    }

    //Recycler view copy

//    private void updateUI() {
//        List<Progress> log = goalWrapper.getProgressLog();
//
//        ProgressAdapter mProgressAdapter = new ProgressAdapter(log);
//        mProgressLogRecyclerView.setAdapter(mProgressAdapter);
//    }
//
//    private class ProgressAdapter extends RecyclerView.Adapter<ProgressHolder> {
//
//        private List<Progress> mLogs;
//
//        private ProgressAdapter(List<Progress> logs) {
//            mLogs = logs;
//        }
//
//        @Override
//        public ProgressHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            LayoutInflater layoutInflater = LayoutInflater.from(GoalActivity.this);
//            View view = layoutInflater.inflate(R.layout.progress_item, parent, false);
//            return new ProgressHolder(view);
//        }
//
//        @Override
//        public void onBindViewHolder(ProgressHolder holder, int position) {
//            Progress log = mLogs.get(position);
//            holder.bindProgress(log);
//        }
//
//        @Override
//        public int getItemCount() {
//            return mLogs.size();
//        }
//    }
//
//    private class ProgressHolder extends RecyclerView.ViewHolder {
//
//        private TextView mProgressItemDate;
//        private TextView mProgressItemText;
//
//        private Progress mProgress;
//
//        private ProgressHolder(View itemView) {
//            super(itemView);
//            mProgressItemDate = (TextView)itemView.findViewById(R.id.progress_item_date);
//            mProgressItemText = (TextView)itemView.findViewById(R.id.progress_item_text);
//        }
//
//        private void bindProgress(Progress progress) {
//            mProgress = progress;
//            mProgressItemDate.setText(progress.getDate());
//            mProgressItemText.setText(progress.getReport());
//            if(progress.getReport().startsWith("+")) {
//                mProgressItemText.setTextColor(getResources().getColor(R.color.positive_color));
//            } else {
//                mProgressItemText.setTextColor(getResources().getColor(R.color.negative_color));
//            }
//        }
//    }
}