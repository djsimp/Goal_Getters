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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MyProgressFragment extends Fragment {

    private RecyclerView mProgressLogRecyclerView;
    private Button reportProgressButton;
    private List<Progress> progressLog;

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


        //Update Person Information
        reportProgressButton = (Button)v.findViewById(R.id.report_progress_button);
        reportProgressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show();
            }
        });

        TextView personName = (TextView) v.findViewById(R.id.person_name);
        personName.setText(Singleton.get().getUser().getName());

        TextView goalText = (TextView) v.findViewById(R.id.goalText);
        goalText.setText(Singleton.get().getCurrentGoal().getTitle());



        ImageView profile = (ImageView) v.findViewById(R.id.profile_picture);
        //profile.setBackground();
        //Finish updating person information

        updateUI();


        return v;
    }


    private void reportLog(int amount, String report) {

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
