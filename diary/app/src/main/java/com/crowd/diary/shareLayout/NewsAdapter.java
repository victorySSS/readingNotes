package com.crowd.diary.shareLayout;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaocc14 on 2018/1/2.
 */

public class NewsAdapter extends BaseAdapter {
    private ArrayList<Item> list;
    private LayoutInflater mInflater;

    public NewsAdapter(Context context) {
        list = new ArrayList<Item>();
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Item getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        int type = getItemViewType(position);
        if (null == convertView) {
            viewHolder = new ViewHolder();
            switch (type) {
                case Item.TITLE:
                    convertView = mInflater.inflate(R.layout.item_title, parent, false);
                    viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.title);
                    break;
                case Item.BODY:
                    convertView = mInflater.inflate(R.layout.item_body, parent, false);
                    viewHolder.tvBody = (TextView) convertView.findViewById(R.id.body);
                    break;
                case Item.IMAGE:
                    convertView = mInflater.inflate(R.layout.item_image, parent, false);
                    viewHolder.ivImage = (ImageView) convertView.findViewById(R.id.image);
                    break;
                case Item.BOTTOM:
                    convertView = mInflater.inflate(R.layout.item_bottom, parent, false);
                    break;
                case Item.TEXT:
                    convertView = mInflater.inflate(R.layout.item_text,parent,false);
                    viewHolder.tvText=(TextView) convertView.findViewById(R.id.text);
                    break;
            }
            if (null != convertView) {
                convertView.setTag(viewHolder);
            }
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Item item = list.get(position);
        switch (type) {
            case Item.TITLE:
                viewHolder.tvTitle.setText(item.getText());
                break;
            case Item.BODY:
                viewHolder.tvBody.setText(item.getText());
                break;
            case Item.IMAGE:
                viewHolder.ivImage.setImageResource(item.getImageSource());
                break;
            case Item.TEXT:
                viewHolder.tvText.setText(item.getText());
                break;
        }

        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).getStyleType();
    }

    @Override
    public int getViewTypeCount() {
        return 5;
    }

    public void add(Item item){
        list.add(item);
    }

    public void addAll(List<Item> items){
        list.addAll(items);
    }

    static class ViewHolder {
        TextView tvTitle;
        TextView tvBody;
        TextView tvText;
        ImageView ivImage;
        View bottomView;
    }
}
