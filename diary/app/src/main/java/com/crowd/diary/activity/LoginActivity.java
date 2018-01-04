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
import com.crowd.diary.entity.Diary;
import com.crowd.diary.fragment.WriteDiaryFragment;
import com.crowd.diary.util.Configure;
import com.crowd.diary.internet.Communicate;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText nameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private SharedPreferences sharedPreferences;
    private Button linktoRegisterScreenButton;
    private Diary diary;
    private int result;
    private int userID;


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
                                Communicate communicate = new Communicate();
                                result = communicate.loginToServer(loginName,loginPassword);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    conLogin.start();

                    while (conLogin.getState()!= Thread.State.TERMINATED);
                    //nameEditText.setText("get:"+content);
                    if(result==Communicate.NOTCONNECT){
                        Toast.makeText(this,
                                "网络未连接!",
                                Toast.LENGTH_SHORT).show();
                    }else
                        if(result==Communicate.PSWDERR){
                        Toast.makeText(this,
                                "用户名或密码错误!",
                                Toast.LENGTH_SHORT).show();
                    }else
                        if(result==Communicate.NOTEXIST){
                            Toast.makeText(this,
                                    "用户名不存在！",
                                    Toast.LENGTH_SHORT).show();
                    }
                    else{
                            userID=result;

                            Intent intent1 = new Intent(this, WriteDiaryFragment.class);
                            intent1.putExtra("userId",userID);

                            Toast.makeText(this,
                                    "验证成功!",
                                    Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(this, MainActivity.class);
                            intent.putExtra("from", Configure.FROM_LOGIN_ACTIVITY);
                            intent.putExtra("userId",userID);
                            startActivity(intent);
                            this.finish();
                        }
//                        if ("Login success.".equals(result)) {
//                            Toast.makeText(this,
//                                    "验证成功!",
//                                    Toast.LENGTH_SHORT).show();
//                            Intent intent = new Intent(this, MainActivity.class);
//                            intent.putExtra("from", Configure.FROM_LOGIN_ACTIVITY);
//                            startActivity(intent);
//                            this.finish();
//                            break;
//                        } else
//                            if("Password error.".equals(result)) {
//                                Toast.makeText(this,
//                                        "验证失败!",
//                                        Toast.LENGTH_SHORT).show();
//                            }
//                            else
//                                if("User not exist.".equals(result)){
//                                    Toast.makeText(this,
//                                            "用户名不存在！",
//                                            Toast.LENGTH_SHORT).show();
//                            }
//                  //  }
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