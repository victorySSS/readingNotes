package com.crowd.diary.activity;

import android.content.Intent;
//import android.graphics.Bitmap;
//import android.net.Uri;
//import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
//import android.widget.ImageView;
import android.widget.TextView;

import com.crowd.diary.R;
import com.crowd.diary.entity.Diary;
import com.crowd.diary.util.Configure;

//import java.io.File;
//import java.io.IOException;

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
        TextView showAddress = (TextView) findViewById(R.id.show_address);
        showAddress.setText(diary.getAddress());
        TextView showDate = (TextView) findViewById(R.id.show_date);
        showDate.setText(diary.getDate());
        TextView showTextContent = (TextView) findViewById(R.id.show_text_content);
        showTextContent.setText(diary.getContent());
        TextView showTextComment = (TextView) findViewById(R.id.show_text_comment);
        showTextComment.setText(diary.getNote());

//        ImageView image1 = (ImageView) findViewById(R.id.show_image1);
//        ImageView image2 = (ImageView) findViewById(R.id.show_image2);
//        ImageView image3 = (ImageView) findViewById(R.id.show_image3);
//        ImageView image4 = (ImageView) findViewById(R.id.show_image4);
//        ImageView[] imageViews = new ImageView[]{image1, image2, image3, image4};
//
//        String uris = diary.getUri();
//        if (uris != null && !"".equals(uris)) {
//            String[] uriArr = uris.split(";");
//            for (int i = 0; i < uriArr.length; i++) {
//                Bitmap bitmap = null;
//                try {
//                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.fromFile(new File(uriArr[i])));
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                imageViews[i].setImageBitmap(bitmap);
//                imageViews[i].setVisibility(View.VISIBLE);
//            }
//        }

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