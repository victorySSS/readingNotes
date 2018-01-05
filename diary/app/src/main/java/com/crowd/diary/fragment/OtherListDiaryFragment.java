package com.crowd.diary.fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.crowd.diary.R;
//import com.crowd.diary.activity.LoginActivity;
import com.crowd.diary.activity.MainActivity;
import com.crowd.diary.activity.ShowDiaryActivity;
import com.crowd.diary.database.DiaryDao;
import com.crowd.diary.database.OpenHelper;
import com.crowd.diary.entity.Diary;
import com.crowd.diary.internet.Communicate;
//import com.crowd.diary.internet.JSONParse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.json.JSONException;

public class OtherListDiaryFragment extends Fragment implements AdapterView.OnItemLongClickListener {
    private static AppCompatActivity activity;
    private List<Map<String, String>> dataList;
    private SimpleAdapter simpleAdapter;
    private OpenHelper openHelper;
    private List<Diary> diaryList;
    private String result;
    private Diary diary;
    private int userID;

//    private int times=1;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        userID = Integer.parseInt(bundle.getString("userId"));
        if (saveToSQLte()) {
            initData();
        }
        View view = inflater.inflate(R.layout.list_diary, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        ListView listView = (ListView) view.findViewById(R.id.diary_item);
        simpleAdapter = new SimpleAdapter(
                activity,
                dataList,
                R.layout.diary_item,
                new String[]{"title", "date"},
                new int[]{R.id.item_title, R.id.item_date});
        listView.setAdapter(simpleAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(activity, ShowDiaryActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("diary", diaryList.get(position));
                intent.putExtras(bundle);
                activity.startActivity(intent);
                activity.finish();
            }
        });

        listView.setOnItemLongClickListener(this);
    }

    private void initData() {
//        Thread conLogin = new Thread() {
//            public void run() {
//                try {
//                    Communicate communicate = new Communicate();
//                    result = communicate.getSelfNotesFromServer(20,5);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        };
//        conLogin.start();
//
//        try{
//                    JSONObject jo = new JSONObject(result);
//                    System.out.println(jo.get("text"));
//
//                }catch(JSONException ex){
//                    ex.printStackTrace();
//                }
//                System.out.println(result);
////
//        diary=new Diary();
//        diary.setUserId(userID);
//        try{
//            JSONObject jsonObject = new JSONObject(result);
//            int userId = jsonObject.getInt("userId");
//            diary.setUserId(userId);
//            String bookName = jsonObject.getString("bookName");
//            diary.setTitle(bookName);
//            String text = jsonObject.getString("text");
//            diary.setContent(text);
//            String note = jsonObject.getString("note");
//            diary.setNote(note);
//        }catch (JSONException e){
//            e.printStackTrace();
//        }

        openHelper = new OpenHelper(activity);
        SQLiteDatabase sqLiteDatabase = openHelper.getReadableDatabase();
        DiaryDao diaryDao = new DiaryDao(sqLiteDatabase);
//        boolean flag = diaryDao.insert(diary);
        diaryList = diaryDao.queryAll();
        sqLiteDatabase.close();


        dataList = new ArrayList<>();
        for (Diary diary : diaryList) {
            if(diary.getUserId()!=userID){
                Map<String, String> map = new HashMap<>();
                map.put("title", diary.getTitle());
//            map.put("date", diary.getDate());
                dataList.add(map);
            }
        }
//        dataList = new ArrayList<>();
//        Map<String,String> map = new HashMap<>();
//        for (int i = 0; i < 5; i++) {
//            Thread conLogin = new Thread() {
//                public void run() {
//                    try {
//                        Communicate communicate = new Communicate();
//                        result = communicate.getOtherNotesFromServer(2);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            };
//            conLogin.start();
//            try {
//                JSONObject jsonObject = new JSONObject(result);
//                map.put("title",jsonObject.getString("Note"));
//                map.put("date",jsonObject.getString("Note"));
//                dataList.add(map);
//            }catch (JSONException e){
//                e.printStackTrace();
//            }
//
//        }


    }

    public static OtherListDiaryFragment newInstance(MainActivity activity) {
        OtherListDiaryFragment.activity = activity;

        OtherListDiaryFragment fragment = new OtherListDiaryFragment();
        return fragment;
    }


    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        final int location = position;
        new AlertDialog.Builder(activity)
                .setTitle("警告")
                .setMessage("确定删除此条数据吗？")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SQLiteDatabase sqLiteDatabase = openHelper.getReadableDatabase();
                        DiaryDao diaryDao = new DiaryDao(sqLiteDatabase);
                        diaryDao.delete(diaryList.get(location));
                        diaryList.remove(location);
                        sqLiteDatabase.close();
                        dataList.remove(location);
                        simpleAdapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton("否", null)
                .show();
        return true;
    }

    //    private boolean saveToSQLite() {
//        diary = new Diary();
//        diary.setUserId(userID);
//        diary.setContent(textContent.getText().toString());
//        diary.setNote(textNote.getText().toString());
//        diary.setTitle(title.getText().toString());
////        if (title.getText().toString() == null || "".equals(title.getText().toString())) {
////            diary.setTitle("无题");
////        } else {
////            diary.setTitle(title.getText().toString());
////        }
////        diary.setDate(date.getText().toString());
////        diary.setAddress(address.getText().toString());
////        diary.setAuthor(activity.getSharedPreferences("user",
////                Context.MODE_PRIVATE).getString("name", ""));
////        diary.setUri(uriList);
//
//
//        OpenHelper openHelper = new OpenHelper(activity);
//        SQLiteDatabase sqLiteDatabase = openHelper.getReadableDatabase();
//        DiaryDao diaryDao = new DiaryDao(sqLiteDatabase);
//        boolean flag = diaryDao.insert(diary);
//        sqLiteDatabase.close();
//        return flag;
//    }
    private boolean saveToSQLte() {
        boolean flag = true;
        for (int i = 1; i < 10; i++) {
            final int s = i;
            Thread conLogin = new Thread() {
                public void run() {
                    try {
                        Communicate communicate = new Communicate();
                        result = communicate.getOtherNotesFromServer(s);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };
            conLogin.start();
            while (conLogin.getState() != Thread.State.TERMINATED) ;
//
//        try{
//                    JSONObject jo = new JSONObject(result);
//                    System.out.println(jo.get("text"));
//
//                }catch(JSONException ex){
//                    ex.printStackTrace();
//                }
//                System.out.println(result);
////
            diary = new Diary();
//            diary.setUserId(userID);
            try {
                JSONObject jsonObject = new JSONObject(result);
                String userId = jsonObject.getString("userID");
                int user_Id = Integer.parseInt(userId);
                diary.setUserId(user_Id);
                String bookName = jsonObject.getString("bookName");
                diary.setTitle(bookName);
                String text = jsonObject.getString("text");
                diary.setContent(text);
                String note = jsonObject.getString("note");
                diary.setNote(note);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            openHelper = new OpenHelper(activity);
            SQLiteDatabase sqLiteDatabase = openHelper.getReadableDatabase();
            DiaryDao diaryDao = new DiaryDao(sqLiteDatabase);
            flag = diaryDao.insert(diary);
//            diaryList = diaryDao.queryAll();
            sqLiteDatabase.close();
        }
        return flag;
    }

//    private boolean clearData() {
//        openHelper = new OpenHelper(activity);
//        SQLiteDatabase sqLiteDatabase = openHelper.getReadableDatabase();
//        DiaryDao diaryDao = new DiaryDao(sqLiteDatabase);
////        boolean flag = diaryDao.insert(diary);
//        diaryList = diaryDao.queryAll();
//        sqLiteDatabase.close();
//
//
////        dataList = new ArrayList<>();
//        for (Diary diary : diaryList) {
//            diaryDao.delete(diary);
//        }
//    }
}