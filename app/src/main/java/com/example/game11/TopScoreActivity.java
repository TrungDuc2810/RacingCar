package com.example.game11;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TopScoreActivity extends AppCompatActivity {
    private ListView lvTopScore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_score);
        Button btnExit = findViewById(R.id.btnExit);
        lvTopScore = findViewById(R.id.lvTopScore);
        loadAndDisplayScores();
        btnExit.setOnClickListener(v -> finish());
    }
    private void loadAndDisplayScores() {
        ArrayList<String> scoreList = new ArrayList<>();
        List<String> limitedDataList = scoreList.subList(0, Math.min(scoreList.size(), 10));
        try {
            String filename = "score.txt";
            File file = new File(getFilesDir(), filename);
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                limitedDataList.add(line);
            }
            bufferedReader.close();
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Collections.sort(limitedDataList, (s1, s2) -> {
            int score1 = Integer.parseInt(s1.split(" ")[0]);
            int score2 = Integer.parseInt(s2.split(" ")[0]);
            return Integer.compare(score2, score1);
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, limitedDataList);
        lvTopScore.setAdapter(adapter);
    }
}