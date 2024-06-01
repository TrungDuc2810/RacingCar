package com.example.game11;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Timer;

import android.media.MediaPlayer;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements GameTask {
    private LinearLayout rootLayout;
    private Button btnStart, btnTopScore;
    private GameView mGameView;
    private TextView score;
    private MediaPlayer music, music2;
    private ImageButton btnPause;
    private boolean isGameRunning;

    public void findViews() {
        btnStart = findViewById(R.id.btnStart);
        btnTopScore = findViewById(R.id.btnTopScore);
        rootLayout = findViewById(R.id.rootLayout);
        score = findViewById(R.id.score);
        btnPause = findViewById(R.id.btnPause);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();

        mGameView = new GameView(this, this);
        btnTopScore.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, TopScoreActivity.class);
            startActivity(intent);
        });
        btnPause.setVisibility(View.GONE);
    }

    public void newGame(View view) {
        if (mGameView != null) {
            rootLayout.removeView(mGameView);
            mGameView = null;
        }
        mGameView = new GameView(this, this);
        mGameView.setBackgroundResource(R.drawable.road);
        btnStart.setVisibility(View.GONE);
        btnTopScore.setVisibility(View.GONE);
        score.setVisibility(View.GONE);
        btnPause.setVisibility(View.VISIBLE);
        rootLayout.addView(mGameView);
        music = MediaPlayer.create(MainActivity.this, R.raw.sou_main);
        music.setLooping(true);
        music.start();
        resumeGame();
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void closeGame(int mScore) {
        score.setText("Score: " + mScore);
        try {
            String filename = "score.txt";
            File file = new File(getFilesDir(), filename);
            FileWriter writer = new FileWriter(file, true);
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.getDefault());
            String formattedTime = dateFormat.format(calendar.getTime());
            writer.write(mScore + " at " + formattedTime + "\n");
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        rootLayout.removeView(mGameView);
        btnStart.setVisibility(View.VISIBLE);
        btnTopScore.setVisibility(View.VISIBLE);
        score.setVisibility(View.VISIBLE);
        btnPause.setVisibility(View.GONE);
        if (music != null) {
            music.stop();
            music.release();
            music = null;
        }
        music2 = MediaPlayer.create(MainActivity.this, R.raw.sou_collision);
        music2.setLooping(false);
        music2.start();

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (music2 != null) {
            music2.stop();
            music2.release();
            music2 = null;
        }
    }
    public void PauseGame(View view) {

        if (!isGameRunning) {
            pauseGame();
            music.pause();
        }

        else {
            resumeGame();
            music.start();
        }
    }


    public void pauseGame() {
        isGameRunning = true;
        Timer timer = new Timer();
        timer.cancel();
        timer = null;
        btnPause.setImageDrawable(getResources().getDrawable(R.drawable.play));
        Toast.makeText(MainActivity.this, "Game Paused", Toast.LENGTH_SHORT).show();
        mGameView.setVisibility(View.GONE);
//        rootLayout.removeView(mGameView);
        btnStart.setVisibility(View.VISIBLE);
        score.setVisibility(View.VISIBLE);
        btnTopScore.setVisibility(View.VISIBLE);
        int currentScore = mGameView.getScore();
        score.setText("Score: "+currentScore);


    }
    public void resumeGame() {
        isGameRunning = false;
        Toast.makeText(MainActivity.this, "Game Resume", Toast.LENGTH_SHORT).show();
        btnPause.setImageDrawable(getResources().getDrawable(R.drawable.pause));
        mGameView.setVisibility(View.VISIBLE);
//        rootLayout.addView(mGameView);
        btnStart.setVisibility(View.GONE);
        score.setVisibility(View.GONE);
        btnTopScore.setVisibility(View.GONE);


    }


}