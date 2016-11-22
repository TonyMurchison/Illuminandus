package com.example.tonymurchison.illuminandus;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class Wall extends GameObjects {
    private float opacity;
    private ImageView wallImage;
    int timeTouched=0;
    boolean hittable;
    int special=0;
    Context context;

    //constructor
    public Wall(ImageView wall){

        wallImage = wall;

    }

    public void setHittable(boolean x){
        hittable = x;
    }


    public boolean getHittable(){
        return hittable;
    }

    public void setSpecial(int i){
        special = i;
    }

    public int getSpecial(){
        return special;
    }

    public void setColor(Context c){
        context = c;
        if(special==1) {
            wallImage.setBackgroundColor(context.getResources().getColor(R.color.greenKey));
        }
        if(special==2) {
            wallImage.setBackgroundColor(context.getResources().getColor(R.color.blueKey));
        }
        if(special==3) {
            wallImage.setBackgroundColor(context.getResources().getColor(R.color.redKey));
        }
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
        opacity=i;
        if(i==0){
            wallImage.setVisibility(View.INVISIBLE  );
        }
        else {
            wallImage.setVisibility(View.VISIBLE);
        }
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