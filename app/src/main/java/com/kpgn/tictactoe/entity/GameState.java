package com.kpgn.tictactoe.entity;

public class GameState {

    public static final int[][] WINNING_STATES = {
            {0, 1, 2},
            {3, 4, 5},
            {6, 7, 8},
            {0, 3, 6},
            {1, 4, 7},
            {2, 5, 8},
            {0, 4, 8},
            {2, 4, 6}
    };

    public static final String LINE_TYPE_VERTICAL = "Vertical";
    public static final String LINE_TYPE_HORIZONTAL = "Horizontal";
    public static final String LINE_TYPE_RIGHT_DIAGONAL = "RightDiagonal";
    public static final String LINE_TYPE_LEFT_DIAGONAL = "LeftDiagonal";

    public int currentWinningState = -1;
    public int[] selectedWinningState = null;
    public String lineType = "";

    public String getLineType(int index) {
        if (index == 0 || index == 1 || index == 2) {
            return LINE_TYPE_HORIZONTAL;
        } else if (index == 3 || index == 4 || index == 5) {
            return LINE_TYPE_VERTICAL;
        } else if (index == 6) {
            return LINE_TYPE_LEFT_DIAGONAL;
        } else if (index == 7) {
            return LINE_TYPE_RIGHT_DIAGONAL;
        }
        return "";
    }
}
