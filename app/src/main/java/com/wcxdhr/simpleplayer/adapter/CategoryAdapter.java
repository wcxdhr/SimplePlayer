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

import com.wcxdhr.simpleplayer.Log.LogUtil;
import com.wcxdhr.simpleplayer.R;
import com.wcxdhr.simpleplayer.db.Category;
import com.wcxdhr.simpleplayer.fragment.VideoListFragment;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private List<Category> mCategoryList;

    private Context context;

    private int mSelection = 0;

    static class ViewHolder extends RecyclerView.ViewHolder {

        View categoryView;
        TextView categoryName;
        View categoryLine;

        public ViewHolder(View view) {
            super(view);
            categoryView = view;
            categoryName = (TextView) view.findViewById(R.id.list_name);
            categoryLine = (View) view.findViewById(R.id.select_line);
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
                setmSelection();
                Category category = mCategoryList.get(holder.getAdapterPosition());
                FragmentManager manager = ((AppCompatActivity)context).getSupportFragmentManager();
                VideoListFragment videoListFragment = (VideoListFragment)manager.findFragmentById(R.id.video_list_fragment);
                LogUtil.d("CategoryAdapter: "+String.valueOf(category.getId()));
                videoListFragment.refresh(category.getId());
            }
        });
        //CategoryAdapter.ViewHolder holder = new CategoryAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(CategoryAdapter.ViewHolder holder, int position) {
        /*if (mCategoryList.get(position) != null) {
            holder.categoryView.setBackground(context.getResources().getDrawable(R.color.colorPrimary));
        }
        else {
            holder.categoryView.setBackground(context.getResources().getDrawable(R.color.colorBlack));
        }*/
        if (mSelection == position) {
            holder.categoryLine.setBackground(context.getResources().getDrawable(R.color.colorWhite));
        }
        else {
            holder.categoryLine.setBackground(context.getResources().getDrawable(R.color.colorPrimary));
        }
        Category category = mCategoryList.get(position);
        holder.categoryName.setText(category.getName());
        Log.d("bind", category.getName());
    }

    @Override
    public int getItemCount(){
        return mCategoryList.size();
    }

    public int getmSelection() {
        return mSelection;
    }

    public void setmSelection(int mSelection) {
        this.mSelection = mSelection;
        notifyDataSetChanged();
    }
}
