package org.truthdefender.goalgetters.goals;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
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
    private TextView childCount;

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
        rootTask.setTask("Project Title");
        taskName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(taskTreeStack.isEmpty()) {
                    rootTask.setTask(s.toString());
                } else {
                    taskTreeStack.peek().setTask(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

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

        childCount = v.findViewById(R.id.child_count);
        childCount.setText(rootTask.getCountString());

        mTaskRecyclerView = v.findViewById(R.id.child_recycler);

        return v;
    }

    public TaskTree getTaskTree() {
        return rootTask;
    }

    private void updateUI() {
        if(taskTreeStack.isEmpty()) {
            upToParent.setVisibility(View.GONE);
            if(rootTask.getTask() == null || rootTask.getTask().equals("")) {
                taskName.setText(R.string.project_name);
                rootTask.setTask("Project Title");
            } else {
                taskName.setText(rootTask.getTask());
            }
        } else {
            upToParent.setVisibility(View.VISIBLE);
            TaskTree taskTree = taskTreeStack.peek();
            if(taskTree.getTask() == null || taskTree.getTask().equals("")) {
                taskName.setText(R.string.task_placeholder);
                taskTree.setTask("New Task");
            } else {
                taskName.setText(taskTree.getTask());
            }
        }
        List<TaskTree> taskChildren;
        if(taskTreeStack.isEmpty()) {
            taskChildren = new ArrayList<>(rootTask.getChildren());
            childCount.setText(rootTask.getCountString());
        } else {
            taskChildren = new ArrayList<>(taskTreeStack.peek().getChildren());
            childCount.setText(taskTreeStack.peek().getCountString());
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
        private ImageButton mDownToChild;
        private TextView mChildCount;


        private TaskTree mTask;

        private TaskHolder(View itemView) {
            super(itemView);
            mTaskName = itemView.findViewById(R.id.task_name);
            mDownToChild = itemView.findViewById(R.id.down_to_child);
            mCheckComplete = itemView.findViewById(R.id.check_complete);
            mCheckComplete.setVisibility(View.GONE);
            mDeleteX = itemView.findViewById(R.id.delete_x);
            mPlusChild = itemView.findViewById(R.id.plus_child);
            mPlusChild.setVisibility(View.GONE);
            mChildCount = itemView.findViewById(R.id.child_count);
        }

        private void bindGoal(final TaskTree task) {
            mTask = task;
            if(mTask.getTask() == null || mTask.getTask().equals("")) {
                mTaskName.setText(R.string.task_placeholder);
                task.setTask("New Task");
                mTask.setTask("New Task");
            } else {
                mTaskName.setText(mTask.getTask());
            }
            mTaskName.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    task.setTask(s.toString());
                    mTask.setTask(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            mDeleteX.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    taskTreeStack.peek().deleteChild(mTask.getTask());
                    updateUI();
                }
            });
            mDownToChild.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    taskTreeStack.push(mTask);
                    updateUI();
                }
            });
            mChildCount.setText(mTask.getCountString());
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
            View view = layoutInflater.inflate(R.layout.task_item_child, parent, false);
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
