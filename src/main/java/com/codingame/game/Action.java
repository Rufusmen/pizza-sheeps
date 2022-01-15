package com.codingame.game;

public class Action {

    public final boolean isDog;
    public final int id;
    public final Vector2 direction;
    public Player player;
    public final ActionType type;

    public Action(Player player, ActionType type, int id, Vector2 col, boolean isDog) {
        this.player = player;
        this.id = id;
        this.direction = col;
        this.type = type;
        this.isDog = isDog;
    }

    @Override
    public String toString() {
        return id + " " + direction;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Action) {
            Action other = (Action) obj;
            return direction == other.direction && id == other.id;
        } else {
            return false;
        }
    }

    public enum ActionType {
        MOVE,
        SHEAR,
        BARK
    }
}