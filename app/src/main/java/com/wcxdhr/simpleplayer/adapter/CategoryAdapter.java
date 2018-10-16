package com.wcxdhr.simpleplayer.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wcxdhr.simpleplayer.R;
import com.wcxdhr.simpleplayer.db.Category;
import com.wcxdhr.simpleplayer.fragment.VideoListFragment;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private List<Category> mCategoryList;

    private Context context;

    static class ViewHolder extends RecyclerView.ViewHolder {

        View categoryView;
        TextView categoryName;

        public ViewHolder(View view) {
            super(view);
            categoryView = view;
            categoryName = (TextView) view.findViewById(R.id.list_name);
        }
    }

    public CategoryAdapter(List<Category> categoryList, Context context) {
        mCategoryList = categoryList;
        this.context = context;
    }

    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.categoryView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Category category = mCategoryList.get(holder.getAdapterPosition());
                FragmentManager manager = ((AppCompatActivity)context).getSupportFragmentManager();
                VideoListFragment videoListFragment = (VideoListFragment)manager.findFragmentById(R.id.video_list_fragment);
                videoListFragment.refresh(category.getId());
            }
        });
        //CategoryAdapter.ViewHolder holder = new CategoryAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(CategoryAdapter.ViewHolder holder, int position) {
        Category category = mCategoryList.get(position);
        holder.categoryName.setText(category.getName());
        Log.d("bind", category.getName());
    }

    @Override
    public int getItemCount(){
        return mCategoryList.size();
    }
}
