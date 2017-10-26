package org.truthdefender.goalgetters.goals;

import android.content.Intent;
import android.icu.text.DateFormat;
import android.os.Bundle;
import android.app.FragmentManager;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.NumberPicker;
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
import org.truthdefender.goalgetters.model.Singleton;
import org.truthdefender.goalgetters.model.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by dj on 10/18/17.
 */

public class CreateGroupActivity extends AppCompatActivity {
    private RecyclerView mMemberListRecyclerView;
    private RecyclerView mSearchRecyclerView;
    private Button mCreateGroupButton;
    private TextView mGroupName;

    List<User> memberList;
    List<User> userList;

    String curQuery;

    @Nullable
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        mSearchRecyclerView = (RecyclerView)findViewById(R.id.search_recycler_view);
        mSearchRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mMemberListRecyclerView = (RecyclerView)findViewById(R.id.member_list_recycler_view);
        mMemberListRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mGroupName = (TextView)findViewById(R.id.group_name);

        mCreateGroupButton = (Button)findViewById(R.id.create_group_button);
        mCreateGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> finalMemberList = new HashMap<String, String>();
                for(User member : memberList) {
                    finalMemberList.put(member.getUuid(), member.getName());
                }
                Group group = new Group(mGroupName.getText().toString(), finalMemberList, null);

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference().child("groups");
//                myRef.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        Singleton.get().addGoal(dataSnapshot.getValue(Goal.class));
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//                        Log.w("Create Goal", "Failed to read value.", databaseError.toException());
//                    }
//                });

                String key = myRef.push().getKey();
                myRef.child(key).setValue(group);
                DatabaseReference ref = myRef.getParent().child("users");
                ref.child(user.getUid()).child("groups").child(key).setValue(true);
                for(User member : memberList) {
                    ref.child(member.getUuid()).child("groups").child(key).setValue(true);
                }
                finish();
            }
        });

        initializeUserList();

        SearchView mSearchInput = (SearchView)findViewById(R.id.search_input);
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

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_create_group);
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

    public void initializeUserList() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
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

            mSearchListItem = (RelativeLayout)itemView.findViewById(R.id.user_list_item);
            mSearchListItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    memberList.add(mUser);
                    updateMemberList();
                    updateSearchList(curQuery);
                }
            });
            mSearchItemIcon = (ImageView)itemView.findViewById(R.id.user_profile_icon);
            mSearchName = (TextView)itemView.findViewById(R.id.user_name);
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

            mMemberListListItem = (RelativeLayout)itemView.findViewById(R.id.user_list_item);
            mMemberListListItem.setBackgroundColor(getResources().getColor(R.color.positive_color));
            mMemberListListItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    memberList.remove(memberList.indexOf(mUser));
                    updateMemberList();
                    updateSearchList(curQuery);
                }
            });
            mMemberListItemIcon = (ImageView)itemView.findViewById(R.id.user_profile_icon);
            mMemberListName = (TextView)itemView.findViewById(R.id.user_name);
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