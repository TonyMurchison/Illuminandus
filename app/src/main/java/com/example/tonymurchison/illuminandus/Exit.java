package com.example.tonymurchison.illuminandus;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class Exit extends GameObjects{
    private ImageView exitImage;
    private boolean hittable = true;

    //constructor
    public Exit(ImageView exit){
        exitImage = exit;
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


    public void setCenterX(int x){
        centerX=x;
    }

    public void setCenterY(int y){
        centerY=y;
    }

    public void setVisibility(float x){
        if(x==1){
            exitImage.setVisibility(View.VISIBLE);
        }
        if(x==0){
            exitImage.setVisibility(View.INVISIBLE);
        }
    }

    public void setWidth(int widthInput){
        width = widthInput;
        exitImage.requestLayout();
        exitImage.getLayoutParams().width = width;
    }

    public void setHeight(int heightInput){
        height = heightInput;
        exitImage.requestLayout();
        exitImage.getLayoutParams().height = height;
    }

    public void setCenter(int x, int y){
        centerX=x;
        centerY=y;
        RelativeLayout.LayoutParams alp = getLayoutParams();
        alp.leftMargin=centerX-width/2;
        alp.topMargin=centerY-height/2;
        setLayoutParams(alp);
    }

    public void setHittable(boolean x){
        hittable = x;
    }

    public boolean getHittable(){
        return hittable;
    }

    public RelativeLayout.LayoutParams getLayoutParams(){
        return (RelativeLayout.LayoutParams) exitImage.getLayoutParams();
    }

    public void setLayoutParams(RelativeLayout.LayoutParams alp){
        exitImage.setLayoutParams(alp);
    }
}