package com.wcxdhr.simpleplayer.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.wcxdhr.simpleplayer.ExoPlayer.ExoPlayerVideoHandler;
import com.wcxdhr.simpleplayer.FloatWindow.FloatWindowService;
import com.wcxdhr.simpleplayer.R;
import com.wcxdhr.simpleplayer.adapter.PageAdapter;
import com.wcxdhr.simpleplayer.db.Category;
import com.wcxdhr.simpleplayer.db.VideoDao;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ArrayList<String> mTitles = new ArrayList<>();
    private ArrayList<Category> CategoryList = new ArrayList<>();
    private PageAdapter pageAdapter;
    private VideoDao videoDao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initViews();

    }


    protected void initData() {
        initTab();

    }


    protected void initViews() {
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.my_vp);
        pageAdapter = new PageAdapter(getSupportFragmentManager(), mTitles);
        viewPager.setAdapter(pageAdapter);
        tabLayout.setupWithViewPager(viewPager);
        ImageView searchImg = (ImageView) findViewById(R.id.searchActivity_img);
        searchImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });

    }

    private void initTab() {
        videoDao = new VideoDao(this);
        Cursor cursor = videoDao.getCategories();
        if (cursor.moveToFirst()) {
            do {
                Category category = new Category();
                category.setName(cursor.getString(cursor.getColumnIndex("name")));
                category.setId(cursor.getInt(cursor.getColumnIndex("id")));
                CategoryList.add(category);
                mTitles.add(category.getName());
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    @Override
    public void onDestroy() {
        Intent stopIntent = new Intent(this, FloatWindowService.class);
        stopService(stopIntent);
        ExoPlayerVideoHandler.getInstance().releaseVideoPlayer();
        super.onDestroy();
    }



}
