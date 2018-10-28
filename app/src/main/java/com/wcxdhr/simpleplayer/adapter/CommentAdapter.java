package com.wcxdhr.simpleplayer.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wcxdhr.simpleplayer.R;
import com.wcxdhr.simpleplayer.db.Comment;

import java.util.ArrayList;
import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter <CommentAdapter.ViewHolder> {

    private List<Comment> mCommentList;

    static class ViewHolder extends RecyclerView.ViewHolder {

        View CommentView;
        TextView CommentText;
        TextView CommentTime;

        public ViewHolder(View view) {
            super(view);
            CommentView = view;
            CommentText = (TextView)view.findViewById(R.id.comment_content);
            CommentTime = (TextView)view.findViewById(R.id.comment_time);
        }
    }

    public CommentAdapter(List<Comment> commentList) {
        mCommentList = commentList;
    }

    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(CommentAdapter.ViewHolder holder, int position) {
        Comment comment = mCommentList.get(position);
        holder.CommentText.setText(comment.getContent());
        holder.CommentTime.setText(comment.getSend_time());

    }

    @Override
    public int getItemCount(){
        return mCommentList.size();
    }
}

