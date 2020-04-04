package com.mygdx.magegame;

public class Consts {
    public static final int window_w = 800; // Размеры стандартного окна
    public static final int window_h = 600;

    public static int world_w = 10; // "Приближение" камеры
    public static int world_h = 10;

    public static final int num_of_tilesets = 2; // Количество доступных тайлсетов
    // public static int[] tilesets_sizes = new int[]{ // Размеры спрайтов в соответсвующих тайлсетах
    //         32, 32
    // };
    public static final String[] tilesets_filenames = new String[]{
            "spriteset_0", "spriteset_mages"
    };
    // public static final int num_of_settings_in_spriteset_files = 10; // Максимальное количество параметров в строке файла тайлсета
}

