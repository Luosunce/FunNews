package com.yone.funnews.model.been;

import java.util.List;

/**
 * Created by Yoe on 2016/10/20.
 */

public class HotListBean {

    private List<RecentBean> recent;

    public List<RecentBean> getRecent() {
        return recent;
    }

    public void setRecent(List<RecentBean> recent) {
        this.recent = recent;
    }

    public static class RecentBean{

        private int news_id;
        private String url;
        private String thumbnail;
        private String title;
        private boolean readState;

        public String getThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
        }

        public int getNews_id() {
            return news_id;
        }

        public void setNews_id(int news_id) {
            this.news_id = news_id;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public boolean getReadState() {
            return readState;
        }

        public void setReadState(boolean readState) {
            this.readState = readState;
        }

    }
}
