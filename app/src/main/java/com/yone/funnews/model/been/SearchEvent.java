package com.yone.funnews.model.been;

/**
 * Created by Yoe on 2016/10/21.
 */

public class SearchEvent {

    private String query;
    private int type;

    public SearchEvent(String query, int type){
        this.query = query;
        this.type = type;

    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

}
