package org.truthdefender.goalgetters.goals;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import org.truthdefender.goalgetters.model.Person;
import org.truthdefender.goalgetters.model.Progress;
import org.truthdefender.goalgetters.model.Singleton;
import org.w3c.dom.Text;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MyProgressFragment extends Fragment {

    private RecyclerView mProgressLogRecyclerView;
    private Button reportProgressButton;
    private List<Progress> progressLog;
    private View mProgressBar;
    private View mInvProgressBar;
    private View mToDateBar;
    private View mInvToDateBar;
    private TextView goalStatus;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_my_progress, container, false);

        mProgressLogRecyclerView = (RecyclerView)v.findViewById(R.id.my_progress_recycler_view);
        mProgressLogRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));





        //Update big goal
        TextView goalTitle = (TextView) getActivity().findViewById(R.id.goal_title);
        goalTitle.setText(Singleton.get().getCurrentGoal().getTitle());

        goalStatus = (TextView) getActivity().findViewById(R.id.goal_status);

        TextView timeLeft = (TextView) getActivity().findViewById(R.id.days_left);
        timeLeft.setText(Singleton.get().getCurrentGoal().getDaysLeft());
        mProgressBar = getActivity().findViewById(R.id.progress_bar);
        mInvProgressBar = getActivity().findViewById(R.id.inv_progress_bar);
        mToDateBar = getActivity().findViewById(R.id.to_date_bar);
        mInvToDateBar = getActivity().findViewById(R.id.inv_to_date_bar);

        updateBar();


        //Update Person Information
        reportProgressButton = (Button)v.findViewById(R.id.report_progress_button);
        reportProgressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show();
            }
        });


        //profile.setBackground();
        //Finish updating person information
        return v;
    }

    public void updateBar() {
        LinearLayout.LayoutParams progParams = new LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.MATCH_PARENT,
                Singleton.get().getCurrentGoal().getPercentComplete()
        );
        mProgressBar.setLayoutParams(progParams);
        LinearLayout.LayoutParams invProgParams = new LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.MATCH_PARENT,
                Singleton.get().getCurrentGoal().getPercentLeft()
        );
        mInvProgressBar.setLayoutParams(invProgParams);
        LinearLayout.LayoutParams timeParams = new LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.MATCH_PARENT,
                Singleton.get().getCurrentGoal().getPercentTimeTaken()
        );
        mToDateBar.setLayoutParams(timeParams);
        LinearLayout.LayoutParams invTimeParams = new LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.MATCH_PARENT,
                Singleton.get().getCurrentGoal().getPercentTimeLeft()
        );
        mInvToDateBar.setLayoutParams(invTimeParams);
        goalStatus.setText(Singleton.get().getCurrentGoal().getStatus());
    }

    @Override
    public void onResume() {
        super.onResume();
        initializeProgressList();
    }

    public void initializeProgressList() {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("goals/" + Singleton.get().getCurrentGoal().getGoalId() + "/progress_log");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Progress> progressList = new ArrayList<Progress>();
                for (DataSnapshot dataProg : dataSnapshot.getChildren()) {
                    progressList.add(dataProg.getValue(Progress.class));
                }
                Singleton.get().getCurrentGoal().setProgressLog(progressList);
                updateUI();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Progress> progressList = new ArrayList<Progress>();
                for (DataSnapshot dataProg : dataSnapshot.getChildren()) {
                    progressList.add(dataProg.getValue(Progress.class));
                }
                Singleton.get().getCurrentGoal().setProgressLog(progressList);
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

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("goals").child(Singleton.get().getCurrentGoal().getGoalId());
//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//            }
//        });

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        Date date = Calendar.getInstance().getTime();

        Progress progress = new Progress(df.format(date), report, user.getUid(), Singleton.get().getUser().getName(), amount);

        //String date, String report, String userId, String name, int amount

        myRef.push().setValue(progress);
        Singleton.get().getCurrentGoal().addProgressToLog(progress);
        Singleton.get().getCurrentGoal().setProgress(Singleton.get().getCurrentGoal().getProgress() + amount);
        myRef.child("progress").setValue(Singleton.get().getCurrentGoal().getProgress());
        updateBar();
        updateUI();
    }

    //Recycler view copy

    private void updateUI() {
        List<Progress> log = Singleton.get().getCurrentGoal().getProgressLog();
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
            mProgressItemName = (TextView)itemView.findViewById(R.id.progress_item_name);
            mProgressItemText = (TextView)itemView.findViewById(R.id.progress_item_text);
        }

        private void bindGoal(Progress progress) {
            mProgress = progress;
            mProgressItemName.setText(progress.getName());
            mProgressItemText.setText(progress.getLog());
//            if(progress.getReport().startsWith("+")) {
//                mProgressItemText.setTextColor(getResources().getColor(R.color.positive_color));
//            } else {
//                mProgressItemText.setTextColor(getResources().getColor(R.color.negative_color));
//            }
        }
    }

    public void show() {
        final Dialog reportDialog = new Dialog(getActivity());
        reportDialog.setTitle("Set Amount");
        reportDialog.setContentView(R.layout.report_progress_layout);
        Button setBtn = (Button) reportDialog.findViewById(R.id.setBtn);
        Button cnlBtn = (Button) reportDialog.findViewById(R.id.CancelButton_NumberPicker);
        TextView units = (TextView) reportDialog.findViewById(R.id.report_units);
        units.setText(Singleton.get().getCurrentGoal().getUnits());

        final EditText description = (EditText) reportDialog.findViewById(R.id.report_description);

        final NumberPicker numberPicker = (NumberPicker) reportDialog.findViewById(R.id.number_picker);

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
