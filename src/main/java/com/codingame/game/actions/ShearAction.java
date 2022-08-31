package com.codingame.game.actions;

import com.codingame.game.Player;

/**
 * SHEAR action object.
 */
public class ShearAction extends AbstractAction{

    public int sheepId;

    public ShearAction(Player player, int id, int sheepId) {
        super(player, id);
        this.sheepId = sheepId;
    }
}
