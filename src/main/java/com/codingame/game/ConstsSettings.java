package com.codingame.game;

public class ConstsSettings {

    public double dogSpeed = 0.5;
    public double shepardSpeed = 0.3;
    public double sheepSpeed1 = 0.2000;
    public double sheepSpeed2 = 0.3000;
    public double sheepSpeed3 = 0.5000;
    public double dangerRadius = 2;
    public double barkRadius = 1;
    public double entityRadius = 1;
    public int barkCoolDown = 5;
    public int shepardMaxWool = 10;
    public int initialSheepWool = 10;
    public int mapSize = 10;
    public int calmCoolDown = 3;
    public int turns = 100;

    @Override
    public String toString() {
        return String.format("%d %d %d %.6f %.6f %.6f %.6f %.6f %.6f %.6f %d %.6f %d %d", mapSize, initialSheepWool, shepardMaxWool,
            entityRadius, sheepSpeed1, sheepSpeed2, sheepSpeed3, shepardSpeed, dogSpeed, dangerRadius, barkCoolDown, barkRadius,
            calmCoolDown, turns);
    }
}
