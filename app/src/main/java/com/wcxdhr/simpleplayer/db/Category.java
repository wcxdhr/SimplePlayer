package com.wcxdhr.simpleplayer.db;

import org.litepal.crud.LitePalSupport;

import java.util.ArrayList;
import java.util.List;

public class Category extends LitePalSupport {

    private String name;

    private int id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

