package org.truthdefender.goalgetters.goals;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import org.truthdefender.goalgetters.model.Person;
import org.truthdefender.goalgetters.model.Singleton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyGoalsFragment extends Fragment {

    private RecyclerView mGoalsRecyclerView;
    private FloatingActionButton createGoalButton;

    static List<String> goalList = new ArrayList<>();
    static List<Goal> myGoals = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_my_goals, container, false);

        mGoalsRecyclerView = (RecyclerView)v.findViewById(R.id.my_goals_recycler_view);
        mGoalsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        createGoalButton = (FloatingActionButton)v.findViewById(R.id.create_goal_button);
        createGoalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CreateGoalActivity.class);
                startActivity(intent);
            }
        });
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        initializeGoalsList();
    }

    public void initializeGoalsList() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users/" + user.getUid() + "/goals");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                goalList = new ArrayList<String>();
                for(DataSnapshot dataGoal : dataSnapshot.getChildren()) {
                        goalList.add(dataGoal.getKey());
                }
                initializeGoals();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //handle databaseError
            }
        });

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                goalList = new ArrayList<String>();
                for(DataSnapshot dataGoal : dataSnapshot.getChildren()) {
                        goalList.add(dataGoal.getKey());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //handle databaseError
            }
        });
    }

    public void initializeGoals() {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("goals");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                myGoals = new ArrayList<Goal>();
                for(DataSnapshot dataGoal : dataSnapshot.getChildren()) {
                    if(goalList.contains(dataGoal.getKey())) {
                        myGoals.add(dataGoal.getValue(Goal.class));
                    }
                }
                updateUI();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                myGoals = new ArrayList<Goal>();
                for(DataSnapshot dataGoal : dataSnapshot.getChildren()) {
                    if(goalList.contains(dataGoal.getKey())) {
                        myGoals.add(dataGoal.getValue(Goal.class));
                        myGoals.get(myGoals.size()-1).setGoalId(dataGoal.getKey());
                    }
                }
                updateUI();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //Recycler view copy

    private void updateUI() {
        GoalAdapter mGoalAdapter = new GoalAdapter(myGoals);
        mGoalsRecyclerView.setAdapter(mGoalAdapter);
    }

    private class GoalHolder extends RecyclerView.ViewHolder {

        private LinearLayout mListItemGoal;
        private CardView mGoalCard;
        private TextView mGoalTitle;
        private View mGoalTotalBar;
        private View mProgressBar;
        private View mInvProgressBar;
        private View mToDateBar;
        private View mInvToDateBar;
        private TextView mGoalStatus, mDaysLeft;

        private Goal mGoal;

        private GoalHolder(View itemView) {
            super(itemView);

            mListItemGoal = (LinearLayout) itemView.findViewById(R.id.list_item_goal);
            mListItemGoal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Singleton.get().setCurrentGoal(mGoal);
                    Intent intent = new Intent(getActivity(), GoalActivity.class);
                    startActivity(intent);
                }
            });
            mGoalCard = (CardView)itemView.findViewById(R.id.goal_card);
            mGoalTitle = (TextView)itemView.findViewById(R.id.goal_title);
            mProgressBar = itemView.findViewById(R.id.progress_bar);
            mInvProgressBar = itemView.findViewById(R.id.inv_progress_bar);
            mToDateBar = itemView.findViewById(R.id.to_date_bar);
            mInvToDateBar = itemView.findViewById(R.id.inv_to_date_bar);
            mGoalStatus = (TextView)itemView.findViewById(R.id.goal_status);
            mDaysLeft = (TextView)itemView.findViewById(R.id.days_left);
        }

        private void bindGoal(Goal goal) {
            mGoal = goal;
            mGoalTitle.setText(goal.getTitle());
            String status = goal.getStatus();
            mGoalStatus.setText(status);
            if(status.substring(status.length()-5).equals("ahead")) {
                mListItemGoal.setBackgroundColor(getResources().getColor(R.color.positive_color));
            } else if(status.substring(status.length()-5).equals("behind")) {
                mListItemGoal.setBackgroundColor(getResources().getColor(R.color.negative_color));
            }
            mDaysLeft.setText(goal.getDaysLeft());
            LinearLayout.LayoutParams progParams = new LinearLayout.LayoutParams(
                    0, ViewGroup.LayoutParams.MATCH_PARENT,
                    goal.getPercentComplete()
            );
            mProgressBar.setLayoutParams(progParams);
            LinearLayout.LayoutParams invProgParams = new LinearLayout.LayoutParams(
                    0, ViewGroup.LayoutParams.MATCH_PARENT,
                    goal.getPercentLeft()
            );
            mInvProgressBar.setLayoutParams(invProgParams);
            LinearLayout.LayoutParams timeParams = new LinearLayout.LayoutParams(
                    0, ViewGroup.LayoutParams.MATCH_PARENT,
                    goal.getPercentTimeTaken()
            );
            mToDateBar.setLayoutParams(timeParams);
            LinearLayout.LayoutParams invTimeParams = new LinearLayout.LayoutParams(
                    0, ViewGroup.LayoutParams.MATCH_PARENT,
                    goal.getPercentTimeLeft()
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
