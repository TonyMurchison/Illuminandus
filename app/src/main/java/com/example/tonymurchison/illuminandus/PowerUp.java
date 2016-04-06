package com.example.tonymurchison.illuminandus;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;


public class PowerUp extends GameObjects {
    private ImageView powerUpImage;
    private int type;
    private boolean hittable=true;

    //constructor
    public PowerUp(int typeInput, ImageView powerUp){
        powerUpImage = powerUp;     //store the given image for the power up
        type = typeInput;

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

    //make the power up go invisible
    public void setInvisible(){powerUpImage.setVisibility(View.GONE);}

    public void setVisible(){powerUpImage.setVisibility(View.VISIBLE);}


    public void setWidth(int widthInput){
        width=widthInput;
        powerUpImage.requestLayout();
        powerUpImage.getLayoutParams().width = width;
    }

    public void setHeight(int heightInput){
        height=heightInput;
        powerUpImage.requestLayout();
        powerUpImage.getLayoutParams().height = height;
    }

    public void setCenter(int x, int y){
        centerX=x;
        centerY=y;
        RelativeLayout.LayoutParams alp = getLayoutParams();
        alp.leftMargin=centerX-width/2;
        alp.topMargin=centerY-height/2;
        setLayoutParams(alp);
    }

    public int getType(){
        return type;
    }

    public void setType(int typeInput){
        type=typeInput;
    }

    public void setHittable(boolean set_hittable){
        hittable=set_hittable;
    }

    public boolean getHittable(){
        return hittable;
    }

    public RelativeLayout.LayoutParams getLayoutParams(){
        return (RelativeLayout.LayoutParams) powerUpImage.getLayoutParams();
    }

    public void setLayoutParams(RelativeLayout.LayoutParams alp){
        powerUpImage.setLayoutParams(alp);
    }
}