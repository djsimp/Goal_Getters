package org.truthdefender.goalgetters.goals;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import org.truthdefender.goalgetters.model.Group;
import org.truthdefender.goalgetters.model.Person;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class MyGroupsFragment extends Fragment {

    private TextView mSectionTitle;
    private RecyclerView mGroupsRecyclerView;
    private FloatingActionButton createGroupButton;

    static List<String> groupList;
    static List<Group> myGroups;

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


        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        initializeGroupList();
    }

    public void initializeGroupList() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users/" + user.getUid() + "/groups");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                groupList = new ArrayList<String>();
                for(DataSnapshot dataGroup : dataSnapshot.getChildren()) {
                    groupList.add(dataGroup.getKey());
                }
                initializeGroups();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //handle databaseError
            }
        });

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                groupList = new ArrayList<String>();
                for(DataSnapshot dataGroup : dataSnapshot.getChildren()) {
                    if(!groupList.contains(dataGroup.getKey())) {
                        groupList.add(dataGroup.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //handle databaseError
            }
        });
    }

    public void initializeGroups() {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("groups");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                myGroups = new ArrayList<Group>();
                for(DataSnapshot dataGroup : dataSnapshot.getChildren()) {
                    if(groupList.contains(dataGroup.getKey())) {
                        myGroups.add(dataGroup.getValue(Group.class));
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
                myGroups = new ArrayList<Group>();
                for(DataSnapshot dataGroup : dataSnapshot.getChildren()) {
                    if(groupList.contains(dataGroup.getKey())) {
                        myGroups.add(dataGroup.getValue(Group.class));
                    }
                }
                updateUI();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void updateUI() {
        GroupAdapter mGroupAdapter = new GroupAdapter(myGroups);
        mGroupsRecyclerView.setAdapter(mGroupAdapter);
    }


    private class GroupHolder extends RecyclerView.ViewHolder {

        private CardView mGroupCard;
        private TextView mGroupName;
        private TextView mGroupMemberList;

        private Group mGroup;

        private GroupHolder(View itemView) {
            super(itemView);

            mGroupCard = (CardView) itemView.findViewById(R.id.group_card);
            mGroupCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), GroupActivity.class);
                    startActivity(intent);
                }
            });
            mGroupName = (TextView)itemView.findViewById(R.id.group_name);

            mGroupMemberList = (TextView)itemView.findViewById(R.id.group_member_list);
        }

        private void bindGroup(Group group) {
            mGroup = group;
            mGroupName.setText(group.getName());
            mGroupMemberList.setText(group.getMemberList());
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
            View view = layoutInflater.inflate(R.layout.layout_group_card, parent, false);
            return new MyGroupsFragment.GroupHolder(view);
        }

        @Override
        public void onBindViewHolder(GroupHolder holder, int position) {
            Group group = mGroups.get(position);
            holder.bindGroup(group);
        }

        @Override
        public int getItemCount() {
            if(mGroups == null) {
                return 0;
            }
            return mGroups.size();
        }
    }
}
