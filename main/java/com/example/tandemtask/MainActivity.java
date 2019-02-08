package com.example.tandemtask;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;

import com.example.tandemtask.R.id;

public final class MainActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);
        this.findViewById(id.ic_logo).startAnimation(AnimationUtils.loadAnimation(this, R.anim.splash_in));

        (new Handler()).postDelayed((new Runnable() {
            public final void run() {
                MainActivity.this.findViewById(id.ic_logo).startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.splash_out));

                (new Handler()).postDelayed((new Runnable() {
                    public final void run() {
                        MainActivity.this.findViewById(id.ic_logo).setVisibility(View.GONE);
                        MainActivity.this.startActivity(new Intent(MainActivity.this, ListPageActivity.class)); //transfer to ListPageActivity
                        MainActivity.this.finish(); //end this activity
                    }
                }), 500L);
            }
        }), 1000L);
    }

}