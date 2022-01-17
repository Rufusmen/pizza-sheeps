package com.codingame.game.entity;

import com.codingame.game.Vector2;

public class Sheep extends Entity{
    public int wool;
    public boolean isSheared;
    public boolean isScared;

    public Sheep(Vector2 position) {
        super(position);
        isSheared = false;
        isScared = false;
    }

    public void onBark(Vector2 v,double rad){
        isScared = !isSheared && position.inRadius(v, rad);
    }

    @Override
    public void draw() {
        circle.setFillColor(0x0000ff);
        updatePosition();
    }

    @Override
    public String toString(){
        return String.format("%.6f %.6f %d %d",position.getX(),position.getY(),wool,isSheared? 1: 0);
    }

}
