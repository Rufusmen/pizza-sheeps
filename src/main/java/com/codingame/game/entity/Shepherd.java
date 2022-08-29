package com.codingame.game.entity;

import com.codingame.game.Vector2;

public class Shepherd extends Entity {

    public int shearing = 0;
    public int wool;

    public Shepherd(int id,Vector2 position, int owner) {
        super(id,position);
        this.owner = owner;
    }

    @Override
    public void draw() {
        updatePosition();
    }

    @Override
    public String tooltipTxt() {
        return String.format("id: %d%n position: (%.6f,%.6f)%n wool: %d",id,position.getX(),position.getY(),wool);
    }

    @Override
    protected void setSprite() {
        sprite.setImage(owner == 0 ? "shepard1.png" : "shepard2.png");
    }

    public String toStringFull() {
        return String.format("%d %.6f %.6f %d %d %d" ,id, position.getX(), position.getY(), wool, shearing,owner);
    }
}
