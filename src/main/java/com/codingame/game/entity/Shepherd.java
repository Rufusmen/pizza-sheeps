package com.codingame.game.entity;

import com.codingame.game.Vector2;

public class Shepherd extends Entity {

    public int shearing = -1;
    public int wool;

    public Shepherd(Vector2 position, int owner) {
        super(position);
        this.owner = owner;
    }

    @Override
    public void draw() {
        updatePosition();
    }

    @Override
    protected void setSprite() {
        sprite.setImage(owner == 0 ? "shepard1.png" : "shepard2.png");
    }

    public String toStringFull() {
        return String.format("%.6f %.6f %d %d %d" , position.getX(), position.getY(), wool, shearing + 1,owner);
    }
}
