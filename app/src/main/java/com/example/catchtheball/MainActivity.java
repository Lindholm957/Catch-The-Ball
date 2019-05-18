package com.example.catchtheball;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Point;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.*;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    DBHelper dbHelper;

    private TextView scoreLabel;
    private TextView startLabel;
    private ImageView box;
    private ImageView orange;
    private ImageView pink;
    private ImageView black;

    //Размер
    private int frameHeight;
    private int boxSize;
    private int screenWidth;
    private int screenHeight;

    //Позиция
    private int boxY;
    private int orangeX;
    private int orangeY;
    private int pinkX;
    private int pinkY;
    private int blackX;
    private int blackY;

    // Скорость
    private int boxSpeed;
    private int orangeSpeed;
    private int pinkSpeed;
    private int blackSpeed;

    //Счёт
    private int score = 0;
    private long startTime = 0;
    private long stopTime = 0;
    private long time;
    private boolean running = false;
    private int oranges_get = 0;
    private int pinks_get = 0;
    private int touches = 0;


    //Инициализируем классы
    private Handler handler = new Handler();
    private Timer timer = new Timer();
    private SoundPlayer sound;

    //Проверка статуса
    private boolean action_flg = false;
    private boolean start_flg = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sound = new SoundPlayer(this);

        scoreLabel = (TextView) findViewById(R.id.scoreLabel);
        startLabel = (TextView) findViewById(R.id.startLabel);
        box = (ImageView) findViewById(R.id.box);
        orange = (ImageView) findViewById(R.id.orange);
        pink = (ImageView) findViewById(R.id.pink);
        black = (ImageView) findViewById(R.id.black);

        dbHelper = new DBHelper(this);

        //Получаем размер экрана
        WindowManager wm = getWindowManager();
        Display disp = wm.getDefaultDisplay();
        Point size = new Point();
        disp.getSize(size);

        screenWidth = size.x;
        screenHeight = size.y;

        //Nexus4 width: 768 height: 1184
        //Speed box: 20 orange: 12 pink: 20 black 16
        boxSpeed = Math.round(screenHeight / 60F); // 1184 / 60 = 19.733... => 20
        orangeSpeed = Math.round(screenWidth / 60F); // 768 / 60 = 12.8... => 13
        pinkSpeed = Math.round(screenWidth / 36F); // 768 / 36 = 21.333... => 21
        blackSpeed = Math.round(screenWidth / 45F); // 768 / 45 = 17.06... => 17

        Log.v("SPEED_BOX",  boxSpeed+"");
        Log.v("SPEED_ORANGE",  orangeSpeed+"");
        Log.v("SPEED_PINK",  pinkSpeed+"");
        Log.v("SPEED_BLACK",  blackSpeed+"");

        //Переместиться за пределы экрана
        orange.setX(-80);
        orange.setY(-80);
        pink.setX(-80);
        pink.setY(-80);
        black.setX(-80);
        black.setY(-80);

        scoreLabel.setText("Score: 0");
    }

    public void changePos() {

        hitCheck();
        // Orange
        orangeX -= orangeSpeed;
        if (orangeX < 0){
            orangeX = screenWidth +20;
            orangeY = (int) Math.floor(Math.random() * (frameHeight - orange.getHeight()));
        }
        orange.setX(orangeX);
        orange.setY(orangeY);

        //Black
        blackX -= blackSpeed;
        if (blackX < 0) {
            blackX = screenWidth + 10;
            blackY = (int) Math.floor(Math.random() * (frameHeight - black.getHeight()));
        }

        black.setX(blackX);
        black.setY(blackY);

        //Pink
        pinkX -= pinkSpeed;
        if (pinkX < 0){
            pinkX = screenWidth +5000;
            pinkY = (int) Math.floor(Math.random() * (frameHeight - pink.getHeight()));
        }

        pink.setX(pinkX);
        pink.setY(pinkY);

        //Перемещение box
        if (action_flg == true){
            //Touching
            boxY -= boxSpeed;
        } else  {
            //Releasing
            boxY += boxSpeed;
        }

        //Проверка позиции box
        if (boxY < 0) boxY = 0;

        if (boxY > frameHeight - boxSize) boxY = frameHeight - boxSize;

        box.setY(boxY);

        scoreLabel.setText("Score: "+ score);


    }

    public void hitCheck(){
        // Если шарик в центре box, считается как попадание

        //Orange
        int orangeCenterX = orangeX + orange.getWidth()/2;
        int orangeCenterY = orangeY + orange.getHeight()/2;

        if (0 <= orangeCenterX && orangeCenterX <= boxSize &&
                boxY <= orangeCenterY && orangeCenterY <= boxY + boxSize){
            score += 10;
            orangeX = -10;
            oranges_get++;
            sound.playHitSound();
        }

        //Pink
        int pinkCenterX = pinkX + pink.getWidth()/2;
        int pinkCenterY = pinkY + pink.getHeight()/2;

        if (0 <= pinkCenterX && pinkCenterX <= boxSize &&
                boxY <= pinkCenterY && pinkCenterY <= boxY + boxSize){
            score += 30;
            pinkX = -10;
            pinks_get++;
            sound.playHitSound();
        }

        //Black
        int blackCenterX = blackX + black.getWidth()/2;
        int blackCenterY = blackY + black.getHeight()/2;

        if (0 <= blackCenterX && blackCenterX <= boxSize &&
                boxY <= blackCenterY && blackCenterY <= boxY + boxSize){
            timer.cancel();
            timer = null;
            stop();

            sound.playOverSound();

            time = getElapsedTimeSecs();
            //Результат!!!

            addSession();

            SharedPreferences settings = getSharedPreferences("GAME_DATA", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();

            int lastScore = settings.getInt("LAST_SCORE", 0);
            editor.putInt("LAST_SCORE", score);
            editor.apply();

            Intent intent = new Intent(getApplicationContext(), Result.class);
            intent.putExtra("SCORE", score);
            startActivity(intent);
        }
    }


    public boolean onTouchEvent(MotionEvent me){


        if(start_flg == false){

            start();

            start_flg = true;

            FrameLayout frame = (FrameLayout) findViewById(R.id.frame);
            frameHeight = frame.getHeight();

            boxY = (int)box.getY();

            boxSize = box.getHeight();

            startLabel.setVisibility(View.GONE);

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            changePos();
                        }
                    });
                }
            }, 0, 20); //вызывыем changePos() каждые 20 милисекунд

        } else {
            if (me.getAction() == MotionEvent.ACTION_DOWN){
                action_flg = true;
                touches++;

            } else if (me.getAction() == MotionEvent.ACTION_UP){
                action_flg = false;
            }
        }


        return true;
    }

    public void start() {
        this.startTime = System.currentTimeMillis();
        this.running = true;
    }

    public void stop() {
        this.stopTime = System.currentTimeMillis();
        this.running = false;
    }

    //elaspsed time in seconds
    public long getElapsedTimeSecs() {
        long elapsed;
        if (running) {
            elapsed = ((System.currentTimeMillis() - startTime) / 1000);
        } else {
            elapsed = ((stopTime - startTime) / 1000);
        }
        return elapsed;
    }

    //Кнопка "назад" остановлена
    @Override
    public boolean dispatchKeyEvent(KeyEvent event){
        if (event.getAction() == KeyEvent.ACTION_DOWN){
            switch (event.getKeyCode()){
                case KeyEvent.KEYCODE_BACK:
                    return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }

    public void addSession(){
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        String str_score = String.valueOf(score);
        String str_time = String.valueOf(time);
        String str_orange = String.valueOf(oranges_get);
        String str_pink = String.valueOf(pinks_get);
        String str_touches = String.valueOf(touches);

        contentValues.put(DBHelper.KEY_SCORE, str_score);
        contentValues.put(DBHelper.KEY_TIME, str_time);
        contentValues.put(DBHelper.KEY_ORANGE, str_orange);
        contentValues.put(DBHelper.KEY_PINK, str_pink);
        contentValues.put(DBHelper.KEY_TOUCHES, str_touches);

        database.insert(DBHelper.TABLE_SESSIONS, null, contentValues);
        database.close();
    }
}
