package com.codingame.game.entity;

import static com.codingame.game.Util.convert;

import com.codingame.game.Vector2;
import com.codingame.gameengine.module.entities.Circle;
import com.codingame.gameengine.module.entities.Mask;
import com.codingame.gameengine.module.entities.Sprite;

public abstract class Entity {

    private static final double TO_RAD = 180.0 / Math.PI;
    private static final double TO_ANG = Math.PI / 180.0;
    public int id;
    protected int owner = 0;

    public Vector2 getPosition() {
        return position;
    }

    protected Vector2 position;
    private final int radius = 50;

    public Sprite getSprite() {
        return sprite;
    }

    protected Sprite sprite;
    private int cellSize;
    private int origX;
    private int origY;
    private double maxMoveX;
    private double maxMoveY;
    public Vector2 lastMove = new Vector2(0,0);


    public Entity(int id,Vector2 position) {
        this.id= id;
        this.position = position;
    }

    public void move(Vector2 direction, double speed) {
        direction.normalize();
        position.add(speed * direction.getX(), speed * direction.getY());
        lastMove = direction;
        position.checkInBound(maxMoveX,maxMoveY);
    }

    public Sprite drawInit(Sprite sprite, int origX, int origY, int cellSize, int maxMoveX,int maxMoveY) {
        this.sprite = sprite;
        setSprite();
        this.maxMoveX = maxMoveX;
        this.maxMoveY = maxMoveY;
        this.origX = origX;
        this.origY = origY;
        this.cellSize = cellSize;
        updatePosition();
        return this.sprite;
    }

    protected void updatePosition() {
        sprite.setY(convert(origX, cellSize, position.getX())).setX(convert(origY, cellSize, position.getY()));
        sprite.setRotation(Math.atan2(lastMove.getX(), lastMove.getY())+Math.PI/2);
    }

    public abstract void draw();

    protected abstract void setSprite();

    public abstract String tooltipTxt();

    public int getOwner() {
        return this.owner;
    }


    @Override
    public String toString() {
        return String.format("%d %.6f %.6f %d",id, position.getX(), position.getY(),owner);
    }

}