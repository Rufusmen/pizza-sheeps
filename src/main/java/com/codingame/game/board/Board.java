package com.codingame.game.board;

import static com.codingame.game.util.Util.convert;

import com.codingame.game.entity.Dog;
import com.codingame.game.util.Vector2;
import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.entities.Group;
import com.codingame.gameengine.module.entities.Sprite;
import com.codingame.gameengine.module.tooltip.TooltipModule;
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

    public int getShedSize() {
        return sheds.size();
    }

    /**
     * Generates map.
     * @param sizeX x size of map
     * @param sizeY y size of map
     * @param random random number generator
     */
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

    /**
     * Initializes static graphical entities.
     * @param origX x pixel offset
     * @param origY y pixel offset
     * @param cellSize size off one cell in pixels
     */
    public void drawInit(int origX, int origY, int cellSize) {
        Group entity = graphicEntityModule.createGroup();

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

    /**
     * @param v unit position
     * @return a shed that unit is on or null if unit is in empty cell
     */
    public Shed getShed(Vector2 v) {
        if (cells[(int) v.getX()][(int) v.getY()] instanceof Shed) {
            return (Shed) cells[(int) v.getX()][(int) v.getY()];
        }
        return null;
    }

    /**
     * Updates sheds ownership using dogs' positions.
     * @param dogs list of dogs
     */
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

    /**
     * @return list of inputs for players with sheds information
     */
    public List<String> getShedsInput() {
        List<String> res = new ArrayList<>();
        sheds.forEach(s -> res.add(s.toString()));
        return res;
    }

    /**
     * @param id player id
     * @return score for player with given id
     */
    public int getScore(int id) {
        return sheds.stream().filter(s -> s.owner == id).mapToInt(s -> s.wool).sum();
    }

    /**
     * Updates sheds tooltips.
     * @param tooltips Tooltip object for update
     */
    public void updateTooltip(TooltipModule tooltips) {
        sheds.forEach(s -> tooltips.setTooltipText(s.sprite,s.tooltipTxt()));
    }
}
