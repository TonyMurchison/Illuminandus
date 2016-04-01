package com.example.tonymurchison.illuminandus;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.AssetManager;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class LevelPlay extends AppCompatActivity implements SensorEventListener {
    boolean displayedFinishScreen= false;

    private SensorManager sManager;
    private int a;      //stores x position for the layout of the ball
    private int b;      //stores y position for the layout of the ball
    int x = 0;          //stores the angle of the phone around the x-axis
    int y = 0;          //stores the angle of the phone around the y-axis



    //ball storage info
    Ball playingBall;
    boolean allowedMovement[] = {true, true, true, true};

    //power up storage info
    PowerUp powerUps[] = new PowerUp[15];

    //wall storage info
    Wall mazeWall[] = new Wall[137];

    //opacity settings
    float show = 1;
    float hide = 0;

    int timeStart;
    int time;
    int timeMinutes;
    int timeSeconds;
    double speedAdjustment;
    TextView timeText;
    TextView powerUpsTouched;
    TextView parTimeText;
    TextView levelText;

    int visibleThreshold=10000;
    int invert=1;
    int pickup_range=500;
    int amountPowerUpsTouched=0;
    int parTimeMinutes;
    int parTimeSeconds;
    int pausedTime=0;
    int timeAtPause=0;
    int levelNumber=0;  //level 1 equals levelNumber 0 etc

    int allowMovement =1;

    ImageView pauseScreenBackground;
    ImageView pauseScreenBackground2;
    ImageView playButton;
    TextView pauseScreenTimeText;
    ImageView quitLevelButton;
    ImageView restartLevelButton;

    TextView partimeFinishedScreen;
    TextView timeFinishedScreen;
    ImageView nextLevelButton;
    ImageView finishedScreenBackground;
    Button homeButton;

    int powerUpsPlacement[][]= new int[6][13];
    int powerUpCounter=0;

    boolean horizontalWalls[][] = new boolean[5][13];

    boolean verticalWalls[][] = new boolean[12][6];

    int ballPosition=0;

    int parTime=50000;

    int wallNumber=0;
    int screenWidth;
    int block;
    boolean touchAllowed = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        levelNumber = extras.getInt("levelNumber");

        setContentView(R.layout.level);
        //hide top bar
        getSupportActionBar().hide();

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        sManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        ImageView ballImage = (ImageView)findViewById(R.id.ball);
        playingBall = new Ball(ballImage);

        parTimeText = (TextView) findViewById(R.id.parTimeText);
        levelText = (TextView) findViewById(R.id.levelText);
        timeText = (TextView) findViewById(R.id.timeText);
        powerUpsTouched = (TextView) findViewById(R.id.powerUpsTouched);

        int parTimeArray [] = getResources().getIntArray(R.array.parTime);
        parTime = parTimeArray[levelNumber];
        parTimeMinutes=(parTime/(1000*60))%60;
        parTimeSeconds=((parTime-(parTimeMinutes*60*1000))/1000)%60;
        parTimeText.setText("  /  0"+Integer.toString(parTimeMinutes)+":"+Integer.toString(parTimeSeconds));

        levelText.setText(Integer.toString(levelNumber+1));

        timeStart=(int)System.currentTimeMillis();

        pauseScreenBackground = (ImageView) findViewById(R.id.menu_background);
        pauseScreenBackground2 = (ImageView) findViewById(R.id.menu_background_2);
        playButton = (ImageView) findViewById(R.id.playButton);
        quitLevelButton = (ImageView) findViewById(R.id.quitLevelButton);
        restartLevelButton = (ImageView) findViewById(R.id.restartButton);
        pauseScreenTimeText = (TextView) findViewById(R.id.timePause);


        readFile();

        finishedScreenBackground = (ImageView) findViewById(R.id.finishedScreenBackground);
        nextLevelButton = (ImageView) findViewById(R.id.nextLevelButton);


/*
        partimeFinishedScreen = (TextView) findViewById(R.id.partime_finished_screen);
        timeFinishedScreen = (TextView) findViewById(R.id.time_finished_screen);
        nextLevelButton = (ImageView) findViewById(R.id.next_level_button);
        finishedScreenBackground = (ImageView) findViewById(R.id.finished_background);
        homeButton = (Button) findViewById(R.id.home);
*/
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        speedAdjustment=screenWidth/1920;
        block=(int)Math.round(screenWidth/90d);

        start();
    }


    void readFile(){
        AssetManager assetManager = getResources().getAssets();
        InputStream inputStream = null;

        try {
            inputStream = assetManager.open("levelsFile.txt");

        } catch (IOException e) {
            e.printStackTrace();
        }

        Scanner scanner = new Scanner(inputStream);
        boolean reading = true;
        String level = "Level "+Integer.toString(levelNumber+1);
        while(reading==true){
            if(level.equals(scanner.nextLine())){
                for(int i=0;i<6;i++){
                    String line = scanner.nextLine();
                    for(int j=0;j<13;j++){
                        powerUpsPlacement[i][j]=Character.getNumericValue(line.charAt(j));
                    }
                }

                scanner.nextLine();

                for(int i=0;i<5;i++){
                    String line = scanner.nextLine();
                    for(int j=0;j<13;j++){
                        if(Character.getNumericValue(line.charAt(j))==1){
                            horizontalWalls[i][j]=true;
                        }
                        else{
                            horizontalWalls[i][j]=false;
                        }
                    }
                }

                scanner.nextLine();

                for(int i=0;i<12;i++){
                    String line = scanner.nextLine();
                    for(int j=0;j<6;j++){
                        if(Character.getNumericValue(line.charAt(j))==1){
                            verticalWalls[i][j]=true;
                        }
                        else{
                            verticalWalls[i][j]=false;
                        }
                    }
                }

                scanner.nextLine();

                String line = scanner.nextLine();
                ballPosition=Integer.parseInt(line);
                reading=false;

            }
        }


    }

    @Override
    public void onSensorChanged (SensorEvent event){

        //if sensor is unreliable, return void
        if (event.accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE) {
            return;
        }

        //adjusts and then saves the angle of phone in the x and y direction.
        x = Math.round((event.values[1]) / (int) (3f * invert * speedAdjustment)*allowMovement);
        y = Math.round((-event.values[2]) / (int) (3f * invert * speedAdjustment)*allowMovement);

        //limits the speed
        if (x > 15) {
            x = 15;
        }
        if (x < -15) {
            x = -15;
        }
        if (y > 15) {
            y = 15;
        }
        if (y < -15) {
            y = -15;
        }


        time = (int) (System.currentTimeMillis() - timeStart - pausedTime);
        for (int i = 0; i < wallNumber - 4; i++) {
            if (time - mazeWall[i].getTimeTouched() > visibleThreshold) {
                //mazeWall[i].setVisibility(hide);
            }
        }

        timeMinutes = (time / (1000 * 60)) % 60;
        timeSeconds = ((time - (timeMinutes * 60 * 1000)) / 1000) % 60;

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

        if (time > (parTime + 1000)) {
            timeText.setTextColor(getResources().getColor(R.color.red));
        }

        move(x, y);

    }

    public void restartButtonClick(View v){
        super.recreate();
    }

    public void quitLevelClick(View v){
        Intent intent = new Intent(LevelPlay.this, LevelSelect.class);
        intent.putExtra("levelNumber", levelNumber);
        startActivity(intent);
    }

    public void playButtonClick(View v){
        pauseScreenBackground.setVisibility(View.GONE);
        pauseScreenBackground2.setVisibility(View.GONE);
        playButton.setVisibility(View.GONE);
        quitLevelButton.setVisibility(View.GONE);
        restartLevelButton.setVisibility(View.GONE);
        pauseScreenTimeText.setVisibility(View.GONE);

        playButton.setClickable(false);
        quitLevelButton.setClickable(false);
        restartLevelButton.setClickable(false);
        
        allowMovement =1;
        pausedTime=pausedTime+((int)System.currentTimeMillis()-timeAtPause);

        for(int i=0;i<powerUpCounter;i++){
            if(powerUps[i].getHittable()==true) {
                powerUps[i].setVisible();
            }
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(touchAllowed==true) {
            pauseScreen();
        }
        return super.onTouchEvent(event);
    }

    //when this Activity starts
    @Override
    protected void onResume() {
        super.onResume();
        /*register the sensor listener to listen to the gyroscope sensor, use the
        callbacks defined in this class, and gather the sensor information as quick
        as possible*/
        sManager.registerListener(this, sManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_FASTEST);

    }

    //When this Activity isn't visible anymore
    @Override
    protected void onStop() {
        //unregister the sensor listener
        sManager.unregisterListener(this);
        super.onStop();
    }

    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1) {
        //Do nothing.
    }

    /*
        This method is called when a wall has been hit.
        It will then analyze from which way the ball came in relation to the wall it hit.
        With that information it stores which movements are now still allowed (up, down, right or left).
        This is done via the Minkowski addition formula: https://en.wikipedia.org/wiki/Minkowski_addition

        This method receives a ball and a wall entity.
        It returns nothing

        It changes the allowedMovement boolean
    */

    public void pauseScreen(){
        timeAtPause=(int) System.currentTimeMillis();
        allowMovement =0;

        for (int i=0;i<powerUpCounter;i++){
            powerUps[i].setInvisible();
        }

        pauseScreenBackground.setVisibility(View.VISIBLE);
        pauseScreenBackground2.setVisibility(View.VISIBLE);
        playButton.setVisibility(View.VISIBLE);
        quitLevelButton.setVisibility(View.VISIBLE);
        restartLevelButton.setVisibility(View.VISIBLE);

        playButton.setClickable(true);
        quitLevelButton.setClickable(true);
        restartLevelButton.setClickable(true);

        if(time>parTime){
            pauseScreenTimeText.setTextColor(getResources().getColor(R.color.red));
        }

        pauseScreenTimeText.setVisibility(View.VISIBLE);
        if (timeMinutes < 10) {
            if (timeSeconds < 10) {
                pauseScreenTimeText.setText("0" + Integer.toString(timeMinutes) + ":0" + Integer.toString(timeSeconds) + "/0" + Integer.toString(parTimeMinutes) + ":" + Integer.toString(parTimeSeconds));
            } else {
                pauseScreenTimeText.setText("0" + Integer.toString(timeMinutes) + ":" + Integer.toString(timeSeconds) + "/0" + Integer.toString(parTimeMinutes) + ":" + Integer.toString(parTimeSeconds));
            }
        } else {
            if (timeSeconds < 10) {
                pauseScreenTimeText.setText(Integer.toString(timeMinutes) + ":0" + Integer.toString(timeSeconds)+"/0"+Integer.toString(parTimeMinutes)+":"+Integer.toString(parTimeSeconds));
            } else {
                pauseScreenTimeText.setText(Integer.toString(timeMinutes) + ":" + Integer.toString(timeSeconds) + "/0" + Integer.toString(parTimeMinutes) + ":" + Integer.toString(parTimeSeconds));
            }
        }



    }

    public void limitMovement(Ball ball, Wall wall) {
        float wy = (ball.getWidth() + wall.getWidth()) * (ball.getCenterY() - wall.getCenterY());
        float hx = (ball.getHeight() + wall.getHeight()) * (ball.getCenterX() - wall.getCenterX());

        wall.setVisibility(show);
        wall.setTimeTouched((int) System.currentTimeMillis()-timeStart);

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

    /*
        This methods determines if a wall is hit by the ball.
        If this is the case then it will make that wall visible and it will call the limitMovement() method to determine which movement should be blocked.

        This method receives a ball and wall entity.
        It returns nothing

        It changes opacity of the wall that is hit
    */

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

    /*
        This methods determines if a power up is hit by the ball.
        If this is the case then it will call the hitPowerUp() method, which determines what should happen.

        This method receives a ball and powerUp entity.
        It returns nothing

        It changes nothing.
     */
    public void intersectPowerUp(Ball ball, PowerUp powerUp) {
        //top left corner of the ball
        if (ball.getTopLeftX() >= powerUp.getTopLeftX() && ball.getTopLeftX() <= powerUp.getTopRightX()) {              //is the x position of the ball between those of the two sides of the power up
            if (ball.getTopLeftY() >= powerUp.getTopLeftY() && ball.getTopLeftY() <= powerUp.getBottomLeftY()) {        //is the y position of the ball between those of the two sides of the power up
                hitPowerUp(powerUp);
                return;
            }
        }

        //top rigth corner of the ball
        if (ball.getTopRightX() >= powerUp.getTopLeftX() && ball.getTopRightX() <= powerUp.getTopRightX()) {            //is the x position of the ball between those of the two sides of the power up
            if (ball.getTopRightY() >= powerUp.getTopLeftY() && ball.getTopRightY() <= powerUp.getBottomLeftY()) {      //is the y position of the ball between those of the two sides of the power up
                hitPowerUp(powerUp);
                return;
            }
        }

        //bottom left corner of the ball
        if (ball.getBottomLeftX() >= powerUp.getBottomLeftX() && ball.getBottomLeftX() <= powerUp.getBottomRightX()) {  //is the x position of the ball between those of the two sides of the power up
            if (ball.getBottomLeftY() >= powerUp.getTopLeftY() && ball.getBottomLeftY() <= powerUp.getBottomLeftY()) {  //is the y position of the ball between those of the two sides of the power up
                hitPowerUp(powerUp);
                return;
            }
        }

        //bottom rigth corner of the ball
        if (ball.getBottomRightX() >= powerUp.getBottomLeftX() && ball.getBottomRightX() <= powerUp.getBottomRightX()) {//is the x position of the ball between those of the two sides of the power up
            if (ball.getBottomRightY() >= powerUp.getTopLeftY() && ball.getBottomRightY() <= powerUp.getBottomLeftY()) {//is the y position of the ball between those of the two sides of the power up
                hitPowerUp(powerUp);
            }
        }
    }

    /*
        This method determines what should happen if a power up is hit

        It receives a powerUp entity
        It returns nothing

        It changes the opacity of the powerUp
     */
    public void hitPowerUp(PowerUp powerUp){
        if(powerUp.getHittable()) {
            amountPowerUpsTouched = amountPowerUpsTouched + 1;
            powerUpsTouched.setText(Integer.toString(amountPowerUpsTouched) + "  /  " + Integer.toString(powerUpCounter));
        }

        if(amountPowerUpsTouched==powerUpCounter) {
          finishedLevel();
        }

        if(powerUp.getType()==1 && powerUp.getHittable()){
            yellowPowerUp(powerUp);
        }

        if(powerUp.getType()==2 && powerUp.getHittable()){
            pinkPowerUp(powerUp);
        }

        if (powerUp.getType()==3 && powerUp.getHittable()){
            greenPowerUp(powerUp);
        }

        if(powerUp.getType()==4 && powerUp.getHittable()){
            redPowerUp(powerUp);
        }

        if(powerUp.getType()==5 && powerUp.getHittable()){
            bluePowerUp(powerUp);
        }

        if(powerUp.getType()==6 && powerUp.getHittable()){
            multiPowerUp(powerUp);
        }
    }

    private void finishedLevel(){
        if(displayedFinishScreen==false) {
            int finishTime= time;
            timeMinutes = (finishTime / (1000 * 60)) % 60;
            timeSeconds = ((finishTime - (timeMinutes * 60 * 1000)) / 1000) % 60;

            finishedScreenBackground.setVisibility(View.VISIBLE);
            pauseScreenBackground.setVisibility(View.VISIBLE);
            nextLevelButton.setVisibility(View.VISIBLE);
            nextLevelButton.setClickable(true);


            pauseScreenTimeText.setVisibility(View.VISIBLE);

            if(finishTime>parTime){
                pauseScreenTimeText.setTextColor(getResources().getColor(R.color.red));
            }

            if (timeMinutes < 10) {
                if (timeSeconds < 10) {
                    pauseScreenTimeText.setText("0" + Integer.toString(timeMinutes) + ":0" + Integer.toString(timeSeconds) + "/0" + Integer.toString(parTimeMinutes) + ":" + Integer.toString(parTimeSeconds));
                } else {
                    pauseScreenTimeText.setText("0" + Integer.toString(timeMinutes) + ":" + Integer.toString(timeSeconds) + "/0" + Integer.toString(parTimeMinutes) + ":" + Integer.toString(parTimeSeconds));
                }
            } else {
                if (timeSeconds < 10) {
                    pauseScreenTimeText.setText(Integer.toString(timeMinutes) + ":0" + Integer.toString(timeSeconds) + "/0" + Integer.toString(parTimeMinutes) + ":" + Integer.toString(parTimeSeconds));
                } else {
                    pauseScreenTimeText.setText(Integer.toString(timeMinutes) + ":" + Integer.toString(timeSeconds) + "/0" + Integer.toString(parTimeMinutes) + ":" + Integer.toString(parTimeSeconds));
                }
            }
            displayedFinishScreen=true;

            for (int i=0;i<powerUpCounter;i++){
                powerUps[i].setInvisible();
            }


            quitLevelButton.setVisibility(View.VISIBLE);
            restartLevelButton.setVisibility(View.VISIBLE);

            quitLevelButton.setClickable(true);
            restartLevelButton.setClickable(true);

            //Right here

            HighScoreEditor highScoreEditor = new HighScoreEditor();
            if(finishTime < highScoreEditor.getValue(this, "HighScore_" + levelNumber)) {
                highScoreEditor.saveInt(this, "HighScore_" + levelNumber, finishTime);
            }
        }
    }

    public void nextLevelButtonClick(View v){
        Intent intent = new Intent(LevelPlay.this, LevelPlay.class);
        intent.putExtra("levelNumber", levelNumber+1);
        startActivity(intent);
        finish();
    }

    //hides whole map
    public void yellowPowerUp(PowerUp powerUp){
        for(int i=0; i<wallNumber-4;i++){
            mazeWall[i].setVisibility(hide);
        }
        powerUp.setInvisible();
        powerUp.setHittable(false);
    }

    //shows whole map
    public void pinkPowerUp(PowerUp powerUp){
        for(int i=0; i<wallNumber;i++){
            mazeWall[i].setVisibility(show);
            mazeWall[i].setTimeTouched((int) System.currentTimeMillis() - timeStart);
        }
        powerUp.setInvisible();
        powerUp.setHittable(false);
    }

    //pause time
    public void greenPowerUp(PowerUp powerUp){



        powerUp.setInvisible();
        powerUp.setHittable(false);
    }

    //inverts controls
    public void redPowerUp(PowerUp powerUp){
        invert = -1;
        powerUp.setInvisible();
        powerUp.setHittable(false);
    }

    //shows certain walls
    public void bluePowerUp(PowerUp powerUp){
        for(int i=0;i<wallNumber;i++){
            double deltaDsquared = Math.pow(mazeWall[i].getCenterX() - powerUp.getCenterX(), 2) + Math.pow(mazeWall[i].getCenterY() - powerUp.getCenterY(), 2);
            if(deltaDsquared < Math.pow(pickup_range, 2)){
                mazeWall[i].setVisibility(show);
                mazeWall[i].setTimeTouched((int) System.currentTimeMillis());
            }
        }
        powerUp.setInvisible();
        powerUp.setHittable(false);
    }

    //choses a random effect
    public void multiPowerUp(PowerUp powerUp){

        powerUp.setInvisible();
        powerUp.setHittable(false);
    }





    /*
        This method is called when there is a valid change in the values that are measured by the gyroscope.

        The steps that this method takes are quite complicated.
            The first step that is taken is to determine how may steps should be taken in the x and y direction.
            It then keeps score of how many steps have been taken in both the x and y direction.
            If this below the steps that have to be taken then the while loop will go through another cycle.
            This loop moves the ball one pixel in both directions (if this is still necessary and a movement in that direction is still allowed) and then checks if there is a collision.
            To detect this the intersectWall() method is called.
            Right after this the intersectPowerUp() method is called to detect if a power up has been hit.
            When this is all done the new position will be send to ImageView of the ball by setLayoutParams() method.

        This method receives two integers, one for the angle in the x direction and one for the y direction.
        It returns nothing

        It changes the following:
            * the layout of the ball
            * the offset from the side and top
     */

    public void move(int x, int y) {
        RelativeLayout.LayoutParams alp = playingBall.getLayoutParams();
        int maxMovementX = Math.abs(x);
        int maxMovenentY = Math.abs(y);
        int stepsTakenX = 0;
        int stepsTakenY = 0;
        a = alp.leftMargin;
        b = alp.topMargin;

        while (maxMovementX > stepsTakenX || maxMovenentY > stepsTakenY) {
            //up 0, down 1, right 3, left 2
            for (int i = 0; i < wallNumber; i++) {
                intersectWall(playingBall, mazeWall[i]);
            }

            for(int i=0; i< powerUpCounter; i++){
                intersectPowerUp(playingBall, powerUps[i]);
            }

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
        playingBall.setLayoutParams(alp);
    }

    /*
        This method sets all the data for the power ups, walls and the balls.
        This can not be done in the onCreate method because the layout has not been initialized there.

        It receives nothing
        It returns nothing

        It changes the same for all the objects:
            * Width
            * Height
            * Centers
            * Corners
     */

    public void start() {

        double offsetX = 11.5;
        double offsetY = 4.7;


        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 13; j++) {
                if (horizontalWalls[i][j] == true) {
                    int id = getResources().getIdentifier("horizontal_wall_" + ((i * 13) + (j + 1)), "id", getPackageName());
                    ImageView wallImage = (ImageView) findViewById(id);
                    mazeWall[wallNumber] = new Wall(wallImage);
                    mazeWall[wallNumber].setWidth(6 * block);
                    mazeWall[wallNumber].setHeight(1 * block);
                    mazeWall[wallNumber].setCenter((int) ((double) j * 5d * (double) block + (4d + offsetX) * (double) block), (int) ((double) i * 5d * (double) block + (double) block * (6d + offsetY)));
                    mazeWall[wallNumber].setCorners();
                    mazeWall[wallNumber].setVisibility(hide);
                    wallNumber = wallNumber + 1;
                }
            }
        }

        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 6; j++) {
                if (verticalWalls[i][j] == true) {
                    int id = getResources().getIdentifier("vertical_wall_" + ((i * 6) + (j + 1)), "id", getPackageName());
                    ImageView wallImage = (ImageView) findViewById(id);
                    mazeWall[wallNumber] = new Wall(wallImage);
                    mazeWall[wallNumber].setWidth(1 * block);
                    mazeWall[wallNumber].setHeight(5 * block);
                    mazeWall[wallNumber].setCenter((int) ((double) i * 5d * (double) block + (double) block * (6.5d + offsetX)), (int) ((double) j * 5d * (double) block + ((double) block * (offsetY + 3d))));
                    mazeWall[wallNumber].setCorners();
                    mazeWall[wallNumber].setVisibility(hide);
                    wallNumber = wallNumber + 1;
                }

            }
        }

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 13; j++) {
                if (powerUpsPlacement[i][j] > 0) {

                    ImageView imagePowerUp = new ImageView(LevelPlay.this);

                    if (powerUpsPlacement[i][j] == 1) {
                        imagePowerUp.setImageResource(R.drawable.pickup_blue_v2);
                    }
                    if (powerUpsPlacement[i][j] == 2) {
                        imagePowerUp.setImageResource(R.drawable.pickup_red_v2);
                    }
                    if (powerUpsPlacement[i][j] == 3) {
                        imagePowerUp.setImageResource(R.drawable.pickup_pink_v2);
                    }
                    if (powerUpsPlacement[i][j] == 4) {
                        imagePowerUp.setImageResource(R.drawable.pickup_green_v2);
                    }
                    if (powerUpsPlacement[i][j] == 5) {
                        imagePowerUp.setImageResource(R.drawable.pickup_yellow_v2);
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

            ImageView leftBorderWallImage = (ImageView) findViewById(R.id.leftBorderWall);
            mazeWall[wallNumber] = new Wall(leftBorderWallImage);
            mazeWall[wallNumber].setWidth(1 * block);
            mazeWall[wallNumber].setHeight(32 * block);
            mazeWall[wallNumber].setCenter((int) ((offsetX + 0.5d) * (double) block), (int) ((offsetY + 15.5d) * (double) block));
            mazeWall[wallNumber].setCorners();
            wallNumber = wallNumber + 1;

            ImageView rightBorderWallImage = (ImageView) findViewById(R.id.rightBorderWall);
            mazeWall[wallNumber] = new Wall(rightBorderWallImage);
            mazeWall[wallNumber].setWidth(1 * block);
            mazeWall[wallNumber].setHeight(32 * block);
            mazeWall[wallNumber].setCenter((int) ((67.5d + offsetX) * (double) block), (int) ((offsetY + 15.5d) * (double) block));
            mazeWall[wallNumber].setCorners();
            wallNumber = wallNumber + 1;

            ImageView topBorderWallImage = (ImageView) findViewById(R.id.topBorderWall);
            mazeWall[wallNumber] = new Wall(topBorderWallImage);
            mazeWall[wallNumber].setWidth(68 * block);
            mazeWall[wallNumber].setHeight(1 * block);
            mazeWall[wallNumber].setCenter((int) ((offsetX + 34d) * (double) block), (int) ((offsetY) * (double) block));
            mazeWall[wallNumber].setCorners();
            wallNumber = wallNumber + 1;

            ImageView bottomBorderWallImage = (ImageView) findViewById(R.id.bottomBorderWall);
            mazeWall[wallNumber] = new Wall(bottomBorderWallImage);
            mazeWall[wallNumber].setWidth(68 * block);
            mazeWall[wallNumber].setHeight(1 * block);
            mazeWall[wallNumber].setCenter((int) ((offsetX + 34d) * (double) block), (int) ((31d + offsetY) * (double) block));
            mazeWall[wallNumber].setCorners();
            wallNumber = wallNumber + 1;


            ImageView border = (ImageView) findViewById(R.id.border);
            border.getLayoutParams().height = (int) (39.20d * block);
            border.getLayoutParams().width = (int) (82d * block);
            RelativeLayout.LayoutParams borderLayout = (RelativeLayout.LayoutParams) border.getLayoutParams();
            borderLayout.topMargin = (int) (1.4d * block);
            borderLayout.leftMargin = (int) (4.4d * (double) block);
            border.setLayoutParams(borderLayout);


            playingBall.setWidth((int) (2d * block));
            playingBall.setHeight((int) (2d * block));
            playingBall.setCenter((int) ((4d + offsetX) * block), (int) ((offsetY + 3.5d) * block));
            playingBall.setCorners();

            timeText.setWidth(30 * block);
            timeText.setHeight(10 * block);
            timeText.setTextSize(block);
            RelativeLayout.LayoutParams paramsTimeText = (RelativeLayout.LayoutParams)timeText.getLayoutParams();
            paramsTimeText.leftMargin=15*block;
            paramsTimeText.topMargin=46*block;
            timeText.setLayoutParams(paramsTimeText);

            parTimeText.setWidth(30 * block);
            parTimeText.setHeight(10 * block);
            parTimeText.setTextSize(block);
            RelativeLayout.LayoutParams paramsParTimeText = (RelativeLayout.LayoutParams) parTimeText.getLayoutParams();
            paramsParTimeText.leftMargin=45*block;
            paramsParTimeText.topMargin=46*block;
            parTimeText.setLayoutParams(paramsParTimeText);

            levelText.setWidth(35 * block);
            levelText.setHeight(30 * block);
            levelText.setTextSize(3 * block);
            RelativeLayout.LayoutParams paramsLevelText = (RelativeLayout.LayoutParams)levelText.getLayoutParams();
            paramsLevelText.leftMargin=60*block;
            paramsLevelText.topMargin=37*block;
            levelText.setLayoutParams(paramsLevelText);


            powerUpsTouched.setWidth(35 * block);
            powerUpsTouched.setHeight(30 * block);
            powerUpsTouched.setTextSize((int) (1.5d * block));
            RelativeLayout.LayoutParams paramsPowerUpsTouched = (RelativeLayout.LayoutParams)powerUpsTouched.getLayoutParams();
            paramsPowerUpsTouched.leftMargin = (int) (37d * block);
            paramsPowerUpsTouched.topMargin = 40 * block;
            powerUpsTouched.setLayoutParams(paramsPowerUpsTouched);

            powerUpsTouched.setText(Integer.toString(amountPowerUpsTouched) + "    /    " + Integer.toString(powerUpCounter));


            pauseScreenBackground2.requestLayout();
            pauseScreenBackground2.getLayoutParams().width = 4*(16*block);
            pauseScreenBackground2.getLayoutParams().height = 4*(9*block);
            RelativeLayout.LayoutParams paramsBackground2 = (RelativeLayout.LayoutParams)pauseScreenBackground2.getLayoutParams();
            paramsBackground2.leftMargin=(int)((5d+7d)*block);
            paramsBackground2.topMargin=(int)((5d+3d)*block);
            pauseScreenBackground2.setLayoutParams(paramsBackground2);


            playButton.requestLayout();
            playButton.getLayoutParams().width = (int)(3.1d*(9*block));
            playButton.getLayoutParams().height = (int)(3.1d*(9*block));
            RelativeLayout.LayoutParams paramsPlayButton = (RelativeLayout.LayoutParams)playButton.getLayoutParams();
            paramsPlayButton.leftMargin=(int)((8.35d+7d)*block);
            paramsPlayButton.topMargin=(int)((8.65d+3d)*block);
            playButton.setLayoutParams(paramsPlayButton);


            quitLevelButton.requestLayout();
            quitLevelButton.getLayoutParams().width = (int)(5*4.65d*block);
            quitLevelButton.getLayoutParams().height = 5*block;
            RelativeLayout.LayoutParams paramsQuitLevel = (RelativeLayout.LayoutParams) quitLevelButton.getLayoutParams();
            paramsQuitLevel.leftMargin=(int)(48d*block);
            paramsQuitLevel.topMargin=28*block;
            quitLevelButton.setLayoutParams(paramsQuitLevel);


            restartLevelButton.requestLayout();
            restartLevelButton.getLayoutParams().width = (int)(5*4.65d*block);
            restartLevelButton.getLayoutParams().height = 5*block;
            RelativeLayout.LayoutParams paramsRestartLevel = (RelativeLayout.LayoutParams)restartLevelButton.getLayoutParams();
            paramsRestartLevel.leftMargin=(int)(42d*block);
            paramsRestartLevel.topMargin=35*block;
            restartLevelButton.setLayoutParams(paramsRestartLevel);

            pauseScreenTimeText.setWidth(35 * block);
            pauseScreenTimeText.setHeight(30 * block);
            pauseScreenTimeText.setTextSize((int)(1.25d * block));
            RelativeLayout.LayoutParams paramsPauseTimeText = (RelativeLayout.LayoutParams)pauseScreenTimeText.getLayoutParams();
            paramsPauseTimeText.leftMargin=45*block;
            paramsPauseTimeText.topMargin=20*block;
            pauseScreenTimeText.setLayoutParams(paramsPauseTimeText);

            nextLevelButton.requestLayout();
            nextLevelButton.getLayoutParams().width = (int)(3.1d*(9*block));
            nextLevelButton.getLayoutParams().height = (int)(3.1d*(9*block));
            RelativeLayout.LayoutParams paramsNextLevelButton = (RelativeLayout.LayoutParams)nextLevelButton.getLayoutParams();
            paramsNextLevelButton.leftMargin=(int)((8.35d+7d)*block);
            paramsNextLevelButton.topMargin=(int)((8.65d+3d)*block);
            nextLevelButton.setLayoutParams(paramsNextLevelButton);

            finishedScreenBackground.requestLayout();
            finishedScreenBackground.getLayoutParams().width = 4*(16*block);
            finishedScreenBackground.getLayoutParams().height = 4*(9*block);
            RelativeLayout.LayoutParams paramsFinished = (RelativeLayout.LayoutParams)finishedScreenBackground.getLayoutParams();
            paramsFinished.leftMargin=(int)((5d+7d)*block);
            paramsFinished.topMargin=(int)((5d+3d)*block);
            finishedScreenBackground.setLayoutParams(paramsFinished);

        touchAllowed=true;

    }


}