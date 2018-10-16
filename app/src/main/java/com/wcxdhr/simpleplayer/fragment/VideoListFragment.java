package com.wcxdhr.simpleplayer.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.wcxdhr.simpleplayer.R;
import com.wcxdhr.simpleplayer.adapter.VideoAdapter;
import com.wcxdhr.simpleplayer.db.Category;
import com.wcxdhr.simpleplayer.db.Video;
import com.wcxdhr.simpleplayer.db.VideoDao;

import java.util.ArrayList;
import java.util.List;

public class VideoListFragment extends Fragment {

    private View view;

    private VideoDao videoDao;

    private List<Video> VideoList = new ArrayList<>();

    private RecyclerView recyclerView;

    private LinearLayoutManager LayoutManager;

    private VideoAdapter adapter;

    private Button add_button;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.videolist_frag, container, false);
        addVideotoList(1);
        //showRecyclerView();
        recyclerView = (RecyclerView) view.findViewById(R.id.videolist_view);
        LayoutManager = new LinearLayoutManager(getActivity());
        adapter = new VideoAdapter(VideoList);
        recyclerView.setLayoutManager(LayoutManager);
        recyclerView.setAdapter(adapter);
        return view;
    }

    public void refresh(int category){
        VideoList.clear();
        View visibilityLayout = view.findViewById(R.id.videolist_view);
        visibilityLayout.setVisibility(View.VISIBLE);
        addVideotoList(category);
        adapter.notifyDataSetChanged();
    }

    private void addVideotoList(int category){
        videoDao = new VideoDao(getContext());
        Cursor cursor = videoDao.getVideos(category);
        if (cursor.moveToFirst()) {
            do {
                Video video = new Video();
                video.setId(cursor.getInt(cursor.getColumnIndex("id")));
                video.setAuthor(cursor.getString(cursor.getColumnIndex("author")));
                video.setName(cursor.getString(cursor.getColumnIndex("name")));
                video.setCount(cursor.getInt(cursor.getColumnIndex("count")));
                video.setPath(cursor.getString(cursor.getColumnIndex("path")));
                video.setCategory(category);
                VideoList.add(video);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }
}
