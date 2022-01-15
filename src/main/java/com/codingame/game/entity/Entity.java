package com.codingame.game.entity;

import com.codingame.game.Vector2;
import com.codingame.gameengine.module.entities.Rectangle;

public class Entity {

    private static final double TO_RAD = 180.0/Math.PI;
    private static final double TO_ANG = Math.PI/180.0;

    protected int owner;
    protected Type type;
    private Vector2 position;

    public Entity(int owner, Type type) {
        this.owner = owner;
        this.type = type;
    }



    public Entity() {
        owner = 0;
    }

    public void move(Vector2 direction, double speed){
        double angle = Math.atan(direction.getY()/direction.getX());
        position.add(speed*Math.cos(angle),speed*Math.sin(angle));
    }

    public int getOwner() { return this.owner; }
    public void setOwner(int owner) { this.owner = owner; }
    public void switchOwner(){owner = owner == 1 ? 2 : 1;}
    public enum Type{
        SHEEP,
        PLAYER,
        DOG
    }


}