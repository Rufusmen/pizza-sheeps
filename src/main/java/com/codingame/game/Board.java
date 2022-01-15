package com.codingame.game;

import static com.codingame.game.Util.convert;

import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.entities.Group;
import com.codingame.gameengine.module.entities.Rectangle;
import com.google.inject.Inject;

public class Board {

    @Inject
    private GraphicEntityModule graphicEntityModule;

    public Cell[][] cells;
    public int size;

    private int origX;
    private int origY;
    private int cellSize;


    public void init(){
        size=10;
        cells = new Cell[size][size];
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                cells[i][j] = new Cell();
            }
        }

        cells[0][0] = new Shed();
        cells[9][9] = new Shed();
    }

    public void drawInit(int origX, int origY, int cellSize, int lineColor) {
        this.origX = origX;
        this.origY = origY;
        this.cellSize = cellSize;
        Group entity = graphicEntityModule.createGroup();

        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                Rectangle rectangle = graphicEntityModule.createRectangle()
                    .setY(convert(origX, cellSize, i))
                    .setX(convert(origY, cellSize, j))
                    .setWidth(cellSize)
                    .setHeight(cellSize)
                    .setLineWidth(5)
                    .setLineColor(lineColor);
                switch (cells[i][j].type){
                    case EMPTY:
                        rectangle.setFillColor(0xffffff);
                        break;
                    case WALL:
                        rectangle.setFillColor(0xffff);
                        break;
                    case SHED:
                        rectangle.setFillColor(0x00ffff);
                        break;
                }
                cells[i][j].rectangle = rectangle;
                entity.add(rectangle);
            }
        }
    }

    public void draw()
    {
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                if (cells[i][j].type == CellType.SHED) {
                    cells[i][j].rectangle.setFillColor(0x00ffff);
                }
            }
        }
    }

}
