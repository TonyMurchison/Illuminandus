package com.example.tonymurchison.illuminandus;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/*
    Class describes the wall
        Includes:
            * positions of the centers and the corners
            * the width and height
            * the ImageView itself
            * the opacity of the wall

        There are methods implemented to set and get this data.
 */

public class Wall extends AppCompatActivity {
    //coordinates of the wall
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


    //info of the wall
    private int width;
    private int height;
    private float opacity;
    private ImageView wallImage;
    int timeTouched=0;

    //constructor
    public Wall(ImageView wall){

        wallImage = wall;

    }

    //This method sets the coordinates for the four corners
    public void setCorners() {
        if(width>height){
            //top left corner
            topLeftX=(centerX-(width/2)+2);
            topLeftY=(centerY-(height/2));

            //top right corner
            topRightX=(centerX+(width/2)-2);
            topRightY=(centerY-(height/2));

            //bottom right corner
            bottomRightX=(centerX+(width/2)-2);
            bottomRightY=(centerY+(height/2));

            //bottom left corner
            bottomLeftX=(centerX-(width/2)+2);
            bottomLeftY=(centerY+(height/2));
            return;
        }

        else{
            //top left corner
            topLeftX=(centerX-(width/2));
            topLeftY=(centerY-(height/2)+2);

            //top right corner
            topRightX=(centerX+(width/2));
            topRightY=(centerY-(height/2)+2);

            //bottom right corner
            bottomRightX=(centerX+(width/2));
            bottomRightY=(centerY+(height/2)-2);

            //bottom left corner
            bottomLeftX=(centerX-(width/2)+2);
            bottomLeftY=(centerY+(height/2)-2);
        }
    }

    public float getVisility(){
        return opacity;
    }

    public void setVisibility(float i){
        opacity=1;
        if(i==0){
            wallImage.setVisibility(View.INVISIBLE  );
        }
        else {
            wallImage.setVisibility(View.VISIBLE);
        }
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

    public void setWidth(int widthInput) {
        width = widthInput;
        wallImage.requestLayout();
        wallImage.getLayoutParams().width = width;
    }

    public void setHeight(int heightInput){
        height = heightInput;
        wallImage.requestLayout();
        wallImage.getLayoutParams().height = height;
    }

    public void setCenter(int x, int y){
        centerX=x;
        centerY=y;
        RelativeLayout.LayoutParams alp = getLayoutParams();
        alp.leftMargin=centerX-width/2;
        alp.topMargin=centerY-height/2;
        setLayoutParams(alp);

    }

    public void setTimeTouched(int timeInput){
        timeTouched=timeInput;
    }

    public int getTimeTouched(){
        return timeTouched;
    }

    public RelativeLayout.LayoutParams getLayoutParams(){
        return (RelativeLayout.LayoutParams) wallImage.getLayoutParams();
    }

    public void setLayoutParams(RelativeLayout.LayoutParams alp){
        wallImage.setLayoutParams(alp);
    }
}