package org.truthdefender.goalgetters.goals;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.app.FragmentManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.truthdefender.goalgetters.R;
import org.truthdefender.goalgetters.groupchannel.GroupChatFragment;
import org.truthdefender.goalgetters.main.MainActivity;
import org.truthdefender.goalgetters.model.Progress;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dj on 10/18/17.
 */

public class GoalActivity extends AppCompatActivity {

    MyProgressFragment myProgressFragment;
    ProgressFragment progressFragment;
    GroupChatFragment groupChatFragment;
    LinearLayout groupGoalCard;
    RecyclerView mProgressLogRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal);

        if(myProgressFragment == null) {
            myProgressFragment = new MyProgressFragment();
        }
        if(progressFragment == null) {
            progressFragment = new ProgressFragment();
        }
        if(groupChatFragment == null) {
            groupChatFragment = new GroupChatFragment();
        }

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.progress_frame, myProgressFragment)
                .addToBackStack(null)
                .commit();

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_goal);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        mProgressLogRecyclerView = (RecyclerView)findViewById(R.id.group_progress_recycler_view);
        mProgressLogRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        groupGoalCard = (LinearLayout)findViewById(R.id.list_item_goal);
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

        updateUI();

//        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.nav_goal);
//        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                FragmentManager menuFragmentManager = getFragmentManager();
//                switch(item.getItemId()) {
//                    case R.id.nav_my_progress:
//                        groupGoalCard.setClickable(false);
//                        menuFragmentManager.beginTransaction()
//                                .replace(R.id.progress_frame, myProgressFragment)
//                                .addToBackStack(null)
//                                .commit();
//                        break;
////                    case R.id.nav_members:
////                        groupGoalCard.setClickable(true);
////                        menuFragmentManager.beginTransaction()
////                                .replace(R.id.progress_frame, progressFragment)
////                                .addToBackStack(null)
////                                .commit();
////                        break;
////                    case R.id.nav_chat:
////                        groupGoalCard.setClickable(false);
////                        menuFragmentManager.beginTransaction()
////                                .replace(R.id.progress_frame, groupChatFragment)
////                                .addToBackStack(null)
////                                .commit();
////                        break;
//                }
//                return true;
//            }
//        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent(GoalActivity.this.getApplicationContext(), MainActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);

        //onBackPressed();
        return true;
    }


    private List<Progress> generateProgress() {
        List<Progress> log = new ArrayList<>();
        for(int i = 0; i < 15; i++) {
            if(i%2 == 0) {
                log.add(new Progress("October 20, 2017", "+$150 - I am making some great progress!"));
            } else {
                log.add(new Progress("October 20, 2017", "-$100 - Another one bites the dust!"));
            }
        }
        return log;
    }

    //Recycler view copy

    private void updateUI() {
        List<Progress> log = generateProgress();

        ProgressAdapter mProgressAdapter = new ProgressAdapter(log);
        mProgressLogRecyclerView.setAdapter(mProgressAdapter);
    }

    private class ProgressAdapter extends RecyclerView.Adapter<ProgressHolder> {

        private List<Progress> mLogs;

        private ProgressAdapter(List<Progress> logs) {
            mLogs = logs;
        }

        @Override
        public ProgressHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(GoalActivity.this);
            View view = layoutInflater.inflate(R.layout.progress_item, parent, false);
            return new ProgressHolder(view);
        }

        @Override
        public void onBindViewHolder(ProgressHolder holder, int position) {
            Progress log = mLogs.get(position);
            holder.bindGoal(log);
        }

        @Override
        public int getItemCount() {
            return mLogs.size();
        }
    }

    private class ProgressHolder extends RecyclerView.ViewHolder {

        private TextView mProgressItemDate;
        private TextView mProgressItemText;

        private Progress mProgress;

        private ProgressHolder(View itemView) {
            super(itemView);
            mProgressItemDate = (TextView)itemView.findViewById(R.id.progress_item_date);
            mProgressItemText = (TextView)itemView.findViewById(R.id.progress_item_text);
        }

        private void bindGoal(Progress progress) {
            mProgress = progress;
            mProgressItemDate.setText(progress.getDate());
            mProgressItemText.setText(progress.getReport());
            if(progress.getReport().startsWith("+")) {
                mProgressItemText.setTextColor(getResources().getColor(R.color.positive_color));
            } else {
                mProgressItemText.setTextColor(getResources().getColor(R.color.negative_color));
            }
        }
    }
}