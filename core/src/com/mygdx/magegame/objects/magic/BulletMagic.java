package com.mygdx.magegame.objects.magic;

import com.mygdx.magegame.objects.GameObject;

public interface BulletMagic extends SimpleMagic {
    // Магия, которая представляет собой объект, обычно летящий в пространстве, с действием при столкновении с чем-то

    void onCollision(GameObject collision_target); // Действие, происходящее при столкновении снаряда с каким-либо объектом
}
