package com.crowd.diary.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{
    private Button registerButton;
    private Button linkToLoginScreenButton;
    private EditText registerNameEditText;
    private EditText registerPasswordEditText;

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private String content;
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
                                socket = new Socket("123.207.97.94", 8888);
                                in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
                                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                                        socket.getOutputStream())), true);
                                out.write("login\n");

                                out.write(registerName+"\n");
                                //out.write("perfect\n");
                                out.write(registerPassword+"\n");
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
                        Intent intent1 = new Intent(this, LoginActivity.class);
                        intent1.putExtra("from", Configure.FROM_LOGIN_ACTIVITY);
                        startActivity(intent1);
                        this.finish();
                        break;
                    } else {
                        Toast.makeText(this,
                                "验证失败!",
                                Toast.LENGTH_SHORT).show();
                    }
                    //  }
                break;
                }
        }
    }
}
