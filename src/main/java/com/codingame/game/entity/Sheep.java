package com.codingame.game.entity;

import com.codingame.game.Vector2;

public class Sheep extends Entity {

    public int wool;
    public boolean isSheared;
    public boolean isScared;

    public Sheep(Vector2 position, int wool) {
        super(position);
        this.wool = wool;
        isSheared = false;
        isScared = false;
    }

    public void decrWool(){
        wool--;
        if(wool==0){
            sprite.setImage("sheepBald.png").setBaseWidth(27).setBaseHeight(49).setAnchor(0.5);
        }
    }

    public void onBark(Vector2 v, double rad) {
        isScared = !isSheared && position.inRadius(v, rad);
    }

    @Override
    protected void setSprite() {
        sprite.setImage("sheep.png").setBaseWidth(27).setBaseHeight(49).setAnchor(0.5);
    }

    @Override
    public void draw() {
        updatePosition();
    }

    @Override
    public String toString() {
        return String.format("%.6f %.6f %d %d", position.getX(), position.getY(), wool, isSheared ? 1 : 0);
    }

}
