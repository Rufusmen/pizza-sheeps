package com.codingame.game;

import static com.codingame.game.Util.convert;

import com.codingame.game.entity.Shed;
import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.entities.Group;
import com.codingame.gameengine.module.entities.Rectangle;
import com.google.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class Board {

    @Inject
    private GraphicEntityModule graphicEntityModule;

    public Cell[][] cells;
    public int size;

    private List<Shed> sheds;

    private Group entity;

    private int origX;
    private int origY;
    private int cellSize;


    public void init(int size){
        this.size=size;
        cells = new Cell[size][size];
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                cells[i][j] = new Cell();
            }
        }

        sheds = new ArrayList<>();
        sheds.add(new Shed(0,0));
        sheds.add(new Shed(9,9));
        sheds.forEach(s->cells[s.x][s.y]=s);
    }

    public void drawInit(int origX, int origY, int cellSize, int lineColor) {
        this.origX = origX;
        this.origY = origY;
        this.cellSize = cellSize;
        entity = graphicEntityModule.createGroup();

        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                Rectangle rectangle = graphicEntityModule.createRectangle()
                    .setY(convert(origX, cellSize, i))
                    .setX(convert(origY, cellSize, j))
                    .setWidth(cellSize)
                    .setHeight(cellSize)
                    .setLineWidth(5)
                    .setLineColor(lineColor);
                switch (cells[i][j].type) {
                    case EMPTY -> rectangle.setFillColor(0xffffff);
                    case WALL -> rectangle.setFillColor(0xffff);
                    case SHED -> rectangle.setFillColor(0x00ffff);
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

    public Shed getShed(Vector2 v){
        if(cells[(int) v.getX()][(int) v.getY()] instanceof Shed){
            return (Shed) cells[(int) v.getX()][(int) v.getY()];
        }
        return null;
    }

    public List<String> getShedsInput() {
        List<String> res = new ArrayList<>();
        res.add(Integer.toString(sheds.size()));
        sheds.forEach(s -> res.add(s.toString()));
        return res;
    }
}
