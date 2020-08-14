package com.kpgn.tictactoe.processor;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class MoveProcessor {

    public static String getAIMove(char[][] currentGameBoard) {
        Set<String> visitedIndex = new HashSet<>();

        do {
            int randX = getRandomIndex();
            int randY = getRandomIndex();
            if (currentGameBoard[randX][randY] == '-') {
                return randX + String.valueOf(randY);
            } else {
                visitedIndex.add(randX + String.valueOf(randY));
            }
        } while (visitedIndex.size() < 8);

        return null;
    }

    private static int getRandomIndex() {
        return ThreadLocalRandom.current().nextInt(0, 3);
    }
}
