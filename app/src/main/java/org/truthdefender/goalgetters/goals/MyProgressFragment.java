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
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.truthdefender.goalgetters.R;
import org.truthdefender.goalgetters.model.Goal;
import org.truthdefender.goalgetters.model.Person;
import org.truthdefender.goalgetters.model.Progress;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MyProgressFragment extends Fragment {

    private RecyclerView mProgressLogRecyclerView;
    private Button reportProgressButton;

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

        reportProgressButton = (Button)v.findViewById(R.id.report_progress_button);
        reportProgressButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                show();
            }
        });

        updateUI();


        return v;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    private List<Progress> generateProgress() {
        List<Progress> log = new ArrayList<>();
        for(int i = 0; i < 15; i++) {
            if(i%2 == 0) {
                log.add(new Progress("October 20, 2017", "+$150 - I am making some great progress!"));
            } else {
                log.add(new Progress("October 20, 2017", "-$100 - Another one bites the dust!"));
            }
        }
        return log;
    }

    //Recycler view copy

    private void updateUI() {
        List<Progress> log = generateProgress();

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

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void show() {
        final Dialog reportDialog = new Dialog(this.getContext());
        reportDialog.setTitle("Set Amount");
        reportDialog.setContentView(R.layout.report_progress_layout);
        Button setBtn = (Button) reportDialog.findViewById(R.id.setBtn);
        Button cnlBtn = (Button) reportDialog.findViewById(R.id.CancelButton_NumberPicker);

        final EditText description = (EditText) reportDialog.findViewById(R.id.report_description);

        final NumberPicker numberPicker = (NumberPicker) reportDialog.findViewById(R.id.number_picker);
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
