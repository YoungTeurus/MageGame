package com.mygdx.magegame.mechanics;

import com.badlogic.gdx.Gdx;

public interface Dropable {
    void processDrop();

    // Вызывается, когда этот обьект падает ниже 0 уровня
    void onFallDown();
}
