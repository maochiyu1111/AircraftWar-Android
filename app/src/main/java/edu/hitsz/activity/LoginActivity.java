package edu.hitsz.activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import edu.hitsz.R;

public class LoginActivity extends AppCompatActivity {

    private final static String TAG = "LoginActivity";

    Button loginButton;
    Button registerButton;
    EditText login_ID;
    EditText login_password;

    private String user_ID;
    private String user_password;

    private PrintWriter out;
    private Socket socket;
    public Handler handler;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = findViewById(R.id.login_in_btn);
        registerButton = findViewById(R.id.register_btn);
        login_ID = findViewById(R.id.login_ID);
        login_password = findViewById(R.id.login_password);

        login_ID.setTextColor(Color.WHITE);
        login_password.setTextColor(Color.WHITE);

        //注册按钮
        registerButton.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        //登录按钮
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.login_in_btn) {
                    user_ID = login_ID.getText().toString();
                    user_password = login_password.getText().toString();
                    Intent startGame = new Intent(LoginActivity.this, OnlineActivity.class);
                    Toast.makeText(LoginActivity.this, "登陆成功",Toast.LENGTH_SHORT).show();
                    startActivity(startGame);
                }
            }
        });

    }
}