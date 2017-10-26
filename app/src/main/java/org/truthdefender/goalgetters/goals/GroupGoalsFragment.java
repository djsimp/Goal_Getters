package org.truthdefender.goalgetters.goals;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.truthdefender.goalgetters.R;
import org.truthdefender.goalgetters.model.Goal;
import org.truthdefender.goalgetters.model.Group;
import org.truthdefender.goalgetters.model.Person;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class GroupGoalsFragment extends Fragment {

    private RecyclerView mGoalsRecyclerView;
    private FloatingActionButton createGoalButton;

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

        updateUI();


        return v;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    private List<Goal> generateGoals() {
        List<Goal> goals = new ArrayList<>();
        for(int i = 0; i < 10; i++) {
            goals.add(new Goal("This is a Great Goal", "$", 5000, 2500, Calendar.getInstance().getTime(), Calendar.getInstance().getTime(), "Hello"));
        }
        return goals;
    }

    //Recycler view copy

    private void updateUI() {
        //List<Goal> goals = goalGroups.getMyGoals();
        List<Goal> goals = generateGoals();

        GoalAdapter mGoalAdapter = new GoalAdapter(goals);
        mGoalsRecyclerView.setAdapter(mGoalAdapter);
    }


    private class GoalHolder extends RecyclerView.ViewHolder {

        private LinearLayout mListItemGoal;
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

            mListItemGoal = (LinearLayout)itemView.findViewById(R.id.list_item_goal);
            mListItemGoal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    PeopleEvents.get().setCurrentEvent(mEvent);
                    Intent intent = new Intent(getActivity(), GoalActivity.class);
                    startActivity(intent);
                }
            });
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
            mDaysLeft.setText("21 Days Left");
            LinearLayout.LayoutParams progParams = new LinearLayout.LayoutParams(
                    0, ViewGroup.LayoutParams.MATCH_PARENT,
                    (float)(goal.getProgress() / goal.getGoal())
            );
            mProgressBar.setLayoutParams(progParams);
            LinearLayout.LayoutParams invProgParams = new LinearLayout.LayoutParams(
                    0, ViewGroup.LayoutParams.MATCH_PARENT,
                    (float)(1 - (goal.getProgress() / goal.getGoal()))
            );
            mInvProgressBar.setLayoutParams(invProgParams);
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
            return mGoals.size();
        }
    }
}
