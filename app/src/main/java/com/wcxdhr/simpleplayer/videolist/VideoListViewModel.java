package com.wcxdhr.simpleplayer.videolist;

import androidx.lifecycle.ViewModel;

import com.wcxdhr.simpleplayer.db.Category;

import java.util.ArrayList;

public class VideoListViewModel extends ViewModel {

    private ArrayList<String> categoryList = new ArrayList<>();

    public VideoListViewModel() {
        initCategory();
    }

    private void initCategory() {
        categoryList.add("动画");
        categoryList.add("电影");
        categoryList.add("电视剧");
        categoryList.add("音乐");
        categoryList.add("生活");
        categoryList.add("科技");
    }
}
