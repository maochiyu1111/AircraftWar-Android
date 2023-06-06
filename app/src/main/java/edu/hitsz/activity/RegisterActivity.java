package edu.hitsz.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import edu.hitsz.R;

public class RegisterActivity extends AppCompatActivity {

    private final static String TAG = "RegisterActivity";

    private Button register;
    private EditText registerID;
    private EditText registerPassword;
    private EditText registerConfirmPassword;

    private String userID;
    private String userPassword;
    private String userConfirmPassword;

    private Handler handler;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        register = findViewById(R.id.register_btn);
        registerID = findViewById(R.id.register_ID);
        registerPassword = findViewById(R.id.register_password);
        registerConfirmPassword = findViewById(R.id.confirm_password);

        registerID.setTextColor(Color.WHITE);
        registerPassword.setTextColor(Color.WHITE);
        registerConfirmPassword.setTextColor(Color.WHITE);

        register.setOnClickListener(v -> {
            if (v.getId() == R.id.register_btn) {
                userID = registerID.getText().toString();
                userPassword = registerPassword.getText().toString();
                userConfirmPassword = registerConfirmPassword.getText().toString();

                //判断两次输入是否相同
                if (!userPassword.equals(userConfirmPassword)) {
                    Toast.makeText(RegisterActivity.this, "两次密码输入不同，请重新输入", Toast.LENGTH_LONG).show();
                } else {
                    //通过新线程连接socket
                    Toast.makeText(RegisterActivity.this, "注册成功",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                }
            }
        });

    }

}






