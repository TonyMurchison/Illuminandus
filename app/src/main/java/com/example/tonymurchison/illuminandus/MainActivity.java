package com.example.tonymurchison.illuminandus;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;


public class MainActivity extends AppCompatActivity {

    private ImageView startButton;
    private ImageView fadeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();


        startButton = (ImageView) findViewById(R.id.start_button);

    }

    public void startButtonClick(View v){
        //TODO create animation
        Intent intent = new Intent(MainActivity.this,LevelSelect.class);
        startActivity(intent);
        finish();

    }

    private void animateIn(ImageView image) {
        Animation fadein = new AlphaAnimation(0.f, 1.f);
        fadein.setDuration(500);
        final View viewToAnimate = image;
        fadein.setAnimationListener(new Animation.AnimationListener(){

            @Override
            public void onAnimationStart(Animation animation){}

            @Override
            public void onAnimationRepeat(Animation animation){}

            @Override
            public void onAnimationEnd(Animation animation){
                Intent intent = new Intent(MainActivity.this,LevelSelect.class);
                startActivity(intent);
            }
        });
        image.startAnimation(fadein);
    }

}
