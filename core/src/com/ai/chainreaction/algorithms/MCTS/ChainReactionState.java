package com.ai.chainreaction.algorithms.MCTS;

import com.ai.chainreaction.Tile;
import com.ai.chainreaction.Utilities;

import java.awt.Color;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Pranav on 01-12-2015.
 */
public class ChainReactionState implements MctsDomainState<Utilities.Pos, ChainReactionPlayer> {

//        private static final int BOARD_SIZE = 3;
//        private static final char EMPTY_BOARD_POSITION = '-';
//        private static final int ACTION_ROW_POSITION = 0;
//        private static final int ACTION_COLUMN_POSITION = 1;
//        private static final int FINAL_ROUND = 9;

    private int[][] board;
    private int[][] prevBoard;
    private ChainReactionPlayer[] players;
    private int currentPlayerIndex;
    private int previousPlayerIndex;
    private int currentRound;

//    public static ChainReactionState initialize(int playerToBegin) {
//        int[][] board = initializeEmptyBoard();
//        ChainReactionPlayer[] players = initializePlayers();
//        int currentPlayerIndex = getPlayerToBeginIndex(playerToBegin);
//        return new ChainReactionState(board, players, currentPlayerIndex);
//    }

    public ChainReactionState(int[][] board, int currentPlayerIndex) {
        this.board = board;
        this.players = initializePlayers();
        this.currentPlayerIndex = currentPlayerIndex;
        this.previousPlayerIndex = 2 - currentPlayerIndex - 1;
        this.currentRound = 0;
    }

//    private static int[][] initializeEmptyBoard() {
//        char[][] board = new char[board.length][board[0].length];
//        for (int row = 0; row < BOARD_SIZE; row++) {
//            Arrays.fill(board[row], EMPTY_BOARD_POSITION);
//        }
//        return board;
//    }

    private static ChainReactionPlayer[] initializePlayers() {
        ChainReactionPlayer[] players = new ChainReactionPlayer[2];
        players[0] = new ChainReactionPlayer(Tile.RED);
        players[1] = new ChainReactionPlayer(Tile.BLUE);
        return players;
    }

    private static int getPlayerToBeginIndex(int playerToBegin) {
        switch (playerToBegin) {
            case Tile.RED:
                return 0;
            case Tile.BLUE:
                return 1;
            default:
                throw new IllegalArgumentException("Error: invalid player type passed as function parameter");
        }
    }

    protected void setBoard(int[][] board) {
        this.board = board;
    }

    protected int[][] getBoard() {
        return board;
    }

    protected void setCurrentRound(int round) {
        this.currentRound = round;
    }

    @Override
    public boolean isTerminal() {
        return Utilities.checkWinnerIfExists(board)!=0;
    }

//    public boolean isDraw() {
//        return !somePlayerWon() && currentRound == FINAL_ROUND;
//    }

//    private boolean somePlayerWon() {
//        return specificPlayerWon(players[currentPlayerIndex])
//                || specificPlayerWon(players[previousPlayerIndex]);
//    }
//
    protected boolean specificPlayerWon(ChainReactionPlayer player) {
        return player.getTurn()==Utilities.checkWinnerIfExists(board);
    }

//    private boolean boardContainsPlayersFullRow(ChainReactionPlayer player) {
//        for (int row = 0; row < BOARD_SIZE; row++) {
//            if (board[row][0] == player.getBoardPositionMarker()
//                    && board[row][1] == player.getBoardPositionMarker()
//                    && board[row][2] == player.getBoardPositionMarker())
//                return true;
//        }
//        return false;
//    }
//
//    private boolean boardContainsPlayersFullColumn(ChainReactionPlayer player) {
//        for (int column = 0; column < BOARD_SIZE; column++) {
//            if (board[0][column] == player.getBoardPositionMarker()
//                    && board[1][column] == player.getBoardPositionMarker()
//                    && board[2][column] == player.getBoardPositionMarker())
//                return true;
//        }
//        return false;
//    }
//
//    private boolean boardContainsPlayersFullDiagonal(ChainReactionPlayer player) {
//        return boardContainsPlayersFullAscendingDiagonal(player)
//                || boardContainsPlayersFullDescendingDiagonal(player);
//    }
//
//    private boolean boardContainsPlayersFullAscendingDiagonal(ChainReactionPlayer player) {
//        for (int i = 0; i < BOARD_SIZE; i++) {
//            if (board[i][BOARD_SIZE - 1 - i] != player.getBoardPositionMarker())
//                return false;
//        }
//        return true;
//    }
//
//    private boolean boardContainsPlayersFullDescendingDiagonal(ChainReactionPlayer player) {
//        for (int i = 0; i < BOARD_SIZE; i++) {
//            if (board[i][i] != player.getBoardPositionMarker())
//                return false;
//        }
//        return true;
//    }

    @Override
    public ChainReactionPlayer getCurrentAgent() {
        return players[currentPlayerIndex];
    }

    @Override
    public ChainReactionPlayer getPreviousAgent() {
        return players[previousPlayerIndex];
    }

    @Override
    public int getNumberOfAvailableActionsForCurrentAgent() {
        return getAvailableActionsForCurrentAgent().size();
    }

    @Override
    public List<Utilities.Pos> getAvailableActionsForCurrentAgent() {
//        List<Pos> availableActions = new ArrayList<>();
//        for (int row = 0; row < board; row++) {
//            List<String> availableActionsInRow = getAvailableActionsInBoardRow(board[row], row);
//            availableActions.addAll(availableActionsInRow);
//        }
//        return availableActions;
        return Utilities.getPlayablePositions(board, players[currentPlayerIndex].getTurn());
    }

//    private List<String> getAvailableActionsInBoardRow(char[] row, int rowIndex) {
//        List<String> availableActionsInRow = new ArrayList<>();
//        for (int columnIndex = 0; columnIndex < BOARD_SIZE; columnIndex++) {
//            if (row[columnIndex] == EMPTY_BOARD_POSITION) {
//                String action = generateActionFromBoardPosition(rowIndex, columnIndex);
//                availableActionsInRow.add(action);
//            }
//        }
//        return availableActionsInRow;
//    }

//    private String generateActionFromBoardPosition(int row, int column) {
//        return Integer.toString(row) + Integer.toString(column);
//    }

    @Override
    public MctsDomainState performActionForCurrentAgent(Utilities.Pos action) {
        validateIsValidAction(action);
        applyActionOnBoard(action);
        selectNextPlayer();
        currentRound++;
        return this;
    }

    private void validateIsValidAction(Utilities.Pos action) {
        if (!getAvailableActionsForCurrentAgent().contains(action)) {
            throw new IllegalArgumentException("Error: invalid action passed as function parameter");
        }
    }

    private void applyActionOnBoard(Utilities.Pos action) {
        int row = action.row;
        int column = action.col;
        prevBoard = Utilities.copyGrid(board);
//        board[row][column] = players[currentPlayerIndex].getBoardPositionMarker();
        Utilities.clickTile(board, players[currentPlayerIndex].getTurn(), row, column);
    }

    protected MctsDomainState undoAction(Utilities.Pos action) {
//        validateIsValidUndoAction(action);
        applyUndoActionOnBoard();
        selectNextPlayer();
        currentRound--;
        return this;
    }

//    private void validateIsValidUndoAction(String action) {
//        int row = getRowFromAction(action);
//        int column = getColumnFromAction(action);
//        if (!(-1 < row && row < 3) && !(-1 < column && column < 3))
//            throw new IllegalArgumentException("Error: invalid action passed as function parameter");
//    }

    private void applyUndoActionOnBoard() {
       Utilities.revertGrid(prevBoard,board);
    }

//    private int getRowFromAction(String action) {
//        String row = action.split("")[ACTION_ROW_POSITION];
//        return Integer.parseInt(row);
//    }
//
//    private int getColumnFromAction(String action) {
//        String column = action.split("")[ACTION_COLUMN_POSITION];
//        return Integer.parseInt(column);
//    }

    private void selectNextPlayer() {
        currentPlayerIndex = 2 - currentPlayerIndex - 1;
        previousPlayerIndex = 2 - previousPlayerIndex - 1;
    }

    @Override
    public MctsDomainState skipCurrentAgent() {
        return this;
    }
}
