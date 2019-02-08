package com.example.tandemtask;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Paint;

import java.util.ArrayList;
import java.util.List;

public final class Database extends SQLiteOpenHelper {

    private static final String DB_NAME = "TandemTaskDB";
    private static final int DB_VERSION = 1;
    private static final boolean DB_NOTIFICATIONS = true;
    private final Context context;

    public Database(Context context) { //constructor
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    public final Context getContext() { return this.context; }

    public void onCreate(SQLiteDatabase db) {
        String createListTable = "CREATE TABLE Lists (id integer PRIMARY KEY AUTOINCREMENT, createdAt datetime DEFAULT CURRENT_TIMESTAMP, name varchar, isCompleted integer);";
        String createTaskTable = "CREATE TABLE Tasks (id integer PRIMARY KEY AUTOINCREMENT, createdAt datetime DEFAULT CURRENT_TIMESTAMP, listid integer, itemName varchar, isCompleted integer);";
        db.execSQL(createListTable);
        db.execSQL(createTaskTable);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }

    public final void addList(ListObj list) { //add new list
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", list.getName());
        cv.put("isCompleted", list.getCompleted());
        db.insert("Lists", null, cv);
    }

    public final void updateList(ListObj list) { //update list
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", list.getName());
        cv.put("isCompleted", list.getCompleted());
        db.update("Lists", cv, "id=?", new String[]{String.valueOf(list.getID())});
    }

    public final void deleteList(int listID) { //delete list
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("Tasks", "listid=?", new String[]{String.valueOf(listID)});
        db.delete("Lists", "id=?", new String[]{String.valueOf(listID)});
    }

    public final List getLists() {
        List result = new ArrayList();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor queryResult = db.rawQuery("SELECT * FROM Lists;", null);

        if (queryResult.moveToFirst()) {
            do {
                ListObj list = new ListObj();
                list.setID(queryResult.getInt(queryResult.getColumnIndex("id")));
                String listname = queryResult.getString(queryResult.getColumnIndex("name"));
                list.setName(listname);
                list.setCompleted(queryResult.getInt(queryResult.getColumnIndex("isCompleted")));
                result.add(list);
            } while(queryResult.moveToNext());
        }

        queryResult.close();
        return result;
    }

    public final void updateListStatus(int id, boolean isCompleted) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor queryResult = db.rawQuery("SELECT * FROM Lists WHERE id=" + id, null); //need join here probably...

        if (queryResult.moveToFirst()) {
            do {
                TaskObj task = new TaskObj();
                task.setID(queryResult.getInt(queryResult.getColumnIndex("id")));
                task.setListID(queryResult.getInt(queryResult.getColumnIndex("listid")));
                String taskname = queryResult.getString(queryResult.getColumnIndex("itemName"));
                task.setName(taskname);
                task.setCompleted(isCompleted);
                this.updateTask(task);
            } while(queryResult.moveToNext());
        }

        queryResult.close();
    }



    public final void addTask(TaskObj task) { //add new task
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("itemName", task.getName());
        cv.put("listid", task.getListID());
        cv.put("isCompleted", task.getCompleted());
        db.insert("Tasks", null, cv);
    }

    public final void updateTask(TaskObj task) { //update task
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("itemName", task.getName());
        cv.put("listid", task.getListID());
        cv.put("isCompleted", task.getCompleted());
        db.update("Tasks", cv, "id=?", new String[]{String.valueOf(task.getID())});
        db.close();
    }

    public final void deleteTask(int taskID) { //delete task
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("Tasks", "id=?", new String[]{String.valueOf(taskID)});
    }

    public final List getTasks(int id) {
        List result = new ArrayList();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor queryResult = db.rawQuery("SELECT * FROM Tasks WHERE listid=" + id, null);

        if (queryResult.moveToFirst()) {
            do {
                TaskObj task = new TaskObj();
                task.setID(queryResult.getInt(queryResult.getColumnIndex("id")));
                task.setListID(queryResult.getInt(queryResult.getColumnIndex("listid")));
                String taskname = queryResult.getString(queryResult.getColumnIndex("itemName"));
                task.setName(taskname);
                task.setCompleted(queryResult.getInt(queryResult.getColumnIndex("isCompleted")) == 1);
                result.add(task);
            } while(queryResult.moveToNext());
        }

        queryResult.close();
        return result;
    }

    public final void updateTaskStatus(int id, boolean isCompleted) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor queryResult = db.rawQuery("SELECT * FROM Tasks WHERE listid=" + id, null);

        if (queryResult.moveToFirst()) {
            do {
                TaskObj task = new TaskObj();
                task.setID(queryResult.getInt(queryResult.getColumnIndex("id")));
                task.setListID(queryResult.getInt(queryResult.getColumnIndex("listid")));
                String taskname = queryResult.getString(queryResult.getColumnIndex("itemName"));
                task.setName(taskname);
                task.setCompleted(isCompleted);
                this.updateTask(task);
            } while(queryResult.moveToNext());
        }

        queryResult.close();
    }

}