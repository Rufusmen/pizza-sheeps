package com.codingame.game;

import com.codingame.game.Action.ActionType;
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

    public List<Action> getActions() throws TimeoutException, NumberFormatException {
        List<Action> actions = new ArrayList<>();
        for (String output : getOutputs()
        ) {
            String[] outputs = output.split(" ");
            ActionType type = ActionType.valueOf(outputs[0]);
            actions.add(new Action(this, type, Integer.parseInt(outputs[2]),
                type == ActionType.MOVE ? new Vector2(Double.parseDouble(outputs[3]), Double.parseDouble(outputs[4])) : null,
                type == ActionType.MOVE && (Integer.parseInt(outputs[1]) != 1)));
        }
        return actions;
    }
}
