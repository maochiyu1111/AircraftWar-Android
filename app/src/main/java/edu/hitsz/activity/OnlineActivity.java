package edu.hitsz.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import edu.hitsz.R;
import edu.hitsz.game.Game;
import edu.hitsz.game.HardGame;

public class OnlineActivity extends AppCompatActivity {

    private Socket socket;
    private PrintWriter writer;
    private BufferedReader reader;
    private Handler handler;

    private int opponentScore;

    private String getscores;
    private Game game;


    private int selfScore;

    private static boolean gameOverFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online);
        Log.i("socket", "测试");

        handler = new Handler(getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 2 ) {
                    selfScore = (int) msg.obj;
                    Log.i("socket", "游戏分数返回");

                } else if (msg.what == 1 && msg.obj.equals("start")) {
                    game = new HardGame(OnlineActivity.this, handler);
                    game.setPlayMusic(MainActivity.myBinder != null);

                    setContentView(game);

                    // 给服务端发送信息
                    new Thread(() -> {
                        while (!game.isGameOverFlag()) {
                            writer.println(game.getScore());
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                        Message msgForWaiting = new Message();
                        msgForWaiting.what = 1;
                        msgForWaiting.obj = "waiting";
                        handler.sendMessage(msgForWaiting);
                        writer.println("end");

                    }).start();
                } else if(msg.what == 1 && msg.obj.equals("waiting")) {
                    setContentView(R.layout.online_waiting);
                } else {
                    getscores = (String) msg.obj;
                    Log.i("socket", "已收到两玩家分数");
                    String[] scores = getscores.split(",");
                    int score1 = Integer.parseInt(scores[0]);
                    int score2 = Integer.parseInt(scores[1]);
                    if (score1 == selfScore) {
                        opponentScore = score2;
                    } else {
                        opponentScore = score1;
                    }

                    Log.i("socket", ""+selfScore);
                    Log.i("socket", ""+opponentScore);
                    Intent intent = new Intent(OnlineActivity.this, ResultActivity.class);
                    intent.putExtra("selfScore",selfScore);
                    intent.putExtra("opponentScore",opponentScore);
                    startActivity(intent);

                }
            }
        };

        new Thread(new NetConn(handler)).start();

    }

    private class NetConn extends Thread {

        private final Handler handler;

        public NetConn(Handler handler) {
            this.handler = handler;
        }

        @Override
        public void run() {
            try {
                socket = new Socket();
                socket.connect(new InetSocketAddress("10.250.25.106", 6666), 5000);
                Log.i("socket", "已发起连接请求");
                writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                        socket.getOutputStream(), StandardCharsets.UTF_8
                )), true);
                reader = new BufferedReader(new InputStreamReader(
                        socket.getInputStream(), StandardCharsets.UTF_8
                ));

                // 接收服务端信息
                new Thread(() -> {
                    String msg;
                    try{
                        while ((msg = reader.readLine()) != null){
                            Message msgFromServer = new Message();
                            msgFromServer.what = 1;
                            msgFromServer.obj = msg;
                            handler.sendMessage(msgFromServer);
                        }
                    } catch(IOException e){
                        e.printStackTrace();
                    }
                }).start();


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public int getOpponentScore() {
        return opponentScore;
    }

    public int getSelfScore() {
        return selfScore;
    }

}

