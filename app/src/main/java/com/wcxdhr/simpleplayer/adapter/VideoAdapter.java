package com.wcxdhr.simpleplayer.adapter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import static com.bumptech.glide.request.RequestOptions.centerCropTransform;

import com.wcxdhr.simpleplayer.R;
import com.wcxdhr.simpleplayer.data.Video;

import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder>  implements  View.OnClickListener, View.OnLongClickListener{

    private List<Video> mVideoList;

    private OnItemClickListener mOnVideoItemClickListener = null;

    private Context mContext;

    public static interface OnItemClickListener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view , int position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView video_name;
        TextView video_author;
        TextView video_count;
        ImageView video_bitmap;

        public ViewHolder(View view){
            super(view);
            video_name = (TextView) view.findViewById(R.id.video_name_small);
            video_author = (TextView) view.findViewById(R.id.video_author_small);
            video_count = (TextView) view.findViewById(R.id.video_count_small);;
            video_bitmap = (ImageView) view.findViewById(R.id.video_bitmap);
        }
    }

    public VideoAdapter(List<Video> videoList) {
        mVideoList = videoList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        view.setOnClickListener(this);
        view.setOnLongClickListener(this);
        return holder;
    }

    @Override
    public void onClick(View view){
        if (mOnVideoItemClickListener != null) {
            mOnVideoItemClickListener.onItemClick(view, (int)view.getTag());
        }
    }

    @Override
    public boolean onLongClick(View view) {
        if (mOnVideoItemClickListener != null) {
            mOnVideoItemClickListener.onItemLongClick(view, (int)view.getTag());
        }
        return true;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Video video = mVideoList.get(position);
        holder.video_name.setText(video.getName());
        holder.video_author.setText(video.getAuthor());
        holder.video_count.setText(String.valueOf(video.getCount()));
        Glide.with(mContext)
                .load(video.getPath())
                .apply(centerCropTransform())
                .into(holder.video_bitmap);
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount(){
        return mVideoList.size();
    }

    public void setOnVideoItemClickListener(OnItemClickListener listener) {
        this.mOnVideoItemClickListener = listener;
    }

}
