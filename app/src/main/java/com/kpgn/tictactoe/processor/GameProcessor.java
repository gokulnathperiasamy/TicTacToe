package com.kpgn.tictactoe.processor;

import android.text.TextUtils;

import com.kpgn.tictactoe.entity.GameState;
import com.kpgn.tictactoe.entity.Player;

import java.util.Arrays;

public class GameProcessor {

    public static GameState checkEndState(char[][] currentGameBoard, Player currentPlayer) {
        String[] flattenedGameBoardString = Arrays.stream(currentGameBoard).map(String::new).toArray(String[]::new);
        char[] flattenedGameBoardChar = TextUtils.join("", flattenedGameBoardString).toCharArray();
        GameState gameState = new GameState();

        for (int i = 0; i < 8; i++) {
            if (flattenedGameBoardChar[GameState.WINNING_STATES[i][0]] == currentPlayer.getPlayerId()
                    && flattenedGameBoardChar[GameState.WINNING_STATES[i][1]] == currentPlayer.getPlayerId()
                    && flattenedGameBoardChar[GameState.WINNING_STATES[i][2]] == currentPlayer.getPlayerId()) {
                gameState.currentWinningState = i;
                gameState.selectedWinningState = GameState.WINNING_STATES[i];
                gameState.lineType = gameState.getLineType(i);
                return gameState;
            }
        }

        return gameState;
    }
}
