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