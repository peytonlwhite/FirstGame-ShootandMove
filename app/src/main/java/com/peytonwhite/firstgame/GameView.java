package com.peytonwhite.firstgame;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.view.MotionEvent;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameView extends SurfaceView implements Runnable {

    private Thread thread;
    private boolean isPlaying, isGameOver = false;
    private int screenX, screenY, score = 0;
    public static float screenRatioX, screenRatioY;
    private Paint paint;
    private Enemy[] enemies;
   // private Enemy[] enemys;
    private SharedPreferences prefs;
    private Random random;
    private SoundPool soundPool;
    private List<Bullet> bullets;
    private int sound;
    private Player player;
    private GameActivity activity;
    private Background background1, background2;
    private int whichEnemy;
    private int picIdEnemy;
    private int picIdPlayer;


    public GameView(GameActivity activity, int screenX, int screenY) {
        super(activity);

        this.activity = activity;
        Intent intent = activity.getIntent();
        whichEnemy = intent.getIntExtra("game",1);

        if(whichEnemy == 1)
        {
            picIdEnemy = R.drawable.bird1;
            picIdPlayer = R.drawable.fly1;
        }
        else if(whichEnemy == 2)
        {
            picIdEnemy = R.drawable.austinfacef;
            picIdPlayer = R.drawable.peytonfacef;
        }





        prefs = activity.getSharedPreferences("game", Context.MODE_PRIVATE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .build();

            soundPool = new SoundPool.Builder()
                    .setAudioAttributes(audioAttributes)
                    .build();

        } else
            soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);

        sound = soundPool.load(activity, R.raw.shoot, 1);

        this.screenX = screenX;
        this.screenY = screenY;
        screenRatioX = 1920f / screenX;
        screenRatioY = 1080f / screenY;

        background1 = new Background(screenX, screenY, getResources());
        background2 = new Background(screenX, screenY, getResources());

        player = new Player(this, screenY, getResources(),picIdPlayer);

        bullets = new ArrayList<>();

        background2.x = screenX;

        paint = new Paint();
        paint.setTextSize(128);
        paint.setColor(Color.WHITE);

        enemies = new Enemy[4];
       // enemys = new Enemy[4];

        for (int i = 0;i < 4;i++) {

            /*
            Bird bird = new Bird(getResources());
            birds[i] = bird;


             */
            Enemy enemy = new Enemy(getResources(), picIdEnemy);
            enemies[i] = enemy;


        }

        random = new Random();

    }

    @Override
    public void run() {

        while (isPlaying) {

            update ();
            draw ();
            sleep ();

        }

    }

    private void update () {

        background1.x -= 10 * screenRatioX;
        background2.x -= 10 * screenRatioX;

        if (background1.x + background1.background.getWidth() < 0) {
            background1.x = screenX;
        }

        if (background2.x + background2.background.getWidth() < 0) {
            background2.x = screenX;
        }

        if (player.isGoingUp)
            player.y -= 30 * screenRatioY;
        else
            player.y += 30 * screenRatioY;

        if (player.y < 0)
            player.y = 0;

        if (player.y >= screenY - player.height)
            player.y = screenY - player.height;

        List<Bullet> trash = new ArrayList<>();

        for (Bullet bullet : bullets) {

            if (bullet.x > screenX)
                trash.add(bullet);

            bullet.x += 50 * screenRatioX;

            for (Enemy enemy : enemies) {

                if (Rect.intersects(enemy.getCollisionShape(),
                        bullet.getCollisionShape())) {

                    score++;
                    enemy.x = -500;
                    bullet.x = screenX + 500;
                    enemy.wasShot = true;

                }

            }

        }

        for (Bullet bullet : trash)
            bullets.remove(bullet);

        for (Enemy enemy : enemies) {

            enemy.x -= enemy.speed;

            if (enemy.x + enemy.width < 0) {

                if (!enemy.wasShot) {
                    isGameOver = true;
                    return;
                }

                int bound = (int) (30 * screenRatioX);
                enemy.speed = random.nextInt(bound);

                if (enemy.speed < 10 * screenRatioX)
                    enemy.speed = (int) (10 * screenRatioX);

                enemy.x = screenX;
                enemy.y = random.nextInt(screenY - enemy.height);

                enemy.wasShot = false;
            }

            if (Rect.intersects(enemy.getCollisionShape(), player.getCollisionShape())) {

                isGameOver = true;
                return;
            }

        }

    }

    private void draw () {

        if (getHolder().getSurface().isValid()) {

            Canvas canvas = getHolder().lockCanvas();
            canvas.drawBitmap(background1.background, background1.x, background1.y, paint);
            canvas.drawBitmap(background2.background, background2.x, background2.y, paint);

            for (Enemy enemy : enemies)
                canvas.drawBitmap(enemy.getEnemy(), enemy.x, enemy.y, paint);

            canvas.drawText(score + "", screenX / 2f, 164, paint);

            if (isGameOver) {
                isPlaying = false;
                canvas.drawBitmap(player.getDead(), player.x, player.y, paint);
                getHolder().unlockCanvasAndPost(canvas);
                saveIfHighScore();
                waitBeforeExiting ();
                return;
            }

            canvas.drawBitmap(player.getFlight(), player.x, player.y, paint);

            for (Bullet bullet : bullets)
                canvas.drawBitmap(bullet.bullet, bullet.x, bullet.y, paint);

            getHolder().unlockCanvasAndPost(canvas);

        }

    }

    private void waitBeforeExiting() {

        try {
            Thread.sleep(3000);
            activity.startActivity(new Intent(activity, MainActivity.class));
            activity.finish();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void saveIfHighScore() {

        if(whichEnemy == 1) {
            if (prefs.getInt("highscore1", 0) < score) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("highscore1", score);
                editor.apply();
            }
        }
        else
        {
            if (prefs.getInt("highscore2", 0) < score) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("highscore2", score);
                editor.apply();
            }
        }

    }

    private void sleep () {
        try {
            Thread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void resume () {

        isPlaying = true;
        thread = new Thread(this);
        thread.start();

    }

    public void pause () {

        try {
            isPlaying = false;
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (event.getX() < screenX / 2) {
                    player.isGoingUp = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                player.isGoingUp = false;
                if (event.getX() > screenX / 2)
                    player.toShoot++;
                break;

        }

        return true;
    }

    public void newBullet() {

        if (!prefs.getBoolean("isMute", false))
            soundPool.play(sound, 1, 1, 0, 0, 1);

        Bullet bullet = new Bullet(getResources());
        bullet.x = player.x + player.width;
        bullet.y = player.y + (player.height / 2);
        bullets.add(bullet);

    }
}