package com.example.tonymurchison.illuminandus;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;


public class Key extends GameObjects {
    private float opacity;
    private ImageView keyImage;
    private int type;
    private boolean hittable =false;

    public Key(int typeInput, ImageView key){
        type = typeInput;
        keyImage = key;
    }

    public void setHittable(boolean x){
        hittable = x;
    }


    public boolean getHittable(){
        return hittable;
    }

    public int returnType(){
        return type;
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
            keyImage.setVisibility(View.INVISIBLE  );
        }
        else {
            keyImage.setVisibility(View.VISIBLE);
        }
    }

    public void setWidth(int widthInput) {
        width = widthInput;
        keyImage.requestLayout();
        keyImage.getLayoutParams().width = width;
    }

    public void setHeight(int heightInput){
        height = heightInput;
        keyImage.requestLayout();
        keyImage.getLayoutParams().height = height;
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
        return (RelativeLayout.LayoutParams) keyImage.getLayoutParams();
    }

    public void setLayoutParams(RelativeLayout.LayoutParams alp){
        keyImage.setLayoutParams(alp);
    }


}
