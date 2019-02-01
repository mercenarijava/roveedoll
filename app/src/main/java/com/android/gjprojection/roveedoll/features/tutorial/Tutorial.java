package com.android.gjprojection.roveedoll.features.tutorial;

import android.widget.VideoView;

public class Tutorial {
    int background;
    String card_title;
    int video;

    public Tutorial(){
    }

    public Tutorial(int background, String card_title, int video){
        this.background = background;
        this.card_title = card_title;
        this.video = video;
    }

    public int getBackground() {
        return background;
    }

    public String getCard_title() {
        return card_title;
    }

    public int getVideo() {
        return video;
    }

    public void setBackground(int background) {
        this.background = background;
    }

    public void setCard_title(String card_title) {
        this.card_title = card_title;
    }

    public void setVideo(int video) {
        this.video = video;
    }
}

