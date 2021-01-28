package com.peytonwhite.firstgame;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import static com.peytonwhite.firstgame.GameView.screenRatioX;
import static com.peytonwhite.firstgame.GameView.screenRatioY;

public class Player {

    public boolean isGoingUp = false;
    public int toShoot = 0;
    int x, y,width,height,wingCounter =0,shootCounter = 1;
    Bitmap flight1, flight2,shoot1,shoot2,shoot3,shoot4,shoot5,dead;
    private GameView gameView;



    public Player(GameView gameView,int screenY, Resources res,int rId)
    {
        this.gameView = gameView;
        flight1 = BitmapFactory.decodeResource(res,rId);
        //flight2 = BitmapFactory.decodeResource(res,rId);

        width =  flight1.getWidth();
        height = flight1.getHeight();

        width /=10;
        height /= 10;

        width = (int) (width * screenRatioX);
        height = (int) (height * screenRatioY);

        flight1 = Bitmap.createScaledBitmap(flight1,width,height,false);

        /*
        flight2 = Bitmap.createScaledBitmap(flight2,width,height,false);
        shoot1 = BitmapFactory.decodeResource(res,rId);
        shoot2 = BitmapFactory.decodeResource(res,rId);
        shoot3 = BitmapFactory.decodeResource(res,rId);
        shoot4 = BitmapFactory.decodeResource(res,rId);
        shoot5 = BitmapFactory.decodeResource(res,rId);


        shoot1 = Bitmap.createScaledBitmap(shoot1,width,height,false);
        shoot2 = Bitmap.createScaledBitmap(shoot2,width,height,false);
        shoot3 = Bitmap.createScaledBitmap(shoot3,width,height,false);
        shoot4 = Bitmap.createScaledBitmap(shoot4,width,height,false);
        shoot5 = Bitmap.createScaledBitmap(shoot5,width,height,false);


         */
/*
        dead = BitmapFactory.decodeResource(res,rId);
        dead = Bitmap.createScaledBitmap(dead,width,height,false);


 */
        y = screenY /2;
        x = (int) (64 * screenRatioX);


    }

    Bitmap getFlight()
    {
        if(toShoot != 0) {

            shootCounter = 1;
            toShoot--;
            gameView.newBullet();
            return flight1;

        }



        wingCounter--;
        return flight1;
    }


    Rect getCollisionShape()
    {
        return new Rect(x,y,x+width,y+height);
    }

    Bitmap getDead()
    {
        return flight1;
    }



}
