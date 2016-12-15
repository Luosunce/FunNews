package com.yone.funnews.model.been;

import java.util.List;

/**
 * Created by Yoe on 2016/10/20.
 */

public class SectionChildListBean {

    private int timestamp;
    private String name;
    private List<StoriesBean> stories;

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<StoriesBean> getStories() {
        return stories;
    }

    public void setStories(List<StoriesBean> stories) {
        this.stories = stories;
    }

    public static class StoriesBean{

        private String display_date;
        private String title;
        private String date;
        private boolean multipic;
        private int id;
        private List<String> images;
        private boolean readState;

        public boolean isMultipic() {
            return multipic;
        }

        public void setMultipic(boolean multipic) {
            this.multipic = multipic;
        }

        public String getDisplay_date() {
            return display_date;
        }

        public void setDisplay_date(String display_date) {
            this.display_date = display_date;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public List<String> getImages() {
            return images;
        }

        public void setImages(List<String> images) {
            this.images = images;
        }

        public boolean getReadState() {
            return readState;
        }

        public void setReadState(boolean readState) {
            this.readState = readState;
        }

    }
}
