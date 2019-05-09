package com.example.catchtheball;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class all_results extends AppCompatActivity {

    TextView tv_score;


    int score, highScore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_results);


        tv_score = (TextView) findViewById(R.id.tv_score);

        score = getIntent().getIntExtra("SCORE", 0);


        SharedPreferences settings = getSharedPreferences("GAME_DATA", Context.MODE_PRIVATE);
        highScore = settings.getInt("HIGH_SCORE", 0);

        int best2 = settings.getInt("BEST2", 0);
        int best3 = settings.getInt("BEST3", 0);
        int best4 = settings.getInt("BEST4", 0);
        int best5 = settings.getInt("BEST5", 0);

        if (score > best5) {
            best5 = score;
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt("BEST5", best5);
            editor.commit();
        }

        if (score > best4) {
            int temp = best4;
            best4 = score;
            best5 = temp;
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt("BEST5", best5);
            editor.putInt("BEST4", best4);
            editor.commit();
        }

        if (score > best3) {
            int temp = best3;
            best3 = score;
            best4 = temp;
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt("BEST4", best4);
            editor.putInt("BEST3", best3);
            editor.commit();
        }

        if (score > best2) {
            int temp = best2;
            best2 = score;
            best3 = temp;
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt("BEST3", best3);
            editor.putInt("BEST2", best2);
            editor.apply();
        }

        if (score > highScore) {
            int temp = highScore;
            highScore = score;
            best2 = temp;
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt("BEST2", best2);
            editor.putInt("HIGH_SCORE", highScore);
            editor.commit();
        }


        tv_score.setText("BEST1: " + highScore + "\n" +
                "BEST2: " + best2 + "\n" +
                "BEST3: " + best3 + "\n" +
                "BEST4: " + best4 + "\n" +
                "BEST5: " + best5);

    }

    @Override
    public void onBackPressed() {
        SharedPreferences settings = getSharedPreferences("GAME_DATA", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.commit();

        Intent intent = new Intent(getApplicationContext(), result.class);
        intent.putExtra("SCORE", score);
        startActivity(intent);
    }
}