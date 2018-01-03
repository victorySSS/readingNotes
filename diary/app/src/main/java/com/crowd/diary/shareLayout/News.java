package com.crowd.diary.shareLayout;

/**
 * Created by zhaocc14 on 2018/1/2.
 */

public class News {
    public static final int TEXT = 1;
    public static final int IMAGE = 2;

    private int newsId;
    private int type;
    private String title;
    private String text;
    private String body;
    private int imageSource;

    public News(int newsId, int type, String title, String text, String body, int imageSource) {
        this.newsId = newsId;
        this.type = type;
        this.title = title;
        this.body = body;
        this.imageSource = imageSource;
        this.text=text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getNewsId() {
        return newsId;
    }

    public int getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public int getImageSource() {
        return imageSource;
    }
}
