package com.example.tonymurchison.illuminandus;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    /*
    private ImageView startButton;
    private ImageView fadeView;
    private ImageView background;
    private int screenWidth;
    private int screenHeight;
    private long timeAtStart=0;
    boolean notYetStarted = true;
*/

    //TODO zorgen dat muren niet uitsteken
    //TODO zorgen dat je de bewegingen kan calibreren naar een nieuw 0 punt
    //TODO rode bal minder heftig maken
    //TODO timing in hidden analyseren of het goed is
    //TODO pauze scherm en eind scherm mooi maken
    //TODO knoppen mooi maken
    //TODO tekst vergroten
    //TODO changing mazes zorgen dat de muren niet problemen geven met de bal
    //TODO visuele indicatie van de tijd die een powerup nog actief is
    //TODO level nummer laten zien in game



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(this, MainLevelSelect.class);
        startActivity(intent);
        finish();
    }


    /*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
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
            Intent intent = new Intent(MainActivity.this, MainLevelSelect.class);
            startActivity(intent);
            System.gc();
            finish();
        }

    }
    public void startButtonClick(View v){
        notYetStarted=false;
        Intent intent = new Intent(MainActivity.this,MainLevelSelect.class);
        startActivity(intent);
        System.gc();
        finish();
    }
    */
}