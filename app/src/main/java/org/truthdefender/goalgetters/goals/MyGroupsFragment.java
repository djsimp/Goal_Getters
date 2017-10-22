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
import org.truthdefender.goalgetters.model.Group;
import org.truthdefender.goalgetters.model.Person;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MyGroupsFragment extends Fragment {

    private TextView mSectionTitle;
    private RecyclerView mGroupsRecyclerView;
    private FloatingActionButton createGroupButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_my_groups, container, false);

        mSectionTitle = (TextView)v.findViewById(R.id.section_title_text);
        mSectionTitle.setText("My Groups");

        mGroupsRecyclerView = (RecyclerView)v.findViewById(R.id.my_groups_recycler_view);
        mGroupsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        createGroupButton = (FloatingActionButton)v.findViewById(R.id.create_group_button);
        createGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CreateGroupActivity.class);
                startActivity(intent);
            }
        });

        updateUI();


        return v;
    }

    private List<Group> generateGroups() {
        List<Group> groups = new ArrayList<>();
        for(int i = 0; i < 10; i++) {
            groups.add(new Group(null, "The Group Who Shall Not Be Named"));
        }
        return groups;
    }

    //Recycler view copy

    private void updateUI() {
        //List<Group> Groups = GroupGroups.getMyGroups();
        List<Group> groups = generateGroups();

        GroupAdapter mGroupAdapter = new GroupAdapter(groups);
        mGroupsRecyclerView.setAdapter(mGroupAdapter);
    }


    private class GroupHolder extends RecyclerView.ViewHolder {

        private LinearLayout mListItemGroup;
        private TextView mGroupTitle;
        private View mGroupTotalBar;
        private View mProgressBar;
        private View mInvProgressBar;
        private View mToDateBar;
        private View mInvToDateBar;
        private RelativeLayout mLeftOfDaysLeft;
        private TextView mDaysLeft;
        private RelativeLayout mRightOfDaysLeft;

        private Group mGroup;

        private GroupHolder(View itemView) {
            super(itemView);

//            mListItemGroup = (LinearLayout)itemView.findViewById(R.id.list_item_group);
//            mListItemGroup.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
////                    PeopleEvents.get().setCurrentEvent(mEvent);
//                    Intent intent = new Intent(getActivity(), GroupActivity.class);
//                    startActivity(intent);
//                }
//            });
//            mGroupTitle = (TextView)itemView.findViewById(R.id.group_title);
//            mProgressBar = itemView.findViewById(R.id.progress_bar);
//            mInvProgressBar = itemView.findViewById(R.id.inv_progress_bar);
//            mToDateBar = itemView.findViewById(R.id.to_date_bar);
//            mInvToDateBar = itemView.findViewById(R.id.inv_to_date_bar);
//            mLeftOfDaysLeft = (RelativeLayout)itemView.findViewById(R.id.left_of_days_left);
//            mDaysLeft = (TextView)itemView.findViewById(R.id.days_left);
//            mRightOfDaysLeft = (RelativeLayout)itemView.findViewById(R.id.right_of_days_left);
        }

        private void bindGroup(Group group) {
            mGroup = group;
           // mGroupTitle.setText(group.getTitle());
            //mDaysLeft.setText("21 Days Left");
        }
    }

    private class GroupAdapter extends RecyclerView.Adapter<GroupHolder> {

        private List<Group> mGroups;

        private GroupAdapter(List<Group> groups) {
            mGroups = groups;
        }

        @Override
        public GroupHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.layout_group, parent, false);
            return new MyGroupsFragment.GroupHolder(view);
        }

        @Override
        public void onBindViewHolder(GroupHolder holder, int position) {
            Group group = mGroups.get(position);
            holder.bindGroup(group);
        }

        @Override
        public int getItemCount() {
            return mGroups.size();
        }
    }
}
