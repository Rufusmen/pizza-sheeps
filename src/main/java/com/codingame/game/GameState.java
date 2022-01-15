package com.codingame.game;

import com.codingame.game.entity.Entity;
import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.google.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class GameState {

    @Inject
    private Board board;

    @Inject
    private GraphicEntityModule graphicEntityModule;
    private List<Entity> pawns = new ArrayList<>();


    public void drawInit(int i, int i1, int bigCellSize, int i2, int i3) {
        board.drawInit(i,i1,bigCellSize,i2);
        
    }

    public void init() {
        board.init();
    }

    public void draw() {
        board.draw();
    }

    public void resolveActions(List<Action> actions) {
    }
}
