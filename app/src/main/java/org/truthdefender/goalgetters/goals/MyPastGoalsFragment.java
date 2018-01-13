package org.truthdefender.goalgetters.goals;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
import org.truthdefender.goalgetters.model.HabitGoal;
import org.truthdefender.goalgetters.model.HabitGoalWrapper;
import org.truthdefender.goalgetters.model.Progress;
import org.truthdefender.goalgetters.model.SmartGoal;
import org.truthdefender.goalgetters.model.SmartGoalWrapper;
import org.truthdefender.goalgetters.model.TaskGoal;
import org.truthdefender.goalgetters.model.TaskGoalWrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyPastGoalsFragment extends Fragment {

    private RecyclerView mPastGoalsRecyclerView;
    private TextView mSectionTitle;

    static List<String> goalIds = new ArrayList<>();
    static HashMap<String, Goal> myGoals = new HashMap<>();

    private int progPos;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_my_past_goals, container, false);

        mSectionTitle = v.findViewById(R.id.section_title_text);
        mSectionTitle.setText(R.string.my_past_goals_title);

        mPastGoalsRecyclerView = v.findViewById(R.id.my_past_goals_recycler_view);
        mPastGoalsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        progPos = 0;

        getMyPastGoals();
        return v;
    }

    public void getMyPastGoals() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null) {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users/" + user.getUid() + "/past_goals");
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    goalIds = new ArrayList<>();
                    myGoals = new HashMap<>();
                    for (DataSnapshot goalId : dataSnapshot.getChildren()) {
                        goalIds.add(goalId.getKey());
                    }
                    getPastGoals();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    goalIds = new ArrayList<>();
                    myGoals = new HashMap<>();
                    for (DataSnapshot goalId : dataSnapshot.getChildren()) {
                        goalIds.add(goalId.getKey());
                    }
                    getPastGoals();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    public void getPastGoals() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("past_goals");
        for(final String goalId : goalIds) {
            ref.child(goalId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String goalType = dataSnapshot.child("type").getValue(String.class);
                    if (goalType != null) {
                        Goal goal;
                        switch(goalType) {
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
                        myGoals.put(goalId, goal);
                    }
                    updateUI();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            ref.child(goalId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String goalType = dataSnapshot.child("type").getValue(String.class);
                    if (goalType != null) {
                        Goal goal;
                        switch(goalType) {
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
                        if(goal != null) {
                            goal.setGoalId(goalId);
                            myGoals.put(goalId, goal);
                        }
                    }
                    updateUI();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        initializePastGoalsList();
//    }
//
//    public void initializePastGoalsList() {
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users/" + user.getUid() + "/past_goals");
//        ref.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                updateGoalList(dataSnapshot);
//                initializeGoals();
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                //handle databaseError
//            }
//        });
//
//        ref.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                updateGoalList(dataSnapshot);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                //handle databaseError
//            }
//        });
//    }
//
//    public void updateGoalList(DataSnapshot dataSnapshot) {
//        goalList = new ArrayList<String>();
//        for(DataSnapshot dataGoal : dataSnapshot.getChildren()) {
//            goalList.add(dataGoal.getKey());
//        }
//    }
//
//    public void updateGoals(DataSnapshot dataSnapshot) {
//        myGoals = new ArrayList<Goal>();
//        for(DataSnapshot dataGoal : dataSnapshot.getChildren()) {
//            if(goalList.contains(dataGoal.getKey())) {
//                myGoals.add(dataGoal.getValue(Goal.class));
//                myGoals.get(myGoals.size()-1).setGoalId(dataGoal.getKey());
//            }
//        }
//        updateUI();
//    }
//
//    public void initializeGoals() {
//        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("past_goals");
//        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                updateGoals(dataSnapshot);
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
//                updateGoals(dataSnapshot);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//    }

    //Recycler view copy

    private void updateUI() {
        List<Goal> goals = new ArrayList<>(myGoals.values());
        GoalAdapter mGoalAdapter = new GoalAdapter(goals);
        mPastGoalsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mPastGoalsRecyclerView.setAdapter(mGoalAdapter);
    }

    private class GoalHolder extends RecyclerView.ViewHolder {

        //private LinearLayout mListItemGoal;
        private CardView mGoalCard;
        private TextView mGoalTitle;
        //private View mGoalTotalBar;
        private View mProgressBar;
        private View mInvProgressBar;
        private View mToDateBar;
        private View mInvToDateBar;
        private TextView mGoalStatus, mDaysLeft;

        private RecyclerView mProgressLogRecyclerView;

        private Goal mGoal;
        private GoalWrapper mGoalWrapper;

        private GoalHolder(View itemView) {
            super(itemView);

            mProgressLogRecyclerView = itemView.findViewById(R.id.past_progress_log_recycler);
            mProgressLogRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

            //mListItemGoal = (LinearLayout) itemView.findViewById(R.id.list_item_goal);
//            mListItemGoal.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if(mProgressLogRecyclerView.getVisibility() == View.VISIBLE) {
//                        mProgressLogRecyclerView.setVisibility(View.GONE);
//                    } else {
//                        mProgressLogRecyclerView.setVisibility(View.VISIBLE);
//                    }
//                }
//            });
            mGoalCard = itemView.findViewById(R.id.goal_card);
            mGoalCard.bringToFront();
            mGoalTitle = itemView.findViewById(R.id.goal_title);
            mProgressBar = itemView.findViewById(R.id.progress_bar);
            mInvProgressBar = itemView.findViewById(R.id.inv_progress_bar);
            mToDateBar = itemView.findViewById(R.id.to_date_bar);
            mInvToDateBar = itemView.findViewById(R.id.inv_to_date_bar);
            mGoalStatus = itemView.findViewById(R.id.goal_status);
            mDaysLeft = itemView.findViewById(R.id.days_left);
            updateProgressUI();
        }

        private void bindGoal(Goal goal) {
            mGoal = goal;
            if(goal instanceof SmartGoal) {
                mGoalWrapper = new SmartGoalWrapper((SmartGoal)mGoal);
            } else if(goal instanceof TaskGoal) {
                mGoalWrapper = new TaskGoalWrapper((TaskGoal)mGoal);
            } else if(goal instanceof HabitGoal) {
                mGoalWrapper = new HabitGoalWrapper((HabitGoal)mGoal);
            }
            mGoalTitle.setText(mGoalWrapper.getTitle());
            String status = mGoalWrapper.getStatus();
            mGoalStatus.setText(status);
            mDaysLeft.setText(mGoalWrapper.getDaysLeft());
            LinearLayout.LayoutParams progParams = new LinearLayout.LayoutParams(
                    0, ViewGroup.LayoutParams.MATCH_PARENT,
                    mGoalWrapper.getPercentComplete()
            );
            mProgressBar.setLayoutParams(progParams);
            LinearLayout.LayoutParams invProgParams = new LinearLayout.LayoutParams(
                    0, ViewGroup.LayoutParams.MATCH_PARENT,
                    mGoalWrapper.getPercentLeft()
            );
            mInvProgressBar.setLayoutParams(invProgParams);
            LinearLayout.LayoutParams timeParams = new LinearLayout.LayoutParams(
                    0, ViewGroup.LayoutParams.MATCH_PARENT,
                    mGoalWrapper.getPercentTimeTaken()
            );
            mToDateBar.setLayoutParams(timeParams);
            LinearLayout.LayoutParams invTimeParams = new LinearLayout.LayoutParams(
                    0, ViewGroup.LayoutParams.MATCH_PARENT,
                    mGoalWrapper.getPercentTimeLeft()
            );
            mInvToDateBar.setLayoutParams(invTimeParams);
        }

        private void updateProgressUI() {
            List<Progress> log = new ArrayList<>(); //myGoals.get(progPos).getProgressLog();

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
                LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
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

            private TextView mProgressItemName;
            private TextView mProgressItemText;

            private Progress mProgress;

            private ProgressHolder(View itemView) {
                super(itemView);
                mProgressItemName = itemView.findViewById(R.id.progress_item_name);
                mProgressItemText = itemView.findViewById(R.id.progress_item_text);
            }

            private void bindGoal(Progress progress) {
                mProgress = progress;
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users/" + mProgress.getUserId() + "/name");
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        mProgressItemName.setText(dataSnapshot.getValue(String.class));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                mProgressItemText.setText(progress.getLog());
            }
        }
    }

    private class GoalAdapter extends RecyclerView.Adapter<GoalHolder> {

        private List<Goal> mGoals;

        private GoalAdapter(List<Goal> goals) {
            mGoals = goals;
        }

        @Override
        public GoalHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.layout_past_goal, parent, false);
            return new GoalHolder(view);
        }

        @Override
        public void onBindViewHolder(GoalHolder holder, int position) {
            Goal goal = mGoals.get(position);
            holder.bindGoal(goal);
            progPos++;
        }

        @Override
        public int getItemCount() {
            if(mGoals == null) {
                return 0;
            }
            return mGoals.size();
        }
    }
}
