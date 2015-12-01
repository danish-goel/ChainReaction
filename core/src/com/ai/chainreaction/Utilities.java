package com.ai.chainreaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * Created by Manan on 18-11-2015.
 */
public class Utilities {

    public static class Pos {
        public Pos(int row, int col) {
            this.row = row;
            this.col = col;
        }

        public int row;
        public int col;
    }

    public static List<Pos> getPlayablePositions(Tile[][] grid, int turn) {
        List<Pos> listPlayablePositions = new ArrayList<Pos>();
        int rows = grid.length;
        int cols = grid[0].length;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Tile tile = grid[i][j];
                if (tile.color == turn || tile.color == Tile.EMPTY)
                    listPlayablePositions.add(new Pos(i, j));
            }
        }


        return listPlayablePositions;
    }

    public static List<Pos> getPlayablePositions(int[][] grid, int turn) {
        List<Pos> listPlayablePositions = new ArrayList<Pos>();
        int rows = grid.length;
        int cols = grid[0].length;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int tile = grid[i][j];
                int tileTurn = 0;
                if (tile != 0)
                    tileTurn = tile / Math.abs(tile);
                if (tileTurn == turn || tile == Tile.EMPTY)
                    listPlayablePositions.add(new Pos(i, j));
            }
        }
        return listPlayablePositions;
    }

    public static int getOrbCount(int[][] grid, int turn, boolean includeAndSubtractEnemy, boolean useTileCountNotOrbCount) {
        int score = 0;
        int rows = grid.length;
        int cols = grid[0].length;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int color = (int) Math.signum(grid[i][j]);
                int numOrbs = Math.abs(grid[i][j]);
                if (color == turn)
                {
                    if (useTileCountNotOrbCount) {
                        score++;
                    } else {
                        score += numOrbs;
                    }
                }
                else if(color == -turn && includeAndSubtractEnemy) {
                    if (useTileCountNotOrbCount) {
                        score--;
                    } else {
                        score -= numOrbs;
                    }
                }
            }
        }
        return score;
    }

    public static Map<Pos, Integer> getChains(int[][] grid, int color) {
        return getChains(grid, color, false);
    }

    public static Map<Pos, Integer> getChains(int[][] grid, int color, boolean includeOpponentInChain) {
        Map<Pos, Integer> chains = new HashMap<Pos, Integer>();
        //init vars
        int numRows = grid.length;
        int numColumns = grid[0].length;
        boolean visited[][] = new boolean[numRows][numColumns];
        //init visited
        for (int i = 0; i < numRows; i++)
            for (int j = 0; j < numColumns; j++)
                visited[i][j] = false;

        // main code
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numColumns; col++) {
                //for each tile
                if (visited[row][col])
                    continue;
                int tile = grid[row][col];
                int tileColor = (int) Math.signum(tile);
                int tileThreshold = Tile.getThreshold(numRows, numColumns, row, col);
                int tileNumOrbs = Math.abs(tile);
                //if my cell is unstable - need to count this chain
                if (tileNumOrbs == tileThreshold - 1 && tileColor == color) {
                    //init dfs
                    int count = 0;
                    Stack<Pos> stack = new Stack();
                    stack.add(new Pos(row, col));
                    //dfs loop
                    while (!stack.empty()) {
                        Pos pos = stack.pop();
                        count++;
                        visited[pos.row][pos.col] = true;
                        //iterating over neighbors
                        for (Pos eachPos : Tile.get4Neighbours(numRows, numColumns, pos.row, pos.col)) {
                            //visited
                            if (visited[eachPos.row][eachPos.col])
                                continue;
                            visited[eachPos.row][eachPos.col] = true;

                            //add tile if unstable
                            tile = grid[eachPos.row][eachPos.col];
                            tileColor = (int) Math.signum(tile);
                            tileThreshold = Tile.getThreshold(numRows, numColumns, eachPos.row, eachPos.col);
                            tileNumOrbs = Math.abs(tile);
                            if (tileNumOrbs == tileThreshold - 1 && (tileColor == color || includeOpponentInChain)) {
                                stack.add(eachPos);
                            }
                        }
                    }
                    chains.put(new Pos(row, col), count);
                }
            }
        }
        return chains;
    }

    public static int[][] initalizeGrid(Tile tiles[][]) {
        int grid[][] = new int[tiles.length][tiles[0].length];
        for (int row = 0; row < tiles.length; row++) {
            for (int col = 0; col < tiles[0].length; col++) {
                grid[row][col] = tiles[row][col].getColor() * tiles[row][col].getNumOrbs();
            }
        }
        return grid;
    }



}
