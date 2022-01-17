package com.codingame.game.entity;

import com.codingame.game.Vector2;

public class Shepherd extends Entity {

    public int shearing;
    public int wool;

    public Shepherd(Vector2 position, int owner) {
        super(position);
        this.owner = owner;
    }

    @Override
    public void draw() {
        circle.setFillColor(0x00ff00);
        updatePosition();
    }

    public String toStringFull() {
        return String.format("%s %d %d", this, wool, shearing);
    }
}
