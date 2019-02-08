package com.example.tandemtask;

import java.util.ArrayList;
import java.util.List;

public class ListObj {
    private int id = -1;
    private String name = "";
    private String createdAt = "";
    private int isCompleted = 0;
    private int itemsCompleted = 0;
    private List items = new ArrayList();

    //GETTERS
    public final int getID() { return this.id; }
    public final String getName() { return this.name; }
    public final String getCreatedAt() { return this.createdAt; }
    public final int getCompleted() { return this.isCompleted; }
    public final List getItems() { return this.items; }
    public final int getItemsCompleted() { return this.itemsCompleted; }

    //SETTERS
    public final void setID(int var) { this.id = var; }
    public final void setName(String var) { this.name = var; }
    public final void setCreatedAt(String var) { this.createdAt = var; }
    public final void setCompleted(int var) { this.isCompleted = var; }
    public final void setItems(List var) { this.items = var; }
    public final void plusItemComplete() { ++this.itemsCompleted; }
    public final void setItemsCompletedTo0() { this.itemsCompleted = 0; }
}