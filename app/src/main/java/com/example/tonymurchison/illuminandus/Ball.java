package com.example.tonymurchison.illuminandus;

import android.widget.ImageView;
import android.widget.RelativeLayout;

/*
    Class describes the ball
        Includes:
            * positions of the centers and the corners
            * the width and height
            * the ImageView itself

        There are methods implemented to set and get this data.
 */

public class Ball{
    //coordinates of the ball
    private int centerX;
    private int centerY;
    private int topLeftX;
    private int topRightX;
    private int bottomLeftX;
    private int bottomRightX;
    private int topLeftY;
    private int topRightY;
    private int bottomLeftY;
    private int bottomRightY;

    //information off the ball
    private int width;
    private int height;
    public ImageView ballImage;

    //constructor
    public Ball(ImageView ball){
        ballImage = ball;
    }

    //This method sets the coordinates for the four corners
    public void setCorners() {
        //top left corner
        topLeftX=(centerX-(width/2));
        topLeftY=(centerY-(height/2));

        //top right corner
        topRightX=(centerX+(width/2));
        topRightY=(centerY-(height/2));

        //bottom right corner
        bottomRightX=(centerX+(width/2));
        bottomRightY=(centerY+(height/2));

        //bottom left corner
        bottomLeftX=(centerX-(width/2));
        bottomLeftY=(centerY+(height/2));
    }

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }

    public int getCenterX(){
        return centerX;
    }

    public int getCenterY(){
        return centerY;
    }

    public int getTopLeftX(){
        return topLeftX;
    }

    public int getTopRightX(){
        return topRightX;
    }

    public int getBottomLeftX(){
        return bottomLeftX;
    }

    public int getBottomRightX(){
        return bottomRightX;
    }

    public int getTopLeftY(){
        return topLeftY;
    }

    public int getTopRightY(){
        return topRightY;
    }

    public int getBottomLeftY(){
        return bottomLeftY;
    }

    public int getBottomRightY(){
        return bottomRightY;
    }

    public void setCenterX(int x){
        centerX=x;
    }

    public void setCenterY(int y){
        centerY=y;
    }

    public void setWidth(int widthInput){
        width = widthInput;
        ballImage.requestLayout();
        ballImage.getLayoutParams().width = width;
    }

    public void setHeight(int heightInput){
        height = heightInput;
        ballImage.requestLayout();
        ballImage.getLayoutParams().height = height;
    }

    public void setCenter(int x, int y){
        centerX=x;
        centerY=y;
        RelativeLayout.LayoutParams alp = getLayoutParams();
        alp.leftMargin=centerX-width/2;
        alp.topMargin=centerY-height/2;
        setLayoutParams(alp);
    }

    public RelativeLayout.LayoutParams getLayoutParams(){
        return (RelativeLayout.LayoutParams) ballImage.getLayoutParams();
    }

    public void setLayoutParams(RelativeLayout.LayoutParams alp){
        ballImage.setLayoutParams(alp);
    }
}