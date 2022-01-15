package com.codingame.game;

import com.codingame.gameengine.module.entities.Rectangle;

public class Cell {

    public int owner;
    public Rectangle rectangle;
    public CellType type;

    public Cell(){
        type = CellType.EMPTY;
    }
}
