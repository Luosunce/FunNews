package com.yone.funnews.model.been;

import java.util.List;

/**
 * Created by Yoe on 2016/10/18.
 */

public class ThemeListBean {

    private int limit;
    private List<?> subscribed;
    private List<OthersBean> others;

    public List<OthersBean> getOthers() {
        return others;
    }

    public void setOthers(List<OthersBean> others) {
        this.others = others;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public List<?> getSubscribed() {
        return subscribed;
    }

    public void setSubscribed(List<?> subscribed) {
        this.subscribed = subscribed;
    }

    public static class OthersBean{
        private int color;
        private String thumbnail;
        private String description;
        private int id;
        private String name;

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getColor() {
            return color;
        }

        public void setColor(int color) {
            this.color = color;
        }

        public String getThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }
}
