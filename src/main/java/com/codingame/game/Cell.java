package com.codingame.game;

import com.codingame.gameengine.module.entities.Rectangle;
import com.codingame.gameengine.module.entities.Sprite;

public class Cell {

    public int owner;
    public Sprite sprite;
    public CellType type;

    public Cell(){
        type = CellType.EMPTY;
    }
}
