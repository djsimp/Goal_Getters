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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.truthdefender.goalgetters.R;
import org.truthdefender.goalgetters.model.Goal;
import org.truthdefender.goalgetters.model.Person;

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
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(getActivity(), ReportProgressActivity.class);
//                startActivity(intent);
            }
        });

        updateUI();


        return v;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    private List<String> generateProgress() {
        List<String> log = new ArrayList<>();
        for(int i = 0; i < 15; i++) {
            if(i%2 == 0) {
                log.add("+$150 - I am making some great progress!");
            } else {
                log.add("-$100 - Another one bites the dust!");
            }
        }
        return log;
    }

    //Recycler view copy

    private void updateUI() {
        List<String> log = generateProgress();

        ProgressAdapter mProgressAdapter = new ProgressAdapter(log);
        mProgressLogRecyclerView.setAdapter(mProgressAdapter);
    }


    private class ProgressHolder extends RecyclerView.ViewHolder {

        private TextView mProgressItem;

        private String mProgress;

        private ProgressHolder(View itemView) {
            super(itemView);
            mProgressItem = (TextView)itemView.findViewById(R.id.progress_log_item);
        }

        private void bindGoal(String progress) {
            mProgress = progress;
            mProgressItem.setText(progress);
            if(progress.startsWith("+")) {
                mProgressItem.setTextColor(getResources().getColor(R.color.positive_color));
            } else {
                mProgressItem.setTextColor(getResources().getColor(R.color.negative_color));
            }
        }
    }

    private class ProgressAdapter extends RecyclerView.Adapter<ProgressHolder> {

        private List<String> mLogs;

        private ProgressAdapter(List<String> logs) {
            mLogs = logs;
        }

        @Override
        public ProgressHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.item_progress_log, parent, false);
            return new ProgressHolder(view);
        }

        @Override
        public void onBindViewHolder(ProgressHolder holder, int position) {
            String log = mLogs.get(position);
            holder.bindGoal(log);
        }

        @Override
        public int getItemCount() {
            return mLogs.size();
        }
    }
}
