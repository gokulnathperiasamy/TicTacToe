package com.kpgn.tictactoe.entity;

public enum Player {
    FIRST('X'),
    SECOND('O'),
    EMPTY('-');

    public char getPlayerId() {
        return playerId;
    }

    private final char playerId;

    Player(char playerId) {
        this.playerId = playerId;
    }

    public static String toString(char playerId) {
        if (playerId == FIRST.getPlayerId()) {
            return "Player";
        } else {
            return "Computer";
        }
    }
}