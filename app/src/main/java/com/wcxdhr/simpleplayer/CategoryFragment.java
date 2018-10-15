package com.wcxdhr.simpleplayer;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wcxdhr.simpleplayer.db.Category;

import org.litepal.LitePal;
import org.litepal.tablemanager.callback.DatabaseListener;

import java.util.ArrayList;
import java.util.List;

public class CategoryFragment extends Fragment {

    private View view;

    private List<Category> CategoryList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.category_frag, container, false);
        initCategories();
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.category_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        CategoryAdapter adapter = new CategoryAdapter(CategoryList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        return view;
    }

    private void initCategories() {
        initDatabase();
        //SQLiteDatabase db = LitePal.getDatabase();
        CategoryList = LitePal.findAll(Category.class);
        for (Category category: CategoryList) {
            Log.d("check", category.getName());
        }
    }

    private void initDatabase(){
        LitePal.registerDatabaseListener(new DatabaseListener() {
            @Override
            public void onCreate() {
                Category cartoon = new Category();
                cartoon.setName("动画");
                cartoon.save();
                Category drama = new Category();
                drama.setName("电视剧");
                drama.save();
                Category movie = new Category();
                movie.setName("电影");
                movie.save();
                Category music = new Category();
                music.setName("音乐");
                music.save();
            }

            @Override
            public void onUpgrade(int oldVersion, int newVersion) {

            }
        });
    }
}
