package com.example.tonymurchison.illuminandus;



import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.AssetManager;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;
import android.os.Handler;

public class LevelPlay2 extends AppCompatActivity implements SensorEventListener{
    int levelNumber;

    private Wall mazeWall[] = new Wall[241];
    private boolean horizontalWalls[][] = new boolean[7][9];
    private boolean verticalWalls[][] = new boolean[10][6];
    private int wallNumber=0;

    int screenWidth;
    double speedAdjustment;
    int block;

    private float show = 1;
    private float hide = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setup level
        /*
        Bundle extras = getIntent().getExtras();
        levelNumber = extras.getInt("levelNumber");
        */
        setContentView(R.layout.vertical_level);
        getSupportActionBar().hide();
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        for(int i=0;i<7;i++){
            for(int j=0;j<9;j++){
                horizontalWalls[i][j] = true;
/*
                if(i%2==0) {
                    horizontalWalls[i][j] = true;
                }
                else{
                    horizontalWalls[i][j] = false;
                }
*/
            }
        }

        for(int i=0;i<10;i++){
            for(int j=0;j<6;j++){
                verticalWalls[i][j] = true;
/*
                if(i%2==0) {
                    verticalWalls[i][j] = true;
                }
                else{
                    verticalWalls[i][j] = false;
                }
*/
            }
        }

        //set the things that are depended of the screen resolution
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        speedAdjustment=screenWidth/1920d;
        block=(int)Math.round(screenWidth / 90d);

        //run the method that initializes the layout
        start();

    }




    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1) {
        //Do nothing.
    }

    public void onSensorChanged (SensorEvent event){

        //if sensor is unreliable, return void
        if (event.accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE) {
            return;
        }
/*
        //read the data from the gyroscopes
        double xb=event.values[1]*speedAdjustment;
        double yb=event.values[2]*speedAdjustment;

        //max out the speed
        if (xb > 15d*speedAdjustment) {
            xb = 15d*speedAdjustment;
        }
        if (xb < -15d*speedAdjustment) {
            xb = -15d*speedAdjustment;
        }
        if (yb > 15d*speedAdjustment) {
            yb = 15d*speedAdjustment;
        }
        if (yb < -15d*speedAdjustment) {
            yb = -15d*speedAdjustment;
        }




        //determine the amount of steps in each direction to be taken for the next move
        x = (int)Math.round((xb/(1d*invert))*allowMovement);
        y =(int) Math.round((-yb/(1d*invert ))*allowMovement);

        //check if a wall should be set invisible, this happens when the "visibleThreshold" has been reached
        time = (int) (System.currentTimeMillis() - timeStart - pausedTime);
        for (int i = 0; i < wallNumber - 4; i++) {
            if (time - mazeWall[i].getTimeTouched() > visibleThreshold) {
                mazeWall[i].setVisibility(hide);
            }
        }

        //show the ball when it has been hidden for more than 3 seconds
        if(ballHidden==true && time>timePlayingBallHit+3000){
            playingBall.setVisibility(show);
            ballHidden=false;
        }

        //determine the current playing time in seconds and minutes
        timeMinutes = (time / (1000 * 60)) % 60;
        timeSeconds = ((time - (timeMinutes * 60 * 1000)) / 1000) % 60;

        //display current time in game
        if (timeMinutes < 10) {
            if (timeSeconds < 10) {
                timeText.setText("0" + Integer.toString(timeMinutes) + ":0" + Integer.toString(timeSeconds));
            } else {
                timeText.setText("0" + Integer.toString(timeMinutes) + ":" + Integer.toString(timeSeconds));
            }
        } else {
            if (timeSeconds < 10) {
                timeText.setText(Integer.toString(timeMinutes) + ":0" + Integer.toString(timeSeconds));
            } else {
                timeText.setText(Integer.toString(timeMinutes) + ":" + Integer.toString(timeSeconds));
            }
        }

        //if current time is above partime then set the text color to red
        if (time > (parTime + 1000)) {
            timeText.setTextColor(getResources().getColor(R.color.red));
        }
        */
    }

    public void start() {
        //TODO textview uitlijnen in spelscherm
        double offsetX = 3;
        double offsetY = 25;


        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 9; j++) {
                if (horizontalWalls[i][j] == true) {
                    int id = getResources().getIdentifier("horizontal_wall_" + ((i) + (j*7)), "id", getPackageName());
                    ImageView wallImage = (ImageView) findViewById(id);
                    mazeWall[wallNumber] = new Wall(wallImage);
                    mazeWall[wallNumber].setWidth((int)(1d * block));
                    mazeWall[wallNumber].setHeight((int)(8.25d * block));
                    //setCenter(hoogte, breedte) in portrait mode perspectief.
                    mazeWall[wallNumber].setCenter((int) ((double) j * 7.25d * (double) block + (1d + offsetY) * (double) block), (int) ((double) i * 7.25d * (double) block + (double) block * (0.9d + offsetX)));
                    mazeWall[wallNumber].setCorners();
                    mazeWall[wallNumber].setVisibility(show);
                    wallNumber = wallNumber + 1;
                }
            }
        }

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 6; j++) {
                if (verticalWalls[i][j] == true) {
                    int id = getResources().getIdentifier("vertical_wall_" + ((i*6) + (j)), "id", getPackageName());
                    ImageView wallImage = (ImageView) findViewById(id);
                    mazeWall[wallNumber] = new Wall(wallImage);
                    mazeWall[wallNumber].setWidth((int)(8.25d * block));
                    mazeWall[wallNumber].setHeight((int)(1d * block));
                    mazeWall[wallNumber].setCenter((int) ((double) i * 7.25d * (double) block + (double) block * (-2.62d + offsetY)), (int) ((double) j * 7.25d * (double) block + ((double) block * (offsetX + 4.5d))));
                    mazeWall[wallNumber].setCorners();
                    mazeWall[wallNumber].setVisibility(show);
                    wallNumber = wallNumber + 1;
                }
            }
        }
/*
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 13; j++) {
                if (powerUpsPlacement[i][j] > 0) {

                    ImageView imagePowerUp = new ImageView(LevelPlay.this);

                    if (powerUpsPlacement[i][j] == 1) {
                        imagePowerUp.setImageResource(R.drawable.pickup_yellow_v2);
                    }
                    if (powerUpsPlacement[i][j] == 2) {
                        imagePowerUp.setImageResource(R.drawable.pickup_pink_v2);
                    }
                    if (powerUpsPlacement[i][j] == 3) {
                        imagePowerUp.setImageResource(R.drawable.pickup_green_v2);
                    }
                    if (powerUpsPlacement[i][j] == 4) {
                        imagePowerUp.setImageResource(R.drawable.pickup_red_v2);
                    }
                    if (powerUpsPlacement[i][j] == 5) {
                        imagePowerUp.setImageResource(R.drawable.pickup_blue_v2);
                    }
                    if (powerUpsPlacement[i][j] == 6) {
                        imagePowerUp.setImageResource(R.drawable.pickup_multi_v2);
                    }

                    RelativeLayout rl = (RelativeLayout) findViewById(R.id.relativeLayout);
                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT);
                    rl.addView(imagePowerUp, lp);

                    powerUps[powerUpCounter] = new PowerUp(powerUpsPlacement[i][j], imagePowerUp);
                    powerUps[powerUpCounter].setWidth((int) (2d * block));
                    powerUps[powerUpCounter].setHeight((int) (2d * block));
                    powerUps[powerUpCounter].setCenter((int) ((double) j * 5d * (double) block + (4d + offsetX) * (double) block), (int) ((double) i * 5d * (double) block + (double) block * (3.5d + offsetY)));
                    powerUps[powerUpCounter].setCorners();
                    powerUpCounter = powerUpCounter + 1;
                }
            }
        }
*/

        ImageView leftBorderWallImage = (ImageView) findViewById(R.id.leftBorderWall);
        mazeWall[wallNumber] = new Wall(leftBorderWallImage);
        mazeWall[wallNumber].setWidth(72 * block);
        mazeWall[wallNumber].setHeight(1 * block);
        mazeWall[wallNumber].setCenter((int) ((54.75d) * (double) block), (int) ((51d) * (double) block));
        mazeWall[wallNumber].setCorners();
        wallNumber = wallNumber + 1;

        ImageView rightBorderWallImage = (ImageView) findViewById(R.id.rightBorderWall);
        mazeWall[wallNumber] = new Wall(rightBorderWallImage);
        mazeWall[wallNumber].setWidth(72 * block);
        mazeWall[wallNumber].setHeight(1 * block);
        mazeWall[wallNumber].setCenter((int) ((54.75d) * (double) block), (int) ((0.5d) * (double) block));
        mazeWall[wallNumber].setCorners();
        wallNumber = wallNumber + 1;

        ImageView topBorderWallImage = (ImageView) findViewById(R.id.topBorderWall);
        mazeWall[wallNumber] = new Wall(topBorderWallImage);
        mazeWall[wallNumber].setWidth(1 * block);
        mazeWall[wallNumber].setHeight(52 * block);
        mazeWall[wallNumber].setCenter((int) ((18.5d) * (double) block), (int) ((26d) * (double) block));
        mazeWall[wallNumber].setCorners();
        wallNumber = wallNumber + 1;

        ImageView bottomBorderWallImage = (ImageView) findViewById(R.id.bottomBorderWall);
        mazeWall[wallNumber] = new Wall(bottomBorderWallImage);
        mazeWall[wallNumber].setWidth(1 * block);
        mazeWall[wallNumber].setHeight(52 * block);
        mazeWall[wallNumber].setCenter((int) ((91d) * (double) block), (int) ((26d) * (double) block));
        mazeWall[wallNumber].setCorners();
        wallNumber = wallNumber + 1;


/*
        playingBall.setWidth((int) (2d * block));
        playingBall.setHeight((int) (2d * block));
        playingBall.setCenter((int) ((4d + offsetX + ballPositionX * 5) * block), (int) ((offsetY + 3.5d + ballPositionY * 5) * block));
        playingBall.setCorners();

        timeText.setWidth(30 * block);
        timeText.setHeight(10 * block);
        timeText.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float) (4d * block));
        RelativeLayout.LayoutParams paramsTimeText = (RelativeLayout.LayoutParams) timeText.getLayoutParams();
        paramsTimeText.leftMargin = 15 * block;
        paramsTimeText.topMargin = 46 * block;
        timeText.setLayoutParams(paramsTimeText);

        parTimeText.setWidth(30 * block);
        parTimeText.setHeight(10 * block);
        parTimeText.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float) (4d * block));
        RelativeLayout.LayoutParams paramsParTimeText = (RelativeLayout.LayoutParams) parTimeText.getLayoutParams();
        paramsParTimeText.leftMargin = 45 * block;
        paramsParTimeText.topMargin = 46 * block;
        parTimeText.setLayoutParams(paramsParTimeText);

        levelText.setWidth(35 * block);
        levelText.setHeight(30 * block);
        levelText.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float) (12d * block));
        RelativeLayout.LayoutParams paramsLevelText = (RelativeLayout.LayoutParams) levelText.getLayoutParams();
        paramsLevelText.leftMargin = 60 * block;
        paramsLevelText.topMargin = 37 * block;
        levelText.setLayoutParams(paramsLevelText);

        powerUpsTouched.setWidth(35 * block);
        powerUpsTouched.setHeight(30 * block);
        powerUpsTouched.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float) (4d * block));
        RelativeLayout.LayoutParams paramsPowerUpsTouched = (RelativeLayout.LayoutParams) powerUpsTouched.getLayoutParams();
        paramsPowerUpsTouched.leftMargin = (int) (37d * block);
        paramsPowerUpsTouched.topMargin = 40 * block;
        powerUpsTouched.setLayoutParams(paramsPowerUpsTouched);
        powerUpsTouched.setText(Integer.toString(amountPowerUpsTouched) + "    /    " + Integer.toString(powerUpCounter));

        pauseScreenBackground2.requestLayout();
        pauseScreenBackground2.getLayoutParams().width = 4 * (16 * block);
        pauseScreenBackground2.getLayoutParams().height = 4 * (9 * block);
        RelativeLayout.LayoutParams paramsBackground2 = (RelativeLayout.LayoutParams) pauseScreenBackground2.getLayoutParams();
        paramsBackground2.leftMargin = (int) ((5d + 7d) * block);
        paramsBackground2.topMargin = (int) ((5d + 3d) * block);
        pauseScreenBackground2.setLayoutParams(paramsBackground2);

        playButton.requestLayout();
        playButton.getLayoutParams().width = (int) (3.1d * (9 * block));
        playButton.getLayoutParams().height = (int) (3.1d * (9 * block));
        RelativeLayout.LayoutParams paramsPlayButton = (RelativeLayout.LayoutParams) playButton.getLayoutParams();
        paramsPlayButton.leftMargin = (int) ((8.35d + 7d) * block);
        paramsPlayButton.topMargin = (int) ((8.65d + 3d) * block);
        playButton.setLayoutParams(paramsPlayButton);

        quitLevelButton.requestLayout();
        quitLevelButton.getLayoutParams().width = (int) (5 * 4.65d * block);
        quitLevelButton.getLayoutParams().height = 5 * block;
        RelativeLayout.LayoutParams paramsQuitLevel = (RelativeLayout.LayoutParams) quitLevelButton.getLayoutParams();
        paramsQuitLevel.leftMargin = (int) (48d * block);
        paramsQuitLevel.topMargin = 28 * block;
        quitLevelButton.setLayoutParams(paramsQuitLevel);

        restartLevelButton.requestLayout();
        restartLevelButton.getLayoutParams().width = (int) (5 * 4.65d * block);
        restartLevelButton.getLayoutParams().height = 5 * block;
        RelativeLayout.LayoutParams paramsRestartLevel = (RelativeLayout.LayoutParams) restartLevelButton.getLayoutParams();
        paramsRestartLevel.leftMargin = (int) (42d * block);
        paramsRestartLevel.topMargin = 35 * block;
        restartLevelButton.setLayoutParams(paramsRestartLevel);

        pauseScreenTimeText.setWidth(35 * block);
        pauseScreenTimeText.setHeight(30 * block);
        pauseScreenTimeText.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float) (5d * block));
        RelativeLayout.LayoutParams paramsPauseTimeText = (RelativeLayout.LayoutParams) pauseScreenTimeText.getLayoutParams();
        paramsPauseTimeText.leftMargin = 45 * block;
        paramsPauseTimeText.topMargin = 20 * block;
        pauseScreenTimeText.setLayoutParams(paramsPauseTimeText);

        nextLevelButton.requestLayout();
        nextLevelButton.getLayoutParams().width = (int) (3.1d * (9 * block));
        nextLevelButton.getLayoutParams().height = (int) (3.1d * (9 * block));
        RelativeLayout.LayoutParams paramsNextLevelButton = (RelativeLayout.LayoutParams) nextLevelButton.getLayoutParams();
        paramsNextLevelButton.leftMargin = (int) ((8.35d + 7d) * block);
        paramsNextLevelButton.topMargin = (int) ((8.65d + 3d) * block);
        nextLevelButton.setLayoutParams(paramsNextLevelButton);

        finishedScreenBackground.requestLayout();
        finishedScreenBackground.getLayoutParams().width = 4 * (16 * block);
        finishedScreenBackground.getLayoutParams().height = 4 * (9 * block);
        RelativeLayout.LayoutParams paramsFinished = (RelativeLayout.LayoutParams) finishedScreenBackground.getLayoutParams();
        paramsFinished.leftMargin = (int) ((5d + 7d) * block);
        paramsFinished.topMargin = (int) ((5d + 3d) * block);
        finishedScreenBackground.setLayoutParams(paramsFinished);

        touchAllowed = true;
        */
    }

}
