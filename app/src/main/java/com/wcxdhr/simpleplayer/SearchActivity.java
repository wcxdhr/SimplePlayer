package com.wcxdhr.simpleplayer;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.wcxdhr.simpleplayer.adapter.VideoAdapter;
import com.wcxdhr.simpleplayer.db.Video;
import com.wcxdhr.simpleplayer.db.VideoDao;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private List<Video> mVideoList =new ArrayList<>();

    private RecyclerView searchView;

    private VideoAdapter adapter;

    public static final int CHOOSE_NAME = 1;

    public static final int CHOOSE_AUTHOR = 2;

    private int choose_range_flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Spinner search_sp = (Spinner) findViewById(R.id.search_sp);
        ArrayAdapter<CharSequence> sp_adapter = ArrayAdapter.createFromResource(this,R.array.search_range,android.R.layout.simple_spinner_item);
        sp_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        search_sp.setAdapter(sp_adapter);
        EditText editText = (EditText) findViewById(R.id.search_text);
        Button searchBtn = (Button) findViewById(R.id.search_btn);
        searchView = (RecyclerView) findViewById(R.id.search_result_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        adapter = new VideoAdapter(mVideoList);
        searchView.setLayoutManager(layoutManager);
        searchView.setAdapter(adapter);
        choose_range_flag = CHOOSE_NAME;
        search_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] search_range = getResources().getStringArray(R.array.search_range);
                if (search_range[position].equals("名称")) {
                    choose_range_flag = CHOOSE_NAME;
                }
                else {
                    choose_range_flag = CHOOSE_AUTHOR;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = editText.getText().toString();
                searchForResult(content);
            }
        });

        adapter.setOnVideoItemClickListener(new VideoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Video video = mVideoList.get(position);
                Intent intent = new Intent(SearchActivity.this,VideoPlayerActivity.class);
                intent.putExtra("video_data", video);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });

    }

    private void searchForResult(String content){
        mVideoList.clear();
        Cursor cursor;
        VideoDao videoDao = new VideoDao(this);
        if (choose_range_flag == CHOOSE_NAME) {
            cursor = videoDao.findVideosByName(content);
        }
        else {
            cursor = videoDao.findVideosByAuthor(content);
        }
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    Video video = new Video();
                    video.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                    video.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
                    video.setAuthor(cursor.getString(cursor.getColumnIndexOrThrow("author")));
                    video.setCount(cursor.getInt(cursor.getColumnIndexOrThrow("count")));
                    video.setPath(cursor.getString(cursor.getColumnIndexOrThrow("path")));
                    video.setCategory(cursor.getInt(cursor.getColumnIndexOrThrow("category")));
                    mVideoList.add(video);
                }while (cursor.moveToNext());
            }
        }
        cursor.close();
        adapter.notifyDataSetChanged();
    }
}
