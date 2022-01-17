package com.codingame.game.entity;

import com.codingame.game.Vector2;

public class Dog extends Entity {

    public int barkCoolDown;

    public Dog(Vector2 position,int owner) {
        super(position);
        this.owner=owner;
    }

    @Override
    public void draw() {
        circle.setFillColor(0xff0000);
        updatePosition();
    }

    public void onTurnEnd(){
        barkCoolDown--;
    }
}
