package com.example.tandemtask;

public class TaskObj {
    private int id = -1;
    private int listid = -1;
    private String itemName = "";
    private boolean isCompleted = false;

    //GETTERS
    public final int getID() { return this.id; }
    public final int getListID() { return this.listid; }
    public final String getName() { return this.itemName; }
    public final boolean getCompleted() { return this.isCompleted; }

    //SETTERS
    public final void setID(int var) { this.id = var; }
    public final void setListID(int var) { this.listid = var; }
    public final void setName(String var) { this.itemName = var; }
    public final void setCompleted(boolean var) { this.isCompleted = var; }
}