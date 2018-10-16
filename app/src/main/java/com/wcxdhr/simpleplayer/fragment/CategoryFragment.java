package com.wcxdhr.simpleplayer.fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wcxdhr.simpleplayer.R;
import com.wcxdhr.simpleplayer.adapter.CategoryAdapter;
import com.wcxdhr.simpleplayer.db.Category;
import com.wcxdhr.simpleplayer.db.DataBaseHelper;
import com.wcxdhr.simpleplayer.db.VideoDao;

import org.litepal.LitePal;
import org.litepal.tablemanager.callback.DatabaseListener;

import java.util.ArrayList;
import java.util.List;

public class CategoryFragment extends Fragment {

    private View view;

    public static final String TAG = "TEST";

    private List<Category> CategoryList = new ArrayList<>();

    private VideoDao videoDao;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.category_frag, container, false);
        videoDao = new VideoDao(getContext());
        initCategories();
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.category_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        VideoListFragment videoListFragment = (VideoListFragment) getFragmentManager().findFragmentById(R.id.video_list_fragment);
        CategoryAdapter adapter = new CategoryAdapter(CategoryList,getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        return view;
    }

    private void initCategories() {
        Cursor cursor = videoDao.getCategories();
        if (cursor.moveToFirst()) {
            do {
                Category category = new Category();
                category.setName(cursor.getString(cursor.getColumnIndex("name")));
                category.setId(cursor.getInt(cursor.getColumnIndex("id")));
                CategoryList.add(category);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }


}
