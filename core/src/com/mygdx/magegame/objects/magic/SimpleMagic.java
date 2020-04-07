package com.mygdx.magegame.objects.magic;

import com.mygdx.magegame.objects.Player;

public interface SimpleMagic {
    // Обычная магия, воздействующая на игрока, который кастовал заклинание

    boolean onCast(); // Действие, происходящее при касте
}
