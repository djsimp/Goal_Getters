package org.truthdefender.goalgetters.goals;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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
import org.truthdefender.goalgetters.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CreateGroupActivity extends AppCompatActivity {
    private RecyclerView mMemberListRecyclerView;
    private RecyclerView mSearchRecyclerView;
    private TextView mGroupName;
    private Spinner mGroupTypeSpinner;
    private List<User> memberList;
    private List<User> userList;
    private String userName;

    String curQuery;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        getUsersName();

        mSearchRecyclerView = findViewById(R.id.search_recycler_view);
        mSearchRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mMemberListRecyclerView = findViewById(R.id.member_list_recycler_view);
        mMemberListRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mGroupName = findViewById(R.id.group_name);

        mGroupTypeSpinner = findViewById(R.id.group_type_spinner);
// Create an ArrayAdapter using the string array and a default spinner layout
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.group_type_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        mGroupTypeSpinner.setAdapter(adapter);

        Button mCreateGroupButton = findViewById(R.id.create_group_button);
        mCreateGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user == null) {
                    return;
                }
                HashMap<String, String> finalMemberList = new HashMap<>();
                finalMemberList.put(user.getUid(), userName);
                for(User member : memberList) {
                    finalMemberList.put(member.getUuid(), member.getName());
                }

                Group group = new Group(mGroupName.getText().toString(), mGroupTypeSpinner.getSelectedItem().toString(), finalMemberList, null);

                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference().child("groups");

                String key = myRef.push().getKey();
                myRef.child(key).setValue(group);
                DatabaseReference ref = myRef.getParent().child("users");
                ref.child(user.getUid()).child("groups").child(key).setValue(group.getName());
                for(User member : memberList) {
                    ref.child(member.getUuid()).child("groups").child(key).setValue(group.getName());
                }
                finish();
            }
        });

        initializeUserList();

        SearchView mSearchInput = findViewById(R.id.search_input);
        mSearchInput.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mSearchRecyclerView.setVisibility(View.VISIBLE);
                curQuery = query;
                updateSearchList(curQuery);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                mSearchRecyclerView.setVisibility(View.VISIBLE);
                curQuery = query;
                updateSearchList(curQuery);
                return true;
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar_create_group);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void getUsersName() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null) {
            return;
        }
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users/" + user.getUid());
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userName = dataSnapshot.child("name").getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userName = dataSnapshot.child("name").getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void initializeUserList() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user == null) {
                    return;
                }
                userList = new ArrayList<>();
                memberList = new ArrayList<>();
                for(DataSnapshot dataUser : dataSnapshot.getChildren()) {
                    if(!dataUser.getKey().equals(user.getUid())) {
                        userList.add(dataUser.getValue(User.class));
                        userList.get(userList.size() - 1).setUuid(dataUser.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //handle databaseError
            }
        });
    }

    private void updateSearchList(String newText) {
        List<User> curUserList = new ArrayList<>();
        for(User user : userList) {
            if((user.getName().toLowerCase().contains(newText.toLowerCase())
                || user.getEmail().toLowerCase().contains(newText.toLowerCase()))
                && !memberList.contains(user)) {
                curUserList.add(user);
            }
        }
        SearchAdapter mSearchAdapter = new SearchAdapter(curUserList);
        mSearchRecyclerView.setAdapter(mSearchAdapter);
    }

    private class SearchHolder extends RecyclerView.ViewHolder {

        private RelativeLayout mSearchListItem;
        private ImageView mSearchItemIcon;
        private TextView mSearchName;
        private User mUser;

        private SearchHolder(View itemView) {
            super(itemView);

            mSearchListItem = itemView.findViewById(R.id.user_list_item);
            mSearchListItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    memberList.add(mUser);
                    updateMemberList();
                    updateSearchList(curQuery);
                }
            });
            mSearchItemIcon = itemView.findViewById(R.id.user_profile_icon);
            mSearchName = itemView.findViewById(R.id.user_name);
        }

        private void bindSearchItem(User user) {
            mUser = user;
            mSearchItemIcon.setImageResource(user.getProfileImageTag());
            mSearchName.setText(user.getName());
        }
    }

    private class SearchAdapter extends RecyclerView.Adapter<SearchHolder> {

        private List<User> mUsers;

        private SearchAdapter(List<User> users) {
            mUsers = users;
        }

        @Override
        public SearchHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(CreateGroupActivity.this);
            View view = layoutInflater.inflate(R.layout.list_item_user, parent, false);
            return new SearchHolder(view);
        }

        @Override
        public void onBindViewHolder(SearchHolder holder, int position) {
            User user = mUsers.get(position);
            holder.bindSearchItem(user);
        }

        @Override
        public int getItemCount() {
            return mUsers.size();
        }
    }





    private void updateMemberList() {
        MemberListAdapter mMemberListAdapter = new MemberListAdapter(memberList);
        mMemberListRecyclerView.setAdapter(mMemberListAdapter);
    }

    private class MemberListHolder extends RecyclerView.ViewHolder {

        private RelativeLayout mMemberListListItem;
        private ImageView mMemberListItemIcon;
        private TextView mMemberListName;
        private User mUser;

        private MemberListHolder(View itemView) {
            super(itemView);

            mMemberListListItem = itemView.findViewById(R.id.user_list_item);
            mMemberListListItem.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            mMemberListListItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    memberList.remove(memberList.indexOf(mUser));
                    updateMemberList();
                    updateSearchList(curQuery);
                }
            });
            mMemberListItemIcon = itemView.findViewById(R.id.user_profile_icon);
            mMemberListName = itemView.findViewById(R.id.user_name);
        }

        private void bindMemberListItem(User user) {
            mUser = user;
            mMemberListItemIcon.setImageResource(user.getProfileImageTag());
            mMemberListName.setText(user.getName());
        }
    }

    private class MemberListAdapter extends RecyclerView.Adapter<MemberListHolder> {

        private List<User> mUsers;

        private MemberListAdapter(List<User> users) {
            mUsers = users;
        }

        @Override
        public MemberListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(CreateGroupActivity.this);
            View view = layoutInflater.inflate(R.layout.list_item_user, parent, false);
            return new MemberListHolder(view);
        }

        @Override
        public void onBindViewHolder(MemberListHolder holder, int position) {
            User user = mUsers.get(position);
            holder.bindMemberListItem(user);
        }

        @Override
        public int getItemCount() {
            return mUsers.size();
        }
    }
}