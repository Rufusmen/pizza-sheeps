package com.codingame.game;

import com.codingame.game.actions.AbstractAction;
import com.codingame.game.actions.BarkAction;
import com.codingame.game.actions.InvalidAction;
import com.codingame.game.actions.MoveAction;
import com.codingame.game.actions.ShearAction;
import com.codingame.game.actions.TransferWoolAction;
import com.codingame.game.util.Vector2;
import com.codingame.gameengine.core.AbstractMultiplayerPlayer;
import com.codingame.gameengine.module.entities.Group;
import java.util.ArrayList;
import java.util.List;

/**
 * Player object.
 */
public class Player extends AbstractMultiplayerPlayer {

    public Group hud;

    /**
     * Number of pawns under player control.
     */
    public int pawns;

    @Override
    public int getExpectedOutputLines() {
        return pawns;
    }

    public void setPawns(int pawns) {
        this.pawns = pawns;
    }

    /**
     * Parses player outputs to action objects.
     *
     * @return list of player's actions
     */
    public List<AbstractAction> getActions() throws TimeoutException, NumberFormatException, InvalidAction {
        List<AbstractAction> actions = new ArrayList<>();
        for (String output : getOutputs()
        ) {
            String[] outputs = output.split(" ");
            switch (outputs[0]) {
                case "MOVE":
                    actions.add(new MoveAction(this, Integer.parseInt(outputs[1]),
                        new Vector2(Double.parseDouble(outputs[2]), Double.parseDouble(outputs[3]))));
                    break;
                case "BARK":
                    actions.add(new BarkAction(this, Integer.parseInt(outputs[1])));
                    break;
                case "SHEAR":
                    actions.add(new ShearAction(this, Integer.parseInt(outputs[1]), Integer.parseInt(outputs[2])));
                    break;
                case "TRANSFER_WOOL":
                    actions.add(new TransferWoolAction(this, Integer.parseInt(outputs[2]), Integer.parseInt(outputs[1]) == 1,
                        Integer.parseInt(outputs[3])));
                    break;
                default:
                    throw new InvalidAction("invalid action: " + output);
            }
        }
        return actions;
    }
}
