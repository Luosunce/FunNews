package com.yone.funnews.model.been;

/**
 * Created by Yoe on 2016/10/10.
 */

public class WelcomeBean {

    private String text;
    private String img;

    public String getText(){ return text; }

    public void setText(String text){ this.text = text; }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
