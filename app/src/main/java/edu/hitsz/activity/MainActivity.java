package edu.hitsz.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import edu.hitsz.R;
import edu.hitsz.application.ImageManager;
import edu.hitsz.application.MusicService;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    public static int screenWidth;
    public static int screenHeight;

    public static boolean isOnline = false;
    private SwitchCompat swMusic;
    private Button btnOffline;
    private Button btnOnline;

    public static MusicService.MyBinder myBinder;
    private MainActivity.Connect conn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityManager.getIns().add(this);

        // 初始化屏幕参数
        getScreenHW();

        // 初始化按钮
        swMusic = findViewById(R.id.music_switch);
        btnOffline = findViewById(R.id.offline);
        btnOnline = findViewById(R.id.online);

        // 初始化图片资源
        ImageManager.initImage(getResources());

        // 按钮监听
        btnOffline.setOnClickListener( view -> {
            checkMusic();
            Intent intent = new Intent(this, OfflineActivity.class);
            startActivity(intent);
        });

        btnOnline.setOnClickListener( view -> {
            isOnline = true;
            checkMusic();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        });
    }

    public void getScreenHW(){
        //定义DisplayMetrics 对象
        DisplayMetrics dm = new DisplayMetrics();
        //取得窗口属性
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        //窗口的宽度
        screenWidth= dm.widthPixels;
        //窗口高度
        screenHeight = dm.heightPixels;

        Log.i(TAG, "screenWidth : " + screenWidth + " screenHeight : " + screenHeight);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    private void checkMusic(){
        if(swMusic.isChecked()){
            conn = new Connect();
            Intent intent = new Intent(this, MusicService.class);

            // 将 Intent 对象和 Connect 对象作为参数传递进去，
            // 并且指定 Context.BIND_AUTO_CREATE 标志。这个标志表示如果服务不存在，则会自动创建。
            // 这样就建立了 Activity 和 Service 的连接。
            bindService(intent, conn, Context.BIND_AUTO_CREATE);
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        unbindService(conn);
    }

    static class Connect implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service){
            myBinder = (MusicService.MyBinder)service;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            //在服务异常终止时被调用。在这个方法中，我们可以释放所有与服务相关的资源。
        }
    }
}