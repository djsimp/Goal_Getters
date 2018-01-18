package org.truthdefender.goalgetters.goals;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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
import org.truthdefender.goalgetters.model.SmartGoal;
import org.truthdefender.goalgetters.model.SmartGoalWrapper;
import org.truthdefender.goalgetters.model.TaskGoal;
import org.truthdefender.goalgetters.model.TaskGoalWrapper;
import org.truthdefender.goalgetters.model.TaskTree;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MyGoalsFragment extends Fragment {

    private RecyclerView mGoalsRecyclerView;

//    static List<String> smartGoalList = new ArrayList<>();
//    static List<String> habitGoalList = new ArrayList<>();
//    static List<String> taskGoalList = new ArrayList<>();
    static HashMap<String, Goal> myGoals = new HashMap<>();
//    static HashMap<String, Goal> completedGoals = new HashMap<String, Goal>();

    static List<String> goalIds;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_my_goals, container, false);

        mGoalsRecyclerView = v.findViewById(R.id.my_goals_recycler_view);

        FloatingActionButton createGoalButton = v.findViewById(R.id.create_goal_button);
        createGoalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CreateGoalActivity.class);
                startActivity(intent);
            }
        });

        getMyGoals();
        return v;
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        updateGoals();
//    }

    public void getMyGoals() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null) {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users/" + user.getUid() + "/goals");
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    goalIds = new ArrayList<>();
                    myGoals = new HashMap<>();
                    for (DataSnapshot goalId : dataSnapshot.getChildren()) {
                        goalIds.add(goalId.getKey());
                    }
                    getGoals();
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
                    getGoals();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    public void getGoals() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("goals");
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
                                goal = new TaskGoal(retrieveTaskTree(dataSnapshot.child("taskTree")),
                                        dataSnapshot.child("startdate").getValue(Date.class),
                                        dataSnapshot.child("deadline").getValue(Date.class),
                                        dataSnapshot.child("group").getValue(String.class));
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
                                goal = new TaskGoal(retrieveTaskTree(dataSnapshot.child("taskTree")),
                                        dataSnapshot.child("startdate").getValue(Date.class),
                                        dataSnapshot.child("deadline").getValue(Date.class),
                                        dataSnapshot.child("group").getValue(String.class));
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

    TaskTree retrieveTaskTree(DataSnapshot dataSnapshot) {
        TaskTree taskTree = new TaskTree(dataSnapshot.child("task").getValue(String.class));
        if(dataSnapshot.hasChild("children")) {
            for (DataSnapshot dataChildTree : dataSnapshot.child("children").getChildren()) {
                taskTree.addChild(retrieveTaskTree(dataChildTree));
            }
        }
        return taskTree;
    }

//    public void updateGoals() {
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
//
//        ref.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                completedGoals = new HashMap<>();
//                for(DataSnapshot dataGoal : dataSnapshot.child("goals").getChildren()) {
//                    if(dataGoal.child("progress").getValue(Integer.class) >= dataGoal.child("goal").getValue(Integer.class)
//                            || dataGoal.child("stopdate").getValue(Date.class) != null) {
//                        Goal goal = dataGoal.getValue(Goal.class);
//                        completedGoals.put(dataGoal.getKey(), goal);
//                    }
//                }
//                initializeGoalsList();
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                //handle databaseError
//            }
//        });
//    }

//    public void moveGoalsToPast(HashMap<String, Goal> goals) {
//        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
//        for(Map.Entry<String, Goal> goal : goals.entrySet()) {
//            ref.child("goals").child(goal.getKey()).removeValue();
//            ref.child("past_goals").child(goal.getKey()).setValue(goal.getValue());
//            ref.child("groups").child(goal.getValue().getGroup()).child("goals").child(goal.getKey()).removeValue();
//            ref.child("groups").child(goal.getValue().getGroup()).child("past_goals").child(goal.getKey()).setValue(true);
//            ref.child("users")
//            for(Group group : Singleton.get().getGroups()) {
//                if(group.getGoals().containsKey(goal.getKey())) {
//                    ref.child("groups").child(group.getGroupId())
//                            .child("goals").child(goal.getKey()).removeValue();
//                    ref.child("groups").child(group.getGroupId())
//                            .child("past_goals").child(goal.getKey())
//                            .setValue(true);
//                    for(Map.Entry<String, String> member : group.getMembers().entrySet()) {
//                        ref.child("users").child(member.getKey()).child("goals").child(goal.getKey()).removeValue();
//                        ref.child("users").child(member.getKey()).child("past_goals").child(goal.getKey()).setValue(true);
//                    }
//                }
//            }
//        }
//    }

//    public void initializeGoalsList() {
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users/" + user.getUid() + "/goals");
//        ref.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                getSmartGoalList(dataSnapshot.child("smart"));
//                getHabitGoalList(dataSnapshot.child("habit"));
//                getTaskGoalList(dataSnapshot.child("task"));
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
//                getSmartGoalList(dataSnapshot.child("smart"));
//                getHabitGoalList(dataSnapshot.child("habit"));
//                getTaskGoalList(dataSnapshot.child("task"));
//                initializeGoals();
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                //handle databaseError
//            }
//        });
//    }

//    private void getSmartGoalList(DataSnapshot dataSnapshot) {
//        smartGoalList = new ArrayList<String>();
//        for(DataSnapshot dataGoal : dataSnapshot.getChildren()) {
//            smartGoalList.add(dataGoal.getKey());
//        }
//    }
//
//    private void getHabitGoalList(DataSnapshot dataSnapshot) {
//        habitGoalList = new ArrayList<String>();
//        for(DataSnapshot dataGoal : dataSnapshot.getChildren()) {
//            habitGoalList.add(dataGoal.getKey());
//        }
//    }
//
//    private void getTaskGoalList(DataSnapshot dataSnapshot) {
//        taskGoalList = new ArrayList<String>();
//        for(DataSnapshot dataGoal : dataSnapshot.getChildren()) {
//            taskGoalList.add(dataGoal.getKey());
//        }
//    }
//
//    public void initializeGoals() {
//        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("goals");
//        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                getGoals(dataSnapshot);
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
//                getGoals(dataSnapshot);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//    }

//    private void getGoals(DataSnapshot dataSnapshot) {
//        myGoals = new ArrayList<Goal>();
//        for(DataSnapshot dataGoal : dataSnapshot.getChildren()) {
//            if(habitGoalList.contains(dataGoal.getKey())) {
//                myGoals.add(dataGoal.getValue(HabitGoal.class));
//                myGoals.get(myGoals.size() - 1).setGoalId(dataGoal.getKey());
//            } else if(smartGoalList.contains(dataGoal.getKey())) {
//                myGoals.add(dataGoal.getValue(SmartGoal.class));
//                myGoals.get(myGoals.size() - 1).setGoalId(dataGoal.getKey());
//            } else if(taskGoalList.contains(dataGoal.getKey())) {
//                myGoals.add(dataGoal.getValue(TaskGoal.class));
//                myGoals.get(myGoals.size() - 1).setGoalId(dataGoal.getKey());
//            }
//        }
//        updateUI();
//    }

    //Recycler view copy

    private void updateUI() {
        List<Goal> goals = new ArrayList<>(myGoals.values());
        GoalAdapter mGoalAdapter = new GoalAdapter(goals);
        mGoalsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mGoalsRecyclerView.setAdapter(mGoalAdapter);
    }

    private class GoalHolder extends RecyclerView.ViewHolder {

        private LinearLayout mListItemGoal;
        private CardView mGoalCard;
        private TextView mGoalTitle;
       // private View mGoalTotalBar;
        private View mProgressBar;
        private View mInvProgressBar;
        private View mToDateBar;
        private View mInvToDateBar;
        private TextView mGoalStatus, mDaysLeft;

        private Goal mGoal;
        private GoalWrapper mGoalWrapper;

        private GoalHolder(View itemView) {
            super(itemView);

            mListItemGoal = itemView.findViewById(R.id.list_item_goal);
            mListItemGoal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), GoalActivity.class);
                    intent.putExtra("goalId", mGoal.getGoalId());
                    startActivity(intent);
                }
            });
            mGoalCard = itemView.findViewById(R.id.goal_card);
            mGoalCard.bringToFront();
            mGoalTitle = itemView.findViewById(R.id.goal_title);
            mProgressBar = itemView.findViewById(R.id.progress_bar);
            mInvProgressBar = itemView.findViewById(R.id.inv_progress_bar);
            mToDateBar = itemView.findViewById(R.id.to_date_bar);
            mInvToDateBar = itemView.findViewById(R.id.inv_to_date_bar);
            mGoalStatus = itemView.findViewById(R.id.goal_status);
            mDaysLeft = itemView.findViewById(R.id.days_left);
        }

        private void bindGoal(Goal goal) {
            mGoal = goal;
            if(goal instanceof SmartGoal) {
                mGoalWrapper = new SmartGoalWrapper((SmartGoal)mGoal);
            } else if(goal instanceof HabitGoal) {
                mGoalWrapper = new HabitGoalWrapper((HabitGoal)mGoal);
            } else if(goal instanceof TaskGoal) {
                mGoalWrapper = new TaskGoalWrapper((TaskGoal)mGoal);
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
    }

    private class GoalAdapter extends RecyclerView.Adapter<GoalHolder> {

        private List<Goal> mGoals;

        private GoalAdapter(List<Goal> goals) {
            mGoals = goals;
        }

        @Override
        public GoalHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.layout_goal_card, parent, false);
            return new GoalHolder(view);
        }

        @Override
        public void onBindViewHolder(GoalHolder holder, int position) {
            Goal goal = mGoals.get(position);
            holder.bindGoal(goal);
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
