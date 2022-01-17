package com.codingame.game;

public class Util {

    public static int convert(int orig, int cellSize, double unit) {
        return (int) (orig + unit * cellSize);
    }

    public static int otherId(int id) {
        return id == 0 ? 1 : 0;
    }

}
