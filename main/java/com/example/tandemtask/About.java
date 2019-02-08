package com.example.tandemtask;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.tandemtask.R.id;

public class About extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_about);
        this.setSupportActionBar((Toolbar)this.findViewById(id.about_toolbar));

        ActionBar ab = this.getSupportActionBar();
        if (ab != null) { ab.setDisplayHomeAsUpEnabled(true); }
        ab = this.getSupportActionBar();
        if (ab != null) { ab.setHomeButtonEnabled(true); }
        ab = this.getSupportActionBar();
        if (ab != null) { ab.setTitle("About"); }

        TextView tv = findViewById(id.about_tv4);
        tv.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home: //back button
                this.finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}