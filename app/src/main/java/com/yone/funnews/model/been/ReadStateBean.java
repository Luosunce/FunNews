package com.yone.funnews.model.been;

import io.realm.RealmObject;

/**
 * Created by Yoe on 2016/10/13.
 */

public class ReadStateBean extends RealmObject{

    private int id;

    public ReadStateBean(){ }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }




}
