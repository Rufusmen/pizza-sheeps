package com.codingame.game.entity;

import com.codingame.game.util.Vector2;

/**
 * Representation of sheep unit.
 */
public class Sheep extends Entity {

    public int wool;
    public int shearedBy;
    public boolean isScared;

    public Sheep(int id,Vector2 position, int wool) {
        super(id,position);
        this.wool = wool;
        shearedBy = 0;
        isScared = false;
    }

    public void decrWool(){
        wool--;
        if(wool==0){
            sprite.setImage("sheepBald.png").setBaseWidth(27).setBaseHeight(49).setAnchor(0.5);
        }
    }

    public void onBark(Vector2 v, double rad) {
        isScared = shearedBy ==0 && position.inRadius(v, rad);
    }

    @Override
    protected void setSprite() {
        sprite.setImage("sheep.png").setBaseWidth(27).setBaseHeight(49).setAnchor(0.5);
    }

    @Override
    public String tooltipTxt() {
        return String.format("id: %d%n position: (%.6f,%.6f)%n wool: %d",id,position.getX(),position.getY(),wool);
    }

    @Override
    public void draw() {
        updatePosition();
    }

    @Override
    public String toString() {
        return String.format("%d %.6f %.6f %d %d",id, position.getX(), position.getY(), wool, shearedBy !=0 ? 1 : 0);
    }

}
