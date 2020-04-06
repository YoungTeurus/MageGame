package com.mygdx.magegame.objects.additional;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class AnimatedTextureRegion extends TextureRegion {

    private Array<AnimationCoordinates> animation_coords; // Массив всех коориданат, которые нужны для отрисовки
    int current_frame;
    Integer tacts_before_change_frame; // Необязательная штука, позволяет менять кадр только через определённое количество вызовов next()
    Integer current_tacts;
    boolean freeze = false; // Флаг остановки анимации

    public AnimatedTextureRegion(int id){
        // Загрузка анимации по id из заранее подготовленного файла

        // setRegion(x,y,width,height);
    }

    public AnimatedTextureRegion(Texture texture,int[] coords){
        // Загрузка анимации из текстуры по координатам
        animation_coords = new Array<>();
        if(coords.length%4 != 0) { // Если перечисленно недостаточное количество коориданат
            Gdx.app.log("Error", "There is not enough coords!");
            return;
        }
        this.setTexture(texture);
        int num_of_coords = coords.length/4;
        for(int i=0;i<num_of_coords;i++){
            // Добавляем коориданаты в массив
            AnimationCoordinates temp = new AnimationCoordinates(coords[i*4], coords[i*4+1], coords[i*4+2], coords[i*4+3]);
            animation_coords.add(temp);
        }

        current_frame = 0;
        setRegion(animation_coords.get(current_frame)); // Установка 1-ого кадра анимации
    }

    public void set_timer(int num_of_tacts){
        tacts_before_change_frame = num_of_tacts;
        current_tacts = 0;
    }

    public void next(){
        if (freeze)
            return;
        if (tacts_before_change_frame != null){
            current_tacts++;
            if (current_tacts < tacts_before_change_frame)
                return;
            current_tacts=0;
        }
        current_frame = (current_frame+1)%animation_coords.size;
        setRegion(animation_coords.get(current_frame));
    }

    public void setFrame(int frame){
        if (frame < 0 || frame > animation_coords.size)
            return;
        current_frame = frame;
    }

    public void setFreeze(boolean state){freeze = state;}
    public boolean getFreeze(){return freeze;}

    private void setRegion(AnimationCoordinates animCoord){
        // Вызов оригинальной функции.
        // Лучше не трогать.
        setRegion(animCoord.x, animCoord.y, animCoord.width,animCoord.height);
    }
}

class AnimationCoordinates{
    int x;
    int y;
    int width;
    int height;

    AnimationCoordinates(int x, int y, int width,int height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
}