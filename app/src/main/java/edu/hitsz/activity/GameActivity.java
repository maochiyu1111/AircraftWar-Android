package edu.hitsz.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import edu.hitsz.R;

import androidx.appcompat.app.AppCompatActivity;

import edu.hitsz.game.Game;
import edu.hitsz.game.EasyGame;
import edu.hitsz.game.HardGame;
import edu.hitsz.game.MediumGame;

public class GameActivity extends AppCompatActivity {

    private Handler handler;
    private static final String TAG = "GameActivity";
    private Game game;
    boolean musicOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        switch(OfflineActivity.gameMode){
            case EASY:
                game = new EasyGame(this, handler);
                break;
            case MEDIUM:
                game = new MediumGame(this, handler);
                break;
            case HARD:
                game = new HardGame(this ,handler);
                break;
            default:
                break;
        }

        musicOn=getIntent().getBooleanExtra("playMusic", false);
        game.setPlayMusic(musicOn);
        setContentView(game);


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (musicOn){
            MainActivity.myBinder.stopBgm();
        }
    }
}