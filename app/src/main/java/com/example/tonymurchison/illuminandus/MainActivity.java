package com.example.tonymurchison.illuminandus;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.media.Image;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private ImageView startButton;
    private ImageView fadeView;
    private ImageView background;
    private int screenWidth;
    private int screenHeight;
    private long timeAtStart=0;
    boolean notYetStarted = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        startButton = (ImageView) findViewById(R.id.start_button);
        timeAtStart = System.currentTimeMillis();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                autoStart();
            }
        },3000);
    }

    public void autoStart() {
        //User was too busy contemplating life. The lovely developers helped him out a little
        if(notYetStarted==true) {
            Intent intent = new Intent(MainActivity.this, LevelSelect.class);
            startActivity(intent);
            finish();
        }
    }
    public void startButtonClick(View v){
        notYetStarted=false;
        Intent intent = new Intent(MainActivity.this,LevelSelect.class);
        startActivity(intent);
        finish();
    }
}