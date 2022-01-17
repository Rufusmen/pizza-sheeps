package com.codingame.game;

public class Shed extends Cell {

    public int owner;
    public int wool;
    public int dogs1;
    public int dogs2;

    public int x;
    public int y;

    public Shed(int x, int y) {
        this.x = x;
        this.y = y;
        type = CellType.SHED;
    }

    @Override
    public String toString(){
        return String.format("%d %d %d %d %d %d",x,y,owner,wool,dogs1,dogs2);
    }

}
