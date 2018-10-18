package com.wcxdhr.simpleplayer.db;

import android.os.Parcel;
import android.os.Parcelable;

public class Video implements Parcelable{

    private int id;

    private String name;

    private String author;

    private String path;

    private int count;

    private int category;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int categoryid) {
        this.category = categoryid;
    }

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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(author);
        dest.writeString(path);
        dest.writeInt(count);
        dest.writeInt(category);
    }

    public static final Parcelable.Creator<Video> CREATOR = new Parcelable.Creator<Video>() {

        @Override
        public Video createFromParcel(Parcel source) {
            Video video = new Video();
            video.id = source.readInt();
            video.name = source.readString();
            video.author = source.readString();
            video.path = source.readString();
            video.count = source.readInt();
            video.category = source.readInt();
            return video;
        }
        @Override
        public Video[] newArray(int size) {
            return new Video[size];
        }

    };
}
