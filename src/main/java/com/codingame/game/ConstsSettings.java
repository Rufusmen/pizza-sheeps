package com.codingame.game;

public class ConstsSettings {

    public double dogSpeed = 0.6;
    public double shepardSpeed = 0.4;
    public double sheepSpeed1 = 0.1500;
    public double sheepSpeed2 = 0.2500;
    public double sheepSpeed3 = 0.4000;
    public int sameDirectionProbability = 60;
    public double dangerRadius = 2;
    public double barkRadius = 1;
    public double entityRadius = 0.5;
    public int barkCoolDown = 5;
    public int shepardMaxWool = 10;
    public int initialSheepWool = 3;
    public int mapSizeX = 10;
    public int mapSizeY = 15;
    public int calmCoolDown = 3;
    public int turns = 100;
    public int maxSheep = 100;
    public int minSheep = 10;

    @Override
    public String toString() {
        return String.format("%d %d %d %.6f %.6f %.6f %.6f %.6f %.6f %.6f %d %.6f %d %d", mapSizeX, initialSheepWool, shepardMaxWool,
            entityRadius, sheepSpeed1, sheepSpeed2, sheepSpeed3, shepardSpeed, dogSpeed, dangerRadius, barkCoolDown, barkRadius,
            calmCoolDown, turns);
    }
}
