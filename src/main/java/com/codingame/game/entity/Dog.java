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
        updatePosition();
    }

    @Override
    protected void setSprite() {
        sprite.setImage(owner == 0 ? "dog1.png" : "dog2.png");
    }

    public void onTurnEnd(){
        barkCoolDown--;
    }
}
