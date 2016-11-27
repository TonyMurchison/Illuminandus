package com.example.tonymurchison.illuminandus;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import java.util.Scanner;

public class LevelPlayChangingWalls extends AppCompatActivity implements SensorEventListener{
    private AdView mAdView;

    //game items
    private Ball playingBall;
    private int ballPositionX;
    private int ballPositionY;

    private Exit exitPoint;
    private int exitPositionX;
    private int exitPositionY;

    //remaining items
    private SensorManager sManager;
    private int x = 0;          //stores the angle of the phone around the x-axis
    private int y = 0;          //stores the angle of the phone around the y-axis
    private boolean allowedMovement[] = {true, true, true, true};
    private int levelNumber=0;  //level 1 equals levelNumber 0 etc
    private double allowMovement =1;
    private Handler h;
    private Handler handlerChangingWalls;
    private int delay = 20;
    private int delayHandlerChangingWalls = 2000;

    private Wall mazeWall[] = new Wall[241];
    private boolean horizontalWalls[][] = new boolean[9][7];
    private boolean verticalWalls[][] = new boolean[10][6];
    private int wallNumber=0;

    int screenWidth;
    double speedAdjustment;
    double block;

    private float show = 1;
    private float hide = 0;

    private boolean started=false;
    private int wallAmount=0;

    private SystemUiHelper helper;

    private SensorEvent eventStorage;

    private double offsetMoveX=0;
    private double offsetMoveY=0;

    private TextView levelTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Bundle extras = getIntent().getExtras();
        levelNumber = extras.getInt("levelNumber");

        setContentView(R.layout.level_changing_walls);
        getSupportActionBar().hide();
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        //set ball
        ImageView ballImage = (ImageView)findViewById(R.id.ball);
        playingBall = new Ball(ballImage);

        ImageView exitImage = (ImageView)findViewById(R.id.exit);
        exitPoint = new Exit(exitImage);

        //set the things that are depended of the screen resolution
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        speedAdjustment=screenWidth/1000d;
        block=screenWidth / 60d;

        readFile();


        //run the method that initializes the layout
        start();
        started=true;

        levelTextView = (TextView)findViewById(R.id.levelTextView);
        levelTextView.setText("Level "+Integer.toString(levelNumber+1));

        SharedPreferences prefs = this.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        offsetMoveX = (double) prefs.getInt("OffsetX", 0);
        offsetMoveY = (double) prefs.getInt("OffsetY", 0);


        helper = new SystemUiHelper(
                this,
                3,   // Choose from one of the levels
                0);  // There are additional flags, usually this will be 0


    }



    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        switch (event.getAction()){
            case MotionEvent.ACTION_UP:
                if (helper.isShowing()){
                    helper.hide();
                }else{
                    helper.show();
                }
                break;
        }


        return super.onTouchEvent(event);

    }



    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent intent = new Intent(LevelPlayChangingWalls.this, LevelSelectChanging.class);
        startActivity(intent);
        finish();
    }

    public void calibrateButtonClick(View v){
        double x = eventStorage.values[2];
        double y = eventStorage.values[1];

        offsetMoveX = x;
        offsetMoveY = y;

        SharedPreferences prefs = this.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("OffsetX", (int)x);
        editor.putInt("OffsetY", (int)y);

        editor.commit();
    }


    void readFile(){
        AssetManager assetManager = getResources().getAssets();
        InputStream inputStream = null;

        try {
            inputStream = assetManager.open("LevelsChangingWall.txt");

        } catch (IOException e) {
            e.printStackTrace();
        }

        Scanner scanner = new Scanner(inputStream);
        boolean reading = true;
        String level = "Level "+Integer.toString(levelNumber+1);
        while(reading==true){
            if(level.equals(scanner.nextLine())){

                for(int i=0;i<9;i++){
                    String line = scanner.nextLine();
                    for(int j=0;j<7;j++){
                        if(Character.getNumericValue(line.charAt(j))==1){
                            horizontalWalls[i][j]=true;
                        }
                        else{
                            horizontalWalls[i][j]=false;
                        }
                    }
                }

                scanner.nextLine();

                for(int i=0;i<10;i++){
                    String line = scanner.nextLine();
                    for(int j=0;j<6;j++){
                        if(Character.getNumericValue(line.charAt(5-j))==1){
                            verticalWalls[i][j]=true;
                        }
                        else{
                            verticalWalls[i][j]=false;
                        }
                    }
                }

                scanner.nextLine();

                String line = scanner.nextLine();
                ballPositionX=Integer.parseInt(line)-1;
                line = scanner.nextLine();
                ballPositionY=Integer.parseInt(line)-1;

                scanner.nextLine();

                line = scanner.nextLine();
                exitPositionX=Integer.parseInt(line)-1;
                line = scanner.nextLine();
                exitPositionY=Integer.parseInt(line)-1;

                reading=false;



            }
        }
        scanner.close();
    }

    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1) {
        //Do nothing.
    }

    public void onSensorChanged (SensorEvent event){

        eventStorage = event;

        //if sensor is unreliable, return void
        if (event.accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE) {
            return;
        }

        //read the data from the gyroscopes
        double xb=(event.values[2]-offsetMoveX) * speedAdjustment;
        double yb=(event.values[1]-offsetMoveY) * speedAdjustment*-1;

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
        x = (int)Math.round((xb/(1d))*allowMovement);
        y =(int) Math.round((-yb/(1d))*allowMovement);
    }

    //restart level
    public void restartButtonClick(View v){
        super.recreate();
    }

    //go back to the level select screen
    public void quitLevelClick(View v){
        Intent intent = new Intent(LevelPlayChangingWalls.this, LevelSelectChanging.class);
        startActivity(intent);
        finish();
    }

    public void mainMenuButtonClicked(View v){
        Intent intent = new Intent(LevelPlayChangingWalls.this, MainLevelSelect.class);
        startActivity(intent);
        finish();
    }


    @Override
    protected void onResume() {
        super.onResume();
        sManager.registerListener(this, sManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_FASTEST);

        helper.hide();
    }

    @Override
    protected void onStart(){
        super.onStart();


        sManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        //set a handler that every 20ms calls the move method.
        h = new Handler();
        h.postDelayed(new Runnable() {
            public void run() {
                move(x, y);
                if (h!=null)
                    h.postDelayed(this, delay);
            }
        }, delay);

        handlerChangingWalls = new Handler();
        handlerChangingWalls.postDelayed(new Runnable() {
            public void run() {
                changeWalls();
                if (handlerChangingWalls!=null)
                    handlerChangingWalls.postDelayed(this, delayHandlerChangingWalls);
            }
        }, delayHandlerChangingWalls);


    }

    public boolean contains(int[] array, int x){
        for(int i=0;i<array.length;i++){
            if(array[i]==x){
                return true;
            }
        }
        return false;
    }

    public void changeWalls(){
            Random rand = new Random();

            int wallsToHide[] = new int[20];
            int wallsToShow[] = new int[20];
            int numbersUsed[] = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};

            int i = 0;
            int j = 0;

            while (i < 20) {
                int randomNumber = rand.nextInt(wallAmount);
                if (mazeWall[randomNumber].getVisility()==show && contains(numbersUsed, randomNumber) == false) {
                    wallsToHide[i] = randomNumber;
                    numbersUsed[i] = randomNumber;
                    i = i + 1;
                }

            }

            while (j < 20) {
                int randomNumber = rand.nextInt(wallAmount);
                if (mazeWall[randomNumber].getVisility()==hide && contains(numbersUsed, randomNumber) == false) {
                    wallsToShow[j] = randomNumber;
                    numbersUsed[j + 20] = randomNumber;
                    j = j + 1;
                }
            }

            for (int x = 0; x < 20; x++) {
                mazeWall[wallsToHide[x]].setHittable(false);
                mazeWall[wallsToHide[x]].setVisibility(hide);
            }

            for (int x = 0; x < 20; x++) {
                mazeWall[wallsToShow[x]].setHittable(true);
                mazeWall[wallsToShow[x]].setVisibility(show);
            }

    }

    //When this Activity isn't visible anymore
    @Override
    protected void onStop() {
        //unregister the sensor listener
        sManager.unregisterListener(this);
        h = null;
        super.onStop();
    }



    //this method is called when the last powerup has been picked up
    private void finishedLevel(){


        Intent intent = new Intent(LevelPlayChangingWalls.this, FinishedScreen.class);
        intent.putExtra("levelNumber", levelNumber);
        intent.putExtra("GameType","changing");
        startActivity(intent);
        finish();


    }



    //this method will move the ball
    public void move(int x, int y) {
        RelativeLayout.LayoutParams alp = playingBall.getLayoutParams();
        int maxMovementX = Math.abs(x);
        int maxMovenentY = Math.abs(y);
        int stepsTakenX = 0;
        int stepsTakenY = 0;
        int a = alp.leftMargin;
        int b = alp.topMargin;



        //while steps have to be taken:
        while (maxMovementX > stepsTakenX || maxMovenentY > stepsTakenY) {
            //check all the walls
            for (int i = 0; i < wallNumber; i++) {
                if(mazeWall[i].getHittable()==true) {
                    intersectWall(playingBall, mazeWall[i]);
                }
            }

            if(exitPoint.getHittable()==true) {
                intersectExit(playingBall, exitPoint);
            }

            //this will run if there are steps left to be done in the x direction
            if (stepsTakenX < maxMovementX) {
                stepsTakenX = stepsTakenX + 1;
                if (x > 0 && allowedMovement[3]) {//right
                    playingBall.setCenterX(playingBall.getCenterX() - 1);
                    playingBall.setCorners();
                    a = a - 1;
                }
                if (x < 0 && allowedMovement[2]) {//left
                    playingBall.setCenterX(playingBall.getCenterX() + 1);
                    playingBall.setCorners();
                    a = a + 1;
                }
            }
            //this will run if there are steps left to be done in the y direction
            if (stepsTakenY < maxMovenentY) {
                stepsTakenY = stepsTakenY + 1;
                if (y > 0 && allowedMovement[1]) {//down
                    playingBall.setCenterY(playingBall.getCenterY() - 1);
                    playingBall.setCorners();
                    b = b - 1;
                }
                if (y < 0 && allowedMovement[0]) {//up
                    playingBall.setCenterY(playingBall.getCenterY() + 1);
                    playingBall.setCorners();
                    b = b + 1;
                }
            }
        }

        for (int i = 0; i < 4; i++) {
            allowedMovement[i] = true;
        }

        alp.leftMargin = a;
        alp.topMargin = b;
        playingBall.setLayoutParams(alp);   //render layout
    }

    private void hitExit(){
        exitPoint.setVisibility(hide);
        exitPoint.setHittable(false);

        finishedLevel();
    }

    private void intersectExit(Ball ball, Exit exit){
        //top left corner of the ball
        if (ball.getTopLeftX() >= exit.getTopLeftX() && ball.getTopLeftX() <= exit.getTopRightX()) {              //is the x position of the ball between those of the two sides of the power up
            if (ball.getTopLeftY() >= exit.getTopLeftY() && ball.getTopLeftY() <= exit.getBottomLeftY()) {        //is the y position of the ball between those of the two sides of the power up
                hitExit();
                return;
            }
        }

        //top rigth corner of the ball
        if (ball.getTopRightX() >= exit.getTopLeftX() && ball.getTopRightX() <= exit.getTopRightX()) {            //is the x position of the ball between those of the two sides of the power up
            if (ball.getTopRightY() >= exit.getTopLeftY() && ball.getTopRightY() <= exit.getBottomLeftY()) {      //is the y position of the ball between those of the two sides of the power up
                hitExit();
                return;
            }
        }

        //bottom left corner of the ball
        if (ball.getBottomLeftX() >= exit.getBottomLeftX() && ball.getBottomLeftX() <= exit.getBottomRightX()) {  //is the x position of the ball between those of the two sides of the power up
            if (ball.getBottomLeftY() >= exit.getTopLeftY() && ball.getBottomLeftY() <= exit.getBottomLeftY()) {  //is the y position of the ball between those of the two sides of the power up
                hitExit();
                return;
            }
        }

        //bottom rigth corner of the ball
        if (ball.getBottomRightX() >= exit.getBottomLeftX() && ball.getBottomRightX() <= exit.getBottomRightX()) {//is the x position of the ball between those of the two sides of the power up
            if (ball.getBottomRightY() >= exit.getTopLeftY() && ball.getBottomRightY() <= exit.getBottomLeftY()) {//is the y position of the ball between those of the two sides of the power up
                hitExit();
            }
        }
    }

    //checks if a certain wall and the playing ball intersect
    public void intersectWall(Ball ball, Wall wall) {
        //top left corner of the ball
        if (ball.getTopLeftX() >= wall.getTopLeftX() && ball.getTopLeftX() <= wall.getTopRightX()) {                     //is the x position of the ball between those of the two sides of the wall
            if (ball.getTopLeftY() >= wall.getTopLeftY() && ball.getTopLeftY() <= wall.getBottomLeftY()) {               //is the y position of the ball between those of the two sides of the wall
                limitMovement(ball, wall);
                return;
            }
        }

        if(wall.getTopLeftX() >= ball.getTopLeftX() && wall.getTopLeftX() <= ball.getTopRightX() ){
            if (wall.getTopLeftY() >= ball.getTopLeftY() && wall.getTopLeftY() <= ball.getBottomLeftY()) {
                limitMovement(ball, wall);
                return;
            }
        }

        //top rigth corner of the ball
        if (ball.getTopRightX() >= wall.getTopLeftX() && ball.getTopRightX() <= wall.getTopRightX()) {                   //is the x position of the ball between those of the two sides of the wall
            if (ball.getTopRightY() >= wall.getTopLeftY() && ball.getTopRightY() <= wall.getBottomLeftY()) {             //is the y position of the ball between those of the two sides of the wall
                limitMovement(ball, wall);
                return;
            }
        }

        if (wall.getTopRightX() >= ball.getTopLeftX() && wall.getTopRightX() <= ball.getTopRightX()) {
            if (wall.getTopRightY() >= ball.getTopLeftY() && wall.getTopRightY() <= ball.getBottomLeftY()) {
                limitMovement(ball, wall);
                return;
            }
        }

        //bottom left corner of the ball
        if (ball.getBottomLeftX() >= wall.getBottomLeftX() && ball.getBottomLeftX() <= wall.getBottomRightX()) {        //is the x position of the ball between those of the two sides of the wall
            if (ball.getBottomLeftY() >= wall.getTopLeftY() && ball.getBottomLeftY() <= wall.getBottomLeftY()) {        //is the y position of the ball between those of the two sides of the wall
                limitMovement(ball, wall);
                return;
            }
        }

        if (wall.getBottomLeftX() >= ball.getBottomLeftX() && wall.getBottomLeftX() <= ball.getBottomRightX()) {        //is the x position of the ball between those of the two sides of the wall
            if (wall.getBottomLeftY() >= ball.getTopLeftY() && wall.getBottomLeftY() <= ball.getBottomLeftY()) {        //is the y position of the ball between those of the two sides of the wall
                limitMovement(ball, wall);
                return;
            }
        }

        //bottom rigth corner of the ball
        if (ball.getBottomRightX() >= wall.getBottomLeftX() && ball.getBottomRightX() <= wall.getBottomRightX()) {      //is the x position of the ball between those of the two sides of the wall
            if (ball.getBottomRightY() >= wall.getTopLeftY() && ball.getBottomRightY() <= wall.getBottomLeftY()) {      //is the y position of the ball between those of the two sides of the wall
                limitMovement(ball, wall);
                return;
            }
        }

        if (wall.getBottomRightX() >= ball.getBottomLeftX() && wall.getBottomRightX() <= ball.getBottomRightX()) {      //is the x position of the ball between those of the two sides of the wall
            if (wall.getBottomRightY() >= ball.getTopLeftY() && wall.getBottomRightY() <= ball.getBottomLeftY()) {      //is the y position of the ball between those of the two sides of the wall
                limitMovement(ball, wall);
            }
        }
    }

    //if a wall is hit this method will be called. It decides which side of the wall is hit and therefor decides in which direction the movement of the ball should be stopped.
    public void limitMovement(Ball ball, Wall wall) {
        //this is calculated with the minkowski sum method: (https://en.wikipedia.org/wiki/Minkowski_addition)
        float wy = (ball.getWidth() + wall.getWidth()) * (ball.getCenterY() - wall.getCenterY());
        float hx = (ball.getHeight() + wall.getHeight()) * (ball.getCenterX() - wall.getCenterX());

        if (wy > hx) {
            if (wy > -hx) {//top
                allowedMovement[1] = false;     //block downwards motion
            } else {//left
                allowedMovement[2] = false;     //block rightwards motion
            }
        } else {
            if (wy > -hx) {//right
                allowedMovement[3] = false;     //block leftwards motion
            } else {//bottom
                allowedMovement[0] = false;     //block upwards motion
            }
        }
    }

    public void start() {
        Resources r = getResources();
        double offset;
        DisplayMetrics metrics = getResources().getDisplayMetrics();

        if(metrics.densityDpi>400 && metrics.densityDpi <=720){
            offset = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, r.getDisplayMetrics());
        }
        else{

            offset = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 90, r.getDisplayMetrics());
        }





        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 7; j++) {
                int id = getResources().getIdentifier("horizontal_wall_" + ((i*7) + (j)), "id", getPackageName());
                ImageView wallImage = (ImageView) findViewById(id);
                mazeWall[wallNumber] = new Wall(wallImage);
                mazeWall[wallNumber].setWidth((int)(9.428d * block));
                mazeWall[wallNumber].setHeight((int)(1d * block));
                mazeWall[wallNumber].setCenter((int) ((double) j * 8.428d * block + 4.714d *  block), (int) ((double) i * 8.428d *  block +  block * 30.504d-offset));
                mazeWall[wallNumber].setCorners();

                if (horizontalWalls[i][j] == true) {
                    mazeWall[wallNumber].setHittable(true);
                    mazeWall[wallNumber].setVisibility(show);
                }
                else{
                    mazeWall[wallNumber].setHittable(false);
                    mazeWall[wallNumber].setVisibility(hide);
                }
                wallNumber = wallNumber + 1;
            }
        }

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 6; j++) {
                int id = getResources().getIdentifier("vertical_wall_" + ((i*6) + (j)), "id", getPackageName());
                ImageView wallImage = (ImageView) findViewById(id);
                mazeWall[wallNumber] = new Wall(wallImage);
                mazeWall[wallNumber].setWidth((int)(1d * block));
                mazeWall[wallNumber].setHeight((int)(9.428d * block)); //8.25
                mazeWall[wallNumber].setCenter((int) ((double) j * 8.428d *  block +  block * 8.928d), (int) ((double) i * 8.428d * block +  block * 26.29d-offset));
                mazeWall[wallNumber].setCorners();

                if (verticalWalls[i][j] == true) {
                    mazeWall[wallNumber].setHittable(true);
                    mazeWall[wallNumber].setVisibility(show);
                }
                else{
                    mazeWall[wallNumber].setHittable(false);
                    mazeWall[wallNumber].setVisibility(hide);
                }
                wallNumber = wallNumber + 1;
            }
        }

        ImageView leftBorderWallImage = (ImageView) findViewById(R.id.leftBorderWall);
        mazeWall[wallNumber] = new Wall(leftBorderWallImage);
        mazeWall[wallNumber].setWidth((int)(1 * block));
        mazeWall[wallNumber].setHeight((int)(84 * block));
        mazeWall[wallNumber].setCenter((int) ((0.5d) * block), (int) (64.123d * block-offset));
        mazeWall[wallNumber].setCorners();
        mazeWall[wallNumber].setHittable(true);
        wallNumber = wallNumber + 1;

        ImageView rightBorderWallImage = (ImageView) findViewById(R.id.rightBorderWall);
        mazeWall[wallNumber] = new Wall(rightBorderWallImage);
        mazeWall[wallNumber].setWidth((int)(1 * block));
        mazeWall[wallNumber].setHeight((int)(84 * block));
        mazeWall[wallNumber].setCenter((int) (59.5d * block), (int) (64.123d * block-offset));
        mazeWall[wallNumber].setCorners();
        mazeWall[wallNumber].setHittable(true);
        wallNumber = wallNumber + 1;

        ImageView topBorderWallImage = (ImageView) findViewById(R.id.topBorderWall);
        mazeWall[wallNumber] = new Wall(topBorderWallImage);
        mazeWall[wallNumber].setWidth((int)(60 * block));
        mazeWall[wallNumber].setHeight((int)(1 * block));
        mazeWall[wallNumber].setCenter((int) (30d *  block), (int) (22.076d * block-offset));
        mazeWall[wallNumber].setCorners();
        mazeWall[wallNumber].setHittable(true);
        wallNumber = wallNumber + 1;

        ImageView bottomBorderWallImage = (ImageView) findViewById(R.id.bottomBorderWall);
        mazeWall[wallNumber] = new Wall(bottomBorderWallImage);
        mazeWall[wallNumber].setWidth((int)(60 * block));
        mazeWall[wallNumber].setHeight((int)(1 * block));
        mazeWall[wallNumber].setCenter((int) (30d * block),(int)(106.17d *block-offset));
        mazeWall[wallNumber].setCorners();
        mazeWall[wallNumber].setHittable(true);
        wallNumber = wallNumber + 1;

        playingBall.setWidth((int) (2d * block));
        playingBall.setHeight((int) (2d * block));
        playingBall.setCenter((int) ((4.714d + ballPositionX * 8.428) * block), (int) (((26.29d + ballPositionY * 8.428) * block)-offset));
        playingBall.setCorners();

        exitPoint.setWidth((int) (2d * block));
        exitPoint.setHeight((int) (2d * block));
        exitPoint.setCenter((int) ((4.714d + exitPositionX * 8.428) * block), (int) (((26.29d + exitPositionY * 8.428) * block)-offset));
        exitPoint.setCorners();


        wallAmount = wallNumber-4;

    }



}
