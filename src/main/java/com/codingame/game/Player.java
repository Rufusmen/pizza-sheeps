package com.codingame.game;

import com.codingame.game.actions.AbstractAction;
import com.codingame.game.actions.BarkAction;
import com.codingame.game.actions.MoveAction;
import com.codingame.game.actions.ShearAction;
import com.codingame.game.actions.TransferWoolAction;
import com.codingame.gameengine.core.AbstractMultiplayerPlayer;
import com.codingame.gameengine.module.entities.Group;
import java.util.ArrayList;
import java.util.List;

public class Player extends AbstractMultiplayerPlayer {

    public Group hud;
    public int pawns;

    @Override
    public int getExpectedOutputLines() {
        return pawns;
    }

    public void setPawns(int pawns) {
        this.pawns = pawns;
    }

    public List<AbstractAction> getActions() throws TimeoutException, NumberFormatException, InvalidAction {
        List<AbstractAction> actions = new ArrayList<>();
        for (String output : getOutputs()
        ) {
            String[] outputs = output.split(" ");
            switch (outputs[0]) {
                case "MOVE":
                    actions.add(new MoveAction(this, Integer.parseInt(outputs[2]),
                        new Vector2(Double.parseDouble(outputs[3]), Double.parseDouble(outputs[4])), Integer.parseInt(outputs[1]) != 1));
                    break;
                case "BARK":
                    actions.add(new BarkAction(this, Integer.parseInt(outputs[1])));
                    break;
                case "SHEAR":
                    actions.add(new ShearAction(this, Integer.parseInt(outputs[2]), Integer.parseInt(outputs[3]),
                        Integer.parseInt(outputs[1]) == 1));
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
