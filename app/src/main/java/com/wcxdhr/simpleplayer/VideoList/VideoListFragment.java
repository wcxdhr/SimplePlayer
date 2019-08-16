package com.wcxdhr.simpleplayer.VideoList;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.wcxdhr.simpleplayer.Log.LogUtil;
import com.wcxdhr.simpleplayer.R;
import com.wcxdhr.simpleplayer.VideoDetail.VideoPlayerActivity;
import com.wcxdhr.simpleplayer.adapter.VideoAdapter;
import com.wcxdhr.simpleplayer.data.Video;
import com.wcxdhr.simpleplayer.db.VideoDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static android.content.Context.MODE_PRIVATE;

public class VideoListFragment extends Fragment implements View.OnClickListener {

    private View view;

    private VideoDao videoDao;

    private List<Video> VideoList = new ArrayList<>();

    private RecyclerView recyclerView;

    private LinearLayoutManager LayoutManager;

    private VideoAdapter adapter;

    private Button add_button;

    private int category;

    private Bitmap bitmap;

    public static final String[] nameList = new String[]{"Root","Sameen Shaw", "John Reese", "Harold Finch", "The Machine", "Bear"};

    public static VideoListFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt("ARG_PAGE", page);
        VideoListFragment videoListFragment = new VideoListFragment();
        videoListFragment.setArguments(args);
        return videoListFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.videolist_frag, container, false);
        category = getArguments().getInt("ARG_PAGE");
        addVideotoList(category);
        recyclerView = (RecyclerView) view.findViewById(R.id.videolist_view);
        LayoutManager = new LinearLayoutManager(getActivity());
        adapter = new VideoAdapter(VideoList);
        recyclerView.setLayoutManager(LayoutManager);
        recyclerView.setAdapter(adapter);
        add_button = (Button) view.findViewById(R.id.add_button);
        add_button.setOnClickListener(this);
        adapter.setOnVideoItemClickListener(new VideoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                SharedPreferences.Editor editor = getContext().getSharedPreferences("data", MODE_PRIVATE).edit();
                editor.putInt("category", category);
                editor.apply();
                Video video = VideoList.get(position);
                Intent intent = new Intent(getContext(),VideoPlayerActivity.class);
                intent.putExtra("video_data", video);
                LogUtil.d("传递的video id： "+video.getId());
                startActivityForResult(intent, 2);
            }

            @Override
            public void onItemLongClick(View view, int position) {
                final Video video = VideoList.get(position);
                LogUtil.d("长按事件");
                AlertDialog.Builder delete_dialog = new AlertDialog.Builder(getContext());
                delete_dialog.setTitle("警告");
                delete_dialog.setMessage("确认删除该视频？");
                delete_dialog.setCancelable(true);
                delete_dialog.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        videoDao.delete(video.getId());
                        refresh(category);
                    }
                });
                delete_dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                delete_dialog.show();
            }
        });

        return view;
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.add_button:
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(getActivity(), new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                }else {
                    chooseVideo();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        switch (requestCode){
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    chooseVideo();
                }else{
                    Toast.makeText(getContext(), "拒绝了", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    public void refresh(int category){
        LogUtil.d("用于刷新的category: "+String.valueOf(category));
        this.category = category;
        VideoList.clear();
        View visibilityLayout = view.findViewById(R.id.videolist_view);
        visibilityLayout.setVisibility(View.VISIBLE);
        addVideotoList(category);
        adapter.notifyDataSetChanged();
    }

    private void addVideotoList(int category){
        LogUtil.d("addVideotoList: "+String.valueOf(category));
        if (VideoList.size() == 0) {
            videoDao = new VideoDao(getContext());
            Cursor cursor = videoDao.getVideos(category);
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        Video video = new Video();
                        video.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                        video.setAuthor(cursor.getString(cursor.getColumnIndexOrThrow("author")));
                        video.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
                        video.setCount(cursor.getInt(cursor.getColumnIndexOrThrow("count")));
                        video.setPath(cursor.getString(cursor.getColumnIndexOrThrow("path")));
                        video.setCategory(category);
                        VideoList.add(video);
                    } while (cursor.moveToNext());
                }
            }
            cursor.close();

        }
    }

    private void chooseVideo(){
        SharedPreferences.Editor editor = getActivity().getSharedPreferences("data", MODE_PRIVATE).edit();
        editor.putInt("category",category);
        editor.apply();
        LogUtil.d("存在本地的category："+String.valueOf(category));
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == getActivity().RESULT_OK){
                Uri uri =data.getData();
                Cursor cursor = getActivity().getContentResolver().query(uri,null,null,null,null);
                if (cursor != null){
                    if (cursor.moveToFirst()){
                        Video video = new Video();
                        video.setName(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE)));
                        Random random = new Random();
                        video.setAuthor(nameList[random.nextInt(6)]);
                        video.setPath(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)));
                        video.setCount(0);
                        SharedPreferences preferences = getActivity().getSharedPreferences("data", MODE_PRIVATE);
                        category = preferences.getInt("category", 1);
                        LogUtil.d("取出的category值："+String.valueOf(category));
                        video.setCategory(category);
                        if (videoDao.addVideo(video) == true) {
                            //refresh(category);
                            VideoList.clear();
                            addVideotoList(category);
                            //VideoList.add(video);
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        }
        else if (requestCode == 2) {
            SharedPreferences preferences = getActivity().getSharedPreferences("data", MODE_PRIVATE);
            category = preferences.getInt("category", 1);
            refresh(category);
        }
    }


}
