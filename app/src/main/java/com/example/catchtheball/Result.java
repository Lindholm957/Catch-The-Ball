package com.example.catchtheball;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Result extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        TextView scoreLabel = (TextView) findViewById(R.id.scoreLabel);
        TextView best_results = (Button) findViewById(R.id.best_results);


        int score = getIntent().getIntExtra("SCORE", 0);
        scoreLabel.setText(score + "");


        SharedPreferences settings = getSharedPreferences("GAME_DATA", Context.MODE_PRIVATE);
        int highScore = settings.getInt("HIGH_SCORE", 0);

    }
        public void tryAgain (View view){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }

        public void all_results (View view){
            startActivity(new Intent(getApplicationContext(), AllResults.class));
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