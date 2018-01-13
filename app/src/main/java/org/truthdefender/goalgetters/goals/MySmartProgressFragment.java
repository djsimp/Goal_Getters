package org.truthdefender.goalgetters.goals;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.truthdefender.goalgetters.R;
import org.truthdefender.goalgetters.model.Progress;
import org.truthdefender.goalgetters.model.SmartGoal;
import org.truthdefender.goalgetters.model.SmartGoalWrapper;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MySmartProgressFragment extends Fragment {

    private RecyclerView mProgressLogRecyclerView;
    private List<Progress> progressLog;
    private String goalId;
    private int totalAmount;
    private SmartGoal myGoal;
    private SmartGoalWrapper myGoalWrapper;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_my_smart_progress, container, false);

        if (getArguments() != null) {
            goalId = getArguments().getString("goalId");
        }

        retrieveGoal();

        mProgressLogRecyclerView = v.findViewById(R.id.my_progress_recycler_view);
        mProgressLogRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        //Update Person Information
        Button reportProgressButton = v.findViewById(R.id.report_progress_button);
        reportProgressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    showSmartProgressDialog();
            }
        });

        initializeProgressList();

        //profile.setBackground();
        //Finish updating person information
        return v;
    }

    public void retrieveGoal() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("goals/" + goalId);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                myGoal = dataSnapshot.getValue(SmartGoal.class);
                myGoalWrapper = new SmartGoalWrapper(myGoal);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                myGoal = dataSnapshot.getValue(SmartGoal.class);
                myGoalWrapper = new SmartGoalWrapper(myGoal);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void initializeProgressList() {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("logs/" + goalId);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                totalAmount = 0;
                progressLog = new ArrayList<>();
                for (DataSnapshot dataProg : dataSnapshot.getChildren()) {
                    Progress progress = dataProg.getValue(Progress.class);
                    if(progress != null) {
                        progressLog.add(progress);
                        totalAmount += progress.getAmount();
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
                totalAmount = 0;
                progressLog = new ArrayList<>();
                for (DataSnapshot dataProg : dataSnapshot.getChildren()) {
                    Progress progress = dataProg.getValue(Progress.class);
                    if(progress != null) {
                        progressLog.add(progress);
                        totalAmount += progress.getAmount();
                    }
                }
                updateUI();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void reportLog(int amount, String report) {
        if(progressLog == null) {
            progressLog = new ArrayList<>();
        }
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();

        DateFormat df =  DateFormat.getDateTimeInstance(); //new SimpleDateFormat("yyyy-MM-dd hh:mm");
        Date date = Calendar.getInstance().getTime();

        String userId = "";
        if(user != null) {
            userId = user.getUid();
        }
        Progress progress = new Progress(df.format(date), report, userId, amount);

        //String date, String report, String userId, String name, int amount

        myRef.child("logs").child(goalId).push().setValue(progress);
        totalAmount += amount;
        myRef.child("goals").child(goalId).child("progress").setValue(totalAmount);
        myGoal.setProgress(totalAmount);
        if (myGoal.getProgress() >= myGoal.getGoal()) {
            myRef.child("goals").child(goalId).removeValue();
            myRef.child("past_goals").child(goalId).setValue(myGoal);
            myRef.child("users").child(userId).child("goals").child(goalId).removeValue();
            myRef.child("users").child(userId).child("past_goals").child(goalId).setValue(myGoalWrapper.getTitle());
        }
    }

    //Recycler view copy

    private void updateUI() {
        ProgressAdapter mProgressAdapter = new ProgressAdapter(progressLog);
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
            if(mLogs == null) {
                return 0;
            }
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

    public void showSmartProgressDialog() {
        if(getActivity() == null) {
            return;
        }
        final Dialog reportDialog = new Dialog(getActivity());
        reportDialog.setTitle("Report Progress");
        reportDialog.setContentView(R.layout.report_progress_smart_layout);
        Button setBtn = reportDialog.findViewById(R.id.setBtn);
        Button cnlBtn = reportDialog.findViewById(R.id.CancelButton_NumberPicker);
        TextView units = reportDialog.findViewById(R.id.report_units);
        units.setText(myGoal.getUnits());

        final EditText description = reportDialog.findViewById(R.id.report_description);

        final NumberPicker numberPicker = reportDialog.findViewById(R.id.number_picker);

//        final int minValue = -500;
//        final int maxValue = 500;
//        numberPicker.setMinValue(0);
//        numberPicker.setMaxValue(maxValue - minValue);
//        numberPicker.setValue(0 - minValue);
//        numberPicker.setFormatter(new NumberPicker.Formatter() {
//            @Override
//            public String format(int index) {
//                return Integer.toString(index + minValue);
//            }
//        });

        numberPicker.setMaxValue(1000000);
        numberPicker.setMinValue(0);
        numberPicker.setWrapSelectorWheel(false);
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                // TODO Auto-generated method stub
            }
        });
        setBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //We're going to have to make some kind of call to the server here................
                String number = String.valueOf(numberPicker.getValue());
                String report = description.getText().toString();

                reportDialog.dismiss();
                reportLog(Integer.valueOf(number), report);
            }
        });

        cnlBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reportDialog.dismiss();
            }
        });

        reportDialog.show();
    }
}
