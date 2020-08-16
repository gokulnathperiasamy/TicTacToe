package com.kpgn.tictactoe.processor;

import com.kpgn.tictactoe.entity.Node;
import com.kpgn.tictactoe.entity.NodesAndScores;
import com.kpgn.tictactoe.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class AIMoveProcessor {

    private static char[][] currentGameBoard;
    private static List<NodesAndScores> nodesAndScores;

    public static String getAIMove(char[][] _currentGameBoard) {
        currentGameBoard = _currentGameBoard;
        callMiniMax(0, Player.SECOND);

        Node aiMoveNode = returnBestMove();
        if (aiMoveNode != null) {
            return String.valueOf(aiMoveNode.indexI) + aiMoveNode.indexJ;
        }

        return null;
    }

    private static List<Node> getListOfAvailableNodes() {
        List<Node> availableNodeList = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (currentGameBoard[i][j] == Player.EMPTY.getPlayerId()) {
                    availableNodeList.add(new Node(i, j));
                }
            }
        }
        return availableNodeList;
    }

    public static void callMiniMax(int depth, Player player) {
        nodesAndScores = new ArrayList<>();
        miniMax(depth, player);
    }

    public static int miniMax(int depth, Player player) {

        if (GameStateProcessor.checkEndState(currentGameBoard, Player.FIRST).currentWinningState != -1) {
            return +1;
        }
        if (GameStateProcessor.checkEndState(currentGameBoard, Player.SECOND).currentWinningState != -1) {
            return -1;
        }

        List<Integer> scores = new ArrayList<>();

        List<Node> availableNodeList = getListOfAvailableNodes();
        if (availableNodeList.isEmpty()) {
            return 0;
        }

        for (int i = 0; i < availableNodeList.size(); ++i) {
            Node node = availableNodeList.get(i);
            if (player == Player.FIRST) {
                currentGameBoard[node.indexI][node.indexJ] = Player.FIRST.getPlayerId();
                int currentScore = miniMax(depth + 1, Player.SECOND);
                scores.add(currentScore);
                nodesAndScores.add(new NodesAndScores(currentScore, node));
            } else if (player == Player.SECOND) {
                currentGameBoard[node.indexI][node.indexJ] = Player.SECOND.getPlayerId();
                scores.add(miniMax(depth + 1, Player.FIRST));
            }
            currentGameBoard[node.indexI][node.indexJ] = Player.EMPTY.getPlayerId();
        }

        return returnMin(scores);
    }

    public static int returnMax(List<Integer> list) {
        int max = Integer.MIN_VALUE;
        int index = -1;
        for (int i = 0; i < list.size(); ++i) {
            if (list.get(i) > max) {
                max = list.get(i);
                index = i;
            }
        }
        return list.get(index);
    }

    public static int returnMin(List<Integer> list) {
        int min = Integer.MAX_VALUE;
        int index = -1;
        for (int i = 0; i < list.size(); ++i) {
            if (list.get(i) < min) {
                min = list.get(i);
                index = i;
            }
        }
        return list.get(index);
    }

    public static Node returnBestMove() {
        int MAX = Integer.MIN_VALUE;
        int best = -1;

        for (int i = 0; i < nodesAndScores.size(); ++i) {
            if (MAX < nodesAndScores.get(i).score) {
                MAX = nodesAndScores.get(i).score;
                best = i;
            }
        }

        if (best != -1) {
            return nodesAndScores.get(best).node;
        } else {
            return null;
        }
    }

    private static int getRandomIndex(int bounds) {
        return ThreadLocalRandom.current().nextInt(0, bounds);
    }
}
