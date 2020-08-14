package com.kpgn.tictactoe.entity;

public enum Player {
    FIRST('X'),
    SECOND('O');

    public char getPlayerId() {
        return playerId;
    }

    private final char playerId;

    Player(char playerId) {
        this.playerId = playerId;
    }
}