package com.codingame.game.actions;

public class InvalidAction extends Exception {
    private static final long serialVersionUID = -8185589153224401564L;

    public InvalidAction(String message) {
        super(message);
    }

}
