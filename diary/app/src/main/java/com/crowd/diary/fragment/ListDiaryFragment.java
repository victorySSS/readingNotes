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
import com.crowd.diary.activity.MainActivity;
import com.crowd.diary.activity.ShowDiaryActivity;
import com.crowd.diary.database.DiaryDao;
import com.crowd.diary.database.OpenHelper;
import com.crowd.diary.entity.Diary;
import com.crowd.diary.internet.Communicate;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListDiaryFragment extends Fragment implements AdapterView.OnItemLongClickListener {
    private static AppCompatActivity activity;
    private List<Map<String, String>> dataList;
    private SimpleAdapter simpleAdapter;
    private OpenHelper openHelper;
    private List<Diary> diaryList;
    private int userID;
    private Diary diary;
    private String result;
    private int times;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        userID = Integer.parseInt(bundle.getString("userId"));
        if(saveToSQLte()) {
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
                new String[]{"title", "content"},
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
        openHelper = new OpenHelper(activity);
        SQLiteDatabase sqLiteDatabase = openHelper.getReadableDatabase();
        DiaryDao diaryDao = new DiaryDao(sqLiteDatabase);
        diaryList = diaryDao.queryAll();
        sqLiteDatabase.close();

        dataList = new ArrayList<>();
        for (Diary diary : diaryList) {
            if (diary.getUserId() == userID) {
                Map<String, String> map = new HashMap<>();
                map.put("title", diary.getTitle());
                map.put("content", diary.getContent());
                dataList.add(map);
            }
        }
    }

    public static ListDiaryFragment newInstance(MainActivity activity) {
        ListDiaryFragment.activity = activity;

        ListDiaryFragment fragment = new ListDiaryFragment();
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

    private boolean saveToSQLte() {
        boolean flag = true;
        Thread conLogin = new Thread() {
            public void run() {
                try {
                    Communicate communicate = new Communicate();
                    times = communicate.getSelfCNT(userID);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        conLogin.start();
        while (conLogin.getState() != Thread.State.TERMINATED) ;
//        Thread conLogin1 = new Thread() {
//            public void run() {
//                try {
//                    Communicate communicate = new Communicate();
//                    result = communicate.getSelfNotesFromServer(20,s);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        };
//        conLogin1.start();
        while (conLogin.getState() != Thread.State.TERMINATED) ;
        for (int i = 1; i < times; i++) {
            final int s = i;
            Thread conLogin1 = new Thread() {
                public void run() {
                    try {
                        Communicate communicate = new Communicate();
                        result = communicate.getSelfNotesFromServer(userID,s);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };
            conLogin1.start();
            while (conLogin1.getState() != Thread.State.TERMINATED) ;
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
}