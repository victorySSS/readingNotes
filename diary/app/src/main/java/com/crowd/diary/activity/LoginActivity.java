package com.crowd.diary.activity;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crowd.diary.R;
import com.crowd.diary.util.Configure;
import com.crowd.diary.util.Util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText nameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private AlertDialog registerDialog;
    private EditText registerName;
    private EditText registerPassword;
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
        loginButton.setTag(101);
        loginButton.setOnClickListener(this);
        linktoRegisterScreenButton = (Button)findViewById(R.id.linkToRegisterScreenButton);
        linktoRegisterScreenButton.setTag(104);
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

    private void register() {
        registerDialog = new Util().getDialog(this, registerDialogView());
        registerDialog.show();
    }

    private View registerDialogView() {
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        TextView titleText = new TextView(this);
        titleText.setText("设置账号");
        titleText.setTextSize(20);
        titleText.setGravity(Gravity.CENTER);
        titleText.setPadding(0, 20, 0, 10);
        registerName = new EditText(this);
        registerName.setHint("请输入用户名");
        registerPassword = new EditText(this);
        registerPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        registerPassword.setHint("请输入密码");

        LinearLayout buttonLinearLayout = new LinearLayout(this);
        buttonLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        buttonLinearLayout.setGravity(Gravity.CENTER);

        Button registerButton = new Button(this);
        registerButton.setText("注册");
        registerButton.setOnClickListener(this);
        registerButton.setTag(102);
        Button cancelButton = new Button(this);
        cancelButton.setText("取消");
        cancelButton.setOnClickListener(this);
        cancelButton.setTag(103);

        buttonLinearLayout.addView(registerButton);
        buttonLinearLayout.addView(cancelButton);

        linearLayout.addView(titleText);
        linearLayout.addView(registerName);
        linearLayout.addView(registerPassword);
        linearLayout.addView(buttonLinearLayout);
        return linearLayout;
    }

    private void alertRegister(final String name, final String password) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("您正在设置账号，这个账号将保护您的隐私不被其他人看到。" +
                "现在请点击完成结束设置，或点击取消重新设置");
        builder.setPositiveButton("完成", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("name", name);
                //editor.putString("password", password);
                editor.commit();
                content=null;

                Thread conRegister = new Thread(){
                    public void run(){
                        try {
                            socket = new Socket("123.207.97.94", 8888);
                            in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
                            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                                    socket.getOutputStream())), true);
                            out.write("register\n");

                            out.write(name+"\n");
                            //out.write("perfect\n");
                            out.write(password+"\n");
                            out.flush();

                            while (true) {
                                if (socket.isConnected()) {
                                    //Log.v("connect","OK");
                                    if (!socket.isInputShutdown()) {
                                        //Log.v("stream:","OK");
                                        if ((content = in.readLine()) != null) {
                                            //Log.v("get:",content);
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
                conRegister.start();

                while (conRegister.getState()!=Thread.State.TERMINATED);
                if("OK".equals(content)){
                    registerDialog.dismiss();
                    nameEditText.setText(name);
                }
                else{
                    //用户名已被占用
                    builder.setMessage("用户名已存在，请重新设置！");
                    builder.show();
                }
            }
        });
        builder.setNegativeButton("取消", null);
        builder.show();
    }

    private void alertLoginError() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("您输入的用户名、密码验证不成功，是否需要设置用户名密码？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                register();
            }
        });
        builder.setNegativeButton("取消", null);
        builder.show();
    }

    @Override
    public void onClick(View v) {
        switch ((Integer) v.getTag()) {
            case 101:
                final String loginName = nameEditText.getText().toString();
                final String loginPassword = passwordEditText.getText().toString();
                if ("".equals(loginName) || "".equals(loginPassword)) {
                    //Log.v("输出","\nkk");
                    alertLoginError();
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
                            alertLoginError();
                        }
                  //  }
                }
                break;
            case 102:
                String name = registerName.getText().toString();
                String password = registerPassword.getText().toString();
                if ("".equals(name) || "".equals(password)) {
                    Toast.makeText(this,
                            "输入的值不能为空！",
                            Toast.LENGTH_LONG).show();
                    break;
                } else {
                    alertRegister(name, password);
                }
                break;
            case 103:
                Toast.makeText(this,
                        "您取消了设置账号，这有可能导致您的日记隐私出于不安全状态！",
                        Toast.LENGTH_LONG).show();
                registerDialog.dismiss();
                break;
            case 104:
                register();
                break;
        }
    }


}