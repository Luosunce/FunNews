package com.yone.funnews.model.been;

import java.io.Serializable;

import io.realm.RealmObject;

/**
 * Created by Yoe on 2016/10/15.
 */

public class RealmLikeBean extends RealmObject implements Serializable{

    public RealmLikeBean(){ }

    private String id;

    private String image;

    private String title;

    private int type;

    private long time;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

}

