package org.truthdefender.goalgetters.goals;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.truthdefender.goalgetters.R;
import org.truthdefender.goalgetters.model.Goal;
import org.truthdefender.goalgetters.model.Person;
import org.truthdefender.goalgetters.model.Progress;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GroupMembersFragment extends Fragment {

    private RecyclerView mMemberRecyclerView;
    private ImageButton mAddMemberButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_group_members, container, false);

        mMemberRecyclerView = (RecyclerView)v.findViewById(R.id.group_members_recycler_view);
        mMemberRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAddMemberButton = (ImageButton)v.findViewById(R.id.add_member_button);
        mAddMemberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        updateUI();

        return v;
    }

    private List<Person> generateMembers() {
        List<Person> members = new ArrayList<>();
        for(int i = 0; i < 15; i++) {
            members.add(new Person());
        }
        return members;
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
            View view = layoutInflater.inflate(R.layout.layout_group_member, parent, false);
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
        private ImageButton mDeleteMemberButton;
        private Person mMember;

        private MemberHolder(View itemView) {
            super(itemView);
            mPersonName = (TextView)itemView.findViewById(R.id.person_name);
            mPersonName.setText("DJ");

            mDeleteMemberButton = (ImageButton)itemView.findViewById(R.id.delete_member_button);
            mDeleteMemberButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

        }

        private void bindGoal(Person member) {
            mMember = member;
//            mProgressItemDate.setText(progress.getDate());
//            mProgressItemText.setText(progress.getReport());
        }
    }
}
