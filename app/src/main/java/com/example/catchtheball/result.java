package com.example.catchtheball;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

public class result extends AppCompatActivity {

    int highScore, score, lastScore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        TextView scoreLabel = (TextView) findViewById(R.id.scoreLabel);
        TextView highScoreLabel = (TextView) findViewById(R.id.highScoreLabel);

        SharedPreferences settings = getSharedPreferences("GAME_DATA", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        lastScore = settings.getInt("LAST_SCORE", 0);
        highScore = settings.getInt("HIGH_SCORE", 0);
        score = getIntent().getIntExtra("SCORE", 0);


        if (score > 0){
            editor.putInt("LAST_SCORE", score);
            editor.apply();
            scoreLabel.setText(score + "");
        } else {
            scoreLabel.setText(lastScore + "");
        }


            if (score > highScore){
            highScoreLabel.setText("High Score: " + score);

            //Обновление "Лучшего результата"
            editor.putInt("HIGH_SCORE", score);
                editor.apply();
        } else {
            highScoreLabel.setText("High Score: " + highScore);
        }

    }

    public void tryAgain(View view){
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }

    public void all_results(View view){
        Intent intent = new Intent(getApplicationContext(), all_results.class);
        intent.putExtra("SCORE", score);
        startActivity(intent);
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


}
