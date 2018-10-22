package com.wcxdhr.simpleplayer.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.wcxdhr.simpleplayer.db.Comment;

import java.util.ArrayList;
import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter {

    private List<Comment> mCommentList = new ArrayList<>();

    static class ViewHolder extends RecyclerView.ViewHolder {
        View CommentView;
        TextView CommentText;

        public ViewHolder(View view) {
            super(view);
            CommentView = view;
            CommentText = view.findViewById(R.id.);
        }
    }
}
