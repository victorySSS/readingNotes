package com.crowd.diary.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.crowd.diary.R;
import com.crowd.diary.entity.Diary;
import com.crowd.diary.util.Configure;

public class ShowDiaryActivity extends AppCompatActivity {

    private Diary diary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_diary);
        initData();
        initView();
    }

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        diary = (Diary) bundle.getSerializable("diary");
    }

    private void initView() {
        if (diary == null) {
            return;
        }
        TextView showTitle = (TextView) findViewById(R.id.show_title);
        showTitle.setText(diary.getTitle());
//        TextView showAddress = (TextView) findViewById(R.id.show_address);
//        showAddress.setText(diary.getAddress());
//        TextView showDate = (TextView) findViewById(R.id.show_date);
//        showDate.setText(diary.getDate());
        TextView showTextContent = (TextView) findViewById(R.id.show_text_content);
        showTextContent.setText(diary.getContent());
        TextView showTextComment = (TextView) findViewById(R.id.show_text_comment);
        showTextComment.setText(diary.getNote());
        Button goBackButton = (Button) findViewById(R.id.go_to_list);
        goBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goMainActivity();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            goMainActivity();
        }
        return true;
    }

    private void goMainActivity() {
        Intent intent = new Intent(ShowDiaryActivity.this, MainActivity.class);
        intent.putExtra("from", Configure.FROM_SHOW_DIARY_ACTIVITY);
        startActivity(intent);
        finish();
    }
}