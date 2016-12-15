package com.yone.funnews.model.been;

import java.util.List;

/**
 * Created by Yoe on 2016/10/13.
 */

public class DailyBeforeListBean {

    private String date;
    private List<DailyListBean.StoriesBean> stories;
    private List<DailyListBean.TopStoriesBean> topstories;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<DailyListBean.StoriesBean> getStories() {
        return stories;
    }

    public void setStories(List<DailyListBean.StoriesBean> stories) {
        this.stories = stories;
    }

    public List<DailyListBean.TopStoriesBean> getTopstories() {
        return topstories;
    }

    public void setTopstories(List<DailyListBean.TopStoriesBean> topstories) {
        this.topstories = topstories;
    }
}
