package com.mygdx.magegame;

public class TextObject{
    String text;

    int screen_x;
    int screen_y;

    public TextObject(int x, int y, String text) {
        this.text = text;
        set_pos(x, y);
    }

    public void set_pos(int x, int y){
        screen_x = x;
        screen_y = y;
    }

    public void set_text(String text){
        this.text = text;
    }
}
