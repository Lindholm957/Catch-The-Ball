package com.example.catchtheball;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class AllResults extends AppCompatActivity {

    TextView first, second, third, fourth, fifth;


    int score;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allresults);

        first = (TextView) findViewById(R.id.first);
        second = (TextView) findViewById(R.id.second);
        third = (TextView) findViewById(R.id.third);
        fourth = (TextView) findViewById(R.id.fourth);
        fifth = (TextView) findViewById(R.id.fifth);

        SharedPreferences settings = getSharedPreferences("GAME_DATA", Context.MODE_PRIVATE);
        score = getIntent().getIntExtra("SCORE", 0);
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


        first.setText(best1 + "");
        second.setText(best2 + "");
        third.setText(best3 + "");
        fourth.setText(best4 + "");
        fifth.setText(best5 + "");

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), Result.class);
        startActivity(intent);
    }
}