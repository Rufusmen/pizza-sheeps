package com.codingame.game.board;

import com.codingame.gameengine.module.entities.Sprite;

/**
 * Representation of single cell of the board
 */
public class Cell {

    public int owner;
    public Sprite sprite;
    public CellType type;

    public Cell(){
        type = CellType.EMPTY;
    }
}
