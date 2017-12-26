package com.crowd.diary.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.crowd.diary.R;
import com.crowd.diary.util.Configure;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText nameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private SharedPreferences sharedPreferences;
    private Button linktoRegisterScreenButton;

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private String content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initData();
    }

    private void initView() {
        nameEditText = (EditText) findViewById(R.id.username);
        passwordEditText = (EditText) findViewById(R.id.password);
        loginButton = (Button) findViewById(R.id.login);
        loginButton.setOnClickListener(this);
        linktoRegisterScreenButton = (Button)findViewById(R.id.linkToRegisterScreenButton);
        linktoRegisterScreenButton.setOnClickListener(this);
    }

    private void initData() {
        sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        String name = sharedPreferences.getString("name", "");
        String address = sharedPreferences.getString("password", "");
        if (!name.equals("") && address.equals("")) {
            nameEditText.setText(name);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login:
                final String loginName = nameEditText.getText().toString();
                final String loginPassword = passwordEditText.getText().toString();
                if ("".equals(loginName) || "".equals(loginPassword)) {
                    //Log.v("输出","\nkk");
                    Toast.makeText(this,"请输入用户名或密码！",Toast.LENGTH_SHORT).show();
                    break;
                }
                else {

                    Thread conLogin = new Thread(){
                        public void run(){
                            try {
                                socket = new Socket("123.207.97.94", 8888);
                                in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
                                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                                        socket.getOutputStream())), true);
                                out.write("login\n");

                                out.write(loginName+"\n");
                                //out.write("perfect\n");
                                out.write(loginPassword+"\n");
                                out.flush();

                                while (true) {
                                    if (socket.isConnected()) {
                                        //Log.v("connect","OK");
                                        if (!socket.isInputShutdown()) {
                                            //Log.v("stream:","OK");
                                            if ((content = in.readLine()) != null) {
                                               // Log.v("get:",content);
                                                //content += "\n";
                                                out.write("bye\n");
                                                out.flush();
                                                break;
                                            }
                                        }
                                    }
                                }

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    conLogin.start();

                    while (conLogin.getState()!= Thread.State.TERMINATED);
                    //nameEditText.setText("get:"+content);

                        if ("OK".equals(content)) {
                            Toast.makeText(this,
                                    "验证成功!",
                                    Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(this, MainActivity.class);
                            intent.putExtra("from", Configure.FROM_LOGIN_ACTIVITY);
                            startActivity(intent);
                            this.finish();
                            break;
                        } else {
                            Toast.makeText(this,
                                    "验证失败!",
                                    Toast.LENGTH_SHORT).show();
                        }
                  //  }
                }
                break;
            case R.id.linkToRegisterScreenButton:
                Intent intent = new Intent(this, RegisterActivity.class);
                intent.putExtra("from", Configure.FROM_LOGIN_ACTIVITY);
                startActivity(intent);
                this.finish();
        }
    }


}