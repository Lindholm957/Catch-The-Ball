package com.example.catchtheball;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

public class Result extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        TextView scoreLabel = (TextView) findViewById(R.id.scoreLabel);


        int score = getIntent().getIntExtra("SCORE", 0);

        SharedPreferences settings = getSharedPreferences("GAME_DATA", Context.MODE_PRIVATE);

        if (score > 0){
            scoreLabel.setText(score + "");
        } else {
            int lastScore = settings.getInt("LAST_SCORE", 0);
            scoreLabel.setText(lastScore + "");
        }

        SharedPreferences.Editor editor = settings.edit();

        int best1 = settings.getInt("BEST1", 0);
        int best2 = settings.getInt("BEST2", 0);
        int best3 = settings.getInt("BEST3", 0);
        int best4 = settings.getInt("BEST4", 0);
        int best5 = settings.getInt("BEST5", 0);


        if (score > best5) {
            best5 = score;
            editor.putInt("BEST5", best5);
            editor.apply();
        }

        if (score > best4) {
            int temp = best4;
            best4 = score;
            best5 = temp;
            editor.putInt("BEST5", best5);
            editor.putInt("BEST4", best4);
            editor.apply();
        }

        if (score > best3) {
            int temp = best3;
            best3 = score;
            best4 = temp;
            editor.putInt("BEST4", best4);
            editor.putInt("BEST3", best3);
            editor.apply();
        }

        if (score > best2) {
            int temp = best2;
            best2 = score;
            best3 = temp;
            editor.putInt("BEST3", best3);
            editor.putInt("BEST2", best2);
            editor.apply();
        }

        if (score > best1) {
            int temp = best1;
            best1 = score;
            best2 = temp;
            editor.putInt("BEST2", best2);
            editor.putInt("BEST1", best1);
            editor.apply();
        }

    }
        public void tryAgain (View view){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }

        public void allResults (View view){
            startActivity(new Intent(getApplicationContext(), AllResults.class));
        }

        public void lastResults (View view){
            startActivity(new Intent(getApplicationContext(), LastResults.class));
        }


        //Кнопка "назад" остановлена
        @Override
        public boolean dispatchKeyEvent (KeyEvent event){
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                switch (event.getKeyCode()) {
                    case KeyEvent.KEYCODE_BACK:
                        return true;
                }
            }
            return super.dispatchKeyEvent(event);
        }
}