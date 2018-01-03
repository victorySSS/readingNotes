package com.crowd.diary.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.crowd.diary.R;
import com.crowd.diary.internet.Communicate;
import com.crowd.diary.util.Configure;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{
    private Button registerButton;
    private Button linkToLoginScreenButton;
    private EditText registerNameEditText;
    private EditText registerPasswordEditText;

    private String result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
    }

    private void initView()
    {
        linkToLoginScreenButton = (Button)findViewById(R.id.linkToLoginScreenButton);
        linkToLoginScreenButton.setOnClickListener(this);
        registerButton = (Button)findViewById(R.id.registerButton);
        registerButton.setOnClickListener(this);
        registerNameEditText = (EditText)findViewById(R.id.nameInput);
        registerPasswordEditText = (EditText)findViewById(R.id.passwordInput);
    }

    public void onClick(View v){
        switch(v.getId()){
            case R.id.linkToLoginScreenButton:
                Intent intent = new Intent(this,LoginActivity.class);
                startActivity(intent);
                this.finish();
            case R.id.registerButton:
                final String registerName = registerNameEditText.getText().toString();
                final String registerPassword = registerPasswordEditText.getText().toString();
                if("".equals(registerName)||"".equals(registerPassword)){
                    Toast.makeText(this,"请输入用户名或密码！",Toast.LENGTH_SHORT).show();
                }else{
                    Thread conLogin = new Thread(){
                        public void run(){
                            try {
                                Communicate communicate = new Communicate();
                                result = communicate.registerToServer(registerName,registerPassword);

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    conLogin.start();

                    while (conLogin.getState()!= Thread.State.TERMINATED);
                    //nameEditText.setText("get:"+content);

                    if ("Register success.".equals(result)) {
                        Toast.makeText(this,
                                "注册成功!",
                                Toast.LENGTH_SHORT).show();
                        Intent intent1 = new Intent(this, LoginActivity.class);
                        intent1.putExtra("from", Configure.FROM_LOGIN_ACTIVITY);
                        startActivity(intent1);
                        this.finish();
                        break;
                    } else
                        if("Username existed, register failed.".equals(result)){
                        Toast.makeText(this,
                                "用户名已存在，注册失败!",
                                Toast.LENGTH_SHORT).show();
                    }
                    //  }
                break;
                }
        }
    }
}
