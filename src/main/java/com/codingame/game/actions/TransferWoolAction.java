package com.codingame.game.actions;

import com.codingame.game.Player;

public class TransferWoolAction extends AbstractAction {

    public boolean isDeposit;
    public int amount;

    public TransferWoolAction(Player player, int id, boolean isDeposit, int amount) {
        super(player, id);
        this.isDeposit = isDeposit;
        this.amount = amount;
    }
}
