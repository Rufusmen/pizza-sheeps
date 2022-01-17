package com.codingame.game.actions;

import com.codingame.game.Player;

public abstract class AbstractAction {

    public final int id;
    public Player player;

    public AbstractAction(Player player,  int id) {
        this.player = player;
        this.id = id;
    }


}