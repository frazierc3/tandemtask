package com.example.tandemtask;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.Toast;

import com.example.tandemtask.R.id;

import java.util.List;

public final class ListPageActivity extends AppCompatActivity {

    public Database db;

    public final Database getDatabase() { return this.db; }
    public final void setDatabase(Database var) { this.db = var; }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_listpage);
        this.setSupportActionBar((Toolbar)this.findViewById(id.listpage_toolbar));

        ActionBar ab = this.getSupportActionBar();
        if (ab != null) { ab.setTitle("   Your Lists"); }
        if (ab != null) { ab.setLogo(R.drawable.ic_tblogo); }

        this.db = new Database(this);
        RecyclerView rv = this.findViewById(id.listpage_rv);
        rv.setLayoutManager(new LinearLayoutManager(this));

        //OnClick bind for Floating Action Button (ADD)
        (this.findViewById(id.listpage_fab)).setOnClickListener((new OnClickListener() {
            public final void onClick(View it) {
                Builder dialog = new Builder(ListPageActivity.this);
                dialog.setTitle("Add New List");
                View view = ListPageActivity.this.getLayoutInflater().inflate(R.layout.action_bar, null);
                final EditText name = view.findViewById(id.ev_list);
                dialog.setView(view);
                dialog.setPositiveButton("Add", new android.content.DialogInterface.OnClickListener() {
                    public final void onClick(DialogInterface $noName_0, int $noName_1) {
                        if (name.length() > 0) {
                            ListObj list = new ListObj(); //create new list
                            list.setName(name.getText().toString()); //set list's name to value entered
                            ListPageActivity.this.getDatabase().addList(list); //call add list
                            ListPageActivity.this.refreshView(); //call refresh
                        } else {
                            Toast.makeText(ListPageActivity.this, "Please enter a list name.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                dialog.setNegativeButton("Cancel", null);
                dialog.show();
            }
        }));
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.listpage_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case id.listpage_menu_remind:
                BuildNotification();
                break;
            case id.listpage_menu_about:
                Intent intent = new Intent(ListPageActivity.this, About.class);
                ListPageActivity.this.startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public final void updateList(final ListObj list) {
        Builder dialog = new Builder(this);
        dialog.setTitle("Rename List");
        View view = this.getLayoutInflater().inflate(R.layout.action_bar, null);
        final EditText name = view.findViewById(id.ev_list);
        name.setText(list.getName());
        dialog.setView(view);
        dialog.setPositiveButton("Rename", (new android.content.DialogInterface.OnClickListener() {
            public final void onClick(DialogInterface $noName_0, int $noName_1) {
                if (name.length() > 0) {
                    list.setName(name.getText().toString()); //set list's name to value entered
                    ListPageActivity.this.getDatabase().updateList(list); //call update list
                    ListPageActivity.this.refreshView(); //call refresh
                } else {
                    Toast.makeText(ListPageActivity.this, "Please enter a list name.", Toast.LENGTH_LONG).show();
                }
            }
        }));
        dialog.setNegativeButton("Cancel", null);
        dialog.show();
    }

    public final void BuildNotification() {
        NotificationManager notificationManager = (NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = null;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel notificationChannel = new NotificationChannel("ID", "Name", importance);
            notificationManager.createNotificationChannel(notificationChannel);
            builder = new NotificationCompat.Builder(this.getApplicationContext(), notificationChannel.getId());
        } else {
            builder = new NotificationCompat.Builder(this.getApplicationContext());
        }

        builder = builder
                .setSmallIcon(R.drawable.ic_logo)
                .setColor(ContextCompat.getColor(this, R.color.fab))
                .setContentTitle("TandemTask")
                .setContentText("Friendly reminder to check your tasks.")
                .setTicker("Reminder set!")
                .setWhen(System.currentTimeMillis())
                .setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(true);

        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        /*Uri alarmSound = RingtoneManager.getActualDefaultRingtoneUri(this, RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(alarmSound);*/
        notificationManager.notify(1, builder.build());
    }

    protected void onResume() {
        this.refreshView();
        super.onResume();
    }

    private void refreshView() {
        RecyclerView rv = this.findViewById(id.listpage_rv);
        Database database = this.db;
        ListPageAdapter lpa = new ListPageAdapter(this, database.getLists());
        rv.setAdapter(lpa);
    }

    //ListPageAdapter class
    public static final class ListPageAdapter extends Adapter<ListPageActivity.ListPageAdapter.ViewHolder> {
        private final ListPageActivity activity;
        private final List list;

        public int getItemCount() { return this.list.size(); }
        public final ListPageActivity getActivity() { return this.activity; }
        public final List getList() { return this.list; }

        public ListPageActivity.ListPageAdapter.ViewHolder onCreateViewHolder(ViewGroup p0, int p1) {
            View v = LayoutInflater.from(this.activity).inflate(R.layout.list_row, p0, false);
            return new ListPageActivity.ListPageAdapter.ViewHolder(v);
        }

        public void onBindViewHolder(final ListPageActivity.ListPageAdapter.ViewHolder holder, final int p1) {
            holder.getListName().setText(((ListObj)this.list.get(p1)).getName());

            /*if (this.list.size() > 0) {
                checkListStatus(this.getActivity().getApplicationContext(), holder, p1);
            }*/

            holder.getListName().setOnClickListener((new OnClickListener() {
                public final void onClick(View it) { //transfer to task list activity
                    Intent intent = new Intent(ListPageAdapter.this.getActivity(), TaskPageActivity.class);
                    intent.putExtra("ListID", ((ListObj)ListPageAdapter.this.getList().get(p1)).getID());
                    intent.putExtra("ListName", ((ListObj)ListPageAdapter.this.getList().get(p1)).getName());
                    ListPageAdapter.this.getActivity().startActivity(intent);
                }
            }));

            holder.getMenu().setOnClickListener((new OnClickListener() {
                public final void onClick(View it) {
                    PopupMenu popup = new PopupMenu(ListPageAdapter.this.getActivity(), holder.getMenu());
                    popup.inflate(R.menu.menu);
                    popup.setOnMenuItemClickListener((new OnMenuItemClickListener() {
                            public final boolean onMenuItemClick(MenuItem it) {
                                switch(it.getItemId()) {
                                    case id.menu_checkAll:
                                        ListPageAdapter.this.getActivity().getDatabase().updateTaskStatus(((ListObj)ListPageAdapter.this.getList().get(p1)).getID(), true);
                                        break;
                                    case id.menu_uncheckAll:
                                        ListPageAdapter.this.getActivity().getDatabase().updateTaskStatus(((ListObj)ListPageAdapter.this.getList().get(p1)).getID(), false);
                                        break;
                                    case id.menu_edit:
                                        ListPageAdapter.this.getActivity().updateList((ListObj)ListPageAdapter.this.getList().get(p1));
                                        break;
                                    case id.menu_delete:
                                        Builder dialog = new Builder(ListPageAdapter.this.getActivity());
                                        dialog.setTitle("Confirm Action");
                                        dialog.setMessage("Are you sure you want to delete this list?");
                                        dialog.setPositiveButton("Yes", (new android.content.DialogInterface.OnClickListener() {
                                            public final void onClick(DialogInterface $noName_0, int $noName_1) {
                                                ListPageAdapter.this.getActivity().getDatabase().deleteList(((ListObj)ListPageAdapter.this.getList().get(p1)).getID());
                                                ListPageAdapter.this.getActivity().refreshView();
                                            }
                                        }));
                                        dialog.setNegativeButton("No", null);
                                        dialog.show();
                                }
                                return true;
                            }
                        }));
                        popup.show();
                    }
                }));
        }

        /*public void checkListStatus(Context context, final ListPageActivity.ListPageAdapter.ViewHolder holder, final int p1) { //check to see if list is completed, and if so, grey it out
            ((ListObj)this.list.get(p1)).setItems(this.list);
            ((ListObj)this.list.get(p1)).setItemsCompletedTo0(); //reset itemsCompleted to 0

            for (int i = 0; i < ((ListObj)this.list.get(p1)).getItems().size(); i++) { //loop through lists
                TaskObj task = (TaskObj)(((ListObj)this.list.get(p1)).getItems().get(i)); //get task from list at index i

                if (task.getCompleted()) { //if a task is completed
                    ((ListObj)this.list.get(p1)).plusItemComplete();
                }
            }

            if (((ListObj)this.list.get(p1)).getItemsCompleted() == ((ListObj)this.list.get(p1)).getItems().size()) { //if itemsCompleted = amount of tasks in list
                ((ListObj)this.list.get(p1)).setCompleted(1);
            } else {
                ((ListObj)this.list.get(p1)).setCompleted(0);
            }

            if (((ListObj)this.list.get(p1)).getCompleted() == 1) { //if list is completed
                holder.getListName().setTextColor(ContextCompat.getColor(context, R.color.list_name_dulled));
            } else {
                holder.getListName().setTextColor(ContextCompat.getColor(context, R.color.list_name));
            }
        }*/

        public ListPageAdapter(ListPageActivity activity, List list) { //constructor
            super();
            this.activity = activity;
            this.list = list;
        }

        //ViewHolder sub-class
        public static final class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
            private TextView listName;
            private ImageView menu;

            public final TextView getListName() { return this.listName; }
            public final ImageView getMenu() { return this.menu; }

            public ViewHolder(View v) { //constructor
                super(v);
                View ln = v.findViewById(id.tv_list);
                this.listName = (TextView)ln;
                View m = v.findViewById(id.iv_menu);
                this.menu = (ImageView)m;
            }
        }
    }

}