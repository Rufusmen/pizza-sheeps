package com.codingame.game.entity;

import static com.codingame.game.Util.convert;

import com.codingame.game.Vector2;
import com.codingame.gameengine.module.entities.Circle;

public abstract class Entity {

    private static final double TO_RAD = 180.0 / Math.PI;
    private static final double TO_ANG = Math.PI / 180.0;

    protected int owner = 0;

    public Vector2 getPosition() {
        return position;
    }

    protected Vector2 position;
    private final int radius = 50;
    protected Circle circle;
    private int cellSize;
    private int origX;
    private int origY;
    private double maxMove;


    public Entity(Vector2 position) {
        this.position = position;
    }

    public void move(Vector2 direction, double speed) {
        direction.normalize();
        position.add(speed * direction.getX(), speed * direction.getY());
        position.checkInBound(maxMove);
    }

    public Circle drawInit(Circle circle, int origX, int origY, int cellSize, int maxMove) {
        this.maxMove = maxMove;
        this.origX = origX;
        this.origY = origY;
        this.cellSize = cellSize;
        this.circle = circle.setRadius(radius).setLineWidth(5);
        updatePosition();
        return this.circle;
    }

    protected void updatePosition() {
        circle.setX(convert(origX, cellSize, position.getX())).setY(convert(origY, cellSize, position.getY()));
    }

    public abstract void draw();

    public int getOwner() {
        return this.owner;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }

    public void switchOwner() {
        owner = owner == 1 ? 2 : 1;
    }

    @Override
    public String toString() {
        return String.format("%.6f %.6f", position.getX(), position.getY());
    }

}