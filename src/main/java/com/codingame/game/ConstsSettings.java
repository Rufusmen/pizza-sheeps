package com.codingame.game;

import java.util.Random;

/**
 * Class containing all consts for one game.
 */
public class ConstsSettings {

    public double dogSpeed;
    public double shepardSpeed;
    /**
     * Default sheep speed.
     */
    public double sheepSpeed1;

    /**
     * Speed of sheep that's going away from player units .
     */
    public double sheepSpeed2;
    /**
     * Speed of scared sheep.
     */
    public double sheepSpeed3;
    public int sameDirectionProbability;
    public double dangerRadius;
    public double barkRadius;
    public double entityRadius;
    public int barkCoolDown;
    public int shepardMaxWool;
    public int initialSheepWool;
    public int mapSizeX;
    public int mapSizeY;
    public int calmCoolDown;
    public int turns;
    public int maxSheep;
    public int minSheep;

    public ConstsSettings(Random random) {
        double sp = random.nextDouble()*0.5+0.75;
        dogSpeed = 0.6*sp;
        shepardSpeed = 0.4*sp;
        sheepSpeed1 = 0.1500*sp;
        sheepSpeed2 = 0.2500*sp;
        sheepSpeed3 = 0.3500*sp;
        sameDirectionProbability = 60;
        dangerRadius = 2;
        barkRadius = random.nextDouble()*2+2.0;
        entityRadius = 0.5;
        barkCoolDown = 5+random.nextInt(10);
        shepardMaxWool = 10+random.nextInt(10);
        initialSheepWool = 3+random.nextInt(5);
        mapSizeX = 10;
        mapSizeY = 15;
        calmCoolDown = 5+random.nextInt(6);
        turns = 250;
        maxSheep = 75;
        minSheep = 40;
    }

    @Override
    public String toString() {
        return String.format("%d %d %.6f %.6f %.6f %.6f %.6f %.6f %.6f %d %.6f %d", initialSheepWool, shepardMaxWool,
            entityRadius, sheepSpeed1, sheepSpeed2, sheepSpeed3, shepardSpeed, dogSpeed, dangerRadius, barkCoolDown, barkRadius,
            calmCoolDown);
    }
}
