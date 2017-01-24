package com.example.application.model;

/**
 * Created by warrens on 23.01.17.
 */

public class itemSlideMenu {

    private int imgId;
    private String title;

    public itemSlideMenu(int imgId, String title) {
        this.imgId = imgId;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }
}
