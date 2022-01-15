package com.codingame.game;

public class Shed extends Cell{
    public int owner;
    public int wool;
    public int dogs1;
    public int dogs2;

    public Shed(){
        type = CellType.SHED;
    }

}
