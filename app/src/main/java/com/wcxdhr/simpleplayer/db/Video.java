package com.wcxdhr.simpleplayer.db;

import org.litepal.crud.LitePalSupport;

public class Video extends LitePalSupport {

    private String name;

    private String author;

    private int count;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
