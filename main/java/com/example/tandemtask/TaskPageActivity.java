package com.example.tandemtask;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.tandemtask.R.id;

import java.util.List;

public class TaskPageActivity extends AppCompatActivity {

    public Database db;
    private int listid;

    public final Database getDatabase() { return this.db; }
    public final void setDatabase(Database var) { this.db = var; }
    public final long getListID() { return this.listid; }
    public final void setListID(int var) { this.listid = var; }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_taskpage);
        this.setSupportActionBar((Toolbar)this.findViewById(id.taskpage_toolbar));

        ActionBar ab = this.getSupportActionBar();
        if (ab != null) { ab.setDisplayHomeAsUpEnabled(true); }
        ab = this.getSupportActionBar();
        if (ab != null) { ab.setHomeButtonEnabled(true); }
        ab = this.getSupportActionBar();
        if (ab != null) { ab.setTitle(this.getIntent().getStringExtra("ListName")); }

        this.listid = this.getIntent().getIntExtra("ListID", -1);
        this.db = new Database(this);
        RecyclerView rv = this.findViewById(id.taskpage_rv);
        rv.setLayoutManager((new LinearLayoutManager(this)));

        //OnClick bind for Floating Action Button (ADD)
        (this.findViewById(id.taskpage_fab)).setOnClickListener((new OnClickListener() {
            public final void onClick(View it) {
                Builder dialog = new Builder(TaskPageActivity.this);
                dialog.setTitle("Add New Task");
                View view = TaskPageActivity.this.getLayoutInflater().inflate(R.layout.action_bar, null);
                final EditText name = view.findViewById(id.ev_list);
                dialog.setView(view);
                dialog.setPositiveButton("Add", (new android.content.DialogInterface.OnClickListener() {
                    public final void onClick( DialogInterface $noName_0, int $noName_1) {
                        if (name.length() > 0) {
                            TaskObj task = new TaskObj();
                            task.setName(name.getText().toString());
                            task.setListID(TaskPageActivity.this.listid);
                            task.setCompleted(false);
                            TaskPageActivity.this.getDatabase().addTask(task);
                            TaskPageActivity.this.refreshView();
                        } else {
                            Toast.makeText(TaskPageActivity.this, "Please enter a task name.", Toast.LENGTH_LONG).show();
                        }
                    }
                }));
                dialog.setNegativeButton("Cancel", null);
                dialog.show();
            }
        }));
    }

    public final void updateTask(final TaskObj task) {
        Builder dialog = new Builder(this);
        dialog.setTitle("Rename Task");
        View view = this.getLayoutInflater().inflate(R.layout.action_bar, null);
        final EditText name = view.findViewById(id.ev_list);
        name.setText(task.getName());
        dialog.setView(view);
        dialog.setPositiveButton("Rename", (new android.content.DialogInterface.OnClickListener() {
            public final void onClick( DialogInterface $noName_0, int $noName_1) {
                if (name.length() > 0) {
                    task.setName(name.getText().toString());
                    task.setListID(TaskPageActivity.this.listid);
                    task.setCompleted(false);
                    TaskPageActivity.this.getDatabase().updateTask(task);
                    TaskPageActivity.this.refreshView();
                } else {
                    Toast.makeText(TaskPageActivity.this, "Please enter a task name.", Toast.LENGTH_LONG).show();
                }
            }
        }));
        dialog.setNegativeButton("Cancel", null);
        dialog.show();
    }

    protected void onResume() {
        this.refreshView();
        super.onResume();
    }

    private void refreshView() {
        RecyclerView rv = this.findViewById(id.taskpage_rv);
        Database database = this.db;
        TaskPageAdapter tpa = new TaskPageAdapter(this, database.getTasks(this.listid));
        rv.setAdapter(tpa);
    }

    public boolean onOptionsItemSelected(MenuItem item) { //on menu item selected
        switch(item.getItemId()) {
            case android.R.id.home: //back button
                this.finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //TaskPageAdapter class
    public static final class TaskPageAdapter extends Adapter<TaskPageActivity.TaskPageAdapter.ViewHolder> {
        private final TaskPageActivity activity;
        private final List list;

        public int getItemCount() { return this.list.size(); }
        public final TaskPageActivity getActivity() { return this.activity; }
        public final List getList() { return this.list; }

        public TaskPageActivity.TaskPageAdapter.ViewHolder onCreateViewHolder( ViewGroup p0, int p1) {
            View v = LayoutInflater.from(this.activity).inflate(R.layout.task_row, p0, false);
            return new TaskPageActivity.TaskPageAdapter.ViewHolder(v);
        }

        public void onBindViewHolder(final TaskPageActivity.TaskPageAdapter.ViewHolder holder, final int p1) {
            holder.getTaskName().setChecked(((TaskObj)this.list.get(p1)).getCompleted());
            holder.getTaskName().setText(((TaskObj)this.list.get(p1)).getName());

            checkTaskStatus(this.getActivity().getApplicationContext(), holder, p1);

            holder.getTaskName().setOnClickListener((new OnClickListener() {
                public final void onClick(View it) {
                    ((TaskObj)TaskPageAdapter.this.getList().get(p1)).setCompleted(!((TaskObj)TaskPageAdapter.this.getList().get(p1)).getCompleted());
                    TaskPageAdapter.this.getActivity().getDatabase().updateTask((TaskObj)TaskPageAdapter.this.getList().get(p1));
                    checkTaskStatus(getActivity().getApplicationContext(), holder, p1);
                }
            }));

            holder.getDelete().setOnClickListener((new OnClickListener() {
                public final void onClick(View it) {
                    Builder dialog = new Builder(TaskPageAdapter.this.getActivity());
                    dialog.setTitle("Confirm Action");
                    dialog.setMessage("Are you sure you want to delete this task?");
                    dialog.setPositiveButton("Yes", (new android.content.DialogInterface.OnClickListener() {
                        public final void onClick( DialogInterface $noName_0, int $noName_1) {
                            TaskPageAdapter.this.getActivity().getDatabase().deleteTask(((TaskObj)TaskPageAdapter.this.getList().get(p1)).getID());
                            TaskPageAdapter.this.getActivity().refreshView();
                        }
                    }));
                    dialog.setNegativeButton("No", null);
                    dialog.show();
                }
            }));

            holder.getEdit().setOnClickListener((new OnClickListener() {
                public final void onClick(View it) {
                    TaskPageAdapter.this.getActivity().updateTask((TaskObj)TaskPageAdapter.this.getList().get(p1));
                }
            }));
        }

        public void checkTaskStatus(Context context, final TaskPageActivity.TaskPageAdapter.ViewHolder holder, final int p1) { //check to see if task is completed, and if so, grey it out
            if (((TaskObj)TaskPageAdapter.this.getList().get(p1)).getCompleted()) {
                holder.getTaskName().setTextColor(ContextCompat.getColor(context, R.color.task_name_dulled));
            } else {
                holder.getTaskName().setTextColor(ContextCompat.getColor(context, R.color.task_name));
            }
        }

        public TaskPageAdapter(TaskPageActivity activity,  List list) { //constructor
            super();
            this.activity = activity;
            this.list = list;
        }

        //ViewHolder sub-class
        public static final class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
            private final CheckBox taskName;
            private final ImageView edit;
            private final ImageView delete;

            public final CheckBox getTaskName() { return this.taskName; }
            public final ImageView getEdit() { return this.edit; }
            public final ImageView getDelete() { return this.delete; }

            public ViewHolder(View v) { //constructor
                super(v);
                View tn = v.findViewById(id.cb_task);
                this.taskName = (CheckBox)tn;
                View eb = v.findViewById(id.iv_edit);
                this.edit = (ImageView)eb;
                View db = v.findViewById(id.iv_delete);
                this.delete = (ImageView)db;
            }
        }
    }

}