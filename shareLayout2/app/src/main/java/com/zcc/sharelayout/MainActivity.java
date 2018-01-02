package com.zcc.sharelayout;

import android.app.ListActivity;
import android.os.Bundle;

import java.util.ArrayList;


public class MainActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getListView().setDividerHeight(0);
        getListView().setPadding(10, 10, 10, 10);
        ArrayList<News> newses = new ArrayList<>();
        newses.add(new News(0, News.TEXT, "title1", "This is News 1.","This is body", 0));
        newses.add(new News(1, News.TEXT, "title2", "This is News 2.","This is body", 0));
        newses.add(new News(2, News.IMAGE, "title3", "","This is body", android.R.drawable.ic_menu_camera));
        newses.add(new News(3, News.IMAGE, "title4", "","This is body", android.R.drawable.ic_menu_call));
        newses.add(new News(4, News.TEXT, "title5", "This is News 5.","This is body", 0));
        newses.add(new News(5, News.IMAGE, "title6", "","This is body", android.R.drawable.ic_menu_gallery));
        newses.add(new News(6, News.TEXT, "title7", "This is News 6.","This is body", 0));
        newses.add(new News(7, News.TEXT, "title8", "This is News 7.", "This is body",0));
        newses.add(new News(8, News.IMAGE, "title9", "","This is body", android.R.drawable.ic_menu_compass));
        NewsAdapter adapter = new NewsAdapter(this);
        adapter.addAll(NewsToItem.newsToItems(newses));
        setListAdapter(adapter);

    }

}