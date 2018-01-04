package com.crowd.diary.fragment;

//import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
//import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
//import android.graphics.Bitmap;
//import android.net.Uri;
//import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
//import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
//import android.widget.Toast;

import com.crowd.diary.R;
import com.crowd.diary.activity.MainActivity;
import com.crowd.diary.activity.ShowDiaryActivity;
import com.crowd.diary.database.DiaryDao;
import com.crowd.diary.database.OpenHelper;
import com.crowd.diary.entity.Diary;
import com.crowd.diary.util.Configure;
import com.crowd.diary.util.LocationUtil;
import com.crowd.diary.activity.LoginActivity;
import com.crowd.diary.internet.Communicate;
//import com.crowd.diary.util.Util;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WriteDiaryFragment extends Fragment implements View.OnClickListener {
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Configure.LOCATION_MESSAGE_CODE:
                    address.setText("于" + msg.obj);
                    break;
            }
        }
    };
    private static AppCompatActivity activity;
    private LocationUtil locationUtil;
    private TextView address;
    private TextView date;
    private Button completeButton;
//    private ImageView addContent;
    private AlertDialog addContentDialog;
//    private AlertDialog addImageDialog;
    private EditText title;
    private EditText textContent;
    private EditText textNote;
//    private ImageView image1;
//    private ImageView image2;
//    private ImageView image3;
//    private ImageView image4;
//    private int imageCount = 0;
    private String uriList = "";
    private Diary diary;

    private int result;
    private int userID;
//    private int userId;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.write_diary, container, false);
        Bundle bundle=this.getArguments();
        userID=Integer.parseInt(bundle.getString("userId"));
        initView(view);
        return view;
    }

//    @Override
//    public void onAttach(Activity activity){
//        super.onAttach(activity);
//        userId=((MainActivity)activity).getUserId();
//    }

    @Override
    public void onResume() {
        super.onResume();
        initLocation();
    }

    private void initLocation() {
        locationUtil = new LocationUtil(activity, handler);
        locationUtil.getLocation(this);
    }

    public static WriteDiaryFragment newInstance(MainActivity activity) {
        WriteDiaryFragment.activity = activity;
        WriteDiaryFragment fragment = new WriteDiaryFragment();
        return fragment;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        locationUtil.removeLocationUpdates();
        locationUtil = null;
    }

    private void initView(View view) {
        address = (TextView) view.findViewById(R.id.address);
        date = (TextView) view.findViewById(R.id.date);
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        date.setText(dateString);

        title = (EditText) view.findViewById(R.id.title);
        textContent = (EditText) view.findViewById(R.id.text_content);
        textNote = (EditText) view.findViewById(R.id.text_note);
        completeButton = (Button)view.findViewById(R.id.complete);
        completeButton.setOnClickListener(this);
//        image1 = (ImageView) view.findViewById(R.id.image1);
//        image2 = (ImageView) view.findViewById(R.id.image2);
//        image3 = (ImageView) view.findViewById(R.id.image3);
//        image4 = (ImageView) view.findViewById(R.id.image4);

//        addContent = (ImageView) view.findViewById(R.id.add_content);
//        addContent.setTag(201);
//        addContent.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
//        Util util = new Util();
//        switch ((Integer) v.getTag()) {
//            case 201:
//                addContentDialog = util.getDialog(activity, selectTypeView());
//                addContentDialog.show();
//                break;
//            case 202:
//                addContentDialog.dismiss();
//                addImageDialog = util.getDialog(activity, selectWhichImageView());
//                addImageDialog.show();
//                break;
//            case 203:
//                addImageDialog.dismiss();
//                String[] cameraPermissions = new String[]{
//                        Manifest.permission.CAMERA};
//                boolean cameraPermissionFlag = util.checkPermission(cameraPermissions,
//                        activity);
//                if (cameraPermissionFlag) {
//                    WriteDiaryFragment.this.requestPermissions(cameraPermissions,
//                            Configure.CAMERA_PERMISSION_CODE);
//                } else {
//                    openCamera();
//                }
//                break;
//            case 204:
//                addImageDialog.dismiss();
//                String[] imagePermissions = new String[]{
//                        Manifest.permission.READ_EXTERNAL_STORAGE,
//                        Manifest.permission.WRITE_EXTERNAL_STORAGE};
//                boolean imagePermissionFlag = util.checkPermission(imagePermissions,
//                        activity);
//                if (imagePermissionFlag) {
//                    WriteDiaryFragment.this.requestPermissions(imagePermissions,
//                            Configure.IMAGE_PERMISSION_CODE);
//                } else {
//                    openImageFile();
//                }
//                break;
//            case 205:
//                addContentDialog.dismiss();
//        int result;
//        LoginActivity loginActivity = new LoginActivity();
//        Communicate communicate = new Communicate();
//        final String text = textNote.getText().toString();
//        final String boolName = title.getText().toString();
//        final String content = textContent.getText().toString();
//        try{
//            result = communicate.addNoteToServer(loginActivity.userID, text, boolName, content);
//        } catch(IOException e){
//            e.printStackTrace();
//        }
                if (saveToSQLite()) {
                    Intent intent = new Intent(activity, ShowDiaryActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("diary", diary);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    activity.finish();
                }
//                break;
//        }
    }

    private boolean saveToSQLite() {
        diary = new Diary();
        diary.setUserId(userID);
        diary.setContent(textContent.getText().toString());
        diary.setNote(textNote.getText().toString());
        diary.setTitle(title.getText().toString());
//        if (title.getText().toString() == null || "".equals(title.getText().toString())) {
//            diary.setTitle("无题");
//        } else {
//            diary.setTitle(title.getText().toString());
//        }
//        diary.setDate(date.getText().toString());
//        diary.setAddress(address.getText().toString());
//        diary.setAuthor(activity.getSharedPreferences("user",
//                Context.MODE_PRIVATE).getString("name", ""));
        diary.setUri(uriList);

//        final int userId = userID;
        final String text = textNote.getText().toString();
        final String bookName = title.getText().toString();
        final String content = textContent.getText().toString();

        Thread conLogin = new Thread(){
            public void run(){
                try {
                    Communicate communicate = new Communicate();
                    result = communicate.addNoteToServer(userID, text, bookName, content);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        conLogin.start();
//        try{
//            int b=1;
//            Communicate communicate = new Communicate();
//            result = communicate.addNoteToServer(loginActivity.userID, text, boolName, content);
//            int a=1;
//        } catch(IOException e){
//            e.printStackTrace();
//        }

        OpenHelper openHelper = new OpenHelper(activity);
        SQLiteDatabase sqLiteDatabase = openHelper.getReadableDatabase();
        DiaryDao diaryDao = new DiaryDao(sqLiteDatabase);
        boolean flag = diaryDao.insert(diary);
        sqLiteDatabase.close();
        return flag;
    }
//    private boolean saveToSQLite() {
//        LoginActivity loginActivity=new LoginActivity();
//
//    diary = new Diary();
//    diary.setContent(textContent.getText().toString());
//    if (title.getText().toString() == null || "".equals(title.getText().toString())) {
//        diary.setTitle("无题");
//    } else {
//        diary.setTitle(title.getText().toString());
//    }
////    diary.setDate(date.getText().toString());
////    diary.setAddress(address.getText().toString());
//    diary.setAuthor(activity.getSharedPreferences("user",
//            Context.MODE_PRIVATE).getString("name", ""));
//    diary.setUri(uriList);
//    OpenHelper openHelper = new OpenHelper(activity);
//    SQLiteDatabase sqLiteDatabase = openHelper.getReadableDatabase();
//    DiaryDao diaryDao = new DiaryDao(sqLiteDatabase);
//    boolean flag = diaryDao.insert(diary);
//    sqLiteDatabase.close();
//    return flag;
//}

//    private void addImageToLinearLayout(Bitmap bitmap) {
//        switch (imageCount) {
//            case 0:
//                image1.setVisibility(View.VISIBLE);
//                image1.setImageBitmap(bitmap);
//                imageCount++;
//                break;
//            case 1:
//                image2.setVisibility(View.VISIBLE);
//                image2.setImageBitmap(bitmap);
//                imageCount++;
//                break;
//            case 2:
//                image3.setVisibility(View.VISIBLE);
//                image3.setImageBitmap(bitmap);
//                imageCount++;
//                break;
//            case 3:
//                image4.setVisibility(View.VISIBLE);
//                image4.setImageBitmap(bitmap);
//                imageCount++;
//                break;
//        }
//    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//            Toast.makeText(activity, "获取权限成功",
//                    Toast.LENGTH_SHORT).show();
//            switch (requestCode) {
//                case Configure.LOCATION_PERMISSION_CODE:
//                    locationUtil.getLocation(this);
//                    break;
//                case Configure.CAMERA_PERMISSION_CODE:
//                    openCamera();
//                    break;
//                case Configure.IMAGE_PERMISSION_CODE:
//                    openImageFile();
//                    break;
//            }
//        } else {
//            Toast.makeText(activity, "获取权限失败",
//                    Toast.LENGTH_SHORT).show();
//        }
//    }

//    private void openImageFile() {
//        Intent intent = new Intent();
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//        intent.setType("image/*");
//        if (Build.VERSION.SDK_INT < 19) {
//            intent.setAction(Intent.ACTION_GET_CONTENT);
//        } else {
//            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
//        }
//        startActivityForResult(intent, Configure.IMAGE_ALBUM);
//    }
//
//    private void openCamera() {
//        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
//        startActivityForResult(intent, Configure.IMAGE_CAMERA);
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == activity.RESULT_OK) {
//            switch (requestCode) {
//                case Configure.IMAGE_CAMERA:
//                    setBitmap(data, Configure.IMAGE_CAMERA);
//                    break;
//                case Configure.IMAGE_ALBUM:
//                    setBitmap(data, Configure.IMAGE_ALBUM);
//                    break;
//            }
//        }
//    }
//
//    private void setBitmap(Intent data, int from) {
//        Uri uri = data.getData();
//        Bitmap bitmap = null;
//        if (uri == null) {
//            Bundle bundle = data.getExtras();
//            if (bundle != null) {
//                bitmap = (Bitmap) bundle.get("data");
//            }
//        } else {
//            try {
//                bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), uri);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        try {
//            bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), saveBitmap(bitmap));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        addImageToLinearLayout(bitmap);
//    }
//
//    private Uri saveBitmap(Bitmap bitmap) {
//        File f = new File(getContext().getFilesDir(), System.currentTimeMillis() + ".png");
//        if (f.exists()) {
//            f.delete();
//        }
//        try {
//            FileOutputStream out = new FileOutputStream(f);
//            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
//            out.flush();
//            out.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        uriList += f.getPath() + ";";
//        return Uri.fromFile(f);
//    }

//    private View selectTypeView() {
//        LinearLayout typeLayout = new LinearLayout(activity);
//        typeLayout.setOrientation(LinearLayout.HORIZONTAL);
//        typeLayout.setGravity(Gravity.CENTER);
//
////        if (imageCount < 4) {
////            Button iamgeButton = new Button(activity);
////            iamgeButton.setText("插入图片");
////            iamgeButton.setTag(202);
////            iamgeButton.setOnClickListener(this);
////            typeLayout.addView(iamgeButton);
////        }
//
//        Button complete = new Button(activity);
//        complete.setText("完成笔记");
//        complete.setTag(205);
//        complete.setOnClickListener(this);
//        typeLayout.addView(complete);
//        return typeLayout;
//    }


//    private View selectWhichImageView() {
//        LinearLayout typeLayout = new LinearLayout(activity);
//        typeLayout.setOrientation(LinearLayout.HORIZONTAL);
//        typeLayout.setGravity(Gravity.CENTER);
//
//        Button textButton = new Button(activity);
//        textButton.setText("从相机获取");
//        textButton.setTag(203);
//        textButton.setOnClickListener(this);
//        Button iamgeButton = new Button(activity);
//        iamgeButton.setText("从相册获取");
//        iamgeButton.setTag(204);
//        iamgeButton.setOnClickListener(this);
//
//        typeLayout.addView(textButton);
//        typeLayout.addView(iamgeButton);
//        return typeLayout;
//    }
}