package edu.hitsz.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import edu.hitsz.R;

public class ResultActivity extends AppCompatActivity {
    private int selfScore = 0;
    private int opponentScore = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        TextView self_score = findViewById(R.id.self_score);
        TextView opponent_core = findViewById(R.id.opponent_score);
        TextView results = findViewById(R.id.results);
        Button returnBtn = findViewById(R.id.return_button);

        selfScore = getIntent().getIntExtra("selfScore" , 0);
        opponentScore = getIntent().getIntExtra("opponentScore" , 0);
        self_score.setText("你的分数是"+selfScore);
        opponent_core.setText("对手的分数是"+opponentScore);
        if(opponentScore > selfScore){
            results.setText("很遗憾，你输了");
        } else if(opponentScore < selfScore) {
            results.setText("恭喜你，你赢了");
        } else {
            results.setText("哇，你俩这都能平局");
        }


        returnBtn.setOnClickListener(v -> {
                Intent intent = new Intent(ResultActivity.this, MainActivity.class);
                startActivity(intent);
        });

    }
}
