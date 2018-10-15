package com.wcxdhr.simpleplayer;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wcxdhr.simpleplayer.db.Video;

import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {

    private List<Video> mVideoList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView video_name;
        TextView video_author;
        TextView video_count;

        public ViewHolder(View view){
            super(view);
            video_name = (TextView) view.findViewById(R.id.video_name_small);
            video_author = (TextView) view.findViewById(R.id.video_author_small);
            video_count = (TextView) view.findViewById(R.id.video_count_small);;
        }
    }

    public VideoAdapter(List<Video> videoList) {
        mVideoList = videoList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Video video = mVideoList.get(position);
        holder.video_name.setText(video.getName());
        holder.video_author.setText(video.getAuthor());
        holder.video_count.setText(video.getCount());
    }

    @Override
    public int getItemCount(){
        return mVideoList.size();
    }
}
