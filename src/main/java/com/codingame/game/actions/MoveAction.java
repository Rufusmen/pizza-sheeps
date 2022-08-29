package com.codingame.game.actions;

import com.codingame.game.Player;
import com.codingame.game.Vector2;

public class MoveAction extends AbstractAction {

    public Vector2 direction;

    public MoveAction(Player player, int id, Vector2 direction) {
        super(player, id);
        this.direction = direction;
    }
}
