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
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;
import android.os.Handler;



public class LevelPlay2 extends AppCompatActivity implements SensorEventListener{
    //TODO nieuwe activity als je een op pauze drukt
    //TODO waar komen de advertenties?






    //time items
    private int timeStart;
    private int time;
    private int timeMinutes;
    private int timeSeconds;
    private int parTimeMinutes;
    private int parTimeSeconds;
    private int pausedTime=0;
    private int timeAtPause=0;
    private int parTime;
    private int timePlayingBallHit;

    //game items
    private Ball playingBall;
    private int ballPositionX;
    private int ballPositionY;


    //remaining items
    private boolean displayedFinishScreen= false;
    private SensorManager sManager;
    private int x = 0;          //stores the angle of the phone around the x-axis
    private int y = 0;          //stores the angle of the phone around the y-axis
    private boolean allowedMovement[] = {true, true, true, true};
    private int visibleThreshold=8000;
    private double invert=1;
    private int pickup_range=500;
    private int levelNumber=0;  //level 1 equals levelNumber 0 etc
    private double allowMovement =1;
    private boolean touchAllowed = false;
    private boolean ballHidden = false;
    private Handler h;
    private int delay = 20;

    private Wall mazeWall[] = new Wall[241];
    private boolean horizontalWalls[][] = new boolean[9][7];
    private boolean verticalWalls[][] = new boolean[10][6];
    private int wallNumber=0;

    int screenWidth;
    double speedAdjustment;
    int block;

    private float show = 1;
    private float hide = 0;

    private ArrayList<PowerUp> powerUps = new ArrayList<>();
    private int powerUpsPlacement[][]= new int[10][7];
    private int powerUpCounter=0;
    private int amountPowerUpsTouched=0;


    private TextView levelTextView;
    private TextView powerUpsCounterTextView;
    private TextView timeTextView;
    private String parTimeString;

    private boolean started = false; //dit is hopelijk tijdelijk om te zorgen dat de tijd niet negatief is bij de start


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Bundle extras = getIntent().getExtras();
        levelNumber = extras.getInt("levelNumber");

        setContentView(R.layout.vertical_level);
        getSupportActionBar().hide();
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        //set ball
        ImageView ballImage = (ImageView)findViewById(R.id.ball);
        playingBall = new Ball(ballImage);

        //set the things that are depended of the screen resolution
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.y;
        speedAdjustment=screenWidth/1920d;
        block=(int)Math.round(screenWidth / 90d);

        //set times
        int parTimeArray [] = getResources().getIntArray(R.array.parTime);
        parTime = parTimeArray[levelNumber];
        parTimeMinutes=(parTime/(1000*60))%60;
        parTimeSeconds=((parTime-(parTimeMinutes*60*1000))/1000)%60;
        if(parTimeMinutes>9){
            if(parTimeSeconds>9){
                parTimeString=Integer.toString(parTimeMinutes)+":"+Integer.toString(parTimeSeconds);
            }
            else{
                parTimeString=Integer.toString(parTimeMinutes)+":0"+Integer.toString(parTimeSeconds);
            }
        }
        else{
            if(parTimeSeconds>9){
                parTimeString="0"+Integer.toString(parTimeMinutes)+":"+Integer.toString(parTimeSeconds);
            }
            else{
                parTimeString="0"+Integer.toString(parTimeMinutes)+":0"+Integer.toString(parTimeSeconds);
            }
        }


        timeStart=(int)System.currentTimeMillis();

        readFile();

        levelTextView = (TextView)findViewById(R.id.levelTextView);
        levelTextView.setText("Level "+Integer.toString(levelNumber+1));

        powerUpsCounterTextView = (TextView)findViewById(R.id.powerUpsCounterTextView);
        timeTextView = (TextView)findViewById(R.id.timeTextView);

        //run the method that initializes the layout
        start();



    }

    @Override
    public void onPause(){
        super.onPause();
        timeAtPause = (int)System.currentTimeMillis();
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        //TODO
    }

    void readFile(){
        AssetManager assetManager = getResources().getAssets();
        InputStream inputStream = null;

        try {
            inputStream = assetManager.open("levelsFile2.txt");

        } catch (IOException e) {
            e.printStackTrace();
        }

        Scanner scanner = new Scanner(inputStream);
        boolean reading = true;
        String level = "Level "+Integer.toString(levelNumber+1);
        while(reading==true){
            if(level.equals(scanner.nextLine())){
                for(int i=0;i<10;i++){
                    String line = scanner.nextLine();
                    for(int j=0;j<7;j++){
                        powerUpsPlacement[i][j]=Character.getNumericValue(line.charAt(j));
                    }
                }

                scanner.nextLine();

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

        //if sensor is unreliable, return void
        if (event.accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE) {
            return;
        }

        //read the data from the gyroscopes
        double xb=event.values[2]*speedAdjustment;
        double yb=event.values[1]*speedAdjustment*-1;

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
                timeTextView.setText("0" + Integer.toString(timeMinutes) + ":0" + Integer.toString(timeSeconds)+"/"+parTimeString);
            } else {
                timeTextView.setText("0" + Integer.toString(timeMinutes) + ":" + Integer.toString(timeSeconds)+"/"+parTimeString);
            }
        } else {
            if (timeSeconds < 10) {
                timeTextView.setText(Integer.toString(timeMinutes) + ":0" + Integer.toString(timeSeconds)+"/"+parTimeString);
            } else {
                timeTextView.setText(Integer.toString(timeMinutes) + ":" + Integer.toString(timeSeconds)+"/"+parTimeString);
            }
        }



    }

    //restart level
    public void restartButtonClick(View v){
        super.recreate();
    }

    //go back to the level select screen
    public void quitLevelClick(View v){
        Intent intent = new Intent(LevelPlay2.this, LevelSelect.class);
        intent.putExtra("levelNumber", levelNumber);
        startActivity(intent);
        finish();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if(started==true) {
            pausedTime = pausedTime + ((int) System.currentTimeMillis() - timeAtPause);
        }
        started=true;

        sManager.registerListener(this, sManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_FASTEST);
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
    }

    //When this Activity isn't visible anymore
    @Override
    protected void onStop() {
        //unregister the sensor listener
        sManager.unregisterListener(this);
        h = null;
        super.onStop();
        timeAtPause = (int)System.currentTimeMillis();
    }

    //open up the pause screen
    public void pauseScreen(View v){
        //timeAtPause= (int) System.currentTimeMillis();
        Intent intent = new Intent(LevelPlay2.this, PauseScreen.class);
        startActivity(intent);

    }

    //if a power up is hit this method will determine what has to happen
    public void hitPowerUp(PowerUp powerUp){
        //hide the powerup and count it as hit
        if(powerUp.getHittable()) {
            amountPowerUpsTouched = amountPowerUpsTouched + 1;
            powerUpsCounterTextView.setText(Integer.toString(amountPowerUpsTouched) + " / " + Integer.toString(powerUpCounter));
        }

        else return;

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

    //this method is called when the last powerup has been picked up
    private void finishedLevel(){
        //TODO
        //checks if a new high score has been achieved. If so this will be stored.
        HighScoreEditor highScoreEditor = new HighScoreEditor();
        int previous_score = highScoreEditor.getValue(this, "HighScore_" + levelNumber);
        if(time < previous_score || previous_score == 0) {
            highScoreEditor.saveInt(this, "HighScore_" + levelNumber, time);
        }


    }

    //This method is called when the player hits the next level button
    public void nextLevelButtonClick(View v){
        UnlockEditor unlockCheck = new UnlockEditor();
        //checks if the next level is unlocked
        if(unlockCheck.requestUnlock(this, (levelNumber / 4)) || levelNumber % 4 != 3) {
            if(levelNumber!=8) {
                Intent intent = new Intent(LevelPlay2.this, LevelPlay2.class);
                intent.putExtra("levelNumber", levelNumber + 1);

                startActivity(intent);
                finish();
            }
            else {
                Toast toast = Toast.makeText(this, "Coming soon!", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
        else{
            Toast toast = Toast.makeText(this, "Next level not yet unlocked", Toast.LENGTH_SHORT);
            toast.show();
        }
    }


    //hides whole map
    public void pinkPowerUp(PowerUp powerUp){
        for(int i=0; i<wallNumber-4;i++){
            mazeWall[i].setVisibility(hide);
        }
        powerUp.setInvisible();
        powerUp.setHittable(false);
    }

    //shows whole map
    public void bluePowerUp(PowerUp powerUp){
        for(int i=0; i<wallNumber;i++){
            mazeWall[i].setVisibility(show);
            mazeWall[i].setTimeTouched((int) System.currentTimeMillis() - timeStart-pausedTime);
        }
        powerUp.setInvisible();
        powerUp.setHittable(false);
    }

    //hides ball
    public void yellowPowerUp(PowerUp powerUp){
        playingBall.setVisibility(hide);
        timePlayingBallHit = (int) System.currentTimeMillis()-timeStart-pausedTime;
        ballHidden=true;

        powerUp.setInvisible();
        powerUp.setHittable(false);
    }

    //inverts controls
    public void redPowerUp(PowerUp powerUp){
        invert = invert*-1;
        powerUp.setInvisible();
        powerUp.setHittable(false);
    }

    //shows certain walls
    public void greenPowerUp(PowerUp powerUp){
        for(int i=0;i<wallNumber;i++){
            double deltaDsquared = Math.pow(mazeWall[i].getCenterX() - powerUp.getCenterX(), 2) + Math.pow(mazeWall[i].getCenterY() - powerUp.getCenterY(), 2);
            if(deltaDsquared < Math.pow(pickup_range, 2)){
                mazeWall[i].setVisibility(show);
                mazeWall[i].setTimeTouched((int) System.currentTimeMillis()-timeStart-pausedTime);
            }
        }
        powerUp.setInvisible();
        powerUp.setHittable(false);
    }

    //choses a random effect
    public void multiPowerUp(PowerUp powerUp){
        //TODO add multi effects
        powerUp.setInvisible();
        powerUp.setHittable(false);
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

        if (time > (parTime + 1000)) {
            timeTextView.setTextColor(getResources().getColor(R.color.red));
        }

        //while steps have to be taken:
        while (maxMovementX > stepsTakenX || maxMovenentY > stepsTakenY) {
            //check all the walls
            for (int i = 0; i < wallNumber; i++) {
                intersectWall(playingBall, mazeWall[i]);
            }
            //checks all the powerups
            for(int i=0; i< powerUpCounter; i++){
                intersectPowerUp(playingBall, powerUps.get(i));
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

    //checks if a certain powerup and the playing ball intersect
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

    //if a wall is hit this method will be called. It decides which side of the wall is hit and therefor decides in which direction the movement of the ball should be stopped.
    public void limitMovement(Ball ball, Wall wall) {
        //this is calculated with the minkowski sum method: (https://en.wikipedia.org/wiki/Minkowski_addition)
        float wy = (ball.getWidth() + wall.getWidth()) * (ball.getCenterY() - wall.getCenterY());
        float hx = (ball.getHeight() + wall.getHeight()) * (ball.getCenterX() - wall.getCenterX());

        wall.setVisibility(1f);
        wall.setTimeTouched((int) System.currentTimeMillis() - timeStart - pausedTime);

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



        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 7; j++) {
                if (horizontalWalls[i][j] == true) {
                    int id = getResources().getIdentifier("horizontal_wall_" + ((i*7) + (j)), "id", getPackageName());
                    ImageView wallImage = (ImageView) findViewById(id);
                    mazeWall[wallNumber] = new Wall(wallImage);
                    mazeWall[wallNumber].setWidth((int)(8.25d * block));
                    mazeWall[wallNumber].setHeight((int)(1d * block));
                    //setCenter(hoogte, breedte) in portrait mode perspectief.
                    mazeWall[wallNumber].setCenter((int) ((double) j * 7.25d * (double) block + (3.88d) * (double) block), (int) ((double) i * 7.25d * (double) block + (double) block * (26d)));
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
                    mazeWall[wallNumber].setWidth((int)(1d * block));
                    mazeWall[wallNumber].setHeight((int)(8.25d * block)); //8.25
                    mazeWall[wallNumber].setCenter((int) ((double) j * 7.25d * (double) block + (double) block * (7.5d)), (int) ((double) i * 7.25d * (double) block + ((double) block * (22.38d))));
                    mazeWall[wallNumber].setCorners();
                    mazeWall[wallNumber].setVisibility(show);
                    wallNumber = wallNumber + 1;
                }
            }
        }

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 7; j++) {
                if (powerUpsPlacement[i][j] > 0) {

                    ImageView imagePowerUp = new ImageView(LevelPlay2.this);

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

                    powerUps.add(powerUpCounter,new PowerUp(powerUpsPlacement[i][j], imagePowerUp));
                    powerUps.get(powerUpCounter).setWidth((int) (2d * block));
                    powerUps.get(powerUpCounter).setHeight((int) (2d * block));
                    powerUps.get(powerUpCounter).setCenter((int) ((double) j * 7.25d * (double) block + (3.88d) * (double) block), (int) ((double) i * 7.25d * (double) block + (double) block * (22.38d)));
                    powerUps.get(powerUpCounter).setCorners();
                    powerUpCounter = powerUpCounter + 1;
                }
            }
        }


        ImageView leftBorderWallImage = (ImageView) findViewById(R.id.leftBorderWall);
        mazeWall[wallNumber] = new Wall(leftBorderWallImage);
        mazeWall[wallNumber].setWidth(1 * block);
        mazeWall[wallNumber].setHeight(73 * block);
        mazeWall[wallNumber].setCenter((int) ((0.5d) * (double) block), (int) ((55.5d) * (double) block));
        mazeWall[wallNumber].setCorners();
        wallNumber = wallNumber + 1;

        ImageView rightBorderWallImage = (ImageView) findViewById(R.id.rightBorderWall);
        mazeWall[wallNumber] = new Wall(rightBorderWallImage);
        mazeWall[wallNumber].setWidth(1 * block);
        mazeWall[wallNumber].setHeight(73 * block);
        mazeWall[wallNumber].setCenter((int) ((51d) * (double) block), (int) ((55.5d) * (double) block));
        mazeWall[wallNumber].setCorners();
        wallNumber = wallNumber + 1;

        ImageView topBorderWallImage = (ImageView) findViewById(R.id.topBorderWall);
        mazeWall[wallNumber] = new Wall(topBorderWallImage);
        mazeWall[wallNumber].setWidth(52 * block);
        mazeWall[wallNumber].setHeight(1 * block);
        mazeWall[wallNumber].setCenter((int) ((25.75d) * (double) block), (int) ((18.75d) * (double) block));
        mazeWall[wallNumber].setCorners();
        wallNumber = wallNumber + 1;

        ImageView bottomBorderWallImage = (ImageView) findViewById(R.id.bottomBorderWall);
        mazeWall[wallNumber] = new Wall(bottomBorderWallImage);
        mazeWall[wallNumber].setWidth(52 * block);
        mazeWall[wallNumber].setHeight(1 * block);
        mazeWall[wallNumber].setCenter((int) ((25.75d) * (double) block), (int) ((91d) * (double) block));
        mazeWall[wallNumber].setCorners();
        wallNumber = wallNumber + 1;



        playingBall.setWidth((int) (2d * block));
        playingBall.setHeight((int) (2d * block));
        playingBall.setCenter((int) ((3.88d + ballPositionX * 7.25) * block), (int) ((22.38d + ballPositionY * 7.25) * block));
        playingBall.setCorners();

        powerUpsCounterTextView.setText("0 / "+Integer.toString(powerUpCounter));
    }

}
