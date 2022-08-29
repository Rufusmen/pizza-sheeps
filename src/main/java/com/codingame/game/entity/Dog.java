package com.codingame.game.entity;

import com.codingame.game.Vector2;

public class Dog extends Entity {

    public int barkCoolDown;

    public Dog(int id,Vector2 position,int owner) {
        super(id,position);
        this.owner=owner;
    }

    @Override
    public void draw() {
        updatePosition();
    }

    @Override
    public String tooltipTxt() {
        return String.format("id: %d%n position: (%.6f,%.6f)",id,position.getX(),position.getY());
    }

    @Override
    protected void setSprite() {
        sprite.setImage(owner == 0 ? "dog1.png" : "dog2.png");
    }

    public void onTurnEnd(){
        barkCoolDown--;
    }
}
