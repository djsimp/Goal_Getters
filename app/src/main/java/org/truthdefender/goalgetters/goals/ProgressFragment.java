package org.truthdefender.goalgetters.goals;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.truthdefender.goalgetters.R;
import org.truthdefender.goalgetters.model.Goal;
import org.truthdefender.goalgetters.model.GoalWrapper;
import org.truthdefender.goalgetters.model.Person;
import org.truthdefender.goalgetters.model.Progress;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProgressFragment extends Fragment {

    private RecyclerView mMemberRecyclerView;
    private List<Progress> progressLog;
    private GoalWrapper goalWrapper;
    private String goalId;
    private Goal goal;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_progress, container, false);

        if (getArguments() != null) {
            goalId = getArguments().getString("goalId");
        }

        mMemberRecyclerView = (RecyclerView)v.findViewById(R.id.member_recycler_view);
        mMemberRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        initializeProgressList();

        return v;
    }

    private List<Person> generateMembers() {
        List<Person> members = new ArrayList<>();
        for(int i = 0; i < 15; i++) {
                members.add(new Person());
        }
        return members;
    }

    public void initializeProgressList() {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("logs/" + goalId);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressLog = new ArrayList<Progress>();
                for (DataSnapshot dataProg : dataSnapshot.getChildren()) {
                    progressLog.add(dataProg.getValue(Progress.class));
                }
                //goalWrapper.setProgressLog(progressList);
                updateUI();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressLog = new ArrayList<Progress>();
                for (DataSnapshot dataProg : dataSnapshot.getChildren()) {
                    progressLog.add(dataProg.getValue(Progress.class));
                }
                //goalWrapper.setProgressLog(progressList);
                updateUI();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //Recycler view copy

    private void updateUI() {
        List<Person> members = generateMembers();

        MemberAdapter mMemberAdapter = new MemberAdapter(members);
        mMemberRecyclerView.setAdapter(mMemberAdapter);
    }

    private class MemberAdapter extends RecyclerView.Adapter<MemberHolder> {

        private List<Person> mMembers;

        private MemberAdapter(List<Person> members) {
            mMembers = members;
        }

        @Override
        public MemberHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.layout_member_progress, parent, false);
            return new MemberHolder(view);
        }

        @Override
        public void onBindViewHolder(MemberHolder holder, int position) {
            Person member = mMembers.get(position);
            holder.bindGoal(member);
        }

        @Override
        public int getItemCount() {
            return mMembers.size();
        }
    }

    private class MemberHolder extends RecyclerView.ViewHolder {

        private TextView mPersonName;
        private RecyclerView mProgressLogRecyclerView;
        private RelativeLayout mMemberProgressCard;

        private Person mMember;

        private MemberHolder(View itemView) {
            super(itemView);
            mPersonName = (TextView)itemView.findViewById(R.id.person_name);
            mPersonName.setText("DJ");

            mProgressLogRecyclerView = (RecyclerView) itemView.findViewById(R.id.member_progress_recycler_view);
            mProgressLogRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

            mMemberProgressCard = (RelativeLayout)itemView.findViewById(R.id.member_progress_card);
            mMemberProgressCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mProgressLogRecyclerView.getVisibility() == View.VISIBLE) {
                        mProgressLogRecyclerView.setVisibility(View.GONE);
                    } else {
                        mProgressLogRecyclerView.setVisibility(View.VISIBLE);
                    }
                }
            });

            updateProgressUI();
        }

        private void bindGoal(Person member) {
            mMember = member;
//            mProgressItemDate.setText(progress.getDate());
//            mProgressItemText.setText(progress.getReport());
        }

        //Recycler view copy

        private void updateProgressUI() {
            List<Progress> log = goalWrapper.getProgressLog();

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
}
