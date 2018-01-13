package org.truthdefender.goalgetters.goals;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import org.truthdefender.goalgetters.R;
import org.truthdefender.goalgetters.model.TaskTree;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class CreateTaskFragment extends Fragment {
    private EditText taskName;
    private ImageButton upToParent;
    private Stack<TaskTree> taskTreeStack;
    private TaskTree rootTask;
    private RecyclerView mTaskRecyclerView;

    public CreateTaskFragment() {
        taskTreeStack = new Stack<>();
        rootTask = new TaskTree();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_create_task, container, false);

        taskName = v.findViewById(R.id.task_name);
        taskName.setText(R.string.project_name);

        upToParent = v.findViewById(R.id.up_to_parent);
        upToParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                taskTreeStack.pop();
                updateUI();
            }
        });
        upToParent.setVisibility(View.GONE);

        v.findViewById(R.id.check_complete).setVisibility(View.GONE);
        v.findViewById(R.id.delete_x).setVisibility(View.GONE);

        ImageButton plusChild = v.findViewById(R.id.plus_child);
        plusChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(taskTreeStack.isEmpty()) {
                    TaskTree child = new TaskTree();
                    rootTask.addChild(child);
                } else {
                    taskTreeStack.peek().addChild(new TaskTree());
                }
                updateUI();
            }
        });

        TextView childCount = v.findViewById(R.id.child_count);
        childCount.setText(getCount(rootTask));

        mTaskRecyclerView = v.findViewById(R.id.child_recycler);

        return v;
    }

    public String getCount(TaskTree curTask) {
        return curTask.getCompletedCount() + "/" + curTask.getChildCount();
    }

    public TaskTree getTaskTree() {
        return rootTask;
    }

    private void updateUI() {
        if(taskTreeStack.isEmpty()) {
            upToParent.setVisibility(View.GONE);
            taskName.setText(rootTask.getTask());
        } else {
            upToParent.setVisibility(View.VISIBLE);
            taskName.setText(taskTreeStack.peek().getTask());
        }
        List<TaskTree> taskChildren;
        if(taskTreeStack.isEmpty()) {
            taskChildren = new ArrayList<>(rootTask.getChildren());
        } else {
            taskChildren = new ArrayList<>(taskTreeStack.peek().getChildren());
        }
        TaskAdapter mTaskAdapter = new TaskAdapter(taskChildren);
        mTaskRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mTaskRecyclerView.setAdapter(mTaskAdapter);
    }

    private class TaskHolder extends RecyclerView.ViewHolder {
        private EditText mTaskName;
        private ImageButton mCheckComplete;
        private ImageButton mDeleteX;
        private ImageButton mPlusChild;
        private TextView mChildCount;


        private TaskTree mTask;

        private TaskHolder(View itemView) {
            super(itemView);
            mTaskName = itemView.findViewById(R.id.task_name);
            mCheckComplete = itemView.findViewById(R.id.check_complete);
            mCheckComplete.setVisibility(View.GONE);
            mDeleteX = itemView.findViewById(R.id.delete_x);
            mPlusChild = itemView.findViewById(R.id.plus_child);
            mChildCount = itemView.findViewById(R.id.child_count);
        }

        private void bindGoal(TaskTree task) {
            mTask = task;
            mTaskName.setText(mTask.getTask());
            mDeleteX.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    taskTreeStack.peek().deleteChild(mTask.getTask());
                    updateUI();
                }
            });
            mPlusChild.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    taskTreeStack.push(mTask);
                    updateUI();
                }
            });
            mChildCount.setText(getCount(mTask));
        }
    }

    private class TaskAdapter extends RecyclerView.Adapter<TaskHolder> {

        private List<TaskTree> mTasks;

        private TaskAdapter(List<TaskTree> tasks) {
            mTasks = tasks;
        }

        @Override
        public TaskHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.task_item, parent, false);
            return new TaskHolder(view);
        }

        @Override
        public void onBindViewHolder(TaskHolder holder, int position) {
            TaskTree task = mTasks.get(position);
            holder.bindGoal(task);
        }

        @Override
        public int getItemCount() {
            if(mTasks == null) {
                return 0;
            }
            return mTasks.size();
        }
    }
}
