package com.peytonwhite.firstgame;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import static com.peytonwhite.firstgame.GameView.screenRatioX;
import static com.peytonwhite.firstgame.GameView.screenRatioY;

public class Enemy {

    public int speed = 20;
    public boolean wasShot = true;
    int x, y,width,height;
    Bitmap enemy;

    Enemy(Resources res,int rId)
    {
        enemy = BitmapFactory.decodeResource(res, rId);

        width = enemy.getWidth();
        height = enemy.getHeight();

        width /= 20;
        height /= 20;

        width = (int) (width * screenRatioX);
        height = (int) (height * screenRatioY);

        enemy = Bitmap.createScaledBitmap(enemy,width,height,false);


        y = -height;




    }

    Bitmap getEnemy()
    {
            return enemy;
    }

    Rect getCollisionShape()
    {
        return new Rect(x,y,x+width,y+height);
    }






}
