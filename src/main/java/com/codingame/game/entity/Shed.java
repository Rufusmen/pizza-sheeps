package com.codingame.game.entity;

import com.codingame.game.Cell;
import com.codingame.game.CellType;

public class Shed extends Cell {

    public int wool;
    public int dogs1;
    public int dogs2;

    public int x;
    public int y;

    public Shed(int x, int y,int owner) {
        this.x = x;
        this.y = y;
        this.owner = owner;
        type = CellType.SHED;
    }

    @Override
    public String toString(){
        return String.format("%d %d %d %d %d %d",x,y,owner,wool,dogs1,dogs2);
    }

}
