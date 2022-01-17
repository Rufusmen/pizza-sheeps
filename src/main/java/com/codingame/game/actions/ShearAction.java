package com.codingame.game.actions;

import com.codingame.game.Player;

public class ShearAction extends AbstractAction{

    public int sheepId;
    public boolean isStart;

    public ShearAction(Player player, int id, int sheepId, boolean isStart) {
        super(player, id);
        this.sheepId = sheepId;
        this.isStart = isStart;
    }
}
