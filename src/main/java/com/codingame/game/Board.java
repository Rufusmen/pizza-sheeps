package com.codingame.game;

import static com.codingame.game.Util.convert;

import com.codingame.game.entity.Dog;
import com.codingame.game.entity.Shed;
import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.entities.Group;
import com.codingame.gameengine.module.entities.Sprite;
import com.google.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Board {

    @Inject
    private GraphicEntityModule graphicEntityModule;

    public Cell[][] cells;
    public int sizeX;
    public int sizeY;

    private List<Shed> sheds;

    private Group entity;

    private int origX;
    private int origY;
    private int cellSize;

    public int getShedSize() {
        return sheds.size();
    }

    public void init(int sizeX, int sizeY, Random random) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        cells = new Cell[sizeX][sizeY];
        for (int i = 0; i < sizeX; ++i) {
            for (int j = 0; j < sizeY; ++j) {
                cells[i][j] = new Cell();
            }
        }

        sheds = new ArrayList<>();
        sheds.add(new Shed(random.nextBoolean() ? 0 : 9, 0, 0));
        sheds.add(new Shed(random.nextBoolean() ? 0 : 9, 14, 1));
        if(random.nextBoolean()){
            int x = random.nextInt(10);
            int y = random.nextInt(3)+3;
            sheds.add(new Shed(x, y, -1));
            sheds.add(new Shed(9-x,14-y,-1));
        }

        sheds.forEach(s -> cells[s.x][s.y] = s);
    }

    public void drawInit(int origX, int origY, int cellSize, int lineColor) {
        this.origX = origX;
        this.origY = origY;
        this.cellSize = cellSize;
        entity = graphicEntityModule.createGroup();

        for (int i = 0; i < sizeX; ++i) {
            for (int j = 0; j < sizeY; ++j) {
                Sprite sprite = graphicEntityModule.createSprite()
                    .setY(convert(origX, cellSize, i))
                    .setX(convert(origY, cellSize, j));
                switch (cells[i][j].type) {
                    case EMPTY:
                        sprite.setImage("grass.jpg");
                        break;
                    case SHED:
                        sprite.setImage(cells[i][j].owner == -1 ? "shed.jpg" : cells[i][j].owner == 0 ? "shed1.jpg" : "shed2.jpg");
                        break;
                }
                cells[i][j].sprite = sprite;
                entity.add(sprite);
            }
        }
    }

    public void draw() {
        for (int i = 0; i < sizeX; ++i) {
            for (int j = 0; j < sizeY; ++j) {
                if (cells[i][j].type == CellType.SHED) {
                    //cells[i][j].sprite.setFillColor(0x00ffff);
                }
            }
        }
    }

    public Shed getShed(Vector2 v) {
        if (cells[(int) v.getX()][(int) v.getY()] instanceof Shed) {
            return (Shed) cells[(int) v.getX()][(int) v.getY()];
        }
        return null;
    }

    public void updateSheds(List<Dog> dogs){
        sheds.forEach(Shed::clearDogs);
        dogs.forEach(dog ->{
            Shed s = getShed(dog.getPosition());
            if(s!=null){
                s.addDog(dog.getOwner());
            }
        });
        sheds.forEach(Shed::updateOwnership);
    }

    public List<String> getShedsInput() {
        List<String> res = new ArrayList<>();
        //res.add(Integer.toString(sheds.size()));
        sheds.forEach(s -> res.add(s.toString()));
        return res;
    }

    public int getScore(int id) {
        return sheds.stream().filter(s -> s.owner == id).mapToInt(s -> s.wool).sum();
    }
}
