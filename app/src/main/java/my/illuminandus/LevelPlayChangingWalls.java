package my.illuminandus;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Rect;
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
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import java.util.Scanner;

public class LevelPlayChangingWalls extends AppCompatActivity implements SensorEventListener{


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

    private Wall mazeWall[] = new Wall[260];
    private boolean horizontalWalls[][] = new boolean[13][10];
    private boolean verticalWalls[][] = new boolean[14][9];
    private int wallNumber=0;

    int screenWidth;
    double speedAdjustment;
    double block;

    private float show = 1;
    private float hide = 0;

    private int wallAmount=0;

    private SystemUiHelper helper;

    private SensorEvent eventStorage;

    private double offsetMoveX=0;
    private double offsetMoveY=0;

    private int[] lastNumbers = {-1,-1,-1,-1,-1};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.level_changing_walls);
        getSupportActionBar().hide();
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        AdView mAdView = (AdView) findViewById(R.id.adView);
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
        Intent intent = new Intent(LevelPlayChangingWalls.this, MainLevelSelect.class);
        startActivity(intent);
        finish();
    }

    public void calibrateButtonClick(View v){
        if(eventStorage!=null) {
            double x = eventStorage.values[2];
            double y = eventStorage.values[1];

            offsetMoveX = x;
            offsetMoveY = y;

            SharedPreferences prefs = this.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("OffsetX", (int) x);
            editor.putInt("OffsetY", (int) y);

            editor.commit();
        }
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
        Random rand = new Random();
        levelNumber = rand.nextInt(60) + 1;
        String level = "Level "+Integer.toString(levelNumber);
        while(reading){
            if(level.equals(scanner.nextLine())){

                for(int i=0;i<13;i++){
                    String line = scanner.nextLine();
                    for(int j=0;j<10;j++){
                        horizontalWalls[i][j]=false;
                        if(Character.getNumericValue(line.charAt(j))==1){
                            horizontalWalls[i][j]=true;
                        }

                    }
                }

                scanner.nextLine();

                for(int i=0;i<14;i++){
                    String line = scanner.nextLine();
                    for(int j=0;j<9;j++){
                        verticalWalls[i][j]=false;
                        if(Character.getNumericValue(line.charAt(j))==1){
                            verticalWalls[i][j]=true;
                        }

                    }
                }


                ballPositionX=0;
                ballPositionY=0;

                exitPositionX=9;
                exitPositionY=13;

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
        AssetManager assetManager = getResources().getAssets();
        InputStream inputStream = null;

        try {
            inputStream = assetManager.open("LevelsChangingWall.txt");

        } catch (IOException e) {
            e.printStackTrace();
        }

        int randomLevel=-1;
        do {
            Random rand = new Random();
            randomLevel = rand.nextInt(60) + 1;
        }while(contains(lastNumbers,randomLevel));

        lastNumbers[4]=lastNumbers[3];
        lastNumbers[3]=lastNumbers[2];
        lastNumbers[2]=lastNumbers[1];
        lastNumbers[1]=lastNumbers[0];
        lastNumbers[0]=randomLevel;

        Scanner scanner = new Scanner(inputStream);
        boolean reading = true;
        String level = "Level "+Integer.toString(randomLevel);
        while(reading){
            if(level.equals(scanner.nextLine())){

                for(int i=0;i<13;i++){
                    String line = scanner.nextLine();
                    for(int j=0;j<10;j++){
                        horizontalWalls[i][j]=false;
                        if(Character.getNumericValue(line.charAt(j))==1){
                            horizontalWalls[i][j]=true;
                        }

                    }
                }

                scanner.nextLine();

                for(int i=0;i<14;i++){
                    String line = scanner.nextLine();
                    for(int j=0;j<9;j++){
                        verticalWalls[i][j]=false;
                        if(Character.getNumericValue(line.charAt(j))==1){
                            verticalWalls[i][j]=true;
                        }

                    }
                }


                ballPositionX=0;
                ballPositionY=0;

                exitPositionX=9;
                exitPositionY=13;

                reading=false;



            }
        }
        scanner.close();

        int tempWallNumber=0;
        for(int i = 0; i < 13; i++) {
            for (int j = 0; j < 10; j++) {
                if (horizontalWalls[i][j]) {
                    mazeWall[tempWallNumber].setHittable(true);
                    mazeWall[tempWallNumber].setVisibility(show);
                } else {
                    mazeWall[tempWallNumber].setHittable(false);
                    mazeWall[tempWallNumber].setVisibility(hide);
                }
                tempWallNumber=tempWallNumber+1;
            }
        }

        for(int i = 0; i < 14; i++) {
            for (int j = 0; j < 9; j++) {
                if (verticalWalls[i][j]) {
                    mazeWall[tempWallNumber].setHittable(true);
                    mazeWall[tempWallNumber].setVisibility(show);
                } else {
                    mazeWall[tempWallNumber].setHittable(false);
                    mazeWall[tempWallNumber].setVisibility(hide);
                }
                tempWallNumber=tempWallNumber+1;
            }
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
                if(mazeWall[i].getHittable()) {
                    intersectWall(playingBall, mazeWall[i]);
                }
            }

            if(exitPoint.getHittable()) {
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
        offset = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, r.getDisplayMetrics());

        /*
        if(metrics.densityDpi>400 && metrics.densityDpi <=720){
            offset = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, r.getDisplayMetrics());
        }
        else{

            offset = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 90, r.getDisplayMetrics());
        }

*/



        for (int i = 0; i < 13; i++) {
            for (int j = 0; j < 10; j++) {
                int id = getResources().getIdentifier("horizontal_wall_" + ((i*10) + (j)), "id", getPackageName());
                ImageView wallImage = (ImageView) findViewById(id);
                mazeWall[wallNumber] = new Wall(wallImage);
                mazeWall[wallNumber].setWidth((int)(6.9d * block));
                mazeWall[wallNumber].setHeight((int)(1d * block));
                mazeWall[wallNumber].setCenter((int) ((double) j * 5.9d * block + 3.45d *  block), (int) ((double) i * 5.9d *  block +  block * 29.8d-offset));
                mazeWall[wallNumber].setCorners();

                if (horizontalWalls[i][j]) {
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

        for (int i = 0; i < 14; i++) {
            for (int j = 0; j < 9; j++) {
                int id = getResources().getIdentifier("vertical_wall_" + ((i*9) + (j)), "id", getPackageName());
                ImageView wallImage = (ImageView) findViewById(id);
                mazeWall[wallNumber] = new Wall(wallImage);
                mazeWall[wallNumber].setWidth((int)(1d * block));
                mazeWall[wallNumber].setHeight((int)(6.85d * block)); //8.25
                mazeWall[wallNumber].setCenter((int) ((double) j * 5.9d *  block +  block * 6.4d), (int) ((double) i * 5.9d * block +  block * 26.85d-offset));
                mazeWall[wallNumber].setCorners();

                if (verticalWalls[i][j]) {
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
        mazeWall[wallNumber].setHeight((int)(82.6d * block));
        mazeWall[wallNumber].setCenter((int) ((0.5d) * block), (int) (65.2d * block-offset));
        mazeWall[wallNumber].setCorners();
        mazeWall[wallNumber].setHittable(true);
        wallNumber = wallNumber + 1;

        ImageView rightBorderWallImage = (ImageView) findViewById(R.id.rightBorderWall);
        mazeWall[wallNumber] = new Wall(rightBorderWallImage);
        mazeWall[wallNumber].setWidth((int)(1 * block));
        mazeWall[wallNumber].setHeight((int)(82.6d * block));
        mazeWall[wallNumber].setCenter((int) (59.5d * block), (int) (65.2d * block-offset));
        mazeWall[wallNumber].setCorners();
        mazeWall[wallNumber].setHittable(true);
        wallNumber = wallNumber + 1;

        ImageView topBorderWallImage = (ImageView) findViewById(R.id.topBorderWall);
        mazeWall[wallNumber] = new Wall(topBorderWallImage);
        mazeWall[wallNumber].setWidth((int)(60 * block));
        mazeWall[wallNumber].setHeight((int)(1 * block));
        mazeWall[wallNumber].setCenter((int) (30d *  block), (int) (23.9d * block-offset));
        mazeWall[wallNumber].setCorners();
        mazeWall[wallNumber].setHittable(true);
        wallNumber = wallNumber + 1;

        ImageView bottomBorderWallImage = (ImageView) findViewById(R.id.bottomBorderWall);
        mazeWall[wallNumber] = new Wall(bottomBorderWallImage);
        mazeWall[wallNumber].setWidth((int)(60 * block));
        mazeWall[wallNumber].setHeight((int)(1 * block));
        mazeWall[wallNumber].setCenter((int) (30d * block),(int)(106.5d *block-offset));
        mazeWall[wallNumber].setCorners();
        mazeWall[wallNumber].setHittable(true);
        wallNumber = wallNumber + 1;

        playingBall.setWidth((int) (2d * block));
        playingBall.setHeight((int) (2d * block));
        playingBall.setCenter((int) ((3.45d + ballPositionX * 5.9d) * block), (int) (((26.85d + ballPositionY * 5.9d) * block)-offset));
        playingBall.setCorners();

        exitPoint.setWidth((int) (2d * block));
        exitPoint.setHeight((int) (2d * block));
        exitPoint.setCenter((int) ((3.45d + exitPositionX * 5.9d) * block), (int) (((26.85d + exitPositionY * 5.9d) * block)-offset));
        exitPoint.setCorners();


        wallAmount = wallNumber-4;

    }



}
